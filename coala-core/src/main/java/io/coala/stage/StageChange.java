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
 * {@link StageChange}
 * 
 * @date $Date$
 * @version $Revision$
 * @author <a href="mailto:gebruiker@almende.org">gebruiker</a>
 */
public interface StageChange
{
	/**
	 * @return the concrete/runtime type of the ({@link Staged}) object (if
	 *         available, generic type otherwise)
	 */
	Class<?> getType();

	/**
	 * @return the hash code of the ({@link Staged}) target object (if
	 *         available)
	 */
	int getHash();

	/**
	 * @return the current {@link Stage} event of the ({@link Staged})
	 *         target object
	 */
	Stage getStage();

	/**
	 * @return {@link String} representation of the ({@link Staged}) target
	 *         object's current stage
	 */
	String getCustom();
}