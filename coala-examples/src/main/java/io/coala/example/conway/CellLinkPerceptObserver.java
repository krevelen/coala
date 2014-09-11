/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellLinkPerceptObserver.java $
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

import io.coala.capability.interact.ProxyCapability;
import io.coala.log.LogUtil;

import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import rx.Observer;
import rx.functions.Action1;

/**
 * {@link CellLinkPerceptObserver}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
class CellLinkPerceptObserver implements Observer<CellLinkPercept>
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(CellLinkPerceptObserver.class);

	/** */
	private final Map<LifeState, Integer> neighborStates = Collections
			.synchronizedMap(new EnumMap<LifeState, Integer>(LifeState.class));

	/** */
	private final CellState fromState;

	/** */
	private final BasicCell owner;

	/** */
	private boolean transitionComplete = false;

	/**
	 * {@link CellLinkPerceptObserver} constructor
	 * 
	 * @param fromState
	 */
	public CellLinkPerceptObserver(final BasicCell owner,
			final CellState fromState)
	{
		this.owner = owner;
		this.fromState = fromState;
	}

	/** @see Observer#onError(Throwable) */
	@Override
	public void onError(final Throwable t)
	{
		t.printStackTrace();
	}

	/** @see Observer#onNext(Object) */
	@Override
	public void onNext(final CellLinkPercept percept)
	{
		final CellID neighborID = percept.getNeighborID();
		switch (percept.getType())
		{
		case CONNECTED:
			observeNeighborState(neighborID);
			break;

		case DISCONNECTED:
			LOG.warn("Ignoring disconnect of neighbor: " + neighborID);
			break;

		default:
			LOG.warn("Unexpected connection percept type: " + percept.getType());
		}
	}

	/** @see Observer#onCompleted() */
	@Override
	public void onCompleted()
	{
		LOG.trace("Received all neighbor IDs, trying transition...");
	}

	/**
	 * @param neighborID
	 * @return
	 * @throws Exception
	 */
	protected Cell getNeighborProxy(final CellID neighborID) throws Exception
	{
		return this.owner.getBinder().inject(ProxyCapability.class)
				.getProxy(neighborID, Cell.class);
	}

	/**
	 * @param neighborID
	 * @throws Exception
	 */
	protected void observeNeighborState(final CellID neighborID)
	{
		final Cell neighbor;
		try
		{
			neighbor = getNeighborProxy(neighborID);
		} catch (final Exception e)
		{
			e.printStackTrace();
			return;
		}
		neighbor.getState(this.fromState.getTime()).subscribe(
				new Action1<CellState>()
				{
					@Override
					public void call(final CellState state)
					{
						final Integer count = neighborStates.get(state
								.getState());
						neighborStates.put(state.getState(), count == null ? 1
								: count + 1);
						attemptTransition();

						if (transitionComplete)
							LOG.warn("Unsubscribe this observer!");
					}
				});
	}

	/**
	 * 
	 */
	protected void attemptTransition()
	{
		if (this.transitionComplete)
			return;

		final List<LifeState> options = this.fromState.getState()
				.getTransitionOptions(this.neighborStates);

		// sanity check
		if (options.size() != 1)
			return;

		try
		{
			this.owner.getWorld().performTransition(options.get(0));
			this.transitionComplete = true;
		} catch (final Exception e)
		{
			onError(e);
		}
	}
}