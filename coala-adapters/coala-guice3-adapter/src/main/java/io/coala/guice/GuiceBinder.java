/* $Id: GuiceBinder.java 354 2014-08-09 06:14:14Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/GuiceBinder.java $
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
import io.coala.agent.AgentFactory;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgent;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactoryConfig;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.guice.log.InjectLoggerTypeListener;
import io.coala.log.LogUtil;
import io.coala.model.ModelComponentIDFactory;
import io.coala.model.ModelID;
import io.coala.name.AbstractIdentifiable;
import io.coala.random.RandomDistribution;
import io.coala.random.RandomNumberStream;
import io.coala.random.impl.RandomDistributionFactoryImpl;
import io.coala.random.impl.RandomNumberStreamFactoryWell19937c;
import io.coala.time.ClockID;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.joda.time.Period;

import rx.Observable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.matcher.Matchers;
import com.google.inject.name.Names;

/**
 * {@link GuiceBinder}
 * 
 * @date $Date: 2014-08-09 08:14:14 +0200 (Sat, 09 Aug 2014) $
 * @version $Revision: 354 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class GuiceBinder extends AbstractIdentifiable<AgentID> implements
		Binder
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(GuiceBinder.class);

	/** */
	@JsonIgnore
	private final Injector injector;

	/** */
	//	private final ConfigModule configModule;

	/** */
	private final GuiceLazyBinderModule lazyInstaller;

	/** */
	private final Observable<AgentStatusUpdate> ownerStatus;

	/**
	 * {@link GuiceBinder} constructor
	 * 
	 * @param config
	 * @param clientType
	 */
	public GuiceBinder(final BinderFactoryConfig config,
			final String clientName, final Class<? extends Agent> clientType,
			final Observable<AgentStatusUpdate> ownerStatus,
			final Module... modules)
	{
		this(config, config.getAgentIDFactory().createAgentID(clientName),
				clientType, ownerStatus, modules);
	}

	/**
	 * {@link GuiceBinder} constructor
	 * 
	 * @param config
	 * @param clientType
	 */
	@SuppressWarnings("rawtypes")
	public GuiceBinder(final BinderFactoryConfig config,
			final AgentID clientID, final Class<? extends Agent> clientType,
			final Observable<AgentStatusUpdate> ownerStatus,
			final Module... modules)
	{
		super(clientID);

		this.ownerStatus = ownerStatus;

		final Class<? extends Agent> agentType = clientType != null ? clientType
				: config.getCustomAgentTypes().containsKey(getID()) ? config
						.getCustomAgentTypes().get(getID()) : config
						.getDefaultAgentType();
		LOG.trace("Agent type for: " + getID() + " = " + agentType);

		final List<Module> moduleList = new ArrayList<Module>();

//		this.configModule = new ConfigModule(ConfigUtil.getMainConfig());
//		moduleList.add(this.configModule);

		this.lazyInstaller = new GuiceLazyBinderModule(this)
		{
			@Override
			protected void configure()
			{
				// bind BAAL @InjectLogger injection annotation
				bindListener(Matchers.any(), new InjectLoggerTypeListener());

				bind(Binder.class).toInstance(GuiceBinder.this);

				// bind(BinderFactory.class).toInstance(instance);

				bind(BinderFactoryConfig.class).toInstance(config);

				bind(ModelComponentIDFactory.class).toInstance(
						config.getAgentIDFactory());

				// FIXME replace by binder config
				bind(RandomDistribution.Factory.class).to(
						RandomDistributionFactoryImpl.class);

				// FIXME replace by binder config
				bind(RandomNumberStream.Factory.class).to(
						RandomNumberStreamFactoryWell19937c.class);

				LOG.trace("Bound " + Binder.class.getName() + " to "
						+ GuiceBinder.this + " for client: " + getID());

				// bind(AgentID.class).toInstance(clientID);

				// TODO provide binding configuration, see guice-xml-config and
				// http://beust.com/weblog/2013/07/12/flexible-configuration-with-guice/
				// and OWNER API at http://owner.aeonbits.org/

				bind(ModelID.class).toInstance(config.getModelID());

				bind(Date.class).toInstance(config.getClockOffset());

				bind(TimeUnit.class).toInstance(config.getBaseTimeUnit());

				bind(Period.class).toInstance(config.getClockDuration());

				bind(ClockID.class).toInstance(config.getClockID());

				bindConstant().annotatedWith(Names.named(AGENT_TYPE)).to(
						agentType == null ? Agent.class : agentType);

				for (Entry<Class<? extends CapabilityFactory>, Class<? extends Capability>> entry : config
						.getSingletonServiceTypes().entrySet())
					lazyInstall(
							entry.getKey().asSubclass(CapabilityFactory.class),
							entry.getValue());

				for (Entry<Class<? extends Capability>, Class<? extends Capability>> entry : config
						.getInstantServiceTypes().entrySet())
					bindService(entry.getKey(), entry.getValue());

				install(new FactoryModuleBuilder().implement(SimTime.class,
						GuiceSimTime.class).build(SimTimeFactory.class));

				install(new FactoryModuleBuilder().implement(Agent.class,
						agentType == null ? BasicAgent.class : agentType)
						.build(AgentFactory.class));

			}
		};
		moduleList.add(this.lazyInstaller);
		moduleList.addAll(Arrays.asList(modules));
		this.injector = GuiceUtil.getInstance(getID(), moduleList)
				.getInjector();
	}

	/**
	 * @return
	 */
	protected Injector getInjector()
	{
		return this.injector;
	}

	/**
	 * @return
	 */
	protected Observable<AgentStatusUpdate> getOwnerStatus()
	{
		return this.ownerStatus;
	}

	@Override
	public <T> T inject(final Class<T> type)
	{
		return getInjector().getInstance(type);
	}

	// protected Map<Class<?>,Object> bindings = new HashMap<>();

	@Override
	public Set<Class<?>> getBindings()
	{
		final Set<Class<?>> result = new HashSet<>(
				this.lazyInstaller.available.keySet());
		if (getInjector() != null)
			for (Key<?> key : getInjector().getBindings().keySet())
				result.add(key.getTypeLiteral().getRawType());
		return Collections.unmodifiableSet(result);
	}

	@Override
	public <T> Provider<T> rebind(final Class<T> type, final T instance)
	{
		return rebind(type, new Provider<T>()
		{
			@Override
			public T get()
			{
				return instance;
			}
		});
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Provider<T> rebind(final Class<T> type,
			final Provider<T> provider)
	{
		final T old = (T) this.lazyInstaller.lazyInstances.remove(type);
		LOG.trace("Rebinding " + type.getName() + ", removed lazy instance: "
				+ old);
		return (Provider<T>) this.lazyInstaller.available.put(type,
				new com.google.inject.Provider<T>()
				{
					@Override
					public T get()
					{
						return provider.get();
					}
				});
	}

}
