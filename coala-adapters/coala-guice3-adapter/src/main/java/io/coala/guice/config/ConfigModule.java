/* $Id$
 * $URL: https://dev.almende.com/svn/abms/guice-util/src/main/java/io/coala/guice/config/ConfigModule.java $
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
package io.coala.guice.config;

import org.apache.commons.configuration.Configuration;

import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

/**
 * {@link ConfigModule} inspired by <a
 * href="http://java-taste.blogspot.nl/2011/10/guiced-configuration.html"
 * >here</a>
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class ConfigModule extends AbstractModule
{

	/** */
	private final Configuration configuration;

	/**
	 * {@link ConfigModule} constructor
	 * 
	 * @param configuration
	 */
	public ConfigModule(final Configuration configuration)
	{
		this.configuration = configuration;
	}

	@Override
	protected void configure()
	{
		bind(Configuration.class).toInstance(this.configuration);
		bindListener(Matchers.any(), new ConfigTypeListener(this.configuration));
	}

}
