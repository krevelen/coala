/* $Id$
 * $URL$
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
package io.coala.capability.online;

import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.log.LogUtil;

import java.net.URI;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

/**
 * {@link FluentHCOnlineCapabilityTest}
 * 
 * @version $Revision$
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class FluentHCOnlineCapabilityTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(FluentHCOnlineCapabilityTest.class);

	/** */
	private static final String CONFIG_FILE = "coala.properties";

	@Test
	public void testOnlineClient() throws Exception
	{
		LOG.trace("Started Apache HTTP Client capability test");

		final Binder binder = BinderFactory.Builder
				.fromFile(CONFIG_FILE)
				.withProperty(ReplicationConfig.class,
						ReplicationConfig.MODEL_NAME_KEY,
						"testModel" + System.currentTimeMillis()).build()
				.create("testBooter");

		final OnlineCapability web = binder.inject(OnlineCapability.class);

		final String uri = "http://www.google.com";
		LOG.trace(web.request(URI.create(uri)).first().toString());

		new CountDownLatch(0).await(10, TimeUnit.SECONDS);

		LOG.trace("Completed Apache HTTP Client capability test");
	}

}
