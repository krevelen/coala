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
 * {@link StageEvent}
 * 
 * @date $Date$
 * @version $Id: 4315b371d925dca6e1609359465a960d25eab26d $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public enum StageEvent
{
	/**
	 * Indicates that the {@link Staged}-annotated <b>static</b> method is to be
	 * invoked <b>before</b> the {@link InjectStaged}-annotated object is
	 * constructed or otherwise provided by its {@link Provider}
	 */
	BEFORE_PROVIDE,

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked
	 * <b>after</b> the (owner) {@link InjectStaged}-annotated object is
	 * constructed or otherwise provided by its {@link Provider}
	 */
	AFTER_PROVIDE,

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked
	 * <b>before</b> the (owner) {@link InjectStaged}-annotated object performs
	 * some custom stage
	 */
	BEFORE_STAGE,

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked
	 * <b>after</b> the (owner) {@link InjectStaged}-annotated object performs
	 * some custom stage
	 */
	AFTER_STAGE,

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked when
	 * some managed stage in the (owner) {@link InjectStaged}-annotated object
	 * throws a {@link Throwable}. If the method takes a relevant
	 * {@link Throwable} as (only) argument, it is passed along.
	 */
	BEFORE_FAIL,

	/**
	 * Indicates that the {@link Staged}-annotated <b>static</b> method is to be
	 * invoked after the (owner) {@link InjectStaged}-annotated object is
	 * destroyed (via {@link Object#finalize()} as called at discretion of the
	 * JVM's garbage collector (may be forced by calling {@link System#gc()}).
	 * <p>
	 * WARNING: Any {@link Throwable} will be ignored, perhaps simply output to
	 * {@link System#err}
	 */
	BEFORE_RECYCLE,

	;
}