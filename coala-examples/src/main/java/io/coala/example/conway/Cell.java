/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/Cell.java $
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
package io.coala.example.conway;

import io.coala.agent.Agent;
import io.coala.time.SimTime;
import rx.Observable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link Cell}
 * 
 * @date $Date: 2014-06-03 13:55:16 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the implementation type of {@link Cell}
 */
public interface Cell extends Agent
{

	/**
	 * @param time the {@link SimTime} to return the state of
	 * @return the {@link Cell}'s state at specified {@link SimTime}
	 */
	@JsonIgnore
	Observable<CellState> getState(SimTime time);

}
