/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/bind/BasicBinderFactoryConfig.java $
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
package io.coala.bind;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentIDUtil;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.factory.Factory;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@link BasicBinderFactoryConfig}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@SuppressWarnings({ "rawtypes", "serial" })
public class BasicBinderFactoryConfig implements BinderFactoryConfig
{

	/** */
	private final Class<? extends BinderFactory> binderFactoryType;

	/** */
	private final ReplicationConfig replicationConfig;

	/** */
	private final Class<? extends Agent> defaultAgentType;

	/** */
	private final Map<AgentID, Class<? extends Agent>> customAgentTypes;

	/** */
	private final List<AgentID> bootAgentNames;

	/** */
	private final Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes;

	/** */
	private final Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes;

	/** */
	private final Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes;

	/**
	 * {@link BasicBinderFactoryConfig} constructor
	 * 
	 * @param binderFactoryType
	 * @param idFactory
	 * @param clockName
	 * @param baseTimeUnit
	 * @param clockOffset
	 * @param clockDuration
	 * @param defaultAgentType
	 * @param singletonServiceTypes
	 * @param instantServiceTypes
	 * @param customAgentTypes
	 * @param bootAgentNames
	 */
	protected BasicBinderFactoryConfig(
			final Class<? extends BinderFactory> binderFactoryType,
			final ReplicationConfig replicationConfig,
			final Class<? extends Agent> defaultAgentType,
			final Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes,
			final Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes,
			final Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes,
			final Map<String, Class<? extends Agent>> customAgentTypes,
			final String... bootAgentNames)
	{
		this(binderFactoryType, replicationConfig, defaultAgentType,
				singletonServiceTypes, instantServiceTypes, customFactoryTypes,
				// convert strings to AgentIDs...
				AgentIDUtil.toAgentIDs(replicationConfig.newID(),
						customAgentTypes), AgentIDUtil.toAgentIDs(
						replicationConfig.newID(), bootAgentNames));
	}

	/**
	 * {@link BasicBinderFactoryConfig} constructor
	 * 
	 * @param binderFactoryType
	 * @param modelID
	 * @param clockID
	 * @param baseTimeUnit
	 * @param clockOffset
	 * @param clockDuration
	 * @param agentIDFactory
	 * @param defaultAgentType
	 * @param customAgentTypes
	 * @param bootAgentNames
	 */
	protected BasicBinderFactoryConfig(
			final Class<? extends BinderFactory> binderFactoryType,
			final ReplicationConfig replicationConfig,
			final Class<? extends Agent> defaultAgentType,
			final Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes,
			final Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes,
			final Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes,
			final Map<AgentID, Class<? extends Agent>> customAgentTypes,
			final List<AgentID> bootAgentNames)
	{
		this.binderFactoryType = binderFactoryType;
		this.replicationConfig = replicationConfig;
		this.defaultAgentType = defaultAgentType;

		if (singletonServiceTypes == null || singletonServiceTypes.isEmpty())
			this.singletonServiceTypes = Collections.emptyMap();
		else
			this.singletonServiceTypes = Collections
					.unmodifiableMap(singletonServiceTypes);

		if (instantServiceTypes == null || instantServiceTypes.isEmpty())
			this.instantServiceTypes = Collections.emptyMap();
		else
			this.instantServiceTypes = Collections
					.unmodifiableMap(instantServiceTypes);

		if (customFactoryTypes == null || customFactoryTypes.isEmpty())
			this.customFactoryTypes = Collections.emptyMap();
		else
			this.customFactoryTypes = Collections
					.unmodifiableMap(customFactoryTypes);

		if (customAgentTypes == null || customAgentTypes.isEmpty())
			this.customAgentTypes = Collections.emptyMap();
		else
			this.customAgentTypes = Collections
					.unmodifiableMap(customAgentTypes);

		if (bootAgentNames == null || bootAgentNames.isEmpty())
			this.bootAgentNames = Collections.emptyList();
		else
			this.bootAgentNames = Collections.unmodifiableList(bootAgentNames);
	}

	/** @see BinderFactoryConfig#getBinderFactoryType() */
	@Override
	public Class<? extends BinderFactory> getBinderFactoryType()
	{
		return this.binderFactoryType;
	}

	/** @see BinderFactoryConfig#getDefaultAgentType() */
	@Override
	public Class<? extends Agent> getDefaultAgentType()
	{
		return this.defaultAgentType;
	}

	/** @see BinderFactoryConfig#getCustomAgentTypes() */
	@Override
	public Map<AgentID, Class<? extends Agent>> getCustomAgentTypes()
	{
		return this.customAgentTypes;
	}

	/** @see BinderFactoryConfig#getBootAgentIDs() */
	@Override
	public List<AgentID> getBootAgentIDs()
	{
		return this.bootAgentNames;
	}

	/** @see BinderFactoryConfig#getSingletonServiceTypes() */
	@Override
	public Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> getSingletonServiceTypes()
	{
		return this.singletonServiceTypes;
	}

	/** @see BinderFactoryConfig#getInstantServiceTypes() */
	@Override
	public Map<Class<? extends Capability>, Class<? extends Capability>> getInstantServiceTypes()
	{
		return this.instantServiceTypes;
	}

	/** @see BinderFactoryConfig#getCustomFactoryTypes() */
	@Override
	public Map<Class<? extends Factory>, Class<? extends Factory>> getCustomFactoryTypes()
	{
		return this.customFactoryTypes;
	}

	/** @see io.coala.bind.BinderFactoryConfig#getReplicationConfig() */
	@Override
	public ReplicationConfig getReplicationConfig()
	{
		return this.replicationConfig;
	}

}