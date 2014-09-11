/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/log/MyHierarchy.java $
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

import org.apache.log4j.Hierarchy;
import org.apache.log4j.Logger;

/**
 * {@link CoalaLog4jHierarchy} from <a href=
 * "http://stackoverflow.com/questions/19107383/proper-method-for-registering-custom-loggerfactory-for-log4j-1-2"
 * >StackOverflow</a>: I had to create a custom Hierarchy since it appears it
 * hardcodes the default logging factory. This version makes sure my factory
 * gets used when the single argument getLogger() method is called (which
 * appears to happen via Logger.getLogger() -> LogManager.getLogger() ->
 * Hierarchy.getLogger()):
 * 
 * @version $Revision: 310 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class CoalaLog4jHierarchy extends Hierarchy
{

	/** */
	private CoalaLog4jLoggerFactory defaultFactory = new CoalaLog4jLoggerFactory();

	/**
	 * {@link CoalaLog4jHierarchy} constructor
	 * 
	 * @param root
	 */
	public CoalaLog4jHierarchy(final Logger root)
	{
		super(root);
	}

	/** @see Hierarchy#getLogger(String) */
	public Logger getLogger(final String name, final Object source)
	{
		this.defaultFactory.setSource(source);

		final Logger current = exists(name);
		if (current != null)
			if (source == null)
			{
				System.err.println("Null source, using current: " + name);
				return current;
			} else
				try
				{
					final String newName = name + '.' + source.hashCode();
					final Logger result = getLogger(newName,
							this.defaultFactory);
					// System.err.println("Made duplicate logger with name: "
					// + result.getName() + ", should be " + name);
					return result;
				} catch (final Throwable t)
				{
					t.printStackTrace();
				}

		return getLogger(name, this.defaultFactory);
	}

	/** @see Hierarchy#getLogger(String) */
	@Override
	public Logger getLogger(final String name)
	{
		this.defaultFactory.setSource(null);
		return getLogger(name, this.defaultFactory);
	}
}
