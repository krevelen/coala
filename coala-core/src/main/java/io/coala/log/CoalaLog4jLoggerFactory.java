/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/MyLoggerFactory.java $
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

import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;

/**
 * {@link CoalaLog4jLoggerFactory} from <a href=
 * "http://stackoverflow.com/questions/19107383/proper-method-for-registering-custom-loggerfactory-for-log4j-1-2"
 * >StackOverflow</a>: The following is my logger factory implementation. The
 * {@link CoalaLog4jLogger} implementation is a subclass of Log4j's {@link Logger}
 * class, but it overrides the {@link Logger#forcedLog()} protected method to
 * escape characters in the message before calling the super version of the
 * method.
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class CoalaLog4jLoggerFactory implements LoggerFactory
{

	/** */
	private Object source;

	/**
	 * @param source
	 */
	protected void setSource(final Object source)
	{
		this.source = source;
	}

	/** @see LoggerFactory#makeNewLoggerInstance(String) */
	@Override
	public Logger makeNewLoggerInstance(final String name)
	{
		return new CoalaLog4jLogger(name, this.source == null ? name : this.source);
	}
}
