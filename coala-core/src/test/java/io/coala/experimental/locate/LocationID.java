/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/location/LocationID.java $
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
package io.coala.experimental.locate;

import io.coala.name.AbstractIdentifier;

import java.io.Serializable;

/**
 * {@link LocationID} fixes the identifier type of {@link Location} to
 * {@link String}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T> the type of value for this {@link AbstractIdentifier}
 */
public class LocationID<T extends Serializable & Comparable<T>> extends
		AbstractIdentifier<T>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param value
	 */
	public LocationID(final T value)
	{
		super(value);
	}

}
