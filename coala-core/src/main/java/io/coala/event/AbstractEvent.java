/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/event/AbstractEvent.java $
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
package io.coala.event;

import io.coala.agent.AgentID;
import io.coala.lifecycle.ActivationType;
import io.coala.lifecycle.LifeCycle;
import io.coala.lifecycle.LifeCycleHooks;
import io.coala.log.InjectLogger;
import io.coala.model.ModelComponent;
import io.coala.model.ModelComponentID;
import io.coala.process.AbstractJob;

import org.slf4j.Logger;

/**
 * {@link AbstractEvent}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link EventID} identifier
 * @param <THIS> the concrete sub-type of {@link AbstractEvent}
 */
public abstract class AbstractEvent<ID extends EventID<?>> extends
		AbstractJob<ID> implements Event<ID>, LifeCycleHooks
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private transient Logger log;

	/** */
	private ModelComponentID<?> producerID;

	/** */
	private AgentID ownerID;

	/**
	 * {@link AbstractEvent} constructor
	 * 
	 * @param id
	 * @param producerID
	 */
	protected AbstractEvent(final ID id, final ModelComponent<?> producer)
	{
		this(id, producer.getOwnerID(), producer.getID());
	}

	/**
	 * {@link AbstractEvent} constructor
	 * 
	 * @param id
	 * @param producerID
	 */
	protected AbstractEvent(final ID id, final AgentID ownerID,
			final ModelComponentID<?> producerID)
	{
		super(id);
		this.ownerID = ownerID;
		this.producerID = producerID;
	}

	/**
	 * {@link AbstractEvent} zero-arg bean constructor
	 */
	protected AbstractEvent()
	{
		super();
	}

	/** @param producerID */
	protected synchronized void setProducerID(
			final ModelComponentID<?> producerID)
	{
		this.producerID = producerID;
	}

	/** @see ModelComponent#getOwnerID() */
	@Override
	public AgentID getOwnerID()
	{
		return this.ownerID;
	}

	/** @see Event#getProducerID() */
	@Override
	public synchronized ModelComponentID<?> getProducerID()
	{
		return this.producerID;
	}

	/** @see LifeCycleHooks#initialize() */
	@Override
	public void initialize()
	{
		// override me
	}

	/** @see LifeCycleHooks#activate() */
	@Override
	public void activate()
	{
		// empty
	}

	/** @see LifeCycleHooks#deactivate() */
	@Override
	public void deactivate()
	{
		// empty
	}

	/** @see LifeCycleHooks#finish() */
	@Override
	public void finish()
	{
		// override me
	}

	/** @see LifeCycle#getActivationType() */
	@Override
	public ActivationType getActivationType()
	{
		return ActivationType.ACTIVATE_ONCE;
	}

}
