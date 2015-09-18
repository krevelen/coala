/* $Id: acbcd336f18f8e070089ac13b693055601c6c306 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/impl/MyTestFactInitiatorRole.java $
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
package io.coala.enterprise.test.impl;

import io.coala.agent.AgentID;
import io.coala.bind.Binder;
import io.coala.capability.interact.SendingCapability;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.enterprise.role.AbstractInitiator;
import io.coala.enterprise.role.Initiator;
import io.coala.enterprise.test.TestFact;
import io.coala.enterprise.test.TestFact.Request;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link MyTestFactInitiatorRole}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class MyTestFactInitiatorRole extends
		AbstractInitiator<TestFact.Response> implements TestFact.Initiator
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link TestFactInitiatorRole} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected MyTestFactInitiatorRole(final Binder binder)
	{
		super(binder);
	}

	/** @see TestFact.InitiatorRole#initiate(AgentID) */
	@Override
	public Request initiate(final AgentID executorID) throws Exception
	{
		LOG.info("Initiating " + TestFact.class.getSimpleName()
				+ " for owner type: " + getOwnerType().getSimpleName());
		final Request result = Request.Builder.forProducer(this)
				.withReceiverID(executorID).build();
		getBinder().inject(SendingCapability.class).send(result);
		return result;
	}

	/** @see Initiator#onStated(CoordinationFact) */
	@Override
	public void onStated(final TestFact.Response result)
	{
		LOG.trace("Got result: " + result);
	}

}