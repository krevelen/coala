/* $Id: 54bbae10ba4d6e5d83e152c6dec87d93e99450dc $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/identity/AbstractIdentifier.java $
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

import io.coala.json.JSONConvertible;
import io.coala.json.JsonUtil;

import java.io.Serializable;

import javax.inject.Inject;

/**
 * {@link AbstractIdentifier} contains some identifier content type
 * 
 * @date $Date: 2014-07-10 15:06:54 +0200 (Thu, 10 Jul 2014) $
 * @version $Revision: 327 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T> the type of the {@link Comparable} and {@link Serializable}
 *        value/content for this {@link AbstractIdentifier}
 * @param <THIS> the type of {@link AbstractIdentifier} to compare with
 */
public abstract class AbstractIdentifier<T extends Comparable<T> & Serializable>
		implements Identifier<T, AbstractIdentifier<T>>,
		JSONConvertible<AbstractIdentifier<T>>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** the identifier value */
	private T value = null;

	/**
	 * {@link AbstractIdentifier} zero-arg bean constructor
	 */
	protected AbstractIdentifier()
	{
		//
	}

	/**
	 * {@link AbstractIdentifier} constructor
	 * 
	 * @param value the (unique) {@link T} value of this
	 *        {@link AbstractIdentifier} object
	 */
	@Inject
	public AbstractIdentifier(final T value)
	{
		setValue(value);
	}

	/**
	 * @param value the (unique) {@link T} value of this
	 *        {@link AbstractIdentifier} object
	 */
	protected void setValue(final T value)
	{
		this.value = value;
	}

	/** @return the identifier value of this {@link AbstractIdentifier} object */
	public T getValue()
	{
		return this.value;
	}

//	/**
//	 * @param value the (unique) {@link T} value of this
//	 *        {@link AbstractIdentifier} object
//	 * @return this {@link AbstractIdentifier} object
//	 */
//	@SuppressWarnings("unchecked")
//	protected THIS withValue(final T value)
//	{
//		setValue(value);
//		return (THIS) this;
//	}

	/** @see Comparable#compareTo(Object) */
	@Override
	public int compareTo(final AbstractIdentifier<T> other)
	{
		return getValue().compareTo(other.getValue());
	}

	/** @see Object#toString() */
	@Override
	public String toString()
	{
		return getValue().toString();
	}

	/** @see Object#hashCode() */
	@Override
	public int hashCode()
	{
		return getValue() == null ? super.hashCode() : getValue().hashCode();
	}

	/** @see Object#equals(Object) */
	@Override
	public boolean equals(final Object other)
	{
		if (other == null || other.getClass() != getClass())
			return false;

		@SuppressWarnings("unchecked")
		final AbstractIdentifier<T> that = (AbstractIdentifier<T>) other;
		return getValue().equals(that.getValue());
	}

	/** @see JSONConvertible#toJSON() */
	@Override
	public String toJSON()
	{
		return JsonUtil.toJSONString(this);
	}

	/** @see JSONConvertible#fromJSON(String) */
	@SuppressWarnings("unchecked")
	@Override
	public AbstractIdentifier<T> fromJSON(final String jsonValue)
	{
		return (AbstractIdentifier<T>) JsonUtil.fromJSONString(jsonValue,
				getClass());
	}

}
