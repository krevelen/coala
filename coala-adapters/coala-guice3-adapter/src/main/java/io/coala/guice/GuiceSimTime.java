/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/GuiceSimTime.java $
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
package io.coala.guice;

import io.coala.time.ClockID;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;

import java.util.Date;

import javax.annotation.Nullable;

import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;

/**
 * {@link GuiceSimTime} is the concrete class for Guice to implement/bind
 * SimTime via the {@link InstantFactory}
 * 
 * @date $Date: 2014-05-28 13:23:08 +0200 (Wed, 28 May 2014) $
 * @version $Revision: 283 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class GuiceSimTime extends SimTime
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link GuiceSimTime} constructor
	 * 
	 * @param clockID
	 * @param value
	 * @param unit
	 * @param offset
	 */
	@AssistedInject
	public GuiceSimTime(
			// final TimeUnit baseUnit,
			final ClockID clockID, @Assisted final Number value,
			@Assisted final TimeUnit unit, @Nullable final Date offset)
	{
		super(// baseUnit,
				clockID, value, unit, offset);
	}

}
