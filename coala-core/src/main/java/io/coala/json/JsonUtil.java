/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/json/JsonUtil.java $
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
package io.coala.json;

import io.coala.exception.CoalaExceptionFactory;
import io.coala.util.Util;

import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link JsonUtil}
 * 
 * @date $Date: 2014-08-14 05:46:34 +0200 (Thu, 14 Aug 2014) $
 * @version $Revision: 362 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class JsonUtil implements Util
{

	/** */
	// private static final Logger LOG = Logger.getLogger(JsonUtil.class);

	/** */
	private static final ObjectMapper JOM = new ObjectMapper();

	static
	{

		// .setVisibility(PropertyAccessor.FIELD,
		// Visibility.PROTECTED_AND_PUBLIC)

		// JOM.registerModule(new JsonSimTimeModule());

		// LOG.trace("Created JSON object mapper version: " +
		// JOM.version());
	}

	/** singleton design pattern constructor */
	private JsonUtil()
	{
		// singleton design pattern
	}

	/** */
	public synchronized static ObjectMapper getJOM()
	{
		if (JOM == null)
		{
			// JOM = new ObjectMapper()

			// for marshalling private and protected fields
			;
		}

		return JOM;
	}

	/**
	 * @param object
	 * @return
	 */
	public static String toJSONString(final Object object)
	{
		try
		{
			return JsonUtil.getJOM().writeValueAsString(object);
		} catch (final JsonProcessingException e)
		{
			throw CoalaExceptionFactory.MARSHAL_FAILED.createRuntime(e,
					object.getClass(), object.getClass());
		}
	}

	/**
	 * @param object
	 * @return
	 */
	public static String toPrettyJSON(final Object object)
	{
		try
		{
			return JsonUtil.getJOM()
					.setSerializationInclusion(JsonInclude.Include.NON_NULL)
					.writer().withDefaultPrettyPrinter()
					.writeValueAsString(object);
		} catch (final JsonProcessingException e)
		{
			throw CoalaExceptionFactory.MARSHAL_FAILED.createRuntime(e, object,
					object.getClass());
		}
	}

	/**
	 * @param json the {@link InputStream}
	 * @param resultType the type of result {@link Object}
	 * @return the unmarshalled {@link Object}
	 */
	public static <T> T fromJSON(final InputStream json,
			final Class<T> resultType)
	{
		try
		{
			return (T) JsonUtil.getJOM().readValue(json, resultType);
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e, json,
					resultType);
		}
	}

	/**
	 * @param json
	 * @param resultType the type of result {@link Object}
	 * @return the unmarshalled {@link Object}
	 */
	public static <T> T fromJSONString(final String json,
			final Class<T> resultType)
	{
		try
		{
			return (T) getJOM().readValue(json, resultType);
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e, json,
					resultType);
		}
	}

	/**
	 * @param stream
	 * @return
	 */
	public static JsonNode fromJSON(final InputStream json)
	{
		try
		{
			return getJOM().readTree(json);
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e, json,
					JsonNode.class);
		}
	}

	/**
	 * @param string
	 * @return
	 */
	public static JsonNode fromJSON(final String json)
	{
		try
		{
			return getJOM().readTree(json);
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.createRuntime(e, json,
					JsonNode.class);
		}
	}

	/**
	 * @param profile
	 * @return
	 */
	public static JsonNode toJSON(final Object object)
	{
		return getJOM().valueToTree(object);
	}

}
