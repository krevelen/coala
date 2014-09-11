/* $Id$
 * $URL: https://dev.almende.com/svn/abms/ecj-util/src/main/java/com/almende/train/ec/util/GlobalParamDB.java $
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
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ec.Breeder;
import ec.Evaluator;
import ec.EvolutionState;
import ec.Evolve;
import ec.Exchanger;
import ec.Finisher;
import ec.Initializer;
import ec.Population;
import ec.Problem;
import ec.Statistics;
import ec.simple.SimpleStatistics;
import ec.util.Output;
import ec.util.OutputException;
import ec.util.Parameter;

/**
 * {@link GlobalParamDB}
 * 
 * @date $Date: 2014-02-12 16:00:03 +0100 (Wed, 12 Feb 2014) $
 * @version $Revision: 60 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class GlobalParamDB extends AbstractParamDB<GlobalParamDB>
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * {@link ParameterVerbosity}
	 * 
	 * @date $Date: 2014-02-12 16:00:03 +0100 (Wed, 12 Feb 2014) $
	 * @version $Revision: 60 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public enum ParameterVerbosity
	{
		/** */
		ALL(Evolve.P_PRINTALLPARAMETERS),

		/** */
		ACCESSED(Evolve.P_PRINTACCESSEDPARAMETERS),

		/** */
		USED(Evolve.P_PRINTUSEDPARAMETERS),

		/** */
		UNUSED(Evolve.P_PRINTUNUSEDPARAMETERS),

		/** */
		UNACCESSED(Evolve.P_PRINTUNACCESSEDPARAMETERS);

		/** */
		public final String property;

		/** */
		private ParameterVerbosity(final String property)
		{
			this.property = property;
		}
	}

	/** {@link GlobalParamDB} constructor */
	public GlobalParamDB()
	{
		// FIXME find purpose and original string specifications in ECJ source
		// with("store", true);
		// with("flush", true);
		// with("gc", false);
		// with("gc.modulo", 1);
		// with("aggressive", true);
	}

	/** @see AbstractParamDB#getMainType() */
	@Override
	public Class<?> getMainType()
	{
		return null; // 'null' indicates: no main type info
	}

	public GlobalParamDB withVerbosity(final ParameterVerbosity verbosity)
	{
		return with(verbosity.property, true);
	}

	/**
	 * @param type the type of {@link EvolutionState}
	 * @param quitOnRunComplete
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withState(final Class<? extends EvolutionState> type,
			final boolean quitOnRunComplete)
	{
		return with(Evolve.P_STATE, type).with(
				EvolutionState.P_QUITONRUNCOMPLETE, quitOnRunComplete);
	}

	/**
	 * @param enabled
	 * @param modulo
	 * @param prefix
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withCheckpoint(final boolean enabled,
			final int modulo, final String prefix)
	{
		return with(EvolutionState.P_CHECKPOINT, enabled).with(
				EvolutionState.P_CHECKPOINTMODULO, modulo).with(
				EvolutionState.P_CHECKPOINTPREFIX, prefix);
	}

	/**
	 * @param type the type of {@link Statistics}
	 * @param quitOnRunComplete
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withStatistics(final Class<? extends Statistics> type)
	{
		return with(EvolutionState.P_STATISTICS, type);
	}

	/**
	 * @param type the type of {@link Statistics}
	 * @param quitOnRunComplete
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withSimpleStatistics(
			final Class<? extends SimpleStatistics> type, final String file)
	{
		return withStatistics(type).with(
				new String[] { EvolutionState.P_STATISTICS,
						SimpleStatistics.P_STATISTICS_FILE }, file);
	}

	/**
	 * @param type the type of {@link Initializer}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withInitializer(final Class<? extends Initializer> type)
	{
		return with(EvolutionState.P_INITIALIZER, type);
	}

	/**
	 * @param type the type of {@link Finisher}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withFinisher(final Class<? extends Finisher> type)
	{
		return with(EvolutionState.P_FINISHER, type);
	}

	/**
	 * @param type the type of {@link Exchanger}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withExchanger(final Class<? extends Exchanger> type)
	{
		return with(EvolutionState.P_EXCHANGER, type);
	}

	/**
	 * @param type the type of {@link Breeder}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withBreeder(final Class<? extends Breeder> type)
	{
		return with(EvolutionState.P_BREEDER, type);
	}

	/**
	 * @param type the type of {@link Evaluator}
	 * @param problem the type of {@link Problem}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withEvaluator(final Class<? extends Evaluator> type,
			final Class<? extends Problem> problem)
	{
		return with(EvolutionState.P_EVALUATOR, type)
				.with(new String[] { EvolutionState.P_EVALUATOR,
						Evaluator.P_PROBLEM }, problem);
	}

	/**
	 * @param breedThreads
	 * @param evalThreads
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withThreads(final int breedThreads,
			final int evalThreads)
	{
		return with(Evolve.P_BREEDTHREADS, breedThreads).with(
				Evolve.P_EVALTHREADS, evalThreads);
	}

	/**
	 * @param seeds the random seed values (Integer or null for "time")
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withSeeds(final Integer... seeds)
	{
		if (seeds == null || seeds.length == 0)
			throw new IllegalArgumentException("seeds can't be empty");

		for (int i = 0; i < seeds.length; i++)
			with(new String[] { Evolve.P_SEED, Integer.toString(i) },
					seeds[i] == null ? Evolve.V_SEED_TIME : seeds[i].toString());
		return this;
	}

	/**
	 * @param type the type of {@link Population}
	 * @param problem the type of {@link Problem}
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withPopulation(final Class<? extends Population> type,
			final SubpopParamDB... subpops)
	{
		if (subpops == null || subpops.length == 0)
			throw new IllegalArgumentException("subpops can't be empty");

		return with(Initializer.P_POP, type).with(
				new String[] { Initializer.P_POP, Population.P_SIZE },
				subpops.length).withChildren(Arrays.asList(subpops),
				Initializer.P_POP, Population.P_SUBPOP);
	}

	/**
	 * @param generations the number of generations, e.g. 51
	 * @return this {@link GlobalParamDB}
	 */
	public GlobalParamDB withGenerations(final int generations)
	{
		return with(EvolutionState.P_GENERATIONS, generations);
	}

	/** */
	private static final String FATAL_PREFIX = "FATAL ERROR:\n",
			STARTUP_PREFIX = "STARTUP ERROR:\n", ERROR_PREFIX = "ERROR:\n",
			WARNING_PREFIX = "WARNING:\n";

	/**
	 * @return 
	 * 
	 */
	public EvolutionState run(final Logger LOG)
	{
		final Output l4jOut = new Output(false)
		{

			/** */
			private static final long serialVersionUID = 1L;

			@Override
			public synchronized void println(final String s, final int log,
					final boolean _announcement) throws OutputException
			{
				if (s.startsWith(FATAL_PREFIX))
					LOG.fatal(s.substring(FATAL_PREFIX.length()));
				else if (s.startsWith(STARTUP_PREFIX))
					LOG.fatal(s.substring(STARTUP_PREFIX.length()));
				else if (s.startsWith(ERROR_PREFIX))
					LOG.error(s.substring(ERROR_PREFIX.length()));
				else if (s.startsWith(WARNING_PREFIX))
					LOG.warn(s.substring(WARNING_PREFIX.length()));
				else
					LOG.info(s);
			}

			@Override
			public synchronized void fatal(final String s)
			{
				println(FATAL_PREFIX + s, ALL_LOGS, true);
				// exitWithError();
				throw new Error(s);
			}

			@Override
			public synchronized void fatal(final String s, final Parameter p1)
			{
				println(FATAL_PREFIX + s, ALL_LOGS, true);
				if (p1 != null)
					println("PARAMETER: " + p1, ALL_LOGS, true);
				// exitWithError();
				throw new Error(s);
			}

			@Override
			public synchronized void fatal(final String s, final Parameter p1,
					final Parameter p2)
			{
				println(FATAL_PREFIX + s, ALL_LOGS, true);
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
		// l4jOut.addLog(ec.util.Log.D_STDOUT, false);
		l4jOut.addLog(ec.util.Log.D_STDERR, true);

		final int job = 0;
		final EvolutionState state = Evolve.initialize(this, job, l4jOut);

		final SortedMap<String, String> params = new TreeMap<String, String>();
		for (Object key : this.keySet())
			params.put((String) key, (String) this.get(key));
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
		
		return state;
	}

}