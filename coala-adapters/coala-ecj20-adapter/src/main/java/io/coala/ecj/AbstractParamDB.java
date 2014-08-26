/* $Id: AbstractParamDB.java 20 2014-01-22 08:07:46Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/ecj-util/src/main/java/com/almende/train/ec/util/AbstractParamDB.java $
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
package io.coala.ecj;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import ec.BreedingPipeline;
import ec.util.Parameter;
import ec.util.ParameterDatabase;

/**
 * {@link AbstractParamDB}
 * 
 * @date $Date: 2014-01-22 09:07:46 +0100 (Wed, 22 Jan 2014) $
 * @version $Revision: 20 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public abstract class AbstractParamDB<THIS extends AbstractParamDB<THIS>>
		extends ParameterDatabase
{

	/** */
	private static final long serialVersionUID = 1L;

	/**
	 * @param keys the parameter key
	 * @param value the parameter value
	 * @return this {@link AbstractParamDB}
	 */
	@SuppressWarnings("unchecked")
	public THIS with(final List<String> keys, final Object value)
	{
		if (keys == null || keys.size() == 0)
			throw new IllegalArgumentException("keys can't be empty");
		Parameter param = new Parameter(keys.get(0));
		for (int i = 1; i < keys.size(); i++)
			param = param.push(keys.get(i));
		set(param, value instanceof Class ? ((Class<?>) value).getName()
				: value.toString());
		return (THIS) this;
	}

	/**
	 * @param key the parameter key
	 * @param value the parameter value
	 * @return this {@link AbstractParamDB}
	 */
	public THIS with(final String key, final Object value)
	{
		return with(Collections.singletonList(key), value);
	}

	/**
	 * @param keys the parameter key
	 * @param value the parameter value
	 * @return this {@link AbstractParamDB}
	 */
	public THIS with(final String[] keys, final Object value)
	{
		return with(keys == null ? (List<String>) null : Arrays.asList(keys),
				value);
	}

	/** */
	public abstract Class<?> getMainType();

	/**
	 * {@link PipeParamDB} constructor
	 * 
	 * @param type the type of {@link BreedingPipeline} (e.g. mutation)
	 */
	@SuppressWarnings("unchecked")
	public THIS withChildren(final List<? extends AbstractParamDB<?>> sources,
			final String... prefixes)
	{
		if (prefixes == null || prefixes.length == 0)
			throw new IllegalArgumentException("prefixes can't be empty");
		final List<String> list = Arrays.asList(prefixes);
		for (int i = 0; i < sources.size(); i++)
		{
			final List<String> mainParam = new ArrayList<String>(list);
			mainParam.add(Integer.toString(i));
			if (sources.get(i).getMainType() != null)
				with(mainParam, sources.get(i).getMainType());
			for (Object key : sources.get(i).keySet())
			{
				final List<String> subParam = new ArrayList<String>(mainParam);
				subParam.add((String) key);
				with(subParam, (String) sources.get(i).get(key));
			}
		}
		return (THIS) this;
	}

}