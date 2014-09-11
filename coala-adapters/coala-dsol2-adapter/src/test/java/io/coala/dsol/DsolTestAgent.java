/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/io/coala/dsol/DsolTestAgent.java $
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
package io.coala.dsol;

import io.coala.agent.AgentID;
import io.coala.agent.BasicAgent;
import io.coala.bind.Binder;
import io.coala.capability.replicate.ReplicatingCapability;
import io.coala.invoke.ProcedureCall;
import io.coala.invoke.Schedulable;
import io.coala.lifecycle.ActivationType;
import io.coala.log.InjectLogger;
import io.coala.model.ModelComponent;
import io.coala.time.Instant;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;
import io.coala.time.Trigger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link DsolTestAgent}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DsolTestAgent extends BasicAgent implements
		ModelComponent<AgentID>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private static Logger LOG;

	/**
	 * {@link DsolTestAgent} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected DsolTestAgent(final Binder binder)
	{
		super(binder);
	}

	@Override
	public void initialize()
	{
		LOG.trace("initializing...");
	}

	public ActivationType getActivationType()
	{
		return ActivationType.ACTIVATE_ONCE;
	}

	protected DsolSimulatorService getSimulator()
	{
		return (DsolSimulatorService) getBinder().inject(ReplicatingCapability.class);
	}

	protected SimTimeFactory newSimTime()
	{
		return getBinder().inject(SimTimeFactory.class);
	}

	private static final String schedulableMethodID = "testMethodID";

	@Schedulable(schedulableMethodID)
	protected void testSchedulableMethod(int repeats)
	{
		final SimTime now = getSimulator().getTime();
		LOG.trace("Scheduled call, iteration " + repeats + ", at t=" + now);
		if (repeats <= 0)
			die();
		else
			getSimulator().schedule(
					ProcedureCall.create(this, this, schedulableMethodID,
							repeats - 1),
					Trigger.createDelay(now,
							newSimTime().create(1, TimeUnit.HOURS)));
	}

	@Override
	public void activate()
	{
		LOG.trace("activating, t=" + getSimulator().getTime());
		getSimulator().schedule(
				ProcedureCall.create(this, this, schedulableMethodID, 100),
				Trigger.createAbsolute(newSimTime().create(1, TimeUnit.DAYS)));
		LOG.trace("done!");
	}

	@Override
	public void deactivate()
	{
		LOG.trace("deactivating...");
		// try
		// {
		// getSimulator().getReplication().start();
		// } catch (final SimRuntimeException e)
		// {
		// e.printStackTrace();
		// }
	}

	@Override
	public void finish()
	{
		LOG.trace("finishing...");
	}

	/** @see ModelComponent#getOwnerID() */
	@Override
	public AgentID getOwnerID()
	{
		return getID();
	}

	/** @see ModelComponent#getTime() */
	@Override
	public Instant<?> getTime()
	{
		return getSimulator().getTime();
	}

}