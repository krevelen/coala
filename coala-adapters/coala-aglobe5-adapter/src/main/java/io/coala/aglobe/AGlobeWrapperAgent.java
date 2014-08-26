package io.coala.aglobe;

import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgentStatus;
import io.coala.capability.interact.ReceivingCapability;
import io.coala.log.LogUtil;

import org.apache.log4j.Logger;

import rx.Observer;
import aglobe.container.MessageHandler;
import aglobe.container.agent.Agent;
import aglobe.ontology.AgentInfo;
import aglobe.ontology.Message;

/**
 * {@link AGlobeWrapperAgent}
 * 
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AGlobeWrapperAgent extends Agent implements
		Observer<AgentStatusUpdate>
{

	/** */
	private static final long serialVersionUID = -1293178780584928667L;

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(AGlobeWrapperAgent.class);

	/** @return the {@link AgentID} identifier of the wrapped agent */
	protected final AgentID getAgentID()
	{
		return AGlobeUtil.toAgentID(getName());
	}

	/**
	 * @param status
	 */
	protected final void updateWrapperStatus(final BasicAgentStatus status)
	{
		final AgentID agentID = getAgentID();
		AGlobeAgentManager.getInstance().updateWrapperAgentStatus(agentID,
				status);
	}

	/**
	 * maps this {@link EveWrapperAgent}'s behavior to the wrapped {@link Agent}
	 * 's state
	 */
	protected final void configureLifeCycleHandling()
	{
		final AgentID agentID = getAgentID();
		LOG.info(String.format("AGlobe wrapper: %s of agent: %s "
				+ "went online at: %s", getName(), agentID, getAddress()));

		try
		{
			// AGlobeWrapperAgentFactory.getInstance().setAddress(getName(),
			// getAddress());

			AGlobeAgentManager.getInstance().getAgentStatus(agentID)
					.subscribe(this);
		} catch (final Throwable t)
		{
			LOG.error(String.format("Problem for wrapper %s getting "
					+ "status updates from agent %s", getName(), agentID), t);
		}
	}

	@Override
	public final void init(final AgentInfo a, final int initState)
	{
		updateWrapperStatus(BasicAgentStatus.CREATED);

		// get AGlobe container to run initialize() when scheduler is running
		scheduleEvent(new Runnable()
		{
			@Override
			public void run()
			{
				initialize();
			}
		}, 10);
	}

	/** @see Observer#onError(Throwable) */
	@Override
	public final void onError(final Throwable t)
	{
		t.printStackTrace();
	}

	/** @see Observer#onNext(Object) */
	@Override
	public final void onNext(final AgentStatusUpdate update)
	{
		final AgentStatus<?> status = update.getStatus();
		if (status.isInitializedStatus())
		{
			updateWrapperStatus(BasicAgentStatus.PASSIVE);
			// let AGlobe perform the activate() method, not this observer
			scheduleEvent(new Runnable()
			{
				@Override
				public void run()
				{
					activate();
				}
			}, 10);
		} else if (status.isFinishedStatus() || status.isFailedStatus())
		{
			updateWrapperStatus(BasicAgentStatus.COMPLETE);

			// let AGlobe perform the finish() method, not this observer
			scheduleEvent(new Runnable()
			{
				@Override
				public void run()
				{
					finish();
				}
			}, 10);
		}
	}

	/** @see Observer#onCompleted() */
	@Override
	public final void onCompleted()
	{
		// TODO cleanup?
	}

	public final void initialize()
	{
		LOG.trace("AGlobe wrapper " + getName() + " now initializing...");

		configureLifeCycleHandling();

		// trigger initialization of wrapped agent
		updateWrapperStatus(BasicAgentStatus.INITIALIZED);
	}

	public final void activate()
	{
		LOG.trace("AGlobe wrapper " + getName() + " now activating...");

		// trigger activation of wrapped agent
		updateWrapperStatus(BasicAgentStatus.ACTIVE);

		LOG.trace("AGlobe wrapper " + getName() + " now pausing...");

		// completed activation of wrapped agent
		updateWrapperStatus(BasicAgentStatus.PASSIVE);
	}

	/** @see MessageHandler#handleIncomingMessage(Message) */
	@Override
	public final void handleIncomingMessage(final Message envelope)
	{
		((io.coala.message.MessageHandler) AGlobeAgentManager
				.getInstance().getAgent(getAgentID(), true).getBinder()
				.inject(ReceivingCapability.class))
				.onMessage((io.coala.message.Message<?>) envelope
						.getContent());
	}

	@Override
	protected final void finish()
	{
		super.finish();
		LOG.trace("AGlobe wrapper " + getName() + " now finishing...");
		try
		{
			getContainer().getAgentManager().killAgent(getName());
			updateWrapperStatus(BasicAgentStatus.FINISHED);
		} catch (final Throwable t)
		{
			LOG.error(String.format("Problem deleting"
					+ " AGlobe wrapper %s of agent: %s", getName(),
					getAgentID()), t);
			updateWrapperStatus(BasicAgentStatus.FAILED);
		}
	}

	@Override
	protected final void sysFinish()
	{
		super.sysFinish();
		LOG.info(String.format("AGlobe wrapper: %s of agent: %s "
				+ "went offline at: %s", getName(), getAgentID(), getAddress()));
	}

}
