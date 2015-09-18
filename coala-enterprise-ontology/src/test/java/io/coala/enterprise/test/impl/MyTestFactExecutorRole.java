/* $Id: 1929d3174b5b648673b49b19c945bdab7cc915fa $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/impl/MyTestFactExecutorRole.java $
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

import io.coala.bind.Binder;
import io.coala.enterprise.role.AbstractExecutor;
import io.coala.enterprise.test.TestFact;
import io.coala.invoke.ProcedureCall;
import io.coala.invoke.Schedulable;
import io.coala.log.InjectLogger;
import io.coala.time.SimTime;
import io.coala.time.TimeUnit;
import io.coala.time.Trigger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link MyTestFactInitiatorRole}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class MyTestFactExecutorRole extends AbstractExecutor<TestFact.Request>
		implements TestFact.Executor
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link MyTestFactExecutorRole} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected MyTestFactExecutorRole(final Binder binder)
	{
		super(binder);
	}

	@Override
	public void onRequested(final TestFact.Request request)
	{
		LOG.trace("Owner type: " + getOwnerType().getName());
		final SimTime then = getTime().plus(1, TimeUnit.HOURS);
		LOG.trace("Sending response @ " + then + " for request " + request);
		getSimulator()
				.schedule(
						ProcedureCall.create(this, this, DO_RESPONSE_METHOD_ID,
								request), Trigger.createAbsolute(then));
	}

	private static final String DO_RESPONSE_METHOD_ID = "doResponse/1";

	@Schedulable(DO_RESPONSE_METHOD_ID)
	protected void doResponse(final TestFact.Request request) throws Exception
	{
		send(TestFact.Response.Builder.forProducer(this, request).build());
	}
}