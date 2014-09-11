/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/model/Indicator.java $
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
package io.coala.model;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.event.AbstractTimedEvent;
import io.coala.event.TimedEventID;
import io.coala.log.LogUtil;
import io.coala.model.Indicator.ValueChangedEvent;
import io.coala.time.Instant;

import java.rmi.RemoteException;

import org.apache.log4j.Logger;

import com.eaio.uuid.UUID;

/**
 * {@link Indicator}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link SimulatorInterface}
 * @param <M> the type of {@link DsolModel}
 * @param <THIS> the final type of {@link Indicator}
 */
public class Indicator<A extends Agent, I extends Instant<I>, THIS extends Indicator<A, I, THIS>>
		extends
		AbstractModelComponent<BasicModelComponentID, A, ValueChangedEvent<I>>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(Indicator.class);

	/** */
	public static class ValueChangedEvent<I extends Instant<I>> extends
			AbstractTimedEvent<TimedEventID<UUID, I>>
	{

		/** */
		private static final long serialVersionUID = 1L;

		/** */
		private final AgentID ownerID;

		/** */
		private final Number oldValue;

		/** */
		private final Number newValue;

		/**
		 * {@link ValueChangedEvent} constructor
		 * 
		 * @param source
		 * @param simTime
		 * @param oldValue
		 * @param newValue
		 */
		public ValueChangedEvent(final Indicator<?, I, ?> source,
				final I simTime, final Number oldValue, final Number newValue)
		{
			super(new TimedEventID<UUID, I>(source.getID().getModelID(),
					new UUID(), simTime), source.getOwnerID(), source.getID());
			this.ownerID = source.getOwnerID();
			this.oldValue = oldValue;
			this.newValue = newValue;
		}

		public Number getOldValue()
		{
			return this.oldValue;
		}

		public Number getNewValue()
		{
			return this.newValue;
		}

		public Number getValueChange()
		{
			return getNewValue().doubleValue() - getOldValue().doubleValue();
		}

		/** @see ModelComponent#getOwnerID() */
		@Override
		public AgentID getOwnerID()
		{
			return this.ownerID;
		}

		/** @see ModelComponent#getTime() */
		@Override
		public Instant<?> getTime()
		{
			return getID().getTime();
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
	 * {@link Indicator} constructor
	 * 
	 * @param owner
	 * @param name
	 */
	public Indicator(final A owner, final String name)
	{
		super(new BasicModelComponentID(owner.getID().getModelID(), name),
				owner);
	}

	/**
	 * {@link Indicator} constructor
	 * 
	 * @param owner the agent that owns this indicator
	 * @param title the (chart) title of what values actually indicate
	 * @param unitName the (axis) unit name of what a value represents
	 * @param name the name of the indicator itself (for logging etc.)
	 * @param initialValue the initial value
	 * @throws SimRuntimeException
	 * @throws RemoteException
	 */
	public Indicator(final A owner, final String name, final String title,
			final String unitName, final Number initialValue)
	{
		super(new BasicModelComponentID(owner.getID().getModelID(), name),
				owner);
		withValueTitle(title);
		withValueUnitName(unitName);
		this.value = initialValue; // don't use the setter to prevent premature
									// event firing

		/* FIXME
		getSimulator().schedule(new Job(){}, trigger);
		getSimulator().schedule(0, this, this, SET_VALUE_METHOD_ID,
				new Object[] { initialValue });
				*/
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

	/** @return the current value of this {@link Indicator} */
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

	/** @param value the new value to set for this {@link Indicator} */
	@SuppressWarnings("unchecked")
	public synchronized void setValue(final Number value)
	{
		final double currentValue = this.value == null ? 0 : this.value
				.doubleValue();
		if (this.valueMin != null
				&& value.doubleValue() < this.valueMin.doubleValue())
		{
			LOG.trace("Cropping [" + getID() + "] " + getValueTitle()
					+ " value " + value + " to minimum: " + this.valueMin);
			this.value = this.valueMin;
		} else if (this.valueMax != null
				&& value.doubleValue() > this.valueMax.doubleValue())
		{
			LOG.trace("Cropping [" + getID() + "] " + getValueTitle()
					+ " value " + value + " to maximum: " + this.valueMax);
			this.value = this.valueMax;
		} else
		{
			this.value = value;
		}
		if (currentValue != value.doubleValue())
			fireEvent(new ValueChangedEvent<I>(this, (I) getTime(),
					currentValue, value.doubleValue()));
	}

	/** @param valueTitle the new title to set for this {@link Indicator} */
	public void setValueUnitName(final String unit)
	{
		this.valueUnitName = unit;
	}

	/** @param title the new title to set for this {@link Indicator} */
	public void setTitle(final String title)
	{
		this.valueTitle = title;
	}

	/** @param minimum the minimum value to set for this {@link Indicator} */
	public synchronized void setValueMinimum(final Number minimum)
	{
		this.valueMin = minimum;
	}

	/** @param maximum the maximum value to set for this {@link Indicator} */
	public synchronized void setValueMaximum(final Number maximum)
	{
		this.valueMax = maximum;
	}

}
