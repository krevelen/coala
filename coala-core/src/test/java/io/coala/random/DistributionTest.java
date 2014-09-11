/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/DistributionTest.java $
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
package io.coala.random;

import io.coala.log.LogUtil;
import io.coala.random.RandomNumberDistribution;
import io.coala.random.RandomNumberStream;
import io.coala.random.impl.RandomDistributionFactoryImpl;
import io.coala.random.impl.RandomNumberStreamFactoryWell19937c;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link DistributionTest}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DistributionTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DistributionTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting Distribution tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed Distribution tests!");
	}

	@Test
	public void testDist()
	{
		final RandomNumberStream rng = new RandomNumberStreamFactoryWell19937c()
				.create("rng", 0L);

		final RandomNumberDistribution<?> dist = new RandomDistributionFactoryImpl()
				.getUniformReal(rng, 1.1, 2.1);

		for (int i = 0; i < 100; i++)
			LOG.trace("Next draw " + i + ": " + dist.draw());
	}
}
