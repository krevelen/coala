/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/log/JULMembersInjector.java $
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
package io.coala.guice.log;

import io.coala.log.LogUtil;
import io.coala.model.ModelComponent;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.google.inject.MembersInjector;

/**
 * {@link JULMembersInjector}
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T>
 */
class JULMembersInjector<T> implements MembersInjector<T>
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(JULMembersInjector.class);

	/** */
	private final Field field;

	/**
	 * {@link JULMembersInjector} constructor
	 * 
	 * @param field
	 */
	public JULMembersInjector(final Field field)
	{
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public void injectMembers(final T t)
	{
		final java.util.logging.Logger logger = t instanceof ModelComponent ? LogUtil
				.getJavaLogger(((ModelComponent<?>) t).getID() + " "
						+ this.field.getDeclaringClass().getName()) : LogUtil
				.getJavaLogger(this.field.getDeclaringClass().getName());
		try
		{
			this.field.set(t, logger);
			LOG.trace("Injected "
					+ java.util.logging.Logger.class.getSimpleName()
					+ " into a " + t.getClass().getSimpleName());
		} catch (final IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}