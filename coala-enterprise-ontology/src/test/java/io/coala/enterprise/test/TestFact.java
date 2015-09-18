/* $Id: fa6a2f7a60a8df9b672c7ac434a74e4e7a5dac69 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/test/java/io/coala/enterprise/test/TestFact.java $
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

import io.coala.agent.AgentID;
import io.coala.capability.CapabilityFactory;
import io.coala.enterprise.fact.AbstractCoordinationFact;
import io.coala.enterprise.fact.AbstractCoordinationFactBuilder;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.enterprise.fact.FactID;
import io.coala.model.ModelComponentID;
import io.coala.time.SimTime;

/**
 * {@link TestFact}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public abstract class TestFact extends AbstractCoordinationFact
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link TestFact} constructor
	 * 
	 * @param id
	 * @param producerID
	 * @param senderID
	 * @param receiverID
	 * @param expiration the absolute instant when this fact invalidates, or
	 *            {@code null} if never
	 */
	protected TestFact(final FactID id, final ModelComponentID<?> producerID,
			final AgentID senderID, final AgentID receiverID,
			final SimTime expiration)
	{
		super(id, producerID, senderID, receiverID, expiration);
	}

	/**
	 * {@link TestFact} zergo-arg bean constructor
	 */
	protected TestFact()
	{
		super();
	}

	/**
	 * {@link Initiator}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public interface Initiator extends
			io.coala.enterprise.role.Initiator<Response>
	{

		/**
		 * {@link Factory}
		 *
		 * @version $Revision: 324 $
		 * @author <a href="mailto:Rick@almende.org">Rick</a>
		 */
		interface Factory extends CapabilityFactory<Initiator>
		{
			// empty
		}

		Request initiate(AgentID executorID) throws Exception;
	}

	/**
	 * {@link Executor}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public interface Executor extends
			io.coala.enterprise.role.Executor<Request>
	{

		/**
		 * {@link Factory}
		 * 
		 * @version $Revision: 324 $
		 * @author <a href="mailto:Rick@almende.org">Rick</a>
		 */
		interface Factory extends CapabilityFactory<Executor>
		{
			// empty
		}

	}

	/**
	 * {@link Request}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 */
	public static class Request extends TestFact
	{

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * {@link Request} zero-arg bean constructor
		 */
		protected Request()
		{
			super();
		}

		/**
		 * {@link Request} constructor
		 * 
		 * @param id
		 * @param producerID
		 * @param senderID
		 * @param receiverID
		 * @param expiration
		 */
		protected Request(final FactID id,
				final ModelComponentID<?> producerID, final AgentID senderID,
				final AgentID receiverID, final SimTime expiration)
		{
			super(id, producerID, senderID, receiverID, expiration);
		}

		/**
		 * {@link Builder}
		 * 
		 * @version $Revision: 324 $
		 * @author <a href="mailto:Rick@almende.org">Rick</a>
		 *
		 */
		public static class Builder extends
				AbstractCoordinationFactBuilder<Request, Builder>
		{

			/**
			 * @param initiator
			 */
			public static Builder forProducer(final TestFact.Initiator initiator)
			{
				return new Builder().withID(initiator.getID().getModelID(),
						initiator.getTime()).withProducer(initiator);
			}

			/** @see CoordinationFact.Builder#build() */
			@Override
			public Request build()
			{
				return new Request(getFactID(), getProducerID(), getSenderID(),
						getReceiverID(), getExpiration());
			}

		}
	}

	/**
	 * {@link Request}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 * 
	 */
	public static class Response extends TestFact
	{

		/** */
		private static final long serialVersionUID = 1L;

		/**
		 * {@link Response} zero-arg bean constructor
		 */
		protected Response()
		{
			super();
		}

		/**
		 * {@link Response} constructor
		 * 
		 * @param id
		 * @param producerID
		 * @param senderID
		 * @param receiverID
		 * @param expiration
		 */
		protected Response(final FactID id,
				final ModelComponentID<?> producerID, final AgentID senderID,
				final AgentID receiverID, final SimTime expiration)
		{
			super(id, producerID, senderID, receiverID, expiration);
		}

		/**
		 * {@link Builder}
		 * 
		 * @version $Revision: 324 $
		 * @author <a href="mailto:Rick@almende.org">Rick</a>
		 *
		 */
		public static class Builder extends
				AbstractCoordinationFactBuilder<Request, Builder>
		{

			/**
			 * @param initiator
			 */
			public static Builder forProducer(final TestFact.Executor executor,
					final Request cause)
			{
				return new Builder().withID(executor.getTime(), cause)
						.withProducer(executor)
						.withReceiverID(cause.getSenderID());
			}

			/** @see CoordinationFact.Builder#build() */
			@Override
			public Request build()
			{
				return new Request(getFactID(), getProducerID(), getSenderID(),
						getReceiverID(), getExpiration());
			}

		}
	}
}
