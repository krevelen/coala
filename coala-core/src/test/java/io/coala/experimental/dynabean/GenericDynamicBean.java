package io.coala.experimental.dynabean;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;

/**
 * {@link GenericDynamicBean}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class GenericDynamicBean extends DynamicBean
{
	@Override
	public void setFieldValue(DynamicBeanField pField, Boolean pFieldValue)
	{
		super.setFieldValue(pField, pFieldValue);
	}

	@Override
	public void setFieldValue(DynamicBeanField pField, Long pFieldValue)
	{
		super.setFieldValue(pField, pFieldValue);
	}

	@Override
	public void setFieldValue(DynamicBeanField pField, Integer pFieldValue)
	{
		super.setFieldValue(pField, pFieldValue);
	}

	@Override
	public void setFieldObjectValue(DynamicBeanField pField, Object pFieldValue)
	{
		super.setFieldObjectValue(pField, pFieldValue);
	}

	@Override
	public void setFieldValue(DynamicBeanField pField, String pFieldValue)
	{
		super.setFieldValue(pField, pFieldValue);
	}

	@Override
	public List<Long> getFieldArrayLongValue(DynamicBeanField pField)
	{
		return super.getFieldArrayLongValue(pField);
	}

	@Override
	public <T> List<T> getFieldArrayObjectValue(DynamicBeanField pField,
			Class<T> pValueClass)
	{
		return super.getFieldArrayObjectValue(pField, pValueClass);
	}

	@Override
	public int getFieldArraySize(DynamicBeanField pField)
	{
		return super.getFieldArraySize(pField);
	}

	@Override
	public List<String> getFieldArrayStringValue(DynamicBeanField pField)
	{
		return super.getFieldArrayStringValue(pField);
	}

	@Override
	public Boolean getFieldBooleanValue(DynamicBeanField pField)
	{
		return super.getFieldBooleanValue(pField);
	}

	@Override
	public Double getFieldDoubleValue(DynamicBeanField pField)
	{
		return super.getFieldDoubleValue(pField);
	}

	@Override
	public Integer getFieldIntValue(DynamicBeanField pField)
	{
		return super.getFieldIntValue(pField);
	}

	@Override
	public Long getFieldLongValue(DynamicBeanField pField)
	{
		return super.getFieldLongValue(pField);
	}

	@Override
	public <T> T getFieldObjectValue(DynamicBeanField pField,
			Class<T> pValueClass)
	{
		return super.getFieldObjectValue(pField, pValueClass);
	}

	@Override
	public JsonNode getFieldJsonNode(DynamicBeanField pField)
	{
		return super.getFieldJsonNode(pField);
	}

	@Override
	public String getFieldStringValue(DynamicBeanField pField)
	{
		return super.getFieldStringValue(pField);
	}

	@Override
	public boolean hasField(DynamicBeanField pField)
	{
		return super.hasField(pField);
	}

	@Override
	public void replaceFieldArrayObjectValue(DynamicBeanField pField,
			int pArrayIndex, Object pReplacementValue)
	{
		super.replaceFieldArrayObjectValue(pField, pArrayIndex,
				pReplacementValue);
	}

	@Override
	public void addFieldArrayObjectValue(DynamicBeanField pField,
			Object pElementValue)
	{
		super.addFieldArrayObjectValue(pField, pElementValue);
	}
}
