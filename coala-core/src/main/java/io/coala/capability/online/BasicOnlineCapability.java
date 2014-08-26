/* $Id: BasicHTTPClientService.java 358 2014-08-11 14:03:01Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/resource/BasicHTTPClientService.java $
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

import io.coala.bind.Binder;
import io.coala.capability.BasicCapability;
import io.coala.resource.ResourceStreamer;
import io.coala.resource.ResourceType;
import io.coala.web.HttpMethod;

import java.net.URI;
import java.util.Map;

import javax.inject.Inject;

/**
 * {@link BasicOnlineCapability}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class BasicOnlineCapability extends BasicCapability implements
		OnlineCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	@Inject
	public BasicOnlineCapability(final Binder binder)
	{
		super(binder);
	}

	@Override
	public ResourceStreamer request(final URI uri)
	{
		return request(uri, (ResourceType) null);
	}

	@Override
	public ResourceStreamer request(final URI uri, final ResourceType resultType)
	{
		return ResourceStreamer.from(resultType, uri);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method)
	{
		return request(uri, method, (ResourceType) null);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType)
	{
		return request(uri, method, resultType, (ResourceStreamer) null);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceStreamer content)
	{
		return request(uri, method, null, content);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final ResourceStreamer content)
	{
		if (method != HttpMethod.GET)
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: " + method));

		if (content != null)
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: <content>"));

		return request(uri, resultType);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final Map<String, ?> formData)
	{
		if (formData != null && !formData.isEmpty())
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: <formData>"));

		return request(uri, method, resultType, (ResourceStreamer) null);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final Map.Entry... formData)
	{
		if (formData != null && formData.length > 0)
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: <formData>"));

		return request(uri, method, resultType, (ResourceStreamer) null);
	}

}
