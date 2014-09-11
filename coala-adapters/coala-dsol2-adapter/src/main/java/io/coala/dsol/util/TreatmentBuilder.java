/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/TreatmentBuilder.java $
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
package io.coala.dsol.util;

import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;

import java.util.Date;
import java.util.Properties;

import javax.naming.NamingException;

import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * {@link TreatmentBuilder}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@SuppressWarnings("serial")
public class TreatmentBuilder extends Treatment
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(TreatmentBuilder.class);

	/** */
	private static final DsolReplicationMode DEFAULT_MODE = DsolReplicationMode.TERMINATING;

	/** */
	private static final long DEFAULT_START_TIME = System.currentTimeMillis();

	/** */
	private static final double DEFAULT_WARMUP_PERIOD = 0d;

	/** */
	private static final long DEFAULT_RUN_LENGTH = Integer.MAX_VALUE;

	/** */
	private static final TimeUnit DEFAULT_TIMEUNIT = TimeUnit.DAYS;

	/**
	 * {@link TreatmentBuilder} constructor
	 * 
	 * @param experiment
	 */
	public TreatmentBuilder(final ExperimentBuilder experiment)
	{
		this(experiment, DEFAULT_MODE);
	}

	/**
	 * {@link TreatmentBuilder} constructor
	 * 
	 * @param experiment
	 */
	public TreatmentBuilder(final ExperimentBuilder experiment,
			final SimTime duration)
	{
		this(experiment, DEFAULT_MODE, duration);
	}

	/**
	 * {@link TreatmentBuilder} constructor
	 * 
	 * @param experiment
	 */
	public TreatmentBuilder(final ExperimentBuilder experiment,
			final DsolReplicationMode mode, final SimTime duration)
	{
		this(experiment, mode);
		withRunLength(duration.doubleValue());
		withTimeUnit(duration.getUnit());
	}

	/**
	 * {@link TreatmentBuilder} constructor
	 * 
	 * @param experiment
	 */
	public TreatmentBuilder(final ExperimentBuilder experiment,
			final DsolReplicationMode mode)
	{
		super(experiment, mode.getValue());
		withStartTime(DEFAULT_START_TIME);
		withWarmupPeriod(DEFAULT_WARMUP_PERIOD);
		withRunLength(DEFAULT_RUN_LENGTH);
		withTimeUnit(DEFAULT_TIMEUNIT);
	}

	@Override
	public ExperimentBuilder getExperiment()
	{
		return (ExperimentBuilder) super.getExperiment();
	}

	public String getProperty(final String key)
	{
		return (String) (getProperties() == null ? null : getProperties().get(
				key));
	}

	public TreatmentBuilder withProperties(final Properties properties)
	{
		setProperties(properties);
		return this;
	}

	public TreatmentBuilder withProperty(final String key, final String value)
	{
		final Properties properties = getProperties();
		if (properties == null)
			setProperties(new Properties());
		getProperties().setProperty(key, value);
		return this;
	}

	@Override
	// @JsonIgnore
	public short getReplicationMode()
	{
		return super.getReplicationMode();
	}

	public TreatmentBuilder withMode(final DsolReplicationMode mode)
	{
		setReplicationMode(mode.getValue());
		return this;
	}

	public TreatmentBuilder withTimeUnit(final TimeUnit timeUnit)
	{
		return withTimeUnit(DsolUtil.toTimeUnit(timeUnit));
	}

	public TreatmentBuilder withTimeUnit(final TimeUnitInterface timeUnit)
	{
		setTimeUnit(timeUnit);
		return this;
	}

	public TreatmentBuilder withStartTime(final DateTime timeOffset)
	{
		return withStartTime(timeOffset.getMillis());
	}

	public TreatmentBuilder withStartTime(final Date timeOffset)
	{
		return withStartTime(timeOffset.getTime());
	}

	public TreatmentBuilder withStartTime(final long timeOffset)
	{
		setStartTime(timeOffset);
		return this;
	}

	public TreatmentBuilder withRunInterval(final Interval interval)
	{
		return withStartTime(interval.getStart()).withRunLength(
				interval.toDuration());
	}

	public TreatmentBuilder withRunLength(final Duration runLength)
	{
		final TimeUnitInterface toTimeUnit = getTimeUnit();
		return withRunLength(DsolUtil.toTimeUnit(toTimeUnit,
				runLength.getMillis(), TimeUnitInterface.MILLISECOND)
				.doubleValue());
	}

	public TreatmentBuilder withRunLength(final Number runLength)
	{
		setRunLength(runLength.doubleValue());
		return this;
	}

	public TreatmentBuilder withWarmupPeriod(final Duration warmupPeriod)
	{
		final TimeUnitInterface toTimeUnit = getTimeUnit();
		return withWarmupPeriod(DsolUtil.toTimeUnit(toTimeUnit,
				warmupPeriod.getMillis(), TimeUnitInterface.MILLISECOND)
				.doubleValue());
	}

	public TreatmentBuilder withWarmupPeriod(final Number warmupPeriod)
	{
		setWarmupPeriod(warmupPeriod.doubleValue());
		return this;
	}

	public ReplicationBuilder newReplication(final String name)
			throws NamingException
	{
		final ReplicationBuilder result = new ReplicationBuilder(this, name);
		getExperiment().withReplications(result);
		return result;
	}

	@Override
	public String toString()
	{
		try
		{
			// final JsonNode node = JsonUtil.getJOM().valueToTree(this);
			return JsonUtil.getJOM().writerWithDefaultPrettyPrinter()
					.writeValueAsString(this);
		} catch (final JsonProcessingException e)
		{
			LOG.warn("Problem marshalling " + getClass().getName(), e);
			return super.toString();
		}
	}
}
