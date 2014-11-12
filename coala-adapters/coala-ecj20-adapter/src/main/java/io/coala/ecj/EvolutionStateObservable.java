/* $Id$
 * $URL$
 * 
 * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
 * Part of the EU project Inertia, see http://www.inertia-project.eu/
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
 * Copyright (c) 2014 Almende B.V. 
 */
package io.coala.ecj;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;
import ec.EvolutionState;
import ec.Evolve;
import ec.simple.SimpleEvolutionState;
import ec.util.Checkpoint;

/**
 * {@link EvolutionStateObservable}
 * 
 * @date $Date$
 * @version $Revision$
 * @author <a href="mailto:rick@almende.org">Rick</a>
 *
 */
public class EvolutionStateObservable extends SimpleEvolutionState
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private final Subject<EvolutionStateEvent, EvolutionStateEvent> evolutionStateEvents = PublishSubject
			.create();

	/**
	 * @return an {@link Observable} of {@link EvolutionStateEvent}s
	 */
	public Observable<EvolutionStateEvent> getEvolutionStateEvents()
	{
		return this.evolutionStateEvents.asObservable();
	}

	@Override
	public void startFresh()
	{
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_SETUP);
		
		output.message("Setting up");
		setup(this, null); // a garbage Parameter
		
		// POPULATION INITIALIZATION
		output.message("Initializing Generation 0");
		statistics.preInitializationStatistics(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_INITIALIZE_POPULATION);
		population = initializer.initialPopulation(this, 0); // unthreaded
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_INITIALIZE_POPULATION);
		statistics.postInitializationStatistics(this);
		

		// INITIALIZE CONTACTS -- done after initialization to allow
		// a hook for the user to do things in Initializer before
		// an attempt is made to connect to island models etc.
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_INITIALIZE_CONTACTS);
		exchanger.initializeContacts(this);
		evaluator.initializeContacts(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_INITIALIZE_CONTACTS);
		
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_SETUP);
	}

	/**
	 * @return the finished {@link EvolutionState}
	 */
	public EvolutionState run(final String... args)
	{
		// state.output.systemMessage("Job: " + job);
		this.job = new Object[] { new Integer(this.randomSeedOffset) };
		this.runtimeArguments = args;

		// now we let it go
		run(EvolutionState.C_STARTED_FRESH);
		Evolve.cleanup(this);

		return this;
	}

	/**
	 * @return
	 * @throws InternalError
	 */
	public int evolve()
	{
		this.evolutionStateEvents.onNext(EvolutionStateEvent.NEXT_GENERATION);
		if (generation > 0)
			output.message("Generation " + generation);

		// EVALUATION
		statistics.preEvaluationStatistics(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_EVALUATE_POPULATION);
		evaluator.evaluatePopulation(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_EVALUATE_POPULATION);
		statistics.postEvaluationStatistics(this);

		// SHOULD WE QUIT?
		if (evaluator.runComplete(this) && quitOnRunComplete)
		{
			output.message("Found Ideal Individual");
			this.evolutionStateEvents.onNext(EvolutionStateEvent.RUN_SUCCESS);
			return R_SUCCESS;
		}

		// SHOULD WE QUIT?
		if (generation == numGenerations - 1)
		{
			this.evolutionStateEvents.onNext(EvolutionStateEvent.RUN_FAILURE);
			return R_FAILURE;
		}

		// PRE-BREEDING EXCHANGING
		statistics.prePreBreedingExchangeStatistics(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_PRE_BREED_EXCHANGE_POPULATION);
		population = exchanger.preBreedingExchangePopulation(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_PRE_BREED_EXCHANGE_POPULATION);
		statistics.postPreBreedingExchangeStatistics(this);

		String exchangerWantsToShutdown = exchanger.runComplete(this);
		if (exchangerWantsToShutdown != null)
		{
			output.message(exchangerWantsToShutdown);
			/*
			 * Don't really know what to return here.  The only place I could
			 * find where runComplete ever returns non-null is 
			 * IslandExchange.  However, that can return non-null whether or
			 * not the ideal individual was found (for example, if there was
			 * a communication error with the server).
			 * 
			 * Since the original version of this code didn't care, and the
			 * result was initialized to R_SUCCESS before the while loop, I'm 
			 * just going to return R_SUCCESS here. 
			 */

			this.evolutionStateEvents.onNext(EvolutionStateEvent.RUN_SUCCESS);
			return R_SUCCESS;
		}

		// BREEDING
		statistics.preBreedingStatistics(this);

		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_BREED_POPULATION);
		population = breeder.breedPopulation(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_BREED_POPULATION);

		// POST-BREEDING EXCHANGING
		statistics.postBreedingStatistics(this);

		// POST-BREEDING EXCHANGING
		statistics.prePostBreedingExchangeStatistics(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_POST_BREED_EXCHANGE_POPULATION);
		population = exchanger.postBreedingExchangePopulation(this);
		this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_POST_BREED_EXCHANGE_POPULATION);
		statistics.postPostBreedingExchangeStatistics(this);

		// INCREMENT GENERATION AND CHECKPOINT
		generation++;
		if (checkpoint && generation % checkpointModulo == 0)
		{
			output.message("Checkpointing");
			statistics.preCheckpointStatistics(this);

			this.evolutionStateEvents.onNext(EvolutionStateEvent.ANTE_CHECKPOINT);
			Checkpoint.setCheckpoint(this);
			this.evolutionStateEvents.onNext(EvolutionStateEvent.POST_CHECKPOINT);
			
			statistics.postCheckpointStatistics(this);
		}

		return R_NOTDONE;
	}

	@Override
	public void finish(int result)
	{
		// Output.message("Finishing");
		/* finish up -- we completed. */
		statistics.finalStatistics(this, result);
		finisher.finishPopulation(this, result);
		exchanger.closeContacts(this, result);
		evaluator.closeContacts(this, result);
	}

}
