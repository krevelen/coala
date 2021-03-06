/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/identity/IDUtil.java $
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
package io.coala.name;

import io.coala.util.Util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * {@link IDUtil} contains some identifier content type
 * 
 * @date $Date: 2014-06-03 14:26:09 +0200 (Tue, 03 Jun 2014) $
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <T> the type of the {@link Comparable} and {@link Serializable}
 *        value/content for this {@link IDUtil}
 * @param <ID> the type of {@link IDUtil} to compare with
 */
public class IDUtil implements Util
{

	/**
	 * @param identifiables
	 * @return the string representations of the wrapped identifiers
	 */
	public static String[] toStringArray(
			final Collection<? extends Identifiable<?>> identifiables)
	{
		final Collection<String> result = toString(identifiables);
		return result.toArray(new String[result.size()]);
	}

	/**
	 * @param identifiables
	 * @return the string representations of the wrapped identifiers
	 */
	public static Collection<String> toString(
			final Collection<? extends Identifiable<?>> identifiables)
	{
		final Collection<String> result = new ArrayList<String>();
		for (Identifiable<?> identifier : identifiables)
			result.add(identifier.getID().toString());
		return result;
	}

}
