package io.coala.eve3;

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.interact.SendingCapability;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.InjectLogger;
import io.coala.message.Message;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link AGlobeMessengerService}
 * 
 * @version $Revision: 277 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class EveSendingCapability extends BasicCapability implements
		SendingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AGlobeMessengerService} constructor
	 * 
	 * @param binder
	 */
	@Inject
	public EveSendingCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see SendingCapability#send(Message) */
	@SuppressWarnings("deprecation")
	@Override
	public void send(final Message<?> msg)
	{
		LOG.trace("Sending "+msg);
		if (msg.getReceiverID() == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.createRuntime(
					"receiverID", msg.toString());
		EveUtil.receiveMessageByPointer(msg);
	}

}
