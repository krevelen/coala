/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/log/Log4JMembersInjector.java $
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
import io.coala.log.CoalaLog4jLogger;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.google.inject.MembersInjector;

/**
 * {@link Log4JMembersInjector}
 * 
 * @date $Date: 2014-06-20 09:39:36 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 310 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T>
 */
class Log4JMembersInjector<T> implements MembersInjector<T>
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(Log4JMembersInjector.class);

	/** */
	private final Field field;

	/**
	 * {@link Log4JMembersInjector} constructor
	 * 
	 * @param field
	 */
	public Log4JMembersInjector(final Field field)
	{
		this.field = field;
		field.setAccessible(true);
	}

	@Override
	public void injectMembers(final T t)
	{
		final String prefix = CoalaLog4jLogger.determineLoggerPrefixForObject(t);
		final Logger logger = LogUtil.getLogger(t.getClass(), t);
		final String actualPrefix = ((CoalaLog4jLogger) logger).getPrefix();
		if (!actualPrefix.equals(prefix))
			LOG.warn("Injecting logger for " + t.getClass()
					+ " with wrong name prefix: " + actualPrefix
					+ " should be: " + prefix);
		try
		{
			this.field.set(t, logger);
			LOG.trace("Injected " + Logger.class.getSimpleName() + " into a "
					+ t.getClass().getSimpleName());
		} catch (final IllegalAccessException e)
		{
			throw new RuntimeException(e);
		}
	}
}