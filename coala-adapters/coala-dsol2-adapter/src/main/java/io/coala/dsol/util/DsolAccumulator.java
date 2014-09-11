/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolAccumulator.java $
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

import io.coala.log.LogUtil;
import io.coala.time.TimeUnit;
import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.TimedEvent;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

/**
 * {@link DsolAccumulator}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link SimulatorInterface}
 * @param <M> the type of {@link DsolModel}
 * @param <THIS> the final type of {@link DsolAccumulator}
 */
public class DsolAccumulator<S extends DEVSSimulatorInterface, M extends DsolModel<S, M>, THIS extends DsolAccumulator<S, M, THIS>>
		extends DsolIndicator<S, M, THIS>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(DsolAccumulator.class);

	/** */
	public static final EventType RATE_CHANGED = new EventType("rate changed");

	/** */
	public static class RateChangedEvent extends TimedEvent
	{

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * {@link RateChangedEvent} constructor
		 * 
		 * @param source
		 * @param simTime
		 * @param oldRate
		 * @param newRate
		 */
		public RateChangedEvent(final DsolAccumulator<?, ?, ?> source,
				final double simTime, final Number oldRate, final Number newRate)
		{
			super(RATE_CHANGED, source, new Number[] { oldRate, newRate },
					simTime);
		}

		public Number getOldRate()
		{
			return ((Number[]) getContent())[0];
		}

		public Number getNewRate()
		{
			return ((Number[]) getContent())[1];
		}

		public Number getRateChange()
		{
			return getNewRate().doubleValue() - getOldRate().doubleValue();
		}

	}

	/** */
	private String rateTitle;

	/** */
	private Number rate = null;

	/** */
	private Number rateMin = null;

	/** */
	private Number rateMax = null;

	/** */
	private Number integrateMin = null;

	/** */
	private Number integrateMax = null;

	/** */
	private String rateUnitName;

	/** TODO use {@link TimeUnit} the unit of time for accumulating this rate */
	private TimeUnitInterface timeUnit;

	/** */
	private DateTime lastChangeTime = null;

	/** */
	private long rateReadCount = 0;

	/**
	 * {@link DsolAccumulator} constructor
	 * 
	 * @param initialValue
	 * @param model
	 * @param valueTitle
	 * @param valueUnitName
	 * @param name
	 * @param initialRate
	 * @param timeUnit
	 * @throws Exception if setting of initial value could not be scheduled
	 */
	public DsolAccumulator(final M model, final String name,
			final String valueTitle, final String valueUnitName,
			final Number initialValue, final String rateTitle,
			final String rateUnitName, final Number initialRate,
			final TimeUnitInterface timeUnit) throws Exception
	{
		super(model, name, valueTitle, valueUnitName, initialValue);
		withRateTitle(rateTitle);
		withRateUnitName(rateUnitName);
		this.rate = initialRate; // don't use setter to avoid premature event
									// firing
		withTimeUnit(timeUnit);
		this.lastChangeTime = DsolUtil.getDateTime(model.getSimulator());
		getSimulator().scheduleEvent(0, this, this, SET_RATE_METHOD_ID,
				new Object[] { initialRate });
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withRate(final Number value)
	{
		setValue(value);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withRateRange(final Number min, final Number max)
	{
		if (max.doubleValue() < min.doubleValue())
		{
			this.rateMin = max;
			this.rateMax = min;
		} else
		{
			this.rateMin = min;
			this.rateMax = max;
		}
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withIntegrateRange(final Number min,
			final Number max)
	{
		if (max.doubleValue() < min.doubleValue())
		{
			this.integrateMin = max;
			this.integrateMax = min;
		} else
		{
			this.integrateMin = min;
			this.integrateMax = max;
		}
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withRateUnitName(final String unit)
	{
		setRateUnitName(unit);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withRateTitle(final String title)
	{
		setRateTitle(title);
		return (THIS) this;
	}

	public synchronized THIS withTimeUnit(final TimeUnit unit)
	{
		return withTimeUnit(DsolUtil.toTimeUnit(unit));
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withTimeUnit(final TimeUnitInterface unit)
	{
		setTimeUnit(unit);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public THIS withRateChangeListener(final EventListenerInterface listener)
	{
		if (!addListener(listener, RATE_CHANGED))
			LOG.warn("Listener already added");
		return (THIS) this;
	}

	/** @return the title of this {@link DsolAccumulator}'s rate */
	public String getRateTitle()
	{
		return this.rateTitle;
	}

	/** @return the unit name of this {@link DsolAccumulator}'s rate */
	public String getRateUnitName()
	{
		return this.rateUnitName;
	}

	/** @return the rate time unit of this {@link DsolAccumulator} */
	public TimeUnitInterface getTimeUnit()
	{
		return this.timeUnit;
	}

	/** helper method */
	protected void updateValue()
	{
		final DateTime changeTime = getDateTime();
		if (!changeTime.isAfter(this.lastChangeTime))
			return;
		final Interval interval = DsolUtil.crop(new Interval(
				this.lastChangeTime, changeTime), getTreatment());
		if (interval.getEndMillis() != changeTime.getMillis())
			LOG.warn(String.format("Cropped interval end time %s to %s",
					changeTime, interval));
		this.lastChangeTime = changeTime;

		final double oldRate = getRate().doubleValue();
		double deltaRate = oldRate;
		if (this.integrateMin != null
				&& this.integrateMin.doubleValue() > deltaRate)
		{
			// LOG.trace("Integrating " + getRateTitle() + " with minimum "
			// + this.integrateMin);
			deltaRate = this.integrateMin.doubleValue();
		} else if (this.integrateMax != null
				&& deltaRate > this.integrateMax.doubleValue())
		{
			// LOG.trace("Integrating " + getRateTitle() + " with maximum "
			// + this.integrateMax);
			deltaRate = this.integrateMax.doubleValue();
		}
		final double deltaValue = DsolUtil.toTimeUnit(this.timeUnit,
				deltaRate * interval.toDurationMillis(),
				TimeUnitInterface.MILLISECOND).doubleValue();
		addValue(deltaValue);
	}

	/** used for scheduling the initial value */
	public static final String SET_RATE_METHOD_ID = "setRate";

	/** @param title the rate title to set for this {@link DsolAccumulator} */
	public synchronized void setRateTitle(final String title)
	{
		this.rateTitle = title;
	}

	/** @param title the rate unit name to set for this {@link DsolAccumulator} */
	public synchronized void setRateUnitName(final String unit)
	{
		this.rateUnitName = unit;
	}

	/** @param title the rate unit name to set for this {@link DsolAccumulator} */
	public synchronized void setTimeUnit(final TimeUnit unit)
	{
		setTimeUnit(DsolUtil.toTimeUnit(unit));
	}

	/** @param title the rate unit name to set for this {@link DsolAccumulator} */
	public synchronized void setTimeUnit(final TimeUnitInterface unit)
	{
		final Number currentValue = super.value;
		this.timeUnit = unit;
		if (currentValue != null && currentValue.doubleValue() != 0.0)
			setValue(DsolUtil.toTimeUnit(unit, currentValue.doubleValue(),
					getTimeUnit()));
	}

	@Override
	public synchronized Number getValue()
	{
		updateValue();
		return super.getValue();
	}

	/** */
	public synchronized Number getValue(final TimeUnit unit)
	{
		return getValue(DsolUtil.toTimeUnit(unit));
	}

	/** */
	public synchronized Number getValue(final TimeUnitInterface unit)
	{
		return DsolUtil.toTimeUnit(unit, getValue(), this.timeUnit);
	}

	/** @param rate the new rate to set for this {@link DsolAccumulator} */
	public synchronized void setRate(final Number rate)
	{
		updateValue();
		final double currentRate = this.rate == null ? 0 : this.rate
				.doubleValue();
		if (this.rateMin != null
				&& rate.doubleValue() < this.rateMin.doubleValue())
		{
			LOG.trace("Cropping [" + getName() + "] " + getRateTitle()
					+ " rate " + rate + " to minimum: " + this.rateMin);
			this.rate = this.rateMin;
		} else if (this.rateMax != null
				&& rate.doubleValue() > this.rateMax.doubleValue())
		{
			LOG.trace("Cropping [" + getName() + "] " + getRateTitle()
					+ " rate " + rate + " to maximum: " + this.rateMax);
			this.rate = this.rateMax;
		} else
		{
			this.rate = rate;
		}
		fireEvent(new RateChangedEvent(this, simTime(), currentRate,
				rate.doubleValue()));
	}

	/** @return the number of times the rate was read */
	public long getRateReadCount()
	{
		return this.rateReadCount;
	}

	/** @return the current rate of this {@link DsolAccumulator} */
	public synchronized Number getRate()
	{
		this.rateReadCount++;
		return this.rate;
	}

	/** @return the current rate for this {@link DsolAccumulator} */
	public synchronized Number getRate(final TimeUnit unit)
	{
		return getRate(DsolUtil.toTimeUnit(unit));
	}

	/** @return the current rate for this {@link DsolAccumulator} */
	public synchronized Number getRate(final TimeUnitInterface unit)
	{
		return DsolUtil.toTimeUnit(unit, getRate(), this.timeUnit);
	}

	/** @param rateFactor the factor by which to multiply the current rate */
	public synchronized void multiplyRate(final Number rateFactor)
	{
		final double oldRate = getRate().doubleValue();
		final double newRate = oldRate * rateFactor.doubleValue();
		// LOG.trace(String.format(
		// "%s multiply %.3f by %.3f >> delta = %.3f * %.3f = %.3f",
		// getName(), oldRate, rateFactor.doubleValue(),
		// rateFactor.doubleValue() - 1, oldRate, newRate));
		setRate(newRate);
	}

	/** used for scheduling the rate changes */
	public static final String ADD_RATE_METHOD_ID = "addRate";

	/** @param deltaRate the difference to add to the current rate */
	public synchronized void addRate(final Number deltaRate)
	{
		final double oldRate = getRate().doubleValue();
		final double newRate = oldRate + deltaRate.doubleValue();
		// LOG.trace(String.format("t=%.4f (%s) %s %s %s %.5f = %.5f %s",
		// simTime(), changeTime, getName(), getRateTitle(),
		// deltaRate.floatValue() < 0f ? "-" : "+",
		// Math.abs(deltaRate.floatValue()), newRate, getRateUnitName()));
		setRate(newRate);
	}

}
