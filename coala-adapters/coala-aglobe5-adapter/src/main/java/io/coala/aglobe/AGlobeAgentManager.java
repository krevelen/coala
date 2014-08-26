package io.coala.aglobe;

import io.coala.agent.AbstractAgentManager;
import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatus;
import io.coala.bind.BinderFactory;
import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;

import org.apache.log4j.Logger;

import rx.functions.Action1;
import aglobe.platform.PlatformMonitor.PlatformState;

/**
 * {@link AGlobeAgentManager}
 * 
 * @version $Revision: 299 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AGlobeAgentManager extends AbstractAgentManager
{
	/** */
	private static final Logger LOG = LogUtil
			.getLogger(AGlobeAgentManager.class);

	/** */
	private static AGlobeAgentManager INSTANCE = null;

	static
	{
		AGlobeUtil.getPlatformStates().subscribe(new Action1<PlatformState>()
		{
			@Override
			public void call(final PlatformState state)
			{
				LOG.trace("State of AGlobe platform is now: " + state);
			}
		});
		AGlobeUtil.startPlatform();
	}

	/**
	 * @return the singleton {@link AGlobeAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static AGlobeAgentManager getInstance()

	{
		if (INSTANCE == null)
			return getInstance((String) null);

		return INSTANCE;
	}

	/**
	 * @param configPath or {@code null} for default config
	 * @return the singleton {@link AGlobeAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static AGlobeAgentManager getInstance(
			final String configPath)
	{
		try
		{
			return getInstance(BinderFactory.Builder.fromFile(configPath));
		} catch (final CoalaException e)
		{
			throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime(e,
					"configPath", configPath);
		}
	}

	/**
	 * @param binderFactoryBuilder or {@code null} for default config
	 * @return the singleton {@link AGlobeAgentManager}
	 * @throws CoalaException
	 */
	public synchronized static AGlobeAgentManager getInstance(
			final BinderFactory.Builder binderFactoryBuilder)
	{
		if (INSTANCE == null)
			INSTANCE = new AGlobeAgentManager(binderFactoryBuilder);

		return INSTANCE;
	}

	/**
	 * {@link AGlobeAgentManager} constructor
	 * 
	 * @param binderFactoryBuilder
	 */
	private AGlobeAgentManager(final BinderFactory.Builder binderFactoryBuilder)
	{
		super(binderFactoryBuilder);
	}

	/** @see AbstractAgentManager#setInitialized(AgentID) */
	@Override
	public void updateWrapperAgentStatus(final AgentID agentID,
			final AgentStatus<?> status)
	{
		super.updateWrapperAgentStatus(agentID, status);
	}

	/** @see AbstractAgentManager#boot(AgentID) */
	@Override
	protected AgentID boot(final Agent agent) throws CoalaException
	{
		final AgentID agentID = agent.getID();
		if (AGlobeUtil.isWrapperAgentCreated(agentID))
		{
			LOG.warn("Agent already created: " + agentID);
			return agentID;
		}
		LOG.trace("Creating wrapper agent for " + agentID + "...");
		try
		{
			AGlobeUtil.createAgent(agentID, AGlobeWrapperAgent.class);

			return agentID;
		} catch (final CoalaException e)
		{
			throw e;
		} catch (final Exception e)
		{
			throw CoalaExceptionFactory.AGENT_CREATION_FAILED
					.create(e, agentID);
		}
	}

	/** @see AbstractAgentManager#shutdown() */
	@Override
	protected void shutdown()
	{
		// AGlobe host is destroyed/cleaned up automatically
	}

	/**
	 * @param agentID
	 * @return
	 */
	public io.coala.agent.Agent getAgent(final AgentID agentID, final boolean block)
	{
		return get(agentID, block);
	}

}
