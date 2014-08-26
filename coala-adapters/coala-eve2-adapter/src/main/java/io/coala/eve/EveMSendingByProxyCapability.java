package io.coala.eve;

import io.coala.bind.Binder;
import io.coala.capability.interact.SendingCapability;
import io.coala.log.InjectLogger;
import io.coala.message.Message;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * {@link AGlobeMessengerService}
 * 
 * @version $Revision: 248 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class EveMSendingByProxyCapability extends EveSendingCapability
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
	public EveMSendingByProxyCapability(final Binder binder)
	{
		super(binder);
	}

	/** @see SendingCapability#send(Message) */
	@Override
	public void send(final Message<?> msg)
	{
		EveUtil.receiveMessageByProxy(msg);
	}

}
