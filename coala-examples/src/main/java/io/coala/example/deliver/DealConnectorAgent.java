/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/deliver/DealConnectorAgent.java $
 * 
 * Part of the Eureka project DELIVER, see http://www.almende.com/deliver/
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
package io.coala.example.deliver;

import java.util.Map;
import java.util.Set;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.almende.eve.agent.Agent;

/**
 * TODO
 * 
 * - port schedule elements (jobs, machines) from {@link ScheduleCachingAgent}
 * to DEAL platform (orders, vehicles)
 * 
 * - listen to updates from DEAL platform
 */
public class DealConnectorAgent extends Agent
{

	public static interface User
	{

		String getID();
	}

	public static interface Replication
	{

		String getID();

		String getUserID();
	}

	public static interface Priority extends Comparable<Priority>
	{

		String getID();
	}

	public static interface Visit
	{

		String getID();

		String getReplicationID();

		String getPriorityID();

		/**
		 * the set of vehicles allowed to perform this task due to vehicle
		 * requirements (permission, etc.)
		 */
		Set<Vehicle> getAllowedVehicles();

		Set<Interval> getAllowedIntervals();

		Duration getExpectedDuration();

		DateTime getActualArrivalTime();

		DateTime getActualDepartureTime();

		String getAssignedVehicleID();
	}

	public static interface Vehicle
	{

		String getID();

		String getReplicationID();

		Map<Interval, Visit> getAssignedTasks();
	}

	public void registerReplicationInDEAL(Replication replication)
	{

	}

	/**
	 * triggered by {@link ScheduleCachingAgent}
	 * 
	 * create/update order/trip in DEAL platform
	 * 
	 * (should trigger TSP/VRP scheduling agents listening to DEAL platform)
	 */
	public void updateOrderStatusInDEAL()
	{
		// TODO
	}

	/**
	 * triggered by {@link ScheduleCachingAgent}
	 * 
	 * create/update vehicle position/status in DEAL platform
	 * 
	 * (should trigger TSP/VRP scheduling agents listening to DEAL platform)
	 */
	public void updateVehicleInDEAL()
	{
	}

	/**
	 * triggered by DEAL platform's external agent framework
	 * 
	 * update changes in order/trip from DEAL platform at
	 * {@link DeliverConnectorAgent} and {@link ScheduleCachingAgent}
	 */
	public void updateFromDEAL()
	{
		// TODO
	}

	/**
	 * triggered by DEAL platform's external agent framework
	 * 
	 * update changes in vehicle location./status from DEAL platform at
	 * {@link DeliverConnectorAgent} and {@link ScheduleCachingAgent}
	 */
	public void updateVehicleFromDEAL()
	{
		// TODO
	}

}
