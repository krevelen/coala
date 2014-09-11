package io.coala.experimental.grant;
///* $Id$
// * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/GrantingSimulatorService.java $
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
// * Copyright (c) 2010-2013 Almende B.V. 
// */
//package com.almende.coala.service.scheduler;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//import javax.inject.Inject;
//
//import org.apache.log4j.Logger;
//
//import rx.Observable;
//import rx.Observer;
//import rx.Subscription;
//import rx.subjects.BehaviorSubject;
//import rx.subjects.ReplaySubject;
//import rx.subjects.Subject;
//
//import com.almende.coala.bind.Binder;
//import com.almende.coala.grant.Grant;
//import com.almende.coala.grant.Grantable;
//import com.almende.coala.grant.Granting;
//import com.almende.coala.identity.Identifiable;
//import com.almende.coala.identity.Identifier;
//import com.almende.coala.log.InjectLogger;
//import com.almende.coala.log.LogUtil;
//import com.almende.coala.time.ClockID;
//import com.almende.coala.time.SimTime;
//import com.almende.coala.time.SimTimeFactory;
//import com.almende.coala.time.TimeUnit;
//import com.almende.coala.time.Timed;
//
///**
// * {@link GrantingSimulatorService} provides the time
// * 
// * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
// * @version $Revision: 296 $
// * @author <a href="mailto:Rick@almende.org">Rick</a>
// */
//public class GrantingSimulatorService extends BasicSimulatorService implements
//		Granting
//{
//
//	/** */
//	private static final long serialVersionUID = 1L;
//
//	/** */
//	private final Subject<SimTime, SimTime> grants;
//
//	/** */
//	private final ExecutorService granter = Executors.newCachedThreadPool();
//
//	/** */
//	// private static final Map<Identifier<?, ?>, Subject<Grant, Grant>> grants
//	// = Collections
//	// .synchronizedMap(new HashMap<Identifier<?, ?>, Subject<Grant, Grant>>());
//
//	/** */
//	private static final Map<Identifier<?, ?>, Subscription> releaseSubscriptions = Collections
//			.synchronizedMap(new HashMap<Identifier<?, ?>, Subscription>());
//
//	/** */
//	@InjectLogger
//	private Logger LOG;
//
//	/** */
//	private SimTime lastGrant = null;
//
//	/**
//	 * {@link GrantingSimulatorService} constructor
//	 * 
//	 * @param binder
//	 * @param timeUnit
//	 */
//	@Inject
//	protected GrantingSimulatorService(final Binder binder,
//			final ClockID clockID, final TimeUnit timeUnit)
//	{
//		super(binder, clockID, timeUnit);
//		this.lastGrant = binder.getFactory(SimTimeFactory.class).create(
//				Double.NaN, timeUnit);
//		this.grants = BehaviorSubject.create(this.lastGrant);
//	}
//
//	/** @see Timed#getTime() */
//	@Override
//	public synchronized SimTime getTime()
//	{
//		return this.lastGrant;
//	}
//
//	protected synchronized void setTime(final SimTime time)
//	{
//		if (!time.isAfter(getTime()))
//		{
//			LOG.warn(String.format("Ignoring new time %s in past (t = %s)",
//					time, getTime()));
//			return;
//		}
//		// TODO wait for releases of all previous grants first !
//		this.granter.submit(new Runnable()
//		{
//			@Override
//			public void run()
//			{
//				lastGrant = time;
//				grants.onNext(time);
//			}
//		});
//	}
//
//	protected synchronized Subject<Grant, Grant> fetchGrants(
//			final Grantable<?> target)
//	{
//		final Identifier<?, ?> id = target.getID();
//		Subject<Grant, Grant> result = grants.get(id);
//		if (!grants.containsKey(id))
//		{
//			// create source for Grant(s)
//			result = ReplaySubject.create();
//			grants.put(id, result);
//
//			// subscribe to Grant release(s)
//			releaseSubscriptions.put(id,
//					target.getReleased().subscribe(new GrantObserver()));
//		}
//		return result;
//	}
//
//	/** @see Granting#requestGrant(Grantable,SimTime) */
//	@Override
//	public synchronized Observable<Grant> requestGrant(
//			final Grantable<?> target, final SimTime time)
//	{
//		final Subject<Grant, Grant> result = fetchGrants(target);
//
//		// FIXME wait for other (remote) Grantables to reach/exceed the same
//		// time
//
//		this.lastGrant = time;
//		result.onNext(new Grant()
//		{
//			@Override
//			public SimTime getTime()
//			{
//				return time;
//			}
//
//			@Override
//			public Identifier<?, ?> getGrantableID()
//			{
//				return target.getID();
//			}
//		});
//
//		return result.asObservable();
//	}
//
//	/** @see SchedulerService#schedule(Job, Trigger) */
//	@Override
//	public <ID extends Identifier<?, ?>> void schedule(final Job<ID> job,
//			final Trigger<SimTime> trigger) throws Exception
//	{
//		final Grantable<ID> target = new Grantable<ID>()
//		{
//
//			@Override
//			public ID getID()
//			{
//				return job.getID();
//			}
//
//			@Override
//			public int compareTo(final Identifiable<ID> o)
//			{
//				return job.compareTo(o);
//			}
//
//			@Override
//			public Observable<Grant> getReleased()
//			{
//				ReplaySubject<Grant> subject = ReplaySubject.create();
//				job.activate();
//				subject.onNext(new Grant()
//				{
//
//					@Override
//					public SimTime getTime()
//					{
//						return trigger.getStartTime();
//					}
//
//					@Override
//					public Identifier<?, ?> getGrantableID()
//					{
//						return job.getID();
//					}
//				});
//				subject.onCompleted();
//				return subject.asObservable();
//			}
//		};
//		requestGrant(target, trigger.getStartTime());
//	}
//
//	/**
//	 * {@link GrantObserver}
//	 * 
//	 * @version $Revision: 296 $
//	 * @author <a href="mailto:Rick@almende.org">Rick</a>
//	 *
//	 */
//	private static class GrantObserver implements Observer<Grant>
//	{
//		/** */
//		private static final Logger LOG = LogUtil
//				.getLogger(GrantObserver.class);
//
//		/** @see Observer#onError(Throwable) */
//		@Override
//		public void onError(final Throwable t)
//		{
//			t.printStackTrace();
//		}
//
//		/** @see Observer#onNext(Object) */
//		@Override
//		public void onNext(final Grant released)
//		{
//			LOG.trace("Observed release of grant for time: "
//					+ released.getTime());
//
//			// FIXME notify other (remotely) pending Grantables
//
//			grants.remove(released.getGrantableID()).onCompleted();
//			synchronized (releaseSubscriptions)
//			{
//				if (releaseSubscriptions.containsKey(released.getGrantableID()))
//					releaseSubscriptions.remove(released.getGrantableID())
//							.unsubscribe();
//			}
//		}
//
//		/** @see Observer#onCompleted() */
//		@Override
//		public void onCompleted()
//		{
//			LOG.trace("No more releases");
//		}
//	}
//
//}
