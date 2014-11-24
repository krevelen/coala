/* $Id: 107ee0a96bd4034cf8be123e21f58331a7b67137 $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/AbstractService.java $
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
package io.coala.capability;

import io.coala.agent.Agent;
import io.coala.agent.AgentStatusObserver;
import io.coala.agent.AgentStatusUpdate;
import io.coala.bind.Binder;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.lifecycle.AbstractLifeCycle;
import io.coala.lifecycle.ActivationType;
import io.coala.lifecycle.LifeCycle;
import io.coala.lifecycle.LifeCycleHooks;
import io.coala.lifecycle.MachineUtil;
import io.coala.log.LogUtil;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import rx.Observer;

/**
 * {@link AbstractCapability}
 * 
 * @date $Date: 2014-08-08 16:20:51 +0200 (Fri, 08 Aug 2014) $
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public abstract class AbstractCapability<ID extends CapabilityID> extends
		AbstractLifeCycle<ID, BasicCapabilityStatus> implements
		Capability<BasicCapabilityStatus>, AgentStatusObserver, LifeCycleHooks
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private Logger LOG;

	/** */
	private transient Binder binder;

	/**
	 * {@link AbstractCapability} constructor
	 * 
	 * @param id
	 */
	@Inject
	protected AbstractCapability(final ID id, final Binder binder)
	{
		super(id);
		this.binder = binder;
	}

	/** @see Agent#getBinder() */
	protected final Binder getBinder()
	{
		return this.binder;
	}

	/** */
	private boolean initialized = false;

	/** */
	private boolean active = false;

	/** */
	private boolean complete = false;

	/** */
	private boolean finalized = false;

	/** @see Observer#onError(Throwable) */
	@Override
	public final void onError(final Throwable t)
	{
		t.printStackTrace();
	}

	@Override
	protected void setStatus(final BasicCapabilityStatus status)
	{
		MachineUtil.setStatus(this, status,
				status.isFinishedStatus() || status.isFailedStatus());
	}

	/** @see LifeCycle#getActivationType() */
	@Override
	public final ActivationType getActivationType()
	{
		return ActivationType.ACTIVATE_ONCE; // default behavior
	}

	/** @see Observer#onNext(Object) */
	@Override
	public synchronized final void onNext(final AgentStatusUpdate update)
	{
		if (LOG == null)
			LOG = LogUtil.getLogger(AbstractCapability.class.getName(), this);

		if (this.complete && !update.getStatus().isFinishedStatus()
				&& !update.getStatus().isFailedStatus())
		{
			LOG.warn(getID()
					+ " already complete, ignoring owner status update: "
					+ update.getStatus());
			return;
		}

		if (this.finalized)
		{
			LOG.warn(getID()
					+ " already finalized, ignoring owner status update: "
					+ update.getStatus());
			return;
		}

		try
		{
			LOG.trace("Got owner update: " + update.getStatus());
			if (update.getStatus().isCreatedStatus())
			{
				if (!initialized)
				{
					initialize();
					this.initialized = true;
					setStatus(BasicCapabilityStatus.INITIALIZED);
				}
				// else
				// LOG.warn("already initialized!");
			} else if (update.getStatus().isInitializedStatus()
					|| update.getStatus().isActiveStatus())
			{
				LOG.trace("initialized or activated");
				if (!initialized)
				{
					// LOG.warn(getID() + " should already be initialized!");
					initialize();
					this.initialized = true;
					setStatus(BasicCapabilityStatus.INITIALIZED);
				}
				if (getActivationType() == ActivationType.ACTIVATE_NEVER)
					return;
				LOG.trace("activating...");
				this.active = true;
				setStatus(BasicCapabilityStatus.STARTED);
				activate();
			} else if (update.getStatus().isPassiveStatus())
			{
				if (!initialized)
				{
					// LOG.warn(getID() + " should already be initialized!");
					initialize();
					this.initialized = true;
					setStatus(BasicCapabilityStatus.INITIALIZED);
					if (getActivationType() != ActivationType.ACTIVATE_NEVER)
					{
						this.active = true;
						setStatus(BasicCapabilityStatus.STARTED);
						activate();
					}
				}
				if (getActivationType() == ActivationType.ACTIVATE_NEVER)
					return;
				if (this.active)
				{
					this.active = false;
					deactivate();
					setStatus(BasicCapabilityStatus.PAUSED);
				}
				if (getActivationType() == ActivationType.ACTIVATE_AND_FINISH)
				{
					this.complete = true;
					setStatus(BasicCapabilityStatus.COMPLETE);
					finish();
					setStatus(BasicCapabilityStatus.FINISHED);
				}
			} else if (update.getStatus().isCompleteStatus()
					|| update.getStatus().isFinishedStatus()
					|| update.getStatus().isFailedStatus())
			{
				if (this.active)
				{
					LOG.warn(getID() + " should no longer be active!");
					this.active = false;
					deactivate();
					setStatus(BasicCapabilityStatus.PAUSED);
				}
				if (!this.complete)
				{
					setStatus(BasicCapabilityStatus.COMPLETE);
					this.complete = true;
				}
				if (!this.finalized)
				{
					finish();
					setStatus(BasicCapabilityStatus.FINISHED);
					this.finalized = true;
				}
			}
			return;
		} catch (final Throwable t)
		{
			setStatus(BasicCapabilityStatus.FAILED);
			this.finalized = true;
			onError(t);
			// LOG.error("Problem executing " + getID(), t);
		}
		CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime("status",
				update.getStatus(), "unexpected");
	}

	/** @see Observer#onCompleted() */
	@Override
	public final void onCompleted()
	{
		if (LOG == null)
			LOG = LogUtil.getLogger(AbstractCapability.class.getName(), this);

		LOG.trace("Cleaning up " + getID() + "...");
		if (this.finalized)
			return;
		if (this.initialized)
			try
			{
				if (this.active)
				{
					deactivate();
					this.complete = false;
				}
				if (!this.complete)
				{
					finish();
					this.finalized = true;
				}
			} catch (final Exception e)
			{
				onError(e);
			}
		LOG.trace("Done cleaning up " + getID() + "!");
	}

	@Override
	public void initialize() throws Exception
	{
		// override me
	}

	@Override
	public void activate() throws Exception
	{
		// override me (called after owner agent has initialized and/or resumed)
	}

	@Override
	public void deactivate() throws Exception
	{
		// override me (called after owner agent has paused)
	}

	@Override
	public void finish() throws Exception
	{
		// override me
	}

}
