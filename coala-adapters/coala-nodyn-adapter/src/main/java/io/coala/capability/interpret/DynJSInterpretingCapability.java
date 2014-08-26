package io.coala.capability.interpret;

import io.coala.agent.AgentStatusObserver;
import io.coala.agent.AgentStatusUpdate;
import io.coala.agent.BasicAgentStatus;
import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.capability.configure.ConfiguringCapability;
import io.coala.capability.interpret.InterpretingCapability;
import io.coala.log.InjectLogger;
import io.coala.message.Message;
import io.coala.nodyn.NodynRunner;
import io.coala.resource.ResourceStreamer;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.dynjs.runtime.DynObject;
import org.dynjs.runtime.ExecutionContext;
import org.dynjs.runtime.Types;
import org.dynjs.runtime.wrapper.JavascriptFunction;

import rx.Observable;
import rx.Observer;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link DynJSInterpretingCapability}
 * 
 * @version $Revision: 360 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * @author <a href="mailto:Suki@almende.org">Suki</a>
 */
public class DynJSInterpretingCapability extends BasicCapability implements InterpretingCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	@InjectLogger
	private Logger LOG;

	/** */
	@SuppressWarnings("rawtypes")
	@Inject
	@Named(Binder.AGENT_TYPE)
	private Class ownerType;

	/** the represented/wrapped object */
	private DynObject object = null;

	/**
	 * {@link DynJSInterpretingCapability} constructor
	 * 
	 * @param binder
	 */
	@Inject
	private <T extends Message<?>> DynJSInterpretingCapability(final Binder binder)
	{
		super(binder);
	}

	@JsonIgnore
	protected DynJSAgentManager getAgentManager()
	{
		return DynJSAgentManager.getInstance(getBinder());
	}

	@Override
	public Observable<Object> eval(final ResourceStreamer scripts)
	{
		return NodynRunner.eval(scripts);
	}

	// @Override
	public Observable<AgentStatusUpdate> init(final Object me)
	{
		final DynJSAgentManager mgr = getAgentManager();
		@SuppressWarnings("unchecked")
		final Observable<AgentStatusUpdate> obs = mgr.boot(getID()
				.getClientID(), this.ownerType);
		// TODO push/call EveJS agent's status handler
		obs.subscribe(new AgentStatusObserver()
		{
			@Override
			public void onError(final Throwable e)
			{
				put(PROXY_STATUS_PROPERTY, e.getMessage());
			}

			@Override
			public void onCompleted()
			{
				put(PROXY_STATUS_PROPERTY, "completed");
			}

			@Override
			public void onNext(final AgentStatusUpdate t)
			{
				put(PROXY_STATUS_PROPERTY, t.getStatus().toString());
				LOG.trace("Status update from COALA proxy to owner agent: "
						+ t.getStatus());
			}
		});
		if (me instanceof DynObject == false)
		{
			LOG.warn("Must initialize an Object type, but got: "
					+ (me instanceof Types ? ((Types) me).toString() : me
							.getClass().getName()));
			return obs;
		}
		this.object = (DynObject) me;
		LOG.trace("Initialized " + this.object.getClassName()
				+ ", properties: " + this.object.getOwnPropertyNames().toList());
		put(PROXY_STATUS_PROPERTY, BasicAgentStatus.CREATED.toString());
		put(MODEL_ID_PROPERTY, getID().getModelID());
		put(LOG_PROPERTY, LOG);
		getBinder().rebind(ConfiguringCapability.class,
				getBinder().inject(ConfiguringCapability.class));

		mgr.updateWrapperAgentStatus(getID().getClientID(),
				BasicAgentStatus.INITIALIZED);
		mgr.updateWrapperAgentStatus(getID().getClientID(),
				BasicAgentStatus.ACTIVE);
		return obs;
	}

	@Override
	public Object getAttribute(final String name)
	{
		return this.object.get(name);
	}

	@Override
	public List<String> listAttributeNames()
	{
		return this.object.getOwnPropertyNames().toList();
	}

	@Override
	public Object put(final String key, final Object value)
	{
		return this.object.put(key, value);
	}

	@Override
	public <T> void subscribe(final Observable<T> observable,
			final Object onNext, final Object onError, final Object onCompleted)
	{
		final ExecutionContext context = ExecutionContext
				.createGlobalExecutionContext(NodynRunner.getRuntime());
		// .createFunctionExecutionContext(((JavascriptFunction)onNext).,
		// onNext, null, "test");
		/*
		try
		{
			context.setFunctionParameters(new Object[] { 
					context.createPropertyReference("testval", "someName")
					
					//new DynObject()
			
					// GlobalObject.newGlobalObject(NodynRunner.getRuntime())
			});
			((JavascriptFunction) onNext).call(context);
		} catch (final Exception e1)
		{
			e1.printStackTrace();
		}
		*/
		if (observable == null)
			throw new NullPointerException("Can't subscribe to null-observer");

		for (Object callback : Arrays.asList(onNext, onError, onCompleted))
			if (callback != null
					&& callback instanceof JavascriptFunction == false)
				throw new IllegalStateException("UNEXPECTED callback type: "
						+ callback.getClass().getName());

		observable.subscribe(new Observer<T>()
		{
			@Override
			public void onCompleted()
			{
				if (onCompleted != null)
					((JavascriptFunction) onCompleted).call(context);
			}

			@Override
			public void onError(final Throwable e)
			{
				if (onError == null)
					return;
				context.setFunctionParameters(new Object[] { e });
				((JavascriptFunction) onError).call(context);
			}

			@Override
			public void onNext(final T t)
			{
				if (onNext == null)
					return;
				context.setFunctionParameters(new Object[] { t });
				((JavascriptFunction) onNext).call(context);
			}
		});

	}
}