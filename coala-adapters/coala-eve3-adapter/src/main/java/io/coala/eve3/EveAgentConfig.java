/* $Id$
 * $URL$
 * 
 * Part of the EU project Inertia, see http://www.inertia-project.eu/
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
package io.coala.eve3;

import static org.aeonbits.owner.util.Collections.entry;
import static org.aeonbits.owner.util.Collections.map;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.exception.CoalaException;
import io.coala.json.JsonUtil;
import io.coala.resource.FileUtil;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Map;

import org.aeonbits.owner.Converter;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import com.almende.eve.agent.Agent;
import com.almende.eve.agent.AgentConfig;
import com.almende.eve.scheduling.Scheduler;
import com.almende.eve.scheduling.SimpleSchedulerBuilder;
import com.almende.eve.state.State;
import com.almende.eve.state.memory.MemoryStateBuilder;
import com.almende.eve.transport.Transport;
import com.almende.eve.transport.http.HttpTransportBuilder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * {@link EveAgentConfig}
 * 
 * @date $Date$
 * @version $Id$
 * @author <a href="mailto:rick@almende.org">Rick</a>
 */
public interface EveAgentConfig extends ReplicationConfig
{

	/** */
	String AGENT_ID_KEY = "master.agent.id";

	/** */
	String AGENT_ID_DEFAULT = "time-control";

	/** */
	String AGENT_CONFIG_FILE_KEY = "agent.config-uri";

	/** */
	String AGENT_CONFIG_FILE_DEFAULT = "eve-wrapper.yaml";

	/** */
	String AGENT_CLASS_KEY = "master.agent.class";

	/** */
	Class<?> AGENT_CLASS_DEFAULT = EveWrapperAgent.class;

	/** */
	String STATE_CLASS_KEY = "master.state.class";

	/** */
	Class<?> STATE_CLASS_DEFAULT = MemoryStateBuilder.class;

	/** */
	String SCHEDULER_CLASS_KEY = "master.scheduler.class";

	/** */
	Class<?> SCHEDULER_CLASS_DEFAULT = SimpleSchedulerBuilder.class;

	/** */
	String TRANSPORT_CLASS_KEY = "master.transport.class";

	/** */
	Class<?> TRANSPORT_CLASS_DEFAULT = HttpTransportBuilder.class;

	/** */
	String TRANSPORT_SERVLET_URL_KEY = "master.transport.servletUrl";

	/** */
	String TRANSPORT_SERVLET_URL_DEFAULT = "http://127.0.0.1:8080/agents/";

	/** */
	String TRANSPORT_AUTHENTICATE_KEY = "master.transport.doAuthentication";

	/** */
	boolean TRANSPORT_AUTHENTICATE_DEFAULT = false;

	/** */
	String STATE_CONFIG_KEY = "master.state";

	/** */
	String SCHEDULER_CONFIG_KEY = "master.scheduler";

	/** */
	String TRANSPORT_CONFIG_KEY = "master.transport";

	/**
	 * Maps default values that are not String constants, e.g. class names
	 */
	@SuppressWarnings("unchecked")
	Map<String, String> DEFAULT_VALUES = map(
			entry(AGENT_CLASS_KEY, AGENT_CLASS_DEFAULT.getName()),
			entry(STATE_CLASS_KEY, STATE_CLASS_DEFAULT.getName()),
			entry(SCHEDULER_CLASS_KEY, SCHEDULER_CLASS_DEFAULT.getName()),
			entry(TRANSPORT_CLASS_KEY, TRANSPORT_CLASS_DEFAULT.getName()));

	@Key(AGENT_ID_KEY)
	@DefaultValue(AGENT_ID_DEFAULT)
	String agentName();

	@Key(AGENT_CONFIG_FILE_KEY)
	@DefaultValue(AGENT_CONFIG_FILE_DEFAULT)
	String agentConfigUri();

	@DefaultValue("${" + AGENT_CONFIG_FILE_KEY + "}")
	@ConverterClass(InputStreamConverter.class)
	InputStream agentConfigStream();

	@Key(AGENT_CLASS_KEY)
	Class<? extends Agent> agentClass();

	@Key(SCHEDULER_CLASS_KEY)
	Class<? extends Scheduler> schedulerClass();

	@Key(SCHEDULER_CONFIG_KEY)
	@DefaultValue("{\"class\":\"${" + SCHEDULER_CLASS_KEY + "}\"}")
	JsonNode schedulerConfig();

	@Key(STATE_CLASS_KEY)
	Class<? extends State> stateClass();

	@Key(STATE_CONFIG_KEY)
	@DefaultValue("{\"class\":\"${" + STATE_CLASS_KEY + "}\"}")
	JsonNode stateConfig();

	@Key(TRANSPORT_CLASS_KEY)
	Class<? extends Transport> transportClass();

	@Key(TRANSPORT_SERVLET_URL_KEY)
	@DefaultValue(TRANSPORT_SERVLET_URL_DEFAULT)
	String transportServletUrl();

	@Key(TRANSPORT_AUTHENTICATE_KEY)
	@DefaultValue("" + TRANSPORT_AUTHENTICATE_DEFAULT)
	boolean transportAuthenticate();

	@Key(TRANSPORT_CONFIG_KEY)
	@DefaultValue("{\"class\":\"${" + TRANSPORT_CLASS_KEY
			+ "}\",\"servletUrl\":\"${"
			+ TRANSPORT_SERVLET_URL_KEY
			+ "}\",\"doAuthentication\":${"
			+ TRANSPORT_AUTHENTICATE_KEY
			+ "},\"doShortcut\":true,"
			+ "\"servletLauncher\":\"JettyLauncher\","
			// \"initParams\":[{\"key\":\"servletUrl\",\"value\":\"${" +
			// TRANSPORT_SERVLET_URL_KEY + "}\"}],"
			+ "\"servletClass\":\"com.almende.eve.transport.http.DebugServlet\"}")
	@ConverterClass(JsonNodeConverter.class)
	JsonNode transportConfig();

	@ConverterClass(AgentConfigConverter.class)
	@DefaultValue("{\"id\":\"${" + AGENT_ID_KEY + "}\",\"class\":\"${"
			+ AGENT_CLASS_KEY + "}\",\"transport\":${" + TRANSPORT_CONFIG_KEY
			+ "},\"state\":${" + STATE_CONFIG_KEY + "},\"scheduler\":${"
			+ SCHEDULER_CONFIG_KEY + "}}")
	AgentConfig agentConfig();

	/**
	 * {@link JsonNodeConverter}
	 *
	 * @date $Date$
	 * @version $Id$
	 * @author <a href="mailto:rick@almende.org">Rick</a>
	 */
	public class JsonNodeConverter implements Converter<JsonNode>
	{
		@Override
		public JsonNode convert(final Method method, final String input)
		{
			return JsonUtil.fromJSON(input);
		}
	}

	/**
	 * {@link AgentConfigConverter}
	 *
	 * @date $Date$
	 * @version $Id$
	 * @author <a href="mailto:rick@almende.org">Rick</a>
	 */
	public class AgentConfigConverter implements Converter<AgentConfig>
	{
		@Override
		public AgentConfig convert(final Method method, final String input)
		{
			return new AgentConfig((ObjectNode) JsonUtil.fromJSON(input));
		}
	}

	/**
	 * {@link AgentConfigConverter}
	 * 
	 * @date $Date$
	 * @version $Id$
	 * @author <a href="mailto:rick@almende.org">Rick</a>
	 */
	public class InputStreamConverter implements Converter<InputStream>
	{
		/** */
		private static final Logger LOG = LogManager
				.getLogger(EveAgentConfig.InputStreamConverter.class);

		@Override
		public InputStream convert(final Method method, final String input)
		{
			try
			{
				return FileUtil.getFileAsInputStream(input);
			} catch (final CoalaException e)
			{
				LOG.warn(e.getMessage());
				return null;
			}
		}
	}

}
