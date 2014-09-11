/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellStateTransition.java $
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
package io.coala.example.conway;

import io.coala.event.AbstractTimedEvent;
import io.coala.model.ModelComponent;
import io.coala.time.Instant;
import io.coala.time.SimTime;

/**
 * {@link CellStateTransition}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class CellStateTransition extends
		AbstractTimedEvent<CellStateTransitionID>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private final CellState fromState;

	/** */
	private final CellState toState;

	/**
	 * {@link CellStateTransition} constructor
	 * 
	 * @param id
	 * @param producerID
	 */
	public CellStateTransition(final CellState fromState, final SimTime time,
			final LifeState toState)
	{
		this(fromState, new CellState(time, fromState.getCellID(), toState));
	}

	/**
	 * {@link CellStateTransition} constructor
	 * 
	 * @param id
	 * @param producerID
	 */
	public CellStateTransition(final CellState fromState,
			final CellState toState)
	{
		super(new CellStateTransitionID(toState.getCellID().getModelID(),
				toState.getGeneration()), toState.getCellID(), toState
				.getCellID());

		// sanity check
		if (fromState != null
				&& !fromState.getCellID().equals(toState.getCellID()))
			throw new IllegalArgumentException("CellIDs must be equal: "
					+ fromState.getCellID() + " =/=" + toState.getCellID());

		this.fromState = fromState;
		this.toState = toState;
	}

	/**
	 * @return the {@link CellState} which the {@link Cell} transitioned from,
	 *         or {@code null} if initial transition}
	 */
	public CellState getFromState()
	{
		return this.fromState;
	}

	/**
	 * @return the {@link CellState} which the {@link Cell} transitioned to
	 */
	public CellState getToState()
	{
		return this.toState;
	}

	@Override
	public String toString()
	{
		return String.format("%d %s %s %s -> %s", getID().getValue().getTime(),
				getToState().getCellID(), getToState().getTime().getValue(),
				getFromState() == null ? "?" : getFromState().getState(),
				getToState().getState());
	}

	/** @see ModelComponent#getTime() */
	@Override
	public Instant<?> getTime()
	{
		return getID().getTime();
	}

}
