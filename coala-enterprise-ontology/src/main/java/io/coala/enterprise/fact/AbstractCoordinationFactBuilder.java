/* $Id: dd62bda028e565c1d5fa8be926e529152b5359e1 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/fact/AbstractCoordinationFactBuilder.java $
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

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.enterprise.fact.CoordinationFact.Builder;
import io.coala.enterprise.transaction.TransactionID;
import io.coala.event.Event;
import io.coala.message.Message;
import io.coala.model.ModelComponent;
import io.coala.model.ModelComponentID;
import io.coala.model.ModelID;
import io.coala.name.Identifiable;
import io.coala.time.SimTime;

/**
 * {@link AbstractCoordinationFactBuilder}
 * 
 * @version $Revision: 290 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class AbstractCoordinationFactBuilder<F extends CoordinationFact, THIS extends AbstractCoordinationFactBuilder<F, THIS>>
		implements Builder<F, THIS>
{

	/** */
	private FactID factID = null;

	/** */
	private ModelComponentID<?> producerID = null;

	/** */
	private AgentID senderID = null;

	/** */
	private AgentID receiverID = null;

	/** */
	private AgentID ownerID = null;

	/** */
	private SimTime expiration = null;

	/** @return the {@link FactID} of the identified {@link CoordinationFact} */
	protected FactID getFactID()
	{
		return this.factID;
	}

	/**
	 * @return the {@link ModelComponentID} identifying the
	 *         {@link ModelComponent} that produced the identified
	 *         {@link CoordinationFact}
	 */
	protected ModelComponentID<?> getProducerID()
	{
		return this.producerID;
	}

	/**
	 * @return the {@link AgentID} identifying the {@link Agent} sending the
	 *         identified {@link CoordinationFact}
	 */
	protected AgentID getSenderID()
	{
		return this.senderID;
	};

	/**
	 * @return the {@link AgentID} identifying the {@link Agent} receiving the
	 *         identified {@link CoordinationFact}
	 */
	protected AgentID getReceiverID()
	{
		return this.receiverID;
	};

	/**
	 * @return the {@link AgentID} identifying the {@link Agent} owning the
	 *         {@link ModelComponent}
	 */
	protected AgentID getOwnerID()
	{
		return this.ownerID;
	};

	/**
	 * @return the {@link SimTime} marking the expiration of the identified
	 *         {@link CoordinationFact}
	 */
	protected SimTime getExpiration()
	{
		return this.expiration;
	}

	@SuppressWarnings("unchecked")
	protected THIS self()
	{
		return (THIS) this;
	}

	/** @see Identifiable.Builder#withID(FactID) */
	@Override
	public THIS withID(final FactID factID)
	{
		this.factID = factID;
		return self();
	}

	/** @see CoordinationFact.Builder#withID(ModelID, SimTime) */
	@Override
	public THIS withID(final ModelID modelID, final SimTime time)
	{
		return withID(new FactID(modelID, time));
	}

	/**
	 * @see CoordinationFact.Builder#withID(ModelID, CoordinationFactType,
	 *      SimTime)
	 */
	@Override
	public THIS withID(final ModelID modelID, final CoordinationFactType type,
			final SimTime time)
	{
		return withID(new FactID(modelID, type, time));
	}

	/**
	 * @see CoordinationFact.Builder#withID(CoordinationFactType, SimTime,
	 *      CoordinationFact)
	 */
	@Override
	public THIS withID(final CoordinationFactType type, final SimTime time,
			final CoordinationFact cause)
	{
		return withID(new FactID(type, time, cause));
	}

	/**
	 * @see CoordinationFact.Builder#withID(CoordinationFactType, SimTime,
	 *      FactID)
	 */
	@Override
	public THIS withID(final CoordinationFactType type, final SimTime time,
			final FactID causeID)
	{
		return withID(new FactID(type, time, causeID));
	}

	/**
	 * @see CoordinationFact.Builder#withID(SimTime, CoordinationFact)
	 */
	@Override
	public THIS withID(final SimTime time, final CoordinationFact cause)
	{
		return withID(new FactID(time, cause));
	}

	/**
	 * @see CoordinationFact.Builder#withID(SimTime, FactID)
	 */
	@Override
	public THIS withID(final SimTime time, final FactID causeID)
	{
		return withID(new FactID(time, causeID));
	}

	/**
	 * @see CoordinationFact.Builder#withID(TransactionID, CoordinationFactType,
	 *      SimTime, CoordinationFact)
	 */
	@Override
	public THIS withID(final TransactionID transactionID,
			final CoordinationFactType type, final SimTime time,
			final CoordinationFact cause)
	{
		return withID(new FactID(transactionID, type, time, cause));
	}

	/**
	 * @see CoordinationFact.Builder#withID(TransactionID, CoordinationFactType,
	 *      SimTime, FactID)
	 */
	@Override
	public THIS withID(final TransactionID transactionID,
			final CoordinationFactType type, final SimTime time,
			final FactID causeID)
	{
		return withID(new FactID(transactionID, type, time, causeID));
	}

	/** @see CoordinationFact.Builder#withExpiration(com.almende.coala.time.SimTime) */
	@Override
	public THIS withExpiration(final SimTime expiration)
	{
		this.expiration = expiration;
		return self();
	}

	/** @see Message.Builder#withSenderID(AgentID) */
	@Override
	public THIS withSenderID(final AgentID senderID)
	{
		this.senderID = senderID;
		return self();
	}

	/** @see Message.Builder#withReceiverID(AgentID) */
	@Override
	public THIS withReceiverID(final AgentID receiverID)
	{
		this.receiverID = receiverID;
		return self();
	}

	/** @see Event.Builder#withProducerID(ModelComponentID) */
	@Override
	public THIS withProducerID(final ModelComponentID<?> producerID)
	{
		this.producerID = producerID;
		return self();
	}

	/** @see ModelComponent.Builder#withOwnerID(AgentID) */
	@Override
	public THIS withOwnerID(final AgentID ownerID)
	{
		this.ownerID = ownerID;
		return self();
	}

	/** @see Event.Builder#withProducerID(ModelComponentID) */
	public THIS withProducer(final ModelComponent<?> producer)
	{
		return withProducerID(producer.getID()).withOwnerID(
				producer.getOwnerID()).withSenderID(producer.getOwnerID());
	}

}
