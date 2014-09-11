/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/booter/BooterService.java $
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
package io.coala.capability.admin;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import rx.Observable;

/**
 * {@link CreatingCapability} links agents for lookup or directory purposes
 * 
 * @date $Date: 2014-08-08 16:20:51 +0200 (Fri, 08 Aug 2014) $
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the (sub)type of {@link CreatingCapability} to build
 */
public interface CreatingCapability extends Capability<BasicCapabilityStatus>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 353 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	interface Factory extends CapabilityFactory<CreatingCapability>
	{
		// empty
	}

	Observable<AgentID> getChildIDs(boolean currentOnly);
	
	/**
	 * @param agentID the agent identifier
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> createAgent(String agentID);

	/**
	 * @param agentID the agent identifier
	 * @param agentType the type of {@link Agent} to boot
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	<A extends Agent> Observable<AgentStatusUpdate> createAgent(String agentID,
			Class<A> agentType);

	/**
	 * @param agentID the agent identifier
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	Observable<AgentStatusUpdate> createAgent(AgentID agentID);

	/**
	 * @param agentID the agent identifier
	 * @param agentType the type of {@link Agent} to boot
	 * @return an {@link Observable} of the new agent's
	 *         {@link AgentStatusUpdate}
	 */
	<A extends Agent> Observable<AgentStatusUpdate> createAgent(AgentID agentID,
			Class<A> agentType);

}
