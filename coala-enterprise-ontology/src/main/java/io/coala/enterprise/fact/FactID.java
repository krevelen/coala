/* $Id: efaf010ab36f301a2035783d3d0b968a35fd7a04 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/fact/FactID.java $
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

import io.coala.enterprise.transaction.Transaction;
import io.coala.enterprise.transaction.TransactionID;
import io.coala.message.MessageID;
import io.coala.model.ModelID;
import io.coala.time.SimTime;

import com.eaio.uuid.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link FactID}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class FactID extends MessageID<UUID, SimTime>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * the {@link TransactionID} identifying the {@link Transaction} of the
	 * {@link CoordinationFact} identified by this {@link FactID}
	 */
	private TransactionID transactionID;

	/**
	 * the {@link CoordinationFactType} of the {@link CoordinationFact}
	 * identified by this {@link FactID}
	 */
	private CoordinationFactType type;

	/**
	 * the {@link FactID} identifying the {@link CoordinationFact} that caused
	 * the {@link CoordinationFact} identified by this {@link FactID}
	 */
	private FactID causeID;

	/**
	 * {@link FactID} constructor for identifying the root fact of type
	 * {@link CoordinationFactType#REQUESTED} of an entire {@link Transaction}
	 * tree (i.e. a fact without any causal {@link CoordinationFact} that
	 * triggers a completely new process structure)
	 * 
	 * @param modelID the {@link ModelID} for generating a new
	 *        {@link TransactionID}
	 * @param time the {@link SimTime} of creation
	 */
	public FactID(final ModelID modelID, final SimTime time)
	{
		this(modelID, CoordinationFactType.REQUESTED, time);
	}

	/**
	 * {@link FactID} constructor for identifying the root fact of type
	 * {@link CoordinationFactType#REQUESTED} of an entire {@link Transaction}
	 * tree (i.e. a fact without any causal {@link CoordinationFact} that
	 * triggers a completely new process structure)
	 * 
	 * @param modelID the {@link ModelID} of the identified fact
	 * @param type the {@link CoordinationFactType}, typically
	 *        {@link CoordinationFactType#REQUESTED}
	 * @param time the {@link SimTime} of creation
	 */
	public FactID(final ModelID modelID, final CoordinationFactType type,
			final SimTime time)
	{
		this(new TransactionID(modelID), type, time, (FactID) null);
	}

	/**
	 * {@link FactID} constructor for identifying a new fact in the same
	 * transaction as the specified causal {@link CoordinationFact}
	 * 
	 * @param type the {@link CoordinationFactType}
	 * @param time the {@link SimTime} of creation
	 * @param cause the {@link CoordinationFact} that caused this new fact
	 */
	public FactID(final CoordinationFactType type, final SimTime time,
			final CoordinationFact cause)
	{
		this(cause.getID().getTransactionID(), type, time, cause.getID());
	}

	/**
	 * {@link FactID} constructor for identifying a new fact in the same
	 * transaction as the specified causal {@link CoordinationFact}
	 * 
	 * @param type the {@link CoordinationFactType}
	 * @param time the {@link SimTime} of creation
	 * @param causeID the {@link FactID} of the fact that caused this new fact
	 */
	public FactID(final CoordinationFactType type, final SimTime time,
			final FactID causeID)
	{
		this(causeID.getTransactionID(), type, time, causeID);
	}

	/**
	 * {@link FactID} constructor for identifying a new
	 * {@link CoordinationFactType#REQUESTED} type fact in a new nested
	 * transaction
	 * 
	 * @param time the {@link SimTime} of creation
	 * @param cause the {@link CoordinationFact} that caused this new fact
	 */
	public FactID(final SimTime time, final CoordinationFact cause)
	{
		this(new TransactionID(time.getClockID().getModelID()),
				CoordinationFactType.REQUESTED, time, cause);
	}

	/**
	 * {@link FactID} constructor for identifying a new
	 * {@link CoordinationFactType#REQUESTED} type fact in a new nested
	 * transaction
	 * 
	 * @param time the {@link SimTime} of creation
	 * @param causeID the {@link FactID} of the fact that caused this new fact
	 */
	public FactID(final SimTime time, final FactID causeID)
	{
		this(new TransactionID(causeID.getModelID()),
				CoordinationFactType.REQUESTED, time, causeID);
	}

	/**
	 * {@link FactID} constructor
	 * 
	 * @param transactionID the {@link TransactionID} the new transaction's id
	 * @param type the {@link CoordinationFactType}
	 * @param time the {@link SimTime} of creation
	 * @param cause the {@link CoordinationFact} that caused this new fact
	 */
	public FactID(final TransactionID transactionID,
			final CoordinationFactType type, final SimTime time,
			final CoordinationFact cause)
	{
		this(transactionID, type, time, cause == null ? null : cause.getID());
	}

	/**
	 * {@link FactID} constructor
	 * 
	 * @param transactionID the {@link TransactionID}
	 * @param type the {@link CoordinationFactType}
	 * @param time the {@link SimTime} of creation
	 * @param causeID the {@link FactID} of the fact that caused this new fact
	 */
	public FactID(final TransactionID transactionID,
			final CoordinationFactType type, final SimTime time,
			final FactID causeID)
	{
		super(transactionID.getModelID(), new UUID(), time);
		this.transactionID = transactionID;
		this.type = type;
		this.causeID = causeID;
	}

	/**
	 * {@link FactID} zero-arg constructor for deserialization
	 */
	protected FactID()
	{
		super();
	}
	
	@JsonIgnore
	@Override
	public ModelID getModelID()
	{
		return getTime().getClockID().getModelID();
	}

	/**
	 * @return the {@link TransactionID} identifying the {@link Transaction} of
	 *         the {@link CoordinationFact} identified by this {@link FactID}
	 */
	public TransactionID getTransactionID()
	{
		return this.transactionID;
	}

	/**
	 * @return the {@link CoordinationFactType} of the {@link CoordinationFact}
	 *         identified by this {@link FactID}
	 */
	public CoordinationFactType getType()
	{
		return this.type;
	}

	/**
	 * @return the {@link FactID} of the {@link CoordinationFact} that caused
	 *         the one identified by this {@link FactID}
	 */
	public FactID getCauseID()
	{
		return this.causeID;
	}

}
