/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/pacing/PacedActor.java $
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
package io.coala.experimental.grant;

import io.coala.agent.AgentID;
import io.coala.agent.BasicAgent;
import io.coala.bind.Binder;
import io.coala.capability.replicate.ReplicatingCapability;
import io.coala.invoke.ProcedureCall;
import io.coala.invoke.Schedulable;
import io.coala.log.InjectLogger;
import io.coala.model.ModelComponent;
import io.coala.process.Job;
import io.coala.time.SimTime;
import io.coala.time.Trigger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link PacedActor}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class PacedActor extends BasicAgent implements ModelComponent<AgentID>
{

	/** */
	private static final long serialVersionUID = 1L;

	@InjectLogger
	private Logger LOG;

	/**
	 * {@link PacedActor} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected PacedActor(final Binder binder)
	{
		super(binder);
	}

	protected ReplicatingCapability getSimulator()
	{
		return getBinder().inject(ReplicatingCapability.class);
	}

	@Override
	public SimTime getTime()
	{
		return getSimulator().getTime();
	}

	@Override
	public void initialize()
	{
		LOG.trace("Initializing");

		final Job<?> job = ProcedureCall.create(this, this, testMethodID);
		final Trigger<?> trigger = Trigger.createAbsolute(getTime());
		getSimulator().schedule(job, trigger);
		die();
	}

	private static final String testMethodID = "";

	@Schedulable(testMethodID)
	protected void scheduleTestMethod(final SimTime time)
	{
		if (time.equals(getTime()))
			LOG.info("Method executed ON TIME");
		else
			LOG.info("Method executed BUT NOT as scheduled");
	}

	@Override
	public void activate()
	{
		LOG.trace("Activating");
	}

	@Override
	public void deactivate()
	{
		LOG.trace("Deactivating");
	}

	@Override
	public void finish()
	{
		LOG.trace("Finishing");
	}

	/** @see ModelComponent#getOwnerID() */
	@Override
	public AgentID getOwnerID()
	{
		return getID();
	}

}
