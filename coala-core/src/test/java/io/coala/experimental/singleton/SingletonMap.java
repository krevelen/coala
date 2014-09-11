/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/util/SingletonMap.java $
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
package io.coala.experimental.singleton;

import io.coala.log.LogUtil;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * {@link SingletonMap}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class SingletonMap
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(SingletonMap.class);

	/**
	 * {@link Singleton}
	 * 
	 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
	 * @version $Revision: 296 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public interface Singleton
	{
		/** @return the context identifier used to map this singleton */
		Serializable getKey();
	}

	/** */
	private final static Map<Serializable, Map<Class<? extends Singleton>, Singleton>> SINGLETONS = new HashMap<Serializable, Map<Class<? extends Singleton>, Singleton>>();

	/**
	 * @param singleton the {@link Singleton} to maintain in this JVM
	 * @return the {@link Singleton} again
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T extends Singleton> T set(final T singleton)
	{
		if (!SINGLETONS.containsKey(singleton.getKey()))
		{
			LOG.info("Adding singleton set for key: " + singleton.getKey());
			SINGLETONS.put(singleton.getKey(),
					new HashMap<Class<? extends Singleton>, Singleton>());
		}

		final Singleton oldVersion = SINGLETONS.get(singleton.getKey()).put(
				singleton.getClass(), singleton);
		if (oldVersion != null)
			LOG.warn("Replaced singleton key: " + oldVersion.getKey()
					+ " with type: " + singleton.getClass() + " for key: "
					+ singleton.getKey());
		else
			LOG.info("Added singleton type: " + singleton.getClass()
					+ " for key: " + singleton.getKey());
		return (T) get(singleton.getKey(), singleton.getClass());
	}

	/**
	 * @param key the {@link Singleton} identifier
	 * @param type the {@link Singleton} subclass
	 * @return {@code true} if exists, {@code false} otherwise
	 */
	public synchronized static boolean has(final Serializable key,
			final Class<? extends Singleton> type)
	{
		return SINGLETONS.containsKey(key)
				&& SINGLETONS.get(key).containsKey(type);
	}

	/**
	 * @param key the {@link Singleton} identifier
	 * @param type the {@link Singleton} subclass
	 * @return the {@link Singleton} for specified {@code key}
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T extends Singleton> T get(
			final Serializable key, final Class<T> type)
	{
		if (!SINGLETONS.containsKey(key))
		{
			// LOG.warn("No singletons cached for key: " + key);
			return null;
		}
		return (T) SINGLETONS.get(key).get(type);
	}
}
