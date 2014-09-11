/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/TimedRemoteProcedureCall.java $
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
package io.coala.invoke;

import io.coala.agent.AgentID;
import io.coala.event.TimedEventID;
import io.coala.message.Message;
import io.coala.message.MessageID;

/**
 * {@link TimedRemoteProcedureCall}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link TimedEventID} for time-ordered event identity
 * @param <A_ID> the type of {@link AgentID} for sender/receiver identification
 * @param <THIS> the (sub)type of {@link TimedRemoteProcedureCall} to build
 */
public abstract class TimedRemoteProcedureCall<ID extends MessageID<?, ?>>
		extends TimedProcedureCall<ID> implements Message<ID>
{

	/** */
	private static final long serialVersionUID = 1L;

}
