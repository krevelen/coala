/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/model/ModelComponentIDFactory.java $
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
package io.coala.model;

import io.coala.agent.AgentID;
import io.coala.factory.Factory;
import io.coala.time.ClockID;

/**
 * {@link ModelComponentIDFactory}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public interface ModelComponentIDFactory extends Factory
{
	
	/**
	 * @param modelID the {@link ModelID} to initialize with
	 * @return this {@link ModelComponentIDFactory} instance
	 */
	ModelComponentIDFactory initialize(ModelID modelID);

	/**
	 * @return
	 */
	ModelID getModelID();

	/**
	 * @param value the agent identifier value
	 * @return the new {@link AgentID} instance
	 */
	AgentID createAgentID(String value);

	/**
	 * @param value the clock identifier value
	 * @return the new {@link ClockID} instance
	 */
	ClockID createClockID(String value);
	
}