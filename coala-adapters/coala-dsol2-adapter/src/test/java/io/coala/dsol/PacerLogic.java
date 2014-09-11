///* $Id$
// * $URL: https://dev.almende.com/svn/abms/dsol-util/src/test/java/io/coala/dsol/PacerLogic.java $
// * 
// * Part of the EU project Adapt4EE, see http://www.adapt4ee.eu/
// * 
// * @license
// * Licensed under the Apache License, Version 2.0 (the "License"); you may not
// * use this file except in compliance with the License. You may obtain a copy
// * of the License at
// * 
// * http://www.apache.org/licenses/LICENSE-2.0
// * 
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
// * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
// * License for the specific language governing permissions and limitations under
// * the License.
// * 
// * Copyright (c) 2010-2014 Almende B.V. 
// */
//package io.coala.dsol;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.NavigableMap;
//import java.util.Set;
//import java.util.concurrent.ConcurrentSkipListMap;
//import java.util.concurrent.CountDownLatch;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import java.util.concurrent.TimeUnit;
//import java.util.logging.Level;
//
//import nl.tudelft.simulation.agents.Agent;
//import nl.tudelft.simulation.agents.AgentFactory;
//import nl.tudelft.simulation.agents.AgentID;
//import nl.tudelft.simulation.agents.AgentLogic;
//import nl.tudelft.simulation.agents.Failure;
//import nl.tudelft.simulation.agents.actor.ActorID;
//import nl.tudelft.simulation.agents.event.scheduling.Simulator;
//import nl.tudelft.simulation.agents.event.time.SimTime;
//import nl.tudelft.simulation.agents.factory.FederationFactory;
//import nl.tudelft.simulation.agents.factory.FederationFactory.SimulatorSettings;
//import nl.tudelft.simulation.agents.naming.IDWrapper;
//import nl.tudelft.simulation.agents.naming.IDWrapper.IDUtil;
//import nl.tudelft.simulation.agents.persistence.PacerDataSource;
//import nl.tudelft.simulation.agents.persistence.entity.FactManipulation;
//import nl.tudelft.simulation.agents.reasoner.ReasonerID;
//import nl.tudelft.simulation.agents.reasoner.ReasoningGrant;
//import nl.tudelft.simulation.agents.reasoner.ReasoningMode;
//import nl.tudelft.simulation.agents.service.DataService;
//import nl.tudelft.simulation.agents.service.PacerService;
//import nl.tudelft.simulation.agents.service.UserService;
//import nl.tudelft.simulation.agents.user.UserType;
//
//import org.joda.time.DateTime;
//
///**
// * <p>Copyright &copy; 2011 Delft University of Technology, Jaffalaan 5, 2628 BX
// * Delft, the Netherlands. All rights reserved. <br/> For project information,
// * see <a href="http://www.simulation.tudelft.nl/">www.simulation.tudelft.nl</a>
// * and <a href="http://www.gscg.org/">www.gscg.org</a>. <br/>
// * The source code and binary code of this software is proprietary information
// * of Delft University of Technology.
// * 
// * @author <a href="http://www.tudelft.nl/averbraeck">Alexander Verbraeck</a>
// * @author <a href="http://www.tudelft.nl/dwfvankrevelen">Rick van Krevelen</a>
// * @date $Date: 2014-05-08 15:40:44 +0200 (Thu, 08 May 2014) $
// * @version SVN $Revision: 263 $ $Author: krevelen $
// * @param <A>
// */
//public class PacerLogic<A extends Agent<?, ?, ?>>
//	extends AgentLogic<A>
//	implements PacerService
//{
//
//	/** */
//	private static final long serialVersionUID = 1L;
//
//	/** */
//	private final Map<ActorID<?>, ExecutionRelease> executionReleases = Collections.synchronizedMap( new HashMap<ActorID<?>, ExecutionRelease>() );
//
//	/** */
//	private final Map<ActorID<?>, MessagingRelease> messagingReleases = Collections.synchronizedMap( new HashMap<ActorID<?>, MessagingRelease>() );
//
//	/** */
//	private final NavigableMap<String, Release> lastReleases = new ConcurrentSkipListMap<String, Release>();
//
//	/** */
//	private final Set<ActorID<?>> federates = Collections.synchronizedSet( new HashSet<ActorID<?>>() );
//
//	/** */
//	private final Set<ActorID<?>> aspiringFederates = Collections.synchronizedSet( new HashSet<ActorID<?>>() );
//
//	/** */
//	private final Set<Grant> failedGrants = Collections.synchronizedSet( new HashSet<Grant>() );
//
//	/** the simulation pacing start time */
//	private SimTime startTime;
//
//	/** the simulation pacing stop time */
//	private SimTime stopTime;
//
//	/** the simulation time step per granted federate simulator advance */
//	private SimTime deltaTime;
//
//	/** wall-clock delay between granted execution advances in milliseconds */
//	private float paceMS;
//
//	/** the time stamp for the next federate simulation execution advance */
//	protected long nextStepMillis;
//
//	/** */
//	private double accelerationFactor;
//
//	/** the pacer's current status */
//	private PacerState state;
//
//	/** the current overall granted execution advance time */
//	private SimTime grant = new SimTime( Double.NaN, Simulator.BASE_TIME_UNIT );
//
//	/** the pacing pause/resume flag */
//	private boolean resume;
//
//	/** */
//	protected final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
//
//	/** */
//	protected CountDownLatch latch;
//
//	/**
//	 * @param owner
//	 * @throws Failure
//	 */
//	public PacerLogic( final A owner )
//		throws Failure
//	{
//		super( owner );
//	}
//
//	/** @see nl.tudelft.simulation.agents.AgentLogic#initialize(nl.tudelft.simulation.agents.AgentFactory) */
//	@Override
//	public void initialize( final AgentFactory<?> aFactory ) throws Failure
//	{
//		super.initialize( aFactory );
//
//		final PacerAgentFactory factory = (PacerAgentFactory) aFactory;
//		final FederationFactory fedFactory = factory.constructFederationFactory( this );
//		final SimulatorSettings simSettings = fedFactory.getSimulatorSettings();
//		this.startTime = simSettings.getStartTime();
//		this.stopTime = this.startTime.add( simSettings.getRunLength() );
//		this.deltaTime = simSettings.getDeltaTime().convertTo(
//			this.startTime.getUnit() );
//		this.paceMS = simSettings.getPaceMS();
//		this.resume = simSettings.getStartOnLoad();
//		this.accelerationFactor = simSettings.getAccelerationFactor();
//
//		final double nrSteps = this.stopTime.quotient( this.deltaTime );
//		this.grant = new SimTime( Double.NaN, this.startTime.getUnit() );
//		this.nextStepMillis = Long.MAX_VALUE; // hold horses until initialized
//		this.state = PacerState.INITIALIZING;
//
//		log( Level.FINEST, String.format( Agent.LOCALE,
//			"Expected run time (min) = T / dT * pacePerMin = "
//				+ "( ( T0=%s + %s = %s ) / %s = %1.2f steps ) * %dms = %1.2fmin",
//			simSettings.getStartTime(), simSettings.getRunLength(), this.stopTime,
//			this.deltaTime, nrSteps, simSettings.getPaceMS(), nrSteps
//				* this.paceMS / 1000 / 60 ) );
//		fedFactory.cleanUp();
//	}
//
//	/** @return <tt>true</tt> if the pacer is running, <tt>false</tt> otherwise */
//	public boolean isRunning()
//	{
//		return this.resume;
//	}
//
//	/** @return the min grant time */
//	public SimTime getStartTime()
//	{
//		return this.startTime;
//	}
//
//	/** @return the max grant time */
//	public SimTime getStopTime()
//	{
//		return this.stopTime;
//	}
//
//	/** @return the max grant time */
//	public SimTime getTimeStep()
//	{
//		return this.deltaTime;
//	}
//
//	/** @return the current delay between simulation grants in milliseconds */
//	public float getDelayMS()
//	{
//		return this.paceMS;
//	}
//
//	/** @return the current grant time */
//	public synchronized SimTime getGrant()
//	{
//		return this.grant;
//	}
//
//	/** @return the current pacer status */
//	public synchronized PacerState getState()
//	{
//		return this.state;
//	}
//
//	/** @param sets the new pacer state */
//	protected synchronized void setState( final PacerState newState )
//	{
//		this.state = newState;
//		notifyAll();
//	}
//
//	/**
//	 * @param federateIDs
//	 * @param federateID the actor taking part in this paced federation
//	 * @throws Failure
//	 */
//	public synchronized void initializeFederates(
//		final Collection<ActorID<?>> federateIDs ) throws Failure
//	{
//		if( !this.state.equals( PacerState.INITIALIZING )
//			&& !this.state.equals( PacerState.COMPLETE ) )
//			throw new Failure( "Can't initialize federates while status is: "
//				+ this.state );
//		this.grant = new SimTime( Double.NaN, getStartTime().getUnit() );
//		this.nextStepMillis = Long.MAX_VALUE; // hold horses until initialized
//		for( ActorID<?> federateID : federateIDs )
//		{
//			if( federateID == null )
//				throw new Failure( "Can't initialize federateID: " + federateID );
//			this.aspiringFederates.add( federateID );
//		}
//		this.state = PacerState.PAUSED; // initialization complete
//		log( Level.FINEST,
//			"Initialized federates: " + IDWrapper.IDUtil.toString( federateIDs ) );
//		if( this.resume )
//			resume();
//	}
//
//	/** */
//	protected synchronized void advanceGrant()
//	{
//		this.nextStepMillis += (long) getDelayMS();
//		final SimTime oldGrant = getGrant();
//		this.grant = oldGrant.getValue().isNaN() ? getStartTime() : SimTime.min(
//			oldGrant.add( getTimeStep() ), getStopTime() );
//	}
//
//	/** */
//	protected boolean checkSimulationComplete()
//	{
//		synchronized( this.federates )
//		{
//			this.federates.addAll( this.aspiringFederates );
//			//this.pendingFederates.clear();
//
//			if( (!getGrant().getValue().isNaN() && getGrant().compareTo(
//				getStopTime() ) >= 0)
//				|| this.federates.size() == 0 )
//			{
//				log( "Halt grant cycle; all available federates ("
//					+ this.federates.size() + ") reached stop time: T="
//					+ getStopTime() );
//				return true;
//			}
//		}
//		return false;
//	}
//
//	/** */
//	protected void continuePacingCycle()
//	{
//		if( checkSimulationComplete() )
//		{
//			log( "Simulation completed " );
//			setState( PacerState.COMPLETE );
//			pause();
//			return; // break cycle
//		}
//		if( isRunning() && PacerState.PAUSED.equals( getState() ) )
//			setState( PacerState.MESSAGING_DONE );
//
//		this.executor.execute( new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				while( !PacerState.MESSAGING_DONE.equals( getState() ) )
//				{
//					//log( "Awaiting pacing cycle start state, now: " + getState() );
//					synchronized( PacerLogic.this )
//					{
//						try
//						{
//							PacerLogic.this.wait( 1000 );
//						}
//						catch( final InterruptedException ignore )
//						{
//							// do nothing
//						}
//					}
//				}
//				long delayMS = PacerLogic.this.nextStepMillis
//					- System.currentTimeMillis();
//				while( delayMS > 0 )
//				{
//					//log( "Waiting " + delayMS + " milliseconds to pace cycle" );
//					synchronized( this )
//					{
//						try
//						{
//							wait( delayMS );
//						}
//						catch( final InterruptedException ignore )
//						{
//							// do nothing
//						}
//					}
//					delayMS = PacerLogic.this.nextStepMillis
//						- System.currentTimeMillis();
//				}
//				//log( "Grant execution..." );
//				setState( PacerState.EXECUTING );
//				advanceGrant();
//				sendExecutionGrants();
//
//				/*
//				while( !PacerState.EXECUTING.equals( getState() )
//					|| !checkExecutionReleases() )
//				{
//					log( "Waiting for execution releases, state: " + getState()
//						+ ", pending: " + getPendingGrants( true ) );
//					synchronized( this )
//					{
//						try
//						{
//							wait( 1000 );
//						}
//						catch( final InterruptedException ignore )
//						{
//							// do nothing
//						}
//					}
//				}
//				*/
//				//log( "Execution complete..." );
//				setState( PacerState.EXECUTING_DONE );
//				setState( PacerState.MESSAGING );
//				//log( "Grant messaging..." );
//				sendMessagingGrants();
//				/*
//				while( !PacerState.MESSAGING.equals( getState() )
//					|| !checkMessagingReleases() )
//				{
//					log( "Waiting for messaging releases, state: " + getState()
//						+ ", pending: " + getPendingGrants( false ) );
//					synchronized( this )
//					{
//						try
//						{
//							wait( 1000 );
//						}
//						catch( final InterruptedException ignore )
//						{
//							// do nothing
//						}
//					}
//				}
//				*/
//				//log( "Messaging complete..." );
//				setState( PacerState.MESSAGING_DONE );
//				if( isRunning() )
//				{
//					//log( "Next cycle" );
//					continuePacingCycle();
//				}
//				else
//				{
//					log( "Breaking pacing cycle; pause ordered" );
//					setState( PacerState.PAUSED );
//				}
//			}
//		} );
//	}
//
//	/** */
//	protected String getPendingGrants( final boolean execution )
//	{
//		final Set<AgentID<?>> pending = new HashSet<AgentID<?>>( this.federates );
//		pending.removeAll( execution
//			? this.executionReleases.keySet() : this.messagingReleases.keySet() );
//		return IDUtil.toString( pending ).toString();
//	}
//
//	/** @param release */
//	public void handleExecutionRelease( final ExecutionRelease release )
//	{
//		this.lastReleases.put( release.getSenderID().getValue().toString(),
//			release );
//		synchronized( this.executionReleases )
//		{
//			if( !PacerState.EXECUTING.equals( getState() )
//				&& !PacerState.PAUSED.equals( getState() ) )
//				log( Level.WARNING, "Unexpected execution release in state: "
//					+ getState() + " from: " + release.getSenderID() );
//
//			if( this.executionReleases.put( release.getSenderID(), release ) != null )
//				log( Level.WARNING, "OVERWRITING OLD EXECUTION RELEASE?" );
//
//			this.latch.countDown();
//			//log( "Released execution: " + release.getSender()
//			//	+ " up to t=" + release.getReleaseTime() + ", "
//			//	+ this.latch.getCount() + " pending: " + getPendingGrants( true ) );
//		}
//
//		synchronized( this.aspiringFederates )
//		{
//			if( this.aspiringFederates.add( release.getSenderID() ) )
//				log( "Added unknown federate from received release: " + release );
//		}
//	}
//
//	/** @param release */
//	public void handleMessagingRelease( final MessagingRelease release )
//	{
//		synchronized( this.messagingReleases )
//		{
//			if( !getState().equals( PacerState.MESSAGING )
//				&& !getState().equals( PacerState.PAUSED ) )
//				log( "Unexpected messaging release in state: " + getState()
//					+ " from: " + release.getSenderID() );
//
//			if( this.messagingReleases.put( release.getSenderID(), release ) != null )
//				log( Level.WARNING, "OVERWRITING OLD MESSAGING RELEASE?" );
//
//			this.latch.countDown();
//			//log( "Released messaging grant: " + release.getSender() + ", "
//			//	+ this.latch.getCount() + " pending: " + getPendingGrants( false ) );
//		}
//
//		synchronized( this.aspiringFederates )
//		{
//			if( this.aspiringFederates.add( release.getSenderID() ) )
//				log( "Added unknown federate from received release: " + release );
//		}
//	}
//
//	/** */
//	protected void sendExecutionGrants()
//	{
//		//log( "Granting execution to "+this.federates );
//		synchronized( this.executionReleases )
//		{
//			SimTime firstRelease = new SimTime( Double.MAX_VALUE );
//			for( ExecutionRelease release : this.executionReleases.values() )
//				if( release.getReleaseTime().compareTo( firstRelease ) < 0 )
//					firstRelease = release.getReleaseTime();
//			/*
//			log( String.format( Locale.US,
//				"Granting up to: [t=%s] (next event at [t=%s])", this.grant,
//				firstRelease ) );
//				*/
//
//			this.executionReleases.clear();
//		}
//		synchronized( this.federates )
//		{
//			this.latch = new CountDownLatch( this.federates.size() );
//			for( ActorID<?> federateID : this.federates )
//			{
//				final Grant _grant = ReasonerID.class.isInstance( federateID )
//					? new ReasoningGrant( (ReasonerID<?>) federateID, getGrant(),
//						ReasoningMode.SYSTEM_TIME_LIMITED ) : new ExecutionGrant(
//						federateID, getGrant() );
//				try
//				{
//					//log( "Sending execution grant: "+_grant );
//					getOwner().send( _grant );
//				}
//				catch( final Failure e )
//				{
//					log( Level.WARNING, "Execution grant failed: " + _grant, e );
//					removeFederate( federateID, _grant );
//				}
//			}
//		}
//
//		while( this.latch.getCount() > 0 )
//		{
//			//log( "Waiting for execution releases..." );
//			try
//			{
//				this.latch.await( 1000, TimeUnit.MILLISECONDS );
//			}
//			catch( final InterruptedException ignore )
//			{
//				// do nothing
//			}
//		}
//	}
//
//	/** */
//	protected void sendMessagingGrants()
//	{
//		synchronized( this.messagingReleases )
//		{
//			this.messagingReleases.clear();
//		}
//		synchronized( this.federates )
//		{
//			this.latch = new CountDownLatch( this.federates.size() );
//			for( ActorID<?> federateID : this.federates )
//			{
//				final MessagingGrant _grant = new MessagingGrant( federateID );
//				try
//				{
//					//log( "Sending messaging grant: "+_grant );
//					getOwner().send( _grant );
//				}
//				catch( final Failure e )
//				{
//					log( Level.WARNING, "Messaging grant failed: " + _grant, e );
//					removeFederate( federateID, _grant );
//				}
//			}
//		}
//
//		while( this.latch.getCount() > 0 )
//		{
//			//log( "Waiting for messaging releases..." );
//			try
//			{
//				this.latch.await( 1000, TimeUnit.MILLISECONDS );
//			}
//			catch( final InterruptedException ignore )
//			{
//				// do nothing
//			}
//		}
//	}
//
//	/** */
//	protected void removeFederate( final ActorID<?> federateID,
//		final Grant failedGrant )
//	{
//		log( Level.WARNING, "Removing federate: " + federateID.getValue() );
//		synchronized( this.federates )
//		{
//			this.federates.remove( federateID );
//			this.latch.countDown();
//		}
//		synchronized( this.aspiringFederates )
//		{
//			this.aspiringFederates.remove( federateID );
//		}
//		synchronized( this.failedGrants )
//		{
//			this.failedGrants.add( failedGrant );
//		}
//	}
//
//	/** */
//	protected boolean checkExecutionReleases()
//	{
//		synchronized( this.federates )
//		{
//			for( AgentID<?> federateID : this.federates )
//			{
//				final ExecutionRelease release = this.executionReleases.get( federateID );
//				if( release == null )
//					return false;
//				else if( release.getReleaseTime().compareTo( getGrant() ) < 0 )
//					log(
//						Level.WARNING,
//						"Federate " + federateID
//							+ " should release AFTER the granted time !! "
//							+ release.getReleaseTime() + " < " + getGrant() );
//			}
//		}
//
//		return true;
//	}
//
//	/** */
//	protected boolean checkMessagingReleases()
//	{
//		synchronized( this.federates )
//		{
//			for( ActorID<?> federateID : this.federates )
//				synchronized( this.messagingReleases )
//				{
//					if( !this.messagingReleases.containsKey( federateID ) )
//					{
//						// waiting for messaging release from federate
//						return false;
//					}
//				}
//		}
//		return true;
//	}
//
//	/** PACER SERVICE IMPL ****************************************************/
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#resume() */
//	@Override
//	public synchronized void resume()
//	{
//		this.resume = true;
//		this.nextStepMillis = System.currentTimeMillis();
//		continuePacingCycle();
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#pause() */
//	@Override
//	public synchronized void pause()
//	{
//		this.resume = false;
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#speedUp() */
//	@Override
//	public synchronized void speedUp()
//	{
//		this.paceMS /= this.accelerationFactor;
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#slowDown() */
//	@Override
//	public synchronized void slowDown()
//	{
//		this.paceMS *= this.accelerationFactor;
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#getStatus() */
//	@Override
//	public synchronized StatusInfo getStatus()
//	{
//		final SimTime _grant = getGrant();
//		final SimTime time = _grant == null ? getStartTime() : _grant;
//		return new StatusInfo( isRunning(), getDelayMS(), time, getStopTime(),
//			getTimeStep(), getState() );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#getFederates() */
//	@Override
//	public String[] getFederates()
//	{
//		final ArrayList<String> result = new ArrayList<String>();
//		synchronized( this.aspiringFederates )
//		{
//			for( ActorID<?> federateID : this.aspiringFederates )
//				result.add( federateID.getValue().toString() );
//		}
//		return result.toArray( new String[result.size()] );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#getFailedGrants() */
//	@Override
//	public synchronized GrantInfo[] getFailedGrants()
//	{
//		final ArrayList<GrantInfo> result = new ArrayList<GrantInfo>();
//		for( Grant failedGrant : this.failedGrants )
//			result.add( new GrantInfo( failedGrant ) );
//		return result.toArray( new GrantInfo[result.size()] );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#getLastReleases() */
//	@Override
//	public synchronized ReleaseInfo[] getLastReleases()
//	{
//		final ArrayList<ReleaseInfo> result = new ArrayList<ReleaseInfo>();
//		for( Release release : this.lastReleases.values() )
//			result.add( new ReleaseInfo( release ) );
//		return result.toArray( new ReleaseInfo[result.size()] );
//	}
//
//	/** */
//	private final Map<String, AccessInfo> actorLogins = new HashMap<String, AccessInfo>();
//
//	/**
//	 * @see nl.tudelft.simulation.agents.service.PacerService#assignFederateLogin(java.lang.String,
//	 *      java.lang.String, java.lang.String, java.lang.String)
//	 */
//	@Override
//	public void assignFederateLogin( final String assigner,
//		final String federate, final String login, final String password )
//	{
//		log( Level.INFO, "Assign federate login: " + login + " with password: "
//			+ password + " by assigner: " + assigner + " for federate: "
//			+ federate );
//		// FIXME obtain credentials of assigner!
//		if( !login.equals( "admin" ) && !login.equals( "rick" )
//			&& !login.equals( assigner ) )
//		{
//			synchronized( this.aspiringFederates )
//			{
//				if( login.length() == 0 ) // revoke access for zero-length login
//				{
//					synchronized( this.actorLogins )
//					{
//						final AccessInfo info = this.actorLogins.get( federate );
//						if( info != null )
//						{
//							try
//							{
//								getOwner().revokeAccess( assigner, info.getLogin() );
//								this.actorLogins.remove( federate );
//								return;
//							}
//							catch( final Failure e )
//							{
//								log( "Problem revoking old login for federate: "
//									+ federate, e );
//							}
//						}
//					}
//				}
//				else
//					for( ActorID<?> federateID : this.aspiringFederates )
//						if( federateID.getValue().toString().equals( federate ) )
//						{
//							try
//							{
//								getOwner().grantAccess( assigner, login, password,
//									UserType.PLAYER );
//								synchronized( this.actorLogins )
//								{
//									this.actorLogins.put( federate, new AccessInfo(
//										federate, login, password ) );
//								}
//							}
//							catch( final Failure e )
//							{
//								log( "Problem setting login for federate: " + federate,
//									e );
//							}
//							return;
//						}
//			}
//		}
//		log( Level.WARNING, "Could not assign login: " + login
//			+ " to unknown federate: " + federate );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.PacerService#getFederateLoginAssignments() */
//	@Override
//	public AccessInfo[] getFederateLoginAssignments()
//	{
//		synchronized( this.actorLogins )
//		{
//			return this.actorLogins.values().toArray(
//				new AccessInfo[this.actorLogins.size()] );
//		}
//	}
//
//	/** */
//	private final Map<String, ChartLoader> chartLoaders = Collections.synchronizedMap( new HashMap<String, ChartLoader>() );
//
//	/** @see nl.tudelft.simulation.agents.service.DataService#addChart(nl.tudelft.simulation.agents.service.DataService.ChartLoader) */
//	@Override
//	public void addChart( final ChartLoader source )
//	{
//		if( this.chartLoaders.containsKey( source.getInfo().getChartID() ) )
//		{
//			log( Level.WARNING, "Problem adding chart, already exists: "
//				+ source.getInfo().getChartID() );
//			return;
//		}
//		this.chartLoaders.put( source.getInfo().getChartID(), source );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.DataService#getCharts() */
//	@Override
//	public ChartInfo[] getCharts()
//	{
//		final ArrayList<ChartInfo> result = new ArrayList<ChartInfo>();
//		synchronized( this.chartLoaders )
//		{
//			for( ChartLoader loader : this.chartLoaders.values() )
//				result.add( loader.getInfo() );
//		}
//		return result.toArray( new ChartInfo[result.size()] );
//	}
//
//	/**
//	 * @see nl.tudelft.simulation.agents.service.DataService#getChartData(java.lang.String,
//	 *      double)
//	 */
//	@Override
//	public ChartData getChartData( final String chartID, final double since )
//	{
//		return this.chartLoaders.get( chartID ).getData(
//			new SimTime( since, DataService.GUI_TIME_UNIT ) );
//	}
//
//	/**
//	 * @see nl.tudelft.simulation.agents.service.UserService#getManipulations(long,
//	 *      int)
//	 */
//	@Override
//	public ManipulationInfo[] getManipulations( final long since, final int max )
//	{
//		final ArrayList<ManipulationInfo> result = new ArrayList<ManipulationInfo>();
//		try
//		{
//			for( FactManipulation fact : ((PacerDataSource) getOwner().getHistory()).findManipulations(
//				new DateTime( since ), max ) )
//				result.add( new ManipulationInfo( fact ) );
//		}
//		catch( final Failure exception )
//		{
//			log( "Problem retrieving manipulations", exception );
//		}
//		return result.toArray( new ManipulationInfo[result.size()] );
//	}
//
//	/**
//	 * @see nl.tudelft.simulation.agents.service.UserService#getManipulations(java.lang.String,
//	 *      long, int)
//	 */
//	@Override
//	public ManipulationInfo[] getManipulations( final String query,
//		final long since, final int max )
//	{
//		final ArrayList<ManipulationInfo> result = new ArrayList<ManipulationInfo>();
//		try
//		{
//			for( FactManipulation fact : ((PacerDataSource) getOwner().getHistory()).findManipulations(
//				query, new DateTime( since ), max ) )
//				result.add( new ManipulationInfo( fact ) );
//		}
//		catch( final Failure exception )
//		{
//			log( "Problem retrieving manipulations", exception );
//		}
//		return result.toArray( new ManipulationInfo[result.size()] );
//	}
//
//	/** @see nl.tudelft.simulation.agents.service.UserService#getManipulators() */
//	@Override
//	public ManipulatorInfo[] getManipulators()
//	{
//		// TODO: proxy manipulators from (non-interactive) actor agents
//		return new ManipulatorInfo[0];
//	}
//
//	/**
//	 * @see UserService#getManipulatorStatus(boolean, boolean, boolean)
//	 */
//	@Override
//	public ManipulatorStatusInfo[] getManipulatorStatus()
//	{
//		// TODO: proxy manipulators from (non-interactive) actor agents
//		return new ManipulatorStatusInfo[0];
//	}
//
//	/**
//	 * @see nl.tudelft.simulation.agents.service.UserService#applyManipulations(java.lang.String,
//	 *      nl.tudelft.simulation.agents.service.UserService.UserManipulationInfo[])
//	 */
//	@Override
//	public void applyManipulations( final String userType,
//		final UserManipulationInfo[] newValues )
//	{
//		// TODO: proxy manipulations to (non-interactive) actor agents
//	}
//
//}
