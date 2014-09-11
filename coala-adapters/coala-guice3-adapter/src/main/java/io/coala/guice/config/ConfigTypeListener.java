/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/config/ConfigTypeListener.java $
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
package io.coala.guice.config;

import io.coala.config.InjectConfig;

import java.lang.reflect.Field;

import javax.inject.Inject;

import org.apache.commons.configuration.Configuration;

import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

/**
 * {@link ConfigTypeListener} inspired by <a
 * href="http://java-taste.blogspot.nl/2011/10/guiced-configuration.html"
 * >here</a>
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ConfigTypeListener implements TypeListener
{

	/** */
	private final Configuration configuration;

	/**
	 * {@link ConfigTypeListener} constructor
	 * 
	 * @param configuration
	 */
	@Inject
	public ConfigTypeListener(final Configuration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	public <T> void hear(final TypeLiteral<T> type,
			final TypeEncounter<T> encounter)
	{
		for (Field field : type.getRawType().getDeclaredFields())
		{
			if (field.isAnnotationPresent(InjectConfig.class))
			{
				encounter.register(new ConfigMembersInjector<T>(field,
						configuration));
			}
		}
	}
}