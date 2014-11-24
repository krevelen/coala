/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/bind/BinderFactoryConfig.java $
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
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.config.CoalaProperty;
import io.coala.config.CoalaPropertyMap;
import io.coala.config.ConfigUtil;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaRuntimeException;
import io.coala.factory.Factory;
import io.coala.log.LogUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigFactory;
import org.aeonbits.owner.Mutable;
import org.apache.log4j.Logger;

/**
 * {@link BinderFactoryConfig}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
@SuppressWarnings("rawtypes")
public interface BinderFactoryConfig extends Config
{

	/** @return the type of {@link BinderFactory} */
	Class<? extends BinderFactory> getBinderFactoryType();

	/**
	 * @return
	 */
	ReplicationConfig getReplicationConfig();

	/** @return the {@link ModelID} identifier */
	// ModelID getModelID();

	/** @return the {@link ModelComponentIDFactory} */
	// ModelComponentIDFactory getAgentIDFactory();

	/** @return the identifier for this particular JVM host's system clock */
	// ClockID getClockID();

	/** @return the simulation model's internal base time unit */
	// TimeUnit getBaseTimeUnit();

	/**
	 * @return the UTC offset {@link Date} for simulation time, or {@code null}
	 *         if simulation time is measured in {@link TimeUnit#TICKS}
	 */
	// Date getClockOffset();

	/**
	 * @return
	 */
	// Period getClockDuration();

	/**
	 * @return a {@link Map} of the agent's service types for lazy loading Key
	 *         is unimplemented interface and value is the implementation
	 * */
	Map<Class<? extends Factory>, Class<? extends Factory>> getCustomFactoryTypes();

	/**
	 * @return a {@link Map} of the agent's singleton service types for lazy
	 *         loading. Keys is the service factory interface, and value is the
	 *         implementation to be bound as singleton
	 * */
	Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> getSingletonServiceTypes();

	/**
	 * @return a {@link List} the simulation model's singleton service
	 *         instances. Key is the {@link Capability} interface, and value is
	 *         the concrete {@link Capability} type to be instantiated on each
	 *         call
	 * */
	Map<Class<? extends Capability>, Class<? extends Capability>> getInstantServiceTypes();

	/**
	 * @return the simulation model's default agent type, or {@code null} if
	 *         none
	 */
	Class<? extends Agent> getDefaultAgentType();

	/** @return the simulation model's custom agent types (if any) */
	Map<AgentID, Class<? extends Agent>> getCustomAgentTypes();

	/** @return a {@link List} of the agents to boot at system boot time */
	List<AgentID> getBootAgentIDs();

	/**
	 * {@link Builder}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 */
	public static class Builder
	{

		/** */
		private static final Logger LOG = LogUtil
				.getLogger(BinderFactoryConfig.Builder.class);

		/** */
		private Class<? extends BinderFactory> binderFactoryType;

		/** */
		private ReplicationConfig replicationConfig = null;

		/** */
		private Class<? extends Agent> defaultAgentType = null;

		/** */
		private Map<String, Class<? extends Agent>> customAgentTypes = new HashMap<String, Class<? extends Agent>>();

		/** */
		private String[] bootAgentNames = {};

		/** */
		private Map<Class<? extends CapabilityFactory>, Class<? extends Capability>> singletonServiceTypes = new HashMap<Class<? extends CapabilityFactory>, Class<? extends Capability>>();

		/** */
		private Map<Class<? extends Capability>, Class<? extends Capability>> instantServiceTypes = new HashMap<Class<? extends Capability>, Class<? extends Capability>>();

		/** */
		private Map<Class<? extends Factory>, Class<? extends Factory>> customFactoryTypes = new HashMap<Class<? extends Factory>, Class<? extends Factory>>();

		/**
		 * {@link Builder} constructor
		 */
		private Builder()
		{
			// factory should not produce protected/public instances
		}

		/**
		 * @return
		 * @throws CoalaException
		 */
		public static Builder fromFile() throws CoalaException
		{
			return fromFile(null);
		}

		/**
		 * @param configPath
		 * @return
		 * @throws CoalaException
		 */
		public static Builder fromFile(final String configPath)
				throws CoalaException
		{
			CoalaPropertyMap.getInstance(false).load(configPath);
			
			final Builder builder = new Builder();

			builder.withBinderFactoryType(CoalaProperty.binderFactoryType
					.value().getType(BinderFactory.class));

			if (configPath != null)
				ConfigFactory.setProperty(ConfigUtil.FILE_NAME_PROPERTY,
						configPath);
			final ReplicationConfig cfg = ConfigFactory
					.create(ReplicationConfig.class);
			LOG.trace("Loaded ReplicationConfig: " + cfg);

			builder.withReplicationConfig(cfg);

			try
			{
				builder.withDefaultAgentType(CoalaProperty.defaultAgentType
						.value().getType(Agent.class));
			} catch (final CoalaRuntimeException e)
			{
				// ok
			}

			// TODO add smart Map getter based on key list property
			final String[] customAgentNames = CoalaProperty.customAgentNames
					.value().getJSON(String[].class);

			if (customAgentNames != null && customAgentNames.length > 0)
				for (String agentName : customAgentNames)
					builder.withCustomAgentType(
							agentName,
							CoalaProperty.agentType.value(agentName).getType(
									Agent.class));

			builder.withBootAgentNames(CoalaProperty.bootAgentNames.value()
					.getJSON(new String[] {}));

			for (Entry<Class<? extends CapabilityFactory>, Class<? extends Capability>> entry : CoalaProperty.singletonServiceTypes
					.value()
					.getBindings(CapabilityFactory.class, Capability.class)
					.entrySet())
				builder.withSingletonServiceType(entry.getKey(),
						entry.getValue());

			for (Entry<Class<? extends Capability>, Class<? extends Capability>> entry : CoalaProperty.instantServiceTypes
					.value().getBindings(Capability.class, Capability.class)
					.entrySet())
				builder.withInstantServiceType(entry.getKey(), entry.getValue());

			for (Entry<Class<? extends Factory>, Class<? extends Factory>> entry : CoalaProperty.customFactoryTypes
					.value().getBindings(Factory.class, Factory.class)
					.entrySet())
				builder.withCustomFactoryType(entry.getKey(), entry.getValue());

			return builder;
		}

		/**
		 * reuse a config that was already built before the
		 * 
		 * @param config
		 * @return
		 */
		public static Builder fromConfig(final BinderFactoryConfig config)
		{
			final ReplicationConfig cfg = config.getReplicationConfig();

			// LOG.trace("Loaded ReplicationConfig: "
			// + JsonUtil.toPrettyJSON(cfg));

			final Builder builder = new Builder();

			builder.withReplicationConfig(cfg);

			if (config.getDefaultAgentType() != null)
				builder.withDefaultAgentType(config.getDefaultAgentType());

			for (Entry<AgentID, Class<? extends Agent>> entry : config
					.getCustomAgentTypes().entrySet())
				builder.withCustomAgentType(entry.getKey().getValue(),
						entry.getValue());

			// FIXME already booted!
			// builder.withBootAgentNames(config.getBootAgentIDs());

			for (Entry<Class<? extends CapabilityFactory>, Class<? extends Capability>> entry : config
					.getSingletonServiceTypes().entrySet())
				builder.withSingletonServiceType(entry.getKey(),
						entry.getValue());

			for (Entry<Class<? extends Capability>, Class<? extends Capability>> entry : config
					.getInstantServiceTypes().entrySet())
				builder.withInstantServiceType(entry.getKey(), entry.getValue());

			for (Entry<Class<? extends Factory>, Class<? extends Factory>> entry : config
					.getCustomFactoryTypes().entrySet())
				builder.withCustomFactoryType(entry.getKey(), entry.getValue());

			return builder;
		}

		/**
		 * @param binderFactoryType
		 * @return
		 */
		public Builder withBinderFactoryType(
				final Class<? extends BinderFactory> binderFactoryType)
		{
			this.binderFactoryType = binderFactoryType;
			return this;
		}

		/**
		 * @param clockName
		 * @return
		 */
		public Builder withReplicationConfig(
				final ReplicationConfig replicationConfig)
		{
			this.replicationConfig = replicationConfig;
			return this;
		}

		/**
		 * @param defaultAgentType
		 * @return
		 */
		public Builder withDefaultAgentType(
				final Class<? extends Agent> defaultAgentType)
		{
			this.defaultAgentType = defaultAgentType;
			return this;
		}

		/**
		 * @param agentName
		 * @param agentType
		 * @return
		 */
		public Builder withCustomAgentType(final String agentName,
				final Class<? extends Agent> agentType)
		{
			this.customAgentTypes.put(agentName, agentType);
			return this;
		}

		/**
		 * @param bootAgentNames
		 * @return
		 */
		public Builder withBootAgentNames(final String... bootAgentNames)
		{
			this.bootAgentNames = bootAgentNames;
			return this;
		}

		/**
		 * @param serviceFactoryType
		 * @param serviceImplementationType
		 * @return
		 */
		public Builder withSingletonServiceType(
				final Class<? extends CapabilityFactory> serviceFactoryType,
				final Class<? extends Capability> serviceImplementationType)
		{
			this.singletonServiceTypes.put(serviceFactoryType,
					serviceImplementationType);
			return this;
		}

		/**
		 * @param serviceType
		 * @param serviceImplementationType
		 * @return
		 */
		public Builder withInstantServiceType(
				final Class<? extends Capability> serviceType,
				final Class<? extends Capability> serviceImplementationType)
		{
			this.instantServiceTypes
					.put(serviceType, serviceImplementationType);
			return this;
		}

		/**
		 * @param factoryType
		 * @param factoryImplementationType
		 * @return
		 */
		public Builder withCustomFactoryType(
				final Class<? extends Factory> factoryType,
				final Class<? extends Factory> factoryImplementationType)
		{
			this.customFactoryTypes.put(factoryType, factoryImplementationType);
			return this;
		}

		/**
		 * @return
		 */
		public BinderFactoryConfig build()
		{
			return new BasicBinderFactoryConfig(this.binderFactoryType,
					this.replicationConfig, this.defaultAgentType,
					this.singletonServiceTypes, this.instantServiceTypes,
					this.customFactoryTypes, this.customAgentTypes,
					this.bootAgentNames);
		}

		/**
		 * @param configType
		 * @param key
		 * @param value
		 */
		public void withModelName(final Class<? extends Mutable> configType,
				final String key, final String value)
		{
			if (configType.equals(ReplicationConfig.class))
				this.replicationConfig.setProperty(key, value);
			else
				throw new IllegalStateException("FIXME not supported: "
						+ configType.getName());
		}

	}

}
