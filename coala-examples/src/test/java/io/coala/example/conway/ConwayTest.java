/* $Id: ConwayTest.java 312 2014-06-20 10:27:58Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/conway/ConwayTest.java $
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

import io.coala.agent.AgentStatusObserver;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.admin.CreatingCapability;
import io.coala.exception.CoalaException;
import io.coala.log.LogUtil;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import rx.functions.Action1;

/**
 * {@link ConwayTest}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
@Ignore
public class ConwayTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(ConwayTest.class);

	/** */
	private static final String CONFIG_FILE = "conway.properties";

	@BeforeClass
	public static void setupBinderFactory() throws CoalaException
	{
		CellWorldLattice.getGlobalTransitions().subscribe(
				new Action1<CellStateTransition>()
				{

					@Override
					public void call(final CellStateTransition transition)
					{
						LOG.trace("Observed transition: " + transition);
					}
				});
	}

	// @Test
	public void testBasicMethods() throws Exception
	{
		final Binder binder = BinderFactory.Builder.fromFile(CONFIG_FILE)
				.withModelName("testModel" + System.currentTimeMillis())
				.build().create("methodTester");

		final SimTimeFactory timer = binder.inject(SimTimeFactory.class);
		final SimTime t1 = timer.create(1.5, TimeUnit.TICKS);
		final SimTime t2 = timer.create(1.6, TimeUnit.TICKS);

		final CellID cellID1 = new CellID(binder.getID().getModelID(), 1, 1);
		final CellState state1 = new CellState(t1, cellID1, LifeState.ALIVE);
		LOG.trace("Created: " + state1);

		final CellState state2 = new CellState(t2, cellID1, LifeState.ALIVE);
		LOG.trace("Created: " + state2);

		final CellID cellID2 = new CellID(binder.getID().getModelID(), 1, 2);
		LOG.trace("Booted agent with id: " + cellID1);

		final CellState state3a = new CellState(t2, cellID2, LifeState.DEAD);
		final CellState state3b = new CellState(t2, cellID2, LifeState.DEAD);
		LOG.trace("Created: " + state3a);

		Assert.assertNotEquals("Hash codes should not match for " + state1
				+ " and " + state2, state1.hashCode(), state2.hashCode());
		Assert.assertNotEquals("Hash codes should not match for " + state2
				+ " and " + state3a, state2.hashCode(), state3a.hashCode());
		Assert.assertNotEquals("Hash codes should not match for " + state1
				+ " and " + state3a, state1.hashCode(), state3a.hashCode());

		Assert.assertTrue("Should be smaller", state1.compareTo(state2) < 0);
		Assert.assertTrue("Should be smaller", state2.compareTo(state3a) < 0);
		Assert.assertTrue("Should be smaller", state1.compareTo(state3a) < 0);

		Assert.assertEquals("Should be equal", state3a, state3b);

		final CellStateTransition tran = new CellStateTransition(state1, state2);
		LOG.trace("Created: " + tran.toString());
	}

	@Test
	public void testCellWorld() throws Exception
	{
		final Binder binder = BinderFactory.Builder.fromFile(CONFIG_FILE)
				.withModelName("testModel" + System.currentTimeMillis())
				.build().create("conwayBooter");

		final CellWorldLattice world = (CellWorldLattice) binder
				.inject(CellWorld.class);
		final CreatingCapability booterSvc = binder
				.inject(CreatingCapability.class);
		final List<Map<CellID, LifeState>> cellStates = world
				.getInitialStates();
		LOG.trace("Got initial states: " + cellStates);
		final CountDownLatch latch = new CountDownLatch(cellStates.size()
				* cellStates.get(0).size());
		final Set<CellID> initialized = new HashSet<CellID>();
		for (Map<CellID, LifeState> row : cellStates)
			for (CellID cellID : row.keySet())
			{
				final CellID myCellID = cellID;
				LOG.trace("Booting agent with id: " + cellID
						+ ", initialized: " + initialized);
				booterSvc.createAgent(cellID, BasicCell.class).subscribe(
						new AgentStatusObserver()
						{
							@Override
							public void onNext(final AgentStatusUpdate update)
							{
								System.err.println("Observed status update: "
										+ update);
								if (update.getStatus().isInitializedStatus()
										&& initialized.add((CellID) update
												.getAgentID()))
									latch.countDown();
							}

							@Override
							public void onCompleted()
							{
								System.err.println("No more updates for "
										+ myCellID);
							}

							@Override
							public void onError(final Throwable e)
							{
								e.printStackTrace();
							}
						});
			}
		latch.await();
		LOG.trace("Cells initialized: " + initialized);
	}
}
