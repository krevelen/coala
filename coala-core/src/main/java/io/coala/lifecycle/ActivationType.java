/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/ActivationType.java $
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
package io.coala.lifecycle;

/**
 * {@link ActivationType} specifies the behavior of a {@link LifeCycle} object
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public enum ActivationType
{

	/**
	 * {@link LifeCycle#activate()} or {@link LifeCycle#deactivate()} are never
	 * called but {@link LifeCycle} object remains passively available (much
	 * like a service)
	 */
	ACTIVATE_NEVER,

	/**
	 * {@link LifeCycle#activate()} is called once and
	 * {@link LifeCycle#deactivate()} afterwards, but {@link LifeCycle} object
	 * remains passively available, until eventually {@link LifeCycle#finish()}
	 * is called
	 */
	ACTIVATE_ONCE,

	/**
	 * {@link LifeCycle#activate()} is called once and
	 * {@link LifeCycle#deactivate()} afterwards, possibly to be activated again
	 * (much like a iterative {@link Runnable})
	 */
	ACTIVATE_MANY,

	/**
	 * {@link LifeCycle#activate()} is called once and
	 * {@link LifeCycle#deactivate()} afterwards immediately followed by
	 * {@link LifeCycle#finish()} (much like a one-shot {@link Runnable})
	 */
	ACTIVATE_AND_FINISH,

	;

}
