/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/random/RandomDistribution.java $
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

import java.io.Serializable;
import java.util.List;

/**
 * {@link RandomDistribution}
 * 
 * @version $Revision: 332 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface RandomDistribution<T> extends Serializable
{

	/**
	 * @return the next pseudo-random sample
	 */
	T draw();

	/**
	 * {@link Factory}
	 * 
	 * FIXME prefer Long over Integer for discrete dists, same as DSOL?
	 * 
	 * TODO add follwoing distribution types from DSOL? Bernoulli, Erlang,
	 * Pearson5, Pearson6, NegativeBionomial
	 * 
	 * @version $Revision: 332 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	interface Factory
	{

		/**
		 * @param constant the constant to be returned on each draw
		 * @return the {@link RandomDistribution}
		 */
		<T> RandomDistribution<T> getConstant(T constant);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param trials
		 * @param p
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getBinomial(RandomNumberStream rng,
				Number trials, Number p);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param probabilities the probability mass function enumerated for
		 *            each value
		 * @return the {@link RandomDistribution}
		 */
		<T> RandomDistribution<T> getEnumerated(RandomNumberStream rng,
				List<ProbabilityMass<T, Number>> probabilities);

		/**
		 * @param rng the {@link RandomNumberStream} 
		 * @param probabilities the probability mass function enumerated for
		 *            each value
		 * @return the {@link RandomDistribution}
		 */
		<N extends Number> RandomNumberDistribution<N> getEnumeratedNumber(
				RandomNumberStream rng,
				List<ProbabilityMass<N, Number>> probabilities);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param p
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getGeometric(RandomNumberStream rng,
				Number p);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param populationSize
		 * @param numberOfSuccesses
		 * @param sampleSize
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getHypergeometric(
				RandomNumberStream rng, Number populationSize,
				final Number numberOfSuccesses, final Number sampleSize);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param r
		 * @param p
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getPascal(RandomNumberStream rng,
				Number r, Number p);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param alpha
		 * @param beta
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getPoisson(RandomNumberStream rng,
				Number alpha, Number beta);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param lower
		 * @param upper
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getUniformInteger(
				RandomNumberStream rng, Number lower, Number upper);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param alpha
		 * @param beta
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Integer> getZipf(RandomNumberStream rng,
				Number numberOfElements, Number exponent);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param alpha
		 * @param beta
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getBeta(RandomNumberStream rng,
				Number alpha, Number beta);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param median
		 * @param scale
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getCauchy(RandomNumberStream rng,
				Number median, Number scale);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param degreesOfFreedom
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getChiSquared(RandomNumberStream rng,
				Number degreesOfFreedom);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param mean
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getExponential(RandomNumberStream rng,
				Number mean);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param numeratorDegreesOfFreedom
		 * @param denominatorDegreesOfFreedom
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getF(RandomNumberStream rng,
				Number numeratorDegreesOfFreedom,
				Number denominatorDegreesOfFreedom);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param shape
		 * @param scale
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getGamma(RandomNumberStream rng,
				Number shape, Number scale);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param mu
		 * @param c
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getLevy(RandomNumberStream rng,
				Number mu, Number c);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param scale
		 * @param shape
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getLogNormal(RandomNumberStream rng,
				Number scale, Number shape);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param mean
		 * @param sd the standard deviation
		 * @return the Gaussian / Normal {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getNormal(RandomNumberStream rng,
				Number mean, Number sd);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param scale
		 * @param shape
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getPareto(RandomNumberStream rng,
				Number scale, Number shape);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param denominatorDegreesOfFreedom
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getT(RandomNumberStream rng,
				Number denominatorDegreesOfFreedom);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param a
		 * @param b
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getTriangular(RandomNumberStream rng,
				Number a, Number b, Number c);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param lower
		 * @param upper
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getUniformReal(RandomNumberStream rng,
				Number lower, Number upper);

		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param alpha
		 * @param beta
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomNumberDistribution<Double> getWeibull(RandomNumberStream rng,
				Number alpha, Number beta);

		// TODO scrap or wrap (double <-> Number) using closures?
		/**
		 * @param rng the {@link RandomNumberStream}
		 * @param means
		 * @param covariances
		 * @return the {@link RandomNumberDistribution}
		 */
		RandomDistribution<double[]> getMultivariateNormal(
				RandomNumberStream rng, double[] means, double[][] covariances);
	}

}