/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/ProcedureCall.java $
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
package io.coala.invoke;

import io.coala.event.AbstractEvent;
import io.coala.event.EventID;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.exception.CoalaRuntimeException;
import io.coala.model.ModelComponent;
import io.coala.model.ModelComponentID;
import io.coala.process.BasicProcessStatus;
import io.coala.time.Instant;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import com.eaio.uuid.UUID;

/**
 * {@link ProcedureCall}
 * 
 * @date $Date: 2014-06-19 15:17:32 +0200 (Thu, 19 Jun 2014) $
 * @version $Revision: 308 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <ID> the type of {@link EventID}
 * @param <THIS> the concrete type of {@link ProcedureCall}
 */
public class ProcedureCall<ID extends EventID<?>> extends AbstractEvent<ID>
		implements Callable<Void>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private ProcedureID<?> procedureID;

	/** */
	private List<Object> arguments;

	/**
	 * {@link AbstractEvent} constructor
	 * 
	 * @param id
	 * @param producerID
	 */
	protected ProcedureCall(final ID id, final ModelComponent<?> producer,
			final ProcedureID<?> procedureID, final Object... args)
	{
		super(id, producer);

		this.procedureID = procedureID;
		if (args == null || args.length == 0)
			this.arguments = null;// Collections.emptyList();
		else
			this.arguments = Arrays.asList(args);

		setStatus(BasicProcessStatus.INITIALIZED);
	}

	/**
	 * {@link AbstractEvent} zero-arg serializable constructor
	 */
	protected ProcedureCall()
	{
		super();
	}

	protected List<Object> getArguments()
	{
		return this.arguments;
	}

	protected ModelComponent<?> getTarget()
	{
		return TARGET_CACHE.get(this.procedureID);
	}

	protected String getMethodReference()
	{
		return this.procedureID.getValue();
	}

	/** */
	private boolean called = false;

	/** @see Callable#call() */
	@Override
	public synchronized Void call()
	{
		if (this.called)
			return null;

		this.called = true;
		try
		{
			setStatus(BasicProcessStatus.ACTIVE);
			activate();
			setStatus(BasicProcessStatus.COMPLETE);
			finish();
			setStatus(BasicProcessStatus.FINISHED);
			return null;
		} catch (final CoalaRuntimeException t)
		{
			throw t;
		} catch (final Throwable t)
		{
			setStatus(BasicProcessStatus.FAILED);
			throw CoalaExceptionFactory.INVOCATION_FAILED.createRuntime(t,
					this.procedureID.getValue(), getTarget(), this.arguments);
		}
	}

	public Callable<Object> toCallable()
	{
		return Schedulable.Util.toCallable(getMethodReference(), getTarget(),
				getArguments());
	}

	/** @see AbstractEvent#activate() */
	@Override
	public void activate()
	{
		try
		{
			Schedulable.Util.call(getMethodReference(), getTarget(),
					getArguments());
		} catch (final CoalaRuntimeException t)
		{
			throw t;
		} catch (final Throwable t)
		{
			throw CoalaExceptionFactory.INVOCATION_FAILED.createRuntime(t,
					this.procedureID.getValue(), getTarget(), this.arguments);
		}
	}

	/** */
	protected static final Map<ProcedureID<?>, ModelComponent<?>> TARGET_CACHE = Collections
			.synchronizedMap(new HashMap<ProcedureID<?>, ModelComponent<?>>());

	/** */
	protected static long ID_COUNT = 0; // TODO use UUID instead?

	/**
	 * @param source the {@link ModelComponent} calling the procedure, or
	 *        {@code null}
	 * @param target the {@link ModelComponent} performing the procedure
	 * @param schedulableMethodID the {@link Schedulable} identifier
	 * @param arguments the procedure/method's arguments (if any)
	 * @return the procedure call
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static <ID extends EventID<String>, T extends ModelComponentID<?>> ProcedureCall<ID> create(
			final ModelComponent<?> source, final ModelComponent<T> target,
			final String schedulableMethodID, final Object... arguments)
	{
		final ProcedureID procedureID = new ProcedureID(schedulableMethodID,
				target);
		TARGET_CACHE.put(procedureID, target);
		final String idValue = new UUID().toString();//ProcedureCall.class.getSimpleName() + (ID_COUNT++);
		final ProcedureCall<ID> result = new ProcedureCall<ID>(
				(ID) new EventID(source.getID().getModelID(), idValue), source,
				procedureID, arguments);
		return result;
	}

	/** @see ModelComponent#getTime() */
	@Override
	public Instant<?> getTime()
	{
		return null;
	}

}
