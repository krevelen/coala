/* $Id$
 * $URL$
 * 
 * Part of the EU project Inertia, see http://www.inertia-project.eu/
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
 * Copyright (c) 2014 Almende B.V. 
 */
package io.coala.stage;

/**
 * {@link Stage}
 * 
 * @date $Date$
 * @version $Revision$
 * @author <a href="mailto:gebruiker@almende.org">gebruiker</a>
 */
public enum Stage
{
	/**
	 * executing {@link Staged}-annotated event handler methods subscribed to
	 * {@link StageEvent#BEFORE_PROVIDE} events
	 */
	PREPARING,

	/** executing the {@link Provider} for the {@link InjectStaged} */
	PROVIDING,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to {@link StageEvent#AFTER_PROVIDE} events
	 */
	PROVIDED,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to {@link StageEvent#BEFORE_STAGE} events
	 */
	STARTING,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to a custom stage (mentioned in e.g. {@link InjectStaged#beforeProvide()
	 * beforeConstruct=...} or {@link InjectStaged#afterProvide()
	 * afterConstruct=...})
	 */
	STARTED,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to {@link StageEvent#AFTER_STAGE}
	 */
	STOPPING,

	/** construction (and stages, if any) complete */
	STOPPED,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to {@link StageEvent#BEFORE_FAIL}
	 */
	FAILING,

	/** error/exception/throwable handled */
	FAILED,

	/**
	 * executing {@link InjectStaged}-annotated event handler methods subscribed
	 * to {@link StageEvent#BEFORE_RECYCLE}
	 */
	RECYCLING,

	/**
	 * destroyed (subject has been garbage collected, e.g. via
	 * {@link System#gc()})
	 */
	RECYCLED,

	;
}