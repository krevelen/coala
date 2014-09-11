/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/interpreter/InterpreterService.java $
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
package io.coala.capability.interpret;

import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.resource.ResourceStreamer;

import java.util.List;

import rx.Observable;
import rx.Observer;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link InterpretingCapability} links agents for lookup or directory purposes
 * 
 * @date $Date: 2014-08-12 12:56:22 +0200 (Tue, 12 Aug 2014) $
 * @version $Revision: 360 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 * @param <THIS> the (sub)type of {@link InterpretingCapability} to build
 */
public interface InterpretingCapability extends Capability<BasicCapabilityStatus>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 360 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	interface Factory extends CapabilityFactory<InterpretingCapability>
	{
		// empty
	}

	String LOG_PROPERTY = "log";

	String PROXY_STATUS_PROPERTY = "proxyStatus";

	String MODEL_ID_PROPERTY = "modelID";

	/**
	 * @param scripts the JS script resources
	 * @return an {@link Observable} of the evaluated result {@link Object}s
	 */
	Observable<Object> eval(ResourceStreamer scripts);

	/**
	 * @param me the JS version of my agent
	 * @return an {@link Observable} of the proxy's {@link AgentStatusUpdate}s
	 */
	// Observable<AgentStatusUpdate> init(Object me);

	// /**
	// * @param me the JS version of my agent
	// * @param proxyType the type of {@link InterfaceProxyAgent} representing
	// me
	// * in the JVM
	// * @return an {@link Observable} of the proxy's {@link AgentStatusUpdate}s
	// */
	// Observable<AgentStatusUpdate> init(Object me,
	// Class<? extends InterfaceProxyAgent> proxyType);

	/**
	 * @return the represented (agent) object's attribute names
	 */
	@JsonIgnore
	List<String> listAttributeNames();

	/**
	 * @param name the represented (agent) object's attribute name
	 * @return the represented (agent) object's attribute value
	 */
	Object getAttribute(String name);

	/**
	 * Update an attribute of the represented (agent) object
	 * 
	 * @param name the name of the represented (agent) object's attribute
	 * @param value the new value of the represented (agent) object's attribute
	 * @return
	 */
	Object put(String name, Object value);

	/**
	 * @param observable the (Java) {@link Observable} to be subscribed to with
	 *            a wrapping/proxy {@link Observer}
	 * @param onNext an (interpreted) callback method to be wrapped by
	 *            {@link Observer#onNext()}, or {@code null} to ignore
	 * @param onError an (interpreted) callback method to be wrapped by
	 *            {@link Observer#onError(Throwable)}, or {@code null} to ignore
	 * @param onCompleted an (interpreted) callback method to be wrapped by
	 *            {@link Observer#onCompleted()}, or {@code null} to ignore
	 */
	<T> void subscribe(Observable<T> observable, Object onNext, Object onError,
			Object onCompleted);

}
