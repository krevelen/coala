/* $Id: 5eb3bf4113f548d7e14d74ac511d2c18a091380f $
 * $URL: https://dev.almende.com/svn/abms/coala-nodyn-adapter/src/main/java/io/coala/nodyn/NodynRunner.java $
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
package io.coala.nodyn;

import io.coala.log.LogUtil;
import io.coala.resource.ResourceStreamer;
import io.nodyn.Nodyn;
import io.nodyn.runtime.NodynConfig;
import io.nodyn.runtime.RuntimeFactory;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;

/**
 * {@link NodynRunner}
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class NodynRunner
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(NodynRunner.class);

	/** */
	private static Nodyn nodyn = null;

	/**
	 * @return
	 */
	public synchronized static Nodyn getRuntime()
	{
		if (nodyn == null)
		{
			System.setProperty("dynjs.require.path", "node_modules");
//			final NodynConfig config = new NodynConfig();
//			// TODO import Nodyn config parameters, e.g. from properties
//			final boolean clustered = true;
//
//			config.setOutputStream(System.out);
//			config.setErrorStream(System.err);
//			config.setInvokeDynamicEnabled(true);
//			if (clustered)
//			{
//				System.setProperty("vertx.clusterManagerFactory",
//						HazelcastClusterManagerFactory.class.getName());
//				// config.setClustered(true);
//				// config.setHost("localhost");
//			}
//			// JSObject globalObject = new DynObject();
//			// globalObject.defineNonEnumerableProperty(null, "console", new
//			// ConsoleModule());
//			nodyn = new DynJSRuntime(config);
		}
		return nodyn;
	}

	/**
	 * @param path
	 * @throws Throwable 
	 */
	public static Object eval(final String source) throws Throwable
	{
		// LOG.trace("env vars: " + JsonUtil.toPrettyJSON(System.getenv()));
		LOG.trace("Evaluating script:\n" + source);

		final NodynConfig config = new NodynConfig(new String[] { "--eval",
				source });
		RuntimeFactory factory = RuntimeFactory.init(config.getClassLoader(),
				RuntimeFactory.RuntimeType.DYNJS);
		nodyn = factory.newRuntime(config);
		final Object result = nodyn.run();
		LOG.trace("Evaluated to: "
				+ (result == null ? null : result.getClass().getName()));
		return result;
	}

	/**
	 * @param path
	 */
	public static Observable<Object> eval(final ResourceStreamer script)
	{
		return eval(script.toStrings());
	}

	/**
	 * @param path
	 */
	public static Observable<Object> eval(final Observable<String> scripts)
	{
		// TODO use rxJava
		return Observable.create(new OnSubscribe<Object>()
		{
			@Override
			public void call(final Subscriber<? super Object> sub)
			{
				scripts.subscribe(new Observer<String>()
				{
					@Override
					public void onCompleted()
					{
						sub.onCompleted();
					}

					@Override
					public void onError(final Throwable e)
					{
						sub.onError(e);
					}

					@Override
					public void onNext(final String t)
					{
						try
						{
							sub.onNext(eval(t));
						} catch (final Throwable e)
						{
							sub.onError(e);
						}
					}
				});
			}
		});
	}
}
