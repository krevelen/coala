/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/process/BasicProcessorStatus.java $
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
package io.coala.process;

import io.coala.lifecycle.MachineStatus;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;

public enum BasicProcessorStatus implements ProcessorStatus<BasicProcessorStatus>
{
	/** */
	CREATED,

	/** */
	IDLE,

	/** */
	BUSY,

	;

	private final Collection<BasicProcessorStatus> permittedNext;

	private BasicProcessorStatus(BasicProcessorStatus... premittedNextStatus)
	{
		if (premittedNextStatus == null || premittedNextStatus.length == 0)
			this.permittedNext = Collections.emptySet();
		else
			this.permittedNext = Collections.unmodifiableSet(EnumSet
					.copyOf(Arrays.asList(premittedNextStatus)));
	}

	/** @see MachineStatus#getPermittedTransitions() */
	//@Override
	public Collection<BasicProcessorStatus> getPermittedTransitions()
	{
		return this.permittedNext;
	}

	/** @see MachineStatus#permitsTransitionFrom(MachineStatus) */
	@Override
	public boolean permitsTransitionFrom(final BasicProcessorStatus status)
	{
		return status.getPermittedTransitions().contains(this);
	}

	/** @see MachineStatus#permitsTransitionTo(MachineStatus) */
	@Override
	public boolean permitsTransitionTo(final BasicProcessorStatus status)
	{
		return getPermittedTransitions().contains(status);
	}

}