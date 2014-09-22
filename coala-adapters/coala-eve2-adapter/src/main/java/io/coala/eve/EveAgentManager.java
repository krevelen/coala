/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/main/java/com/almende/coala/eve/EveAgentManager.java $
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
package io.coala.eve;

import io.coala.agent.AbstractAgentManager;
import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * {@link EveAgentManager}
 * 
 * @date $Date: 2014-07-08 12:11:12 +0200 (Tue, 08 Jul 2014) $
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class EveAgentManager extends AbstractAgentManager
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(EveAgentManager.class);

	/** */
	private static EveAgentManager INSTANCE;

	/**
	 * @return the singleton {@link EveAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static EveAgentManager getInstance()
	{
		if (INSTANCE == null)
			return getInstance((String) null);

		return INSTANCE;
	}

	/**
	 * @param configPath or {@code null} for default config
	 * @return the singleton {@link EveAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static EveAgentManager getInstance(
			final String configPath)
	{
		if (INSTANCE == null)
			try
			{
				INSTANCE = getInstance(BinderFactory.Builder
						.fromFile(configPath));
			} catch (final CoalaException e)
			{
				throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime(e,
						"configPath", configPath);
			}

		return INSTANCE;
	}

	/**
	 * @param binder
	 * @return
	 */
	public synchronized static EveAgentManager getInstance(final Binder binder)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new EveAgentManager(binder);

			INSTANCE.bootAgents();
		}

		return INSTANCE;
	}

	/**
	 * @param binderFactoryBuilder or {@code null} for default config
	 * @return the singleton {@link EveAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static EveAgentManager getInstance(
			final BinderFactory.Builder binderFactoryBuilder)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new EveAgentManager(binderFactoryBuilder);

			INSTANCE.bootAgents();
		}

		return INSTANCE;
	}

	/**
	 * {@link EveAgentManager} constructor
	 * 
	 * @param binderFactoryBuilder
	 */
	protected EveAgentManager(final BinderFactory.Builder binderFactoryBuilder)
	{
		super(binderFactoryBuilder);
	}

	/**
	 * {@link EveAgentManager} constructor
	 * 
	 * @param binder
	 */
	protected EveAgentManager(final Binder binder)
	{
		super(binder);
	}

	@Override
	protected void updateWrapperAgentStatus(final AgentID agentID,
			final AgentStatus<?> status)
	{
		super.updateWrapperAgentStatus(agentID, status);
	}

	/**
	 * @param agent
	 * @return the {@link EveWrapperAgent} for the created agent
	 * @throws Exception
	 */
	@Override
	protected AgentID boot(final Agent agent) throws CoalaException
	{
		final AgentID agentID = agent.getID();
		final String eveAgentID = EveUtil.toEveAgentId(agentID);
		if (EveUtil.getEveHost().hasAgent(eveAgentID))
		{
			LOG.warn("Agent already wrapped by an Eve agent: " + agentID);
			return agentID;
		}
		try
		{
			EveUtil.getEveHost().createAgent(EveWrapperAgent.class, eveAgentID);
			return agentID;
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.AGENT_CREATION_FAILED
					.create(e, agentID);
		}
	}

	@Override
	protected void shutdown()
	{
		// FIXME destroy/cleanup eve host somehow
	}

	// TODO store wrapper agent address(es) in (distributed) hash map

	/** */
	private final Map<AgentID, List<String>> agentURLs = Collections
			.synchronizedMap(new HashMap<AgentID, List<String>>());

	protected void setExposed(final AgentID agentID, final Object exposed)
			throws Exception
	{
		final String agentId = EveUtil.toEveAgentId(agentID);
		if (!EveUtil.getEveHost().hasAgent(agentId))
			throw new IllegalStateException("No wrapper agent for: " + agentID);

		((EveExposingAgent) EveUtil.getEveHost().getAgent(agentId))
				.setExposed(exposed);
	}

	/**
	 * @param agentID
	 * @param eveURLs
	 */
	protected void setAddress(final String eveId, final List<String> eveURLs)
	{
		this.agentURLs.put(EveUtil.toAgentID(eveId), eveURLs);
	}

	/**
	 * @param agentID
	 * @return
	 */
	protected List<String> getAddress(final AgentID agentID)
	{
		return this.agentURLs.get(agentID);
	}

	/**
	 * @return
	 */
	protected Agent getAgent(final AgentID agentID, final boolean block)
	{
		return get(agentID, block);
	}
}
