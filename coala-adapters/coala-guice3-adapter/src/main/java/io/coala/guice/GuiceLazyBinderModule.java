/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/GuiceLazyBinderModule.java $
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
package io.coala.guice;

import io.coala.agent.AgentStatusObserver;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.factory.ClassUtil;
import io.coala.log.LogUtil;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.assistedinject.FactoryModuleBuilder;

/**
 * {@link GuiceLazyBinderModule}
 * 
 * @version $Revision: 354 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class GuiceLazyBinderModule extends AbstractModule
{

	/** */
	private final GuiceBinder owner;

	/**
	 * {@link GuiceLazyBinderModule} constructor
	 * 
	 * @param owner
	 */
	public GuiceLazyBinderModule(final GuiceBinder owner)
	{
		this.owner = owner;
	}

	private static final Logger LOG = LogUtil
			.getLogger(GuiceLazyBinderModule.class);

	/** service instances already created with their respective factory */
	protected final Map<Class<?>, Object> lazyInstances = Collections
			.synchronizedMap(new HashMap<Class<?>, Object>());

	/** */
	protected Set<Class<?>> unavailable = new HashSet<>();

	/** */
	protected Map<Class<?>, javax.inject.Provider<?>> available = new HashMap<>();

	/**
	 * bind a concrete type to a new service object newly created with the
	 * binder itself
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	protected <S, F extends CapabilityFactory<S>, T extends S> void lazyInstall(
			final Class<? extends CapabilityFactory> factoryType,
			final Class<T> serviceTypeImpl)
	{
		final List<Class<?>> typeArgs = ClassUtil.getTypeArguments(
				CapabilityFactory.class, factoryType);
		final Class<S> serviceType = (Class<S>) typeArgs.get(0);
		lazyInstall(serviceType, (Class<F>) factoryType, serviceTypeImpl);
	}

	/**
	 * helper method used for e.g. instant service types
	 * 
	 * @param serviceType
	 * @param serviceImplType
	 */
	// @Deprecated
	protected <T extends Capability<?>> void bindService(
			final Class<T> serviceType, final Class<?> serviceImplType)
	{
		bind(serviceType).to(serviceImplType.asSubclass(serviceType));
	}

	/**
	 * bind a concrete type to a new service object newly created with the
	 * binder itself
	 */
	protected <S, T extends S> void lazyInstall(final Class<S> serviceType,
			final Class<? extends CapabilityFactory<S>> factoryType,
			final Class<T> serviceTypeImpl)
	{
		if (serviceTypeImpl == null)
			this.unavailable.add(serviceType);
		else
		{
			/*final Binding<S> binding = this.owner.getInjector() == null ? null
					: this.owner.getInjector().getBinding(serviceType);
			if (binding != null)
			{
				LOG.warn("Already bound: "
						+ serviceType.getName()
						+ " to: "
						+ binding.getKey().getTypeLiteral().getRawType()
								.getName());
				return;
			}*/
			
			// first install the service factory
			install(new FactoryModuleBuilder().implement(serviceType,
					serviceTypeImpl).build(factoryType));
			// LOG.trace(String.format("Local singleton %s bound on demand to %s",
			// serviceType.getSimpleName(), serviceTypeImpl.getSimpleName()));

			// then bind the service type to a lazy provider that uses the
			// factory
			final Provider<S> provider = createProvider(serviceType,
					factoryType);

			// LOG.trace(String.format("Bound provider for %s (args: %s)",
			// Arrays.asList(provider.getClass().getInterfaces()),
			// ClassUtil.getTypeArguments(Provider.class,
			// provider.getClass())));

			bind(serviceType).toProvider(provider);
			this.available.put(serviceType, provider);
		}
	}

	/**
	 * @param serviceType
	 * @param serviceFactoryType
	 * @return the lazy provider that uses a {@link CapabilityFactory} from the
	 *         same injector
	 */
	protected <S> Provider<S> createProvider(final Class<S> serviceType,
			final Class<? extends CapabilityFactory<S>> serviceFactoryType)
	{
		return new Provider<S>()
		{
			@Override
			public S get()
			{
				if (!lazyInstances.containsKey(serviceType))
				{
					if (unavailable.contains(serviceType))
					{
						for (Object impl : lazyInstances.values())
							if (serviceType.isInstance(impl))
							{
								lazyInstances.put(serviceType, impl);
								LOG.trace(String.format("Provided %s <= %s",
										serviceType.getSimpleName(), impl
												.getClass().getName()));
								break;
							}
						if (!lazyInstances.containsKey(serviceType))
							throw CoalaExceptionFactory.VALUE_NOT_SET
									.createRuntime(serviceType.getClass()
											.getName());
					} else
					{
						final S service = owner.getInjector()
								.getInstance(serviceFactoryType).create();

						if (service instanceof AgentStatusObserver)
							owner.getOwnerStatus().subscribe(
									(AgentStatusObserver) service);

						LOG.trace(String.format("Provided %s <= %s",
								serviceType.getSimpleName(), service.getClass()
										.getName()));
						lazyInstances.put(serviceType, service);
					}
				}

				return serviceType.cast(lazyInstances.get(serviceType));
			}
		};
	}
}