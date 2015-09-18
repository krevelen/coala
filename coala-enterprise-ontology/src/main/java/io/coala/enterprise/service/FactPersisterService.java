/* $Id: 05be175242c7d5dd2c22205b26c308804c748ea4 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/service/FactPersisterService.java $
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
package io.coala.enterprise.service;

import io.coala.capability.know.PersistingCapability;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.enterprise.fact.CoordinationFactType;
import io.coala.enterprise.fact.FactID;
import rx.Observable;

/**
 * {@link FactPersisterService}
 * 
 * @version $Revision: 279 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public interface FactPersisterService extends PersistingCapability
{

	/**
	 * @return an {@link Observable} of all persisted facts
	 */
	Observable<CoordinationFact> find();

	/**
	 * @param fact the type of {@link CoordinationFact} to match
	 * @return an {@link Observable} of the matching persisted facts
	 */
	<F extends CoordinationFact> Observable<F> find(Class<F> fact);

	/**
	 * @param fact the type of {@link CoordinationFact} to match
	 * @param factType the {@link CoordinationFactType} to match
	 * @return an {@link Observable} of the matching persisted facts
	 */
	<F extends CoordinationFact> Observable<F> find(Class<F> fact,
			CoordinationFactType factType);

	/**
	 * @param fact the type of {@link CoordinationFact} to return
	 * @param factID the {@link FactID} to match
	 * @return the persisted fact or {@code null} if not found
	 */
	<F extends CoordinationFact> F find(Class<F> fact, FactID factID);

}
