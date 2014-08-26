package io.coala.experimental.dynabean;

import io.coala.log.LogUtil;

import java.io.IOException;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.jsontype.impl.AsPropertyTypeDeserializer;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class DynamicBeanJacksonDeserializer extends
		StdDeserializer<DynamicBean>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(DynamicBeanJacksonDeserializer.class);
	
	public DynamicBeanJacksonDeserializer()
	{
		super(DynamicBean.class);
	}

	@Override
	public DynamicBean deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException
	{
		throw new IllegalStateException(
				"Dynamic bean deserialization should have been handled by deserializeWithType() and not passed to this method.");
	}

	@SuppressWarnings("deprecation")
	@Override
	public DynamicBean deserializeWithType(JsonParser jp,
			DeserializationContext ctxt, TypeDeserializer typeDeserializer)
			throws IOException, JsonProcessingException
	{
		ObjectMapper tMapper = (ObjectMapper) jp.getCodec();
		JsonNode tJsonRootNode = tMapper.readTree(jp);
		ObjectNode tRootNode = null;

		if (tJsonRootNode.isObject())
		{
			tRootNode = (ObjectNode) tJsonRootNode;
		} else
		{
			tRootNode = tMapper.createObjectNode();
			tRootNode.put("value", tJsonRootNode);
		}

		JsonNode tDynamicBeanClassNameNode = tRootNode.get(typeDeserializer
				.getPropertyName());

		String tDynamicBeanClassName = ((AsPropertyTypeDeserializer) typeDeserializer)
				.baseTypeName();
		if (tDynamicBeanClassNameNode != null)
		{
			tDynamicBeanClassName = tRootNode.get(
					typeDeserializer.getPropertyName()).asText();
		}

		try
		{
			ClassLoader tCurrentThreadLoader = Thread.currentThread()
					.getContextClassLoader();

			@SuppressWarnings("unchecked")
			Class<? extends DynamicBean> tDynamicBeanClass = (Class<? extends DynamicBean>) Class
					.forName(tDynamicBeanClassName, true, tCurrentThreadLoader);

			DynamicBean tDeserializerDynamicBean = DynamicBeanFactory
					.parseJSONNode(tRootNode, tDynamicBeanClass);

			// clean up the @class field
			tDeserializerDynamicBean.deleteField(DealField
					.useCustomField(typeDeserializer.getPropertyName()));

			return tDeserializerDynamicBean;
		} catch (ClassNotFoundException ex)
		{
			LOG.warn("Cannot parse Dynamic Bean class " + tDynamicBeanClassName
					+ ", returning null.", ex);
			return getNullValue();
		}
	}

}
