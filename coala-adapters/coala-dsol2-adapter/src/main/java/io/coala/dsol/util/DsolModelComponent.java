/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolModelComponent.java $
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/
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
package io.coala.dsol.util;

import io.coala.time.ClockID;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * {@link InertiaModelComponent}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link SimulatorInterface}
 * @param <M> the type of {@link DsolModel}
 */
public interface DsolModelComponent<S extends SimulatorInterface, M extends DsolModel<S, M>>
{

	/** the identifier for the main/default random number generator/stream */
	String RNG_ID = "main";

	/** */
	Object[] NO_ARGS = new Object[] {};

	/** return the component name */
	String getName();

	/** @return the model */
	M getModel();

	/** @return the treatment */
	Treatment getTreatment();

	/** @return the main random number generator/stream named {@link RNG_ID} */
	StreamInterface getRNG();

	/** @return the specified random number generator/stream */
	StreamInterface getRNG(final String name);

	/** @return the simulator */
	S getSimulator();

	/** @return the (unique/federate) simulator name */
	ClockID getSimulatorName();

	/** @return the current simulation time */
	double simTime();

	/** @return the current simulation time in specified timeUnit */
	double simTime(TimeUnit timeUnit);

	/** @return the current simulation time in specified timeUnit */
	double simTime(TimeUnitInterface timeUnit);

	/** @return the simulation time for specified ISO time */
	double simTime(DateTime time);

	/** @return the simulation time for specified duration */
	double simTime(Duration duration);

	/** @return the current simulation time */
	SimTime getSimTime();

	/** @return the current simulation time */
	SimTime getSimTime(double simTime);

	/**
	 * @param time
	 * @return
	 */
	SimTime getSimTime(DateTime time);

	/** @return the current simulation ISO date time */
	DateTime getDateTime();
	
	/** @return the simulation ISO interval after warm-up and before run length */
	Interval getInterval();
}
