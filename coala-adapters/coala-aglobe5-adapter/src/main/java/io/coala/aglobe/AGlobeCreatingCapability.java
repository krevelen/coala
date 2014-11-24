package io.coala.aglobe;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.admin.CreatingCapability;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observable;

/**
 * {@link AGlobeCreatingCapability}
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AGlobeCreatingCapability extends BasicCapability implements CreatingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AGlobeCreatingCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private AGlobeCreatingCapability(final Binder binder)
	{
		super(binder);
	}

	@Override
	public Observable<AgentStatusUpdate> createAgent(String agentID)
	{
		return createAgent(agentID, null);
	}

	@Override
	public <A extends Agent> Observable<AgentStatusUpdate> createAgent(
			String agentID, Class<A> agentType) 
	{
		return AGlobeAgentManager.getInstance().boot(agentID, agentType);
	}

	@Override
	public Observable<AgentStatusUpdate> createAgent(final AgentID agentID)
	{
		return createAgent(agentID, null);
	}

	@Override
	public <A extends Agent> Observable<AgentStatusUpdate> createAgent(
			final AgentID agentID, final Class<A> agentType)
	{
		return AGlobeAgentManager.getInstance().boot(agentID, agentType);
	}

	@Override
	public Observable<AgentID> getChildIDs(boolean currentOnly)
	{
		return AGlobeAgentManager.getInstance().getChildIDs(getID().getOwnerID(), currentOnly);
	}

}