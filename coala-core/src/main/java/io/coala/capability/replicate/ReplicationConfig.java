/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/ReplicationConfig.java $
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
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.capability.replicate;

import io.coala.config.ConfigUtil;
import io.coala.model.BasicModelComponentIDFactory;
import io.coala.model.ModelComponentIDFactory;
import io.coala.model.ModelID;
import io.coala.time.ClockID;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import java.lang.reflect.Method;
import java.util.Date;

import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Separator;
import org.aeonbits.owner.Config.Sources;
import org.aeonbits.owner.Converter;
import org.aeonbits.owner.Mutable;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link ReplicationConfig} uses the <A href="http://owner.aeonbits.org/">OWNER
 * API</a>
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@LoadPolicy(LoadType.MERGE)
@Sources({ "file:${" + ConfigUtil.FILE_NAME_PROPERTY + "}",
		"classpath:${" + ConfigUtil.FILE_NAME_PROPERTY + "}",
		"file:${user.dir}/" + ConfigUtil.FILE_NAME_DEFAULT,
		"file:~/" + ConfigUtil.FILE_NAME_DEFAULT,
		"classpath:" + ConfigUtil.FILE_NAME_DEFAULT })
@Separator(",")
public interface ReplicationConfig extends Mutable // Config
{
	/** */
	String MODEL_NAME_KEY = "modelName";

	/** */
	String CLOCK_NAME_KEY = "clockName";

	/** */
	String OFFSET_KEY = "offset";

	/** */
	String BASE_TIMEUNIT_KEY = "baseTimeUnit";

	/** */
	String RANDOM_SEED_KEY = "randomSeed";

	/** */
	String DURATION_KEY = "duration";

	@Key(MODEL_NAME_KEY)
	@DefaultValue("defaultRepl")
	String getModelName();

	@Key(CLOCK_NAME_KEY)
	@DefaultValue("_clock_")
	String getClockName();

	@Key(OFFSET_KEY)
	@ConverterClass(DateTimeConverter.class)
	DateTime getOffset();

	@Key(DURATION_KEY)
	@DefaultValue("P30D")
	// follows standard Period string format, see
	// http://www.w3schools.com/schema/schema_dtypes_date.asp
	String getPeriod();

	@DefaultValue("${duration}")
	@ConverterClass(PeriodConverter.class)
	@JsonIgnore
	Period getDuration();

	@Key(BASE_TIMEUNIT_KEY)
	@DefaultValue("HOURS")
	TimeUnit getBaseTimeUnit();

	@Key(RANDOM_SEED_KEY)
	@DefaultValue("1")
	long getSeed();

	// @Key("randomSeeds")
	// @DefaultValue("1, 2, 3")
	// Long[] getSeeds();

	/* Derived values */

	String VALUE_SEP_REGEX = "\\|";

	@DefaultValue("${modelName}")
	@ConverterClass(ModelIDConverter.class)
	@JsonIgnore
	ModelID getReplicationID();

	@DefaultValue("${modelName}|${clockName}")
	@ConverterClass(ClockIDConverter.class)
	@JsonIgnore
	ClockID getClockID();

	@DefaultValue("${offset}|${duration}")
	@ConverterClass(IntervalConverter.class)
	@JsonIgnore
	Interval getInterval();

	@DefaultValue("${modelName}")
	@ConverterClass(ModelComponentIDFactoryConverter.class)
	ModelComponentIDFactory newID();

	@DefaultValue("${modelName}|${clockName}|${offset}")
	@ConverterClass(SimTimeFactoryConverter.class)
	SimTimeFactory newTime();

	/**
	 * {@link DateTimeConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class DateTimeConverter implements Converter<DateTime>
	{
		private static final DateTime START = DateTime.now()
				.withTimeAtStartOfDay();

		@Override
		public DateTime convert(final Method targetMethod, final String input)
		{
			return input == null || input.isEmpty() ? START : DateTime
					.parse(input);
		}
	}

	/**
	 * {@link PeriodConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class PeriodConverter implements Converter<Period>
	{
		@Override
		public Period convert(final Method targetMethod, final String input)
		{
			return new Period(input);
		}
	}

	/**
	 * {@link ModelIDConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class ModelIDConverter implements Converter<ModelID>
	{
		@Override
		public ModelID convert(final Method targetMethod, final String input)
		{
			return new ModelID(input);
		}
	}

	/**
	 * {@link ClockIDConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class ClockIDConverter implements Converter<ClockID>
	{
		@Override
		public ClockID convert(final Method targetMethod, final String input)
		{
			final String[] split = input.split(VALUE_SEP_REGEX, -1);
			// System.err.println("Converting to ClockID: " + input + " = "
			// + Arrays.asList(split));
			return new ClockID(new ModelID(split.length < 2 ? "defaultRepl"
					: split[0]), split[split.length - 1]);
		}
	}

	/**
	 * {@link IntervalConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class IntervalConverter implements Converter<Interval>
	{
		@Override
		public Interval convert(final Method targetMethod, final String input)
		{
			final String[] split = input.split(VALUE_SEP_REGEX, -1);
			// System.err.println("Converting to Interval: " + input + " = "
			// + Arrays.asList(split));
			final DateTime startInstant = split[0].isEmpty() ? DateTimeConverter.START
					: DateTime.parse(split[0]);
			return new Interval(startInstant,
					split.length < 2 ? Duration.standardDays(30) : new Period(
							split[1]).toDurationFrom(startInstant));
		}
	}

	/**
	 * {@link ModelComponentIDFactoryConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class ModelComponentIDFactoryConverter implements
			Converter<ModelComponentIDFactory>
	{
		@Override
		public ModelComponentIDFactory convert(final Method targetMethod,
				final String input)
		{
			return new BasicModelComponentIDFactory().initialize(new ModelID(
					input));
		}
	}

	/**
	 * {@link SimTimeFactoryConverter}
	 * 
	 * @version $Revision$
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class SimTimeFactoryConverter implements Converter<SimTimeFactory>
	{
		@Override
		public SimTimeFactory convert(final Method targetMethod,
				final String input)
		{
			final String[] split = input.split(VALUE_SEP_REGEX, -1);
			// System.err.println("Converting to SimTimeFactory: " + input +
			// " = "
			// + Arrays.asList(split));
			final ClockID clockID = new ClockID(new ModelID(
					split.length < 2 ? "defaultRepl" : split[0]),
					split[split.length - 1]);
			final Date offset = split[2].isEmpty() ? DateTimeConverter.START
					.toDate() : DateTime.parse(split[2]).toDate();
			return new SimTimeFactory()
			{
				@Override
				public SimTime create(final Number value, final TimeUnit unit)
				{
					return new SimTime(clockID, value, unit, offset);
				}
			};
		}
	}
}