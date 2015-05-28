/*
 * $Id$
 * $URL:
 * https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/
 * coala/lifecycle/MachineUtil.java $
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
 * @license
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy
 * of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 * Copyright (c) 2010-2014 Almende B.V.
 */
package io.coala.lifecycle;

import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.factory.ClassUtil;
import io.coala.log.LogUtil;
import io.coala.name.Identifiable;
import io.coala.util.Util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

import rx.Observer;

/**
 * {@link MachineUtil} utility class, e.g. to manage the {@link MachineStatus}
 * in {@link Machine}s
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class MachineUtil implements Util {

	/** */
	private static final Logger	LOG	= LogUtil.getLogger(MachineUtil.class);

	/**
	 * {@link MachineUtil} constructor
	 */
	private MachineUtil() {
		// utility class should not provide protected/public instances
	}

	/** */
	private static final ExecutorService	STATUS_NOTIFIER	= Executors
																	.newCachedThreadPool();

	static class Maps {
		final Map<Field, Class<?>>	statusFields	= new HashMap<Field, Class<?>>();
		final Map<Field, Class<?>>	statusObservers	= new HashMap<Field, Class<?>>();
	}

	final static Map<String, Maps>	machineMaps	= new HashMap<String, Maps>();

	final static <S extends MachineStatus<S>> void fillMaps(Machine<S> target, Class<?> type) {
		Maps maps = machineMaps.get(target.getClass().getName());
		if (maps == null){
			maps = new Maps();
		}
		// move up the ancestor class
		for (Class<?> declaringType = target.getClass(); declaringType != Object.class; declaringType = declaringType
				.getSuperclass()) {
			for (Field field : declaringType.getDeclaredFields()) {
				if (!field.isAnnotationPresent(LifeCycleManaged.class))
					continue;

				try {
					field.setAccessible(true);
					if (ClassUtil.isAssignableFrom(Observer.class,
							field.getType()))
						maps.statusObservers.put(field, declaringType);
					else if (ClassUtil.isAssignableFrom(field.getType(),
							type))
						maps.statusFields.put(field, declaringType);
					else
						LOG.error(String.format(
								"Field annotated with %s should declare "
										+ "either a (subtype of) %s "
										+ "or a (supertype of) %s, "
										+ "but is: %s", LifeCycleManaged.class
										.getSimpleName(), Observer.class
										.getName(), type.getName(), field.getType().getName()));
				} catch (final Throwable t) {
					throw CoalaExceptionFactory.STATUS_UPDATE_FAILED
							.createRuntime(t, target, type);
				}
			}
			
		}
		machineMaps.put(target.getClass().getName(), maps);
	}

	/**
	 * @param target
	 * @param newStatus
	 * @throws CoalaException
	 */
	@SuppressWarnings("unchecked")
	public static <S extends MachineStatus<S>> void setStatus(
			final Machine<S> target, final S newValue, final boolean completed) {
		if (target == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.createRuntime("target");

		final String targetName = target instanceof Identifiable ? String
				.format("%s[%s]", target.getClass().getSimpleName(),
						((Identifiable<?>) target).getID()) : target.getClass()
				.getSimpleName();

		Maps maps = machineMaps.get(target.getClass().getName());
		if (maps == null) {
			fillMaps(target,newValue.getClass());
			maps = machineMaps.get(target.getClass().getName());
		}

		if (maps.statusFields.isEmpty()) {
			LOG.warn(String.format("No %s fields found to update for %s of "
					+ "concrete type: %s", newValue.getClass().getSimpleName(),
					Machine.class.getSimpleName(), target.getClass().getName()));
			return;
		}
		synchronized (target) {
			// update the status fields
			for (Entry<Field, Class<?>> entry : maps.statusFields.entrySet()) {
				final Field statusField = entry.getKey();
				final Class<?> statusType = entry.getValue();
				final Exception error = new IllegalStateException();
				// STATUS_NOTIFIER.submit(new Runnable()
				// {
				// @Override
				// public void run()
				// {
				try {
					final S oldValue = (S) statusField.get(target);
					if (oldValue != null
							&& !oldValue.permitsTransitionTo(newValue)) {
						LOG.warn(String.format("Illegal transition "
								+ "from %s to %s at %s (field %s.%s)",
								oldValue, newValue, targetName,
								statusType.getSimpleName(),
								statusField.getName()), error);
						// return;
					}
					statusField.set(target, newValue);

					// notify the status observers
					for (Entry<Field, Class<?>> obsEntry : maps.statusObservers
							.entrySet()) {
						final Field observerField = obsEntry.getKey();
						final Class<?> observerType = obsEntry.getValue();
						try {
							final Observer<S> observer = (Observer<S>) observerField
									.get(target);
							STATUS_NOTIFIER.submit(new Runnable() {
								@Override
								public void run() {
									try {
										observer.onNext(newValue);
										// System.err.println(String
										// .format("Notified %s at "
										// + "%s (field %s.%s)",
										// newValue,
										// targetName,
										// observerType
										// .getSimpleName(),
										// observerField
										// .getName()));
									} catch (final Throwable t) {
										LOG.error(String.format(
												"Problem notifying %s at "
														+ "%s (field %s.%s)",
												newValue, targetName,
												observerType.getSimpleName(),
												observerField.getName()), t);
									}

									// notify completed to observers
									if (completed)
										try {
											observer.onCompleted();
											// System.err.println(String
											// .format("Notified COMPLETED at %s (field %s.%s)",
											// targetName,
											// observerType
											// .getSimpleName(),
											// observerField
											// .getName()));
										} catch (final Throwable t) {
											LOG.error(
													String.format(
															"Problem notifying COMPLETED at "
																	+ "%s (field %s.%s)",
															targetName,
															observerType
																	.getSimpleName(),
															observerField
																	.getName()),
													t);
										}
								}
							});
						} catch (final Throwable t) {
							LOG.error(String.format("Problem accessing %s at "
									+ "%s.%s of %s", observerField.getType()
									.getSimpleName(), observerType.getName(),
									observerField.getName(), targetName), t);
							t.printStackTrace();
						}
					}

				} catch (final Throwable t) {
					// notify the status observers of an error
					for (Entry<Field, Class<?>> obsEntry : maps.statusObservers
							.entrySet()) {
						final Field observerField = obsEntry.getKey();
						final Class<?> declaringType = obsEntry.getValue();
						try {
							final Observer<S> observer = (Observer<S>) observerField
									.get(target);
							STATUS_NOTIFIER.submit(new Runnable() {
								@Override
								public void run() {
									observer.onError(t);
								}
							});
						} catch (final Throwable t1) {
							LOG.error(String.format("Problem accessing %s at "
									+ "%s.%s of %s", observerField.getType()
									.getSimpleName(), declaringType.getName(),
									observerField.getName(), targetName), t);
							t.printStackTrace();
						}
					}
				}
				// }
				// });
				// LOG.trace(String.format(
				// "Status at %s.%s (%s) transitioned from %s to %s",
				// declaringType.getName(), field.getName(),
				// target.toString(), oldValue, newValue));
			}
		}
	}
}
