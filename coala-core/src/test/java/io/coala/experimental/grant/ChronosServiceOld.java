package io.coala.experimental.grant;
//package com.almende.service.time;
//
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//import org.apache.log4j.Logger;
//
//import com.almende.agent.AgentID;
//import com.almende.time.Instant;
//
//public class ChronosService<I extends Instant<I>>
//{
//	/** */
//	private static final Logger LOG = Logger.getLogger(ChronosService.class);
//
//	private String replicationID;
//
//	private final Map<AgentID<?, ?>, GrantCallback> lastRequests = Collections
//			.synchronizedMap(new HashMap<AgentID<?, ?>, GrantCallback>());
//
//	private final SortedMap<Long, Set<AgentID<?, ?>>> pendingGrants = Collections
//			.synchronizedSortedMap(new TreeMap<Long, Set<AgentID<?, ?>>>());
//
//	private final Map<AgentID<?, ?>, GrantCallback> federation = Collections
//			.synchronizedMap(new HashMap<AgentID<?, ?>, GrantCallback>());
//
//	private I lastGrant = null;//Long.MIN_VALUE;
//
//	private I simulationEndTime = null;//Long.MAX_VALUE;
//
//	private boolean granting = false;
//
//	protected long SIMULATIONSTART = 0;
//
//	public ChronosService(final String replicationID)
//	{
//		if (replicationID == null)
//			throw new NullPointerException(
//					"A replicationID is mandatory for time management.");
//		this.replicationID = replicationID;
//	}
//
//	protected void setSimulationEndTime(long simulationEndTime)
//	{
//		this.simulationEndTime = simulationEndTime;
//		setGranting(true);
//	}
//
//	protected Long getSimulationEndTime()
//	{
//		return simulationEndTime;
//	}
//
//	protected void setGranting(final boolean granting)
//	{
//		this.granting = granting;
//	}
//
//	public interface GrantCallback<I extends Instant<I>>
//	{
//		I getTime();
//
//		boolean emptyInbox();
//
//		void granted(I time);
//
//		void appocalypseHandler();
//	}
//
//	public synchronized void requestGrant(final AgentID<?, ?> agent,
//			final GrantCallback<I> callback)
//	{
//		LOG.info("CHRONOS(" + replicationID + "): " + agent
//				+ "\'s grant request for time: " + callback.getTime());
//		if (this.lastGrant >= callback.getTime())
//		{
//			LOG.info("CHRONOS(" + replicationID + "): " + agent + "\'s: "
//					+ callback.getTime() + " request in past, granting: "
//					+ this.lastGrant);
//			callback.granted(this.lastGrant);
//
//			return;
//		}
//		synchronized (this.lastRequests)
//		{
//			if (!this.lastRequests.containsKey(agent))
//				this.lastRequests.put(agent, callback);
//		}
//		LOG.info("CHRONOS(" + replicationID + "): did " + agent
//				+ "\'s grant request for time: " + callback.getTime());
//
//		this.federation.put(agent, callback);
//		addGrant(agent, callback.getTime());
//		startGranting(this.pendingGrants.firstKey());
//		// final GrantCallback oldCallback = lastRequests.put(agent, callback);
//		// if (oldCallback == null)
//		// {
//		// LOG.info("Added " + agent + " to federation");
//		// addGrant(agent, callback.getTime());
//		// } else if (this.lastGrant >= callback.getTime())
//		// {
//		// LOG.info("Ignoring grant=" + callback.getTime() + " request from "
//		// + agent + ", already granted up to "
//		// + this.lastGrant);
//		// addGrant(agent, callback.getTime());
//		// lastRequests.put(agent, oldCallback);
//		// } else
//		// {
//		// LOG.info("Replaced " + agent + " grant: "
//		// + oldCallback.getTime() + " with grant: "
//		// + callback.getTime());
//		// addGrant(agent, callback.getTime());
//		// lastRequests.put(agent, oldCallback);
//		// //removeGrant(agent, oldCallback.getTime());
//		// }
//		// LOG.info("Pending: " + this.pendingGrants);
//	}
//
//	private final Set<AgentID<?, ?>> inboxEmpty = Collections
//			.synchronizedSet(new HashSet<AgentID<?, ?>>());
//
//	public void inboxEmptied(final AgentID<?, ?> agent)
//	{
//		synchronized (this.inboxEmpty)
//		{
//			this.inboxEmpty.add(agent);
//
//			// check if all inboxes are emptied for first upcoming grant time
//			synchronized (this.pendingGrants)
//			{
//				if (!this.pendingGrants.isEmpty())
//				{
//					final Long firstKey = this.pendingGrants.firstKey();
//					if (this.inboxEmpty.containsAll(this.pendingGrants
//							.get(firstKey)))
//					{
//						LOG.trace("All inboxes emptied: " + this.inboxEmpty);
//						this.inboxEmpty.clear();
//						completeGranting(firstKey);
//					}
//				}
//			}
//		}
//
//		// grant all up to upcoming grant time
//
//	}
//
//	private void completeGranting(final Long firstKey)
//	{
//		synchronized (this.pendingGrants)
//		{
//			final Set<AgentID<?, ?>> agents = this.pendingGrants
//					.remove(firstKey);
//			final long maxTime = Math.max(this.lastGrant, firstKey);
//			this.lastGrant = maxTime;
//			if (agents != null)
//				for (AgentID<?, ?> agent : agents)
//				{
//					final GrantCallback callback = this.lastRequests.get(agent);
//					LOG.info("Granting: " + maxTime + " to: " + agent);
//					callback.granted(maxTime);
//				}
//
//			// LOG.info("Pending after grant "+maxTime+": " +
//			// this.pendingGrants);
//		}
//	}
//
//	public void removeFromFederation(final AgentID<?, ?> agent)
//	{
//		if (this.federation.containsKey(agent))
//			this.federation.remove(agent).appocalypseHandler();
//		else
//			return;
//		// Iterator<Set<AID>> values = this.pendingGrants.values().iterator();
//		// while (values.hasNext()) {
//		// values.next().remove(agent);
//		// }
//		// this.lastRequests.remove(agent);
//		// this.inboxEmpty.remove(agent);
//		LOG.info("Removed " + agent + " from federation: " + this.federation);
//		if (this.federation.size() == 0)
//		{
//			LOG.info("No more federates, simulation complete!");
//		} else
//			synchronized (this.federation)
//			{
//				for (AgentID<?, ?> federate : this.federation.keySet())
//				{
//					// Update whole federation with current time.
//					this.federation.get(federate).granted(this.lastGrant);
//				}
//			}
//	}
//
//	private void addGrant(final AgentID<?, ?> agent, final Long time)
//	{
//		LOG.info("CHRONOS(" + replicationID + "): adding " + agent
//				+ "\'s grant request for time: " + time);
//
//		synchronized (this.pendingGrants)
//		{
//			final Long grant = this.pendingGrants.isEmpty() ? time : Math.max(
//					time, this.pendingGrants.firstKey());
//
//			Set<AgentID<?, ?>> agents = this.pendingGrants.get(grant);
//			if (agents == null)
//			{
//				agents = new HashSet<AgentID<?, ?>>();
//				this.pendingGrants.put(grant, agents);
//			}
//			agents.add(agent);
//		}
//	}
//
//	private void startGranting(final Long firstKey)
//	{
//		LOG.info("CHRONOS(" + replicationID + "): starting grant " + firstKey);
//		synchronized (this.lastRequests)
//		{
//			if (this.granting)
//			{
//				synchronized (this.pendingGrants)
//				{
//					for (AgentID<?, ?> federate : this.pendingGrants
//							.get(firstKey))
//						this.lastRequests.get(federate).emptyInbox();
//				}
//			}
//		}
//	}
//
//	// private boolean removeGrant(final AID agent, final Long time)
//	// {
//	// synchronized (this.pendingGrants)
//	// {
//	// Set<AID> agents = this.pendingGrants.get(time);
//	// if (agents != null)
//	// return agents.remove(agent);
//	// }
//	// return false;
//	// }
//
//	/** */
//	public Double getPercentageComplete()
//	{
//
//		Long grantedTime = this.lastGrant;
//		Long totalTime = getSimulationEndTime() - SIMULATIONSTART;
//		Long passedTime = grantedTime.longValue() - SIMULATIONSTART;
//		double percentageComplete = (passedTime.doubleValue() / totalTime
//				.doubleValue()) * 100;
//		return percentageComplete;
//	}
//
//}
