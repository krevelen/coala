/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/xml/XmlContext.java $
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

import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.factory.ClassUtil;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEventHandler;

/**
 * {@link XmlContext}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class XmlContext<T>
{

	/** */
	private final XmlContextID<?> contextID;

	/** */
	private final ValidationEventHandler validationEventHandler;

	/** */
	private JAXBContext _Context = null;

	/** */
	private T _ObjectMapper = null;

	/** */
	private Marshaller _Marshaller = null;

	/** */
	private Unmarshaller _Unmarshaller = null;

	/**
	 * {@link XmlContext} constructor
	 * 
	 * @param contextPath
	 */
	protected XmlContext(final XmlContextID<?> contextID,
			final ValidationEventHandler validationEventHandler)
	{
		this.contextID = contextID;
		this.validationEventHandler = validationEventHandler;
	}

	protected ValidationEventHandler getValidationEventHandler()
	{
		return this.validationEventHandler;
	}

	/**
	 * @return the {@link XmlContextID}
	 */
	public XmlContextID<?> getID()
	{
		return this.contextID;
	}

	/**
	 * @return the cached ObjectMapper
	 */
	@SuppressWarnings("unchecked")
	public synchronized T getObjectFactory()
	{
		if (this._ObjectMapper == null)
			try
			{
				this._ObjectMapper = (T) ClassUtil.instantiate(this.contextID
						.getObjectFactoryType());
			} catch (final CoalaException e)
			{
				throw CoalaExceptionFactory.INCONSTRUCTIBLE.createRuntime(e,
						this.contextID.getObjectFactoryType(), null);
			}

		return this._ObjectMapper;
	}

	/**
	 * @return the {@link JAXBContext}
	 */
	public synchronized JAXBContext getJAXBContext()
	{
		if (this._Context == null)
			try
			{
				this._Context = (getID().getObjectTypes() != null && getID()
						.getObjectTypes().length > 0) ? (JAXBContext) JAXBContext
						.newInstance(getID().getObjectTypes())
						: (JAXBContext) JAXBContext.newInstance(getID()
								.getObjectFactoryType().getPackage().getName());
			} catch (final JAXBException e)
			{
				throw CoalaExceptionFactory.INCONSTRUCTIBLE.createRuntime(e,
						JAXBContext.class, null);
			}

		return this._Context;
	}

	/**
	 * @return the cached {@link Marshaller}
	 */
	public synchronized Marshaller getMarshaller()
	{
		if (this._Marshaller == null)
			try
			{
				this._Marshaller = getJAXBContext().createMarshaller();
				this._Marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,
						true);
			} catch (final JAXBException e)
			{
				throw CoalaExceptionFactory.INCONSTRUCTIBLE.createRuntime(e,
						Marshaller.class, null);
			}
		return this._Marshaller;
	}

	/**
	 * @return the cached {@link Unmarshaller}
	 */
	public synchronized Unmarshaller getUnmarshaller()
	{
		if (_Unmarshaller == null)
			try
			{
				_Unmarshaller = getJAXBContext().createUnmarshaller();
				_Unmarshaller.setEventHandler(getValidationEventHandler());
			} catch (final JAXBException e)
			{
				throw CoalaExceptionFactory.INCONSTRUCTIBLE.createRuntime(e,
						Marshaller.class, null);
			}

		return _Unmarshaller;
	}

	/**
	 * @param contextID
	 * @param validationEventHandler
	 * @return the new {@link XmlContext} instance
	 */
	public static <T> XmlContext<T> of(final XmlContextID<?> contextID,
			final ValidationEventHandler validationEventHandler)
	{
		return new XmlContext<T>(contextID, validationEventHandler);
	}

}