/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/ResourceTest.java $
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
package io.coala.resource;

import io.coala.log.LogUtil;

import java.io.File;
import java.net.URI;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link ResourceTest}
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ResourceTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(ResourceTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting Resource tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed Resource tests!");
	}

	@Test
	public void testResource() throws Exception
	{
		final ResourceStreamer r1 = ResourceStreamer.from("testcontent",
				"testcontent2");
		LOG.trace("r1: " + r1);

		final ResourceStreamer r2 = ResourceStreamer
				.from(Thread
						.currentThread()
						.getContextClassLoader()
						.getResource(
								getClass().getName().replace(".", "/")
										+ ".class"));
		LOG.trace("r2: " + r2);

		final ResourceStreamer r3 = ResourceStreamer.from(new File("pom.xml"));
		LOG.trace("r3: " + r3);

		final ResourceStreamer r4 = ResourceStreamer.from(
				URI.create("http://www.google.nl/"),
				URI.create("http://www.google.nl/"),
				URI.create("http://www.google.nl/"),
				URI.create("http://www.google.nl/"));
		LOG.trace("r4: " + r4);
	}
}
