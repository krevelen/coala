/* $Id: 4f36f802c91e5a1f2427b0ab9afe4cd6fd5a6f15 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/role/AbstractExecutor.java $
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
 * Copyright (c) 2010-2014 Almende B.V. 
 */
package io.coala.enterprise.role;

import io.coala.bind.Binder;
import io.coala.enterprise.fact.CoordinationFact;
import io.coala.log.InjectLogger;

import javax.inject.Inject;

import org.slf4j.Logger;

/**
 * {@link AbstractExecutor}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * @param <F> the (super)type of {@link CoordinationFact} being handled
 */
public abstract class AbstractExecutor<F extends CoordinationFact> extends
		AbstractActorRole<F> implements Executor<F>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AbstractExecutor} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected AbstractExecutor(final Binder binder)
	{
		super(binder);
	}

	/** @see Executor#onRequested(CoordinationFact) */
	protected abstract void onRequested(F request);

	/** @see Executor#onCancelledRequest(CoordinationFact) */
	protected void onCancelledRequest(final F cancel)
	{
		logIgnore(cancel, false);
	}

	/** @see Executor#onExpiredPromise(CoordinationFact) */
	protected void onExpiredPromise(final F promise)
	{
		logIgnore(promise, true);
	}

	/** @see Executor#onExpiredPromiseCancellation(CoordinationFact) */
	protected void onExpiredPromiseCancellation(final F cancel)
	{
		logIgnore(cancel, true);
	}

	/** @see Executor#onAllowedPromiseCancellation(CoordinationFact) */
	protected void onAllowedPromiseCancellation(final F allow)
	{
		logIgnore(allow, false);
	}

	/** @see Executor#onRefusedPromiseCancellation(CoordinationFact) */
	protected void onRefusedPromiseCancellation(final F refuse)
	{
		logIgnore(refuse, false);
	}

	/** @see Executor#onExpiredState(CoordinationFact) */
	protected void onExpiredState(final F state)
	{
		logIgnore(state, true);
	}

	/** @see Executor#onExpiredStateCancellation(CoordinationFact) */
	protected void onExpiredStateCancellation(final F cancel)
	{
		logIgnore(cancel, true);
	}

	/** @see Executor#onAllowedStateCancellation(CoordinationFact) */
	protected void onAllowedStateCancellation(final F allow)
	{
		logIgnore(allow, false);
	}

	/** @see Executor#onRefusedStateCancellation(CoordinationFact) */
	protected void onRefusedStateCancellation(final F refuse)
	{
		logIgnore(refuse, false);
	}

	/** @see Executor#onAccepted(CoordinationFact) */
	protected void onAccepted(final F accept)
	{
		logIgnore(accept, false);
	}

	/** @see Executor#onCancelledAccept(CoordinationFact) */
	protected void onCancelledAccept(final F cancel)
	{
		logIgnore(cancel, false);
	}

	/** @see Executor#onRejected(CoordinationFact) */
	protected void onRejected(final F reject)
	{
		logIgnore(reject, false);
	}

}
