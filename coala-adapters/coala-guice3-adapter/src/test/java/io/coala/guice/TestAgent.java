/* $Id: TestAgent.java 240 2014-04-18 15:39:26Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/test/java/io/coala/guice/TestAgent.java $
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
package io.coala.guice;

import io.coala.agent.BasicAgent;
import io.coala.bind.Binder;
import io.coala.lifecycle.LifeCycle;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link TestAgent}
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class TestAgent extends BasicAgent
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link TestAgent} constructor
	 * 
	 * @param id
	 * @param host
	 */
	@Inject
	public TestAgent(final Binder host)
	{
		super(host);
	}

	/** @see LifeCycle#activate() */
	@Override
	public void activate()
	{
		LOG.info("Executed");
	}

}
