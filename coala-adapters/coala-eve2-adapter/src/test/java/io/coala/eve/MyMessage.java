/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/test/java/com/almende/coala/eve/MyMessage.java $
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

import io.coala.agent.AgentID;
import io.coala.message.AbstractMessage;
import io.coala.time.SimTime;

/**
 * {@link MyMessage}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class MyMessage extends AbstractMessage<MyMessageID>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	public String content = null;

	/**
	 * {@link MyMessage} zero-arg bean constructor
	 */
	protected MyMessage()
	{
	}

	/**
	 * {@link MyMessage} constructor
	 * 
	 * @param time
	 * @param senderID
	 * @param receiverID
	 * @param content
	 */
	public MyMessage(SimTime time, AgentID senderID, AgentID receiverID,
			String content)
	{
		super(new MyMessageID(senderID.getModelID(), time), senderID
				.getModelID(), senderID, receiverID);
		this.content = content;
	}

}