/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/bind/Binder.java $
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
import io.coala.capability.Capability;
import io.coala.factory.Factory;
import io.coala.name.Identifiable;

import java.util.Set;

import javax.inject.Provider;

/**
 * {@link Binder} can contain {@link Agent}s and provides services
 * 
 * @date $Date: 2014-08-08 16:20:51 +0200 (Fri, 08 Aug 2014) $
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the (sub)type of {@link Binder} to build
 */
public interface Binder extends Identifiable<AgentID>
{

	/** */
	String AGENT_TYPE = "agentType";

	/**
	 * Injection of an object, e.g. a {@link Capability} or {@link Factory}
	 * 
	 * @param type the type of object to inject
	 * @return the object resulting from a (local) binding to some instance,
	 *         factory or provider
	 */
	<T> T inject(Class<T> type);

	Set<Class<?>> getBindings();

	<T> Provider<T> rebind(Class<T> type, T instance);

	<T> Provider<T> rebind(Class<T> type, Provider<T> provider);
	
}
