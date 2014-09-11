/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/FederationModelSimulationTest.java $
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
 * Copyright (c) 2014 Almende B.V. 
 */
package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.ExperimentBuilder;
import io.coala.dsol.util.ReplicationBuilder;
import io.coala.log.LogUtil;
import io.coala.time.TimeUnit;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulator;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.junit.Ignore;
import org.junit.Test;

/**
 * {@link FederationModelSimulationTest}
 * 
 * @date $Date: 2014-06-04 15:54:19 +0200 (Wed, 04 Jun 2014) $
 * @version $Revision: 297 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
@Ignore
public class FederationModelSimulationTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(FederationModelSimulationTest.class);

	@Test
	public void runModel() throws Exception
	{
		// configure model
		final int fedSize = 10;
		final boolean remoteAlloc = true;
		final boolean broker = true;
		final FederationModel model = new FederationModel(fedSize, remoteAlloc,
				broker);

		// configure single replication treatment
		final DateTime now = new DateTime().withTimeAtStartOfDay();
		final Interval runInterval = new Interval(now, now.plusWeeks(2));
		final ReplicationBuilder repl = new ExperimentBuilder()
				.withSimulator(new DEVSSimulator()).withModel(model)
				.newTreatment().withTimeUnit(TimeUnit.DAYS)
				.withRunInterval(runInterval).newReplication("run1")
				.withStream(123).initialize();

		LOG.trace("Starting replication " + repl);
		repl.start();

		while (repl.getExperiment().getSimulator().isRunning())
		{
			try
			{
				Thread.sleep(1L);
			} catch (final InterruptedException ignore)
			{
			}
		}

		LOG.trace("Completed replication " + repl);
	}

}
