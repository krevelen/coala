/* $Id: 55348422652f944b6e38d90ea251da12a25fb188 $
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/test/java/com/almende/coala/eve/MyMessageID.java $
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
package io.coala.eve;

import io.coala.message.MessageID;
import io.coala.model.ModelID;
import io.coala.time.SimTime;

/**
 * {@link MyMessageID}
 * 
 * @version $Revision: 290 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class MyMessageID extends MessageID<Long, SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static long msgID = 0;

	/**
	 * {@link MyMessageID} constructor
	 */
	protected MyMessageID()
	{
	}

	/**
	 * {@link MyMessageID} constructor
	 * 
	 * @param modelID
	 * @param instant
	 */
	public MyMessageID(final ModelID modelID, final SimTime instant)
	{
		super(modelID, msgID++, instant);
	}

}