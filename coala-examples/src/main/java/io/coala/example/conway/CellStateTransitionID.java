/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellStateTransitionID.java $
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
package io.coala.example.conway;

import io.coala.event.TimedEventID;
import io.coala.model.ModelID;
import io.coala.time.SimTime;

import com.eaio.uuid.UUID;

/**
 * {@link CellStateTransitionID}
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class CellStateTransitionID extends TimedEventID<UUID, SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link CellStateTransitionID} constructor
	 * 
	 * @param modelID
	 * @param value
	 * @param instant
	 */
	public CellStateTransitionID(final ModelID modelID, final SimTime instant)
	{
		super(modelID, new UUID(), instant);
	}

	/**
	 * {@link CellStateTransitionID} zero-arg bean constructor
	 */
	protected CellStateTransitionID()
	{
		super();
	}

}
