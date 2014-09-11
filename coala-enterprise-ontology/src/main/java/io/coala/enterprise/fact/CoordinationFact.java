/* $Id$
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/fact/CoordinationFact.java $
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
import io.coala.message.Message;
import io.coala.model.ModelID;
import io.coala.time.SimTime;

/**
 * {@link CoordinationFact}
 * 
 * @version $Revision: 305 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public interface CoordinationFact extends Message<FactID>
{

	/**
	 * @return the {@link TransactionID} identifying the {@link Transaction} of
	 *         this {@link CoordinationFact}
	 */
	TransactionID getTransactionID();

	/**
	 * @return the {@link CoordinationFactType} of this {@link CoordinationFact}
	 */
	CoordinationFactType getType();

	/**
	 * @return the {@link FactID} of the {@link CoordinationFact} that caused
	 *         the one
	 */
	FactID getCauseID();

	/** @return the absolute instant of expiration, or {@code null} if never */
	SimTime getExpiration();

	/**
	 * {@link CoordinationFactBuilder}
	 * 
	 * @version $Revision: 305 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 * @param <F> the (super)type of {@link CoordinationFact}
	 * @parm <THIS> the concrete type of {@link CoordinationFactBuilder}
	 */
	public interface Builder<F extends CoordinationFact, THIS extends Builder<F, THIS>>
			extends Message.Builder<FactID, F, THIS>
	{

		/**
		 * {@link FactID} builder for identifying the root REQUEST of a
		 * transaction tree (i.e. without any causal {@link CoordinationFact})
		 * 
		 * @param modelID the {@link ModelID} for generating a new
		 *            {@link TransactionID}
		 * @param time the {@link SimTime} of creation
		 */
		THIS withID(ModelID modelID, SimTime time);

		/**
		 * {@link FactID} builder for identifying the root fact of a transaction
		 * tree (i.e. without any causal {@link CoordinationFact})
		 * 
		 * @param modelID the {@link ModelID} of the identified fact
		 * @param type the {@link CoordinationFactType}, typically
		 *            {@link CoordinationFactType#REQUESTED}
		 * @param time the {@link SimTime} of creation
		 */
		THIS withID(ModelID modelID, CoordinationFactType type, SimTime time);

		/**
		 * {@link FactID} builder for identifying a new fact in the same
		 * transaction as the specified causal {@link CoordinationFact}
		 * 
		 * @param type the {@link CoordinationFactType}
		 * @param time the {@link SimTime} of creation
		 * @param cause the {@link CoordinationFact} that caused this new fact
		 */
		THIS withID(CoordinationFactType type, SimTime time,
				CoordinationFact cause);

		/**
		 * {@link FactID} builder for identifying a new fact in the same
		 * transaction as the specified causal {@link CoordinationFact}
		 * 
		 * @param type the {@link CoordinationFactType}
		 * @param time the {@link SimTime} of creation
		 * @param causeID the {@link FactID} of the fact that caused this new
		 *            fact
		 */
		THIS withID(CoordinationFactType type, SimTime time, FactID causeID);

		/**
		 * {@link FactID} builder for identifying a new
		 * {@link CoordinationFactType#REQUESTED} type fact in a new nested
		 * transaction
		 * 
		 * @param time the {@link SimTime} of creation
		 * @param cause the {@link CoordinationFact} that caused this new fact
		 */
		THIS withID(SimTime time, CoordinationFact cause);

		/**
		 * {@link FactID} builder for identifying a new
		 * {@link CoordinationFactType#REQUESTED} type fact in a new nested
		 * transaction
		 * 
		 * @param time the {@link SimTime} of creation
		 * @param causeID the {@link FactID} of the fact that caused this new
		 *            fact
		 */
		THIS withID(SimTime time, FactID causeID);

		/**
		 * {@link FactID} constructor
		 * 
		 * @param transactionID the {@link TransactionID} the new transaction's
		 *            id
		 * @param type the {@link CoordinationFactType}
		 * @param time the {@link SimTime} of creation
		 * @param cause the {@link CoordinationFact} that caused this new fact
		 */
		THIS withID(TransactionID transactionID, CoordinationFactType type,
				SimTime time, CoordinationFact cause);

		/**
		 * {@link FactID} builder
		 * 
		 * @param transactionID the {@link TransactionID}
		 * @param type the {@link CoordinationFactType}
		 * @param time the {@link SimTime} of creation
		 * @param causeID the {@link FactID} of the fact that caused this new
		 *            fact
		 */
		THIS withID(TransactionID transactionID, CoordinationFactType type,
				SimTime time, FactID causeID);

		/**
		 * @param expiration
		 * @return this {@link Builder} for chaining purposes
		 */
		THIS withExpiration(SimTime expiration);

	}

}
