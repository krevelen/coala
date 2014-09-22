/* $Id$
 * $URL: https://dev.almende.com/svn/abms/eve-util/src/main/java/com/almende/coala/eve/EveBooterService.java $
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
package io.coala.eve;

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.interact.ExposingCapability;
import io.coala.log.InjectLogger;

import java.io.Serializable;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * {@link EveExposingCapability}
 * 
 * @date $Date: 2014-08-08 16:20:51 +0200 (Fri, 08 Aug 2014) $
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class EveExposingCapability extends BasicCapability implements
		ExposingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link EveExposingCapability} constructor
	 * 
	 * @param clientID
	 */
	@Inject
	private EveExposingCapability(final Binder binder)
	{
		super(binder);
	}

	@Override
	public <T extends Serializable> void expose(final Class<T> api, final T implementation)
	{
		try
		{
			EveAgentManager.getInstance(getBinder()).setExposed(
					getID().getClientID(), implementation);
			LOG.trace("Exposed object: " + implementation);
		} catch (final Exception e)
		{
			LOG.error("Problem exposing object: " + implementation, e);
		}
	}
}
