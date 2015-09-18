/* $Id: 9607458631c488b37d84cb325800b2fa97c96cb1 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/TestInitiatorOrganization.java $
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

import io.coala.enterprise.organization.Organization;

/**
 * {@link TestInitiatorOrganization}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface TestInitiatorOrganization extends Organization
{

	TestFact.Initiator getTestFactInitiator();
	
	String EXECUTOR_NAME_KEY = "executor";
	
	String EXECUTOR_NAME_DEFAULT = "executorOrg";

}
