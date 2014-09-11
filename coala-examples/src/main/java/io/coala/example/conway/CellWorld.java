/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/conway/CellWorld.java $
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
package io.coala.example.conway;

import io.coala.capability.CapabilityFactory;
import io.coala.capability.embody.GroundingCapability;
import rx.Observable;

/**
 * {@link CellWorld}
 * 
 * @date $Date: 2014-06-03 13:55:16 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public interface CellWorld extends GroundingCapability// <CellWorld>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 295 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	interface Factory extends CapabilityFactory<CellWorld>
	{
		// empty
	}

	/** @return the neighboring {@link Cell}s' {@link CellID}s */
	Observable<CellLinkPercept> perceiveLinks();

	/**
	 * @return the actual {@link CellStateTransition} (incl. execution time)
	 *         that the {@link CellWorld} determined for this {@link Cell}
	 */
	Observable<CellStateTransition> perceiveTransitions();

	/**
	 * @param toState the {@link Cell}'s new {@link LifeState}
	 * @return {@code true} if transition succeeded, {@code false} otherwise
	 * @throws Exception if transition failed, e.g. timeout?
	 */
	void performTransition(final LifeState toState) throws Exception;

}
