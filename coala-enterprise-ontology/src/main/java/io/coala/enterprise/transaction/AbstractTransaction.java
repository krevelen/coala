/* $Id: d5a9a0abe0a92ffbc60a84cf33d0bf0b20b18703 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/transaction/AbstractTransaction.java $
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

import io.coala.agent.AgentID;
import io.coala.bind.Binder;
import io.coala.capability.AbstractCapability;
import io.coala.capability.CapabilityID;
import io.coala.capability.plan.TimingCapability;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.enterprise.service.FactPersisterService;
import io.coala.model.ModelComponent;
import io.coala.time.SimTime;

import javax.inject.Inject;

import rx.Observable;

/**
 * {@link AbstractTransaction}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <F> the (super)type of {@link CoordinationFact}
 * @param <THIS> the concrete type of {@link Transaction}
 */
public class AbstractTransaction<F extends CoordinationFact, THIS extends AbstractTransaction<F, THIS>>
		extends AbstractCapability<CapabilityID> implements Transaction<F>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link AbstractTransaction} zero-arg constructor, to adhere to
	 * bean-convention
	 */
	@SuppressWarnings({ "unchecked" })
	@Inject
	protected AbstractTransaction(final Binder binder)
	{
		super(null, binder);
		setID(new TransactionTypeID<F, THIS>(getBinder().getID(),
				(Class<THIS>) getClass()));
	}

	/** @see Transaction#facts() */
	@SuppressWarnings("unchecked")
	@Override
	public Observable<F> facts()
	{
		return getBinder().inject(FactPersisterService.class).find(
				((TransactionTypeID<F, THIS>) getID()).getFactType());
	}

	/** @see ModelComponent#getOwnerID() */
	@Override
	public AgentID getOwnerID()
	{
		return super.getID().getOwnerID();
	}

	/**
	 * helper method
	 * 
	 * @return the current {@link SimTime}
	 */
	@Override
	public SimTime getTime()
	{
		return (SimTime) getBinder().inject(TimingCapability.class).getTime();
	}

}