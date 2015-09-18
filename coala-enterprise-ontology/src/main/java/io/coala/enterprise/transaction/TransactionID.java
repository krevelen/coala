/* $Id: 44c1e4ed770da5acd337833ee8f5dad61f84d561 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/transaction/TransactionID.java $
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
package io.coala.enterprise.transaction;

import io.coala.model.ModelComponentID;
import io.coala.model.ModelID;

import com.eaio.uuid.UUID;

/**
 * {@link TransactionID}
 * 
 * @version $Revision: 290 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class TransactionID extends ModelComponentID<UUID>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link TransactionID} constructor
	 * 
	 * @param modelID
	 */
	public TransactionID(final ModelID modelID)
	{
		super(modelID, new UUID());
	}

	/**
	 * {@link TransactionID} zero-arg bean constructor
	 */
	protected TransactionID()
	{
		super();
	}

}
