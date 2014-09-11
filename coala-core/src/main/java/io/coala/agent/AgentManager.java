/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/agent/AgentManager.java $
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
package io.coala.agent;

import rx.Observable;

/**
 * {@link AgentManager}
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface AgentManager
{

	Observable<AgentStatusUpdate> getWrapperAgentStatus();

	Observable<AgentStatusUpdate> getAgentStatus(AgentID agentID);

	/**
	 * Boot and wrap a (cached) instance/clone of specified agent name
	 * 
	 * @param agentName
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> boot(String agentName);

	/**
	 * Boot and wrap a (cached) instance/clone of specified agent name
	 * 
	 * @param agentName
	 * @param agentType the type of {@link Agent} to create
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> boot(String agentName,
			Class<? extends Agent> agentType);

	/**
	 * Boot and wrap a (cached) instance/clone of specified agent ID
	 * 
	 * @param agentID
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> boot(final AgentID agentID);

	/**
	 * Boot and wrap a (cached) instance/clone of specified agent ID
	 * 
	 * @param agentID the {@link AgentID} of the {@link Agent} to boot
	 * @param agentType the type of {@link Agent} to create
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> boot(final AgentID agentID,
			Class<? extends Agent> agentType);

	/**
	 * @param clientID
	 * @param currentOnly
	 * @return
	 */
	Observable<AgentID> getChildIDs(AgentID clientID, boolean currentOnly);

}
