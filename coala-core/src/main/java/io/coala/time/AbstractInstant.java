/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/AbstractInstant.java $
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
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package io.coala.time;

import io.coala.exception.CoalaRuntimeException;
import io.coala.json.JSONConvertible;
import io.coala.json.JsonUtil;
import io.coala.log.InjectLogger;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.inject.Inject;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link AbstractInstant}
 * 
 * @date $Date: 2014-08-12 17:43:51 +0200 (Tue, 12 Aug 2014) $
 * @version $Revision: 361 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the concrete sub-type of {@link Instant}
 */
@Embeddable
public abstract class AbstractInstant<THIS extends AbstractInstant<THIS>>
// extends Number
		implements Instant<THIS>, JSONConvertible<THIS>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private static Logger LOG;

	/** */
	@Embedded
	private ClockID clockID;

	/** */
	private Number value;

	/** */
	private TimeUnit unit;

	/**
	 * {@link AbstractInstant} constructor
	 */
	protected AbstractInstant()
	{
		//
	}

	/**
	 * {@link AbstractInstant} constructor
	 * 
	 * @param value
	 */
	@Inject
	public AbstractInstant(final ClockID source, final Number value,
			final TimeUnit unit)
	{
		setClockID(source);
		setValue(value);
		setUnit(unit);
	}

	/** @see io.coala.time.Instant#getClockID() */
	@Override
	public synchronized ClockID getClockID()
	{
		return this.clockID;
	}

	/**
	 * @param source the source to set
	 */
	protected synchronized void setClockID(ClockID source)
	{
		this.clockID = source;
	}

	/** @see io.coala.time.Instant#getValue() */
	@Override
	public synchronized Number getValue()
	{
		return this.value;
	}

	/**
	 * @param value the value to set
	 */
	protected synchronized void setValue(Number value)
	{
		this.value = value;
	}

	/** @see Instant#getUnit() */
	@Override
	public synchronized TimeUnit getUnit()
	{
		return this.unit;
	}

	/**
	 * @param unit the unit to set
	 */
	protected synchronized void setUnit(TimeUnit unit)
	{
		this.unit = unit;
	}

	/**
	 * @param clockID
	 * @param value
	 * @param unit
	 * @return this {@link Instant} object
	 */
	@SuppressWarnings("unchecked")
	public THIS withTime(final ClockID clockID, final Number value,
			final TimeUnit unit)
	{
		setValue(value);
		setUnit(unit);
		setClockID(clockID);
		return (THIS) this;
	}

	/** @see Object#toString() */
	@Override
	public String toString()
	{
		return String.format("%s %s @%s", getValue(), getUnit(), getClockID());
	}

	/** @see Comparable#compareTo(Object) */
	@Override
	public int compareTo(final Instant<?> other)
	{
		final int compareClockID = getClockID().compareTo(other.getClockID());
		if (compareClockID != 0)
			return compareClockID;

		return Double.compare(doubleValue(), other.toUnit(getUnit())
				.doubleValue());
	}

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((this.clockID == null) ? 0 : this.clockID.hashCode());
		result = prime * result
				+ ((this.unit == null) ? 0 : this.unit.hashCode());
		result = prime * result
				+ ((this.value == null) ? 0 : this.value.hashCode());
		return result;
	}

	/** @see Object#equals(Object) */
	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;

		final THIS other = (THIS) obj;

		if (getClockID() == null)
		{
			if (other.getClockID() != null)
				return false;
		} else if (!getClockID().equals(other.getClockID()))
			return false;

		if (this.getUnit() != other.getUnit())
			return false;

		if (this.getValue() == null)
		{
			if (other.getValue() != null)
				return false;
		} else if (!this.getValue().equals(other.getValue()))
			return false;

		return true;
	}

	/** @see Number#intValue() */
	@Override
	public int intValue()
	{
		return getValue().intValue();
	}

	/** @see Number#longValue() */
	@Override
	public long longValue()
	{
		return getValue().longValue();
	}

	/** @see Number#floatValue() */
	@Override
	public float floatValue()
	{
		return getValue().floatValue();
	}

	/** @see Number#doubleValue() */
	@Override
	public double doubleValue()
	{
		return getValue().doubleValue();
	}

	// /** @see Instant#toBaseUnit() */
	// @Override
	// public THIS toBaseUnit()
	// {
	// return toUnit(getBaseUnit());
	// }

	/** @see Instant#toNanoseconds() */
	@Override
	public THIS toNanoseconds()
	{
		return toUnit(TimeUnit.NANOS);
	}

	/** @see Instant#toMilliseconds() */
	@Override
	public THIS toMilliseconds()
	{
		return toUnit(TimeUnit.MILLIS);
	}

	/** @return the time in milliseconds */
	@JsonIgnore
	public long getMillis()
	{
		return toMilliseconds().longValue();
	}

	/** @see Instant#toSeconds() */
	@Override
	public THIS toSeconds()
	{
		return toUnit(TimeUnit.SECONDS);
	}

	/** @see Instant#toMinutes() */
	@Override
	public THIS toMinutes()
	{
		return toUnit(TimeUnit.MINUTES);
	}

	/** @see Instant#toHours() */
	@Override
	public THIS toHours()
	{
		return toUnit(TimeUnit.HOURS);
	}

	/** @see Instant#toDays() */
	@Override
	public THIS toDays()
	{
		return toUnit(TimeUnit.DAYS);
	}

	/** @see Instant#toWeeks() */
	@Override
	public THIS toWeeks()
	{
		return toUnit(TimeUnit.WEEKS);
	}

	/** @see Instant#toDate() */
	@Override
	public Date toDate()
	{
		return new Date(getMillis());
	}

	/** @see Instant#toDate(Date) */
	@Override
	public Date toDate(final Date offset)
	{
		return new Date(offset.getTime() + getMillis());
	}

	/** @see Instant#toDateTime() */
	@Override
	public DateTime toDateTime()
	{
		return new DateTime(getMillis(), DEFAULT_DATETIME_ZONE);
	}

	/** @seeInstant#toDateTime(DateTime) */
	@Override
	public DateTime toDateTime(final DateTime offset)
	{
		return offset.plus(getMillis());
	}

	/** @see Instant#toCalendar() */
	@Override
	public Calendar toCalendar()
	{
		final Calendar result = GregorianCalendar.getInstance(
				DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
		result.setTimeInMillis(getMillis());
		return result;
	}

	/** @see Instant#toCalendar(Date) */
	@Override
	public Calendar toCalendar(final Date offset)
	{
		final Calendar result = GregorianCalendar.getInstance(
				DEFAULT_TIME_ZONE, DEFAULT_LOCALE);
		result.setTimeInMillis(offset.getTime() + toMilliseconds().longValue());
		return result;
	}

	/** @see Instant#plus(Number, TimeUnit) */
	@Override
	public THIS plus(final Number value, final TimeUnit unit)
	{
		Number delta = 0;
		try
		{
			delta = getUnit().convertFrom(value, unit);
		} catch (final CoalaRuntimeException e)
		{
			LOG.warn("Problem adding " + value + unit, e);
		}
		return plus(delta.doubleValue());
	}

	/** @see Instant#plus(Instant) */
	@Override
	public THIS plus(final Instant<?> value)
	{
		return plus(value.toUnit(getUnit()).doubleValue());
	}

	/** @see Instant#minus(Number) */
	@Override
	public THIS minus(final Number value)
	{
		return plus(-value.doubleValue());
	}

	/** @see Instant#minus(Number, TimeUnit) */
	@Override
	public THIS minus(final Number value, final TimeUnit unit)
	{
		return plus(-value.doubleValue(), unit);
	}

	/** @see Instant#minus(Instant) */
	@Override
	public THIS minus(final Instant<?> value)
	{
		return minus(value.toUnit(getUnit()).doubleValue());
	}

	/** @see Instant#multipliedBy(Number) */
	@Override
	public THIS multipliedBy(final Number factor)
	{
		return minus((factor.doubleValue() - 1) * getValue().doubleValue());
	}

	/** @see Instant#dividedBy(Number) */
	@Override
	public THIS dividedBy(final Number factor)
	{
		return multipliedBy(1. / factor.doubleValue());
	}

	/** @see Instant#dividedBy(Instant) */
	@Override
	public Number dividedBy(final Instant<?> value)
	{
		return doubleValue() / value.toUnit(getUnit()).doubleValue();
	}

	/** @see Instant#max(Instant[]) */
	@SuppressWarnings("unchecked")
	@Override
	public THIS max(final THIS... others)
	{
		THIS result = (THIS) this;
		if (others != null && others.length != 0)
			for (THIS other : others)
				if (other.isAfter(result))
					result = other;
		return result;
	}

	/** @see Instant#min(Instant[]) */
	@SuppressWarnings("unchecked")
	@Override
	public THIS min(final THIS... others)
	{
		THIS result = (THIS) this;
		if (others != null && others.length != 0)
			for (THIS other : others)
				if (other.isBefore(result))
					result = other;
		return result;
	}

	/** @see Instant#isBefore(Number) */
	@Override
	public boolean isBefore(final Number value)
	{
		return isBefore(value, getUnit());
	}

	/** @see Instant#isBefore(Number, TimeUnit) */
	@Override
	public boolean isBefore(final Number value, final TimeUnit unit)
	{
//		try
//		{
			return doubleValue() < getUnit().convertFrom(value, unit).doubleValue() ;
//		} catch (final CoalaRuntimeException e)
//		{
//			return value.doubleValue() < doubleValue();
//		}
	}

	/** @see Instant#isBefore(Instant) */
	@Override
	public boolean isBefore(final Instant<?> value)
	{
		return isBefore(value.getValue(), value.getUnit());
	}

	/** @see Instant#isOnOrBefore(Number) */
	@Override
	public boolean isOnOrBefore(final Number value)
	{
		return !isAfter(value);
	}

	/** @see Instant#isOnOrBefore(Number, TimeUnit) */
	@Override
	public boolean isOnOrBefore(final Number value, final TimeUnit unit)
	{
		return !isAfter(value, unit);
	}

	/** @see Instant#isOnOrBefore(Instant) */
	@Override
	public boolean isOnOrBefore(final Instant<?> value)
	{
		return !isAfter(value);
	}

	/** @see Instant#isOnOrAfter(Number) */
	@Override
	public boolean isOnOrAfter(final Number value)
	{
		return !isBefore(value);
	}

	/** @see Instant#isOnOrAfter(Number,TimeUnit) */
	@Override
	public boolean isOnOrAfter(final Number value, final TimeUnit unit)
	{
		return !isBefore(value, unit);
	}

	/** @see Instant#isOnOrAfter(Instant) */
	@Override
	public boolean isOnOrAfter(final Instant<?> value)
	{
		return !isBefore(value);
	}

	/** @see Instant#isAfter(Number) */
	@Override
	public boolean isAfter(final Number value)
	{
		return isAfter(value, getUnit());
	}

	/** @see Instant#isAfter(Number, TimeUnit) */
	@Override
	public boolean isAfter(final Number value, final TimeUnit unit)
	{
//		try
//		{
			return doubleValue() > getUnit().convertFrom(value, unit).doubleValue();
//		} catch (final CoalaRuntimeException e)
//		{
//			return value.doubleValue() > doubleValue();
//		}
	}

	/** @see Instant#isAfter(Instant) */
	@Override
	public boolean isAfter(final Instant<?> value)
	{
		return isAfter(value.getValue(), value.getUnit());
	}

	/** @see Instant#getRange(Instant, Instant) */
	@Override
	public Iterable<THIS> getRange(final Instant<?> interval,
			final Instant<?> max)
	{
		@SuppressWarnings("unchecked")
		final Iterator<THIS> iterator = Range.of((THIS) this, interval, max);
		return new Iterable<THIS>()
		{
			@Override
			public Iterator<THIS> iterator()
			{
				return iterator;
			}
		};
	}

	/** @see JSONConvertible#toJSON() */
	@Override
	public String toJSON()
	{
		return JsonUtil.toJSONString(this);
	}

	/** @see JSONConvertible#fromJSON(String) */
	@SuppressWarnings("unchecked")
	@Override
	public THIS fromJSON(final String jsonValue)
	{
		return (THIS) JsonUtil.fromJSONString(jsonValue, getClass());
	}

}
