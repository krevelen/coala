package io.coala.eve3;

import io.coala.message.Message;

import java.io.IOException;

import com.almende.eve.protocol.jsonrpc.formats.JSONRPCException;

/**
 * {@link EveSenderAgent}
 * 
 * @date $Date: 2014-04-18 16:38:34 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 235 $
 * @author <a href="mailto:suki@almende.org">suki</a>
 * 
 */
public interface EveSenderAgent extends EveWrapper
{

	/**
	 * @param payload
	 * @throws JSONRPCException
	 * @throws IOException
	 */
	void doSend(Message<?> payload) throws IOException, JSONRPCException;

}
