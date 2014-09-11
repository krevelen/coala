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
import io.coala.factory.Factory;
import io.coala.model.ModelComponentIDFactory;
import io.coala.model.ModelID;
import io.coala.time.ClockID;
import io.coala.time.TimeUnit;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.joda.time.Period;

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
	private final ModelID modelID;

	/** */
	private final ClockID clockID;

	/** */
	private final TimeUnit baseTimeUnit;

	/** */
	private final Date clockOffset;

	/** */
	private final Period clockDuration;

	/** */
	private final ModelComponentIDFactory agentIDFactory;

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
			final ModelComponentIDFactory idFactory,
			final String clockName,
			final TimeUnit baseTimeUnit,
			final Date clockOffset,
			final Period clockDuration,
			final Class<? extends Agent> defaultAgentType,
			final Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes,
			final Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes,
			final Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes,
			final Map<String, Class<? extends Agent>> customAgentTypes,
			final String... bootAgentNames)
	{
		this(
				binderFactoryType,
				idFactory.getModelID(),
				// generate clockID for this model
				idFactory.createClockID(clockName), baseTimeUnit, clockOffset,
				clockDuration, idFactory, defaultAgentType,
				singletonServiceTypes, instantServiceTypes, customFactoryTypes,
				// convert strings to AgentIDs...
				AgentIDUtil.toAgentIDs(idFactory, customAgentTypes),
				AgentIDUtil.toAgentIDs(idFactory, bootAgentNames));
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
			final ModelID modelID,
			final ClockID clockID,
			final TimeUnit baseTimeUnit,
			final Date clockOffset,
			final Period clockDuration,
			final ModelComponentIDFactory agentIDFactory,
			final Class<? extends Agent> defaultAgentType,
			final Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes,
			final Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes,
			final Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes,
			final Map<AgentID, Class<? extends Agent>> customAgentTypes,
			final List<AgentID> bootAgentNames)
	{
		this.binderFactoryType = binderFactoryType;
		this.modelID = modelID;
		this.clockID = clockID;
		this.baseTimeUnit = baseTimeUnit;
		this.clockOffset = clockOffset;
		this.clockDuration = clockDuration;
		this.agentIDFactory = agentIDFactory;
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

	/** @see BinderFactoryConfig#getAgentIDFactory() */
	@Override
	public ModelComponentIDFactory getAgentIDFactory()
	{
		return this.agentIDFactory;
	}

	/** @see BinderFactoryConfig#getModelID() */
	@Override
	public ModelID getModelID()
	{
		return this.modelID;
	}

	/** @see BinderFactoryConfig#getClockID() */
	@Override
	public ClockID getClockID()
	{
		return this.clockID;
	}

	/** @see BinderFactoryConfig#getBaseTimeUnit() */
	@Override
	public TimeUnit getBaseTimeUnit()
	{
		return this.baseTimeUnit;
	}

	/** @see BinderFactoryConfig#getClockOffset() */
	@Override
	public Date getClockOffset()
	{
		return this.clockOffset;
	}

	/** @see BinderFactoryConfig#getClockDuration() */
	@Override
	public Period getClockDuration()
	{
		return this.clockDuration;
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

}