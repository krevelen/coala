/* $Id: GuiceBinderFactory.java 299 2014-06-11 11:21:10Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/GuiceBinderFactory.java $
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

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.BinderFactory;
import io.coala.bind.BinderFactoryConfig;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.functions.Func1;
import rx.subjects.BehaviorSubject;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * {@link GuiceBinderFactory}
 * 
 * @date $Date: 2014-06-11 13:21:10 +0200 (Wed, 11 Jun 2014) $
 * @version $Revision: 299 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class GuiceBinderFactory implements BinderFactory
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(GuiceBinderFactory.class);

	/** */
	private final Map<AgentID, GuiceBinder> binderCache = Collections
			.synchronizedMap(new HashMap<AgentID, GuiceBinder>());

	/** */
	private BinderFactoryConfig config = null;

	/** */
	private Subject<AgentStatusUpdate, AgentStatusUpdate> statusUpdates = PublishSubject
			.create();

	/**
	 * {@link GuiceBinderFactory} zero-argument constructor for instantiation by
	 * reflection
	 */
	public GuiceBinderFactory()
	{
		// empty
	}

	/** @see BinderFactory#initialize(BinderFactoryConfig, Observable) */
	@Override
	public BinderFactory initialize(final BinderFactoryConfig config,
			final Observable<AgentStatusUpdate> ownerStatus)
	{
		this.config = config;
		// System.err.println("Initialized binder factory for model: "
		// + config.getModelID());
		ownerStatus.subscribe(this.statusUpdates);
		return this;
	}

	/** @see BinderFactory#getConfig() */
	@Override
	public BinderFactoryConfig getConfig()
	{
		return this.config;
	}

	/** @see BinderFactory#create(AgentID) */
	@Override
	public synchronized GuiceBinder create(final String agentName)

	{
		return create(getConfig().getAgentIDFactory().createAgentID(agentName));
	}

	/** @see BinderFactory#create(AgentID) */
	@Override
	public synchronized GuiceBinder create(final String agentName,
			final Class<? extends Agent> agentType)
	{
		return create(getConfig().getAgentIDFactory().createAgentID(agentName),
				agentType);
	}

	/** @see BinderFactory#create(AgentID) */
	// @Override
	public synchronized GuiceBinder create(final AgentID agentID)
	{
		return create(agentID, null);
	}

	/** @see BinderFactory#create(AgentID, Class) */
	@Override
	public synchronized GuiceBinder create(final AgentID agentID,
			final Class<? extends Agent> agentType) // throws CoalaException
	{
		final GuiceBinder cached = this.binderCache.get(agentID);
		if (cached != null)
		{
			LOG.warn("UNEXPECTED: re-using binder previously created for agent: "
					+ agentID);
			return cached;
		}
		if (getConfig() == null)
		{
			throw CoalaExceptionFactory.VALUE_NOT_CONFIGURED.createRuntime(
					"config",
					"use BinderFactory#initialize(BinderFactoryConfig)");
		}

		final AgentStatusUpdate defaultValue = new AgentStatusUpdate()
		{

			@Override
			public AgentID getAgentID()
			{
				return agentID;
			}

			@Override
			public AgentStatus<?> getStatus()
			{
				return BasicAgentStatus.CREATED;
			}
		};
		final Subject<AgentStatusUpdate, AgentStatusUpdate> behaviorSubject = BehaviorSubject
				.create(defaultValue);

		this.statusUpdates.filter(new Func1<AgentStatusUpdate, Boolean>()
		{
			@Override
			public Boolean call(final AgentStatusUpdate update)
			{
				return update.getAgentID().equals(agentID);
			}
		}).subscribe(behaviorSubject);

		final GuiceBinder result = new GuiceBinder(getConfig(), agentID,
				agentType, behaviorSubject.asObservable());

		this.binderCache.put(agentID, result);
		LOG.trace("Cached new binder for agent: " + agentID);

		return result;
	}

	/** @see BinderFactory#remove(AgentID) */
	@Override
	public GuiceBinder remove(final AgentID agentID)
	{
		return this.binderCache.remove(agentID);
	}

}
