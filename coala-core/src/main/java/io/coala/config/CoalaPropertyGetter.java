/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/CoalaPropertyGetter.java $
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


/**
 * {@link CoalaPropertyGetter}
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class CoalaPropertyGetter extends AbstractPropertyGetter
{

	/** */
	private final CoalaPropertyMap properties;

	/** */
	private static final char KEY_SEP = '.';

	/**
	 * {@link CoalaPropertyGetter} constructor
	 * 
	 * @param key
	 */
	public CoalaPropertyGetter(final String key)
	{
		this(key, null);
	}

	/**
	 * {@link CoalaPropertyGetter} constructor
	 * 
	 * @param key
	 * @param defaultValue
	 */
	public CoalaPropertyGetter(final String key, final String defaultValue)
	{
		super(key, defaultValue);
		this.properties = CoalaPropertyMap.getInstance();
	}

	/** @see AbstractPropertyGetter#getProperties() */
	@Override
	protected CoalaPropertyMap getProperties()
	{
		return this.properties;
	}

	/**
	 * @param key
	 * @param prefixes
	 * @return
	 */
	public static String addKeyPrefixes(final String key,
			final String... prefixes)
	{
		if (prefixes == null || prefixes.length == 0)
			return key;
		
		final StringBuilder result = new StringBuilder();
		for (String prefix : prefixes)
			result.append(prefix).append(KEY_SEP);
		return result.append(key).toString();
	}

}