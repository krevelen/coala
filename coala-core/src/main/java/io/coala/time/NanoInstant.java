/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/NanoInstant.java $
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

/**
 * {@link NanoInstant} has the nano-second as base time unit
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class NanoInstant extends AbstractInstant<NanoInstant>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	// private static final Logger LOG = LogUtil.getLogger(NanoInstant.class);

	/** */
	// private static final TimeUnit BASE_UNIT = TimeUnit.NANOS;

	/** */
	public static final NanoInstant ZERO = new NanoInstant(null, 0);

	/**
	 * {@link NanoInstant} constructor
	 * 
	 * @param value
	 */
	public NanoInstant(final ClockID clockID, final Number value)
	{
		super(clockID, value, TimeUnit.NANOS);
	}

	// /**
	// * {@link NanoInstant} constructor
	// *
	// * @param value
	// */
	// public NanoInstant(final ClockID clockID, final Number value,
	// final TimeUnit unit)
	// {
	// super(clockID, value, unit);
	// }
	//
	// /** @see Instant#getBaseUnit() */
	// @Override
	// public TimeUnit getBaseUnit()
	// {
	// return BASE_UNIT;
	// }

	/** @see Instant#toUnit(TimeUnit) */
	@Override
	public NanoInstant toUnit(final TimeUnit unit)
	{
		throw new RuntimeException(
				"Can't convert NanoInstant to another TimeUnit");
	}

	/** @see Instant#plus(Number) */
	@Override
	public NanoInstant plus(final Number value)
	{
		return new NanoInstant(getClockID(), getValue().doubleValue()
				+ value.doubleValue());
	}
}
