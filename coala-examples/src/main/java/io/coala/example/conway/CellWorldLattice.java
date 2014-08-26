/* $Id: CellWorldLattice.java 312 2014-06-20 10:27:58Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellWorldLattice.java $
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

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.exception.CoalaException;
import io.coala.log.InjectLogger;
import io.coala.model.ModelID;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.functions.Action1;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link CellWorldLattice}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class CellWorldLattice extends BasicCapability implements CellWorld
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private transient Logger LOG;

	/** */
	private static final Map<ModelID, List<List<CellID>>> CELL_IDS = Collections
			.synchronizedMap(new HashMap<ModelID, List<List<CellID>>>());

	/** */
	private static final Subject<CellStateTransition, CellStateTransition> GLOBAL_TRANSITIONS = ReplaySubject
			.create();

	/** */
	private final Subject<CellStateTransition, CellStateTransition> transitions = ReplaySubject
			.create();

	/** */
	private final Collection<CellLinkPercept> neighborPercepts;

	/** */
	private final SimTime cycleDuration;

	private static final Map<ModelID, List<Map<CellID, LifeState>>> INITIAL_STATES = Collections
			.synchronizedMap(new HashMap<ModelID, List<Map<CellID, LifeState>>>());

	@JsonIgnore
	public synchronized List<Map<CellID, LifeState>> getInitialStates()
			throws CoalaException
	{
		final ModelID modelID = getID().getModelID();
		List<Map<CellID, LifeState>> result = INITIAL_STATES.get(modelID);
		if (result != null)
			return result;

		result = new ArrayList<Map<CellID, LifeState>>();
		INITIAL_STATES.put(modelID, Collections.unmodifiableList(result));

		final int[][] initialStates = getProperty("initialStates").getJSON(
				int[][].class);

		for (List<CellID> row : generateLatticeCellIDs(modelID,
				initialStates.length, initialStates[0].length))
		{
			final Map<CellID, LifeState> rowStates = new HashMap<CellID, LifeState>();
			result.add(rowStates);

			for (CellID cellID : row)
				rowStates.put(cellID, initialStates[cellID.getRow()][cellID
						.getCol()] == 0 ? LifeState.DEAD : LifeState.ALIVE);
		}
		return result;
	}

	/**
	 * {@link CellWorldLattice} constructor
	 * 
	 * @param host
	 * @throws CoalaException
	 */
	@Inject
	public CellWorldLattice(final Binder binder) throws CoalaException
	{
		super(binder);

		final SimTimeFactory simTimeFactory = getBinder().inject(
				SimTimeFactory.class);

		final SimTime startCycle = simTimeFactory.create(0, TimeUnit.TICKS);

		this.cycleDuration = simTimeFactory.create(getProperty("cycleDuration")
				.getNumber(), TimeUnit.TICKS);

		// this lattice has fixed neighbor links
		final List<Map<CellID, LifeState>> initialStates = getInitialStates();
		final Collection<CellLinkPercept> neighborPercepts = new ArrayList<CellLinkPercept>();
		this.neighborPercepts = Collections
				.unmodifiableCollection(neighborPercepts);

		this.transitions.subscribe(GLOBAL_TRANSITIONS);

		if (getID().getClientID() instanceof CellID)
		{
			final CellID myID = (CellID) getID().getClientID();
			final LifeState startState = getInitialStates().get(myID.getRow())
					.get(myID);

			for (CellID cellID : getNeighborIDs(myID, initialStates.size(),
					initialStates.get(0).size()))
				neighborPercepts.add(new CellLinkPerceptImpl(cellID,
						CellLinkPerceptType.CONNECTED));

			final CellStateTransition initialTransition = new CellStateTransition(
					null, new CellState(startCycle, myID, startState));
			this.transitions.onNext(initialTransition);
			System.err.println("Initial transition: " + initialTransition);
		}
	}

	/** @see CellWorld#perceiveNeighbors() */
	@Override
	public Observable<CellLinkPercept> perceiveLinks()
	{
		return Observable.from(this.neighborPercepts);
	}

	/** @see Cell#getStates() */
	@Override
	public Observable<CellStateTransition> perceiveTransitions()
	{
		return this.transitions.asObservable();
	}

	/** @see CellWorld#transition(CellState, LifeState) */
	@Override
	public void performTransition(final LifeState toState) throws Exception
	{
		this.transitions.last().subscribe(new Action1<CellStateTransition>()
		{
			@Override
			public void call(final CellStateTransition lastTransition)
			{
				final CellState fromState = lastTransition.getToState();
				final SimTime toTime = fromState.getTime().plus(cycleDuration);
				final CellStateTransition transition = new CellStateTransition(
						fromState, toTime, toState);
				System.err.println("New transition: " + transition);
				transitions.onNext(transition);
			}
		});
	}

	/**
	 * @param agentIDFactory
	 * @param rows
	 * @param cols
	 * @return
	 */
	protected static List<List<CellID>> generateLatticeCellIDs(
			final ModelID modelID, final int rows, final int cols)
	{
		final List<List<CellID>> result = new ArrayList<List<CellID>>(rows);
		for (int row = 0; row < rows; row++)
		{
			final List<CellID> rowMap = new ArrayList<CellID>(cols);
			result.add(Collections.unmodifiableList(rowMap));

			for (int col = 0; col < cols; col++)
				rowMap.add(new CellID(modelID, row, col));
		}
		return Collections.unmodifiableList(result);
	}

	/**
	 * @param cellID
	 * @param rows
	 * @param cols
	 * @return
	 */
	protected static Collection<CellID> getNeighborIDs(final CellID cellID,
			final int rows, final int cols)
	{
		final ModelID modelID = cellID.getModelID();
		List<List<CellID>> cellIDs = CELL_IDS.get(modelID);
		if (cellIDs == null)
		{
			cellIDs = generateLatticeCellIDs(modelID, rows, cols);
			CELL_IDS.put(modelID, cellIDs);
		}

		final int row = cellID.getRow();
		final int col = cellID.getCol();
		final int north = (rows + row - 1) % rows;
		final int south = (rows + row + 1) % rows;
		final int west = (cols + col - 1) % cols;
		final int east = (cols + col + 1) % cols;
		final Collection<CellID> result = Arrays.asList(
				cellIDs.get(north).get(west), // northwest
				cellIDs.get(row).get(west), // west
				cellIDs.get(south).get(west), // south-west
				cellIDs.get(north).get(col), // north
				cellIDs.get(south).get(col), // south
				cellIDs.get(north).get(east), // northeast
				cellIDs.get(row).get(east), // east
				cellIDs.get(south).get(east)); // south-east
		return result;
	}

	/**
	 * @return
	 */
	public static Observable<CellStateTransition> getGlobalTransitions()
	{
		return GLOBAL_TRANSITIONS.asObservable();
	}

}
