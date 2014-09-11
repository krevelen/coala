/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/Service.java $
 * 
 * Part of the EU project All4Green, see http://www.all4green-project.eu/
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
 * Copyright � 2010-2012 Almende B.V. 
 */
package com.almende.dsol.example.datacenters;

import io.coala.log.LogUtil;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;

import org.apache.log4j.Logger;
import org.joda.time.Interval;

/**
 * Service
 * 
 * @date $Date: 2014-04-18 16:38:34 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 235 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 * 
 */
public class Service extends AbstractFederationModelComponent
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(Service.class);

	/** */
	private static int ID_COUNT = 1;

	/** */
	private final String ownerName;

	/** */
	private final double startTime;

	/** service power (Watt) = energy (J) over time (s) */
	private final double consumptionKW;

	/** service cash flow (EUR) per hour */
	private final double hourlyCashflow;

	/**
	 * @param datacenter
	 * @throws RemoteException
	 * @throws SimRuntimeException
	 */
	public Service(final FederationModel model, final Datacenter owner,
			final Interval interval, final double consumptionKW,
			final double hourlyCashflow)
	{
		super(model, "SVC" + ID_COUNT++);
		this.ownerName = owner.getName();
		this.consumptionKW = consumptionKW;
		this.hourlyCashflow = hourlyCashflow;

		this.startTime = simTime(interval.getStart());
		final double endTime = simTime(interval.getEnd());
		try
		{
			getSimulator().scheduleEvent(
					new SimEvent(this.startTime, this, this, STARTED_METHOD_ID,
							FederationModel.NO_ARGS));
			getSimulator().scheduleEvent(
					new SimEvent(endTime, this, this, COMPLETED_METHOD_ID,
							FederationModel.NO_ARGS));
		} catch (final RemoteException | SimRuntimeException e)
		{
			LOG.error("t=" + model.getSimTime() + " (" + model.getDateTime()
					+ ") Problem scheduling service start/end: " + interval, e);
		}

		// LOG.trace(String.format(
		// "t=%.4f (%s) Service %d scheduled for %.2f-%.2f (%s)",
		// model.getSimTime(), model.getDateTime(), getId(),
		// this.startTime, endTime, interval));
	}

	private static final String STARTED_METHOD_ID = "started";

	protected void started()
	{
		// LOG.trace("Service " + getId() + " started, t= "
		// + this.model.toDateTime());
		fireEvent(SERVICE_STARTED, null);
		fireEvent(CONSUMPTION_CHANGED, this.consumptionKW);
		fireEvent(CASHFLOW_CHANGED, this.hourlyCashflow);
	}

	private static final String COMPLETED_METHOD_ID = "completed";

	protected void completed()
	{
		// LOG.trace("Service " + getId() + " completed, t= "
		// + this.model.toDateTime());
		fireEvent(SERVICE_COMPLETED, null);
		fireEvent(CONSUMPTION_CHANGED, -this.consumptionKW);
		fireEvent(CASHFLOW_CHANGED, -this.hourlyCashflow);
	}

	/** @return the id */
	public String getOwnerName()
	{
		return this.ownerName;
	}

}
