/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/agent/BasicAgent.java $
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
package io.coala.agent;

import io.coala.bind.Binder;
import io.coala.capability.admin.CreatingCapability;
import io.coala.capability.admin.DestroyingCapability;
import io.coala.capability.configure.ConfiguringCapability;
import io.coala.capability.embody.GroundingCapability;
import io.coala.capability.interact.ReceivingCapability;
import io.coala.capability.interact.SendingCapability;
import io.coala.capability.know.ReasoningCapability;
import io.coala.capability.plan.SchedulingCapability;
import io.coala.capability.replicate.RandomizingCapability;
import io.coala.capability.replicate.ReplicatingCapability;
import io.coala.config.PropertyGetter;
import io.coala.lifecycle.AbstractLifeCycle;
import io.coala.lifecycle.ActivationType;
import io.coala.lifecycle.LifeCycle;
import io.coala.lifecycle.LifeCycleHooks;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link BasicAgent}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the concrete type of {@link BasicAgent} to inject, compare etc.
 */
public class BasicAgent extends AbstractLifeCycle<AgentID, BasicAgentStatus>
		implements Agent, LifeCycle<BasicAgentStatus>, LifeCycleHooks
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private transient Logger LOG;

	/** */
	private transient Binder binder;

	/**
	 * {@link BasicAgent} constructor
	 * 
	 * @param id
	 * @param binder
	 */
	@Inject
	protected BasicAgent(final Binder binder)
	{
		super(binder.getID());
		this.binder = binder;
	}

	/**
	 * helper method
	 */
	protected void die()
	{
		setStatus(BasicAgentStatus.COMPLETE);
	}

	/**
	 * helper method
	 * 
	 * @param key
	 * @return
	 */
	protected PropertyGetter getProperty(final String key)
	{
		return getBinder().inject(ConfiguringCapability.class).getProperty(key);
	}

	/** @return the agent's local {@link CreatingCapability} */
	@JsonIgnore
	protected CreatingCapability getBooter()
	{
		return getBinder().inject(CreatingCapability.class);
	}

	/** @return the agent's local {@link SchedulingCapability} */
	@JsonIgnore
	protected SchedulingCapability<?> getScheduler()
	{
		return getSimulator();
	}

	/** @return the agent's local {@link ReplicatingCapability} */
	@JsonIgnore
	protected ReplicatingCapability getSimulator()
	{
		return getBinder().inject(ReplicatingCapability.class);
	}

	/** @return the agent's local {@link SendingCapability} */
	@JsonIgnore
	protected SendingCapability getMessenger()
	{
		return getBinder().inject(SendingCapability.class);
	}

	/** @return the agent's local {@link ReceivingCapability} */
	@JsonIgnore
	protected ReceivingCapability getReceiver()
	{
		return getBinder().inject(ReceivingCapability.class);
	}

	/** @return the agent's local {@link ReasoningCapability} */
	@JsonIgnore
	protected ReasoningCapability getReasoner()
	{
		return getBinder().inject(ReasoningCapability.class);
	}

	@JsonIgnore
	protected DestroyingCapability getFinalizer()
	{
		return getBinder().inject(DestroyingCapability.class);
	}

	/** @return the agent's local {@link RandomizingCapability} */
	@JsonIgnore
	protected RandomizingCapability getRandomizer()
	{
		return getSimulator();// getBinder().bind(RandomizerService.class);
	}

	/** @return the agent's local {@link GroundingCapability} */
	@JsonIgnore
	protected GroundingCapability getWorld()
	{
		return getBinder().inject(GroundingCapability.class);
	}

	/** @see Agent#getBinder() */
	@Override
	public final synchronized Binder getBinder()
	{
		return this.binder;
	}

	@Override
	public String toString()
	{
		return String.format("%s[%s]", getClass().getSimpleName(), getID());
	}

	/** */
	private boolean initialized = false;

	/** @see LifeCycle#initialize() */
	@Override
	public void initialize() throws Exception
	{
		if (this.initialized)
			throw new IllegalStateException("Agent already initialized: "
					+ getID());

		this.initialized = true;
	}

	/** @see Process#activate() */
	@Override
	public void activate() throws Exception
	{
		// override me
	}

	/** @see LifeCycle#deactivate() */
	@Override
	public void deactivate() throws Exception
	{
		// override me
	}

	/** @see LifeCycle#finish() */
	@Override
	public void finish() throws Exception
	{
		// override me
	}

	/** @see LifeCycle#getActivationType() */
	@Override
	public ActivationType getActivationType()
	{
		// override me
		return ActivationType.ACTIVATE_NEVER;
	}

}
