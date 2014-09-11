/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/Instant.java $
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

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * {@link Instant}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the type of {@link Instant} to compare with
 */
public interface Instant<THIS extends Instant<THIS>> extends Serializable,
		Comparable<Instant<?>>
{

	/**
	 * The default time zone used to generate {@link Calendar} instances.
	 * Greenwich Mean Time (GMT) is practically equivalent with Universal Time
	 * Coordinated (UTC), the international atom time standard.
	 */
	TimeZone DEFAULT_TIME_ZONE = TimeZone.getTimeZone("GMT");

	/**
	 * The default time zone used to generate {@link Calendar} instances.
	 * Greenwich Mean Time (GMT) is practically equivalent with Universal Time
	 * Coordinated (UTC), the international atom time standard.
	 */
	DateTimeZone DEFAULT_DATETIME_ZONE = DateTimeZone.UTC;

	/** the default locale used to generate {@link Calendar} instances */
	Locale DEFAULT_LOCALE = Locale.ROOT;

	/** @return ID of the source clock generating this time instant */
	ClockID getClockID();

	/** @return the value of this {@link Instant} as {@link Number} */
	Number getValue();

	/** @see Number#intValue() */
	int intValue();

	/** @see Number#longValue() */
	long longValue();

	/** @see Number#floatValue() */
	float floatValue();

	/** @see Number#doubleValue() */
	double doubleValue();

	/** @return the {@link TimeUnit} of this {@link Instant} */
	TimeUnit getUnit();

	THIS plus(Number value);

	THIS plus(Number value, TimeUnit unit);

	THIS plus(Instant<?> value);

	THIS minus(Number value);

	THIS minus(Number value, TimeUnit unit);

	THIS minus(Instant<?> value);

	THIS multipliedBy(Number factor);

	THIS dividedBy(Number factor);

	@SuppressWarnings("unchecked")
	THIS max(THIS... others);

	@SuppressWarnings("unchecked")
	THIS min(THIS... others);

	boolean isBefore(Number value);

	boolean isBefore(Number value, TimeUnit unit);

	boolean isBefore(Instant<?> value);

	boolean isOnOrBefore(Number value);

	boolean isOnOrBefore(Number value, TimeUnit unit);

	boolean isOnOrBefore(Instant<?> value);

	boolean isAfter(Number value);

	boolean isAfter(Number value, TimeUnit unit);

	boolean isAfter(Instant<?> value);

	boolean isOnOrAfter(Number value);

	boolean isOnOrAfter(Number value, TimeUnit unit);

	boolean isOnOrAfter(Instant<?> value);

	Number dividedBy(Instant<?> value);

	/**
	 * Compare this {@link Instant} with another based on {@link ClockID}s and
	 * values (in base {@link TimeUnit}s)
	 * 
	 * @param other the final concrete {@link Instant} of the same type
	 * @return {@code x < 0} if this {@link Instant} is smaller, {@code x == 0}
	 *         if equal, and {@code x > 0} if greater than the specified
	 *         {@code other}
	 * 
	 * @see java.lang.Comparable#compareTo(Object)
	 */
	@Override
	int compareTo(Instant<?> other);

//	@JsonIgnore
//	TimeUnit getBaseUnit();

	THIS toUnit(TimeUnit unit);

//	THIS toBaseUnit();

	THIS toNanoseconds();

	THIS toMilliseconds();

	THIS toSeconds();

	THIS toMinutes();

	THIS toHours();

	THIS toDays();

	THIS toWeeks();

	/** @return the value of this {@link Instant} as {@link Date} */
	Date toDate();

	/** @return the value of this {@link Instant} as {@link Date} */
	Date toDate(Date offset);

	/** @return the value of this {@link Instant} as {@link DateTime} */
	DateTime toDateTime();

	/** @return the value of this {@link Instant} as {@link DateTime} */
	DateTime toDateTime(DateTime offset);

	/** @return the value of this {@link Instant} as {@link Calendar} */
	Calendar toCalendar();

	/** @return the value of this {@link Instant} as {@link Calendar} */
	Calendar toCalendar(Date offset);

	/**
	 * @param interval the duration between iterations
	 * @param max the (exclusive) limit, or {@code null} for unlimited
	 * @return the {@link Iterable} range of {@link Instant}s starting with this
	 *         instant
	 */
	Iterable<THIS> getRange(Instant<?> interval, Instant<?> max);

	/**
	 * {@link Range}
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 * @param <I>
	 */
	class Range<I extends Instant<?>> implements Iterator<I>
	{

		/** */
		private final Instant<?> interval;

		/** */
		private final Instant<?> max;

		/** */
		private I time;

		/**
		 * {@link Range} constructor
		 * 
		 * @param start
		 * @param interval
		 * @param max the (exclusive) limit, or {@code null} for unlimited
		 */
		protected Range(final I start, final Instant<?> interval,
				final Instant<?> max)
		{
			this.interval = interval;
			this.max = max;
			this.time = start;
		}

		/**
		 * {@link Range} constructor
		 * 
		 * @param start the start {@link Instant}
		 * @param interval the duration between iterations
		 * @param max the (exclusive) limit, or {@code null} for unlimited
		 */
		public static <I extends Instant<?>> Range<I> of(final I start,
				final Instant<?> interval, final Instant<?> max)
		{
			return new Range<I>(start, interval, max);
		}

		/** @see Iterator#hasNext() */
		@Override
		public boolean hasNext()
		{
			return max == null || this.time.isBefore(max);
		}

		/** @see Iterator#next() */
		@SuppressWarnings("unchecked")
		@Override
		public I next()
		{
			final I result = this.time;
			this.time = (I) this.time.plus(this.interval);
			return result;
		}

		/** @see Iterator#remove() */
		@Override
		public void remove()
		{
			throw new IllegalStateException("READ-ONLY RANGE ITERATOR");
		}
	}

}
