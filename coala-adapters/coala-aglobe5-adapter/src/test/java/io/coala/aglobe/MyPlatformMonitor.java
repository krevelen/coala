/* $Id: MyPlatformMonitor.java 240 2014-04-18 15:39:26Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/aglobe-util/src/test/java/com/almende/coala/aglobe/MyPlatformMonitor.java $
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
package io.coala.aglobe;

import io.coala.log.LogUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;

import aglobe.platform.PlatformMonitor;
import aglobe.platform.transport.MessageTransport;

/**
 * {@link MyPlatformMonitor} helper class
 * 
 * @date $Date: 2014-04-18 17:39:26 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 240 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class MyPlatformMonitor implements PlatformMonitor
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(MyPlatformMonitor.class);

	/** */
	private PlatformState currentState = null;

	/** @return the current {@link PlatformState} */
	public PlatformState getState()
	{
		return this.currentState;
	}

	@Override
	public synchronized void platformStateChanged(
			final PlatformState currentState)
	{
		LOG.trace("Platform state changed to: " + currentState.name());
		this.currentState = currentState;
		notifyAll();
	}

	/** */
	public void waitForState(final PlatformState... states)
	{
		final List<PlatformState> list;
		if (states == null)
			list = Collections.emptyList();
		else
			list = Arrays.asList(states);
		while (MessageTransport.localAddress == null
				|| !list.contains(this.currentState))
		{
			LOG.trace("Waiting for state " + list + ", currently: "
					+ this.currentState);
			try
			{
				synchronized (this)
				{
					wait(1000);
				}
			} catch (final InterruptedException ignore)
			{
				//
			}
		}
	}
}