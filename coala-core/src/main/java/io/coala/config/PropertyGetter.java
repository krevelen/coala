/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/PropertyGetter.java $
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
package io.coala.config;

import io.coala.exception.CoalaException;

import java.math.BigDecimal;
import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;

/**
 * {@link PropertyGetter}
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public interface PropertyGetter
{

	/**
	 * @return the configured value
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	String get() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	String get(String defaultValue);

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 * @throws NumberFormatException
	 */
	Number getNumber() throws NumberFormatException, CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Number getNumber(Number defaultValue) throws NumberFormatException;

	/**
	 * @param defaultValue
	 * @return
	 */
	BigDecimal getBigDecimal(BigDecimal defaultValue);

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	Boolean getBoolean() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Boolean getBoolean(Boolean defaultValue);

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 * @throws NumberFormatException
	 */
	Byte getByte() throws NumberFormatException, CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Byte getByte(Byte defaultValue);

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	Character getChar() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Character getChar(Character defaultValue);

	/**
	 * @return
	 * @throws CoalaException
	 */
	Short getShort() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Short getShort(Short defaultValue);

	/**
	 * @return
	 * @throws CoalaException
	 */
	Integer getInt() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Integer getInt(Integer defaultValue);

	/**
	 * @return
	 * @throws CoalaException
	 */
	Long getLong() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Long getLong(Long defaultValue);

	/**
	 * @return
	 * @throws CoalaException
	 */
	Float getFloat() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Float getFloat(Float defaultValue);

	/**
	 * @return
	 * @throws CoalaException
	 */
	Double getDouble() throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	Double getDouble(Double defaultValue);

	/**
	 * @param enumType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	<E extends Enum<E>> E getEnum(Class<E> enumType) throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	<E extends Enum<E>> E getEnum(E defaultValue);

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	<T> T getObject(Class<T> valueType) throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 */
	<T> T getObject(T defaultValue);

	/**
	 * @param valueTypeRef
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	@SuppressWarnings("rawtypes")
	<T> T getJSON(TypeReference valueTypeRef) throws CoalaException;

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	<T> T getJSON(JavaType valueType) throws CoalaException;

	/**
	 * @param valueType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	<T> T getJSON(Class<T> valueType) throws CoalaException;

	/**
	 * @param defaultValue
	 * @return
	 * @throws CoalaException if umarshalling failed
	 */
	<T> T getJSON(T defaultValue) throws CoalaException;

	/**
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	Class<?> getType() throws CoalaException;

	/**
	 * @param superType
	 * @return
	 * @throws CoalaException if value was not configured nor any default was set
	 */
	<T> Class<? extends T> getType(Class<T> superType) throws CoalaException;

	/**
	 * @param keySuperType
	 * @param valueSuperType
	 * @return a {@link Map} linking {@code keySuperType} subtypes to
	 *         {@code valueSuperType} subtypes
	 * @throws CoalaException
	 */
	<K, V> Map<Class<? extends K>, Class<? extends V>> getBindings(
			final Class<K> keySuperType, final Class<V> valueSuperType)
			throws CoalaException;

}
