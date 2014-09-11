/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/config/ConfigConverter.java $
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

/**
 * {@link ConfigConverter} inspired by <a
 * href="http://java-taste.blogspot.nl/2011/10/guiced-configuration.html"
 * >here</a>
 * 
 * @version $Revision: 300 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface ConfigConverter<T>
{
	/**
	 * Converts string value provided by configuration to type expected by
	 * annotated field. Each implementation must provide no-argument constructor
	 * in order to be instantiated by injector.
	 * 
	 * @param property
	 */
	T convert(String property);
}