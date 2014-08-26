/* $Id: AuthorizationID.java 295 2014-06-03 11:55:16Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/deliver/AuthorizationID.java $
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
package io.coala.example.deliver;

import io.coala.model.ModelComponentID;
import io.coala.model.ModelID;

/**
 * {@link AuthorizationID}
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class AuthorizationID extends ModelComponentID<String>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	public String dealCompanyKey = null;

	/**
	 * {@link AuthorizationID} zero-arg bean constructor
	 */
	protected AuthorizationID()
	{
	}

	/**
	 * {@link AuthorizationID} constructor
	 * 
	 * @param companyID
	 * @param dealCompanyKey
	 * @param authorizationToken
	 */
	public AuthorizationID(final ModelID companyID,
			final String dealCompanyKey, final String authorizationToken)
	{
		super(companyID, authorizationToken);
		this.dealCompanyKey = dealCompanyKey;
	}
}