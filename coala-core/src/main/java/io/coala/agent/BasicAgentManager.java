/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/interpreter/InterpreterAgentManager.java $
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
package io.coala.agent;

import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;

/**
 * {@link BasicAgentManager}
 * 
 * @date $Date: 2014-08-12 12:56:22 +0200 (Tue, 12 Aug 2014) $
 * @version $Revision: 360 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class BasicAgentManager extends AbstractAgentManager
{

	/** */
//	private static final Logger LOG = LogUtil
//			.getLogger(InterfaceAgentManager.class);

	/** */
	private static BasicAgentManager INSTANCE;

	/**
	 * @return the singleton {@link BasicAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static BasicAgentManager getInstance()
	{
		if (INSTANCE == null)
			return getInstance((String) null);

		return INSTANCE;
	}

	/**
	 * @param configPath or {@code null} for default config
	 * @return the singleton {@link BasicAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static BasicAgentManager getInstance(
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
	public synchronized static BasicAgentManager getInstance(
			final Binder binder)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new BasicAgentManager(binder);

			INSTANCE.bootAgents();
		}

		return INSTANCE;
	}

	/**
	 * @param binderFactoryBuilder or {@code null} for default config
	 * @return the singleton {@link BasicAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static BasicAgentManager getInstance(
			final BinderFactory.Builder binderFactoryBuilder)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new BasicAgentManager(binderFactoryBuilder);

			INSTANCE.bootAgents();
		}

		return INSTANCE;
	}

	/**
	 * {@link BasicAgentManager} constructor
	 * 
	 * @param binderFactoryBuilder
	 */
	protected BasicAgentManager(
			final BinderFactory.Builder binderFactoryBuilder)
	{
		super(binderFactoryBuilder);
	}

	/**
	 * {@link BasicAgentManager} constructor
	 * 
	 * @param binder
	 */
	protected BasicAgentManager(final Binder binder)
	{
		super(binder);
	}

	/**
	 * @param agent
	 * @return the {@link EveWrapperAgent} for the created agent
	 * @throws Exception
	 */
	@Override
	protected AgentID boot(final Agent agent) throws CoalaException
	{
		// LOG.warn("Oops !", new IllegalStateException("NOT IMPLEMENTED"));
		return agent.getID();
	}

	/** @see AbstractAgentManager#shutdown() */
	@Override
	protected void shutdown()
	{
		// FIXME destroy/cleanup proxy host somehow
	}

}
