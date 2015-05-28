/* $Id: 5e294a74e4c087d25cd899618010bf19544ad1bd $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/identity/AbstractIdentifiable.java $
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
package io.coala.name;

import io.coala.json.JsonUtil;
import io.coala.log.InjectLogger;

import java.io.Serializable;

import org.slf4j.Logger;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * {@link AbstractIdentifiable}
 * 
 * @date $Date: 2014-06-17 15:03:44 +0200 (Tue, 17 Jun 2014) $
 * @version $Revision: 302 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link AbstractIdentifier} value used
 * @param <THIS> the (sub)type of {@link AbstractIdentifiable} to build
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "class")
public abstract class AbstractIdentifiable<ID extends Identifier<?, ?>>
		implements Identifiable<ID>, Serializable
{

	/** */
	private static final long serialVersionUID = 1L;

	/** the injected {@link Logger} */
	@InjectLogger
	private transient Logger LOG;

	/** */
	private ID iD;

	/**
	 * {@link AbstractIdentifiable} zero-arg bean constructor
	 * 
	 * @param iD
	 */
	protected AbstractIdentifiable()
	{
	}

	/**
	 * {@link AbstractIdentifiable} constructor
	 * 
	 * @param ID
	 */
	protected AbstractIdentifiable(final ID ID)
	{
		setID(ID);
	}

	@Override
	public ID getID()
	{
		return this.iD;
	}

	/**
	 * @param iD the {@link AbstractIdentifier} identifying this
	 *        {@link Identifiable} object
	 */
	protected void setID(final ID iD)
	{
		if (this.iD == null)
			this.iD = iD;
		else
			LOG.warn("ID already set, ignoring: " + iD);
	}

	// /** @see Identifiable#withID(AbstractIdentifier) */
	// @SuppressWarnings("unchecked")
	// public THIS withID(final ID iD)
	// {
	// setID(iD);
	// return (THIS) this;
	// }

	/** @see Comparable#compareTo(Object) */
	@SuppressWarnings("unchecked")
	@Override
	public int compareTo(final Identifiable<ID> other)
	{
		return ((Comparable<ID>) getID()).compareTo(other.getID());
	}

	/** @see Object#toString() */
	@Override
	public String toString()
	{
		try
		{
			return JsonUtil.toJSONString(this);
		} catch (final Throwable t)
		{
			t.printStackTrace();
			return getClass().getName();
		}
	}

	/** @see Object#hashCode() */
	@Override
	public int hashCode()
	{
		return getID() == null ? super.hashCode() : getID().hashCode();
	}

	/** @see Object#equals(Object) */
	@Override
	public boolean equals(final Object other)
	{
		if (other == null || other.getClass() != getClass())
			return false;

		@SuppressWarnings("unchecked")
		final AbstractIdentifiable<ID> that = getClass().cast(other);
		return getID().equals(that.getID());
	}

}
