/* $Id$
 * $URL: https://dev.almende.com/svn/abms/aglobe-util/src/test/java/com/almende/coala/aglobe/MyContainerMonitor.java $
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

import io.coala.log.LogUtil;

import org.apache.log4j.Logger;

import aglobe.container.AgentContainer;
import aglobe.container.DuplicateNameException;
import aglobe.container.ElementaryEntity;
import aglobe.container.agent.Agent;
import aglobe.container.agent.AgentManager;
import aglobe.ontology.AgentInfo;
import aglobe.ontology.Libraries;
import aglobe.platform.ContainerMonitor;

/**
 * {@link MyContainerMonitor} helper class
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class MyContainerMonitor implements ContainerMonitor
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(MyContainerMonitor.class);

	/** */
	private final String containerName;

	/** */
	AgentContainer container = null;

	/** */
	private boolean removed = false;

	/** */
	public MyContainerMonitor(final String containerName)
	{
		this.containerName = containerName;
	}

	@Override
	public synchronized void containerStarted(final AgentContainer container)
	{
		if (!container.getContainerName().equals(this.containerName))
		{
			LOG.trace("Ignoring start of another container: "
					+ container.getContainerName());
			return;
		}

		this.container = container;
		notifyAll();
	}

	public static String entityStateToString(final int state)
	{
		// if ((state & ElementaryEntity.USE_IDLE_MASK) != 0)
		// return "idle";

		switch (state)
		{
		case ElementaryEntity.CLONING:
			return "cloning";
		case ElementaryEntity.DEAD:
			return "dead";
		case ElementaryEntity.DONE:
			return "done";
		case ElementaryEntity.EXCEPTION:
			return "exception";
		case ElementaryEntity.INIT:
			return "init";
		case ElementaryEntity.MIGRATING:
			return "migrating";
		case ElementaryEntity.RUNNING:
			return "running";
		}
		return "unknown";
	}

	/**
	 * @param simpleName
	 * @param aGENT_CLASS
	 * @throws Exception
	 * @throws DuplicateNameException
	 */
	public <T extends Agent> T createAgent(final String name,
			final Class<T> clazz) throws DuplicateNameException, Exception
	{
		waitUntilContainerStarted();

		final AgentManager mgr = this.container.getAgentManager();

		final AgentInfo ai = new AgentInfo();
		ai.setName(name);
		ai.setAutoRun(true);
		ai.setMainClass(clazz.getName());
		ai.setLibraries(new Libraries());

		LOG.trace("Creating agent " + name + "...");
		mgr.createAgent(ai);

		final T agent = clazz.cast(mgr.getAgentInstance(ai.getName()));

		boolean initiated = false;
		while (!initiated)
		{
			LOG.trace("Agent " + name + " state: "
					+ entityStateToString(agent.getState()));
			try
			{
				Thread.sleep(10);
			} catch (final InterruptedException ignore)
			{
				//
			}
			if (agent.getAddress().getName() != null
					&& agent.getState() == ElementaryEntity.RUNNING)
			{
				initiated = true;
			}

		}

		return agent;
	}

	@Override
	public synchronized void containerRemoved(final String containerName)
	{
		if (!containerName.equals(this.containerName))
		{
			LOG.trace("Ignoring removal of another container: " + containerName);
			return;
		}

		this.removed = true;
		notifyAll();
	}

	/** */
	public void waitUntilContainerStarted()
	{
		while (this.container == null)
		{
			LOG.trace("Waiting for container " + this.containerName + "...");
			try
			{
				synchronized (this)
				{
					wait();
				}
			} catch (final InterruptedException ignore)
			{
				//
			}
		}
	}

	/** */
	public void waitUntilRemoved()
	{
		while (!this.removed)
		{
			LOG.trace("Waiting for container to be removed");
			try
			{
				synchronized (this)
				{
					wait();
				}
			} catch (final InterruptedException ignore)
			{
				//
			}
		}
	}
}