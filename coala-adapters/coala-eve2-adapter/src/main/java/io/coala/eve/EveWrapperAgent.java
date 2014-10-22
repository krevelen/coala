/* $Id$
 * $URL$
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
package io.coala.eve;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgentStatus;
import io.coala.capability.interact.ReceivingCapability;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;
import io.coala.message.Message;
import io.coala.message.MessageHandler;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;

import rx.Observer;

import com.almende.eve.rpc.annotation.Access;
import com.almende.eve.rpc.annotation.AccessType;
import com.almende.eve.rpc.jsonrpc.JSONRPCException;
import com.almende.eve.rpc.jsonrpc.JSONRequest;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * {@link EveWrapperAgent}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class EveWrapperAgent extends com.almende.eve.agent.Agent implements
		Observer<AgentStatusUpdate>, EveSenderAgent, EveReceiverAgent,
		EveExposingAgent {

	/** */
	private static final Logger LOG = LogUtil.getLogger(EveWrapperAgent.class);

	/** @return the {@link AgentID} identifier of the wrapped agent */
	protected final AgentID getAgentID() {
		return EveUtil.toAgentID(getId());
	}

	/**
	 * @param status
	 */
	protected final void updateWrapperStatus(final BasicAgentStatus status) {
		final AgentID agentID = getAgentID();
		EveAgentManager.getInstance().updateWrapperAgentStatus(agentID, status);
	}

	/**
	 * maps this {@link EveWrapperAgent}'s behavior to the wrapped {@link Agent}
	 * 's state
	 */
	protected final void configureLifeCycleHandling() {
		final AgentID agentID = getAgentID();
		final List<String> eveURLs = getUrls();
		LOG.info(String.format("Eve wrapper: %s of agent: %s "
				+ "went online at: %s", getId(), agentID, eveURLs));

		try {
			EveAgentManager.getInstance().setAddress(getId(), eveURLs);
			EveAgentManager.getInstance().getAgentStatus(agentID)
					.subscribe(this);
		} catch (final Throwable t) {
			LOG.error(String.format("Problem for wrapper %s getting "
					+ "status updates from agent %s", getId(), agentID), t);
		}
	}

	/** @see Observer#onError(Throwable) */
	@Override
	public final void onError(final Throwable t) {
		t.printStackTrace();
	}

	/** @see Observer#onNext(Object) */
	@Override
	public final void onNext(final AgentStatusUpdate update) {
		final AgentStatus<?> status = update.getStatus();
		if (this.destroyed)
			throw CoalaExceptionFactory.STATUS_UPDATE_FAILED.createRuntime(
					(Agent) null, status, "Eve wrapper " + getId()
							+ " ALREADY DESTROYED");

		if (status.isInitializedStatus()) {
			updateWrapperStatus(BasicAgentStatus.PASSIVE);
			// let Eve perform the activate() method, not this observer
			getScheduler().createTask(
					new JSONRequest("activate", JsonUtil.getJOM()
							.createObjectNode()), 0);
		} else if (status.isFinishedStatus() || status.isFailedStatus()) {
			updateWrapperStatus(BasicAgentStatus.COMPLETE);

			// let Eve perform the finish() method, not this observer
			getScheduler().createTask(
					new JSONRequest("finish", JsonUtil.getJOM()
							.createObjectNode()), 0);
		}
	}

	/** @see Observer#onCompleted() */
	@Override
	public final void onCompleted() {
		// TODO cleanup?
	}

	@Override
	public String getDescription() {
		return "The Eve Manifestation of " + getAgentID();
	}

	@Override
	public String getId() {
		return super.getId();
	}

	@Override
	public String getType() {
		return EveAgentManager.getInstance().getAgent(getAgentID(), true)
				.getClass().getSimpleName();
	}

	@Override
	public String getVersion() {
		return "$Id$";
	}

	@Override
	public final void onCreate() {
		updateWrapperStatus(BasicAgentStatus.CREATED);

		// get Eve container to run initialize() when the scheduler is running
		getScheduler().createTask(
				new JSONRequest("initialize", JsonUtil.getJOM()
						.createObjectNode()), 0);
	}

	@Access(AccessType.SELF)
	public final void initialize() {
		LOG.trace("Eve wrapper " + getId() + " now initializing...");

		configureLifeCycleHandling();

		// trigger initialization of wrapped agent
		updateWrapperStatus(BasicAgentStatus.INITIALIZED);
	}

	@Access(AccessType.SELF)
	public final void activate() {
		LOG.trace("Eve wrapper " + getId() + " now activating...");

		// trigger activation of wrapped agent
		updateWrapperStatus(BasicAgentStatus.ACTIVE);

		LOG.trace("Eve wrapper " + getId() + " now pausing...");

		// completed activation of wrapped agent
		updateWrapperStatus(BasicAgentStatus.PASSIVE);
	}

	private boolean destroyed = false;

	@Access(AccessType.SELF)
	public synchronized final void finish() {
		if (this.destroyed) {
			LOG.trace("Eve wrapper " + getId() + " ALREADY DESTROYED");
			return;
		}

		this.destroyed = true;
		LOG.trace("Eve wrapper " + getId() + " now finishing...");
		try {
			updateWrapperStatus(BasicAgentStatus.FINISHED);
			// FIXME causes IOException on (delayed) null-response to self:
			// EveUtil.getEveHost().deleteAgent(getId());
		} catch (final Throwable t) {
			LOG.error(String.format("Problem deleting"
					+ " Eve wrapper %s of agent: %s", getId(), getAgentID()), t);
			updateWrapperStatus(BasicAgentStatus.FAILED);
		}
	}

	@Override
	public final void onDelete() {
		super.onDelete();
		final AgentID agentID = getAgentID();
		final List<String> addresses = EveAgentManager.getInstance()
				.getAddress(agentID);
		LOG.info(String.format("Eve wrapper: %s of agent: %s "
				+ "went offline at: %s", getId(), agentID, addresses));
	}

	@Override
	public final void doSend(final Message<?> payload) throws IOException,
			JSONRPCException {
		final ObjectNode params = JsonUtil.getJOM().createObjectNode();
		params.set(PAYLOAD_FIELD_NAME, JsonUtil.getJOM().valueToTree(payload));
		send(EveUtil.getAddress(payload.getReceiverID()), "doReceive", params);
	}

	@Override
	public final void doReceive(final Message<?> payload) {
		// final ObjectNode params = JsonUtil.getJOM().createObjectNode();
		// params.set(PAYLOAD_FIELD_NAME,
		// JsonUtil.getJOM().valueToTree(payload));
		// getScheduler().createTask(new JSONRequest("receive", params), 0);
		// }
		//
		// @Access(AccessType.SELF)
		// protected void receive(@Name(PAYLOAD_FIELD_NAME) final Message<?, ?>
		// payload)
		// throws BAALException
		// {
		((MessageHandler) EveAgentManager.getInstance()
				.getAgent(getAgentID(), true).getBinder()
				.inject(ReceivingCapability.class)).onMessage(payload);
	}

	@Override
	public void setExposed(final Object exposed) {
		getState().put(NAMESPACE, exposed);
		// LOG.trace("Stored exposed object: " + exposed);
	}

	@Override
	public Object getExposed() {
		final Object result = getState().get(NAMESPACE, Object.class);
		// LOG.trace("Getting exposed object: " + result);
		return result;
	}

}
