/* $Id$
 * $URL: https://dev.almende.com/svn/abms/jade-util/src/test/java/io/coala/jade/JadeTest.java $
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
package io.coala.jade;

import io.coala.log.LogUtil;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import jade.wrapper.StaleProxyException;

import java.util.Arrays;

import org.apache.log4j.Logger;

/**
 * {@link JadeTest}
 * 
 * @version $Revision: 298 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class JadeTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(JadeTest.class);

	/**
	 * from: Luis Lezcano Airaldi <luislezcair@gmail.com> to:
	 * jade-develop@avalon.tilab.com date: Sat, Jun 7, 2014 at 3:10 PM
	 * 
	 * Hello! It's very easy to use Jade as a library. Here's an example from a
	 * small application I made:
	 */
	public void testJade1()
	{
		// This is the important method. This launches the jade platform.
		final Runtime rt = Runtime.instance();

		final Profile profile = new ProfileImpl();

		// With the Profile you can set some options for the container
		profile.setParameter(Profile.PLATFORM_ID, "Platform Name");
		profile.setParameter(Profile.CONTAINER_NAME, "Container Name");

		// Create the Main Container
		final AgentContainer mainContainer = rt.createMainContainer(profile);
		final String agentName = "manager";
		final String agentType = "ia.main.AgentManager"; // FIXME
		final Object[] agentArgs = {};

		try
		{
			// Here I create an agent in the main container and start it.
			final AgentController ac = mainContainer.createNewAgent(agentName,
					agentType, agentArgs);
			ac.start();
		} catch (final StaleProxyException e)
		{
			LOG.error(String.format("Problem creating/starting Jade agent '%s'"
					+ " of type: %s with args: %s", agentName, agentType,
					Arrays.asList(agentArgs)), e);
		}
	}
}
