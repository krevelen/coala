package io.coala.eve;

import io.coala.agent.Agent;
import io.coala.agent.AgentID;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.admin.DestroyingCapability;
import io.coala.lifecycle.MachineUtil;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link EveDestroyingCapability}
 * 
 * @date $Date: 2014-06-17 15:03:44 +0200 (Tue, 17 Jun 2014) $
 * @version $Revision: 302 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class EveDestroyingCapability extends BasicCapability implements
		DestroyingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link EveDestroyingCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private EveDestroyingCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see DestroyingCapability#destroy(AgentID) */
	@Override
	public AgentID destroy()
	{
		return destroy(getID().getOwnerID());
	}

	/** @see DestroyingCapability#destroy(AgentID) */
	@Override
	public AgentID destroy(final AgentID agentID)
	{
		final Agent agent = EveAgentManager.getInstance().getAgent(agentID,
				false);

		if (agent == null)
		{
			LOG.warn("Could not kill agent, not available in this VM");
			return agentID;
		}

		final BasicAgentStatus status = BasicAgentStatus
				.determineKillStatus(agent);
		LOG.info("Going for the kill: " + status);
		MachineUtil.setStatus(agent, status, status.isFinishedStatus()
				|| status.isFailedStatus());

		return agentID;
	}

	/** @see com.almende.coala.service.finalizer.FinalizerService#terminate(com.almende.coala.service.ServiceID) */
	/*@Override
	public <T extends ServiceID> T terminate(AbstractService<T> service) throws Exception
	{
		final BasicServiceStatus status = BasicServiceStatus.
				determineKillStatus(service);
		final T serviceID = service.getID();
		LOG.info("terminating service:"+serviceID);
		MachineUtil.setStatus(service, status, status.isFinishedStatus()
				|| status.isFailedStatus());
		
		return serviceID;
	}*/

}
