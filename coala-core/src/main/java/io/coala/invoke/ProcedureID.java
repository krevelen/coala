/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/ProcedureID.java $
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
package io.coala.invoke;

import io.coala.model.ModelComponent;
import io.coala.model.ModelComponentID;
import io.coala.name.AbstractIdentifier;

import java.io.Serializable;

/**
 * {@link ProcedureID} FIXME describe access type (self, local, public, etc.)
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class ProcedureID<T extends Serializable & Comparable<T>> extends
		AbstractIdentifier<String>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private ModelComponentID<T> targetID;

	/**
	 * {@link ProcedureID} constructor
	 * 
	 * @param methodID
	 * @param target
	 */
	public <ID extends ModelComponentID<T>> ProcedureID(
			final String methodID, final ModelComponent<ID> target)
	{
		this(methodID, target.getID());
	}

	/**
	 * {@link ProcedureID} constructor
	 * 
	 * @param methodID
	 * @param targetID
	 */
	public ProcedureID(final String methodID,
			final ModelComponentID<T> targetID)
	{
		super(methodID);
		this.targetID = targetID;
	}

	/**
	 * {@link ProcedureID} zero-arg serializable constructor
	 */
	protected ProcedureID()
	{
		super();
	}

	/**
	 * @return the ownerID
	 */
	public ModelComponentID<T> getTargetID()
	{
		return this.targetID;
	}

	/** @see AbstractIdentifier#toString() */
	@Override
	public String toString()
	{
		return (getTargetID() == null ? "null" : getTargetID().getValue()) + "|"
				+ getValue();
	}

	/** @see AbstractIdentifier#hashCode() */
	@Override
	public int hashCode()
	{
		// FIXME apply some common strategy via Visitor design pattern

		final int prime = 31;
		int result = super.hashCode(); // ID value hash code
		result = prime * result
				+ (getTargetID() == null ? 0 : getTargetID().hashCode());
		return result;
	}

	/** @see AbstractIdentifier#equals(Object) */
	@Override
	public boolean equals(final Object other)
	{
		// FIXME apply some common strategy via Visitor design pattern

		if (this == other)
			return true;

		if (!super.equals(other) || getClass() != other.getClass())
			return false;

		@SuppressWarnings("unchecked")
		final ProcedureID<T> that = (ProcedureID<T>) other;
		if (getTargetID() == null)
		{
			if (that.getTargetID() != null)
				return false;
		} else if (!getTargetID().equals(that.getTargetID()))
			return false;

		return super.equals(other);
	}

	/** @see Comparable#compareTo(Object) */
	@Override
	public int compareTo(final AbstractIdentifier<String> other)
	{
		// FIXME apply some common strategy via Visitor design pattern

		@SuppressWarnings("unchecked")
		final int ownerIDCompare = getTargetID().compareTo(
				(ModelComponentID<T>) ((ProcedureID<T>) other).getTargetID());
		if (ownerIDCompare != 0)
			return ownerIDCompare;

		return getValue().compareTo(other.getValue());
	}

}
