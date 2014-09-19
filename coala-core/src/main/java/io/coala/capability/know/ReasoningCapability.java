/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/reasoner/ReasonerService.java $
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
package io.coala.capability.know;

import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;

import java.io.Serializable;
import java.util.Map;

import rx.Observable;

/**
 * {@link ReasoningCapability} e.g. based on OWLIM?
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public interface ReasoningCapability extends Capability<BasicCapabilityStatus>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	// @SuppressWarnings("rawtypes")
	interface Factory extends CapabilityFactory<ReasoningCapability>
	{
		// empty
	}

	interface Belief extends Serializable // Tag
	{
		Belief negate();
	}

	interface Query extends Serializable // Tag
	{
		Query negate();
	}

	/**
	 * Method that converts a POJO into a Belief and instantiates it with a
	 * optional set of parameters.
	 * 
	 * @param javaObject The javaObject that has to be converted into a belief.
	 * @param keyValuePairs A optional map of key => value that will be used to
	 *        create an instance of the belief for the javaObject that will be
	 *        returned as a Belief
	 * @return
	 */
	Belief toBelief(final Object javaObject,
			final Map<String, Object> keyValuePairs);

	/**
	 * Method that converts a POJO into a Belief and instantiates it with a
	 * optional set of parameters.
	 * 
	 * @param javaObject The javaObject that has to be converted into a belief.
	 * @param params A optional list of parameters that will be used to create
	 *        an instance of the belief for the javaObject that will be returned
	 *        as a Belief
	 * @return
	 */
	Belief toBelief(final Object javaObject, final Object... params);

	/**
	 * Method that converts a POJO into a Query and instantiates it with a
	 * optional set of parameters.
	 * 
	 * @param javaObject The javaObject that has to be converted into a query.
	 * @param keyValuePairs A optional map of key => value's that will be used
	 *        to create an instance of the belief for the javaObject that will
	 *        be returned as a Query
	 * @return
	 */
	Query toQuery(final Object javaObject,
			final Map<String, Object> keyValuePairs);

	/**
	 * Method that converts a POJO into a Query and instantiates it with a
	 * optional set of parameters.
	 * 
	 * @param javaObject The javaObject that has to be converted into a query.
	 * @param params A optional list of parameters that will be used to create
	 *        an instance of the belief for the javaObject that will be returned
	 *        as a Query
	 * @return
	 */
	Query toQuery(final Object javaObject, final Object... params);

	/**
	 * Method to assert a belief into the knowledge base.
	 * 
	 * @param belief the belief that will be asserted into the KBase.
	 * @see KBase
	 */
	void addBeliefToKBase(final Belief belief);

	/**
	 * Method to assert a belief into the knowledge base.
	 * 
	 * @param belief the belief that will be retracted from the KBase.
	 * @see KBase
	 */
	void removeBeliefFromKBase(final Belief belief);

	/**
	 * Method that performs a query to the knowledge base as observer.
	 * 
	 * @param query A query will be queried to the KBase
	 * @return A Observable that observes a set of Map's containing the
	 *         uninstantiated values of the javaObject that where found with the
	 *         query
	 * @see KBase
	 * @see Observable
	 */
	Observable<Map<String, Object>> queryToKBase(final Query query);

	/**
	 * TODO compare existing (distributed, asynchronous) cluster computing APIs
	 * (a la <a href="http://hadoop.apache.org/">Hadoop</a>/<a
	 * href="http://storm.incubator.apache.org/">Storm</a>-based <a
	 * href="https://spark.incubator.apache.org/">Spark</a>, <a
	 * href="http://mahout.apache.org/">Mahout</a>, <a
	 * href="https://twitter.com/summingbird">Summingbird</a>) to see how
	 * reasoning can be supported
	 */
	// <B extends Belief> Observable<B> getBeliefs(Matcher<B> matcher);

	/** get the agent's local knowledge base */
	Object getKBase();
}
