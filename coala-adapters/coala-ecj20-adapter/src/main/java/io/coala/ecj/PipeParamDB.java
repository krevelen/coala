/* $Id: PipeParamDB.java 20 2014-01-22 08:07:46Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/ecj-util/src/main/java/com/almende/train/ec/util/PipeParamDB.java $
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
package io.coala.ecj;

import java.util.Arrays;

import ec.BreedingPipeline;
import ec.BreedingSource;
import ec.SelectionMethod;

/**
 * {@link PipeParamDB}
 * 
 * @date $Date: 2014-01-22 09:07:46 +0100 (Wed, 22 Jan 2014) $
 * @version $Revision: 20 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class PipeParamDB extends AbstractParamDB<PipeParamDB>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private final Class<? extends BreedingSource> type;

	/**
	 * {@link PipeParamDB} constructor
	 * 
	 * @param type the type of {@link SelectionMethod} (pipeline leaf node)
	 */
	public PipeParamDB(final Class<? extends SelectionMethod> type)
	{
		this.type = type;
	}

	/**
	 * {@link PipeParamDB} constructor
	 * 
	 * @param type the type of {@link BreedingPipeline} (e.g. mutation)
	 */
	public PipeParamDB(final Class<? extends BreedingPipeline> type,
			final PipeParamDB... sources)
	{
		this.type = type;

		if (sources == null || sources.length == 0)
			throw new IllegalArgumentException("sources can't be empty");

		withChildren(Arrays.asList(sources), BreedingPipeline.P_SOURCE);
	}

	/** @see AbstractParamDB#getMainType() */
	@Override
	public Class<? extends BreedingSource> getMainType()
	{
		return this.type;
	}

}