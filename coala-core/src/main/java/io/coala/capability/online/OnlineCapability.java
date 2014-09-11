/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/resource/HTTPClientService.java $
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/inertia/
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
package io.coala.capability.online;

import io.coala.capability.BasicCapabilityStatus;
import io.coala.capability.Capability;
import io.coala.capability.CapabilityFactory;
import io.coala.resource.ResourceStreamer;
import io.coala.resource.ResourceType;
import io.coala.web.HttpMethod;

import java.io.InputStream;
import java.net.URI;
import java.util.Map;

/**
 * {@link OnlineCapability}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface OnlineCapability extends Capability<BasicCapabilityStatus>
{

	/**
	 * {@link Factory}
	 * 
	 * @version $Revision: 358 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	interface Factory extends CapabilityFactory<OnlineCapability>
	{
		// empty
	}

	/** boolean property key for disabling the SSL certificate/trust manager */
	String TRUST_MANAGER_DISABLED_PROPERTY_KEY = "ssl.trustmanager.disable";

	/** boolean property key for disabling the SSL certificate/trust manager */
	boolean TRUST_MANAGER_DISABLED_PROPERTY_DEFAULT = false;

	/**
	 * @param uri the {@link URI} to {@link HttpMethod#GET GET} from
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri);

	/**
	 * @param uri the {@link URI} to {@link HttpMethod#GET GET} from
	 * @param responseType the {@link ResourceType} of the expected
	 *            {@link ResourceStreamer result}, or {@code null} if irrelevant
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri, ResourceType responseType);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri, HttpMethod method);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @param responseType the {@link ResourceType} of the expected
	 *            {@link ResourceStreamer result}, or {@code null} if irrelevant
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri, HttpMethod method,
			ResourceType responseType);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @param content the {@link ResourceStreamer} providing the content's
	 *            {@link InputStream}(s), or {@code null} if empty
	 * @return the {@link ResourceStreamer} with same type as
	 *         {@link ResourceStreamer#getType() content.getType()}
	 */
	ResourceStreamer request(URI uri, HttpMethod method,
			ResourceStreamer content);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @param resultType the {@link ResourceType} of the expected result, or
	 *            {@code null} if same as {@link ResourceStreamer#getType()
	 *            content.getType()}
	 * @param content the {@link ResourceStreamer} providing the content's
	 *            {@link InputStream}(s), or {@code null} if empty
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri, HttpMethod method,
			ResourceType resultType, ResourceStreamer content);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @param responseType the {@link ResourceType} of the expected
	 *            {@link ResourceStreamer result}, or {@code null} if irrelevant
	 * @param formData a {@link Map} of key-value pairs to send as form data, or
	 *            {@code null} if empty
	 * @return the {@link ResourceStreamer}
	 */
	ResourceStreamer request(URI uri, HttpMethod method,
			ResourceType responseType, Map<String, ?> formData);

	/**
	 * @param uri the {@link URI}
	 * @param method the {@link HttpMethod}
	 * @param responseType the {@link ResourceType} of the expected
	 *            {@link ResourceStreamer result}, or {@code null} if irrelevant
	 * @param formData an {@link Map.Entry Map.Entry[]} array of key-value pairs
	 *            to send as form data, or {@code null} if empty
	 * @return the {@link ResourceStreamer}
	 */
	@SuppressWarnings("rawtypes")
	ResourceStreamer request(URI uri, HttpMethod method,
			ResourceType responseType, Map.Entry... formData);

}
