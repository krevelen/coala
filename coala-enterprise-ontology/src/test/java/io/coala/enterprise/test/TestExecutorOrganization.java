/* $Id: fdff45541c58bed0c7e56b0dabd77ec2201e1515 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/TestExecutorOrganization.java $
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
 * {@link TestExecutorOrganization}
 * 
 * @version $Revision: 279 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface TestExecutorOrganization extends Organization
{

	// TestExecutorWorld getWorld();

	TestFact.Executor getTestFactExecutor();

}
