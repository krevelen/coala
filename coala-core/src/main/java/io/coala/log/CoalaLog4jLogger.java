/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/MyLogger.java $
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
package io.coala.log;

import io.coala.model.ModelComponent;
import io.coala.name.Identifiable;
import io.coala.name.Identifier;
import io.coala.time.Instant;
import io.coala.time.SimTime;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.apache.log4j.Priority;
import org.apache.log4j.spi.LoggingEvent;

/**
 * {@link CoalaLog4jLogger}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class CoalaLog4jLogger extends Logger
{

	/** */
	private static final String ID_PREFIX_FORMAT = "%s - ";

	/** */
	private static final String EMPTY_PREFIX_FORMAT = "";

	/** */
	private static final String NAN_TIME_PREFIX_FORMAT = "t=       ?%s - ";

	/** */
	private static final String POS_INF_TIME_PREFIX_FORMAT = "t=   +inf %s - ";

	/** */
	private static final String NEG_INF_TIME_PREFIX_FORMAT = "t=   -inf %s - ";

	/** */
	private static final String TIME_PREFIX_FORMAT = "t=%10.4f%s - ";

	/** */
	private static final String SIMTIME_PREFIX_FORMAT = "t=%10.4f%s (%s) - ";

	/** */
	private static final DateFormat SIMTIME_DATE_FORMAT = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/** */
	private static final String METHOD_AFFIX_FORMAT = " [at %s.%s(%s:%d)]";

	/** */
	private final String idPrefix;

	/** */
	private final ModelComponent<?> clock;

	/**
	 * @return
	 */
	public String getPrefix()
	{
		return this.idPrefix;
	}

	/**
	 * @param source
	 * @return
	 */
	public static String determineLoggerPrefixForObject(final Object source)
	{
		final String result = source == null ? EMPTY_PREFIX_FORMAT
				: source instanceof Identifier ? String.format(
						ID_PREFIX_FORMAT, (Identifier<?, ?>) source)
						: source instanceof Identifiable ? String.format(
								ID_PREFIX_FORMAT,
								((Identifiable<?>) source).getID())
								: EMPTY_PREFIX_FORMAT;

		// System.err.println("Determined prefix for " + source.getClass() +
		// ": "
		// + result);
		return result;
	}

	/**
	 * {@link CoalaLog4jLogger} constructor
	 * 
	 * @param name
	 * @param source
	 */
	protected CoalaLog4jLogger(final String name, final Object source)
	{
		super(name);

		if (source == null)
			new NullPointerException("New logger for null object")
					.printStackTrace();

		this.idPrefix = source instanceof String ? EMPTY_PREFIX_FORMAT
		// String.format(ID_PREFIX_FORMAT, source)
				: determineLoggerPrefixForObject(source);

		// System.err.println("New logger idPrefix: " + this.idPrefix + " for "
		// + this);

		if (source instanceof ModelComponent)
		{
			this.clock = (ModelComponent<?>) source;
			// System.err.println("1. start logger at time: " + getTimePrefix()
			// + " for " + name);
		} else
		{
			this.clock = null;
			// System.err.println("1. no clock for " + getName());
		}
	}

	/**
	 * @return
	 */
	protected String getTimePrefix()
	{
		if (this.clock == null)
		{
			// System.err.println("2. clock was null for " + getName());
			return EMPTY_PREFIX_FORMAT;
		}

		final Instant<?> time = this.clock.getTime();
		if (time == null)
		{
			// System.err.println("2. time was null for " + getName());
			return EMPTY_PREFIX_FORMAT;
		}

		if (time.getValue() instanceof Double)
		{
			final double t = time.doubleValue();
			if (t == Double.NaN)
			{
				// System.err.println("2. time is NaN for " + getName());
				return String.format(NAN_TIME_PREFIX_FORMAT, time.getUnit()
						.toString());
			}

			if (t == Double.NEGATIVE_INFINITY)
			{
				// System.err.println("2. time is pos inf for " + getName());
				return String.format(NEG_INF_TIME_PREFIX_FORMAT, time.getUnit()
						.toString());
			}

			if (t == Double.NEGATIVE_INFINITY)
			{
				// System.err.println("2. time is neg inf for " + getName());
				return String.format(POS_INF_TIME_PREFIX_FORMAT, time.getUnit()
						.toString());
			}
		}

		// System.err.println("2. time is " + time + " for " + getName());
		try
		{
			if (time instanceof SimTime)
				return String.format(SIMTIME_PREFIX_FORMAT, time.doubleValue(),
						time.getUnit().toString(), SIMTIME_DATE_FORMAT
								.format(((SimTime) time).getIsoTime()));
			return String.format(TIME_PREFIX_FORMAT, time.doubleValue(), time
					.getUnit().toString());
		} catch (final Throwable t)
		{
			t.printStackTrace();
			return time.toString();
		}
	}

	/** */
	private static final String[] loggingPackages = {

	/* */
	org.apache.commons.logging.Log.class.getPackage().getName(),

	/* */
	org.slf4j.Logger.class.getPackage().getName(),

	/* */
	java.util.logging.Logger.class.getPackage().getName(),

	/* */
	org.apache.log4j.Logger.class.getPackage().getName()

	};

	private boolean isLoggingPackage(final String className)
	{
		for (String loggingPackage : loggingPackages)
			if (className.startsWith(loggingPackage))
				return true;
		return false;
	}

	/**
	 * @return
	 */
	protected String getMethodAffix()
	{
		int i = 4;
		StackTraceElement elem = Thread.currentThread().getStackTrace()[i];
		while (isLoggingPackage(elem.getClassName()))
			elem = Thread.currentThread().getStackTrace()[++i];
		return String.format(METHOD_AFFIX_FORMAT, elem.getClassName(),
				elem.getMethodName(), elem.getFileName(), elem.getLineNumber());
	}

	@Override
	protected void forcedLog(final String fqcn, final Priority level,
			final Object message, final Throwable t)
	{
		final String timePrefix = getTimePrefix();
		// System.err.println("3. time prefix: " + timePrefix + " for "
		// + getName());

		final String methodAffix = getMethodAffix();
		try
		{
			callAppenders(new LoggingEvent(fqcn, this, level,
					new StringBuilder(timePrefix).append(this.idPrefix)
							.append(message).append(methodAffix).toString(), t));
		} catch (final Throwable e)
		{
			e.printStackTrace();
			callAppenders(new LoggingEvent(fqcn, this, level, message, t));
		}
	}

}