/* $Id: 84641cea282a251c570ac4b1862cb91a08141ac7 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/impl/TestExecutorWorldImpl.java $
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
package io.coala.enterprise.test.impl;

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.enterprise.test.TestExecutorWorld;

/**
 * {@link TestExecutorWorldImpl}
 * 
 * @version $Revision: 279 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class TestExecutorWorldImpl extends BasicCapability implements
		TestExecutorWorld
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link TestExecutorWorldImpl} constructor
	 * 
	 * @param binder
	 */
	protected TestExecutorWorldImpl(final Binder binder)
	{
		super(binder);
	}

}
