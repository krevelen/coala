/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/com/almende/dsol/example/datacenters/AbstractFederationModelComponent.java $
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
package com.almende.dsol.example.datacenters;

import io.coala.dsol.util.AbstractDsolModelComponent;
import nl.tudelft.simulation.dsol.simulators.DEVSSimulatorInterface;

/**
 * {@link AbstractFederationModelComponent}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class AbstractFederationModelComponent extends
		AbstractDsolModelComponent<DEVSSimulatorInterface, FederationModel>
		implements FederationModelComponent
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link AbstractFederationModelComponent} constructor
	 * 
	 * @param model
	 * @param name
	 */
	public AbstractFederationModelComponent(final FederationModel model,
			final String name)
	{
		super(model, name);
	}

}
