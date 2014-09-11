/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/time/ClockID.java $
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
package io.coala.time;

import io.coala.model.ModelComponentID;
import io.coala.model.ModelID;

import javax.inject.Inject;
import javax.persistence.Embeddable;

/**
 * {@link ClockID} contains a value to identify some {@link Clock}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@Embeddable
public class ClockID extends ModelComponentID<String>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param value
	 */
	protected ClockID()
	{
		this(null, null);
	}

	/**
	 * @param value
	 */
	@Inject
	public ClockID(final ModelID modelID, final String value)
	{
		super(modelID, value);
	}
	
	@Override // expose for (JSON) serialization
	public String getValue()
	{
		return super.getValue();
	}

}
