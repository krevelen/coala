package io.coala.experimental.dynabean;

import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;

import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ContainerNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.node.POJONode;

@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = JsonTypeInfo.As.PROPERTY, property = "@class")
@JsonDeserialize(using = DynamicBeanJacksonDeserializer.class)
public abstract class DynamicBean implements CustomSerializable,
		Externalizable, JsonSerializable
{
	private static final long serialVersionUID = 876150470008636468L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(DynamicBean.class);

	public static final Character cSeparatorCharacter = '/';
	public static final int cSeparatorCodePoint = Character.codePointAt(
			new char[] { cSeparatorCharacter }, 0);

	public JsonNode mBeanRootNode;
	public ObjectMapper mJsonMapper;

	private HashMap<DynamicBeanField, JsonNode> mCacheMap;

	protected boolean mShouldThrowExceptionForNullValues = false;

	protected Map<Class<?>, Map<DynamicBeanField, List<?>>> mArrayCacheMaps = new HashMap<Class<?>, Map<DynamicBeanField, List<?>>>();

	public DynamicBean()
	{
		mJsonMapper = JsonUtil.getJOM();
		mCacheMap = new HashMap<DynamicBeanField, JsonNode>();

		// initialize empty
		useStringHashMap(new HashMap<String, String>());
	}

	protected void addFieldArrayObjectValue(DynamicBeanField pField,
			Object pElementValue)
	{
		getOrCreateFieldArrayNode(pField).addPOJO(pElementValue);
		invalidateCache(pField);
	}

	protected void replaceFieldArrayObjectValue(DynamicBeanField pField,
			int pArrayIndex, Object pReplacementValue)
	{
		if (!hasField(pField))
			return;
		if (pArrayIndex < 0)
			return;

		JsonNode tFieldNode = getFieldJsonNode(pField);
		if (!tFieldNode.isArray())
			return;

		ArrayNode tFieldArrayNode = (ArrayNode) tFieldNode;
		if (pArrayIndex >= tFieldArrayNode.size())
			return;

		tFieldArrayNode.remove(pArrayIndex);
		tFieldArrayNode.insertPOJO(pArrayIndex, pReplacementValue);
	}

	protected void addFieldArrayValue(DynamicBeanField pField,
			Long pElementValue)
	{
		getOrCreateFieldArrayNode(pField).add(pElementValue);
		invalidateCache(pField);
	}

	protected void addFieldArrayValue(DynamicBeanField pField,
			String pElementValue)
	{
		getOrCreateFieldArrayNode(pField).add(pElementValue);
		invalidateCache(pField);
	}

	protected void checkForNullValue(Object tFieldNode, DynamicBeanField pField)
	{
		if (tFieldNode == null && shouldThrowExceptionForNullValues())
		{
			// re-set the should throw exception for next caller, since the
			// default state is not throwing
			resetShouldThrowExceptionForNullValues();
			if (pField == null)
			{
				throw new IllegalStateException("Cannot get value for message "
						+ toJsonString() + " since given field is null");
			} else
			{
				throw new IllegalStateException("No value for field "
						+ pField.getFieldName() + " in message "
						+ toJsonString());
			}
		}
	}

	protected void deleteField(DynamicBeanField pField)
	{
		getRootObjectNode().remove(pField.getFieldName());
		invalidateCache(pField);
	}

	private <T> List<T> getArrayFromCache(Class<T> pClass,
			DynamicBeanField pField)
	{
		Map<DynamicBeanField, List<?>> tStoredMap = mArrayCacheMaps.get(pClass);

		if (tStoredMap == null)
		{
			tStoredMap = new HashMap<DynamicBeanField, List<?>>();
			mArrayCacheMaps.put(pClass, tStoredMap);
		}

		@SuppressWarnings("unchecked")
		List<T> tStoredList = (List<T>) tStoredMap.get(pField);

		if (tStoredList == null)
		{
			tStoredList = getFieldArrayValue(pField, pClass);

			if (tStoredList != null)
			{
				tStoredMap.put(pField, tStoredList);
			}
		}

		return tStoredList;
	}

	/*    protected Collection<String> getContainedFieldNames()
	    {
	        return ListUtils.newArrayList( ( mBeanRootNode.fieldNames() ) );
	    }
	*/
	protected List<Long> getFieldArrayLongValue(DynamicBeanField pField)
	{
		return getArrayFromCache(Long.class, pField);
	}

	protected List<Integer> getFieldArrayIntValue(DynamicBeanField pField)
	{
		return getArrayFromCache(Integer.class, pField);
	}

	protected List<JsonNode> getFieldArrayNodeValue(DynamicBeanField pField)
	{
		return getArrayFromCache(JsonNode.class, pField);
	}

	protected <T> List<T> getFieldArrayObjectValue(DynamicBeanField pField,
			Class<T> pValueClass)
	{
		return getArrayFromCache(pValueClass, pField);
	}

	protected int getFieldArraySize(DynamicBeanField pField)
	{
		if (!hasField(pField))
			return 0;
		if (!getFieldJsonNode(pField).isArray())
			return -1;
		return getOrCreateFieldArrayNode(pField).size();
	}

	protected List<String> getFieldArrayStringValue(DynamicBeanField pField)
	{
		return getArrayFromCache(String.class, pField);
	}

	@SuppressWarnings("unchecked")
	// make this method private so we could control what class that we
	// would allow to be passed to the pArrayElementClass variable
	private <T> List<T> getFieldArrayValue(DynamicBeanField pField,
			Class<T> pArrayElementClass)
	{
		// // TODO: Check me!
		// if ( JsonNode.class.isAssignableFrom( pArrayElementClass ) )
		// {
		// return (List<T>) mBeanRootNode.findValues( pField.getFieldName() );
		// }

		JsonNode tFieldNode = getFieldJsonNode(pField);

		// /*BKR3System.out.println("Field " + pField.getFieldName() +
		// " of type " +
		// pArrayElementClass.getName() + " = " + tFieldNode );BKR3*/

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		// if ( !tFieldNode.isArray() ) return null;
		if (!tFieldNode.isArray())
		{
			tFieldNode = JsonNodeFactory.instance.arrayNode().addAll(
					Arrays.asList(tFieldNode));
		}

		ArrayNode tFieldNodeArray = (ArrayNode) tFieldNode;
		ArrayList<T> tReturnList = new ArrayList<T>();

		for (int i = 0; i < tFieldNodeArray.size(); i++)
		{
			Object tArrayElement = tFieldNodeArray.get(i); // default
															// behavior, get
															// the element as
															// JsonNode

			if (pArrayElementClass != JsonNode.class)
			{
				tArrayElement = getObjectValueFromJsonNode(
						(JsonNode) tArrayElement, pArrayElementClass);
			}

			// last check before adding to array
			if (tArrayElement == null)
			{
				LOG.warn("Cannot add null element to array [" + tReturnList
						+ "], original value is " + tFieldNodeArray.get(i));
			} else if (pArrayElementClass.isAssignableFrom(tArrayElement
					.getClass()))
			{
				tReturnList.add((T) tArrayElement);
			} else
			{
				LOG.warn("DynamicBean.getFieldArrayValue(DynamicBeanField, Class) : Cannot add ["
						+ tArrayElement.getClass()
						+ "] to array of type ["
						+ pArrayElementClass + "]");
			}
		}

		return tReturnList;
	}

	protected Boolean getFieldBooleanValue(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return Boolean.FALSE;
		}

		resetShouldThrowExceptionForNullValues();

		return getObjectValueFromJsonNode(tFieldNode, Boolean.class);
	}

	protected Double getFieldDoubleValue(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		return getObjectValueFromJsonNode(tFieldNode, Double.class);
	}

	protected Integer getFieldIntValue(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		return getObjectValueFromJsonNode(tFieldNode, Integer.class);
	}

	protected JsonNode getFieldJsonNode(DynamicBeanField pField)
	{
		// for singular, non-branch selected values, put them in cache
		if (!hasFieldValueBeenCached(pField))
		{
			// putFieldValueInCache( pField, getFieldJsonNode( pField, new int[]
			// {} ) );
			putFieldValueInCache(pField,
					getFieldJsonNodeWithoutBranching(pField));
		}

		return getFieldValueFromCache(pField);
	}

	protected JsonNode getFieldJsonNode(DynamicBeanField pField,
			int... pNodeBranchSelector)
	{
		LinkedList<Integer> tNodeBranchSelectorList = new LinkedList<Integer>();

		for (int tNodeBranchIdx : pNodeBranchSelector)
		{
			tNodeBranchSelectorList.add(tNodeBranchIdx);
		}

		return getFieldJsonNode(mBeanRootNode, pField.getFieldName(),
				tNodeBranchSelectorList);
	}

	private JsonNode getFieldJsonNodeWithoutBranching(DynamicBeanField pField)
	{
		List<JsonNode> tFoundNodesSoFar = new ArrayList<JsonNode>();
		findFieldJsonNodeWithoutBranching(getRootObjectNode(), pField,
				tFoundNodesSoFar);

		// BKR1System.out.println( "Found node so far = " + tFoundNodesSoFar );

		if (tFoundNodesSoFar.isEmpty())
		{
			return null;
		} else if (tFoundNodesSoFar.size() == 1)
		{
			return tFoundNodesSoFar.get(0);
		} else
		{
			List<JsonNode> tMergedFoundNodeSoFar = new ArrayList<JsonNode>();

			for (JsonNode tFoundNode : tFoundNodesSoFar)
			{
				if (!tFoundNode.isArray())
				{
					tMergedFoundNodeSoFar.add(tFoundNode);
				} else
				{
					// FIXME
					// tMergedFoundNodeSoFar.addAll(Lists.newArrayList(tFoundNode.elements()));
				}
			}

			return JsonNodeFactory.instance.arrayNode().addAll(
					tMergedFoundNodeSoFar);
		}
	}

	private void findFieldJsonNodeWithoutBranching(JsonNode pParentNode,
			DynamicBeanField pField, List<JsonNode> pFoundNodeSoFar)
	{
//		String tFieldPath = pField.getFieldName();
		List<String> tFieldSubPaths = new ArrayList<String>();
		// FIXME
		// Lists.newArrayList(Splitter.on(cSeparatorCharacter).trimResults().split(tFieldPath));

		// BKR1System.out.println( "Parent node = " + pParentNode );
		// BKR1System.out.println( "Field sub paths = " + tFieldSubPaths );

		if (tFieldSubPaths.size() == 1)
		{
			String tFieldSubPath = tFieldSubPaths.get(0);
			JsonNode tFoundSubNode = pParentNode.get(tFieldSubPath); // .findValues(
																		// tFieldSubPath
																		// );

			if (tFoundSubNode == null)
			{
				return;
			}
			// else if ( tFoundSubNode.isArray() )
			// {
			// Iterator<JsonNode> tArrayNodeElements =
			// tFoundSubNode.getElements();
			//
			// while ( tArrayNodeElements.hasNext() )
			// {
			// pFoundNodeSoFar.add( tArrayNodeElements.next() );
			// }
			// }
			else
			{
				pFoundNodeSoFar.add(tFoundSubNode);
			}

			// pFoundNodeSoFar.addAll( tFoundSubNodes );
		} else if (tFieldSubPaths.size() > 1)
		{
			String tFieldSubPath = tFieldSubPaths.get(0);
			String tNewSubPath = "";// FIXME
									// Joiner.on(cSeparatorCharacter).join(
									// tFieldSubPaths.subList(1,
									// tFieldSubPaths.size()));

			JsonNode tFoundSubNode = pParentNode.get(tFieldSubPath);

			if (tFoundSubNode == null)
			{
				return;
			} else if (tFoundSubNode.isArray())
			{
				Iterator<JsonNode> tArrayNodeElements = tFoundSubNode
						.elements();

				while (tArrayNodeElements.hasNext())
				{
					findFieldJsonNodeWithoutBranching(
							tArrayNodeElements.next(),
							DealField.useCustomField(tNewSubPath),
							pFoundNodeSoFar);
				}

			} else
			{
				findFieldJsonNodeWithoutBranching(tFoundSubNode,
						DealField.useCustomField(tNewSubPath), pFoundNodeSoFar);
			}
			// BKR1System.out.println("Found sub-nodes = " + tFoundSubNodes);
			//
			//
			//
			// for ( JsonNode tFoundSubNode : tFoundSubNodes )
			// {
			// findFieldJsonNodeWithoutBranching( tFoundSubNode,
			// DealField.useCustomField( tNewSubPath ),
			// pFoundNodeSoFar );
			// }
		}

		// pFieldPath.split( cSeparatorCharacter );
		//
		// int tSeparatorPositionInFieldPath = pFieldPath.indexOf(
		// cSeparatorCodePoint );
		//
		// if ( tSeparatorPositionInFieldPath == -1 )
		// {
		// JsonNode tChildNode = pParentNode.get( pFieldPath );
		//
		// if ( tChildNode == null ) return null; // child not found
		//
		// // if the child node is an array and the branch selector
		// // is not empty (if it's empty, then we return the array node)
		// if ( tChildNode.isArray() && !pNodeBranchSelector.isEmpty() )
		// {
		// int tSelectorIdx = pNodeBranchSelector.removeFirst();
		//
		// return ( (ArrayNode) tChildNode ).get( tSelectorIdx );
		// }
		//
		// return pParentNode.get( pFieldPath );
		// }
		//
		// // else
		// String tCurrentNodeStringPath = pFieldPath.substring( 0,
		// tSeparatorPositionInFieldPath );
		//
		// JsonNode tChildNode = pParentNode.get( tCurrentNodeStringPath );
		//
		// if ( tChildNode != null && tChildNode.isArray() )
		// {
		// int tSelectorIdx = 0; // default value if no branching is defined
		//
		// // override with defined value if the selector list is not empty
		// if ( !pNodeBranchSelector.isEmpty() ) tSelectorIdx =
		// pNodeBranchSelector.removeFirst();
		//
		// tChildNode = ( (ContainerNode) tChildNode ).get( tSelectorIdx );
		// }
		//
		// return getFieldJsonNode( tChildNode,
		// pFieldPath.substring( tSeparatorPositionInFieldPath + 1,
		// pFieldPath.length() ),
		// pNodeBranchSelector );
	}

	protected JsonNode getFieldJsonNode(JsonNode pParentNode,
			String pFieldPath, LinkedList<Integer> pNodeBranchSelector)
	{
		if (pParentNode == null)
			return null;

		if (pFieldPath == null)
			throw new IllegalArgumentException(
					"Input field path string is null.");

		if (pNodeBranchSelector == null)
			throw new IllegalArgumentException("Node branch selector is null.");

		int tSeparatorPositionInFieldPath = pFieldPath
				.indexOf(cSeparatorCodePoint);

		if (tSeparatorPositionInFieldPath == -1)
		{
			JsonNode tChildNode = pParentNode.get(pFieldPath);

			if (tChildNode == null)
				return null; // child not found

			// if the child node is an array and the branch selector
			// is not empty (if it's empty, then we return the array node)
			if (tChildNode.isArray() && !pNodeBranchSelector.isEmpty())
			{
				int tSelectorIdx = pNodeBranchSelector.removeFirst();

				return ((ArrayNode) tChildNode).get(tSelectorIdx);
			}

			return pParentNode.get(pFieldPath);
		}

		// else
		String tCurrentNodeStringPath = pFieldPath.substring(0,
				tSeparatorPositionInFieldPath);

		JsonNode tChildNode = pParentNode.get(tCurrentNodeStringPath);

		if (tChildNode != null && tChildNode.isArray())
		{
			int tSelectorIdx = 0; // default value if no branching is defined

			// override with defined value if the selector list is not empty
			if (!pNodeBranchSelector.isEmpty())
				tSelectorIdx = pNodeBranchSelector.removeFirst();

			tChildNode = ((ContainerNode<?>) tChildNode).get(tSelectorIdx);
		}

		return getFieldJsonNode(tChildNode, pFieldPath.substring(
				tSeparatorPositionInFieldPath + 1, pFieldPath.length()),
				pNodeBranchSelector);
	}

	protected Long getFieldLongValue(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		return getObjectValueFromJsonNode(tFieldNode, Long.class);
	}

	protected <T> T getFieldObjectValue(DynamicBeanField pField,
			Class<T> pValueClass)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		// We return null if the node is a literal null.
		if (tFieldNode.isNull())
		{
			return null;
		}

		// /*BKR3System.out.println( "Field Node type is " +
		// tFieldNode.getClass().getName() );BKR3*/

		T tObjectValue = getObjectValueFromJsonNode(tFieldNode, pValueClass);
		if (tFieldNode.isObject())
		{
			setFieldObjectValue(pField, tObjectValue);
		}

		return tObjectValue;
	}

	protected String getFieldStringValue(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null)
		{
			checkForNullValue(tFieldNode, pField); // throws exception if
													// we're set for throwing
													// exceptions
			return null;
		}

		resetShouldThrowExceptionForNullValues();

		if (tFieldNode.isNull())
			return null;

		// BKR2System.out.println( "Node value is " + tFieldNode );

		return getObjectValueFromJsonNode(tFieldNode, String.class);
	}

	protected JsonNode getFieldValueFromCache(DynamicBeanField pField)
	{
		return mCacheMap.get(pField);
	}

	private JsonTypeInfo getJsonTypeInfoAnnotation()
	{
		Class<?> tInspectedClass = this.getClass();
		do
		{
			JsonTypeInfo tCheckedAnnotation = tInspectedClass
					.getAnnotation(JsonTypeInfo.class);
			if (tCheckedAnnotation != null)
			{
				return tCheckedAnnotation;
			}
			tInspectedClass = tInspectedClass.getSuperclass();
		} while (tInspectedClass != null);

		return null;
	}

	private String getJsonTypePropertyString()
	{
		Class<?> tInspectedClass = this.getClass();

		do
		{
			JsonTypeInfo tJsonTypeInfoAnnotation = tInspectedClass
					.getAnnotation(JsonTypeInfo.class);
			if (tJsonTypeInfoAnnotation != null)
			{
				return tJsonTypeInfoAnnotation.property();
			}

			tInspectedClass = tInspectedClass.getSuperclass();
		} while (tInspectedClass != null);

		return null;
	}

	@SuppressWarnings("unchecked")
	private <T> T getObjectValueFromJsonNode(JsonNode tFieldNode,
			Class<T> pValueClass)
	{
		// initial check for primitives
		if (Boolean.class.isAssignableFrom(pValueClass))
		{
			return (T) (Boolean) tFieldNode.asBoolean();
		} else if (Integer.class.equals(pValueClass))
		{
			return (T) (Integer) tFieldNode.asInt();
		} else if (Long.class.equals(pValueClass))
		{
			return (T) (Long) tFieldNode.asLong();
		} else if (Double.class.equals(pValueClass))
		{
			return (T) (Double) tFieldNode.asDouble();
		} else if (String.class.equals(pValueClass))
		{
			return (T) (String) tFieldNode.asText();
		} else if (tFieldNode.isObject()) // serialized objects, deserialize it
											// with the mapper
		{
			try
			{
				T tDeserializedObject = mJsonMapper.convertValue(tFieldNode,
						pValueClass);

				// after we deserialize it, store it as a POJO object, so that
				// future access would return same object using the isPojo()
				// check below, instead of re-deserializing it from String as
				// per this scope.

				// getRootObjectNode().findParent( tFieldNode. )

				return tDeserializedObject;
			} catch (Exception ex)
			{
				LOG.warn("Cannot parse Json value " + tFieldNode.toString()
						+ " as class " + pValueClass.getName() + ".", ex);
				return null;
			}
		} else if (tFieldNode.isPojo()) // stored object that is not yet
										// serialized
		{
			Object tPojoValue = ((POJONode) tFieldNode).getPojo();

			if (tPojoValue == null)
				return null;

			if (pValueClass.isAssignableFrom(tPojoValue.getClass()))
			{
				return (T) tPojoValue;
			} else
			{
				LOG.warn("Cannot get value as class " + pValueClass.getName()
						+ ", stored object is of class "
						+ tPojoValue.getClass().getName() + ".");
				return null;
			}
		} else if (tFieldNode.isTextual() && pValueClass.isEnum()) // enums are
																	// stored as
																	// strings
		{
			T[] tEnumValues = pValueClass.getEnumConstants();
			String tObjectText = tFieldNode.asText();

			for (T tEnumValue : tEnumValues)
			{
				if (((Enum<?>) tEnumValue).name().equalsIgnoreCase(tObjectText))
				{
					return tEnumValue;
				}
			}
			return null;
		} else if ((tFieldNode.isBinary() || tFieldNode.isTextual())
				&& byte[].class.isAssignableFrom(pValueClass))
		{
			try
			{
				return (T) tFieldNode.binaryValue();
			} catch (IOException ex)
			{
				LOG.warn(
						"Cannot deserialize field node into binary value, returning null value instead.",
						ex);
				return null;
			}
		} else if (tFieldNode.isTextual()) // last attempt, try to deserialize
											// the string content to objects
		{
			try
			{
				T tDeserializedObject = mJsonMapper.readValue(
						tFieldNode.asText(), pValueClass);
				return tDeserializedObject;
			} catch (IOException ex)
			{
				LOG.warn("Cannot parse Json value " + tFieldNode.toString()
						+ " as class " + pValueClass.getName() + ".", ex);
				return null;
			}
		} else
		{
			return null;
		}
	}

	private ArrayNode getOrCreateFieldArrayNode(DynamicBeanField pField)
	{
		JsonNode tFieldNode = getFieldJsonNode(pField);

		if (tFieldNode == null || (tFieldNode != null && !tFieldNode.isArray()))
		{
			invalidateCache(pField);

			// create/overwrite with new ArrayNode
			return getRootObjectNode().putArray(pField.getFieldName());
		} else
		{
			return (ArrayNode) tFieldNode;
		}
	}

	protected ObjectNode getRootObjectNode()
	{
		return (ObjectNode) mBeanRootNode;
	}

	@Override
	public boolean hasCustomDeserializer()
	{
		return false;
	}

	protected boolean hasField(DynamicBeanField pField)
	{
		// /*BKR3System.out.println( "getFieldJsonNode( pField ) = " +
		// getFieldJsonNode( pField ) );BKR3*/
		// /*BKR3System.out.println( "getFieldJsonNode( pField ) class = " +
		// getFieldJsonNode( pField ) == null ? null : getFieldJsonNode( pField
		// ).getClass().getName() );BKR3*/

		return getFieldJsonNode(pField) != null;
	}

	protected boolean hasFieldValueBeenCached(DynamicBeanField pField)
	{
		return mCacheMap.containsKey(pField);
	}

	protected void invalidateCache(DynamicBeanField pField)
	{
		mCacheMap.remove(pField);
		for (Map<DynamicBeanField, List<?>> tArrayCacheMap : mArrayCacheMaps
				.values())
		{
			tArrayCacheMap.remove(pField);
		}
	}

	protected boolean isFieldNull(DynamicBeanField pCheckedField)
	{
		if (!hasField(pCheckedField))
			return true;

		JsonNode tCheckedJsonNode = getFieldJsonNode(pCheckedField);

		if (tCheckedJsonNode.isPojo())
		{
			return ((POJONode) tCheckedJsonNode).getPojo() == null;
		} else
		{
			return getFieldJsonNode(pCheckedField).isNull();
		}
	}

	protected boolean isFieldNumeric(DynamicBeanField pCheckedField)
	{
		if (!hasField(pCheckedField))
			return false;

		JsonNode tCheckedJsonNode = getFieldJsonNode(pCheckedField);

		if (tCheckedJsonNode.isPojo())
		{
			return false;// FIXME ServerUtils.isObjectNumeric(((POJONode)
							// tCheckedJsonNode).getPojo());
		} else
		{
			return getFieldJsonNode(pCheckedField).isNumber();
		}
	}

	protected void putFieldValueInCache(DynamicBeanField pField,
			JsonNode pJsonNode)
	{
		mCacheMap.put(pField, pJsonNode);
	}

	@Override
	public void readExternal(ObjectInput in) throws IOException,
			ClassNotFoundException
	{
		useJSONString(in.readUTF());
	}

	protected void resetShouldThrowExceptionForNullValues()
	{
		mShouldThrowExceptionForNullValues = false;
	}

	@Override
	public void serialize(JsonGenerator jgen, SerializerProvider provider)
			throws IOException, JsonProcessingException
	{
		serializeWithType(jgen, provider, null);
	}

	@Override
	public void serializeWithType(JsonGenerator jgen,
			SerializerProvider provider, TypeSerializer typeSer)
			throws IOException, JsonProcessingException
	{
		// check if this class have JsonTypeInfo annotation
		JsonTypeInfo tJsonTypeInfoAnnotation = getJsonTypeInfoAnnotation();
		if (tJsonTypeInfoAnnotation != null)
		{
			setFieldValue(DealField.useCustomField(tJsonTypeInfoAnnotation
					.property()), this.getClass().getName());
			jgen.writeTree(mBeanRootNode);
			deleteField(DealField.useCustomField(tJsonTypeInfoAnnotation
					.property()));
		} else
		{
			jgen.writeTree(mBeanRootNode);
		}
	}

	protected void setFieldObjectValue(DynamicBeanField pField,
			Object pFieldValue)
	{
		if (pFieldValue == null)
		{
			getRootObjectNode().putNull(pField.getFieldName());
		} else if (Long.class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (Long) pFieldValue);
		} else if (Integer.class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (Integer) pFieldValue);
		} else if (Double.class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (Double) pFieldValue);
		} else if (String.class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (String) pFieldValue);
		} else if (Boolean.class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (Boolean) pFieldValue);
		} else if (byte[].class.isAssignableFrom(pFieldValue.getClass()))
		{
			setFieldValue(pField, (byte[]) pFieldValue);
		} else
		{
			getRootObjectNode().putPOJO(pField.getFieldName(), pFieldValue);
		}

		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, byte[] pFieldValue)
	{
		getRootObjectNode().put(pField.getFieldName(), pFieldValue);
		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, Boolean pFieldValue)
	{
		getRootObjectNode().put(pField.getFieldName(), pFieldValue);
		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, Double pFieldValue)
	{
		getRootObjectNode().put(pField.getFieldName(), pFieldValue);
		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, Integer pFieldValue)
	{
		getRootObjectNode().put(pField.getFieldName(), pFieldValue);
		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, Long pFieldValue)
	{
		// /*BKR3System.out.println( "Storing long value of " + pFieldValue +
		// " to field " + pField.getFieldName() + "." );BKR3*/

		getRootObjectNode().put(pField.getFieldName(), pFieldValue);

		// /*BKR3System.out.println( "Getting long value of " +
		// getRootObjectNode().get( pField.getFieldName() ).asLong() +
		// " from field " + pField.getFieldName() + "." );BKR3*/

		// after setting, we need to invalidate the internal cache map so that
		// the next fetch would take the data from the new node path
		// instead of the in-memory cached value
		invalidateCache(pField);
	}

	protected void setFieldValue(DynamicBeanField pField, String pFieldValue)
	{
		getRootObjectNode().put(pField.getFieldName(), pFieldValue);
		invalidateCache(pField);
	}

	protected boolean shouldThrowExceptionForNullValues()
	{
		return mShouldThrowExceptionForNullValues;
	}

	public String toClasslessJsonString()
	{
		String tClassPropertyString = getJsonTypePropertyString();

		if (tClassPropertyString == null)
			return toJsonString();

		try
		{
			String tJsonString = toJsonString();
			ObjectNode tJsonNode = (ObjectNode) mJsonMapper
					.readTree(tJsonString);
			tJsonNode.remove(tClassPropertyString);

			return mJsonMapper.writeValueAsString(tJsonNode);
		} catch (IOException ex)
		{
			LOG.warn("", ex);
			return null;
		}
	}

	@Override
	public String toCustomSerializedString()
	{
		throw new UnsupportedOperationException("Class " + this.getClass()
				+ " does not support custom serializer."); // should
															// be
															// overriden
															// by
															// inheriting
															// class
	}

	public Map<String, String> toFlatStringMap()
	{
		Map<String, String> tFlattenedBeanMap = new HashMap<String, String>();
		Iterator<Entry<String, JsonNode>> tBeanFieldIterator = getRootObjectNode()
				.fields();

		while (tBeanFieldIterator.hasNext())
		{
			Entry<String, JsonNode> tBeanFieldEntry = tBeanFieldIterator.next();
			tFlattenedBeanMap.put(tBeanFieldEntry.getKey(), tBeanFieldEntry
					.getValue().toString());
		}

		return tFlattenedBeanMap;
	}

	public String toJsonString()
	{
		if (mBeanRootNode == null)
			return null;
		if (mJsonMapper == null)
			return null;

		try
		{
			return mJsonMapper.writeValueAsString(this);
		} catch (Exception ex)
		{
			LOG.warn("", ex);
			return null;
		}
	}

	@Override
	public DynamicBean useCustomDeserializer(String pSerializedString)
	{
		throw new UnsupportedOperationException("Class " + this.getClass()
				+ " does not support custom deserializer."); // should
																// be
																// overriden
																// by
																// inheriting
																// class
	}

	public DynamicBean useJSONNode(JsonNode pJSONNode)
	{
		mBeanRootNode = pJSONNode;
		return this;
	}

	public DynamicBean useJSONString(String pJSONString) throws IOException
	{
		if (pJSONString != null)
		{
			mBeanRootNode = mJsonMapper.readTree(pJSONString);
		}
		return this;
	}

	public DynamicBean useStringHashMap(Map<String, String> pStringHashMap)
	{
		mBeanRootNode = JsonNodeFactory.instance.objectNode();

		for (String tMapKey : pStringHashMap.keySet())
		{
			((ObjectNode) mBeanRootNode).put(tMapKey,
					pStringHashMap.get(tMapKey));
		}

		return this;
	}

	protected DynamicBean withNullCheck()
	{
		mShouldThrowExceptionForNullValues = true;
		return this;
	}

	@Override
	public void writeExternal(ObjectOutput out) throws IOException
	{
		out.writeUTF(toJsonString());
	}

	protected Map<String, JsonNode> getRootChildNodes()
	{
		Map<String, JsonNode> tReturnMap = new LinkedHashMap<String, JsonNode>();
		if (getRootObjectNode() == null)
			return tReturnMap;

		Iterator<Entry<String, JsonNode>> tRootChildNodesIterator = getRootObjectNode()
				.fields();

		while (tRootChildNodesIterator.hasNext())
		{
			Entry<String, JsonNode> tRootChildNodeEntry = tRootChildNodesIterator
					.next();
			tReturnMap.put(tRootChildNodeEntry.getKey(),
					tRootChildNodeEntry.getValue());
		}

		return tReturnMap;
	}
}
