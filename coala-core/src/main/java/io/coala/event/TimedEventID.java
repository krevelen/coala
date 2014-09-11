/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/event/TimedEventID.java $
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
package io.coala.event;

import io.coala.model.ModelID;
import io.coala.name.AbstractIdentifier;
import io.coala.time.Instant;
import io.coala.time.Timed;

import java.io.Serializable;

/**
 * {@link TimedEventID}
 * 
 * @date $Date: 2014-08-04 14:19:04 +0200 (Mon, 04 Aug 2014) $
 * @version $Revision: 336 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T> the type of value for this {@link AbstractIdentifier} of an
 *        {@link Event}
 * @param <I> the type of instant for the identified {@link Event}
 */
public class TimedEventID<T extends Serializable & Comparable<T>, I extends Instant<I>>
		extends EventID<T> implements Timed<I>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** the {@link Instant} the identified {@link Event} occurs */
	private I instant;

	/**
	 * {@link TimedEventID} zreo-arg bean constructor
	 */
	protected TimedEventID()
	{

	}

	/**
	 * {@link TimedEventID} constructor
	 * 
	 * @param modelID
	 * @param value the (unique) value of this identifier
	 * @param instant the {@link Instant} the identified {@link Event} occurs
	 */
	public TimedEventID(final ModelID modelID, final T value, final I instant)
	{
		super(modelID, value);
		this.instant = instant;
	}

	/** @return the {@link Instant} the identified {@link Event} occurs */
	@Override
	public I getTime()
	{
		return this.instant;
	}

	@Override
	public String toString()
	{
		return String.format("%s t=%s", super.toString(),
				getTime() == null ? "?" : getTime().toString());
	}

}
