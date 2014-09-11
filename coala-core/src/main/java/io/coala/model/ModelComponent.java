/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/model/ModelComponent.java $
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
package io.coala.model;

import io.coala.agent.AgentID;
import io.coala.name.Identifiable;
import io.coala.time.Instant;

import java.io.Serializable;

/**
 * {@link ModelComponent}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link ModelComponentID}
 * @param <THIS> the concrete type of {@link ModelComponent}
 */
public interface ModelComponent<ID extends ModelComponentID<?>> extends
		Identifiable<ID>, Serializable
{

	/** @return the {@link AgentID} of the owner of this {@link ModelComponent} */
	AgentID getOwnerID();

	/**
	 * @return
	 */
	Instant<?> getTime();

	/**
	 * {@link Builder}
	 * 
	 * @version $Revision: 300 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 * @param <ID>
	 * @param <M>
	 * @param <THIS>
	 */
	public interface Builder<ID extends ModelComponentID<?>, M extends ModelComponent<ID>, THIS extends Builder<ID, M, THIS>>
			extends Identifiable.Builder<ID, M, THIS>
	{
		/**
		 * @param ownerID the {@link AgentID} to set
		 * @return this {@link Builder}
		 */
		THIS withOwnerID(AgentID ownerID);
	}

}
