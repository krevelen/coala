/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/GuiceUtil.java $
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
package io.coala.guice;

import io.coala.agent.AgentID;
import io.coala.log.LogUtil;
import io.coala.util.Util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * {@link GuiceUtil}
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class GuiceUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(GuiceUtil.class);

	/** the singleton instance */
	private static final Map<AgentID, GuiceUtil> INSTANCES = Collections
			.synchronizedMap(new HashMap<AgentID, GuiceUtil>());

	/** @return the singleton {@link GuiceUtil} instance */
	public synchronized static GuiceUtil getInstance(final AgentID clientID)
	{
		if (!INSTANCES.containsKey(clientID))
			LOG.error("No injector set yet for host: " + clientID);

		return INSTANCES.get(clientID);
	}

	/** @return the singleton {@link GuiceUtil} instance */
	public synchronized static GuiceUtil getInstance(final AgentID clientID,
			final Module... modules)
	{
		return getInstance(clientID,
				modules == null ? null : Arrays.asList(modules));
	}

	/** @return the singleton {@link GuiceUtil} instance */
	public synchronized static GuiceUtil getInstance(final AgentID clientID,
			final Collection<Module> modules)
	{
		if (!INSTANCES.containsKey(clientID))
			INSTANCES.put(clientID,
					new GuiceUtil(clientID, Guice.createInjector(modules)));
		else if (!modules.isEmpty())
			LOG.warn("Ignoring new modules, injector already configured");

		return INSTANCES.get(clientID);
	}

	/** */
	private final AgentID clientID;

	/** */
	private final Injector injector;

	/** {@link GuiceUtil} constructor */
	private GuiceUtil(final AgentID clientID, final Injector injector)
	{
		this.clientID = clientID;
		this.injector = injector;
	}

	public AgentID getClientID()
	{
		return this.clientID;
	}

	public Injector getInjector()
	{
		return this.injector;
	}
}
