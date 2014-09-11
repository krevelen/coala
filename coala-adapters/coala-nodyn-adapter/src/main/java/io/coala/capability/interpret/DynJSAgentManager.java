/* $Id$
 * $URL$
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
package io.coala.capability.interpret;

import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.agent.BasicAgentManager;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;

/**
 * {@link DynJSAgentManager}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DynJSAgentManager extends BasicAgentManager
{

	/** */
	private static DynJSAgentManager INSTANCE;

	/**
	 * @param binder
	 * @return
	 */
	public synchronized static DynJSAgentManager getInstance(final Binder binder)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new DynJSAgentManager(binder);

			INSTANCE.bootAgents();
		}

		return INSTANCE;
	}

	/**
	 * {@link DynJSAgentManager} constructor
	 * 
	 * @param binder
	 */
	protected DynJSAgentManager(final Binder binder)
	{
		super(binder);
	}

	/** expose for {@link DynJSInterpretingCapability} */
	@Override
	protected void updateWrapperAgentStatus(final AgentID agentID,
			final AgentStatus<?> status)
	{
		super.updateWrapperAgentStatus(agentID, status);
	}

	/** expose for {@link DynJSInterpretingCapability} */
	@Override
	public BinderFactory getBinderFactory()
	{
		return super.getBinderFactory();
	}

}
