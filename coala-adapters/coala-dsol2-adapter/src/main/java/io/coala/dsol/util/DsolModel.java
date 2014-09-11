/* $Id$
 * $URL: https://dev.almende.com/svn/abms/dsol-util/src/main/java/io/coala/dsol/util/DsolModel.java $
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/
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
package io.coala.dsol.util;

import nl.tudelft.simulation.dsol.ModelInterface;
import nl.tudelft.simulation.dsol.simulators.SimulatorInterface;

/**
 * {@link InertiaModelComponent}
 * 
 * @date $Date: 2014-05-07 11:59:26 +0200 (Wed, 07 May 2014) $
 * @version $Revision: 258 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <S> the type of {@link SimulatorInterface}
 * @param <THIS> the final type of {@link DsolModel}
 */
public interface DsolModel<S extends SimulatorInterface, THIS extends DsolModel<S, THIS>>
		extends DsolModelComponent<S, THIS>, ModelInterface
{

	/** should be called by {@link #constructModel(SimulatorInterface)} */
	void onInitialize() throws Exception;

}
