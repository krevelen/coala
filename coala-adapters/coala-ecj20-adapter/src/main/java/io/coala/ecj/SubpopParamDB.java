/* $Id$
 * $URL: https://dev.almende.com/svn/abms/ecj-util/src/main/java/com/almende/train/ec/util/SubpopParamDB.java $
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

import ec.Breeder;
import ec.BreedingPipeline;
import ec.EvolutionState;
import ec.Fitness;
import ec.Individual;
import ec.Species;
import ec.Subpopulation;
import ec.vector.IntegerVectorSpecies;
import ec.vector.VectorSpecies;

/**
 * {@link SubpopParamDB}
 * 
 * @date $Date: 2014-01-22 09:07:46 +0100 (Wed, 22 Jan 2014) $
 * @version $Revision: 20 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class SubpopParamDB extends AbstractParamDB<SubpopParamDB>
{
	/** */
	private static final long serialVersionUID = 1L;

	public enum VectorCrossoverType
	{

		/** */
		ONE_POINT(VectorSpecies.V_ONE_POINT),

		/** */
		TWO_POINT(VectorSpecies.V_TWO_POINT),

		/** */
		ANY_POINT(VectorSpecies.V_ANY_POINT),

		/** */
		LINE_RECOMB(VectorSpecies.V_LINE_RECOMB),

		/** */
		INTERMED_RECOMB(VectorSpecies.V_INTERMED_RECOMB),

		/** */
		SIMULATED_BINARY(VectorSpecies.V_SIMULATED_BINARY);

		/** */
		public final String value;

		/** */
		private VectorCrossoverType(final String value)
		{
			this.value = value;
		}
	}

	/** */
	private final Class<? extends Subpopulation> type;

	/**
	 * {@link SubpopParamDB} constructor
	 * 
	 * @param type the type of {@link Breeder}
	 */
	public SubpopParamDB(final Class<? extends Subpopulation> type)
	{
		this.type = type;
	}

	/** @see AbstractParamDB#getMainType() */
	@Override
	public Class<? extends Subpopulation> getMainType()
	{
		return this.type;
	}

	/**
	 * @param type the type of {@link Breeder}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withBreeder(final Class<? extends Breeder> type)
	{
		return with(EvolutionState.P_BREEDER, type);
	}

	/**
	 * @param size the number of individuals in this {@link Subpopulation}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withSize(final int size)
	{
		return with(Subpopulation.P_SUBPOPSIZE, size);
	}

	/**
	 * @param retries the number of duplicate retries in this
	 *        {@link Subpopulation}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withRetries(final int retries)
	{
		return with(Subpopulation.P_RETRIES, retries);
	}

	/**
	 * @param type the type of {@link Species} in this {@link Subpopulation}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withSpecies(final Class<? extends Species> type)
	{
		return with(Subpopulation.P_SPECIES, type);
	}

	/**
	 * @param type the type of {@link VectorSpecies} in this
	 *        {@link Subpopulation}
	 * @param genomeSize the genome size
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withVectorSpecies(
			final Class<? extends VectorSpecies> type, final int genomeSize)
	{
		return withSpecies(type).with(
				new String[] { Subpopulation.P_SPECIES,
						VectorSpecies.P_GENOMESIZE }, genomeSize);
	}

	/**
	 * @param type the type of {@link IntegerVectorSpecies} in this
	 *        {@link Subpopulation}
	 * @param genomeSize the genome size
	 * @param minGene the minimum gene value
	 * @param maxGene the maximum gene value
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withIntegerVectorSpecies(
			final Class<? extends IntegerVectorSpecies> type,
			final int genomeSize, final int minGene, final int maxGene)
	{
		return withVectorSpecies(type, genomeSize).with(
				new String[] { Subpopulation.P_SPECIES,
						IntegerVectorSpecies.P_MINGENE }, minGene).with(
				new String[] { Subpopulation.P_SPECIES,
						IntegerVectorSpecies.P_MAXGENE }, maxGene);
	}

	/**
	 * @param probability the mutation probability
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withMutation(final double probability)
	{
		return with(new String[] { Subpopulation.P_SPECIES,
				VectorSpecies.P_MUTATIONPROB }, probability);
	}

	/**
	 * @param type the type of crossover
	 * @param probability the crossover probability (only for
	 *        {@link VectorCrossoverType.ANY_POINT}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withCrossover(final VectorCrossoverType type,
			final double probability)
	{
		if (type == VectorCrossoverType.ANY_POINT)
			with(new String[] { Subpopulation.P_SPECIES,
					VectorSpecies.P_CROSSOVERPROB }, probability);
		return with(new String[] { Subpopulation.P_SPECIES,
				VectorSpecies.P_CROSSOVERTYPE }, type.value);
	}

	/**
	 * @param type the type of {@link Individual}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withIndividual(final Class<? extends Individual> type)
	{
		return with(new String[] { Subpopulation.P_SPECIES,
				Species.P_INDIVIDUAL }, type);
	}

	/**
	 * @param type the type of {@link Fitness}
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withFitness(final Class<? extends Fitness> type)
	{
		return with(
				new String[] { Subpopulation.P_SPECIES, Species.P_FITNESS },
				type);
	}

	/**
	 * @param type the type of {@link BreedingPipeline}
	 * @param sources the source pipelines e.g. selection or other pipelines
	 * @return this {@link SubpopParamDB}
	 */
	public SubpopParamDB withPipe(final Class<? extends BreedingPipeline> type,
			final PipeParamDB... sources)
	{
		if (sources == null || sources.length == 0)
			throw new IllegalArgumentException("sources can't be empty");

		return with(new String[] { Subpopulation.P_SPECIES, Species.P_PIPE },
				type).withChildren(Arrays.asList(sources),
				Subpopulation.P_SPECIES, Species.P_PIPE,
				BreedingPipeline.P_SOURCE);
	}

}