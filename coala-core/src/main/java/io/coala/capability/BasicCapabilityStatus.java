/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/BasicServiceStatus.java $
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
package io.coala.capability;

import io.coala.lifecycle.LifeCycleStatus;
import io.coala.lifecycle.MachineStatus;

public enum BasicCapabilityStatus implements CapabilityStatus<BasicCapabilityStatus>
{
	/** */
	CREATED,

	/** */
	INITIALIZED,

	/** */
	STARTED,

	/** */
	PAUSED,

	/** */
	COMPLETE,

	/** */
	FINISHED,

	/** */
	FAILED,

	;

	/** @see MachineStatus#permitsTransitionFrom(MachineStatus) */
	@Override
	public boolean permitsTransitionFrom(final BasicCapabilityStatus status)
	{
		return status.permitsTransitionTo(this);
	}

	/** @see MachineStatus#permitsTransitionTo(MachineStatus) */
	@Override
	public boolean permitsTransitionTo(final BasicCapabilityStatus status)
	{
		switch (status)
		{
		case CREATED:
			return this == CREATED;

		case FAILED:
			return true; // always allowed to transition to FAILED

		case INITIALIZED:
			return this == INITIALIZED || this == CREATED;
		case PAUSED:
			return this == INITIALIZED || this == PAUSED || this == STARTED;
		case STARTED:
			return this == INITIALIZED || this == PAUSED || this == STARTED;
		case COMPLETE:
			return this == CREATED || this == INITIALIZED || this == PAUSED
					|| this == STARTED || this == COMPLETE;
		case FINISHED:
			return this == COMPLETE || this == FINISHED;
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
		return this == STARTED;
	}

	/** @see LifeCycleStatus#isPassiveStatus() */
	@Override
	public boolean isPassiveStatus()
	{
		return this == PAUSED;
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
}