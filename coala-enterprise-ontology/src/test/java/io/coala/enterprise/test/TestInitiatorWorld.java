/* $Id: a6402e7a6b803bb8847eb9d53fcd4c7fc725ef5e $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/TestInitiatorWorld.java $
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
package io.coala.enterprise.test;

import io.coala.capability.CapabilityFactory;
import io.coala.capability.embody.GroundingCapability;

/**
 * {@link TestInitiatorWorld}
 * 
 * @version $Revision: 279 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface TestInitiatorWorld extends GroundingCapability
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 279 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	interface Factory extends CapabilityFactory<TestInitiatorWorld>
	{
		// empty
	}

}
