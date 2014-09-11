/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/deliver/DealUtil.java $
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

import io.coala.model.ModelID;
import io.coala.util.Util;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Map;

import org.joda.time.DateTime;

/**
 * {@link DealUtil} provides helper methods for working with the DEAL platform
 * based on calls to its REST API as specified <a
 * href="http://deal-deliver.appspot.com/tools.html">here</a>
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class DealUtil implements Util
{
	/** */
	// private static final Logger LOG = LogUtil.getLogger(DealUtil.class);

	/**
	 * {@link DealUtil} constructor, hidden for utility purposes
	 */
	private DealUtil()
	{
		// empty
	}

	/**
	 * @param taskDataFile
	 * @param offset
	 * @return
	 */
	public static Scenario generateDistributions(final File taskDataFile,
			final DateTime offset)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param uri
	 * @param superuser
	 * @param companyName
	 * @return
	 */
	public static AuthorizationID registerCompany(final URI uri,
			final String superuser, final ModelID companyName)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param authID
	 * @param coordinator
	 * @return
	 */
	public static String registerCoordinator(final AuthorizationID authID,
			final String coordinator)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param authID
	 * @param vehicleID
	 * @param assignedFleetID
	 * @param homeID
	 * @return
	 */
	public static String registerVehicle(final AuthorizationID authID,
			final VehicleID vehicleID, final FleetID assignedFleetID,
			final LocationID homeID)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param authID
	 * @param taskID
	 * @param fleetID
	 * @param locationID
	 * @return
	 */
	public static String registerTask(final AuthorizationID authID,
			final TaskID taskID, final FleetID assignedFleetID,
			final LocationID locationID)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param task
	 */
	public static void triggerFullVRP(
			final Map<FleetID, Collection<Vehicle>> fleets)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param task
	 */
	public static void simulateMovement(final Task task)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param taskKey
	 */
	public static void deleteTask(final String taskKey)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param vehicleKey
	 */
	public static void deleteVehicle(final String vehicleKey)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @param fleets
	 * @param incident
	 */
	public static void triggerVRPExtraOrder(
			final Map<FleetID, Collection<Vehicle>> fleets, final Task incident)
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

}
