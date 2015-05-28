/* $Id: a5f5f5565fb0c75c612035f5f5d6bceaef1f171c $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/MachineUtil.java $
 * 
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
 * 
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * 
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.stage;

import io.coala.factory.ClassUtil;
import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;
import io.coala.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.inject.Provider;

import org.apache.log4j.Logger;

import rx.Observer;

/**
 * {@link StageUtil} utility class for interception and extension of an injected
 * object's life cycle
 * 
 * @version $Id: a5f5f5565fb0c75c612035f5f5d6bceaef1f171c $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class StageUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(StageUtil.class);

	/** */
	private static final Map<Class<?>, Map<StageEvent, SortedMap<Method, Staged>>> stageEventHandlerCache = new HashMap<>();

	/** */
	private static final Map<Class<?>, Map<String, SortedMap<Method, Staged>>> customStageHandlerCache = new HashMap<>();

	/** */
	private static final Map<Class<?>, Set<Class<? extends Throwable>>> absorptionLevelCache = new HashMap<>();

	/**
	 * {@link StageUtil} singleton constructor
	 */
	private StageUtil()
	{
		// utility class should not provide protected/public instances
	}

	/** decide which stage event handler to execute first */
	private static final Comparator<Method> METHOD_COMPARATOR = new Comparator<Method>()
	{

		@Override
		public int compare(final Method o1, final Method o2)
		{
			if (o1 == o2)
				return 0;

			// compare priority
			int result = Integer.compare(o1.getAnnotation(Staged.class)
					.priority(), o2.getAnnotation(Staged.class).priority());
			if (result != 0)
				return result;

			// same priority; compare class hierarchy
/*			result = o1.getDeclaringClass().getName()
					.compareTo(o2.getDeclaringClass().getName());
			if (result != 0)
				return result;
*/

			// same priority and class; compare method name
			result = o1.getName().compareTo(o2.getName());
			if (result != 0)
				return result;

			// same priority, class, and name; compare method name
			return toSignatureString(o1).compareTo(toSignatureString(o2));
		}
	};

	/**
	 * @param type
	 */
	private static synchronized void findHandlers(final Class<?> type)
	{
		LOG.trace("Caching handlers for " + type.getName());
		final Map<StageEvent, SortedMap<Method, Staged>> handlersByEvent = type
				.getSuperclass() == Object.class
				|| type.getSuperclass() == null ? new EnumMap<StageEvent, SortedMap<Method, Staged>>(
				StageEvent.class) : findEventHandlers(type.getSuperclass());
		stageEventHandlerCache.put(type, handlersByEvent);
		final Map<String, SortedMap<Method, Staged>> handlersByStage = type
				.getSuperclass() == Object.class
				|| type.getSuperclass() == null ? new HashMap<String, SortedMap<Method, Staged>>()
				: findStageHandlers(type.getSuperclass());
		customStageHandlerCache.put(type, handlersByStage);

		for (Class<?> iface : type.getInterfaces())
		{
			handlersByEvent.putAll(findEventHandlers(iface));
			handlersByStage.putAll(findStageHandlers(iface));
		}

		for (Method method : type.getDeclaredMethods())
		{
			final Staged ann = method.getAnnotation(Staged.class);
			if (ann == null)
				continue;

			for (StageEvent on : ann.on())
			{
				SortedMap<Method, Staged> handlers = handlersByEvent.get(on);
				if (handlers == null)
				{
					handlers = new TreeMap<>(METHOD_COMPARATOR);
					handlersByEvent.put(on, handlers);
				}
				handlers.put(method, ann);
			}

			for (String stage : ann.onCustom())
			{
				SortedMap<Method, Staged> handlers = handlersByStage.get(stage);
				if (handlers == null)
				{
					handlers = new TreeMap<>(METHOD_COMPARATOR);
					handlersByStage.put(stage, handlers);
				}
				handlers.put(method, ann);
			}
		}
		// if (handlersByEvent.isEmpty() && handlersByStage.isEmpty())
		// LOG.warn("No @" + Staged.class.getSimpleName()
		// + " annotations found in target: " + type.getName());
	}

	/**
	 * @param type
	 * @param event
	 * @return
	 */
	public static synchronized Map<StageEvent, SortedMap<Method, Staged>> findEventHandlers(
			final Class<?> type)
	{
		Map<StageEvent, SortedMap<Method, Staged>> result = stageEventHandlerCache
				.get(type);

		if (result == null)
		{
			findHandlers(type);
			result = stageEventHandlerCache.get(type);
		}
		return result;
	}

	/**
	 * @param type
	 * @param event
	 * @return
	 */
	public static Map<Method, Staged> findEventHandlers(final Class<?> type,
			final StageEvent event)
	{
		final Map<Method, Staged> result = findEventHandlers(type).get(event);
		if (result == null)
			return Collections.emptyMap();
		return result;
	}

	/**
	 * @param type
	 * @param stage
	 * @return
	 */
	public static synchronized Map<String, SortedMap<Method, Staged>> findStageHandlers(
			final Class<?> type)
	{
		Map<String, SortedMap<Method, Staged>> result = customStageHandlerCache
				.get(type);

		if (result == null)
		{
			findHandlers(type);
			result = customStageHandlerCache.get(type);
		}
		return result;
	}

	/**
	 * @param type
	 * @param stage
	 * @return
	 */
	public static Map<Method, Staged> findStageHandlers(final Class<?> type,
			final String stage)
	{
		final Map<Method, Staged> result = findStageHandlers(type).get(stage);
		if (result == null)
			return Collections.emptyMap();
		return result;
	}

	/**
	 * @param type
	 * @return
	 */
	public synchronized static SortedSet<String> findStages(
			final Class<?> type, final InjectStaged.StageSelector selector)
	{
		LOG.trace("Caching " + selector + " stages for " + type.getName());
		SortedSet<String> result = selector.getCache().get(type);
		if (result != null)
			return result;

		result = new TreeSet<>();
		selector.getCache().put(type, result);

		InjectStaged staging = type.getAnnotation(InjectStaged.class);
		if (staging != null)
			for (String stage : selector.selectStages(staging))
				result.add(stage);

		for (Class<?> iface : type.getInterfaces())
			result.addAll(findStages(iface, selector));

		if (type.getSuperclass() != Object.class
				&& type.getSuperclass() != null)
			result.addAll(findStages(type.getSuperclass(), selector));

		return result;
	}

	/**
	 * use sub-most available specification (if any) from Objects only (ignore
	 * interfaces and possibly overridden specifications)
	 * 
	 * @param type
	 * @return
	 */
	public synchronized static Set<Class<? extends Throwable>> findAbsorptionLevels(
			final Class<?> type)
	{
		LOG.trace("Caching absorption levels for " + type.getName());
		Set<Class<? extends Throwable>> result = absorptionLevelCache.get(type);
		if (result != null)
			return result;

		absorptionLevelCache.put(type, result);

		Class<?> stagingType = type;
		InjectStaged staging = stagingType.getAnnotation(InjectStaged.class);
		while (staging == null && stagingType.getSuperclass() != Object.class)
		{
			stagingType = stagingType.getSuperclass();
			staging = stagingType.getAnnotation(InjectStaged.class);
		}
		if (staging != null)
			result = subsume(type.getName(), staging.ignore());
		// else
		// LOG.warn("(Super)type missing @"
		// + InjectStaged.class.getSimpleName() + ": "
		// + type.getName());
		return result;
	}

	/**
	 * @param type
	 * @return
	 */
	public static Set<Class<? extends Throwable>> findAbsorptionLevels(
			final Class<?> type, final Method method)
	{
		final Set<Class<? extends Throwable>> result;
		final Staged staged = method.getAnnotation(Staged.class);

		// override class-level annotation by method-level annotation
		if (staged == null)
			result = findAbsorptionLevels(type);
		else
			result = subsume(toSignatureString(method), staged.ignore());

		LOG.trace("Absorption for " + toSignatureString(method) + ": " + result);
		return result;
	}

	/**
	 * @param source the (annotation) origin
	 * @param ignore the set of {@link Throwable}s to reduce (if subsumed)
	 * @return
	 */
	@SafeVarargs
	public static Set<Class<? extends Throwable>> subsume(final String source,
			final Class<? extends Throwable>... ignore)
	{
		final Set<Class<? extends Throwable>> result = new HashSet<>();
		if (ignore != null && ignore.length != 0)
			outer: for (Class<? extends Throwable> cls : ignore)
			{
				for (Class<? extends Throwable> old : result)
				{
					if (old.isAssignableFrom(cls))
					{
						LOG.info("ignore() level '" + old.getName()
								+ "' subsumes '" + cls.getName()
								+ "' annotated in " + source);
						continue outer;
					}
					if (cls.isAssignableFrom(old))
					{
						LOG.info("ignore() level '" + cls.getName()
								+ "' annotated in " + source + " subsumes '"
								+ old.getName() + "'");
						result.remove(old);
					}
				}
				result.add(cls);
			}
		return result;
	}

	/**
	 * shortcut/convenience method
	 * 
	 * @param provider
	 * @return
	 * @throws Throwable
	 */
	public static <T> T inject(final Provider<T> provider) throws Throwable
	{
		return inject(provider, null);
	}

	/**
	 * @param provider
	 * @param stageObserver
	 * @return
	 * @throws Throwable
	 */
	public static <T> T inject(final Provider<T> provider,
			final Observer<StageChange> stageObserver) throws Throwable
	{
		@SuppressWarnings("unchecked")
		final Class<T> type = (Class<T>) ClassUtil.getTypeArguments(
				Provider.class, provider.getClass()).get(0);

		boolean success = true;

		if (stageObserver != null)
			stageObserver.onNext(new StageChangeImpl(type, null,
					Stage.PREPARING, null));

		success &= invokeEventHandlers(stageObserver, type, null, null,
				StageEvent.BEFORE_PROVIDE);

		success &= invokeStages(stageObserver, type, null,
				findStages(type, InjectStaged.BEFORE_PROVIDE_SELECTOR));

		if (stageObserver != null)
			stageObserver.onNext(new StageChangeImpl(type, null,
					Stage.PROVIDING, null));

		final FinalizeDecorator<T> subject = FinalizeDecorator.from(
				stageObserver, type, provider.get());

		if (stageObserver != null)
			stageObserver.onNext(new StageChangeImpl(type, subject.getTarget(),
					Stage.PROVIDED, null));
		success &= invokeEventHandlers(stageObserver, type,
				subject.getTarget(), null, StageEvent.AFTER_PROVIDE);

		success &= invokeStages(stageObserver, type, subject.getTarget(),
				findStages(type, InjectStaged.AFTER_PROVIDE_SELECTOR));

		if (stageObserver != null && success)
			stageObserver.onNext(new StageChangeImpl(type, subject.getTarget(),
					Stage.STOPPED, null));
		return subject.getTarget();
	}

	public static <T> boolean invokeStages(
			final Observer<StageChange> stageObserver, final Class<T> type,
			final T target, final Collection<String> stages) throws Throwable
	{
		if (stages == null || stages.isEmpty())
			return true;

		boolean success = true;
		for (String stage : stages)
		{
			if (stageObserver != null)
				stageObserver.onNext(new StageChangeImpl(type, target,
						Stage.STARTING, stage));
			success &= invokeEventHandlers(stageObserver, type, target, stage,
					StageEvent.BEFORE_STAGE);

			if (stageObserver != null)
				stageObserver.onNext(new StageChangeImpl(type, target,
						Stage.STARTED, stage));
			final List<String> nextStages = invokeStageHandlers(stageObserver,
					type, target, stage);
			success &= nextStages != null;

			if (stageObserver != null)
				stageObserver.onNext(new StageChangeImpl(type, target,
						Stage.STOPPING, stage));
			success &= invokeEventHandlers(stageObserver, type, target, stage,
					StageEvent.AFTER_STAGE);

			if (nextStages != null && !nextStages.isEmpty())
			{
				LOG.trace("Starting *nested* stage: " + nextStages);
				invokeStages(stageObserver, type, target, nextStages);
			}
		}
		return success;
	}

	public static <T> boolean invokeEventHandlers(
			final Observer<StageChange> stageObserver, final Class<T> type,
			final T target, final String currentStage, final StageEvent event)
			throws Throwable
	{
		Object result;
		boolean failed = false;
		for (Map.Entry<Method, Staged> entry : findEventHandlers(type, event)
				.entrySet())
		{
			LOG.trace("Calling 'on=" + event.name() + "' handler method: "
					+ toSignatureString(entry.getKey()));
			result = invoke(stageObserver, currentStage, type, target,
					entry.getKey());
			failed |= result instanceof Throwable;
		}
		return !failed;
	}

	/**
	 * @param stageObserver
	 * @param type
	 * @param target
	 * @param currentStage
	 * @return the next stage, or {@code null} if failed
	 * @throws Throwable
	 */
	public static <T> List<String> invokeStageHandlers(
			final Observer<StageChange> stageObserver, final Class<T> type,
			final T target, final String currentStage) throws Throwable
	{
		Object result;
		boolean failed = false;
		final List<String> nextStages = new ArrayList<>();
		for (Map.Entry<Method, Staged> entry : findStageHandlers(type,
				currentStage).entrySet())
		{
			LOG.trace("Calling 'onCustom=" + currentStage
					+ "' handler method: " + toSignatureString(entry.getKey()));
			result = invoke(stageObserver, currentStage, type, target,
					entry.getKey());
			if (result instanceof Throwable)
				failed = true;
			else if (entry.getValue().returnsNextStage())
			{
				final String nextStage = result == null ? null : result
						.toString();
				if (nextStage == null)
					LOG.error("Illegal (void or null) next stage returned by "
							+ toSignatureString(entry.getKey()));
				else
					nextStages.add(nextStage);
			}
			failed |= result instanceof Throwable;
		}
		return failed ? null : nextStages;
	}

	/**
	 * @param stageObserver
	 * @param type
	 * @param currentStage
	 * @param method
	 * @param target
	 * @param args
	 * @return
	 * @throws Throwable
	 */
	public static <T> Object invoke(final Observer<StageChange> stageObserver,
			final String currentStage, final Class<T> type, final T target,
			final Method method, final Object... args) throws Throwable
	{
		try
		{
			return method.invoke(target, args);
		} catch (Throwable t)
		{
			if (stageObserver != null)
				stageObserver.onNext(new StageChangeImpl(target.getClass(),
						target, Stage.FAILING, currentStage));
			if (t instanceof InvocationTargetException)
				t = t.getCause();
			for (Map.Entry<Method, Staged> entry : findEventHandlers(type,
					StageEvent.BEFORE_FAIL).entrySet())
			{
				if (target == null
						&& !Modifier.isStatic(entry.getKey().getModifiers()))
				{
					LOG.warn("Can't invoke non-static 'on="
							+ StageEvent.BEFORE_FAIL + "' method: "
							+ toSignatureString(entry.getKey()), t);
					continue;
				}
				LOG.trace("Calling 'on=" + StageEvent.BEFORE_FAIL
						+ "' method: " + toSignatureString(method));
				try
				{
					if (entry.getKey().getParameterTypes().length == 0)
						entry.getKey().invoke(target);
					else if (entry.getKey().getParameterTypes()[0]
							.isAssignableFrom(t.getClass()))
						entry.getKey().invoke(target, t);
					else
						LOG.error("Signature of 'on=" + StageEvent.BEFORE_FAIL
								+ "' method: "
								+ toSignatureString(entry.getKey())
								+ " does not match parameter: "
								+ t.getClass().getName());
				} catch (Throwable t2)
				{
					if (t2 instanceof InvocationTargetException)
						t2 = t2.getCause();
					LOG.error("Uncaught errors for 'on="
							+ StageEvent.BEFORE_FAIL + "' method: "
							+ toSignatureString(entry.getKey()), t2);
				}
			}
			if (stageObserver != null)
			{
				stageObserver.onNext(new StageChangeImpl(target.getClass(),
						target, Stage.FAILED, currentStage));
				if (stageObserver != null)
					stageObserver.onError(t);
			}
			for (Class<? extends Throwable> sup : findAbsorptionLevels(
					target.getClass(), method))
				if (sup.isAssignableFrom(t.getClass()))
				{
					LOG.trace("Absorbed " + t.getClass().getSimpleName() + ": "
							+ t.getMessage());
					return t;
				}
			throw t;
		}
	}

	/**
	 * @param method
	 * @return
	 */
	public static String toSignatureString(final Method method)
	{
		final StringBuilder result = new StringBuilder(method
				.getDeclaringClass().getSimpleName()).append('#')
				.append(method.getName()).append('(');
		boolean first = true;
		for (Class<?> type : method.getParameterTypes())
		{
			if (first)
				first = false;
			else
				result.append(", ");
			result.append(type.getSimpleName());
		}
		return result.append(')').toString();
	}

	/**
	 * {@link StageChangeImpl}
	 * 
	 * @date $Date$
	 * @version $Id: a5f5f5565fb0c75c612035f5f5d6bceaef1f171c $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	private static class StageChangeImpl implements StageChange
	{
		/** */
		private final Stage stage;

		/** */
		private final String custom;

		/** */
		private final Class<?> type;

		/** */
		private final int hash;

		/**
		 * {@link StageChangeImpl} constructor
		 * 
		 * @param target
		 * @param stage
		 * @param custom
		 */
		private StageChangeImpl(final Class<?> type, final Object target,
				final Stage stage, final String custom)
		{
			int hashCode = -1;
			if (target != null)
				try
				{
					hashCode = target.hashCode();
				} catch (final Throwable t)
				{
					LOG.warn("hashCode() failed for " + type.getName(), t);
				}
			this.type = type;
			this.hash = hashCode;
			this.stage = stage;
			this.custom = custom;
		}

		@Override
		public Class<?> getType()
		{
			return this.type;
		}

		@Override
		public int getHash()
		{
			return this.hash;
		}

		@Override
		public Stage getStage()
		{
			return this.stage;
		}

		@Override
		public String getCustom()
		{
			return this.custom;
		}

		@Override
		public String toString()
		{
			return JsonUtil.toJSONString(this);
		}
	}

	/**
	 * {@link FinalizeDecorator}
	 * 
	 * @date $Date$
	 * @version $Id: a5f5f5565fb0c75c612035f5f5d6bceaef1f171c $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 * @param <T>
	 */
	private static class FinalizeDecorator<T>
	{
		/** */
		private final Observer<StageChange> stageObserver;

		/** */
		private final Class<T> type;

		/** */
		private final T target;

		/**
		 * {@link FinalizeDecorator} constructor
		 * 
		 * @param target
		 * @param obs
		 * @param type
		 */
		private FinalizeDecorator(final Observer<StageChange> obs,
				final Class<T> type, final T target)
		{
			this.stageObserver = obs;
			this.type = type;
			this.target = target;
		}

		/**
		 * @return
		 */
		public Class<T> getType()
		{
			return this.type;
		}

		/**
		 * @return
		 */
		public T getTarget()
		{
			return this.target;
		}

		/**
		 * WARNING! Override probably introduces severe performance issues!
		 * 
		 * @see java.lang.Object#finalize()
		 */
		@Override
		public void finalize()
		{
			// LOG.trace("FinalizeDecorator called!");
			if (this.stageObserver != null)
				this.stageObserver.onNext(new StageChangeImpl(getTarget()
						.getClass(), getTarget(), Stage.RECYCLING, null));

			for (Map.Entry<Method, Staged> entry : findEventHandlers(getType(),
					StageEvent.BEFORE_RECYCLE).entrySet())
			{
				LOG.trace("Calling 'on=" + StageEvent.BEFORE_RECYCLE
						+ "' method: " + toSignatureString(entry.getKey()));
				try
				{
					invoke(this.stageObserver, null, getType(), getTarget(),
							entry.getKey());
				} catch (final Throwable e)
				{
					LOG.warn("Errors unhandled for 'on="
							+ StageEvent.BEFORE_RECYCLE + "' method: "
							+ toSignatureString(entry.getKey()), e);
				}
			}
			if (this.stageObserver != null)
			{
				this.stageObserver.onNext(new StageChangeImpl(getTarget()
						.getClass(), getTarget(), Stage.RECYCLED, null));
				this.stageObserver.onCompleted();
			}
		}

		/**
		 * @param target
		 * @param stageObserver
		 * @param type
		 * @return
		 */
		public static <T> FinalizeDecorator<T> from(
				final Observer<StageChange> stageObserver, final Class<T> type,
				final T target)
		{
			return new FinalizeDecorator<T>(stageObserver, type, target);
		}
	}
}
