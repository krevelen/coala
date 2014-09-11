/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/LifeState.java $
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
package io.coala.example.conway;

import io.coala.log.LogUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * {@link LifeState} tells whether a {@link Cell} is (or transitions to being)
 * alive or dead
 * 
 * @date $Date: 2014-06-03 13:55:16 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public enum LifeState
{
	/** the {@link Cell} is alive */
	ALIVE,

	/** the {@link Cell} is dead */
	DEAD,

	;

	/** */
	private static final Logger LOG = LogUtil.getLogger(LifeState.class);

	/**
	 * @param states counts per the neighboring {@link LifeState}
	 * @return the next state as prescribed by Conway's rules given specified
	 *         neighbor states
	 */
	public List<LifeState> getTransitionOptions(
			final Map<LifeState, Integer> states)
	{
		int aliveCount = states.containsKey(ALIVE) ? states.get(ALIVE) : 0;
		int deadCount = states.containsKey(DEAD) ? states.get(DEAD) : 0;
		int total = aliveCount + deadCount;

		// sanity check
		if (total > 8)
			LOG.warn(String.format("Too many neighbors: %d (>8) with "
					+ "%d living and %d dead", total, aliveCount, deadCount));

		if (aliveCount > 3)
			return Collections.singletonList(DEAD);

		if (this == ALIVE && (aliveCount == 2 || aliveCount == 3) && total == 8)
			return Collections.singletonList(ALIVE);

		if (this == DEAD && aliveCount == 3 && total == 8)
			return Collections.singletonList(ALIVE);

		return Arrays.asList(values());
	}
}