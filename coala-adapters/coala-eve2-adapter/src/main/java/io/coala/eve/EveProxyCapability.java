/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/main/java/com/almende/coala/eve/EveProxyService.java $
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
package io.coala.eve;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.interact.ProxyCapability;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.inject.Inject;

/**
 * {@link EveProxyCapability}
 * 
 * @version $Revision: 255 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class EveProxyCapability extends BasicCapability implements ProxyCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link EveProxyCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected EveProxyCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see ProxyCapability#getProxy(AgentID, Class) */
	@SuppressWarnings("unchecked")
	@Override
	public <A extends Agent> A getProxy(final AgentID agentID,
			final Class<A> agentType) throws Exception
	{
		// FIXME apply messaging in stead of actual "remote" agent object
		return (A) Proxy.newProxyInstance(agentType.getClassLoader(),
				new Class<?>[] { agentType }, new InvocationHandler()
				{
					@Override
					public Object invoke(final Object proxy,
							final Method method, final Object[] args)
							throws Throwable
					{
						return method.invoke(EveAgentManager.getInstance()
								.getAgent(agentID, true), args);
					}
				});
	}

}
