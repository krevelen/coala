/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/random/RandomNumberStream.java $
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
package io.coala.random;

import io.coala.name.AbstractIdentifiable;
import io.coala.name.Identifiable;

import org.apache.commons.math3.random.RandomGenerator;

/**
 * {@link RandomNumberStream}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface RandomNumberStream extends Identifiable<RandomNumberStreamID>
{

	boolean nextBoolean();

	void nextBytes(byte[] bytes);

	/**
	 * @return the next pseudo-random int
	 */
	int nextInt();

	/**
	 * @param n
	 * @return the next pseudo-random {@link int} between 0 and {@code n}
	 */
	int nextInt(int n);

	/**
	 * @return the next pseudo-random int
	 */
	// int nextInt(int min, int max);

	long nextLong();

	float nextFloat();

	double nextDouble();

	double nextGaussian();

	/**
	 * {@link RandomNumberStreamFactory}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	interface Factory
	{
		/**
		 * @param id
		 * @param seed
		 * @return
		 */
		RandomNumberStream create(String id, Number seed);

		/**
		 * @param id
		 * @param seed
		 * @return
		 */
		RandomNumberStream create(RandomNumberStreamID id, Number seed);

	}

	/**
	 * {@link Util}
	 * 
	 * @version $Revision: 324 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	class Util implements io.coala.util.Util
	{
		private Util()
		{
			//
		}

		/**
		 * {@link AbstractRandomNumberStream}
		 * 
		 * @version $Revision: 324 $
		 * @author <a href="mailto:Rick@almende.org">Rick</a>
		 *
		 */
		protected static abstract class AbstractRandomNumberStream extends
				AbstractIdentifiable<RandomNumberStreamID> implements
				RandomNumberStream
		{

			/** */
			private static final long serialVersionUID = 1L;

			/**
			 * {@link AbstractRandomNumberStream} zero-arg bean constructor
			 */
			protected AbstractRandomNumberStream()
			{
				//
			}

			/**
			 * {@link AbstractRandomNumberStream} zero-arg bean constructor
			 */
			public AbstractRandomNumberStream(final RandomNumberStreamID id)
			{
				super(id);
			}

		}

		@SuppressWarnings("serial")
		public static RandomNumberStream asStream(
				final RandomNumberStreamID id, final RandomGenerator rng)
		{
			return new AbstractRandomNumberStream(id)
			{
				@Override
				public boolean nextBoolean()
				{
					return rng.nextBoolean();
				}

				@Override
				public void nextBytes(byte[] bytes)
				{
					rng.nextBytes(bytes);
				}

				@Override
				public int nextInt()
				{
					return rng.nextInt();
				}

				@Override
				public int nextInt(int n)
				{
					return rng.nextInt(n);
				}

				@Override
				public long nextLong()
				{
					return rng.nextLong();
				}

				@Override
				public float nextFloat()
				{
					return rng.nextFloat();
				}

				@Override
				public double nextDouble()
				{
					return rng.nextDouble();
				}

				@Override
				public double nextGaussian()
				{
					return rng.nextGaussian();
				}
			};
		}

		public static RandomGenerator asCommonsRandomGenerator(
				final RandomNumberStream rng)
		{
			return new RandomGenerator()
			{

				@Override
				public void setSeed(final int seed)
				{
					throw new IllegalAccessError("Seed is managed elsewhere");
				}

				@Override
				public void setSeed(final int[] seed)
				{
					throw new IllegalAccessError("Seed is managed elsewhere");
				}

				@Override
				public void setSeed(final long seed)
				{
					throw new IllegalAccessError("Seed is managed elsewhere");
				}

				@Override
				public void nextBytes(final byte[] bytes)
				{
					rng.nextBytes(bytes);
				}

				@Override
				public int nextInt()
				{
					return rng.nextInt();
				}

				@Override
				public int nextInt(final int n)
				{
					return rng.nextInt(n);
				}

				@Override
				public long nextLong()
				{
					return rng.nextLong();
				}

				@Override
				public boolean nextBoolean()
				{
					return rng.nextBoolean();
				}

				@Override
				public float nextFloat()
				{
					return rng.nextFloat();
				}

				@Override
				public double nextDouble()
				{
					return rng.nextDouble();
				}

				@Override
				public double nextGaussian()
				{
					return rng.nextGaussian();
				}
			};
		}
	}

}