/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/MyPropertyConfigurator.java $
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
package io.coala.log;

import java.util.Properties;

import org.apache.log4j.PropertyConfigurator;

/**
 * {@link CoalaLog4jPropertyConfigurator} from <a href=
 * "http://stackoverflow.com/questions/19107383/proper-method-for-registering-custom-loggerfactory-for-log4j-1-2"
 * >StackOverflow</a>: Not sure I need to customize PropertyConfigurator, but I
 * did in case there is some execution path that actually uses the logger
 * factory instance it keeps a reference to.
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class CoalaLog4jPropertyConfigurator extends PropertyConfigurator
{
	/**
	 * {@link CoalaLog4jPropertyConfigurator} constructor
	 */
	public CoalaLog4jPropertyConfigurator()
	{
		super.loggerFactory = new CoalaLog4jLoggerFactory();
	}

	/** @see PropertyConfigurator#configureLoggerFactory(Properties) */
	@Override
	protected void configureLoggerFactory(final Properties props)
	{
	}
}
