/* $Id: f9b36771d5f7dc74383f3102c221cb7368ed7d86 $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/booter/BasicBooterService.java $
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
package io.coala.capability.admin;

import io.coala.agent.AbstractAgentManager;
import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentManager;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.Binder;
import io.coala.bind.BinderFactory;
import io.coala.capability.BasicCapability;
import io.coala.exception.CoalaException;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.slf4j.Logger;

import rx.Observable;
import rx.Observer;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * {@link BasicCreatingCapability} generates some fake wrapper/container agent
 * life cycle behavior
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class BasicCreatingCapability extends BasicCapability implements
		CreatingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/** */
	private AgentManager agentManager;

	/**
	 * {@link BasicCreatingCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected BasicCreatingCapability(final Binder binder)
	{
		super(binder);
		this.agentManager = new AbstractAgentManager(
				binder.inject(BinderFactory.Builder.class))
		{
			@Override
			protected AgentID boot(final Agent agent) throws CoalaException
			{
				// fake the wrapper/container agent lifecycle
				Schedulers.trampoline().createWorker().schedule(new Action0()
				{
					@Override
					public void call()
					{
						updateWrapperAgentStatus(agent.getID(),
								BasicAgentStatus.CREATED);
						updateWrapperAgentStatus(agent.getID(),
								BasicAgentStatus.INITIALIZED);
						updateWrapperAgentStatus(agent.getID(),
								BasicAgentStatus.ACTIVE);
					}
				});
				agent.getStatusHistory().subscribe(
						new Observer<BasicAgentStatus>()
						{
							@Override
							public void onError(final Throwable e)
							{
								e.printStackTrace();
							}

							@Override
							public void onNext(final BasicAgentStatus status)
							{
								if (status.isInitializedStatus())
								{
									Schedulers.trampoline().createWorker()
											.schedule(new Action0()
											{
												@Override
												public void call()
												{
													updateWrapperAgentStatus(
															agent.getID(),
															BasicAgentStatus.PASSIVE);
												}
											});
								} else if (status.isFinishedStatus()
										|| status.isFailedStatus())
								{
									Schedulers.trampoline().createWorker()
											.schedule(new Action0()
											{
												@Override
												public void call()
												{
													updateWrapperAgentStatus(
															agent.getID(),
															BasicAgentStatus.COMPLETE);
													updateWrapperAgentStatus(
															agent.getID(),
															BasicAgentStatus.FINISHED);
												}
											});
								}
							}

							@Override
							public void onCompleted()
							{
								// TODO check if fake wrapper/container
								// life cycle is also {@link
								// BasicAgentStatus#FINISHED}
							}
						});
				return agent.getID();
			}

			@Override
			protected void shutdown()
			{
				// nothing to do, service including agent manager will be GC'ed
			}
		};
	}

	@Override
	public Observable<AgentID> getChildIDs(final boolean currentOnly)
	{
		return this.agentManager
				.getChildIDs(getID().getOwnerID(), currentOnly);
	}

	@Override
	public Observable<AgentStatusUpdate> createAgent(final String agentID)
	{
		return this.agentManager.boot(agentID);
	}

	@Override
	public <A extends Agent> Observable<AgentStatusUpdate> createAgent(
			final String agentID, final Class<A> agentType)
	{
		return this.agentManager.boot(agentID, agentType);
	}

	@Override
	public Observable<AgentStatusUpdate> createAgent(final AgentID agentID)
	{
		return this.agentManager.boot(agentID);
	}

	@Override
	public <A extends Agent> Observable<AgentStatusUpdate> createAgent(
			final AgentID agentID, final Class<A> agentType)
	{
		return this.agentManager.boot(agentID, agentType);
	}

}
