package io.coala.experimental.grant;

import io.coala.agent.AgentID;
import io.coala.capability.replicate.ReplicationConfig;
import io.coala.experimental.grant.ChronosService.GrantCallback;
import io.coala.log.LogUtil;
import io.coala.time.SimTime;
import io.coala.time.SimTimeFactory;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;

public class ChronosClient
{
	public interface PacedAgent
	{
		AgentID getAID();

		boolean doEmptyInbox();

		void doExecuteUntilGrant(SimTime timeMS);

		// void die();
	}

	/** */
	private static final Logger LOG = LogUtil.getLogger(ChronosClient.class);

	private final ChronosService chronos;

	private final PacedAgent clientAgent;

	private boolean requestedGrant = false;

	private ClientGrantCallback agentCallback;

	private SimTime lastGrant = null; // min

	private final SortedSet<SimTime> futureGrantRequests = Collections
			.synchronizedSortedSet(new TreeSet<SimTime>());

	private SimTime requestTime = null; // max

	private final SimTime maxValue;

	// private boolean dead = false;

	public ChronosClient(final SimTimeFactory timeFact,
			final ReplicationConfig config, final PacedAgent clientAgent)
	{
		this.chronos = ChronosService.getInstance(timeFact, config);
		this.clientAgent = clientAgent;
		this.lastGrant = timeFact.create(Double.NaN, config.getBaseTimeUnit());
		this.maxValue = timeFact.create(Double.MAX_VALUE,
				config.getBaseTimeUnit());
		this.requestTime = this.maxValue;
	}

	/**
	 * @return the lastGrant
	 */
	public SimTime getLastGrant()
	{
		return lastGrant;
	}

	/**
	 * @param lastGrant the lastGrant to set
	 */
	public void setLastGrant(SimTime lastGrant)
	{
		this.lastGrant = lastGrant;
	}

	public class ClientGrantCallback implements GrantCallback
	{
		private final PacedAgent owner;

		private final SimTime time;

		public ClientGrantCallback(final PacedAgent owner, final SimTime time)
		{
			this.owner = owner;
			this.time = time;
		}

		@Override
		public SimTime getTime()
		{
			return this.time;
		}

		@Override
		public boolean emptyInbox()
		{
			LOG.info(this.owner.getAID() + " is ordered to empty its inbox.");
			return this.owner.doEmptyInbox();
		}

		@Override
		public void granted(final SimTime time)
		{
			if (time.isAfter(this.time))
			{
				LOG.info("Grant was further in future than expected "
						+ getTime() + " for " + time + " "
						+ this.owner.getAID());
				ChronosClient.this.lastGrant = time;
			} else
			{
				LOG.info(this.owner.getAID() + " receives grant for: " + time);
				ChronosClient.this.lastGrant = getTime();
			}
			SimTime newRequest = popNextGrantTime(ChronosClient.this.lastGrant);
			this.owner.doExecuteUntilGrant(ChronosClient.this.lastGrant);
			if (ChronosClient.this.lastGrant.isBefore(newRequest))
				addGrantRequest(newRequest);
		}

		// @Override
		// public void appocalypseHandler()
		// {
		// if (!dead) {
		// ChronosClient.this.dead = true;
		// this.owner.die();
		// }
		// }
	};

	private SimTime popNextGrantTime(final SimTime time)
	{
		if (!futureGrantRequests.isEmpty())
		{
			SimTime found = futureGrantRequests.first();
			if (found.compareTo(time) > 0)
			{
				LOG.info("CANDIDATE:" + found);
				futureGrantRequests.remove(found);
				return found;
			}
		}
		return time;
	}

	public void pause()
	{
		this.chronos.setGranting(false);
	}

	public void play()
	{
		this.chronos.setGranting(true);
	}

	public synchronized void addGrantRequest(final SimTime millis)
	{
		// keep the earliest time to request a grant for
		SimTime t = this.lastGrant.max(this.requestTime).min(millis);
		if (t.isBefore(millis) && millis.isAfter(this.lastGrant))
		{
			LOG.info(this.clientAgent.getAID()
					+ " kept original earlier grant " + t
					+ " and queued later grant request for: " + millis);
			this.futureGrantRequests.add(millis);
		} else if (t.isAfter(this.lastGrant))
		{
			LOG.info(this.clientAgent.getAID()
					+ " adding grant request for time: " + t);
			this.requestTime = t;
		} else
		{
			LOG.info(this.clientAgent.getAID()
					+ " ignoring grant request for time: " + t);
		}
	}

	public synchronized void doRequestGrant()
	{
		// if (this.dead)
		// return;
		if (this.requestedGrant)
		{
			LOG.info(this.clientAgent.getAID()
					+ " ignore grant request: emptying inbox");
			// return;
		}
		if (this.requestTime.isOnOrBefore(this.lastGrant))
			this.requestTime = popNextGrantTime(this.lastGrant);
		if (this.requestTime.equals(this.maxValue))
		{
			LOG.info(this.clientAgent.getAID()
					+ " ignore grant request: not required");
			return;
		}
		this.agentCallback = new ClientGrantCallback(this.clientAgent,
				this.requestTime);
		LOG.info(this.clientAgent.getAID() + " requests grant for time: "
				+ this.requestTime);
		this.requestedGrant = true;
		this.chronos
				.requestGrant(this.clientAgent.getAID(), this.agentCallback);
		this.requestTime = this.maxValue;
	}

	public void disconnect()
	{
		LOG.info(this.clientAgent.getAID()
				+ " removes itself from the federation.");
		this.chronos.removeFromFederation(this.clientAgent.getAID());
	}

	public synchronized void notifyInboxIsEmpty()
	{
		if (this.requestedGrant)
		{
			LOG.info(this.clientAgent.getAID()
					+ " notifies chronos that it's inbox is empty.");
			this.requestedGrant = false;
			chronos.inboxEmptied(this.clientAgent.getAID());
		} else
		{
			LOG.warn(this.clientAgent.getAID()
					+ " ignoring notify for chronos that it's inbox is empty.");
		}
	}

}
