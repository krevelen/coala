/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/random/impl/RandomDistributionFactoryImpl.java $
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

import io.coala.random.ProbabilityMass;
import io.coala.random.RandomDistribution;
import io.coala.random.RandomNumberDistribution;
import io.coala.random.RandomNumberStream;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.math3.distribution.BetaDistribution;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.CauchyDistribution;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;
import org.apache.commons.math3.distribution.EnumeratedDistribution;
import org.apache.commons.math3.distribution.ExponentialDistribution;
import org.apache.commons.math3.distribution.FDistribution;
import org.apache.commons.math3.distribution.GammaDistribution;
import org.apache.commons.math3.distribution.GeometricDistribution;
import org.apache.commons.math3.distribution.HypergeometricDistribution;
import org.apache.commons.math3.distribution.IntegerDistribution;
import org.apache.commons.math3.distribution.LevyDistribution;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateNormalDistribution;
import org.apache.commons.math3.distribution.MultivariateRealDistribution;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.apache.commons.math3.distribution.ParetoDistribution;
import org.apache.commons.math3.distribution.PascalDistribution;
import org.apache.commons.math3.distribution.RealDistribution;
import org.apache.commons.math3.distribution.TDistribution;
import org.apache.commons.math3.distribution.TriangularDistribution;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.distribution.UniformRealDistribution;
import org.apache.commons.math3.distribution.WeibullDistribution;
import org.apache.commons.math3.distribution.ZipfDistribution;
import org.apache.commons.math3.util.Pair;

/**
 * {@link RandomDistributionFactoryImpl} creates {@link RandomDistribution}s
 * implemented by Apache's commons-math3
 * 
 * @version $Revision: 332 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
@SuppressWarnings("serial")
public class RandomDistributionFactoryImpl implements
		RandomDistribution.Factory
{
	@Override
	public <T> RandomDistribution<T> getConstant(final T constant)
	{
		return new RandomDistribution<T>()
		{
			@Override
			public T draw()
			{
				return constant;
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getBinomial(
			final RandomNumberStream rng, final Number trials, final Number p)
	{
		final IntegerDistribution dist = new BinomialDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				trials.intValue(), p.doubleValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public <T> RandomDistribution<T> getEnumerated(
			final RandomNumberStream rng,
			final List<ProbabilityMass<T, Number>> probabilities)
	{
		final List<Pair<T, Double>> pmf = new ArrayList<>();
		if (probabilities != null)
			for (ProbabilityMass<T, Number> p : probabilities)
				pmf.add(Pair.create(p.getValue(), p.getMass().doubleValue()));
		final EnumeratedDistribution<T> dist = new EnumeratedDistribution<T>(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng), pmf);
		return new RandomDistribution<T>()
		{
			@Override
			public T draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public <N extends Number> RandomNumberDistribution<N> getEnumeratedNumber(
			final RandomNumberStream rng,
			final List<ProbabilityMass<N, Number>> probabilities)
	{
		return (RandomNumberDistribution<N>) getEnumerated(rng, probabilities);
	}

	@Override
	public RandomNumberDistribution<Integer> getGeometric(
			final RandomNumberStream rng, final Number p)
	{
		final IntegerDistribution dist = new GeometricDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				p.doubleValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getHypergeometric(
			final RandomNumberStream rng, final Number populationSize,
			final Number numberOfSuccesses, final Number sampleSize)
	{
		final IntegerDistribution dist = new HypergeometricDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				populationSize.intValue(), numberOfSuccesses.intValue(),
				sampleSize.intValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getPascal(
			final RandomNumberStream rng, final Number r, final Number p)
	{
		final IntegerDistribution dist = new PascalDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				r.intValue(), p.doubleValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getPoisson(
			final RandomNumberStream rng, final Number alpha, final Number beta)
	{
		final IntegerDistribution dist = new BinomialDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				alpha.intValue(), beta.doubleValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getUniformInteger(
			final RandomNumberStream rng, final Number lower, final Number upper)
	{
		final IntegerDistribution dist = new UniformIntegerDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				lower.intValue(), upper.intValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Integer> getZipf(
			final RandomNumberStream rng, final Number numberOfElements,
			final Number exponent)
	{
		final IntegerDistribution dist = new ZipfDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				numberOfElements.intValue(), exponent.doubleValue());
		return new RandomNumberDistribution<Integer>()
		{
			@Override
			public Integer draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getBeta(
			final RandomNumberStream rng, final Number alpha, final Number beta)
	{
		final RealDistribution dist = new BetaDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				alpha.doubleValue(), beta.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getCauchy(
			final RandomNumberStream rng, final Number median,
			final Number scale)
	{
		final RealDistribution dist = new CauchyDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				median.doubleValue(), scale.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getChiSquared(
			final RandomNumberStream rng, final Number degreesOfFreedom)
	{
		final RealDistribution dist = new ChiSquaredDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				degreesOfFreedom.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getExponential(
			final RandomNumberStream rng, final Number mean)
	{
		final RealDistribution dist = new ExponentialDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				mean.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getF(final RandomNumberStream rng,
			final Number numeratorDegreesOfFreedom,
			final Number denominatorDegreesOfFreedom)
	{
		final RealDistribution dist = new FDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				numeratorDegreesOfFreedom.doubleValue(),
				denominatorDegreesOfFreedom.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getGamma(
			final RandomNumberStream rng, final Number shape, final Number scale)
	{
		final RealDistribution dist = new GammaDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				shape.doubleValue(), scale.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getLevy(
			final RandomNumberStream rng, final Number mu, final Number c)
	{
		final RealDistribution dist = new LevyDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				mu.doubleValue(), c.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getLogNormal(
			final RandomNumberStream rng, final Number scale, final Number shape)
	{
		final RealDistribution dist = new LogNormalDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				scale.doubleValue(), shape.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getNormal(
			final RandomNumberStream rng, final Number mean, final Number sd)
	{
		final RealDistribution dist = new NormalDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				mean.doubleValue(), sd.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getPareto(
			final RandomNumberStream rng, final Number scale, final Number shape)
	{
		final RealDistribution dist = new ParetoDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				scale.doubleValue(), shape.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getT(final RandomNumberStream rng,
			final Number degreesOfFreedom)
	{
		final RealDistribution dist = new TDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				degreesOfFreedom.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getTriangular(
			final RandomNumberStream rng, final Number a, final Number b,
			final Number c)
	{
		final RealDistribution dist = new TriangularDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				a.doubleValue(), b.doubleValue(), c.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getUniformReal(
			final RandomNumberStream rng, final Number lower, final Number upper)
	{
		final RealDistribution dist = new UniformRealDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				lower.doubleValue(), upper.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomNumberDistribution<Double> getWeibull(
			final RandomNumberStream rng, final Number alpha, final Number beta)
	{
		final RealDistribution dist = new WeibullDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng),
				alpha.doubleValue(), beta.doubleValue());
		return new RandomNumberDistribution<Double>()
		{
			@Override
			public Double draw()
			{
				return dist.sample();
			}
		};
	}

	@Override
	public RandomDistribution<double[]> getMultivariateNormal(
			final RandomNumberStream rng, final double[] means,
			final double[][] covariances)
	{
		final MultivariateRealDistribution dist = new MultivariateNormalDistribution(
				RandomNumberStream.Util.asCommonsRandomGenerator(rng), means,
				covariances);
		return new RandomDistribution<double[]>()
		{
			@Override
			public double[] draw()
			{
				return dist.sample();
			}
		};
	}
}