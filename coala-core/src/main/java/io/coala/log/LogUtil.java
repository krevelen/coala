/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/LogUtil.java $
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
package io.coala.log;

import io.coala.resource.FileUtil;
import io.coala.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.Properties;
//import java.util.logging.Level;



import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.DefaultRepositorySelector;
import org.apache.log4j.spi.RootLogger;
import org.slf4j.bridge.SLF4JBridgeHandler;

/**
 * {@link LogUtil}
 * 
 * @date $Date: 2014-06-13 14:10:35 +0200 (Fri, 13 Jun 2014) $
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class LogUtil implements Util
{

	/** */
	private static final String CONFIG_PROPERTY_KEY = "log4j.configuration";

	/** */
	private static final String CONFIG_PROPERTY_DEFAULT = "log4j.properties";

	/** */
	private static final String LOCALE_PROPERTY_KEY = "locale";

	/** */
	private static final String LOCALE_PROPERTY_DEFAULT = "en";

	static
	{
		// FIXME allow override from COALA config
		Locale.setDefault(Locale.forLanguageTag(System.getProperty(
				LOCALE_PROPERTY_KEY, LOCALE_PROPERTY_DEFAULT)));

		// divert java.util.logging.Logger LogRecords to SLF4J
		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		// load log4j properties using #getFile() from file system or class path
		// PropertyConfigurator.configure(loadProperties(System.getProperty(
		// CONFIG_PROPERTY_KEY, CONFIG_PROPERTY_DEFAULT)));

		LogManager.setRepositorySelector(new DefaultRepositorySelector(
				new CoalaLog4jHierarchy(new RootLogger(Level.INFO))), LogUtil.class);
		
		// FIXME allow override from COALA config
		new CoalaLog4jPropertyConfigurator().doConfigure(loadProperties(System
				.getProperty(CONFIG_PROPERTY_KEY, CONFIG_PROPERTY_DEFAULT)),
				LogManager.getLoggerRepository());
	}

	/**
	 * @param file the properties file location in the classpath
	 * @return the properties loaded from specified location
	 */
	public static Properties loadProperties(final String file)
	{

		final Properties result = new Properties();

		InputStream is = null;
		try
		{
			is = FileUtil.getFileAsInputStream(file);
			result.load(is);
		} catch (final Exception e)
		{
			System.err.println("Problem loading properties from file: " + file);
			e.printStackTrace();
		} finally
		{
			if (is != null)
				try
				{
					is.close();
				} catch (final IOException e)
				{
					System.err.println("Problem closing properties file: "
							+ file);
					e.printStackTrace();
				}
		}
		return result;
	}

	/**
	 * @param clazz the object type generating the log messages
	 * @return the {@link Logger} instance for specified {@code clazz}
	 */
	public static Logger getLogger(final Class<?> clz)
	{
		return getLogger(clz, clz);
	}

	/**
	 * @param clazz the object type generating the log messages
	 * @return the {@link Logger} instance for specified {@code clazz}
	 */
	public static Logger getLogger(final Class<?> clz, final Object source)
	{
		return getLogger(clz.getName(), source);
	}

	/**
	 * @param clazz the object type generating the log messages
	 * @return the {@link Logger} instance for specified {@code clazz}
	 */
	public static Logger getLogger(final String name, final Object source)
	{
		// TODO use wrapper: intercept #forceLog() calls to add stack trace info

		if (source == null)
		{
			new NullPointerException("Using root logger").printStackTrace();
			return Logger.getRootLogger();
		}

		return ((CoalaLog4jHierarchy) LogManager.getLoggerRepository()).getLogger(name,
				source);
	}

	/**
	 * this method is preferred over {@link Logger#getLogger} so as to
	 * initialize the Log$j system correctly via this {@link LogUtil} class
	 * 
	 * @param name
	 * @return
	 */
	public static Logger getLogger(final String name)
	{
		return ((CoalaLog4jHierarchy) LogManager.getLoggerRepository()).getLogger(name);
	}

	/**
	 * @param clazz the object type generating the log messages
	 * @return the {@link java.util.logging.Logger} instance for specified
	 *         {@code clazz}
	 */
	public static java.util.logging.Logger getJavaLogger(final Class<?> clazz)
	{
		return getJavaLogger(clazz.getName());
	}

	/**
	 * @param clazz the object type generating the log messages
	 * @param level the level up to which messages should be logged, if allowed
	 *        by the bound (e.g. root) logger's settings
	 * @return the {@link java.util.logging.Logger} instance for specified
	 *         {@code clazz}
	 */
	public static java.util.logging.Logger getJavaLogger(final Class<?> clazz,
			final java.util.logging.Level level)
	{
		return getJavaLogger(clazz.getName(), level);
	}

	/**
	 * @param name the logger's name
	 * @return the {@link java.util.logging.Logger} instance for specified
	 *         {@code name}
	 */
	public static java.util.logging.Logger getJavaLogger(final String name)
	{
		return getJavaLogger(name, java.util.logging.Level.ALL);
	}

	/**
	 * @param name the logger's name
	 * @param level the level up to which messages should be logged, if allowed
	 *        by the bound (e.g. root) logger's settings
	 * @return the {@link java.util.logging.Logger} instance for specified
	 *         {@code name}
	 */
	public static java.util.logging.Logger getJavaLogger(final String name,
			final java.util.logging.Level level)
	{
		final java.util.logging.Logger result = java.util.logging.Logger
				.getLogger(name);
		result.setLevel(level);
		return result;
	}
}
