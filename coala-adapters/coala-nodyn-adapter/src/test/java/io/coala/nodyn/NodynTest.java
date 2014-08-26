/* $Id: NodynTest.java 362 2014-08-14 03:46:34Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-nodyn-adapter/src/test/java/io/coala/nodyn/NodynTest.java $
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/inertia/
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
package io.coala.nodyn;

import io.coala.log.LogUtil;
import io.coala.resource.ResourceStreamer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

import rx.Observer;

/**
 * {@link NodynTest} tests the Nodyn framework. Node.js-in-JVM alternatives
 * include avatar-js
 * 
 * @version $Revision: 362 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class NodynTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(NodynTest.class);

	@Test
	public void testEveJS() throws Exception
	{
		LOG.trace("Starting Nodyn test");

		final ResourceStreamer script = ResourceStreamer
				.fromPath("src/test/evejs/start.js");

		final CountDownLatch latch = new CountDownLatch(2);
		NodynRunner.eval(script).subscribe(new Observer<Object>()
		{
			@Override
			public void onCompleted()
			{
				LOG.trace("Eval() completed");
				latch.countDown();
			}

			@Override
			public void onError(final Throwable e)
			{
				LOG.error("Problem with eval()", e);
				latch.countDown();
			}

			@Override
			public void onNext(final Object t)
			{
				LOG.trace("Evaluated to "
						+ (t == null ? "<null>" : t.getClass().getName() + ": "
								+ t));
				//NodynRunner.eval("console.log(agentManager.eve.agents);");
			}
		});
		
		
		latch.await(10, TimeUnit.SECONDS);

		LOG.trace("Completed Nodyn test");
	}
}
