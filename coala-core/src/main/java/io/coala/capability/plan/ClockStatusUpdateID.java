/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/ClockStatusUpdateID.java $
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
package io.coala.capability.plan;

import io.coala.name.AbstractIdentifier;

/**
 * {@link ClockStatusUpdateID}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class ClockStatusUpdateID extends AbstractIdentifier<Long>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static long UPDATE_COUNT = 0;

	/**
	 * {@link ClockStatusUpdateID} constructor
	 */
	protected ClockStatusUpdateID()
	{
		super(UPDATE_COUNT++);
	}

}
