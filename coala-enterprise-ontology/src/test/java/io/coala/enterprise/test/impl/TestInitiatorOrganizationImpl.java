/* $Id: e31c6658163d9f6682dc65a18ca863aa48288473 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/impl/TestInitiatorOrganizationImpl.java $
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
import io.coala.capability.configure.ConfiguringCapability;
import io.coala.enterprise.organization.AbstractOrganization;
import io.coala.enterprise.test.TestFact;
import io.coala.enterprise.test.TestInitiatorOrganization;
import io.coala.invoke.ProcedureCall;
import io.coala.invoke.Schedulable;
import io.coala.log.InjectLogger;
import io.coala.model.ModelComponentIDFactory;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;
import io.coala.time.Trigger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link TestInitiatorOrganizationImpl}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class TestInitiatorOrganizationImpl extends AbstractOrganization
		implements TestInitiatorOrganization
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link TestInitiatorOrganizationImpl} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected TestInitiatorOrganizationImpl(final Binder binder)
	{
		super(binder);
	}

	/** @see TestInitiatorOrganization#getTestFactInitiator() */
	@Override
	public TestFact.Initiator getTestFactInitiator()
	{
		return getBinder().inject(TestFact.Initiator.class);
	}

	/** @see AbstractOrganization#initializeRoles() */
	@Override
	public void initialize() throws Exception
	{
		super.initialize();

		LOG.trace("Initializing organization...");
		getSimulator().schedule(
				ProcedureCall.create(this, this, DO_REQUEST_METHOD_ID, 0),
				Trigger.createAbsolute(getTime()));
		LOG.trace("Initialized!");

		getSimulator().start();
	}

	/** */
	private static final String DO_REQUEST_METHOD_ID = "newRequest/1";

	protected ConfiguringCapability getConfig()
	{
		return getBinder().inject(ConfiguringCapability.class);
	}

	protected AgentID newAgentID(final String value)
	{
		return getBinder().inject(ModelComponentIDFactory.class).createAgentID(
				value);
	}

	/**
	 * @param number
	 * @throws Exception
	 */
	@Schedulable(DO_REQUEST_METHOD_ID)
	protected void newRequest(final long number) throws Exception
	{
		LOG.trace("Initiating request " + number);
		final TestFact.Request req = getTestFactInitiator().initiate(
				newAgentID(getConfig().getProperty(EXECUTOR_NAME_KEY).get(
						EXECUTOR_NAME_DEFAULT)));

		if (number >= 2)
		{
			LOG.trace("Initiated request " + number + ": " + req.getID()
					+ ", dying...");
			// die();
			return;
		}

		final SimTime then = getTime().plus(1, TimeUnit.DAYS);
		final long next = number + 1;
		LOG.trace("Repeating initiation @ " + then);

		getSimulator().schedule(
				ProcedureCall.create(this, this, DO_REQUEST_METHOD_ID, next),
				Trigger.createAbsolute(then));
	}

}
