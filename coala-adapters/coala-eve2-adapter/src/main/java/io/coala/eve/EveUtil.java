/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/main/java/com/almende/coala/eve/EveUtil.java $
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

import io.coala.agent.AgentID;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.message.Message;
import io.coala.resource.FileUtil;
import io.coala.util.Util;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.almende.eve.agent.AgentHost;
import com.almende.eve.config.Config;

/**
 * {@link EveUtil}
 * 
 * @date $Date: 2014-05-05 09:27:49 +0200 (Mon, 05 May 2014) $
 * @version $Revision: 248 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class EveUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(EveUtil.class);

	/** */
	private static final String HOST_CONFIG_PROPERTY = "eve.config";

	/** */
	private static final String HOST_CONFIG_DEFAULT = "eve.yaml";

	/** */
	private static boolean HOST_INITIALIZED = false;

	/**
	 * {@link EveUtil} singleton constructor
	 */
	private EveUtil()
	{
		// empty
	}

	/**
	 * @return
	 * @throws CoalaException
	 */
	protected static AgentHost getEveHost() throws CoalaException
	{
		if (!HOST_INITIALIZED)
		{
			final String path = System.getProperty(HOST_CONFIG_PROPERTY,
					HOST_CONFIG_DEFAULT);
			AgentHost.getInstance().loadConfig(
					new Config(FileUtil.getFileAsInputStream(path)));
			HOST_INITIALIZED = true;
		}
		return AgentHost.getInstance();
	}

	/** */
	private static final Map<String, AgentID> AGENT_ID_CACHE = new HashMap<>();

	/**
	 * @param id
	 * @return
	 */
	protected static AgentID toAgentID(final String id)
	{
		// FIXME create/employ global lookup service/agent

		final AgentID result = AGENT_ID_CACHE.get(id);
		if (result == null)
			LOG.warn("Unknown wrapped agentID for Eve agent Id: " + id);

		return AGENT_ID_CACHE.get(id);
	}

	/**
	 * @param agentID
	 * @return
	 */
	protected static String toEveAgentId(final AgentID agentID)
	{
		final String result = agentID.toString();
		// WebUtil.urlEncode(agentID.toString());
		AGENT_ID_CACHE.put(result, agentID);
		return result;
	}

	/**
	 * @param agentID
	 * @return
	 */
	protected static URI getAddress(final AgentID agentID)
	{
		// FIXME create/employ global lookup service/agent
		return URI.create("local:" + toEveAgentId(agentID));
	}

	/**
	 * @deprecated should use proxy somehow
	 * @param msg
	 * @throws Exception
	 */
	@Deprecated
	protected static <M extends Message<?>> void receiveMessageByPointer(
			final M msg)
	{
		try
		{
			((EveReceiverAgent) getEveHost().getAgent(
					toEveAgentId(msg.getReceiverID()))).doReceive(msg);
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(t,
					msg.getReceiverID());
		}
	}

	/**
	 * @param msg
	 * @throws Exception
	 */
	protected static <M extends Message<?>> void receiveMessageByProxy(
			final M msg)
	{
		try
		{
			// FIXME use when agent proxy+delete works:
			// getEveHost().getAgent(toEveAgentId(msg.getSenderID())),
			getEveHost().createAgentProxy(null,
					getAddress(msg.getReceiverID()), EveReceiverAgent.class)
					.doReceive(msg);
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(msg
					.getReceiverID());
		}
	}

	/**
	 * @deprecated please use {@link #receiveMessageByPointer(Message)}
	 * @param msg
	 * @throws Exception
	 */
	@Deprecated
	protected static <M extends Message<?>> void sendMessageByPointer(
			final M msg)
	{
		try
		{
			((EveSenderAgent) getEveHost().getAgent(
					toEveAgentId(msg.getSenderID()))).doSend(msg);
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(msg
					.getSenderID());
		}
	}

	/**
	 * @deprecated please use {@link #receiveMessageByPointer(Message)}
	 * @param msg
	 * @throws Exception
	 */
	@Deprecated
	protected static void sendMessageByProxy(final Message<?> msg)
	{
		try
		{
			getEveHost().createAgentProxy(null,
			// getEveHost().getAgent(toEveAgentId(msg.getSenderID())),
					getAddress(msg.getSenderID()), EveSenderAgent.class)
					.doSend(msg);
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(msg
					.getReceiverID());
		}
	}

}
