/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/random/impl/RandomNumberStreamFactoryWell19937c.java $
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
package io.coala.random.impl;

import io.coala.random.RandomNumberStream;
import io.coala.random.RandomNumberStreamID;

import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/**
 * {@link RandomNumberStreamFactoryWell19937c} for {@link RandomNumberStream}s
 * wrapping the {@link Well19937c} implementation of a {@link RandomGenerator}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class RandomNumberStreamFactoryWell19937c implements
		RandomNumberStream.Factory
{

	@Override
	public RandomNumberStream create(final String id, final Number seed)
	{
		return create(new RandomNumberStreamID(id), seed);
	}

	@Override
	public RandomNumberStream create(final RandomNumberStreamID id,
			final Number seed)
	{
		final RandomGenerator rng = new Well19937c();
		rng.setSeed(seed.intValue());
		return RandomNumberStream.Util.asStream(id, rng);
	}
}