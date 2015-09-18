/* $Id: 0cf65ed5d413a5f1852f4f66607ba86e5cbc8128 $
 * $URL: https://dev.almende.com/svn/abms/jsa-util/src/test/java/test/TestSemanticAgent.java $
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
package test;

import io.coala.log.LogUtil;
import jade.Boot;
import jade.core.Agent;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.util.leap.Properties;
import jade.wrapper.AgentContainer;
import jade.wrapper.PlatformState;
import jade.wrapper.StaleProxyException;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

/**
 * {@link TestSemanticAgent}
 * 
 * @date $Date: 2014-04-18 16:38:34 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 235 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
@Ignore
public class TestSemanticAgent
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(TestSemanticAgent.class);

	/** */
	private static final String[] args = new String[] { "-gui", "-local-host",
			"localhost", "-local-port", "1111", "-container-name",
			"TestContainer", };

	/** */
	private static Profile profile = null;

	/** */
	private static AgentContainer container = null;

	@BeforeClass
	public static void setUp() throws Exception
	{
		// Create the Profile
		if (args.length > 0)
		{
			if (args[0].startsWith("-"))
			{
				// Settings specified as command line arguments
				final Properties pp = Boot.parseCmdLineArgs(args);
				if (pp != null)
				{
					profile = new ProfileImpl(pp);
				} else
				{
					// One of the "exit-immediately" options was specified!
					System.exit(0);
				}
			} else
			{
				// Settings specified in a property file
				profile = new ProfileImpl(args[0]);
			}
		} else
		{
			// Settings specified in the default property file
			profile = new ProfileImpl(Boot.DEFAULT_FILENAME);
		}

		// Start a new JADE runtime system
		Runtime.instance().setCloseVM(false);

		// Check whether this is the Main Container or a peripheral
		// container
	}

	private static AgentContainer getContainer() throws Exception
	{
		if (container != null)
			return container;

		if (profile.getBooleanProperty(Profile.MAIN, true))
		{
			LOG.trace("Creating main container...");
			container = Runtime.instance().createMainContainer(profile);
		} else
		{
			LOG.trace("Creating peripheral agent container...");
			container = Runtime.instance().createAgentContainer(profile);
		}

		if (container == null)
			throw new Exception("Unable to create new agent platform");

		LOG.trace("Initialized " + container.getContainerName());
		return container;
	}

	@AfterClass
	public static void tearDown()
	{
		try
		{
			final AgentContainer container = getContainer();
			while (container.getState().getCode() != PlatformState.cPLATFORM_STATE_KILLED)
			{
				try
				{
					final String name = container.getContainerName();
					LOG.trace("Waiting for " + name + " (state: "
							+ container.getState() + ") to die...");
					container.kill();
					Thread.sleep(100);
				} catch (final InterruptedException ignore)
				{
					//
				} catch (final StaleProxyException e)
				{
					LOG.warn("Container (state: " + container.getState()
							+ ") is stale!?");
					break;
				}
			}
			LOG.trace("Agent container has been killed");
		} catch (final Exception e)
		{
			LOG.error("Problem closing agent container", e);
		}
	}

	@Test
	public void agentTest() throws Exception
	{
		final AgentContainer container = getContainer();
		final String agName = "MyTestAgent";
		final MySemanticAgent agObject = new MySemanticAgent();
		LOG.trace("Adding new agent: " + agName + "...");
		container.acceptNewAgent(agName, agObject);

		while (agObject.getAgentState().getValue() != Agent.AP_DELETED)
		{
			LOG.trace("Waiting for " + agName + " (state: "
					+ agObject.getAgentState() + ") to be killed via GUI...");
			try
			{
				Thread.sleep(1000);
			} catch (final InterruptedException ignore)
			{
				//
			}
		}
		
		container.kill();
	}

}
