/* $Id$
 * $URL:
 * https://svn.gfi-info.com/codeAll4Green/trunk/All4Green/AgentPlatform/src
 * /main/java/eu/a4g/agent/impl/DCAgentImpl.java $
 * 
 * Part of the EU project All4Green, see http://www.all4green-project.eu/
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
 * Copyright © 2010-2013 Almende B.V.
 */
package io.coala.optimize;

import java.io.Serializable;

/**
 * 
 * {@link WeightedCriterion}
 *
 * @date $Date: 2014-07-07 16:12:23 +0200 (Mon, 07 Jul 2014) $
 * @version $Revision: 323 $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 *
 */
public interface WeightedCriterion extends Serializable
{
	
	/** @return this {@link WeightedCriterion}'s identifier */
	String getID();

	/** @return this {@link WeightedCriterion}'s description */
	String getDescription();
	
}