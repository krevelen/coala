/* $Id: fae48dd75c67a41d26e856531d7d4ced697d9825 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/fact/AbstractCoordinationFact.java $
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
package io.coala.enterprise.fact;

import io.coala.agent.AgentID;
import io.coala.enterprise.transaction.TransactionID;
import io.coala.message.AbstractMessage;
import io.coala.model.ModelComponentID;
import io.coala.time.SimTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link AbstractCoordinationFact}
 * 
 * @version $Revision: 305 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class AbstractCoordinationFact extends AbstractMessage<FactID>
		implements CoordinationFact
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private SimTime expiration;

	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID)
	// {
	// this(producerRole, type, receiverID, (CoordinationFact) null,
	// (SimTime) null);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final SimTime timeout)
	// {
	// this(producerRole, type, receiverID, null, timeout);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param cause or {@code null}
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final CoordinationFact cause)
	// {
	// this(producerRole, type, receiverID, cause, null);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param cause or {@code null}
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final CoordinationFact cause, final SimTime timeout)
	// {
	// this(producerRole, type, receiverID, cause == null ? null : cause
	// .getID().getTransactionID(), cause, timeout);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param transactionID or {@code null} for new transaction (e.g. REQUEST)
	// * @param cause or {@code null}
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final TransactionID transactionID, final CoordinationFact cause)
	// {
	// this(producerRole, type, receiverID, transactionID, cause, null);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param transactionID or {@code null} for new transaction (e.g. REQUEST)
	// * @param cause or {@code null}
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final TransactionID transactionID, final CoordinationFact cause,
	// final SimTime timeout)
	// {
	// this(new FactID(transactionID == null ? new TransactionID(producerRole
	// .getOwnerID().getModelID()) : transactionID, type,
	// producerRole.getTime(), cause == null ? null : cause.getID()),
	// producerRole.getID(), producerRole.getOwnerID(), receiverID,
	// timeout);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param transactionID
	// * @param causeID
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final TransactionID transactionID, final FactID causeID)
	// {
	// this(producerRole, type, receiverID, transactionID, causeID, null);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param producerRole
	// * @param type
	// * @param receiverID
	// * @param transactionID
	// * @param causeID
	// */
	// protected AbstractCoordinationFact(final ActorRole<?> producerRole,
	// final CoordinationFactType type, final AgentID receiverID,
	// final TransactionID transactionID, final FactID causeID,
	// final SimTime timeout)
	// {
	// this(new FactID(transactionID, type, producerRole.getTime(), causeID),
	// producerRole.getID(), producerRole.getOwnerID(), receiverID,
	// timeout);
	// }
	//
	// /**
	// * {@link AbstractCoordinationFact} constructor
	// *
	// * @param id
	// * @param producerID
	// * @param senderID
	// * @param receiverID
	// */
	// protected AbstractCoordinationFact(final FactID id,
	// final ModelComponentID<?, ?> producerID, final AgentID senderID,
	// final AgentID receiverID)
	// {
	// this(id, producerID, senderID, receiverID, null);
	// }

	/**
	 * {@link AbstractCoordinationFact} constructor
	 * 
	 * @param id
	 * @param producerID
	 * @param senderID
	 * @param receiverID
	 * @param expiration the absolute instant when this fact invalidates, or
	 *        {@code null} if never
	 */
	protected AbstractCoordinationFact(final FactID id,
			final ModelComponentID<?> producerID, final AgentID senderID,
			final AgentID receiverID, final SimTime expiration)
	{
		super(id, producerID, senderID, receiverID);
		this.expiration = expiration;
	}

	/**
	 * {@link AbstractCoordinationFact} zero-arg bean constructor
	 */
	protected AbstractCoordinationFact()
	{
		super();
	}

	/** @see CoordinationFact#getExpiration() */
	@Override
	public SimTime getExpiration()
	{
		return this.expiration;
	}

	@JsonIgnore
	@Override
	public TransactionID getTransactionID()
	{
		return getID().getTransactionID();
	}

	@JsonIgnore
	@Override
	public CoordinationFactType getType()
	{
		return getID().getType();
	}

	@JsonIgnore
	@Override
	public FactID getCauseID()
	{
		return getID().getCauseID();
	}

}
