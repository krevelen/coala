/* $Id: b6e8bdc8fc2ca222c06869796b997ed04e4a6be3 $
 * $URL: https://dev.almende.com/svn/abms/enterprise-ontology/src/main/java/io/coala/enterprise/role/AbstractInitiator.java $
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
 * {@link AbstractInitiator}
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * @param <F> the (super)type of {@link CoordinationFact} being handled
 */
public abstract class AbstractInitiator<F extends CoordinationFact> extends
		AbstractActorRole<F> implements Initiator<F>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/**
	 * {@link AbstractInitiator} constructor
	 * 
	 * @param binder
	 */
	@Inject
	protected AbstractInitiator(final Binder binder)
	{
		super(binder);
	}

	/** @see Initiator#onExpiredRequest(CoordinationFact) */
	protected void onExpiredRequest(final F request)
	{
		logIgnore(request, true);
	}

	/** @see Initiator#onExpiredRequestCancellation(CoordinationFact) */
	protected void onExpiredRequestCancellation(final F cancel)
	{
		logIgnore(cancel, true);
	}

	/** @see Initiator#onAllowedRequestCancellation(CoordinationFact) */
	protected void onAllowedRequestCancellation(final F allow)
	{
		logIgnore(allow, false);
	}

	/** @see Initiator#onRefusedRequestCancellation(CoordinationFact) */
	protected void onRefusedRequestCancellation(final F refuse)
	{
		logIgnore(refuse, false);
	}

	/** @see Initiator#onPromised(CoordinationFact) */
	protected void onPromised(final F promise)
	{
		logIgnore(promise, false);
	}

	/** @see Initiator#onCancelledPromise(CoordinationFact) */
	protected void onCancelledPromise(final F cancel)
	{
		logIgnore(cancel, false);
	}

	/** @see Initiator#onDeclined(CoordinationFact) */
	protected void onDeclined(final F decline)
	{
		logIgnore(decline, false);
	}

	/** @see Initiator#onStated(CoordinationFact) */
	protected abstract void onStated(F state);

	/** @see Initiator#onCancelledState(CoordinationFact) */
	protected void onCancelledState(final F cancel)
	{
		logIgnore(cancel, false);
	}

	/** @see Initiator#onExpiredAcceptCancellation(CoordinationFact) */
	protected void onExpiredAcceptCancellation(final F cancel)
	{
		logIgnore(cancel, true);
	}

	/** @see Initiator#onAllowedAcceptCancellation(CoordinationFact) */
	protected void onAllowedAcceptCancellation(final F allow)
	{
		logIgnore(allow, false);
	}

	/** @see Initiator#onRefusedAcceptCancellation(CoordinationFact) */
	protected void onRefusedAcceptCancellation(final F refuse)
	{
		logIgnore(refuse, false);
	}

}
