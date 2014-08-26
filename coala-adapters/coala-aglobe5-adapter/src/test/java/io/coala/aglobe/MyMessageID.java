/* $Id: MyMessageID.java 290 2014-06-03 07:28:53Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/aglobe-util/src/test/java/com/almende/coala/aglobe/MyMessageID.java $
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

import io.coala.message.MessageID;
import io.coala.model.ModelID;
import io.coala.time.SimTime;

/**
 * {@link MyMessageID}
 * 
 * @version $Revision: 290 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class MyMessageID extends MessageID<Long, SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static long ID_COUNT = 0;

	/**
	 * {@link MyMessageID} constructor
	 * 
	 * @param modelID
	 * @param instant
	 */
	public MyMessageID(final ModelID modelID, final SimTime instant)
	{
		super(modelID, ID_COUNT++, instant);
	}

}