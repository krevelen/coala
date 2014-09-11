/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/xml/XmlContextID.java $
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
package io.coala.xml;

import io.coala.name.AbstractIdentifier;

import javax.xml.bind.ValidationEventHandler;

/**
 * {@link XmlContextID}
 * 
 * @version $Revision: 349 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class XmlContextID<T> extends AbstractIdentifier<String>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private final Class<T> objectFactoryType;

	/** */
	private final Class<?>[] objectTypes;

	/**
	 * {@link XmlContextIDImpl} constructor
	 * 
	 * @param objectFactoryType the type of ObjectFactory for this context (if
	 *            any, {@code null} otherwise)
	 * @param objectTypes the element object types to include in the context (if
	 *            any)
	 */
	protected XmlContextID(final Class<T> objectFactoryType,
			final Class<?>... objectTypes)
	{
		super(objectFactoryType.getCanonicalName());
		this.objectFactoryType = objectFactoryType;
		this.objectTypes = objectTypes;
	}

	/**
	 * @return the type of ObjectFactory
	 */
	public Class<T> getObjectFactoryType()
	{
		return this.objectFactoryType;
	}

	/**
	 * @return the object types in this context
	 */
	public Class<?>[] getObjectTypes()
	{
		return this.objectTypes;
	}

	/**
	 * @param validationEventHandler the {@link ValidationEventHandler} to use
	 * @return the new {@link XmlContext} instance
	 */
	public XmlContext<T> newContextCacheEntry(
			final ValidationEventHandler validationEventHandler)
	{
		return XmlContext.of(this, validationEventHandler);
	}

	/**
	 * @param clazz the type of ObjectFactory
	 * @return the new {@link XmlContextID}
	 */
	public static <T> XmlContextID<T> of(final Class<T> clazz,
			final Class<?>... objectTypes)
	{
		return new XmlContextID<T>(clazz, objectTypes);
	}
}
