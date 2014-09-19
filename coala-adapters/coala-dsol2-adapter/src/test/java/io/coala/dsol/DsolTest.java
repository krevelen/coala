/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/io/coala/dsol/DsolTest.java $
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
package io.coala.dsol;

import static org.junit.Assert.assertTrue;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusObserver;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.admin.CreatingCapability;
import io.coala.capability.plan.ClockStatusUpdate;
import io.coala.capability.replicate.ReplicatingCapability;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.log.LogUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

import rx.Observer;

/**
 * {@link DsolTest}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
@Ignore
public class DsolTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DsolTest.class);

	@Test
	public void testSimulatorService() throws Exception
	{
		LOG.trace("Started DSOL sim svc test...");

		final Binder binder = BinderFactory.Builder
				.fromFile()
				.withProperty(ReplicationConfig.class,
						ReplicationConfig.MODEL_NAME_KEY,
						"testModel" + System.currentTimeMillis()).build()
				.create("_unittest_");

		final CreatingCapability booterSvc = binder
				.inject(CreatingCapability.class);

		((DsolSimulatorService) binder.inject(ReplicatingCapability.class))
				.getStatusUpdates().subscribe(new Observer<ClockStatusUpdate>()
				{

					@Override
					public void onCompleted()
					{
						LOG.trace("DsolSimulatorEventType COMPLETED");
					}

					@Override
					public void onError(final Throwable e)
					{
						e.printStackTrace();
					}

					@Override
					public void onNext(final ClockStatusUpdate simEvent)
					{
						LOG.trace("Dsol Simulator now " + simEvent);
					}
				});

		final String[] actorNames = { "actor1", "actor2", "actor3" };

		final CountDownLatch latch = new CountDownLatch(actorNames.length);

		final Set<AgentID> failed = new HashSet<>();
		for (String actorName : actorNames)
		{
			final String myName = actorName;
			booterSvc.createAgent(myName, DsolTestAgent.class).subscribe(
					new AgentStatusObserver()
					{

						@Override
						public void onNext(final AgentStatusUpdate update)
						{
							LOG.trace(myName + " observed: " + update);
							// System.err.println("Observed: " + update);

							if (update.getStatus().isFailedStatus())
							{
								failed.add(update.getAgentID());
								latch.countDown();
								LOG.trace("Agent failed, remaining: "
										+ latch.getCount());
							} else if (update.getStatus().isFinishedStatus())
							{
								latch.countDown();
								LOG.trace("Agent finished, remaining: "
										+ latch.getCount());
							}
						}

						@Override
						public void onError(final Throwable e)
						{
							LOG.error("Problem while observing agent status", e);
						}

						@Override
						public void onCompleted()
						{
							LOG.trace(myName + " status updates COMPLETED");
						}
					});
		}

		latch.await();// 5, TimeUnit.SECONDS);
		assertTrue("Agent(s) failed: " + failed, failed.isEmpty());
		assertTrue("Agent(s) still waiting to finish", latch.getCount() == 0);
		LOG.trace("DSOL sim svc test done!");
	}
}
