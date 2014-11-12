/* $Id: 826599c3081e734c02bdc5e95d186d2bc7c6dc47 $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/BasicService.java $
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
package io.coala.capability;

import io.coala.bind.Binder;
import io.coala.capability.configure.ConfiguringCapability;
import io.coala.config.PropertyGetter;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link BasicCapability}
 * 
 * @date $Date: 2014-06-20 12:27:58 +0200 (Fri, 20 Jun 2014) $
 * @version $Revision: 312 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class BasicCapability extends AbstractCapability<CapabilityID> implements
		Capability<BasicCapabilityStatus>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link BasicCapability} constructor
	 * 
	 * @param binder
	 */
	protected BasicCapability(final Binder binder)
	{
		this(null, binder);
		setID(new CapabilityID(binder.getID(), getClass()));
	}

	/**
	 * {@link BasicCapability} constructor
	 * 
	 * @param id
	 * @param binder
	 */
	protected BasicCapability(final CapabilityID id, final Binder binder)
	{
		super(id, binder);
	}

	/**
	 * helper method
	 * 
	 * @param key
	 * @return
	 */
	@JsonIgnore
	protected PropertyGetter getProperty(final String key)
	{
		return getBinder().inject(ConfiguringCapability.class).getProperty(key);
	}

}
