/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellState.java $
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
package io.coala.example.conway;

import io.coala.json.JsonUtil;
import io.coala.time.SimTime;
import io.coala.time.Timed;

import java.io.Serializable;

/**
 * {@link CellState}
 * 
 * @date $Date: 2014-06-17 15:03:44 +0200 (Tue, 17 Jun 2014) $
 * @version $Revision: 302 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class CellState implements Serializable, Comparable<CellState>,
		Timed<SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private SimTime generation = null;

	/** */
	private CellID cellID = null;

	/** */
	private LifeState state = null;

	/**
	 * {@link CellState} constructor
	 * 
	 * @param generation
	 * @param cellID
	 * @param lifeState
	 */
	public CellState(final SimTime generation, final CellID cellID,
			final LifeState lifeState)
	{
		this.generation = generation;
		this.cellID = cellID;
		this.state = lifeState;
	}
	
	/**
	 * {@link CellState} constructor
	 */
	protected CellState()
	{
		super();
	}

	/** @see Timed#getTime() */
	@Override
	public SimTime getTime()
	{
		return this.generation;
	}

	/**
	 * @return the generation
	 */
	public SimTime getGeneration()
	{
		return this.generation;
	}

	/**
	 * @return the cellID
	 */
	public CellID getCellID()
	{
		return this.cellID;
	}

	/**
	 * @return the lifeState
	 */
	public LifeState getState()
	{
		return this.state;
	}

	/** @see Object#hashCode() */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.cellID == null) ? 0 : this.cellID.hashCode());
		result = prime * result
				+ ((this.generation == null) ? 0 : this.generation.hashCode());
		result = prime * result
				+ ((this.state == null) ? 0 : this.state.hashCode());
		return result;
	}

	/** @see Object#equals(Object) */
	@Override
	public boolean equals(final Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;

		final CellState other = (CellState) obj;
		if (this.cellID == null)
		{
			if (other.cellID != null)
				return false;
		} else if (!this.cellID.equals(other.cellID))
			return false;
		if (this.generation == null)
		{
			if (other.generation != null)
				return false;
		} else if (!this.generation.equals(other.generation))
			return false;
		if (this.state != other.state)
			return false;
		return true;
	}

	/** @see Comparable#compareTo(Object) */
	@Override
	public int compareTo(final CellState o)
	{
		int genCompare = Double.compare(this.generation.doubleValue(),
				o.generation.doubleValue());
		if (genCompare != 0)
			return genCompare;
		int cellCompare = getCellID().compareTo(o.getCellID());
		if (cellCompare != 0)
			return cellCompare;
		int stateCompare = getState().compareTo(o.getState());
		if (stateCompare != 0)
			return stateCompare;
		return 0;
	}

	/** @see Object#toString() */
	@Override
	public String toString()
	{
		return getClass().getSimpleName() + JsonUtil.toJSONString(this);
	}

}