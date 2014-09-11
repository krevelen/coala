/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/model/AbstractModelComponent.java $
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
package io.coala.model;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.capability.interact.SendingCapability;
import io.coala.capability.plan.SchedulingCapability;
import io.coala.event.AbstractEventProducer;
import io.coala.event.Event;
import io.coala.log.InjectLogger;
import io.coala.message.Message;
import io.coala.time.Instant;

import org.slf4j.Logger;

/**
 * {@link AbstractModelComponent}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class AbstractModelComponent<ID extends ModelComponentID<?>, A extends Agent, E extends Event<?>>
		extends AbstractEventProducer<ID, E> implements ModelComponent<ID>
{

	/** */
	private static final long serialVersionUID = 1L;

	@InjectLogger
	private Logger log;

	/** */
	private final A owner;

	/**
	 * {@link AbstractModelComponent} constructor
	 * 
	 * @param id
	 * @param owner
	 */
	public AbstractModelComponent(final ID id, final A owner)
	{
		super(id);
		this.owner = owner;
	}

	/** @return the agent owning this {@link ModelComponent} */
	protected A getOwner()
	{
		return this.owner;
	}

	@Override
	public AgentID getOwnerID()
	{
		return getOwner().getID();
	}

	/**
	 * @return the simulator
	 */
	protected SchedulingCapability<?> getSimulator()
	{
		return getOwner().getBinder().inject(SchedulingCapability.class);
	}

	protected ModelComponentIDFactory newID()
	{
		return getOwner().getBinder().inject(ModelComponentIDFactory.class);
	}

	/**
	 * @return the current simulation time
	 */
	@Override
	public Instant<?> getTime()
	{
		return getSimulator().getTime();
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	protected void send(final Message<?> message) throws Exception
	{
		getOwner().getBinder().inject(SendingCapability.class).send(message);
	}

}
