/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/message/MessageID.java $
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
package io.coala.message;

import io.coala.event.TimedEventID;
import io.coala.model.ModelID;
import io.coala.time.Instant;

import java.io.Serializable;

/**
 * {@link MessageID} sets the identifier value type of a {@link Message}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of identifier value for this {@link MessageID}
 * @param <I> the type of {@link Instant}
 * @param <THIS> the type of {@link MessageID} to compare with
 */
public class MessageID<ID extends Serializable & Comparable<ID>, I extends Instant<I>>
		extends TimedEventID<ID, I>
{

	/** */
	private static final long serialVersionUID = 1L;

	public MessageID()
	{

	}

	/**
	 * {@link MessageID} constructor
	 * 
	 * @param modelID
	 * @param value
	 * @param instant
	 */
	public MessageID(final ModelID modelID, final ID value, final I instant)
	{
		super(modelID, value, instant);
	}

}
