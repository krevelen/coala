package io.coala.experimental.dynabean;

import io.coala.exception.CoalaExceptionFactory;
import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.Map;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link DynamicBeanFactory}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DynamicBeanFactory
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(DynamicBeanFactory.class);

	/**
	 * {@link DynamicBeanFactory} constructor
	 */
	private DynamicBeanFactory()
	{
	}

	@SuppressWarnings("unchecked")
	public static <T extends DynamicBean> T cloneDynamicBean(
			T pClonedDynamicBean)
	{
		// TODO: Jackson 2.0.0 come with more efficient clone method, replace
		// this approach
		// with that method once 2.0 is integrated with the platform. See
		// [JACKSON-707]

		// long tStartTimestamp = System.nanoTime();

		String tClonedJsonString = pClonedDynamicBean.toJsonString();

		T tReturnBean = (T) parseJSONString(tClonedJsonString,
				pClonedDynamicBean.getClass());

		// ServerLoggingService.log( "Dynamic bean cloning succeeded in " + ( (
		// System.nanoTime() - tStartTimestamp ) / 1000 ) + " microseconds.",
		// Level.FINER );

		return tReturnBean;
	}

	public static <T extends DynamicBean> T parseDynamicBean(
			DynamicBean pDynamicBean, Class<T> pDynamicBeanClass)
	{
		return parseJSONNode(pDynamicBean.mBeanRootNode, pDynamicBeanClass);
	}

	@SuppressWarnings("unchecked")
	public static <T extends DynamicBean> T parseJSONNode(JsonNode pJSONNode,
			Class<T> pDynamicBeanClass)
	{
		try
		{
			return (T) pDynamicBeanClass.newInstance().useJSONNode(pJSONNode);
		} catch (InstantiationException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONNode.toString(), ex);
			return null;
		} catch (IllegalAccessException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONNode.toString(), ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends DynamicBean> T parseJSONString(String pJSONString,
			Class<T> pDynamicBeanClass)
	{
		try
		{
			// class could be abstract, in that case, let Jackson's mapper to
			// handle this
			if (Modifier.isAbstract(pDynamicBeanClass.getModifiers()))
			{
				return JsonUtil.getJOM().readValue(pJSONString,
						pDynamicBeanClass);
			} else
			{
				return (T) pDynamicBeanClass.newInstance().useJSONString(
						pJSONString);
			}
		} catch (InstantiationException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONString, ex);
			return null;
		} catch (IllegalAccessException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONString, ex);
			return null;
		} catch (JsonParseException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONString, ex);
			throw new IllegalArgumentException(
					"Incorrect JSON String, failed to parse " + pJSONString
							+ ".", ex);
		} catch (IOException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and parsed JSON " + pJSONString, ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends DynamicBean> T parseWithCustomDeserializer(
			String pDeserializedString, Class<T> pDynamicBeanClass)
	{
		try
		{
			T tReturnBean = pDynamicBeanClass.newInstance();

			if (!tReturnBean.hasCustomDeserializer())
			{
				throw new IllegalArgumentException(
						"Cannot instantiate with custom deserializer, dynamic bean class "
								+ pDynamicBeanClass.getSimpleName()
								+ " does not override hasCustomDeserializer() with true flag.");
			}

			return (T) tReturnBean.useCustomDeserializer(pDeserializedString);
		} catch (InstantiationException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and custom serialized string " + pDeserializedString,
					ex);
			return null;
		} catch (IllegalAccessException ex)
		{
			LOG.warn("For bean class " + pDynamicBeanClass.getName()
					+ " and custom serialized string " + pDeserializedString,
					ex);
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <T extends DynamicBean> T parseStringHashMap(
			Map<String, String> pStringHashMap, Class<T> pDynamicBeanClass)
	{
		try
		{
			return (T) pDynamicBeanClass.newInstance().useStringHashMap(
					pStringHashMap);
		} catch (Exception ex)
		{
			LOG.error("For bean class " + String.valueOf(pDynamicBeanClass)
					+ " and string hash map " + pStringHashMap, ex);
			throw CoalaExceptionFactory.MARSHAL_FAILED.createRuntime(ex,
					pStringHashMap, pDynamicBeanClass);
		}
	}
}
