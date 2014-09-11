/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/deliver/Task.java $
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
package io.coala.example.deliver;

import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * {@link Task} is an agent prototype, monitors status/assignment at DEAL
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class Task //
{
	/** */
	public final TaskID id;

	/** */
	public final FleetID assignedFleetID;

	/** */
	public final TaskType type;

	/** */
	public final LocationID locationID;

	/** */
	public final Duration duration;

	/** */
	public final Interval interval;

	/** */
	public TaskStatus status = null;

	/** */
	public VehicleID assignedVehicleName = null;

	/**
	 * {@link Task} constructor
	 * 
	 * @param fleetID
	 * @param type
	 * @param location
	 * @param duration
	 * @param interval
	 */
	public Task(final FleetID fleetID, final TaskType type,
			final LocationID location, final Duration duration,
			final Interval interval)
	{
		this.id = new TaskID(fleetID.getModelID());
		this.assignedFleetID = fleetID;
		this.type = type;
		this.locationID = location;
		this.duration = duration;
		this.interval = interval;
	}
}