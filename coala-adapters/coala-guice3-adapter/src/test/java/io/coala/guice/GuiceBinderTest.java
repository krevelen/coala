/* $Id: GuiceBinderTest.java 312 2014-06-20 10:27:58Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/test/java/io/coala/guice/GuiceBinderTest.java $
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

import io.coala.agent.Agent;
import io.coala.agent.AgentFactory;
import io.coala.bind.BinderFactory;
import io.coala.log.LogUtil;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Test;

import com.google.inject.assistedinject.AssistedInjectBinding;
import com.google.inject.assistedinject.AssistedInjectTargetVisitor;
import com.google.inject.assistedinject.AssistedMethod;
import com.google.inject.spi.DefaultBindingTargetVisitor;

/**
 * {@link GuiceBinderTest}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class GuiceBinderTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(GuiceBinderTest.class);

	/**
	 * {@link Visitor}
	 * 
	 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
	 * @version $Revision: 312 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public static class Visitor extends
			DefaultBindingTargetVisitor<Object, Void> implements
			AssistedInjectTargetVisitor<Object, Void>
	{

		/** @see AssistedInjectTargetVisitor#visit(AssistedInjectBinding) */
		@Override
		public Void visit(final AssistedInjectBinding<? extends Object> binding)
		{
			// Loop over each method in the factory...
			for (AssistedMethod method : binding.getAssistedMethods())
			{
				LOG.trace("Non-assisted Dependencies: "
						+ method.getDependencies() + ", Factory Method: "
						+ method.getFactoryMethod()
						+ ", Implementation Constructor: "
						+ method.getImplementationConstructor()
						+ ", Implementation Type: "
						+ method.getImplementationType());
			}
			return null;
		}
	}

	@Test
	public void injectTest() throws Exception
	{
		final BinderFactory factory = BinderFactory.Builder.fromFile()
				.withModelName("testModel" + System.currentTimeMillis())
				.build();

		final GuiceBinder binder = (GuiceBinder) factory.create("testAgent",
				TestAgent.class);

		// test TestAgentFactory; log binding using a visitor

		binder.getInjector().getBinding(AgentFactory.class)
				.acceptTargetVisitor(new Visitor());

		final AgentFactory agentFactory = binder.inject(AgentFactory.class);

		final Agent testAgent = agentFactory.create();
		LOG.trace("Assisted Injector created a " + testAgent.getClass());

		// test SimTimeFactory; log binding using a visitor

		binder.getInjector().getBinding(SimTimeFactory.class)
				.acceptTargetVisitor(new Visitor());

		final SimTime testTime = (SimTime) binder.inject(
				SimTimeFactory.class).create(1, TimeUnit.TICKS);
		LOG.trace("Assisted Injector created a time: " + testTime
		// + ", in base time units: " + testTime.toBaseUnit()
		);
	}
}
