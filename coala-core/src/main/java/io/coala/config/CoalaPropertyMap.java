/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/CoalaPropertyMap.java $
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
package io.coala.config;

import io.coala.log.LogUtil;
import io.coala.resource.FileUtil;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.configuration.Configuration;
import org.apache.log4j.Logger;

/**
 * {@link CoalaPropertyMap} extends {@link Properties} with some utility methods
 * 
 * FIXME should extend {@link Configuration} instead ?
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class CoalaPropertyMap extends Properties
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil.getLogger(CoalaPropertyMap.class);

	/** the singleton {@link CoalaProperty} instance */
	private static CoalaPropertyMap INSTANCE = null;

	/** @return the singleton {@link CoalaProperty} instance */
	public synchronized static CoalaPropertyMap getInstance()
	{
		if (INSTANCE == null)
			return getInstance(true);
		return INSTANCE;
	}

	/**
	 * @param loadFromDefaultPath whether to load from default path
	 * @return the singleton {@link CoalaProperty} instance
	 */
	public synchronized static CoalaPropertyMap getInstance(
			final boolean loadFromDefaultPath)
	{
		if (INSTANCE == null)
		{
			INSTANCE = new CoalaPropertyMap();
			if (loadFromDefaultPath)
				INSTANCE.load(ConfigUtil.PROPERTIES_FILE);
		}
		// else if (!loadFromDefaultPath && !INSTANCE.loadedPaths.isEmpty())
		// LOG.warn("Already imported BAAL config from path(s): "
		// + INSTANCE.loadedPaths);

		return INSTANCE;
	}

	private Set<String> loadedPaths = new HashSet<String>();

	/**
	 * {@link CoalaPropertyMap} constructor
	 */
	private CoalaPropertyMap()
	{
	}

	/**
	 * {@link CoalaPropertyMap} constructor
	 * 
	 * @param fileName
	 */
	public synchronized CoalaPropertyMap load(final String fileName)
	{

		final String loadPath = fileName == null ? ConfigUtil.PROPERTIES_FILE
				: fileName;

		if (this.loadedPaths.contains(loadPath))
		{
			// LOG.info("Skipping config already imported from path: " +
			// fileName);
			return this;
		} else if (!this.loadedPaths.isEmpty())
			LOG.warn("Overriding config imported previously from path(s): "
					+ this.loadedPaths + " with config from path: " + loadPath);

		try
		{
			final InputStream inStream = FileUtil
					.getFileAsInputStream(loadPath);
			load(inStream);
			this.loadedPaths.add(loadPath);
			LOG.trace("Imported config from path: " + loadPath);
		} catch (final Throwable e)
		{
			if (fileName != null)
				LOG.error("Problem importing config from path: " + loadPath, e);
		}
		return this;
	}

}
