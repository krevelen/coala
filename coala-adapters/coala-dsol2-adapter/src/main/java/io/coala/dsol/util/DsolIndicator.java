/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolIndicator.java $
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

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.SimRuntimeException;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;
import nl.tudelft.simulation.event.EventListenerInterface;
import nl.tudelft.simulation.event.EventType;
import nl.tudelft.simulation.event.TimedEvent;

import org.apache.log4j.Logger;

/**
 * {@link DsolIndicator}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link SimulatorInterface}
 * @param <M> the type of {@link DsolModel}
 * @param <THIS> the final type of {@link DsolIndicator}
 */
public class DsolIndicator<S extends DEVSSimulatorInterface, M extends DsolModel<S, M>, THIS extends DsolIndicator<S, M, THIS>>
		extends AbstractDsolModelComponent<S, M>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(DsolIndicator.class);

	/** */
	public static final EventType VALUE_CHANGED = new EventType("value changed");

	/** */
	public static class ValueChangedEvent extends TimedEvent
	{

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * {@link ValueChangedEvent} constructor
		 * 
		 * @param source
		 * @param simTime
		 * @param oldValue
		 * @param newValue
		 */
		public ValueChangedEvent(final DsolIndicator<?, ?, ?> source,
				final double simTime, final Number oldValue,
				final Number newValue)
		{
			super(VALUE_CHANGED, source, new Number[] { oldValue, newValue },
					simTime);
		}

		public Number getOldValue()
		{
			return ((Number[]) getContent())[0];
		}

		public Number getNewValue()
		{
			return ((Number[]) getContent())[1];
		}

		public Number getValueChange()
		{
			return getNewValue().doubleValue() - getOldValue().doubleValue();
		}

	}

	/** */
	private String valueTitle;

	/** */
	private String valueUnitName;

	/** */
	protected Number value = null;

	/** */
	private Number valueMin = null;

	/** */
	private Number valueMax = null;

	/** */
	private long valueReadCount = 0;

	/**
	 * {@link DsolIndicator} constructor
	 * 
	 * @param model
	 * @param name
	 */
	public DsolIndicator(final M model, final String name)
	{
		super(model, name);
	}

	/**
	 * {@link DsolIndicator} constructor
	 * 
	 * @param model the source model that contains this indicator
	 * @param title the (chart) title of what values actually indicate
	 * @param unitName the (axis) unit name of what a value represents
	 * @param name the name of the indicator itself (for logging etc.)
	 * @param initialValue the initial value
	 * @throws SimRuntimeException
	 * @throws RemoteException
	 */
	public DsolIndicator(final M model, final String name, final String title,
			final String unitName, final Number initialValue)
			throws RemoteException, SimRuntimeException
	{
		super(model, name);
		withValueTitle(title);
		withValueUnitName(unitName);
		this.value = initialValue; // don't use the setter to prevent premature
									// event firing
		getSimulator().scheduleEvent(0, this, this, SET_VALUE_METHOD_ID,
				new Object[] { initialValue });
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withValue(final Number value)
	{
		setValue(value);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public synchronized THIS withValueRange(final Number min, final Number max)
	{
		this.valueMin = min;
		this.valueMax = max;
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public THIS withValueUnitName(final String unit)
	{
		setValueUnitName(unit);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public THIS withValueTitle(final String title)
	{
		setTitle(title);
		return (THIS) this;
	}

	@SuppressWarnings("unchecked")
	public THIS withValueChangeListener(final EventListenerInterface listener)
	{
		if (!addListener(listener, VALUE_CHANGED))
			LOG.warn("Listener already added");
		return (THIS) this;
	}

	/** @return the unit of this indicator's value */
	public String getValueUnitName()
	{
		return this.valueUnitName;
	}

	/** @return the title of this indicator (for graphs etc.) */
	public String getValueTitle()
	{
		return this.valueTitle;
	}

	/** @return the current value of this {@link DsolIndicator} */
	public synchronized Number getValue()
	{
		this.valueReadCount++;
		return this.value;
	}

	/** @return the number of times the value was read */
	public long getValueReadCount()
	{
		return this.valueReadCount;
	}

	/** @param delta the difference to add to the current value */
	public synchronized void addValue(final Number delta)
	{
		setValue(this.value == null ? delta : this.value.doubleValue()
				+ delta.doubleValue());
	}

	/** used for scheduling the initial value */
	public static final String SET_VALUE_METHOD_ID = "setValue";

	/** @param value the new value to set for this {@link DsolIndicator} */
	public synchronized void setValue(final Number value)
	{
		if (this.valueMin != null
				&& value.doubleValue() < this.valueMin.doubleValue())
		{
			LOG.trace("Cropping [" + getName() + "] " + getValueTitle()
					+ " value " + value + " to minimum: " + this.valueMin);
			this.value = this.valueMin;
		} else if (this.valueMax != null
				&& value.doubleValue() > this.valueMax.doubleValue())
		{
			LOG.trace("Cropping [" + getName() + "] " + getValueTitle()
					+ " value " + value + " to maximum: " + this.valueMax);
			this.value = this.valueMax;
		} else
		{
			final double currentValue = this.value == null ? 0 : this.value
					.doubleValue();
			this.value = value;
			fireEvent(new ValueChangedEvent(this, simTime(), currentValue,
					value.doubleValue()));
		}
	}

	/** @param valueTitle the new title to set for this {@link DsolIndicator} */
	public void setValueUnitName(final String unit)
	{
		this.valueUnitName = unit;
	}

	/** @param title the new title to set for this {@link DsolIndicator} */
	public void setTitle(final String title)
	{
		this.valueTitle = title;
	}

	/** @param minimum the minimum value to set for this {@link DsolIndicator} */
	public synchronized void setValueMinimum(final Number minimum)
	{
		this.valueMin = minimum;
	}

	/** @param maximum the maximum value to set for this {@link DsolIndicator} */
	public synchronized void setValueMaximum(final Number maximum)
	{
		this.valueMax = maximum;
	}

}
