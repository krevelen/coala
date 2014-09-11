/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/SchedulerService.java $
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
package io.coala.capability.plan;

import io.coala.capability.CapabilityFactory;
import io.coala.process.Job;
import io.coala.time.Instant;
import io.coala.time.Trigger;

/**
 * {@link SchedulingCapability} provides the time
 * 
 * @date $Date: 2014-07-21 11:20:27 +0200 (Mon, 21 Jul 2014) $
 * @version $Revision: 332 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <I> the type of {@link Instant} to provide
 */
public interface SchedulingCapability<I extends Instant<I>> extends TimingCapability<I>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 332 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	@SuppressWarnings("rawtypes")
	interface Factory extends CapabilityFactory<SchedulingCapability>
	{
		// empty
	}

	/**
	 * {@link EventBuilder} useful to build scheduling commands
	 * 
	 * @version $Revision: 332 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	// interface EventBuilder
	// {
	// EventBuilder call(Job job);
	//
	// EventBuilder at(I absoluteTime);
	//
	// EventBuilder after(I delay);
	// }
	//
	// EventBuilder call(Job job);
	//
	// EventBuilder at(I absoluteTime);
	//
	// EventBuilder after(I delay);

	/** @return */
	// @JsonIgnore
	// Observable<Job<?>> getJobsFired();

	/** */
	void schedule(Job<?> job, Trigger<?> trigger);

	/**
	 * Cancel the specified {@link Job}
	 * 
	 * @param job the command to unschedule
	 * @return {@code true} if cancelled, {@code false} if not found (e.g. never
	 *         scheduled, already cancelled, or already executed)
	 */
	boolean unschedule(Job<?> job);

}
