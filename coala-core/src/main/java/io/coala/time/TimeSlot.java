/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/TimeSlot.java $
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
package io.coala.time;

import io.coala.log.LogUtil;

import java.io.IOException;
import java.io.Serializable;

import org.apache.log4j.Logger;
import org.joda.time.Interval;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * A {@link TimeSlot} is compared by its {@link Interval)'s start and end times
 * 
 * @date $Date: 2014-08-08 07:08:29 +0200 (Fri, 08 Aug 2014) $
 * @version $Revision: 349 $ $Author: krevelen $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 */
public class TimeSlot implements Serializable, Comparable<TimeSlot>
{

	/** */
	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LogUtil.getLogger(TimeSlot.class);

	/** */
	private Interval interval;

	/** */
	public TimeSlot()
	{
		this(null);
	}

	/** */
	public TimeSlot(final Interval interval)
	{
		this.interval = interval;
	}

	/** @return the interval */
	public Interval getInterval()
	{
		return this.interval;
	}

	/**
	 * @param other the other {@link TimeSlot} to check whether its
	 *        {@link Interval} overlaps with this {@link TimeSlot}
	 * @return {@code true} if the specified {@link TimeSlot}'s {@link Interval}
	 *         overlaps this {@link TimeSlot}'s {@link Interval} (assuming
	 *         exclusive end time), {@code false} otherwise
	 */
	public boolean overlaps(final TimeSlot other)
	{
		return getInterval().overlaps(other.getInterval());
	}

	@Override
	public boolean equals(final Object other)
	{
		if (other == this)
			return true;
		if (other == null || other instanceof TimeSlot == false)
			return false;
		final TimeSlot rhs = (TimeSlot) other;
		return getInterval().equals(rhs.getInterval());
	}

	@Override
	public int compareTo(final TimeSlot other)
	{
		// first, compare by start time
		final int compareStart = getInterval().getStart().compareTo(
				other.getInterval().getStart());
		if (compareStart != 0)
			return compareStart;
		// if equal start times, compare by end time
		return getInterval().getEnd().compareTo(other.getInterval().getEnd());
	}

	@Override
	public int hashCode()
	{
		return getInterval().hashCode();
	}

	@Override
	public String toString()
	{
		/*
		final String result = String.format(
				"%s[mode: %s, start: %s, duration: %s, power est.: %s, "
						+ "scheduled: %s]",
				getClass().getSimpleName(),
				getMode(),
				getInterval().getStart().toString(
						"EEE MMM dd, yyyy HH:mm:ss zzz"),
				DateUtil.toString(getInterval().toDuration()),
				getPowerConsumption() == null ? "?" : String.format("%.3fW",
						getPowerConsumption().getPower_consumption()),
				getID() == null ? "-" : getID());
		*/
		return serialize();
	}

	// private static final String SPLIT_REGEX = "_";

	protected String serialize()
	{
		return getInterval().toString();
	}

	public static TimeSlot valueOf(final String value)
	{
		return new TimeSlot(Interval.parse(value));
	}

	/** */
	public static final JsonDeserializer<TimeSlot> JSON_DESERIALIZER = new JsonDeserializer<TimeSlot>()
	{
		@Override
		public TimeSlot deserialize(final JsonParser jp,
				final DeserializationContext ctxt) throws IOException,
				JsonProcessingException
		{
			return valueOf(jp.getText());
		}
	};

	/** */
	public static final JsonSerializer<TimeSlot> JSON_SERIALIZER = new JsonSerializer<TimeSlot>()
	{

		@Override
		public void serialize(final TimeSlot value, final JsonGenerator jgen,
				final SerializerProvider provider) throws IOException,
				JsonProcessingException
		{
			jgen.writeString(value.serialize());
		}
	};

	/** */
	public static class JacksonModule extends SimpleModule
	{
		/** */
		private static final long serialVersionUID = 1L;

		public JacksonModule()
		{
			// super(PackageVersion.VERSION);

			LOG.trace("Registering JSON de/serialization for " + TimeSlot.class);
			addDeserializer(TimeSlot.class, JSON_DESERIALIZER);
			addSerializer(TimeSlot.class, JSON_SERIALIZER);
		}
	}

}
