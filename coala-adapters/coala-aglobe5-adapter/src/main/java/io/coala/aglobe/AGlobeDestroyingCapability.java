package io.coala.aglobe;

import io.coala.agent.AgentID;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.admin.DestroyingCapability;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * {@link AGlobeDestroyingCapability}
 * 
 * @version $Revision: 302 $
 * @author <a href="mailto:Suki@almende.org">Suki</a>
 */
public class AGlobeDestroyingCapability extends BasicCapability implements
		DestroyingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	@InjectLogger
	private Logger LOG;

	@Inject
	private AGlobeDestroyingCapability(final Binder binder)
	{
		super(binder);
	}

	@Override
	public AgentID destroy()
	{
		return destroy(getID().getClientID());
	}

	@Override
	public AgentID destroy(final AgentID id)
	{
		AGlobeAgentManager.getInstance().updateWrapperAgentStatus(id,
				BasicAgentStatus.COMPLETE);
		return AGlobeUtil.killAgent(id);
	}

	/** @see com.almende.coala.service.finalizer.FinalizerService#terminate(com.almende.coala.service.ServiceID) */
	/*	@Override
		public <T extends ServiceID> T terminate(AbstractService<T> service)
				throws Exception
		{
			final BasicServiceStatus status = BasicServiceStatus
					.determineKillStatus(service);
			final T serviceID = service.getID();
			MachineUtil.setStatus(service, status, status.isFinishedStatus()
					|| status.isFailedStatus());
			return serviceID;
		}
	*/
}
