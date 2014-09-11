/* $Id$
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/transaction/Transaction.java $
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
package io.coala.enterprise.transaction;

import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityID;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.model.ModelComponent;
import rx.Observable;

/**
 * {@link Transaction}
 * 
 * @version $Revision: 279 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <F> the (super)type of {@link CoordinationFact}
 * @param <THIS> the concrete type of {@link Transaction}
 */
public interface Transaction<F extends CoordinationFact>
		extends Capability<BasicCapabilityStatus>,
		ModelComponent<CapabilityID>
{

	// <I extends Initiator<F, I>> Class<I> getInitiatorRoleType();

	// <E extends Executor<F, E>> Class<E> getExecutorRoleType();

	// AgentID getInitiatorID();

	// AgentID getExecutorID();

	/** @return the coordination facts having occurred in this {@link Transaction} */
	Observable<F> facts();

	/** @return a new request coordination fact for this {@link Transaction} kind */
	// CoordinationFactBuilder<F, ?> createFact();

	/**
	 * @param cause the {@link CoordinationFact} that led to this request
	 * @return the {@link LookupFact} request resulting from specified cause
	 */
	//F createRequest(final CoordinationFact<?> cause);

	/**
	 * @param request the {@link CoordinationFact} for which to determine a response
	 * @return the {@link CoordinationFact} response or {@code null} for time-out
	 */
	//F createReponse(F request);

}
