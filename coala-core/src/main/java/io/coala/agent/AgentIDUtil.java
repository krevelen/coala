/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/agent/AgentIDUtil.java $
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
package io.coala.agent;

import io.coala.model.ModelComponentIDFactory;
import io.coala.util.Util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * {@link AgentIDUtil}
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class AgentIDUtil implements Util
{

	/**
	 * {@link AgentIDUtil} constructor hidden for utility classes
	 */
	private AgentIDUtil()
	{
		// empty
	}

	/**
	 * Utility method
	 * 
	 * @param agentIDFactory
	 * @param agentNames
	 * @return
	 */
	public static AgentID toAgentID(final ModelComponentIDFactory agentIDFactory,
			final String agentName)
	{
		return agentName == null ? null : agentIDFactory.createAgentID(agentName);
	}

	/**
	 * Utility method
	 * 
	 * @param agentIDFactory
	 * @param agentNames
	 * @return
	 */
	public static List<AgentID> toAgentIDs(final ModelComponentIDFactory agentIDFactory,
			final String... agentNames)
	{
		final List<AgentID> result = new ArrayList<AgentID>();
		if (agentNames != null && agentNames.length > 0)
			for (String agentName : agentNames)
				result.add(toAgentID(agentIDFactory, agentName));
		return result;
	}

	/**
	 * Utility method
	 * 
	 * @param agentIDFactory
	 * @param bootAgentNames
	 * @return
	 */
	public static <T> Map<AgentID, T> toAgentIDs(
			final ModelComponentIDFactory agentIDFactory, final Map<String, T> map)
	{
		final Map<AgentID, T> result = new HashMap<AgentID, T>();
		if (map != null && !map.isEmpty())
			for (Entry<String, T> entry : map.entrySet())
				result.put(agentIDFactory.createAgentID(entry.getKey()),
						entry.getValue());
		return result;
	}

}
