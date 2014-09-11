/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolReplicationMode.java $
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
package io.coala.dsol.util;

import nl.tudelft.simulation.dsol.experiment.Treatment;

/**
 * {@link DsolReplicationMode}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public enum DsolReplicationMode
{
	/** */
	STEADY_STATE(Treatment.REPLICATION_MODE_STEADY_STATE),

	/** */
	TERMINATING(Treatment.REPLICATION_MODE_TERMINATING),

	;

	/** */
	private final short mode;

	/** */
	private DsolReplicationMode(final short mode)
	{
		this.mode = mode;
	}

	/**
	 * @return the respective {@link short} value defined in
	 *         {@link Treatment}
	 */
	public short getValue()
	{
		return this.mode;
	}
}