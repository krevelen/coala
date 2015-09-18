/* $Id: 09f914abc5c3bba5c23035532334e494adb71b31 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/role/ActorRole.java $
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
package io.coala.enterprise.role;

import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityID;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.model.ModelComponent;
import io.coala.time.SimTime;
import io.coala.time.Timed;
import rx.Observable;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link ActorRole}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <F> the (super)type of {@link CoordinationFact}
 */
public interface ActorRole<F extends CoordinationFact> extends
		Capability<BasicCapabilityStatus>, ModelComponent<CapabilityID>,
		Timed<SimTime>
{

	/**
	 * @return an {@link Observable} replaying all the {@link CoordinationFact}s
	 *         until now
	 */
	Observable<F> replayFacts();

	/** @return a new localized {@link SimTime} */
	// SimTime newTime(final Number value, final TimeUnit unit);

	@JsonIgnore
	SimTime getTime();

	// void onStopped(F stop); // common to both initiator and executor role
	// types

	// void onQuit(F quit); // common to both initiator and executor role types

}
