/* $Id$
 * $URL: https://dev.almende.com/svn/abms/ecj-util/src/test/java/com/almende/train/BPMCalibrationByEvolutionTest.java $
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

import io.coala.ecj.GlobalParamDB;
import io.coala.ecj.PipeParamDB;
import io.coala.ecj.SubpopParamDB;
import io.coala.ecj.GlobalParamDB.ParameterVerbosity;
import io.coala.ecj.SubpopParamDB.VectorCrossoverType;
import io.coala.log.LogUtil;

import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;
import org.junit.Test;

import ec.EvolutionState;
import ec.Evolve;
import ec.Population;
import ec.Subpopulation;
import ec.app.tutorial2.AddSubtract;
import ec.app.tutorial2.OurMutatorPipeline;
import ec.select.FitProportionateSelection;
import ec.select.TournamentSelection;
import ec.simple.SimpleBreeder;
import ec.simple.SimpleEvaluator;
import ec.simple.SimpleEvolutionState;
import ec.simple.SimpleExchanger;
import ec.simple.SimpleFinisher;
import ec.simple.SimpleFitness;
import ec.simple.SimpleInitializer;
import ec.simple.SimpleStatistics;
import ec.util.Output;
import ec.util.OutputException;
import ec.util.Parameter;
import ec.util.ParameterDatabase;
import ec.vector.IntegerVectorIndividual;
import ec.vector.IntegerVectorSpecies;
import ec.vector.breed.VectorCrossoverPipeline;

/**
 * {@link EvolutionTest}
 * 
 * Genetic Algorithm approach:
 * <ul>
 * <li><b>individuals</b> (solutions, configurations) are sequences/vectors of
 * event-pair allocations to processes and activities, allocating both one
 * activity ID <i>and</i> process ID to each known event-pair (start+stop use
 * equipment, enter+leave room). So the more event-pairs need to be matched, the
 * longer the individual sequences/vectors become.</li>
 * <li><b>population</b> is initialized with worst-case allocation, i.e. unique
 * activity and process IDs for each event-pair across all individuals</li>
 * <li><b>fitness evaluation</b> is based on multi-criteria optimization
 * including
 * <ol type=a>
 * <li>minimizing the number of unique activity and process IDs, and</li>
 * <li>minimizing the total time between consecutive activities per process</li>
 * </ol>
 * <li><b>recombination operator</b> is multi-point cross-over that chooses
 * cross-over points such that resulting offspring produce valid schedules (i.e.
 * the time/resource allocation constraints still hold, including cycles,
 * resource recurrence, parallel activities, multiple activities per occupant,
 * etc.)</li>
 * <li><b>mutation operator</b>: join event-pairs into the same activity and/or
 * process ID (producing only valid schedules) with preference for event-pairs
 * that are nearby in terms of time and/or process resource allocation</li>
 * </ul>
 * 
 * @date $Date: 2014-04-18 16:38:34 +0200 (Fri, 18 Apr 2014) $
 * @version $Revision: 235 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
// @Ignore
public class EvolutionTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(EvolutionTest.class);

	/** */
	private static final String fatalStr = "FATAL ERROR:\n",
			initStr = "STARTUP ERROR:\n", errorStr = "ERROR:\n",
			warnStr = "WARNING:\n";

	/** */
	private static final Output l4jOut = new Output(false)
	{

		/** */
		private static final long serialVersionUID = 1L;

		@Override
		public synchronized void println(final String s, final int log,
				final boolean _announcement) throws OutputException
		{
			if (s.startsWith(fatalStr))
				LOG.fatal(s.substring(fatalStr.length()));
			else if (s.startsWith(initStr))
				LOG.fatal(s.substring(initStr.length()));
			else if (s.startsWith(errorStr))
				LOG.error(s.substring(errorStr.length()));
			else if (s.startsWith(warnStr))
				LOG.warn(s.substring(warnStr.length()));
			else
				LOG.info(s);
		}

		@Override
		public synchronized void fatal(final String s)
		{
			println(fatalStr + s, ALL_LOGS, true);
			// exitWithError();
			throw new Error(s);
		}

		@Override
		public synchronized void fatal(final String s, final Parameter p1)
		{
			println(fatalStr + s, ALL_LOGS, true);
			if (p1 != null)
				println("PARAMETER: " + p1, ALL_LOGS, true);
			// exitWithError();
			throw new Error(s);
		}

		@Override
		public synchronized void fatal(final String s, final Parameter p1,
				final Parameter p2)
		{
			println(fatalStr + s, ALL_LOGS, true);
			if (p1 != null)
				println("PARAMETER: " + p1, ALL_LOGS, true);
			if (p2 != null && p1 != null)
				println("     ALSO: " + p2, ALL_LOGS, true);
			else
				println("PARAMETER: " + p2, ALL_LOGS, true);
			// exitWithError();
			throw new Error(s);
		}

	};

	{
		// l4jOut.addLog(ec.util.Log.D_STDOUT, false);
		l4jOut.addLog(ec.util.Log.D_STDERR, true);
	}

	protected static void runEC(final ParameterDatabase parameters)
	{
		final int job = 0;
		final EvolutionState state = Evolve.initialize(parameters, job, l4jOut);

		final SortedMap<String, String> params = new TreeMap<String, String>();
		for (Object key : parameters.keySet())
			params.put((String) key, (String) parameters.get(key));
		l4jOut.println(
				"Running with parameters: "
						+ params.toString().replace("{", "{\n\t")
								.replace("}", "\n}").replace(", ", ",\n\t")
								.replace("=", " = "), Output.ALL_LOGS, true);

		// state.output.systemMessage("Job: " + job);
		state.job = new Object[] { new Integer(job) };
		state.runtimeArguments = new String[] {};

		// now we let it go
		state.run(EvolutionState.C_STARTED_FRESH);
		Evolve.cleanup(state);
	}

	/**
	 * Example taken from <a href=
	 * "http://cs.gmu.edu/~eclab/projects/ecj/docs/tutorials/tutorial2/" >ECJ
	 * tutorial 2</a>
	 */
	@Test
	public void testECJTutorial2()
	{
		LOG.trace("Starting");

		final ParameterDatabase parameters = new GlobalParamDB()
				.withVerbosity(ParameterVerbosity.ACCESSED)
				.withThreads(2, 2)
				.withSeeds(null, null)
				.withGenerations(51)
				.withState(SimpleEvolutionState.class, true)
				.withCheckpoint(false, 1, "ec")
				.withSimpleStatistics(SimpleStatistics.class, "$out.stat")
				.withInitializer(SimpleInitializer.class)
				.withFinisher(SimpleFinisher.class)
				.withExchanger(SimpleExchanger.class)
				.withBreeder(SimpleBreeder.class)
				.withEvaluator(SimpleEvaluator.class, AddSubtract.class)
				.withPopulation(
						Population.class,
						new SubpopParamDB(Subpopulation.class)
								.withSize(100)
								.withRetries(0)
								.withIntegerVectorSpecies(
										IntegerVectorSpecies.class, 20, -12312,
										2341212)
								.withMutation(0.05)
								.withCrossover(VectorCrossoverType.TWO_POINT,
										1.0)
								.withIndividual(IntegerVectorIndividual.class)
								.withFitness(SimpleFitness.class)
								.withPipe(
										OurMutatorPipeline.class,
										new PipeParamDB(
												VectorCrossoverPipeline.class,
												new PipeParamDB(
														FitProportionateSelection.class),
												new PipeParamDB(
														TournamentSelection.class)
														.with(TournamentSelection.P_SIZE,
																5)
														.with(TournamentSelection.P_PICKWORST,
																true)).with(
												VectorCrossoverPipeline.P_TOSS,
												true)));

		runEC(parameters);

		LOG.trace("Done!");
	}
}
