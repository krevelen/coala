/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/ConfigUtil.java $
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
package io.coala.config;

import io.coala.util.Util;

import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

/**
 * {@link ConfigUtil}
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ConfigUtil implements Util
{

	/** */
	public static final String FILE_NAME_PROPERTY = "coala.configuration";

	/** */
	public static final String FILE_NAME_DEFAULT = "coala.properties";

	/** the properties (relative) file path */
	public static final String PROPERTIES_FILE = System.getProperty(
			FILE_NAME_PROPERTY, FILE_NAME_DEFAULT);

	/** */
	private static Configuration MAIN_CONFIG = null;

	/**
	 * {@link ConfigUtil} constructor unavailable
	 */
	private ConfigUtil()
	{
		// empty
	}

	/**
	 * @return
	 */
	public static Configuration getMainConfig()
	{
		if (MAIN_CONFIG == null)
			try
			{
				MAIN_CONFIG = new PropertiesConfiguration(PROPERTIES_FILE);
			} catch (final ConfigurationException e)
			{
				e.printStackTrace();
				MAIN_CONFIG = new PropertiesConfiguration();
			}
		return MAIN_CONFIG;
	}

}
