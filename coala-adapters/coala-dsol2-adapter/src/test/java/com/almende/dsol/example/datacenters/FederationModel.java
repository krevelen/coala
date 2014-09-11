/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/FederationModel.java $
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

import io.coala.dsol.util.AbstractDsolModel;
import io.coala.log.LogUtil;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * {@link FederationModel}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 * 
 */
public class FederationModel extends
		AbstractDsolModel<DEVSSimulatorInterface, FederationModel> implements
		FederationModelComponent
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(FederationModel.class);

	/** */
	private final Map<String, Datacenter> datacenters = new HashMap<String, Datacenter>();

	/** the initial number of datacenters in this federation */
	private final int initialSize;

	/** whether remote allocation can occur within this federation */
	private final boolean remoteAlloc;

	/** whether remote allocation can occur within this federation */
	private final boolean broker;

	/** */
	public FederationModel(final int initialSize, final boolean remoteAlloc,
			final boolean broker)
	{
		super("DC_FEDERATION");
		this.initialSize = initialSize;
		this.remoteAlloc = remoteAlloc;
		this.broker = broker;
	}

	/**
	 * @return
	 */
	public boolean hasRemoteAllocation()
	{
		return this.remoteAlloc;
	}

	/** @see io.coala.dsol.util.DsolModel#onInitialize() */
	@Override
	public void onInitialize() throws Exception
	{
		for (int i = 0; i < this.initialSize; i++)
			getSimulator().scheduleEvent(0, this, this, ADD_DC_METHOD_ID,
					NO_ARGS);
		LOG.trace("Initialization complete");
	}

	/**
	 * @return
	 */
	public Collection<Datacenter> getDatacenters()
	{
		return this.datacenters.values();
	}

	/** */
	private static final String ADD_DC_METHOD_ID = "newDatacenter";

	/** */
	public void newDatacenter() throws Exception
	{
		final Datacenter dc = new Datacenter(this);

		// setup DC powermix over time
		final int intervalHours = 4;
		for (DateTime t = getDateTime().withTimeAtStartOfDay().plusHours(2); t
				.isBefore(getInterval().getEndMillis()); t = t
				.plusHours(intervalHours))
		{
			final int hour = t.hourOfDay().get();
			final boolean isDayTime = hour > 5 && hour < 18;
			final boolean isExtreme = getRNG().nextBoolean();
			if (isExtreme)
			{
				final double emissionFactor = isDayTime ? .33 : 1.66;
				dc.newPowermix(new Interval(t, t.plusHours(intervalHours)),
						emissionFactor);

				// setup EP/DC adaptions for 10% of abnormal/extreme cases
				if (getRNG().nextDouble() < .10)
				{
					final boolean extraOrSaving = getRNG().nextBoolean();
					final double consumptionFactor = extraOrSaving ? 1.5 : .7;
					dc.newAdaption(
							new Interval(t.plusHours(1), t.plusHours(3)),
							consumptionFactor);
				}
			}
		}

		// LOG.trace(dc.getName() + " insourceSchedule: "
		// + dc.insourceSchedule.values());
		this.datacenters.put(dc.getName(), dc);
		fireEvent(NEW_DC, dc);
	}

	/**
	 * @return
	 */
	public boolean hasBroker()
	{
		return this.broker;
	}

}
