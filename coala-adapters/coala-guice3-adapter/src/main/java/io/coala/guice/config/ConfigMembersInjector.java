/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/config/ConfigMembersInjector.java $
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

import io.coala.config.ConfigConverter;
import io.coala.config.InjectConfig;

import java.lang.reflect.Field;

import org.apache.commons.configuration.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.MembersInjector;

/**
 * {@link ConfigMembersInjector} inspired by <a
 * href="http://java-taste.blogspot.nl/2011/10/guiced-configuration.html"
 * >here</a>
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ConfigMembersInjector<T> implements MembersInjector<T>
{

	/** */
	private static final String NO_PROPERTY_FOUND_LOG_MESSAGE = "No property {} found. Injecting default value";

	/** */
	private static final Logger LOG = LoggerFactory
			.getLogger(ConfigMembersInjector.class);

	/** */
	private final Field field;

	/** */
	private final Configuration configuration;

	/**
	 * {@link ConfigMembersInjector} constructor
	 * 
	 * @param field
	 * @param configuration
	 */
	public ConfigMembersInjector(final Field field,
			final Configuration configuration)
	{
		this.field = field;
		this.configuration = configuration;
		this.field.setAccessible(true);
	}

	@Override
	public void injectMembers(final T instance)
	{
		final InjectConfig injectConfigAnnotation = field
				.getAnnotation(InjectConfig.class);
		final String configurationParameterName = injectConfigAnnotation.name();
		try
		{
			final Class<?> type = this.field.getType();
			if (type == Integer.TYPE)
			{
				if (this.configuration.containsKey(configurationParameterName))
				{
					this.field.setInt(instance, this.configuration
							.getInt(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setInt(instance,
							injectConfigAnnotation.defaultIntValue());
				}
			} else if (type == Boolean.TYPE)
			{
				if (this.configuration.containsKey(configurationParameterName))
				{
					this.field.setBoolean(instance, this.configuration
							.getBoolean(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setBoolean(instance,
							injectConfigAnnotation.defaultBooleanValue());
				}

			} else if (type == Short.TYPE)
			{
				if (configuration.containsKey(configurationParameterName))
				{
					this.field.setShort(instance, this.configuration
							.getShort(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setShort(instance,
							injectConfigAnnotation.defaultShortValue());
				}
			} else if (type == Byte.TYPE)
			{
				if (configuration.containsKey(configurationParameterName))
				{
					this.field.setByte(instance, this.configuration
							.getByte(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setByte(instance,
							injectConfigAnnotation.defaultByteValue());
				}
			} else if (type == Long.TYPE)
			{
				if (this.configuration.containsKey(configurationParameterName))
				{
					this.field.setLong(instance, this.configuration
							.getLong(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setLong(instance,
							injectConfigAnnotation.defaultLongValue());
				}
			} else if (type == Float.TYPE)
			{
				if (this.configuration.containsKey(configurationParameterName))
				{
					this.field.setFloat(instance, this.configuration
							.getFloat(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setFloat(instance,
							injectConfigAnnotation.defaultFloatValue());
				}
			} else if (type == Double.TYPE)
			{
				if (configuration.containsKey(configurationParameterName))
				{
					this.field.setDouble(instance, this.configuration
							.getDouble(configurationParameterName));
				} else
				{
					LOG.debug(NO_PROPERTY_FOUND_LOG_MESSAGE,
							configurationParameterName);
					this.field.setDouble(instance,
							injectConfigAnnotation.defaultDoubleValue());
				}
			} else if (type == Character.TYPE)
			{
				if (configuration.containsKey(configurationParameterName))
				{
					this.field.setChar(
							instance,
							this.configuration.getString(
									configurationParameterName).charAt(0));
				}
			} else
			{
				final Object property = getProperty(configurationParameterName,
						injectConfigAnnotation);
				this.field.set(instance, property);
			}
		} catch (final Throwable ex)
		{
			LOG.error("Problem injecting configuration", ex);
		}
	}

	/**
	 * @param configurationParameterName
	 * @param injectConfigAnnotation
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private Object getProperty(final String configurationParameterName,
			final InjectConfig injectConfigAnnotation)
			throws InstantiationException, IllegalAccessException
	{
		final String property;
		if (this.configuration.containsKey(configurationParameterName))
		{
			property = this.configuration.getString(configurationParameterName);
		} else
		{
			property = injectConfigAnnotation.defaultValue();
		}
		final ConfigConverter<?> converter = injectConfigAnnotation.converter()
				.newInstance();
		final Object value = converter.convert(property);
		return value;
	}
}
