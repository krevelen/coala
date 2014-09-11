/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/agent/BasicAgentStatus.java $
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

import io.coala.lifecycle.ActivationType;
import io.coala.lifecycle.LifeCycleHooks;
import io.coala.lifecycle.LifeCycleStatus;
import io.coala.lifecycle.MachineStatus;
import io.coala.log.LogUtil;

import org.apache.log4j.Logger;

public enum BasicAgentStatus implements AgentStatus<BasicAgentStatus>
{
	/** */
	CREATED,

	/** */
	INITIALIZED,

	/** */
	ACTIVE,

	/** */
	PASSIVE,

	/** */
	COMPLETE,

	/** */
	FINISHED,

	/** */
	FAILED,

	;

	/** @see MachineStatus#permitsTransitionFrom(MachineStatus) */
	@Override
	public boolean permitsTransitionFrom(final BasicAgentStatus status)
	{
		return status.permitsTransitionTo(this);
	}

	/** @see MachineStatus#permitsTransitionTo(MachineStatus) */
	@Override
	public boolean permitsTransitionTo(final BasicAgentStatus status)
	{
		switch (status)
		{

		case FAILED:
			return true; // always allowed to transition to FAILED

		case CREATED:
			return this == CREATED;

		case INITIALIZED:
			return this == CREATED;

		case PASSIVE:
			return this == INITIALIZED || this == PASSIVE || this == ACTIVE;

		case ACTIVE:
			return this == INITIALIZED || this == PASSIVE || this == ACTIVE;

		case COMPLETE:
			return this == CREATED || this == INITIALIZED || this == PASSIVE
					|| this == ACTIVE;

		case FINISHED:
			return this == INITIALIZED || this == COMPLETE || this == FINISHED;
		}
		return false;
	}

	/** @see LifeCycleStatus#isCreatedStatus() */
	@Override
	public boolean isCreatedStatus()
	{
		return this == CREATED;
	}

	/** @see LifeCycleStatus#isInitializedStatus() */
	@Override
	public boolean isInitializedStatus()
	{
		return this == INITIALIZED;
	}

	/** @see LifeCycleStatus#isActiveStatus() */
	@Override
	public boolean isActiveStatus()
	{
		return this == ACTIVE;
	}

	/** @see LifeCycleStatus#isPassiveStatus() */
	@Override
	public boolean isPassiveStatus()
	{
		return this == PASSIVE;
	}

	/** @see LifeCycleStatus#isCompleteStatus() */
	@Override
	public boolean isCompleteStatus()
	{
		return this == COMPLETE;
	}

	/** @see LifeCycleStatus#isFinishedStatus() */
	@Override
	public boolean isFinishedStatus()
	{
		return this == FINISHED;
	}

	/** @see LifeCycleStatus#isFailedStatus() */
	@Override
	public boolean isFailedStatus()
	{
		return this == FAILED;
	}

	private static final Logger LOG = LogUtil.getLogger(BasicAgentStatus.class);

	/**
	 * @param agent
	 * @return
	 */
	public static BasicAgentStatus determineKillStatus(final Agent agent)
	{
		final BasicAgentStatus currentStatus = agent.getStatus();
		final ActivationType activationType = ((LifeCycleHooks) agent)
				.getActivationType();
		switch (currentStatus)
		{
		case FAILED:
		case FINISHED: // done
			break;

		case CREATED:
		case INITIALIZED:
			return COMPLETE;

		case ACTIVE:
			switch (activationType)
			{
			case ACTIVATE_NEVER:
				LOG.warn("Strange! Agent " + agent.getID() + " was "
						+ currentStatus + " when it should never be activated");
			case ACTIVATE_AND_FINISH:
			case ACTIVATE_ONCE:
			case ACTIVATE_MANY:
				// FIXME interrupt any running threads
			}
			return COMPLETE;

		case PASSIVE:
			// FIXME interrupt any running threads
			return COMPLETE;

		case COMPLETE:
			LOG.warn("Race condition? Agent " + agent.getID() + " is "
					+ currentStatus + " and should already have been finished");
			return FINISHED;

		}

		// should never happen
		LOG.warn("Assuming " + COMPLETE + " to kill agent from status: "
				+ currentStatus + " for activation type: " + activationType);
		return COMPLETE;
	}
}