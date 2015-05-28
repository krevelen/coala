/* $Id: 4315b371d925dca6e1609359465a960d25eab26d $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/BeforeDestruction.java $
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
package io.coala.stage;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import javax.inject.Provider;

/**
 * {@link InjectStaged}
 * 
 * @date $Date$
 * @version $Id: 4315b371d925dca6e1609359465a960d25eab26d $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InjectStaged
{
	/**
	 * @return the sequence of custom stages to be performed immediately
	 *         <b>before</b> construction or otherwise being provided by the
	 *         {@link Provider}
	 */
	String[] beforeProvide() default {};

	/**
	 * @return the sequence of custom stages to be performed immediately
	 *         <b>after</b> construction or otherwise being provided by the
	 *         {@link Provider}
	 */
	String[] afterProvide() default {};

	/**
	 * @return the sequence of stages to be performed immediately <b>before</b>
	 *         a remote procedure call
	 */
	// String[] beforeCall() default {};

	/**
	 * @return the sequence of stages to be performed immediately <b>after</b> a
	 *         remote procedure call
	 */
	// String[] afterCall() default {};

	/**
	 * @return the (super)type(s) of {@link Throwable}, all of whose (sub)types
	 *         are to be absorbed rather than thrown
	 */
	Class<? extends Throwable>[] ignore() default {};

	/**
	 * {@link StageSelector}
	 * 
	 * @date $Date$
	 * @version $Id: 4315b371d925dca6e1609359465a960d25eab26d $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	interface StageSelector
	{
		/**
		 * @param staging
		 * @return
		 */
		String[] selectStages(InjectStaged staging);

		Map<Class<?>, SortedSet<String>> getCache();
	}

	/** */
	StageSelector BEFORE_PROVIDE_SELECTOR = new StageSelector()
	{
		/** */
		private final Map<Class<?>, SortedSet<String>> cache = new HashMap<>();

		@Override
		public String[] selectStages(final InjectStaged staging)
		{
			return staging.beforeProvide();
		}

		@Override
		public Map<Class<?>, SortedSet<String>> getCache()
		{
			return this.cache;
		}

		@Override
		public String toString()
		{
			return "beforeProvide";
		}
	};

	/** */
	StageSelector AFTER_PROVIDE_SELECTOR = new StageSelector()
	{
		/** */
		private final Map<Class<?>, SortedSet<String>> cache = new HashMap<>();

		@Override
		public String[] selectStages(final InjectStaged staging)
		{
			return staging.afterProvide();
		}

		@Override
		public Map<Class<?>, SortedSet<String>> getCache()
		{
			return this.cache;
		}

		@Override
		public String toString()
		{
			return "afterProvide";
		}
	};
}
