package io.coala.experimental.grant;

import io.coala.agent.AgentID;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.log.LogUtil;
import io.coala.model.ModelID;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class ChronosService
{

	/** */
	private static Map<ModelID, ChronosService> theInstances = new HashMap<ModelID, ChronosService>();

	public interface GrantCallback
	{
		SimTime getTime();

		boolean emptyInbox();

		void granted(SimTime time);

	}

	/**
	 * @param config the {@link ReplicationConfig}
	 * @return the {@link ChronosService} instance
	 */
	public synchronized static ChronosService getInstance(
			final SimTimeFactory timeFact, final ReplicationConfig config)
	{
		ChronosService instance = theInstances.get(config.getReplicationID());
		if (instance == null)
		{
			instance = new ChronosService(timeFact, config);
			theInstances.put(config.getReplicationID(), instance);
		}
		return instance;
	}

	/** */
	private final Map<AgentID, GrantCallback> lastRequests = Collections
			.synchronizedMap(new HashMap<AgentID, GrantCallback>());

	/** */
	private final SortedMap<SimTime, Set<AgentID>> pendingGrants = Collections
			.synchronizedSortedMap(new TreeMap<SimTime, Set<AgentID>>());

	/** */
	private final Map<AgentID, GrantCallback> federation = Collections
			.synchronizedMap(new HashMap<AgentID, GrantCallback>());

	/** */
	private final Logger LOG;

	/** */
	private final ReplicationConfig config;

	/** */
	private SimTime lastGrant;

	/** */
	private boolean granting = true;

	private ChronosService(final SimTimeFactory timeFact,
			final ReplicationConfig config)
	{
		this.LOG = LogUtil.getLogger(getClass().getSimpleName() + " "
				+ config.getReplicationID());
		this.config = config;
		this.lastGrant = timeFact.create(Double.NaN, config.getBaseTimeUnit());
	}

	public ReplicationConfig getConfig()
	{
		return this.config;
	}

	public void setGranting(final boolean granting)
	{
		this.granting = granting;
	}

	public synchronized void requestGrant(final AgentID agent,
			final GrantCallback callback)
	{
		LOG.info(agent + " requests grant for: " + callback.getTime());
		if (!this.lastGrant.isBefore(callback.getTime()))
		{
			LOG.info(agent + "\'s: " + callback.getTime()
					+ " request in past, granting: " + this.lastGrant);
			callback.granted(this.lastGrant);

			return;
		}
		synchronized (this.lastRequests)
		{
			if (!this.lastRequests.containsKey(agent))
				this.lastRequests.put(agent, callback);
		}
		LOG.info(agent + "\'s grant request done for time: "
				+ callback.getTime());

		this.federation.put(agent, callback);
		addGrant(agent, callback.getTime());
		startGranting(this.pendingGrants.firstKey());
	}

	private final Set<AgentID> inboxEmpty = Collections
			.synchronizedSet(new HashSet<AgentID>());

	public void inboxEmptied(final AgentID agent)
	{
		synchronized (this.inboxEmpty)
		{
			this.inboxEmpty.add(agent);

			// check if all inboxes are emptied for first upcoming grant time
			synchronized (this.pendingGrants)
			{
				if (!this.pendingGrants.isEmpty())
				{
					final SimTime firstKey = this.pendingGrants.firstKey();
					if (this.inboxEmpty.containsAll(this.pendingGrants
							.get(firstKey)))
					{
						LOG.trace("All inboxes emptied: " + this.inboxEmpty);
						this.inboxEmpty.clear();
						completeGranting(firstKey);
					}
				}
			}
		}

		// grant all up to upcoming grant time

	}

	private void completeGranting(final SimTime firstKey)
	{
		synchronized (this.pendingGrants)
		{
			final Set<AgentID> agents = this.pendingGrants.remove(firstKey);
			final SimTime maxTime = this.lastGrant.max(firstKey);
			this.lastGrant = maxTime;
			if (agents != null)
				for (AgentID agent : agents)
				{
					final GrantCallback callback = this.lastRequests.get(agent);
					LOG.info("Granting: " + maxTime + " to: " + agent);
					callback.granted(maxTime);
				}
		}
	}

	public void removeFromFederation(final AgentID agent)
	{
		if (!this.federation.containsKey(agent))
			return;

		this.federation.remove(agent);// .appocalypseHandler();

		LOG.info("Removed " + agent + " from federation: " + this.federation);

		// if (this.federation.size() == 0)
		// {
		// // FIXME stop simulation !!
		// }
		// synchronized (this.federation)
		// {
		// for (AgentID federate : this.federation.keySet())
		// {
		// // Update whole federation with current time.
		// this.federation.get(federate).granted(this.lastGrant);
		// }
		// }
	}

	private void addGrant(final AgentID agent, final SimTime time)
	{
		LOG.info("Adding " + agent + "\'s grant request for time: " + time);

		synchronized (this.pendingGrants)
		{
			final SimTime grant = this.pendingGrants.isEmpty() ? time : time
					.max(this.pendingGrants.firstKey());

			Set<AgentID> agents = this.pendingGrants.get(grant);
			if (agents == null)
			{
				agents = new HashSet<AgentID>();
				this.pendingGrants.put(grant, agents);
			}
			agents.add(agent);
		}
	}

	private void startGranting(final SimTime firstKey)
	{
		LOG.info("Starting grant " + firstKey);
		synchronized (this.lastRequests)
		{
			if (this.granting)
			{
				synchronized (this.pendingGrants)
				{
					for (AgentID federate : this.pendingGrants.get(firstKey))
						this.lastRequests.get(federate).emptyInbox();
				}
			}
		}
	}

	/** */
	public Double getProgressFraction()
	{
		return this.lastGrant.toMilliseconds().doubleValue()
				/ getConfig().getDuration().getMillis();
	}

}
