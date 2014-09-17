/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/SimTime.java $
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
import io.coala.log.LogUtil;
import io.coala.model.ModelID;
import io.coala.name.AbstractIdentifier;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/**
 * {@link SimTime} is an {@link Instant} with a particular base unit and
 * implementing {@link JSONConvertible}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class SimTime extends AbstractInstant<SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(SimTime.class);

	/** */
	public static final SimTime ZERO = new SimTime(null, 0, TimeUnit.MILLIS,
			null);

	/** */
	// private TimeUnit baseUnit;

	/** redundant storage */
	private Date isoTime;

	/**
	 * {@link SimTime} zero-arg bean constructor
	 */
	protected SimTime()
	{
		// empty
	}

	/**
	 * @param baseUnit
	 * @param source
	 * @param value
	 * @param unit
	 * @param offset
	 */
	@Inject
	public SimTime(
			final ClockID source, final Number value, final TimeUnit unit,
			final Date offset)
	{
		// this.baseUnit = baseUnit;
		setValue(value);
		setUnit(unit);
		setClockID(source);
		Date isoDate = null;
		if (offset != null)
			try
			{
				isoDate = new Date(offset.getTime()
						+ TimeUnit.MILLIS.convertFrom(value, unit).longValue());
			} catch (final CoalaRuntimeException e)
			{
				// LOG.warn("Problem converting to ISO date", e);
			}
		setIsoTime(isoDate);
	}

	/** @return the isoTime */
	public Date getIsoTime()
	{
		return this.isoTime;
	}

	/** @param isoTime the isoTime to set */
	protected void setIsoTime(final Date isoTime)
	{
		this.isoTime = isoTime;
	}

	/** */
	public static final String READABLE_DATETIME_SHORT_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/** @see AbstractIdentifier#toString() */
	@Override
	public String toString()
	{
		return String.format("%1.4f%s @%s (%s)", getValue().doubleValue(),
				getUnit().toString(), getClockID(), new DateTime(getIsoTime())
						.toString(READABLE_DATETIME_SHORT_FORMAT));
	}

	// /** @see Instant#getBaseUnit() */
	// @Override
	// public TimeUnit getBaseUnit()
	// {
	// return this.baseUnit;
	// }

	/**
	 * @return
	 */
	public Date calcOffset()
	{
		long millis = 0;
		try
		{
			// avoid using: getMillis() or toMilliseconds() or toUnit()
			millis = TimeUnit.MILLIS.convertFrom(getValue(), getUnit())
					.longValue();
		} catch (final CoalaRuntimeException e)
		{
			// LOG.warn("Problem converting to " + TimeUnit.MILLIS, e);
			millis = getValue().longValue();
		}
		return new Date(getIsoTime() == null ? 0 : getIsoTime().getTime()
				- millis);
	}

	/** @see Instant#toUnit(TimeUnit) */
	@Override
	public SimTime toUnit(final TimeUnit unit)
	{
		Number toValue = null;
		try
		{
			toValue = unit.convertFrom(getValue(), getUnit());
		} catch (final CoalaRuntimeException e)
		{
			LOG.warn("Problem converting " + toString() + " to " + unit.name(),
					e);
			return this;
		}
		if(toValue==null)
		LOG.warn("Problem converting " + toString() + " to " + unit.name());
		return new SimTime(
				// getBaseUnit(),
				getClockID(), toValue == null ? getValue() : toValue, unit,
				calcOffset());
	}

	/** @see Instant#plus(Number) */
	@Override
	public SimTime plus(final Number value)
	{
		return new SimTime(
				// getBaseUnit(),
				getClockID(), getValue().doubleValue() + value.doubleValue(),
				getUnit(), calcOffset());
	}

	/**
	 * {@link JsonSimTimeModule} is required as {@link SimTime} extends
	 * {@link Number} which causes JSON marshaling problems with Jackson, unless
	 * custom {@link JsonSerializer} and {@link JsonDeserializer}
	 * implementations are applied
	 * 
	 * not deprecated Jackson JSON marshalling of {@link Number} sub-types is
	 * solved
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public static class JsonSimTimeModule extends SimpleModule
	{
		private static final long serialVersionUID = 1L;

		public JsonSimTimeModule()
		{
			addSerializer(new JsonSimTimeSerializer());
			addDeserializer(SimTime.class, new JsonSimTimeDeserializer());
		}
	}

	/**
	 * {@link JsonSimTimeDeserializer}
	 * 
	 * @deprecated Jackson JSON marshalling of {@link Number} sub-types is
	 *             solved
	 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public static class JsonSimTimeDeserializer extends
			JsonDeserializer<SimTime>
	{

		/** @see JsonDeserializer#deserialize(JsonParser, DeserializationContext) */
		@Override
		public SimTime deserialize(final JsonParser jp,
				final DeserializationContext ctxt) throws IOException,
				JsonProcessingException
		{
			if (jp.getText() == null || jp.getText().length() == 0
					|| jp.getText().equals("null"))
				return null;

			final String[] text = jp.getText().split(" ");
			if (text.length != 5)
				throw new IOException("Incorrect number of values: "
						+ jp.getText() + " >>> " + Arrays.asList(text));

			return new SimTime(// TimeUnit.valueOf(text[5]),
					text[0].length() == 0 ? null : new ClockID(new ModelID(
							text[0]), text[1]), Double.valueOf(text[2]),
					TimeUnit.valueOf(text[3]), "null".equals(text[4]) ? null
							: new Date(Long.valueOf(text[4])));
		}

	}

	/**
	 * {@link JsonSimTimeSerializer}
	 * 
	 * @deprecated Jackson JSON marshalling of {@link Number} sub-types is
	 *             solved
	 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public static class JsonSimTimeSerializer extends JsonSerializer<SimTime>
	{

		/** */
		private static final Logger LOG = Logger
				.getLogger(JsonSimTimeSerializer.class);

		/**
		 * @see JsonSerializer#serialize(Object, JsonGenerator,
		 *      SerializerProvider)
		 */
		@Override
		public void serialize(final SimTime time, JsonGenerator jgen,
				SerializerProvider provider) throws IOException,
				JsonProcessingException
		{
			try
			{
				final String serialized = time == null ? "" : String.format(
						"%s %s %f %s %s",
						time.getClockID().getModelID(),
						time.getClockID().getValue(),
						time.getValue().doubleValue(),
						time.getUnit(),
						time.getIsoTime() == null ? "null" : Long.toString(time
								.getIsoTime().getTime()));// ,
															// time.getBaseUnit());
				LOG.trace("Marshalled SimTime to: " + serialized);
				jgen.writeString(serialized);
			} catch (final Throwable t)
			{
				LOG.warn("Problem marshalling SimTime", t);
			}
		}

		@Override
		public Class<SimTime> handledType()
		{
			return SimTime.class;
		}
	}
}