/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/Schedulable.java $
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
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package io.coala.invoke;

import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;

import com.fasterxml.jackson.databind.util.ClassUtil;

/**
 * {@link Schedulable}
 * 
 * @date $Date: 2014-06-17 15:03:44 +0200 (Tue, 17 Jun 2014) $
 * @version $Revision: 302 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Schedulable
{

	/**
	 * @return a class-wide unique reference to identify the {@link Schedulable}
	 *         method within its declaring {@link Class}
	 */
	String value();

	/**
	 * {@link Util}
	 * 
	 * @version $Revision: 302 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 */
	class Util
	{
		/** */
		// private static final Logger LOG = LogUtil
		// .getLogger(Schedulable.Util.class);

		/**
		 * @param reference the method's annotated {@link Schedulable} value
		 * @param target the object implementing the {@link Schedulable} method
		 * @param arguments the arguments to call the method with
		 * @return the {@link Callable} object
		 */
		public static Callable<Object> toCallable(final String reference,
				final Object target, final Object... arguments)
		{
			return new Callable<Object>()
			{
				@Override
				public Object call() throws Exception
				{
					return Util.call(reference, target, arguments);
				}
			};
		}

		/**
		 * @param reference the method's annotated {@link Schedulable} value
		 * @param target the object implementing the {@link Schedulable} method
		 * @param arguments the arguments to call the method with
		 */
		public static Callable<Object> toCallable(final String reference,
				final Object target, final List<Object> arguments)
		{
			return new Callable<Object>()
			{
				@Override
				public Object call() throws Exception
				{
					return Util.call(reference, target, arguments);
				}
			};
		}

		/**
		 * @param reference the method's annotated {@link Schedulable} value
		 * @param target the object implementing the {@link Schedulable} method
		 * @param arguments the arguments to call the method with (or none or
		 *        {@code null})
		 * @throws CoalaException
		 */
		public static Object call(final String reference, final Object target,
				final Object... arguments) throws Exception
		{
			// convert args to list
			final List<Object> args;
			if (arguments == null || arguments.length == 0)
				args = null;// Collections.emptyList();
			else
				args = Arrays.asList(arguments);

			return call(reference, target, args);
		}

		/**
		 * @param reference the method's annotated {@link Schedulable} value
		 * @param target the object implementing the {@link Schedulable} method
		 * @param arguments the arguments to call the method with (or
		 *        {@code null})
		 * @throws CoalaException
		 */
		public static Object call(final String reference, final Object target,
				final List<Object> arguments) throws Exception
		{
			Method method = findSchedulableMethod(target.getClass(), reference);
			
			// search super types/interfaces
			if (method == null)
				for (Class<?> superType : ClassUtil.findSuperTypes(
						target.getClass(), Object.class))
					if ((method = findSchedulableMethod(superType, reference)) != null)
						break;
			
			if (method == null)
				throw CoalaExceptionFactory.ANNOTATION_NOT_FOUND.createRuntime(
						Schedulable.class, target, reference);

			method.setAccessible(true);
			return method.invoke(
					target,
					arguments == null ? null : arguments
							.toArray(new Object[arguments.size()]));
		}

		private static Method findSchedulableMethod(final Class<?> superType,
				final String reference)
		{
			for (Method method : superType.getDeclaredMethods())
			{
				// TODO ignore static methods?

				final Schedulable annot = method
						.getAnnotation(Schedulable.class);

				// TODO add duck-typing?

				if (annot != null && annot.value().equals(reference))
					return method;
			}
			return null;
		}
	}
}
