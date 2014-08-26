/* $Id$
 * $URL$
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
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.aglobe;

import aglobe.container.ElementaryEntity;

/**
 * {@link AGlobeAgentState}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public enum AGlobeAgentState
{
	/** */
	IDLE(ElementaryEntity.USE_IDLE_MASK),

	/** */
	CLONING(ElementaryEntity.CLONING),

	/** */
	DEAD(ElementaryEntity.DEAD),

	/** */
	DONE(ElementaryEntity.DONE),

	/** */
	EXCEPTION(ElementaryEntity.EXCEPTION),

	/** */
	INIT(ElementaryEntity.INIT),

	/** */
	MIGRATING(ElementaryEntity.MIGRATING),

	/** */
	RUNNING(ElementaryEntity.RUNNING),

	;

	private final int stateValue;

	private AGlobeAgentState(final int stateValue)
	{
		this.stateValue = stateValue;
	}

	public static AGlobeAgentState valueOf(final int entityState)
	{
		if ((entityState & ElementaryEntity.USE_IDLE_MASK) != 0)
			return IDLE;

		for (AGlobeAgentState state : values())
			if (state.stateValue == entityState)
				return state;

		throw new IllegalArgumentException("Undefined entity state value: "
				+ entityState);
	}

}