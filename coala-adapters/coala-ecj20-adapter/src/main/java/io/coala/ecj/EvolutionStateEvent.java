/* $Id$
 * $URL$
 * 
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
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
package io.coala.ecj;

/**
 * {@link EvolutionStateEvent}
 * 
 * @date $Date$
 * @version $Revision$
 * @author <a href="mailto:rick@almende.org">Rick</a>
 */
public enum EvolutionStateEvent
{
	/** */
	ANTE_SETUP,

	/** */
	ANTE_INITIALIZE_POPULATION,

	/** */
	POST_INITIALIZE_POPULATION,

	/** */
	ANTE_INITIALIZE_CONTACTS,

	/** */
	POST_INITIALIZE_CONTACTS,

	/** */
	POST_SETUP,

	/** */
	ANTE_EVALUATE_POPULATION,

	/** */
	POST_EVALUATE_POPULATION,

	/** */
	ANTE_PRE_BREED_EXCHANGE_POPULATION,

	/** */
	POST_PRE_BREED_EXCHANGE_POPULATION,

	/** */
	ANTE_BREED_POPULATION,

	/** */
	POST_BREED_POPULATION,

	/** */
	ANTE_POST_BREED_EXCHANGE_POPULATION,

	/** */
	POST_POST_BREED_EXCHANGE_POPULATION,

	/** */
	ANTE_CHECKPOINT,

	/** */
	POST_CHECKPOINT,

	/** */
	NEXT_GENERATION,

	/** */
	RUN_SUCCESS,

	/** */
	RUN_FAILURE,
}