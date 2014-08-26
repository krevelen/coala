/* $Id: TestAgent.java 312 2014-06-20 10:27:58Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/aglobe-util/src/test/java/com/almende/coala/aglobe/TestAgent.java $
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
package io.coala.aglobe;

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
							handleIncommingMyMessage(msg);
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

		if (getID().equals(AGlobeWrapperAgentTest.senderAgentID))
		{
			try
			{
				ping();
			} catch (Exception e)
			{
				LOG.error("No ping today!", e);
			}
		}
	}

	protected SimTime getTick(int t)
	{
		return getBinder().inject(SimTimeFactory.class).create(t,
				TimeUnit.TICKS);
	}

	public void ping() throws Exception
	{
		final MyMessage msg = new MyMessage(getTick(0), getID(),
				AGlobeWrapperAgentTest.receiverAgentID, "ping");

		LOG.info("Going to ping: " + msg);
		getBinder().inject(SendingCapability.class).send(msg);
	}

	public void pong() throws Exception
	{
		final MyMessage msg = new MyMessage(getTick(1), getID(),
				AGlobeWrapperAgentTest.senderAgentID, "pong");

		LOG.info("Going to pong: " + msg);
		getBinder().inject(SendingCapability.class).send(msg);
	}

	/**
	 * @param message
	 * @throws Exception 
	 */
	public void handleIncommingMyMessage(final MyMessage message) throws Exception
	{
		LOG.info(getID().getValue() + " received: " + message.content);
		if (getID().equals(AGlobeWrapperAgentTest.receiverAgentID))
		{
			pong();
		}
		// agent execution is complete after receiving one message
		setStatus(BasicAgentStatus.COMPLETE);
	}

}
