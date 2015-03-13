/* $Id: e72a53373ffafd1e2d2135349896a7825a3546d0 $
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
package io.coala.eve3;

import static org.aeonbits.owner.util.Collections.entry;
import static org.aeonbits.owner.util.Collections.map;
import io.coala.agent.AgentID;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.message.Message;
import io.coala.util.Util;
import io.coala.web.WebUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aeonbits.owner.ConfigFactory;
import org.apache.log4j.Logger;

import com.almende.eve.agent.Agent;
import com.almende.eve.agent.AgentBuilder;
import com.almende.eve.agent.AgentConfig;
import com.almende.eve.capabilities.Config;
import com.almende.eve.config.YamlReader;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

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

	/**
	 * {@link EveUtil} singleton constructor
	 */
	private EveUtil()
	{
		// empty
	}

	/** */
	private static final Map<String, AgentID> AGENT_ID_CACHE = new HashMap<>();

	/** */
	private static final Map<String, EveWrapperAgent> WRAPPER_AGENT_CACHE = new HashMap<>();

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
	public static String toEveAgentId(final AgentID agentID)
	{
		// be robust against spaces, weird characters, etc.
		final String result = WebUtil.urlEncode(agentID.toString());
		AGENT_ID_CACHE.put(result, agentID);
		return result;
	}

	/**
	 * @param agentID
	 * @return
	 */
	public static URI getAddress(final AgentID agentID)
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
			getWrapperAgent(toEveAgentId(msg.getReceiverID())).doReceive(msg);
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
	// protected static <M extends Message<?>> void receiveMessageByProxy(
	// final M msg)
	// {
	// try
	// {
	// // FIXME use when agent proxy+delete works:
	// // getEveHost().getAgent(toEveAgentId(msg.getSenderID())),
	// getEveHost().createAgentProxy(getAddress(msg.getReceiverID()),
	// EveReceiverAgent.class).doReceive(msg);
	// } catch (final Throwable t)
	// {
	// throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(msg
	// .getReceiverID());
	// }
	// }

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
			getWrapperAgent(toEveAgentId(msg.getSenderID())).doSend(msg);
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
	// @Deprecated
	// protected static void sendMessageByProxy(final Message<?> msg)
	// {
	// try
	// {
	// getEveHost().createAgentProxy(null,
	// // getEveHost().getAgent(toEveAgentId(msg.getSenderID())),
	// getAddress(msg.getSenderID()), EveSenderAgent.class)
	// .doSend(msg);
	// } catch (final Throwable t)
	// {
	// throw CoalaExceptionFactory.AGENT_UNAVAILABLE.createRuntime(msg
	// .getReceiverID());
	// }
	// }

	/**
	 * @param id
	 * @return
	 * @throws CoalaException
	 */
	public static List<URI> getAddresses(final AgentID id)
			throws CoalaException
	{
		try
		{
			final Agent agent = getWrapperAgent(toEveAgentId(id));
			if (agent != null)
				return agent.getUrls();
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE.create(id);
		}
		return Collections.emptyList();
	}

	/**
	 * @param eveAgentID
	 * @return
	 * @throws CoalaException
	 */
	protected static boolean hasWrapperAgent(final String eveAgentID)
			throws CoalaException
	{
		synchronized (WRAPPER_AGENT_CACHE)
		{
			return WRAPPER_AGENT_CACHE.containsKey(eveAgentID);
		}
	}

	/**
	 * @param eveAgentID
	 */
	protected static EveWrapperAgent createWrapperAgent(final String eveAgentID)
	{
		synchronized (WRAPPER_AGENT_CACHE)
		{
			EveWrapperAgent result = WRAPPER_AGENT_CACHE.get(eveAgentID);
			if (result == null)
			{
				result = valueOf(eveAgentID, EveWrapperAgent.class);
				WRAPPER_AGENT_CACHE.put(eveAgentID, result);
			}
			return result;
		}
	}

	/**
	 * @param eveAgentID
	 */
	protected static EveWrapperAgent getWrapperAgent(final String eveAgentID)
	{
		return createWrapperAgent(eveAgentID);
	}

	/** */
	@SuppressWarnings("unchecked")
	@SafeVarargs
	public static final <T extends Agent> T valueOf(
			final AgentConfig agentConfig, final Class<T> agentType,
			final Map.Entry<String, ? extends JsonNode>... parameters)
	{
		// checkRegistered(agentType);

		if (parameters != null && parameters.length != 0)
			for (Map.Entry<String, ? extends JsonNode> param : parameters)
				agentConfig.set(param.getKey(), param.getValue());

		final T result = (T) new AgentBuilder().with(agentConfig).build();
		LOG.trace("Created agent with config: " + agentConfig);
		return result;
	}

	/** */
	@SafeVarargs
	public static final <T extends Agent> T valueOf(final String id,
			final Class<T> agentType,
			final Map.Entry<String, ? extends JsonNode>... parameters)
	{
		@SuppressWarnings("unchecked")
		final EveAgentConfig cfg = ConfigFactory.create(
				EveAgentConfig.class,
				EveAgentConfig.DEFAULT_VALUES,
				map(entry(EveAgentConfig.AGENT_CLASS_KEY, agentType.getName()),
						entry(EveAgentConfig.AGENT_ID_KEY, id),
						entry(EveAgentConfig.AGENT_ID_KEY, id)));

		final InputStream is = cfg.agentConfigStream();
		if (is != null)
		{
			final Config config = YamlReader.load(is).expand();
			try
			{
				is.close();
			} catch (final IOException ignore)
			{
				// empty
			}

			for (final JsonNode agent : (ArrayNode) config.get("agents"))
			{
				final JsonNode idNode = agent.get("id");
				if (idNode != null && !idNode.asText().equals(id))
					continue;

				LOG.info("Creating agent " + id + " from config at "
						+ cfg.agentConfigUri());
				return valueOf(new AgentConfig((ObjectNode) agent), agentType,
						parameters);
			}
		}
		LOG.info("No config for agent " + id + " found at: "
				+ cfg.agentConfigUri() + ". Using default config");
		return valueOf(cfg.agentConfig(), agentType, parameters);
	}

}
