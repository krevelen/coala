/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/model/BasicModelComponentIDFactory.java $
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
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.model;

import io.coala.agent.AgentID;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.time.ClockID;

/**
 * {@link BasicModelComponentIDFactory}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class BasicModelComponentIDFactory implements ModelComponentIDFactory
{

	/** */
	private ModelID modelID;

	/**
	 * {@link BasicModelComponentIDFactory} constructor
	 */
	public BasicModelComponentIDFactory()
	{
		// empty
	}

	/** @see ModelComponentIDFactory#getModelID() */
	@Override
	public ModelID getModelID()
	{
		return this.modelID;
	}

	/**
	 * @return
	 */
	protected void checkInitialized(final boolean notNull)
	{
		if (notNull && getModelID() == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.createRuntime("modelID");
		// else if (!expected && getModelID() != null)
		// LOG.warn("MODEL ID ALREADY INITIALIZED");
	}

	@Override
	public ModelComponentIDFactory initialize(final ModelID modelID)
	{
		// checkInitialized(false);
		// new
		// IllegalStateException("Setting modelID: "+modelID).printStackTrace();
		this.modelID = modelID;
		return this;
	}

	@Override
	public AgentID createAgentID(final String value)
	{
		checkInitialized(true);
		final String modelPrefix = getModelID().getValue()
				+ ModelComponentID.PATH_SEP;
		if (value.startsWith(modelPrefix))
			return new AgentID(getModelID(), value.substring(modelPrefix
					.length()));
		return new AgentID(getModelID(), value);
	}

	@Override
	public ClockID createClockID(final String value)
	{
		checkInitialized(true);
		return new ClockID(getModelID(), value == null ? getModelID()
				.getValue() : value);
	}

}
