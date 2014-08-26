package io.coala.experimental.grant;
//package com.almende.service.time;
//
//import java.util.Collections;
//import java.util.SortedSet;
//import java.util.TreeSet;
//
//import org.apache.log4j.Logger;
//
//import com.almende.lifecycle.AbstractLifeCycle;
//import com.almende.service.BasicServiceStatus;
//import com.almende.service.ServiceStatus;
//import com.almende.service.time.ChronosService.GrantCallback;
//import com.almende.time.Instant;
//
//public class ChronosClient<I extends Instant<I>, THIS extends ChronosClient<I, THIS>>
//		extends AbstractLifeCycle<BasicServiceStatus, THIS> implements
//		ClockService<I, THIS>
//{
//	/** */
//	private static final Logger LOG = Logger.getLogger(ChronosClient.class);
//
//	private final ChronosService<I> chronos;
//
//	private final PacedAgent<I, THIS> clientAgent;
//
//	private boolean requestedGrant = false;
//
//	private ClientGrantCallback agentCallback;
//
//	private I lastGrant = null; // Long.MIN_VALUE
//
//	private I requestTime = null; // Long.MAX_VALUE;
//
//	private SortedSet<Long> futureGrantRequests;
//
//	private boolean dead = false;
//
//	public ChronosClient(final ChronosService<I> service, 
//			final PacedAgent<I, THIS> clientAgent)
//	{
//		setStatus(BasicServiceStatus.CREATED);
//		this.chronos = service;
//		this.clientAgent = clientAgent;
//		this.futureGrantRequests = Collections
//				.synchronizedSortedSet(new TreeSet<Long>());
//	}
//
//	/**
//	 * @return the lastGrant
//	 */
//	public I getTime()
//	{
//		return this.lastGrant;
//	}
//
//	/**
//	 * @param lastGrant the lastGrant to set
//	 */
//	public void setLastGrant(final I lastGrant)
//	{
//		this.lastGrant = lastGrant;
//	}
//
//	public class ClientGrantCallback implements GrantCallback
//	{
//		private final PacedAgent<I, THIS> owner;
//
//		private final long time;
//
//		public ClientGrantCallback(final PacedAgent<I, THIS> owner, final Long time)
//		{
//			this.owner = owner;
//			this.time = time;
//		}
//
//		@Override
//		public Long getTime()
//		{
//			return this.time;
//		}
//
//		@Override
//		public boolean emptyInbox()
//		{
//			LOG.info(this.owner.getAID() + " is ordered to empty its inbox.");
//			return this.owner.doEmptyInbox();
//		}
//
//		@Override
//		public void granted(final I time)
//		{
//			if (time > this.time)
//			{
//				LOG.info("Grant was further in future than expected "
//						+ getTime() + " for " + time + " "
//						+ this.owner.getAID());
//				ChronosClient.this.lastGrant = time;
//			} else
//			{
//				LOG.info(this.owner.getAID() + " receives grant for: " + time);
//				ChronosClient.this.lastGrant = getTime();
//			}
//			Long newRequest = popNextGrantTime(ChronosClient.this.lastGrant);
//			this.owner.doExecuteUntilGrant(ChronosClient.this.lastGrant);
//			if (ChronosClient.this.lastGrant < newRequest)
//				addGrantRequest(newRequest);
//		}
//
//		@Override
//		public void appocalypseHandler()
//		{
//			if (!dead)
//			{
//				ChronosClient.this.dead = true;
//				this.owner.die();
//			}
//		}
//	};
//
//	private long popNextGrantTime(final Long time)
//	{
//		if (!futureGrantRequests.isEmpty())
//		{
//			Long found = futureGrantRequests.first();
//			if (found.compareTo(time) > 0)
//			{
//				LOG.info("CANDIDATE:" + found);
//				futureGrantRequests.remove(found);
//				return found;
//			}
//		}
//		return time;
//	}
//
//	/** @see LifeCycle#initialize() */
//	@Override
//	public synchronized void initialize() throws Exception
//	{
//		setStatus(BasicServiceStatus.INITIALIZED);
//	}
//
//	@Override
//	public synchronized void start()
//	{
//		this.chronos.setGranting(true);
//		setStatus(BasicServiceStatus.STARTED);
//	}
//
//	public synchronized void stop()
//	{
//		this.chronos.setGranting(false);
//	}
//
//	public void setIsFakeAgent(boolean fake)
//	{
//		this.chronos.setFakePlatform(true);
//	}
//
//	public synchronized void addGrantRequest(final long millis)
//	{
//		// keep the earliest time to request a grant for
//		Long t = Math.min(Math.max(this.lastGrant, this.requestTime), millis);
//		if (t < millis && millis > this.lastGrant)
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " kept original earlier grant " + t
//					+ " and queued later grant request for: " + millis);
//			this.futureGrantRequests.add(millis);
//		} else if (t > this.lastGrant)
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " adding grant request for time: " + t);
//			this.requestTime = t;
//		} else
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " ignoring grant request for time: " + t);
//		}
//	}
//
//	public synchronized void doRequestGrant()
//	{
//		if (this.dead)
//			return;
//		if (this.requestedGrant)
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " ignore grant request: emptying inbox");
//			// return;
//		}
//		if (this.requestTime <= this.lastGrant)
//			this.requestTime = popNextGrantTime(this.lastGrant);
//		if (this.requestTime == Long.MAX_VALUE)
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " ignore grant request: not required");
//			return;
//		}
//		this.agentCallback = new ClientGrantCallback(this.clientAgent,
//				this.requestTime);
//		LOG.info(this.clientAgent.getAID() + " requests grant for time: "
//				+ this.requestTime);
//		this.requestedGrant = true;
//		this.chronos
//				.requestGrant(this.clientAgent.getAID(), this.agentCallback);
//		this.requestTime = Long.MAX_VALUE;
//	}
//
//	public void disconnect()
//	{
//		LOG.info(this.clientAgent.getAID()
//				+ " removes itself from the federation.");
//		this.chronos.removeFromFederation(this.clientAgent.getAID());
//	}
//
//	public synchronized void notifyInboxIsEmpty()
//	{
//		if (this.requestedGrant)
//		{
//			LOG.info(this.clientAgent.getAID()
//					+ " notifies chronos that it's inbox is empty.");
//			this.requestedGrant = false;
//			chronos.inboxEmptied(this.clientAgent.getAID());
//		} else
//		{
//			LOG.warn(this.clientAgent.getAID()
//					+ " ignoring notify for chronos that it's inbox is empty.");
//		}
//	}
//
//}
