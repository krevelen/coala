/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/BasicCell.java $
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

import io.coala.agent.BasicAgent;
import io.coala.bind.Binder;
import io.coala.log.InjectLogger;
import io.coala.time.SimTime;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * {@link BasicCell}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class BasicCell extends BasicAgent implements
		Cell, Observer<CellStateTransition>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private transient Logger LOG;

	/** */
	private transient final Subject<CellState, CellState> states = ReplaySubject
			.create();

	/**
	 * {@link BasicCell} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private BasicCell(final Binder binder)
	{
		super(binder);
	}
	
	/** @see BasicAgent#initialize() */
	@Override
	public void initialize() throws Exception
	{
		super.initialize();
		
		// handle state transition percepts
		getWorld().perceiveTransitions().subscribe(this);
	}

	/**
	 * @param cell
	 * @return
	 */
	protected CellWorld getWorld()
	{
		return getBinder().inject(CellWorld.class);
	}

	/**
	 * @param state
	 */
	protected synchronized void setState(final CellState state)
	{
		this.states.onNext(state);
	}

	/** @see Cell#getState() */
	@Override
	public synchronized Observable<CellState> getState(final SimTime time)
	{
		return this.states.filter(new Func1<CellState, Boolean>()
		{
			@Override
			public Boolean call(final CellState state)
			{
				return state.getTime().compareTo(time) >= 0;
			}
		}).first(); // return the first state for that matches this filter
	}

	/** @see Observer#onError(Throwable) */
	@Override
	public void onError(final Throwable e)
	{
		e.printStackTrace();
	}

	/** @see Observer#onNext(Object) */
	@Override
	public void onNext(final CellStateTransition transition)
	{
		LOG.trace("Perceived transition: " + transition.toString());
		final CellState toState = transition.getToState();
		setState(toState);

		// next cycle
		getWorld().perceiveLinks().subscribe(
				new CellLinkPerceptObserver(this, toState));
	}

	/** @see Observer#onCompleted() */
	@Override
	public void onCompleted()
	{
		LOG.info("Simulation complete!");
	}
}
