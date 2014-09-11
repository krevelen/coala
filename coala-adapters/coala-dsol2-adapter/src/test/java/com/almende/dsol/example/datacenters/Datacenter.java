/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/Datacenter.java $
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

import io.coala.dsol.util.DsolUtil;
import io.coala.log.LogUtil;

import java.rmi.RemoteException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.formalisms.eventscheduling.SimEvent;
import nl.tudelft.simulation.event.EventInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.jstats.distributions.DistConstant;
import nl.tudelft.simulation.jstats.distributions.DistContinuous;
import nl.tudelft.simulation.jstats.streams.StreamInterface;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * Datacenter
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 * 
 */
public class Datacenter extends AbstractFederationModelComponent implements
		Comparable<Datacenter> {

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(Datacenter.class);

	/** */
	public static int ID_COUNT = 0;

	/** DC Energy Emissions (kg) per kWh */
	private final double emissionsPerKWh;// 1.66d, 5.0, 8.33

	/** DC Energy Costs (EUR) per kWh */
	private final double cashPerKWh;// = 0.1

	/** service power (Watt) = energy (J) over time (s) */
	private final DistContinuous consumptionKW;

	/** service cash flow (EUR) per hour */
	private final DistContinuous hourlyCashflow;

	/** DC cash flow (EUR) */
	private final Accumulator cashflow;

	/** DC emissions (kg CO2) */
	private final Accumulator emissionsKgCO2;

	/** DC power consumption (KWh) */
	private final Accumulator consumptionKWh;

	/** DC current service count */
	private final Indicator currentServiceCount;

	/** DC total service count */
	private final Indicator totalServiceCount;

	/** DC total service count */
	public final Indicator exchangedServiceCount;

	/** DC total service count */
	public final Indicator workloadHours;

	/** DC total service count */
	public final Indicator adaptionHours;

	/** */
	public final Indicator emissionFactor;

	/** */
	public final Indicator consumptionFactor;

	/** */
	protected final NavigableMap<DateTime, Interval> outsourceSchedule = new ConcurrentSkipListMap<>();

	/** */
	protected final NavigableMap<DateTime, Interval> insourceSchedule = new ConcurrentSkipListMap<>();

	/**
	 * @param model
	 * @throws RemoteException
	 * @throws SimRuntimeException
	 */
	public Datacenter(final FederationModel model) throws Exception {
		super(model,"DC" + ID_COUNT++);

		final String indicatorName = getName();
		this.cashflow = new Accumulator(0d, model, "Cash (flow)",
				"Money (EUR)", indicatorName, 0d, TimeUnitInterface.HOUR);

		this.emissionsKgCO2 = new Accumulator(0d, model,
				"Carbon Emissions (rate)", "CO2 (kg/kW)", indicatorName, 0d,
				TimeUnitInterface.HOUR);

		// idle (kW) 20 servers*100 watt(idle)=2 kw (idle * consumption)
		this.consumptionKWh = new Accumulator(0d, model,
				"Energy Consumption (rate)", "Energy (kW)", indicatorName, 2d,
				TimeUnitInterface.HOUR);

		this.currentServiceCount = new Indicator(model, "Current Services",
				"Services (#)", indicatorName, 0d);

		this.totalServiceCount = new Indicator(model, "Total Services",
				"Services (#)", indicatorName, 0d);

		this.exchangedServiceCount = new Indicator(model, "Exchanges (#)",
				"Exchanges (#)", indicatorName, 0d);

		this.workloadHours = new Indicator(model, "Workload Duration",
				"Hours (#)", indicatorName, 0d);

		this.adaptionHours = new Indicator(model, "Adaption Duration",
				"Hours (#)", indicatorName, 0d);

		this.emissionFactor = new Indicator(model, "Power Mix Variation",
				"Carbon Emission factor", indicatorName, 1d);

		this.consumptionFactor = new Indicator(model, "Power Adaption",
				"Energy Consumption factor", indicatorName, 1d);

		final StreamInterface stream = model.getSimulator().getReplication()
				.getStream(FederationModel.RNG_ID);

		this.cashPerKWh = 0.1d;// new DistConstant(stream, .1d);
		this.emissionsPerKWh = 5.0d;// new DistConstant(stream, 5.0d);
		this.consumptionKW = new DistConstant(stream, .1d);
		this.hourlyCashflow = new DistConstant(stream, .1d);

		// add demand and listen for new demand events
		final DemandGenerator demand = new DemandGenerator(model);
		demand.addListener(new EventListenerInterface() {

			@Override
			public void notify(final EventInterface event)
					throws RemoteException {
				try {
					newService((Interval) event.getContent());
				} catch (final SimRuntimeException e) {
					LOG.error("Problem scheduling new service", e);
				}
			}
		}, DemandGenerator.NEW_DEMAND);
	}

	/* @see FederationModelComponent#getName() */
	@Override
	public String toString() {
		return getName();
	}

	public Accumulator getCashflow() {
		return this.cashflow;
	}

	public Accumulator getEmissionsKgCO2() {
		return this.emissionsKgCO2;
	}

	public Accumulator getConsumptionKWh() {
		return this.consumptionKWh;
	}

	public Indicator getCurrentServiceCount() {
		return this.currentServiceCount;
	}

	public Indicator getTotalServiceCount() {
		return this.totalServiceCount;
	}

	private boolean isSavingsMode(final DateTime date) {
		final Map.Entry<DateTime, Interval> lastBefore = this.outsourceSchedule
				.floorEntry(date);
		if (lastBefore != null && lastBefore.getValue().contains(date))
			return true;
		final Map.Entry<DateTime, Interval> firstAfter = this.outsourceSchedule
				.ceilingEntry(date);
		if (firstAfter != null && firstAfter.getValue().contains(date))
			return true;
		return false;
	}

	private boolean isExtraMode(final DateTime date) {
		final Map.Entry<DateTime, Interval> lastBefore = this.insourceSchedule
				.floorEntry(date);
		if (lastBefore != null && lastBefore.getValue().contains(date))
			return true;
		final Map.Entry<DateTime, Interval> firstAfter = this.insourceSchedule
				.ceilingEntry(date);
		if (firstAfter != null && firstAfter.getValue().contains(date))
			return true;
		return false;
	}

	protected void newService(final Interval interval) throws RemoteException,
			SimRuntimeException {
		if (getModel().hasRemoteAllocation()) {
			if (isSavingsMode(interval.getStart())) {
				if (getModel().hasBroker())
					for (Datacenter dc : getModel().getDatacenters()) {
						if (dc == this)
							continue;

						if (dc.isExtraMode(interval.getStart())) {
							newService(interval, .1, .1);
							dc.newService(interval, 1.0, .9);
							// LOG.trace(getName() + " Outsourced to EXTRA dc "
							// + interval);
							return;
						}
					}
				for (Datacenter dc : getModel().getDatacenters()) {
					if (dc == this)
						continue;

					if (!dc.isSavingsMode(interval.getStart())) {
						newService(interval, .1, .1);
						dc.newService(interval, 1.0, .9);
						return;
					}
				}

				LOG.trace(getName() + " Could not remotely allocate service "
						+ interval);
			}
			// else
			// LOG.trace(getName()
			// + " Running locally during EXTRA mode, service: "
			// + interval);
		}
		// else
		// LOG.trace(getName()
		// + " Running locally during REGULAR mode, service: "
		// + interval);
		newService(interval, 1.0, 1.0);
		newService(interval, 0.0, 0.0);
	}

	protected void newService(final Interval interval,
			final double consumptionFactor, final double cashFlowFactor)
			throws RemoteException, SimRuntimeException {
		final Service service = new Service(getModel(), this, interval,
				this.consumptionKW.draw() * consumptionFactor,
				this.hourlyCashflow.draw() * cashFlowFactor);

		final Interval cropped = DsolUtil.crop(interval, getTreatment());
		this.workloadHours.addValue(DsolUtil.toTimeUnit(
				TimeUnitInterface.HOUR, cropped.toDurationMillis(),
				TimeUnitInterface.MILLISECOND));

		// listen for service start and stop events
		service.addListener(new EventListenerInterface() {

			@Override
			public void notify(final EventInterface event) {
				if (consumptionFactor == 0.0)
					return;
				getCurrentServiceCount().addValue(1);
				getTotalServiceCount().addValue(1);
				if (consumptionFactor < 1.0)
					exchangedServiceCount.addValue(1);
			}
		}, Service.SERVICE_STARTED);

		service.addListener(new EventListenerInterface() {

			@Override
			public void notify(final EventInterface event) {
				getCurrentServiceCount().addValue(-1);
			}
		}, Service.SERVICE_COMPLETED);

		service.addListener(new EventListenerInterface() {

			@Override
			public void notify(final EventInterface event) {
				final Number kW = (Number) event.getContent();
				getConsumptionKWh().addRate(
						kW.doubleValue()
								* Datacenter.this.consumptionFactor.getValue()
										.doubleValue());
				getEmissionsKgCO2().addRate(
						kW.doubleValue()
								* Datacenter.this.consumptionFactor.getValue()
										.doubleValue()
								* Datacenter.this.emissionsPerKWh
								* Datacenter.this.emissionFactor.getValue()
										.doubleValue());
				getCashflow().addRate(
						kW.doubleValue() * Datacenter.this.cashPerKWh);
			}
		}, Service.CONSUMPTION_CHANGED);

		service.addListener(new EventListenerInterface() {

			@Override
			public void notify(final EventInterface event) {
				getCashflow().addRate((Number) event.getContent());
			}
		}, Service.CASHFLOW_CHANGED);

	}

	protected void newAdaption(final Interval interval,
			final double consumptionFactor) throws RemoteException,
			SimRuntimeException {
		if (consumptionFactor > 1.0)
			this.outsourceSchedule.put(interval.getStart(), interval);
		else if (consumptionFactor < 1.0)
			this.insourceSchedule.put(interval.getStart(), interval);

		final double startTime = simTime(interval.getStart());
		final double endTime = simTime(interval.getEnd());
		this.adaptionHours.addValue(DsolUtil.toTimeUnit(
				TimeUnitInterface.HOUR, interval.toDuration().getMillis(),
				TimeUnitInterface.MILLISECOND));
		getSimulator().scheduleEvent(
				new SimEvent(startTime, this, this,
						APPLY_CONSUMPTION_FACTOR_METHOD_ID, new Object[] {
								consumptionFactor, false }));
		getSimulator().scheduleEvent(
				new SimEvent(endTime, this, this,
						APPLY_CONSUMPTION_FACTOR_METHOD_ID, new Object[] {
								1.0 / consumptionFactor, true }));
	}

	protected void newPowermix(final Interval interval,
			final double emissionFactor) throws RemoteException,
			SimRuntimeException {
		final double startTime = simTime(interval.getStart());
		final double endTime = simTime(interval.getEnd());
		getSimulator().scheduleEvent(
				new SimEvent(startTime, this, this,
						APPLY_EMISSION_FACTOR_METHOD_ID, new Object[] {
								emissionFactor, false }));
		getSimulator().scheduleEvent(
				new SimEvent(endTime, this, this,
						APPLY_EMISSION_FACTOR_METHOD_ID, new Object[] {
								1.0 / emissionFactor, true }));
	}

	private static final Number ONE = 1.0d;

	private static final String APPLY_CONSUMPTION_FACTOR_METHOD_ID = "applyConsumptionFactor";

	protected void applyConsumptionFactor(final double consumptionFactor,
			final boolean reset) {
		getConsumptionKWh().multiplyRate(consumptionFactor);
		this.consumptionFactor.setValue(reset ? ONE : consumptionFactor);

		// TODO live (re)migration of some active workload?
	}

	private static final String APPLY_EMISSION_FACTOR_METHOD_ID = "applyEmissionFactor";

	protected void applyEmissionFactor(final double emissionFactor,
			final boolean reset) {
		getEmissionsKgCO2().multiplyRate(emissionFactor);
		this.emissionFactor.setValue(reset ? ONE : emissionFactor);
	}

	@Override
	public int compareTo(final Datacenter o) {
		return getName().compareTo(o.getName());
	}

}
