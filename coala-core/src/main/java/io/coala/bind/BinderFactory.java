/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/bind/BinderFactory.java $
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
package io.coala.bind;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.config.CoalaProperty;
import io.coala.config.CoalaPropertyMap;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.factory.ClassUtil;
import io.coala.factory.Factory;

import org.aeonbits.owner.Mutable;

import rx.Observable;
import rx.subjects.PublishSubject;

/**
 * {@link BinderFactory}
 * 
 * @date $Date: 2014-08-04 14:19:04 +0200 (Mon, 04 Aug 2014) $
 * @version $Revision: 336 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public interface BinderFactory extends Factory// , AgentStatusObserver
{

	/**
	 * @param config the {@link BinderFactoryConfig} implementation with
	 *            required initialization values
	 * @param ownerStatusUpdates an {@link Observable} for the created
	 *            {@link Binder}s' owner {@link AgentStatusUpdate}s
	 * @return this {@link BinderFactory} instance
	 */
	BinderFactory initialize(BinderFactoryConfig config,
			Observable<AgentStatusUpdate> ownerStatusUpdates);

	/** @return the {@link BinderFactoryConfig} used for initialization */
	BinderFactoryConfig getConfig();

	/**
	 * @param ownerName the identifier of the agent that owns the {@link Binder}
	 * @return the (new) {@link Binder} object for specified {@code ownerName}
	 */
	Binder create(String ownerName);

	/**
	 * @param ownerName the identifier of the agent that owns the {@link Binder}
	 * @param ownerType
	 * @return the (new) {@link Binder} object for specified {@code ownerName}
	 */
	Binder create(String ownerName, Class<? extends Agent> ownerType);

	/**
	 * @param ownerID the identifier of the agent that owns the {@link Binder}
	 * @return the (new) {@link Binder} object for specified {@code ownerID}
	 */
	Binder create(AgentID ownerID);

	/**
	 * @param ownerID the identifier of the agent that owns the {@link Binder}
	 * @param ownerType
	 * @return the (new) {@link Binder} object for specified {@code ownerID}
	 */
	Binder create(AgentID ownerID, Class<? extends Agent> ownerType);

	/**
	 * @param ownerID
	 */
	Binder remove(AgentID ownerID);

	/**
	 * {@link Builder}
	 * 
	 * @date $Date: 2014-08-04 14:19:04 +0200 (Mon, 04 Aug 2014) $
	 * @version $Revision: 336 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public class Builder
	{

		/** */
		private BinderFactoryConfig.Builder configBuilder;

		/** */
		private Class<? extends BinderFactory> binderFactoryType;

		/** */
		private Observable<AgentStatusUpdate> agentStatusPublisher = PublishSubject
				.create();

		/** */
		private Builder()
		{
			// empty
		}

		/**
		 * @return the {@link BinderFactory}
		 * @throws CoalaException
		 */
		public static Builder fromFile() throws CoalaException
		{
			return fromFile((String) null);
		}

		/**
		 * @param configPath
		 * @return the {@link BinderFactory}
		 * @throws CoalaException
		 */
		public static Builder fromFile(final String configPath)
				throws CoalaException
		{
			CoalaPropertyMap.getInstance(false).load(configPath);

			return new Builder().withType(
					CoalaProperty.binderFactoryType.value().getType(
							BinderFactory.class)).withConfigBuilder(
					BinderFactoryConfig.Builder.fromFile(configPath));
		}

		/**
		 * @param inject
		 * @return
		 */
		public static Builder fromConfig(final BinderFactoryConfig config)
		{
			return new Builder().withType(config.getBinderFactoryType())
					.withConfigBuilder(
							BinderFactoryConfig.Builder.fromConfig(config));
		}

		/**
		 * @param binderFactoryType
		 * @return
		 */
		public Builder withType(
				final Class<? extends BinderFactory> binderFactoryType)
		{
			this.binderFactoryType = binderFactoryType;
			return this;
		}

		/**
		 * @param configBuilder
		 * @return
		 */
		public Builder withConfigBuilder(
				final BinderFactoryConfig.Builder configBuilder)
		{
			this.configBuilder = configBuilder;
			return this;
		}

		/**
		 * @param idFactory
		 * @return
		 */
		public Builder withReplicationConfig(
				final ReplicationConfig replicationConfig)
		{
			this.configBuilder.withReplicationConfig(replicationConfig);
			return this;
		}

		/**
		 * @param defaultAgentType
		 * @return
		 */
		public Builder withDefaultAgentType(
				final Class<? extends Agent> defaultAgentType)
		{
			this.configBuilder.withDefaultAgentType(defaultAgentType);
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
			this.configBuilder.withCustomAgentType(agentName, agentType);
			return this;
		}

		/**
		 * @param bootAgentNames
		 * @return
		 */
		public Builder withBootAgentNames(final String... bootAgentNames)
		{
			this.configBuilder.withBootAgentNames(bootAgentNames);
			return this;
		}

		/**
		 * @param serviceFactoryType
		 * @param serviceImplementationType
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public Builder withSingletonServiceType(
				final Class<? extends CapabilityFactory> serviceFactoryType,
				final Class<? extends Capability> serviceImplementationType)
		{
			this.configBuilder.withSingletonServiceType(serviceFactoryType,
					serviceImplementationType);
			return this;
		}

		/**
		 * @param serviceType
		 * @param serviceImplementationType
		 * @return
		 */
		@SuppressWarnings("rawtypes")
		public Builder withInstantServiceType(
				final Class<? extends Capability> serviceType,
				final Class<? extends Capability> serviceImplementationType)
		{
			this.configBuilder.withInstantServiceType(serviceType,
					serviceImplementationType);
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
			this.configBuilder.withCustomFactoryType(factoryType,
					factoryImplementationType);
			return this;
		}

		public Builder withAgentStatusSource(
				final Observable<AgentStatusUpdate> updates)
		{
			this.agentStatusPublisher = updates;
			return this;
		}

		/**
		 * @return the (new) {@link BinderFactory}
		 */
		public BinderFactory build()
		{
			try
			{
				return ClassUtil.instantiate(this.binderFactoryType)
						.initialize(this.configBuilder.build(),
								this.agentStatusPublisher.asObservable());
			} catch (final CoalaException e)
			{
				throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime(e,
						"binderFactoryType", this.binderFactoryType);
			}
		}

		/**
		 * @param configType
		 * @param key
		 * @param value
		 * @return
		 */
		public Builder withProperty(final Class<? extends Mutable> configType,
				final String key, final String value)
		{
			this.configBuilder.withModelName(configType, key, value);
			return this;
		}

	}

}
