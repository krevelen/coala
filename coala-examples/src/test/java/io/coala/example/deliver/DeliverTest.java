/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/deliver/DeliverTest.java $
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

import io.coala.log.LogUtil;
import io.coala.model.ModelID;

import java.io.File;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.junit.Ignore;
import org.junit.Test;

import rx.Observable;
import rx.Observer;

import com.eaio.uuid.UUID;

/**
 * {@link DeliverTest}
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
@Ignore
public class DeliverTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DeliverTest.class);

	/**
	 * helper method
	 * 
	 * @param scenario
	 * @param type
	 * @param fleetIDs
	 * @return
	 */
	private Collection<Task> generateTaskForFleets(final Scenario scenario,
			final TaskType type, final FleetID... fleetIDs)
	{
		if (fleetIDs == null || fleetIDs.length == 0)
			return Collections.emptySet();

		final LocationID locationID = scenario.getLocationDistribution(type)
				.draw();
		final Duration duration = scenario.getDurationDistribution(type).draw();
		final DateTime startTime = scenario.getOffset().plus(
				scenario.getDailyWindowStartOffsetDistribution(type).draw());
		final Interval interval = new Interval(startTime,
				startTime.plus(scenario.getWindowDurationDistribution(type)
						.draw()));
		final Collection<Task> result = new HashSet<>();
		for (FleetID fleetID : fleetIDs)
			result.add(new Task(fleetID, type, locationID, duration, interval));
		return result;
	}

	// @Test
	/**
	 * @throws Exception
	 */
	/**
	 * @throws Exception
	 */
	/**
	 * @throws Exception
	 */
	/**
	 * @throws Exception
	 */
	@Test
	public void testTrigionScenario() throws Exception
	{
		final URI uri = new URI("http:s//deal-deliver.appspot.com/");
		final String superuser = "rick@almende.org";
		final String coordinator = "jan-paul@almende.org"; // experimenter
		final File taskDataFile = new File("trigion/rotterdam.csv");
		final DateTime offset = DateTime.now().withTimeAtStartOfDay();

		final ModelID companyName = new ModelID((new UUID()).toString());
		final LocationID depotZip = new LocationID(companyName, "3088GG");
		final FleetID fleetID1 = new FleetID(companyName, "fleet1");
		final String fleet1FlexVehicle = "truck0";
		final String[] fleet1ids = new String[] { fleet1FlexVehicle, "truck1",
				"truck2", "truck3", "vehicle4" };
		final FleetID fleetID2 = new FleetID(companyName, "fleet2");
		final String[] fleet2ids = new String[] { "truck5", "truck6", "truck7",
				"truck8", "truck9" };

		final Map<FleetID, Collection<Vehicle>> fleets = new HashMap<>();
		final Collection<Vehicle> fleet1 = new HashSet<>();
		fleets.put(fleetID1, fleet1);
		for (String vehicleName : fleet1ids)
			fleet1.add(new Vehicle(companyName, vehicleName, depotZip,
					fleetID1, !vehicleName.equals(fleet1FlexVehicle)));
		final Collection<Vehicle> fleet2 = new HashSet<>();
		fleets.put(fleetID2, fleet2);
		for (String vehicleName : fleet2ids)
			fleet2.add(new Vehicle(companyName, vehicleName, depotZip,
					fleetID2, true));

		final Scenario scenario = DealUtil.generateDistributions(taskDataFile,
				offset);

		final int taskCount = 40;
		final Collection<Task> tasks = new HashSet<>();
		while (tasks.size() < taskCount)
		{
			final TaskType type = scenario.getTaskTypeDistribution().draw();
			tasks.addAll(generateTaskForFleets(scenario, type, fleetID1,
					fleetID2));
		}
		final int incidentCount = 10;
		final Collection<Task> observableIncidents = new HashSet<>();
		while (observableIncidents.size() < incidentCount)
		{
			final TaskType type = scenario.getIncidentTypeDistribution().draw();
			tasks.addAll(generateTaskForFleets(scenario, type, fleetID1,
					fleetID2));
		}
		final Observable<Task> incidents = Observable.from(observableIncidents);

		LOG.trace("Testing DELIVER agents...");

		final Map<String, String> dealKeyCache = new HashMap<>();

		/* 0. login to DEAL as simulation superuser */

		/*  - [optional] register replication company {@link com.deal.planner.server.rest2.CompanyResource#createCompany(String, String)}
		 *    https://docs.google.com/a/almende.org/document/d/19XQcC4qZsHkx-UMZs6dSMh7Htday03orPXUaH2Lq9ZQ/edit#heading=h.k1huhio84ef3*/

		final AuthorizationID authID = DealUtil.registerCompany(uri, superuser,
				companyName);
		dealKeyCache.put(companyName.getValue(), authID.dealCompanyKey);

		/*  - [optional] register replication user (i.e. coordinator view account)
		 *    https://docs.google.com/a/almende.org/document/d/19XQcC4qZsHkx-UMZs6dSMh7Htday03orPXUaH2Lq9ZQ/edit#heading=h.d90ga1fk4cg6*/

		final String dealCoordinatorKey = DealUtil.registerCoordinator(authID,
				coordinator);
		dealKeyCache.put(coordinator, dealCoordinatorKey);

		/*  - [optional] register replication's (external) vehicle agents to listen for order status update events
		 *    a. either: Configure the mapping on logged in user dealrootuser@gmail.com {@link com.deal.planner.shared.twig.bean.DefaultUser#DEFAULT_DEAL}
		 *    b. or: for initial setup, add to {@link MyUserSetup#orderUpdatedStatusUpdatedEventHandlers} list:
		 *    	EventHandler externalAgentHandler = new EventHandler("External Agent Name", "http://127.0.0.1/....", false, AgentType.EXTERNAL, "Description..");
		 *      orderUpdatedStatusUpdatedEventHandlers.add(externalAgentHandler);*/

		// TODO modify (live) configuration of external agents (mimic GUI
		// commands?)

		/* 1. (re)initialize fleet(s) available trucks
		 *    https://docs.google.com/a/almende.org/document/d/1G4cpPGCGAvERH3rs99oxSYpFLwCYz2absVVKv_syhIk/edit#heading=h.k1huhio84ef3*/

		for (Collection<Vehicle> fleet : fleets.values())
			for (Vehicle vehicle : fleet)
			{
				final String vehicleKey = DealUtil.registerVehicle(authID,
						vehicle.id, vehicle.assignedFleetID, vehicle.homeID);
				dealKeyCache.put(vehicle.id.getValue(), vehicleKey);
				if (vehicle.assignedFleetID == fleetID1)
					vehicle.flexible = !vehicle.flexible;

				/*  - [optional] update vehicle (initial) location
				 *    https://docs.google.com/a/almende.org/document/d/1JWw8zaI_YnCd8VLkUvSH9vnYrms5dywyGy4rMFkzjU/edit#heading=h.oadgav2n7z6j
				 */
			}

		/* 2. (re)initialize orders' status & assignment
		 *    https://docs.google.com/a/almende.org/document/d/1zD6q1fnBxLBBFULh_ml_HJyQ_rKkrbihpQ_jOWE0Ouk/edit#heading=h.k1huhio84ef3
		 */

		for (Task task : tasks)
		{
			dealKeyCache.put(task.id.getValue(), DealUtil.registerTask(authID,
					task.id, task.assignedFleetID, task.locationID));

			/*  - [optional] update order status
			 *    use "status" as the key of the field, and one of the {@link com.deal.planner.shared.twig.bean#OrderState} possible values
			 *    https://docs.google.com/a/almende.org/document/d/1zD6q1fnBxLBBFULh_ml_HJyQ_rKkrbihpQ_jOWE0Ouk/edit#heading=h.j1xtil2mjsh2
			 */

			/*  - [optional] pull vehicle schedule (or 404 NOT FOUND if not exists yet)
			 *    https://docs.google.com/a/almende.org/document/d/1af7EIKfn9mM58erujdG6X3awiQ8t0nAsKrfEn5mPCZ0/edit#heading=h.k1huhio84ef3
			 */

			/*  - [deprecated] do (pre/re)assignment
			 *    https://docs.google.com/a/almende.org/document/d/1JWw8zaI_YnCd8VLkUvS_H9vnYrms5dywyGy4rMFkzjU/edit#heading=h.gdczggmnzrda
			 */

		}

		/* 3. for each fleet, trigger/notify ACO (via LIACS external agent) once
		 */

		DealUtil.triggerFullVRP(fleets);

		for (Task task : tasks)
		{

			/*  - [optional] generate truck movement event (push vehicle location updates) 
			 *    Use REST API at {@link com.deal.planner.server.rest2.SimulatorResource}
			 */

			if (task.status == TaskStatus.PICKED_UP) // currently assigned
				DealUtil.simulateMovement(task);
		}

		/* 4. repeat: generate incident orders + trigger/notify ACO reschedule with designated (sub)fleet
		 */

		incidents.subscribe(new Observer<Task>()
		{
			@Override
			public void onNext(final Task incident)
			{
				// TODO wait until incident generation time is DEAL actual time

				final String taskKey = DealUtil.registerTask(authID,
						incident.id, incident.assignedFleetID,
						incident.locationID);
				dealKeyCache.put(incident.id.getValue(), taskKey);

				DealUtil.triggerVRPExtraOrder(fleets, incident);

				/*  - [optional] generate truck movement event (push vehicle location updates) 
				 *    Use REST API at {@link com.deal.planner.server.rest2.SimulatorResource}
				 */

				if (incident.status == TaskStatus.PICKED_UP)
					DealUtil.simulateMovement(incident);
			}

			@Override
			public void onCompleted()
			{
				LOG.trace("Incidents complete!");
			}

			@Override
			public void onError(final Throwable e)
			{
				e.printStackTrace();
			}
		});

		/* 5. Analyze/export logs
		 *  - GET /rest2/logs/entities/trips/truck/<truck_key> 
		 *  - GET /rest2/logs/entities/trips/order/<order_key> 
		 */

		for (Task task : tasks)
			DealUtil.deleteTask(dealKeyCache.get(task.id.getValue()));

		for (Task task : observableIncidents)
			DealUtil.deleteTask(dealKeyCache.get(task.id.getValue()));

		for (Collection<Vehicle> fleet : fleets.values())
			for (Vehicle vehicle : fleet)
				DealUtil.deleteVehicle(dealKeyCache.get(vehicle.id.getValue()));

		/* 6. cleanup: destroy replication orders, vehicles, user, company
		 *  - delete Order: DELETE to /rest2/entity/order/123
		 *  - delete Vehicle: DELETE to /rest2/entity/truck/123 
		 */

		/*    NOTE: All the Company's users MUST be deleted first before deleting the Company
		 *  - [optional] delete User: DELETE to /rest2/users/email@example.com
		 *  - [optional] delete Company: DELETE to /rest2/companies/123 
		 */

		LOG.trace("Done testing DELIVER agents!");
	}
}
