/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/LifeCycleHooks.java $
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
package io.coala.lifecycle;

/**
 * {@link LifeCycleHooks} provides the default hooks of a managed
 * {@link LifeCycle} object
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link LifeCycleStatus}
 * @param <THIS> the (sub)type of {@link LifeCycleHooks} to build
 */
public interface LifeCycleHooks
{

	/**
	 * @return the type of {@link #activate()} phase management for this
	 *         {@link LifeCycle} object
	 */
	ActivationType getActivationType();

	/** Hook called by its container to setup the {@link LifeCycle} object */
	@AfterInstantiation
	void initialize() throws Exception;

	/**
	 * Hook called by its container to start the {@link LifeCycle} object,
	 * depending on the {@link ActivationType} returned by
	 * {@link #getActivationType()}
	 */
	@BeforeMethodCalls
	void activate() throws Exception;

	/**
	 * Hook called by its container to pause the {@link LifeCycle} object,
	 * depending on the {@link ActivationType} returned by
	 * {@link #getActivationType()}
	 */
	@AfterMethodCalls
	void deactivate() throws Exception;

	/** Hook called by its container to finalize the {@link LifeCycle} object */
	@BeforeDestruction
	void finish() throws Exception;

}
