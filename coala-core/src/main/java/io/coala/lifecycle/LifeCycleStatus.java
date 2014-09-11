/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/LifeCycleStatus.java $
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
package io.coala.lifecycle;

/**
 * {@link LifeCycleStatus} represents a state of a {@link LifeCycle} object, a
 * kind of state machine with standard states for "started" and "stopped"
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the (sub)type of {@link LifeCycleStatus}
 */
public interface LifeCycleStatus<THIS extends LifeCycleStatus<THIS>> extends
		MachineStatus<THIS>
{

	/**
	 * @return the {@code true} if the {@link LifeCycle} is initializing,
	 *         {@code false} otherwise
	 */
	boolean isCreatedStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} has initialized,
	 *         {@code false} otherwise
	 */
	boolean isInitializedStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} is executing, {@code false}
	 *         otherwise
	 */
	boolean isActiveStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} paused, {@code false}
	 *         otherwise
	 */
	boolean isPassiveStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} is finalizing,
	 *         {@code false} otherwise
	 */
	boolean isCompleteStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} has finished,
	 *         {@code false} otherwise
	 */
	boolean isFinishedStatus();

	/**
	 * @return the {@code true} if the {@link LifeCycle} has failed,
	 *         {@code false} otherwise
	 */
	boolean isFailedStatus();

}
