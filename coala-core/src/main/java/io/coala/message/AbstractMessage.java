/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/message/AbstractMessage.java $
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
import io.coala.event.AbstractTimedEvent;
import io.coala.model.ModelComponent;
import io.coala.model.ModelComponentID;
import io.coala.time.Instant;

import javax.inject.Inject;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link AbstractMessage}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public abstract class AbstractMessage<ID extends MessageID<?, ?>> extends
		AbstractTimedEvent<ID> implements Message<ID>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private AgentID senderID;

	/** */
	private AgentID receiverID;

	/**
	 * {@link AbstractMessage} constructor
	 * 
	 * @param id
	 * @param producerID
	 * @param senderID
	 * @param receiverID
	 */
	@Inject
	public AbstractMessage(final ID id, final ModelComponentID<?> producerID,
			final AgentID senderID, final AgentID receiverID)
	{
		super(id, null, null);
		this.senderID = senderID;
		this.receiverID = receiverID;
	}

	/**
	 * {@link AbstractMessage} constructor
	 * 
	 * @param modelID
	 * @param producerID
	 * @param receiverID
	 */
	public AbstractMessage(final ID id, final ModelComponent<?> producer,
			final AgentID receiverID)
	{
		super(id, producer);
		this.senderID = producer.getOwnerID();
		this.receiverID = receiverID;
	}

	/**
	 * {@link AbstractMessage} zero-arg bean constructor
	 */
	protected AbstractMessage()
	{
		super();
	}

	@JsonIgnore
	@Override
	public AgentID getProducerID()
	{
		return getSenderID();
	}

	@JsonIgnore
	@Override
	public AgentID getOwnerID()
	{
		return getSenderID();
	}

	@JsonIgnore
	@Override
	public Instant<?> getTime()
	{
		return getID().getTime();
	}

	@Override
	public AgentID getSenderID()
	{
		return this.senderID;
	}

	@Override
	public AgentID getReceiverID()
	{
		return this.receiverID;
	}

}
