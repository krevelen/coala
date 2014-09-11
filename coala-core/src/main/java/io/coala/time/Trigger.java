/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/Trigger.java $
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

import io.coala.process.Job;
import rx.Observable;

/**
 * {@link Trigger}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class Trigger<I extends Instant<I>>
{

	private final Observable<I> instants;

	private final I start;

	private final Instant<?> interval;

	private final Instant<?> end;

	protected Trigger(final I time)
	{
		this(time, null);
	}

	protected Trigger(final I start, final Instant<?> interval)
	{
		this(start, interval, null);
	}

	protected Trigger(final I start, final Instant<?> interval, final int count)
	{
		this(start, interval, start.plus(interval.multipliedBy(count)));
	}

	protected Trigger(final I start, final Instant<?> interval,
			final Instant<?> end)
	{
		// TODO generate next instant lazily (rx.Schedulers), not immediately
		if (interval == null)
			this.instants = Observable.from(start);
		else
			this.instants = Observable.from(start.getRange(interval, end));

		this.start = start;
		this.interval = interval;
		this.end = end;
	}

	public Observable<I> getInstants()
	{
		return this.instants.asObservable();
	}

	/** @return the start time of this {@link Job} */
	public I getStartTime()
	{
		return this.start;
	}

	/**
	 * @return the cut-off time of this {@link Job}, or {@code null} if never
	 */
	public Instant<?> getCutoffTime()
	{
		return this.end;
	}

	/**
	 * @return the specified duration between executions, or {@code null} if no
	 *         repeat
	 */
	public Instant<?> getInterval()
	{
		return this.interval;
	}

	/**
	 * @return the maximum number of executions for this {@link Job} where
	 *         {@code x <= 0} indicates infinitely repeating
	 */
	public long getCount()
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * @return the ultimate duration of execution for this {@link Job} where
	 *         {@code null} indicates infinitely repeating execution allowed
	 */
	public I getTimeout()
	{
		throw new IllegalStateException("NOT IMPLEMENTED");
	}

	/**
	 * Schedule the specified method to be called with specified args at
	 * specified relative delay (>= 0)
	 * 
	 * @param relativeDelay the simulated delay >= 0 from current simulation
	 *        time
	 */
	public static <I extends Instant<I>> Trigger<I> createDelay(final I now,
			final Instant<?> relativeDelay)
	{
		return new Trigger<I>(now.plus(relativeDelay));
	}

	/**
	 * @param start
	 * @param interval
	 * @param count
	 * @return
	 */
	public static <I extends Instant<I>> Trigger<I> createRange(final I start,
			final Instant<?> interval, final int count)
	{
		return new Trigger<I>(start, interval, count);
	}

	/**
	 * @param start
	 * @param interval
	 * @param limit
	 * @return
	 */
	public static <I extends Instant<I>> Trigger<I> createRange(final I start,
			final Instant<?> interval, final Instant<?> limit)
	{
		return new Trigger<I>(start, interval, limit);
	}

	/**
	 * Schedule the specified method to be called with specified args at
	 * specified relative delay (>= 0)
	 * 
	 * @param absoluteTime the simulated delay >= 0 from current simulation time
	 */
	public static <I extends Instant<I>> Trigger<I> createAbsolute(
			final I absoluteTime)
	{
		return new Trigger<I>(absoluteTime);
	}

}
