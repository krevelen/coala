/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/message/Message.java $
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

import io.coala.agent.AgentID;
import io.coala.event.TimedEvent;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;

/**
 * {@link Message}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of identifier used for this {@link Message} type
 * @param <THIS> the type of {@link Message} to build
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, include = As.PROPERTY, property = "class")
public interface Message<ID extends MessageID<?, ?>> extends TimedEvent<ID>
{

	/** @return the {@link AgentID} of this {@link Message}'s sender */
	AgentID getSenderID();

	/** @return the {@link AgentID} of this {@link Message}'s receiver */
	AgentID getReceiverID();

	/**
	 * {@link Builder}
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 * @param <ID>
	 * @param <M>
	 * @param <THIS>
	 */
	public interface Builder<ID extends MessageID<?, ?>, M extends Message<ID>, THIS extends Builder<ID, M, THIS>>
			extends TimedEvent.Builder<ID, M, THIS>
	{

		/**
		 * @param senderID
		 * @return
		 */
		THIS withSenderID(AgentID senderID);

		/**
		 * @param receiverID
		 * @return
		 */
		THIS withReceiverID(AgentID receiverID);
	}

}
