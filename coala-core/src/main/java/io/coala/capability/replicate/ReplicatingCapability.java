/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/SimulatorService.java $
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
package io.coala.capability.replicate;

import io.coala.capability.CapabilityFactory;
import io.coala.capability.plan.SchedulingCapability;
import io.coala.time.SimTime;

/**
 * {@link ReplicatingCapability} binds the {@link ReplicatingCapability} to the
 * {@link SimTime} instant type
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public interface ReplicatingCapability extends SchedulingCapability<SimTime>,
		RandomizingCapability
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	interface Factory extends CapabilityFactory<ReplicatingCapability>
	{
		// empty
	}

	/** start the simulator. Called by experimenters, not by model components */
	void start();

	/** pause the simulator. Called by experimenters, not by model components */
	void pause();

	/**
	 * @return {@code true} if the simulation is running, {@code false}
	 *         otherwise. Called by experimenters, not by model components
	 */
	boolean isRunning();

	/**
	 * @return {@code true} if the simulation is complete, {@code false}
	 *         otherwise. Called by experimenters, not by model components
	 */
	boolean isComplete();
}
