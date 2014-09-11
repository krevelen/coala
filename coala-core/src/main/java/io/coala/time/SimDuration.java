/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/SimDuration.java $
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
package io.coala.time;

import io.coala.exception.CoalaRuntimeException;
import io.coala.log.LogUtil;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link SimDuration}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class SimDuration extends AbstractInstant<SimDuration>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(SimDuration.class);
	
	/** */
	public static SimDuration ZERO = new SimDuration(0, TimeUnit.MILLIS);

	/** */
	// private static final TimeUnit baseUnit = TimeUnit.MILLIS;

	/**
	 * {@link SimDuration} zero-arg bean constructor
	 */
	protected SimDuration()
	{
		// empty
	}

	/**
	 * @param value
	 * @param unit
	 */
	@Inject
	public SimDuration(final Number value, final TimeUnit unit)
	{
		setValue(value);
		setUnit(unit);
	}

	/** @see Instant#plus(Number) */
	@Override
	public SimDuration plus(final Number value)
	{
		return new SimDuration(getValue().doubleValue() + value.doubleValue(),
				getUnit());
	}

	// /** @see Instant#getBaseUnit() */
	// @Override
	// public TimeUnit getBaseUnit()
	// {
	// return baseUnit;
	// }

	/** @see Instant#toUnit(TimeUnit) */
	@Override
	public SimDuration toUnit(final TimeUnit unit)
	{
		Number toValue = null;
		try
		{
			toValue = unit.convertFrom(getValue(), getUnit());
		} catch (final CoalaRuntimeException e)
		{
			LOG.warn("Problem converting " + toString() + " to " + unit.name()
					+ ": " + e.getMessage());
			return this;
		}
		return new SimDuration(toValue == null ? getValue() : toValue, unit);
	}

}
