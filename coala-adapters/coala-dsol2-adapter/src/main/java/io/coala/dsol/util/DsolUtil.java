/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolUtil.java $
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

import io.coala.log.LogUtil;
import io.coala.time.ClockID;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;

import java.rmi.RemoteException;

import nl.tudelft.simulation.dsol.experiment.TimeUnitInterface;
import nl.tudelft.simulation.dsol.experiment.Treatment;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;

/**
 * {@link DsolUtil}
 * 
 * @date $Date: 2014-05-28 13:23:08 +0200 (Wed, 28 May 2014) $
 * @version $Revision: 283 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class DsolUtil
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DsolUtil.class);

	/** @return the DSOL time unit as {@link TimeUnit} */
	public static TimeUnit toTimeUnit(final TimeUnitInterface timeUnit)
	{
		if (timeUnit.getValue() == TimeUnitInterface.MILLISECOND.getValue())
			return TimeUnit.MILLIS;

		if (timeUnit.getValue() == TimeUnitInterface.SECOND.getValue())
			return TimeUnit.SECONDS;

		if (timeUnit.getValue() == TimeUnitInterface.MINUTE.getValue())
			return TimeUnit.MINUTES;

		if (timeUnit.getValue() == TimeUnitInterface.HOUR.getValue())
			return TimeUnit.HOURS;

		if (timeUnit.getValue() == TimeUnitInterface.DAY.getValue())
			return TimeUnit.DAYS;

		if (timeUnit.getValue() == TimeUnitInterface.WEEK.getValue())
			return TimeUnit.WEEKS;

		// throw new DSOLException("Unknown DSOL time unit value: "
		// + timeUnit.getValue());
		LOG.fatal("Can't convert from DSOL time unit: " + timeUnit
				+ ", assuming DAY");
		return TimeUnit.DAYS;
	}

	/** @return the {@link TimeUnit} as DSOL {@link TimeUnitInterface} */
	public static TimeUnitInterface toTimeUnit(final TimeUnit timeUnit)
	{
		switch (timeUnit)
		{
		case DAYS:
			return TimeUnitInterface.DAY;
		case HOURS:
			return TimeUnitInterface.HOUR;
		case MILLIS:
			return TimeUnitInterface.MILLISECOND;
		case MINUTES:
			return TimeUnitInterface.MINUTE;
		case SECONDS:
			return TimeUnitInterface.SECOND;
		case WEEKS:
			return TimeUnitInterface.WEEK;
		default:
			// throw new DSOLException("Unknown DSOL time unit value: " +
			// timeUnit);
			LOG.fatal("Can't convert to DSOL time unit: " + timeUnit
					+ ", assuming DAY");
			return TimeUnitInterface.DAY;
		}
	}

	/**
	 * @return the specified time value (unit) converted to specified
	 *         {@code toUnit}
	 */
	public static Number toTimeUnit(final TimeUnitInterface toUnit,
			final Number time, final TimeUnitInterface unit)
	{
		return unit == toUnit ? time : time.doubleValue() * unit.getValue()
				/ toUnit.getValue();
	}

	/** @return the DSOL replication's time unit */
	public static TimeUnit getTimeUnit(final SimulatorInterface simulator)
	{
		try
		{
			return toTimeUnit(simulator.getReplication().getTreatment()
					.getTimeUnit());
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/** @return the DSOL replication's time unit */
	public static DateTime getTimeOffset(final SimulatorInterface simulator)
	{
		try
		{
			return new DateTime(simulator.getReplication().getTreatment()
					.getStartTime());
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/** @return the duration between this replication's warm-up and end dates */
	public static Interval getWarmupInterval(final Treatment treatment)
	{
		return new Interval(toDateTime(0.0, treatment), toDateTime(
				treatment.getWarmupPeriod(), treatment));
	}

	/** @return the duration between this replication's warm-up and end dates */
	public static Interval getRunInterval(final Treatment treatment)
	{
		return new Interval(toDateTime(treatment.getWarmupPeriod(), treatment),
				toDateTime(treatment.getRunLength(), treatment));
	}

	/** @return the specified date in the replication's time unit */
	public static double simTime(final DateTime dateTime,
			final Treatment treatment)
	{
		return toTimeUnit(treatment.getTimeUnit(),
				dateTime.getMillis() - treatment.getStartTime(),
				TimeUnitInterface.MILLISECOND).doubleValue();
	}

	/** @return the specified duration in the replication's time unit */
	public static double simTime(final Duration duration,
			final Treatment treatment)
	{
		return toTimeUnit(treatment.getTimeUnit(), duration.getMillis(),
				TimeUnitInterface.MILLISECOND).doubleValue();
	}

	/** @return the specified duration in the replication's time unit */
	public static double simTime(final TimeUnitInterface timeUnit,
			final Duration duration)
	{
		return toTimeUnit(timeUnit, duration.getMillis(),
				TimeUnitInterface.MILLISECOND).doubleValue();
	}

	/**
	 * @return the current simulator time in specified units
	 * @throws DSOLException
	 */
	public static double simTime(final SimulatorInterface simulator)
	{
		try
		{
			return simulator.getSimulatorTime();
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/** @return the simulator's current time as {@link SimTime} */
	public static SimTime getSimTime(final ClockID sourceID,
			final SimulatorInterface simulator)
	{
		return getSimTime(sourceID, simulator, simTime(simulator));
	}

	/** @return the specified time as {@link SimTime} */
	public static SimTime getSimTime(final ClockID sourceID,
			final SimulatorInterface simulator, final DateTime time)
	{
		try
		{
			return getSimTime(sourceID, simulator,
					simTime(time, simulator.getReplication().getTreatment()));
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/** @return the specified simulation time as {@link SimTime} */
	public static SimTime getSimTime(final ClockID sourceID,
			final SimulatorInterface simulator, final double simTime)
	{
		return new SimTime(// getTimeUnit(simulator),
				sourceID, simTime, getTimeUnit(simulator), getTimeOffset(
						simulator).toDate());
	}

	/**
	 * @return the current simulator time in specified units
	 * @throws DSOLException
	 */
	public static double getSimTime(final SimulatorInterface simulator,
			final TimeUnitInterface timeUnit)
	{
		try
		{
			return toTimeUnit(timeUnit, simulator.getSimulatorTime(),
					simulator.getReplication().getTreatment().getTimeUnit())
					.doubleValue();
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/**
	 * @return current replication time as replication ISO date
	 * @throws DSOLException
	 */
	public static DateTime getDateTime(final SimulatorInterface simulator)
	{
		return getDateTime(simulator, DateTimeZone.getDefault());
	}

	/**
	 * @return current replication time as replication ISO date
	 * @throws DSOLException
	 */
	public static DateTime getDateTime(final SimulatorInterface simulator,
			final DateTimeZone zone)
	{
		try
		{
			return toDateTime(simulator.getSimulatorTime(), simulator
					.getReplication().getTreatment(), zone);
		} catch (final RemoteException e)
		{
			LOG.fatal("Problem reaching DSOL replication", e);
			throw new NullPointerException("Replication unreachable");
		}
	}

	/** @return specified simulation time and unit as replication ISO date */
	public static DateTime toDateTime(final Number simTime,
			final Treatment treatment)
	{
		return toDateTime(simTime, treatment, DateTimeZone.getDefault());
	}

	/** @return specified simulation time and unit as replication ISO date */
	public static DateTime toDateTime(final Number simTime,
			final Treatment treatment, final DateTimeZone zone)
	{
		if (Double.isNaN(simTime.doubleValue()))
			return new DateTime(treatment.getStartTime());
		return new DateTime(toTimeUnit(TimeUnitInterface.MILLISECOND, simTime,
				treatment.getTimeUnit()).longValue()
				+ treatment.getStartTime(), zone);
		// // warning: do not use DateTime#plusMillis(int) !!
		// return timeOffset.plus(toTimeUnit(TimeUnitInterface.MILLISECOND,
		// time,
		// unit).longValue());
	}

	/**
	 * @return overlap of specified interval within replication run period
	 *         (after warm-up and before run length)
	 */
	public static Interval crop(final Interval interval,
			final Treatment treatment)
	{
		final Interval runPeriod = getRunInterval(treatment);
		if (interval.overlaps(runPeriod))
		{
			final long croppedStart = Math.max(interval.getStartMillis(),
					runPeriod.getStartMillis());
			final long croppedEnd = Math.min(interval.getEndMillis(),
					runPeriod.getEndMillis());
			return new Interval(croppedStart, croppedEnd);
		}
		return interval;
	}

}
