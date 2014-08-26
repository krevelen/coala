package io.coala.aglobe;

import io.coala.agent.AgentID;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.interact.SendingCapability;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.InjectLogger;
import io.coala.message.Message;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import aglobe.container.transport.InvisibleContainerException;

/**
 * {@link AGlobeSendingCapability}
 * 
 * @version $Revision: 248 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class AGlobeSendingCapability extends BasicCapability implements
		SendingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AGlobeSendingCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private AGlobeSendingCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see SendingCapability#send(Message) */
	@Override
	public void send(final Message<?> payload)
	{
		final AgentID senderID = payload.getSenderID();
		final AGlobeWrapperAgent sender = AGlobeUtil.getAgent(senderID,
				AGlobeWrapperAgent.class);
		final aglobe.ontology.Message envelope = AGlobeUtil.createMessage(
				payload, payload.getSenderID(), payload.getReceiverID());
		LOG.trace("Sending: " + envelope);
		try
		{
			sender.sendMessage(envelope);
		} catch (final InvisibleContainerException e)
		{
			throw CoalaExceptionFactory.AGENT_UNAVAILABLE
					.createRuntime(senderID);
		}
	}

}
