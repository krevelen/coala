/* $Id: 599adb6ed329ecf47b6698a25755027568161d8f $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/AbstractPropertyGetter.java $
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
package io.coala.config;

import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.exception.CoalaRuntimeException;
import io.coala.factory.ClassUtil;
import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

/**
 * {@link AbstractPropertyGetter}
 * 
 * @date $Date: 2014-08-04 14:19:04 +0200 (Mon, 04 Aug 2014) $
 * @version $Revision: 336 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public abstract class AbstractPropertyGetter implements PropertyGetter
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(AbstractPropertyGetter.class);

	/** */
	private final String key;

	/** */
	private final String defaultValue;

	/**
	 * {@link AbstractPropertyGetter} constructor
	 * 
	 * @param key
	 */
	public AbstractPropertyGetter(final String key)
	{
		this(key, null);
	}

	/**
	 * {@link AbstractPropertyGetter} constructor
	 * 
	 * @param key
	 * @param defaultValue
	 */
	public AbstractPropertyGetter(final String key, final String defaultValue)
	{
		this.key = key;
		this.defaultValue = defaultValue;
	}

	protected abstract Properties getProperties();

	/**
	 * @return the configured value, or the default value if available
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public String get() throws CoalaRuntimeException
	{
		final String result = getProperties().getProperty(this.key);

		if (result != null && !result.equals("" + null))
			return result;

		if (this.defaultValue != null)
			return this.defaultValue;

		LOG.trace("Property '" + this.key
				+ "' not found nor default value set: "
				+ getProperties().getProperty(this.key));

		throw CoalaExceptionFactory.VALUE_NOT_CONFIGURED.createRuntime(
				this.key, ConfigUtil.PROPERTIES_FILE);
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public String get(final String defaultValue)
	{
		final String result = CoalaPropertyMap.getInstance().getProperty(
				this.key);
		if (result != null)
			return result;

		LOG.trace(String.format("Properties specify no value for key: '%s', "
				+ "using default value in stead: '%s'", this.key, defaultValue));
		return defaultValue;
	}

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 * @throws NumberFormatException
	 */
	public Number getNumber() throws NumberFormatException,
			CoalaRuntimeException
	{
		return Double.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Number getNumber(final Number defaultValue)
			throws NumberFormatException
	{
		final String value = get(defaultValue == null ? null : defaultValue
				.toString());
		return value == null ? null : Double.valueOf(value);
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public BigDecimal getBigDecimal(final BigDecimal defaultValue)
	{
		return new BigDecimal(get(defaultValue.toString()));
	}

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public Boolean getBoolean() throws CoalaRuntimeException
	{
		return Boolean.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Boolean getBoolean(final Boolean defaultValue)
	{
		final String value = get(defaultValue == null ? null : Boolean
				.toString(defaultValue));
		return value == null ? null : Boolean.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 * @throws NumberFormatException
	 */
	public Byte getByte() throws NumberFormatException, CoalaRuntimeException
	{
		return Byte.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Byte getByte(final Byte defaultValue)
	{
		final String value = get(defaultValue == null ? null : Byte
				.toString(defaultValue));
		return value == null ? null : Byte.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public Character getChar() throws CoalaRuntimeException
	{
		return Character.valueOf(get().charAt(0));
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Character getChar(final Character defaultValue)
	{
		final String value = get(defaultValue == null ? null : Character
				.toString(defaultValue));
		return value == null ? null : value.charAt(0);
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	public Short getShort() throws CoalaRuntimeException
	{
		return Short.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Short getShort(final Short defaultValue)
	{
		final String value = get(defaultValue == null ? null : Short
				.toString(defaultValue));
		return value == null ? null : Short.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	public Integer getInt() throws CoalaRuntimeException
	{
		return Integer.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Integer getInt(final Integer defaultValue)
	{
		final String value = get(defaultValue == null ? null : Integer
				.toString(defaultValue));
		return value == null ? null : Integer.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	public Long getLong() throws CoalaRuntimeException
	{
		return Long.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Long getLong(final Long defaultValue)
	{
		final String value = get(defaultValue == null ? null : Long
				.toString(defaultValue));
		return value == null ? null : Long.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	public Float getFloat() throws CoalaRuntimeException
	{
		return Float.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Float getFloat(final Float defaultValue)
	{
		final String value = get(defaultValue == null ? null : Float
				.toString(defaultValue));
		return value == null ? null : Float.valueOf(value);
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	public Double getDouble() throws CoalaRuntimeException
	{
		return Double.valueOf(get());
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public Double getDouble(final Double defaultValue)
	{
		final String value = get(defaultValue == null ? null : Double
				.toString(defaultValue));
		return value == null ? null : Double.valueOf(value);
	}

	/**
	 * @param enumType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public <E extends Enum<E>> E getEnum(final Class<E> enumType)
			throws CoalaException
	{
		if (enumType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.createRuntime("enumType");

		final String value = get();
		try
		{
			return Enum.valueOf(enumType, value);
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.create(e, value,
					enumType != null ? defaultValue.getClass() : Enum.class);
		}
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	public <E extends Enum<E>> E getEnum(final E defaultValue)
	{
		final String value = defaultValue == null ? get("") : get(defaultValue
				.name());
		if (value != null && !value.isEmpty())
			try
			{
				return Enum.valueOf(defaultValue.getDeclaringClass(), value);
			} catch (final Throwable e)
			{
				LOG.warn("getObject failed",
						CoalaExceptionFactory.UNMARSHAL_FAILED.create(e, value,
								defaultValue != null ? defaultValue.getClass()
										: Enum.class));
			}
		return defaultValue;
	}

	/**
	 * @return
	 *
	 *         public Duration getDuration() { final String value = get();
	 *         return Duration.parse(value); }
	 * 
	 *         /**
	 * @return
	 *
	 *         public Duration getDuration(final Duration defaultValue) { final
	 *         String value = get(""); if (!value.isEmpty()) try { return
	 *         Duration.parse(value); } catch (final Throwable e) {
	 *         LOG.warn("Problem parsing Duration, using default in stead",
	 *         CoalaExceptionFactory.UNMARSHAL_FAILED.create(e, value,
	 *         defaultValue)); } return defaultValue; }
	 */

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public <T> T getObject(final Class<T> valueType)
			throws CoalaRuntimeException
	{
		if (valueType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("valueType");

		final String value = get();
		try
		{
			return valueType.cast(ClassUtil.deserialize(value, valueType));
		} catch (final CoalaRuntimeException e)
		{
			throw e;
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					value, valueType);
		}
	}

	/**
	 * @param defaultValue
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public <T> T getObject(final T defaultValue)
	{
		final String value = get("");
		if (!value.isEmpty())
			try
			{
				return (T) ClassUtil
						.deserialize(value, defaultValue.getClass());
			} catch (final CoalaException e)
			{
				LOG.warn(
						"Problem deserializing object, using default in stead",
						e);
			} catch (final Throwable e)
			{
				LOG.warn(
						"Problem deserializing object, using default in stead",
						CoalaExceptionFactory.UNMARSHAL_FAILED.create(e, value,
								defaultValue != null ? defaultValue.getClass()
										: Object.class));
			}
		return defaultValue;
	}

	/**
	 * @param valueTypeRef
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	@SuppressWarnings("rawtypes")
	public <T> T getJSON(final TypeReference valueTypeRef)
			throws CoalaRuntimeException

	{
		if (valueTypeRef == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("valueTypeRef");

		final String value = get();
		try
		{
			return JsonUtil.getJOM().readValue(value, valueTypeRef);
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					value, valueTypeRef.getType());
		}
	}

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public <T> T getJSON(final JavaType valueType) throws CoalaRuntimeException
	{
		if (valueType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("valueType");

		final String value = get();
		try
		{
			return JsonUtil.getJOM().readValue(value, valueType);
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					value, valueType.getRawClass());
		}
	}

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public <T> T getJSON(final Class<T> valueType) throws CoalaRuntimeException
	{
		final String value = get();
		try
		{
			return JsonUtil.getJOM().readValue(value, valueType);
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					value, valueType.getClass());
		}
	}

	/**
	 * @param defaultValue
	 * @return
	 * @throws CoalaException if umarshalling failed
	 */
	@SuppressWarnings("unchecked")
	public <T> T getJSON(final T defaultValue) throws CoalaRuntimeException
	{
		if (defaultValue == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("defaultValue");

		final String value = get("");
		if (value.isEmpty())
			return defaultValue;

		try
		{
			return JsonUtil.getJOM().readValue(get(),
					(Class<T>) defaultValue.getClass());
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					value, defaultValue.getClass());
		}
	}

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public Class<?> getType() throws CoalaRuntimeException
	{
		final String value = get();
		try
		{
			return Class.forName(value);
		} catch (final ClassNotFoundException e)
		{
			throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime(e,
					this.key, value);
		}
	}

	/**
	 * @param superType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public <T> Class<? extends T> getType(final Class<T> superType)
			throws CoalaRuntimeException
	{
		try
		{
			return getType().asSubclass(superType);
		} catch (final ClassCastException e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e,
					get(), superType);
		}
	}

	/**
	 * @param keySuperType
	 * @param valueSuperType
	 * @return
	 * @throws CoalaException
	 */
	@Override
	public <K, V> Map<Class<? extends K>, Class<? extends V>> getBindings(
			final Class<K> keySuperType, final Class<V> valueSuperType)
			throws CoalaRuntimeException
	{
		final Map<Class<? extends K>, Class<? extends V>> result = new HashMap<>();
		for (Entry<?, ?> entry : ((Map<?, ?>) getJSON(Map.class)).entrySet())
		{
			try
			{
				final Class<? extends K> keyType = Class.forName(
						(String) entry.getKey()).asSubclass(keySuperType);
				final Class<? extends V> valueType = Class.forName(
						(String) entry.getValue()).asSubclass(valueSuperType);
				result.put(keyType, valueType);
			} catch (final Throwable t)
			{
				new IllegalArgumentException("Could not set type from config: "
						+ entry, t).printStackTrace();
			}
		}
		return result;
	}

}