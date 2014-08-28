/* $Id: PacerTest.java 312 2014-06-20 10:27:58Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/pacing/PacerTest.java $
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
package io.coala.experimental.grant;

import static org.junit.Assert.assertTrue;
import io.coala.agent.AgentStatusObserver;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.admin.CreatingCapability;
import io.coala.log.LogUtil;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;

/**
 * {@link PacerTest}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
@Ignore
public class PacerTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(PacerTest.class);

	/** */
	private static final String CONFIG_FILE = "pacer.properties";

	@Test
	public void testPacing() throws Exception
	{
		LOG.trace("Start pacing test");

		final Binder binder = BinderFactory.Builder.fromFile(CONFIG_FILE)
				.withModelName("testModel" + System.currentTimeMillis())
				.build().create("_unittest_");

		final CreatingCapability booterSvc = binder
				.inject(CreatingCapability.class);

		final String[] actorNames = { "actor1", "actor2", "actor3" };

		final CountDownLatch latch = new CountDownLatch(actorNames.length);

		for (String actorName : actorNames)
		{
			final String myName = actorName;
			booterSvc.createAgent(myName, PacedActor.class).subscribe(
					new AgentStatusObserver()
					{

						@Override
						public void onNext(final AgentStatusUpdate update)
						{
							LOG.trace("Observed: " + update);
							// System.err.println("Observed: " + update);

							if (update.getStatus().isFailedStatus()
									|| update.getStatus().isFinishedStatus())
								latch.countDown();
						}

						@Override
						public void onError(final Throwable e)
						{
							e.printStackTrace();
						}

						@Override
						public void onCompleted()
						{
							System.err.println(myName + " has COMPLETED");
						}
					});
		}

		latch.await(5, TimeUnit.SECONDS);
		assertTrue("Agent(s) did not all finish or fail", latch.getCount() == 0);
		LOG.trace("Pacing test done!");
	}
}
