/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/test/java/com/almende/coala/eve/TestAgent.java $
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
package io.coala.eve;

import io.coala.agent.BasicAgent;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.Binder;
import io.coala.capability.interact.ReceivingCapability;
import io.coala.capability.interact.SendingCapability;
import io.coala.log.InjectLogger;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observer;

/**
 * {@link TestAgent}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class TestAgent extends BasicAgent
{

	/** */
	private static final long serialVersionUID = 1L;

	@InjectLogger
	private Logger LOG;

	/**
	 * {@link TestAgent} constructor
	 * 
	 * @param binder
	 */
	@Inject
	public TestAgent(final Binder binder)
	{
		super(binder);
	}

	/** @see BasicAgent#initialize() */
	@Override
	public void initialize()
	{
		// subscribe message handler
		getBinder().inject(ReceivingCapability.class).getIncoming()
				.ofType(MyMessage.class).subscribe(new Observer<MyMessage>()
				{
					@Override
					public void onNext(final MyMessage msg)
					{
						try
						{
							handle(msg);
						} catch (Exception e)
						{
							onError(e);
						}
					}

					@Override
					public void onCompleted()
					{

					}

					@Override
					public void onError(final Throwable t)
					{
						t.printStackTrace();
					}
				});

		if (getID().equals(EveWrapperAgentMessagingTest.senderAgentID))
		{
			try
			{
				LOG.info("About to ping...");
				ping();
			} catch (Exception e)
			{
				LOG.error("No ping today!", e);
			}
		}
	}

	/**
	 * @param t
	 * @return
	 */
	protected SimTime createTick(int t)
	{
		return getBinder().inject(SimTimeFactory.class).create(t,
				TimeUnit.TICKS);
	}

	/**
	 * @throws Exception
	 */
	public void ping() throws Exception
	{
		final MyMessage msg = new MyMessage(createTick(0), getID(),
				EveWrapperAgentMessagingTest.receiverAgentID, "ping");
		LOG.trace("About to send ping: " + msg);
		getBinder().inject(SendingCapability.class).send(msg);
	}

	/**
	 * @throws Exception
	 */
	public void pong() throws Exception
	{
		final MyMessage msg = new MyMessage(createTick(1), getID(),
				EveWrapperAgentMessagingTest.senderAgentID, "pong");

		LOG.trace("About to send pong: " + msg);
		getBinder().inject(SendingCapability.class).send(msg);
	}

	@Override
	public void finish()
	{
		LOG.trace(getID() + " is done");
	}

	/**
	 * @param message
	 * @throws Exception
	 */
	public void handle(final MyMessage message) throws Exception
	{
		LOG.trace(getID().getValue() + " handling " + message.content + "...");
		if (getID().equals(EveWrapperAgentMessagingTest.receiverAgentID))
		{
			pong();
			setStatus(BasicAgentStatus.COMPLETE);
		} else if (getID().equals(EveWrapperAgentMessagingTest.senderAgentID))
		{
			setStatus(BasicAgentStatus.COMPLETE);
		}
	}

}
