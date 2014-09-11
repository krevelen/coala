/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/log/InjectLoggerTypeListener.java $
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

import io.coala.log.InjectLogger;
import io.coala.log.LogUtil;

import java.lang.reflect.Field;

import org.apache.log4j.Logger;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * {@link InjectLoggerTypeListener}
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class InjectLoggerTypeListener implements TypeListener
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(InjectLoggerTypeListener.class);

	@Override
	public <T> void hear(final TypeLiteral<T> typeLiteral,
			final TypeEncounter<T> typeEncounter)
	{
		for (Field field : typeLiteral.getRawType().getDeclaredFields())
		{
			if (!field.isAnnotationPresent(InjectLogger.class))
				continue;

			if (field.getType() == org.apache.log4j.Logger.class)
				typeEncounter.register(new Log4JMembersInjector<T>(field));
			else if (field.getType() == org.slf4j.Logger.class)
				typeEncounter.register(new SLF4JMembersInjector<T>(field));
			else if (field.getType() == java.util.logging.Logger.class)
				typeEncounter.register(new JULMembersInjector<T>(field));
			else
				LOG.warn("@" + InjectLogger.class.getSimpleName()
						+ " annotated unknown logger type " + field.getType());

			// TODO add/inject other logger type implementations
		}
	}
}