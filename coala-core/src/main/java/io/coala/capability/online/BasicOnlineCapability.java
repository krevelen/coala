/* $Id$
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
import io.coala.web.WebUtil;

import java.net.URI;
import java.util.Map;
import java.util.Map.Entry;

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
	protected BasicOnlineCapability(final Binder binder)
	{
		super(binder);
	}

	/**
	 * @param uri
	 * @param formData
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static URI toFormDataURI(final URI uri, final Map.Entry... formData)
	{
		if (formData == null || formData.length == 0)
			return uri;

		final StringBuilder result = new StringBuilder();
		for (Entry entry : formData)
			result.append(result.length() == 0 ? '?' : '&')
					.append(WebUtil.urlEncode(entry.getKey().toString()))
					.append('=')
					.append(WebUtil.urlEncode(entry.getValue().toString()));
		return URI.create(uri.toASCIIString() + result.toString());
	}

	@Override
	public ResourceStreamer request(final URI uri)
	{
		return request(uri, (ResourceType) null);
	}

	@Override
	public ResourceStreamer request(final URI uri, final ResourceType resultType)
	{
		return request(uri, HttpMethod.GET, resultType);
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
		if (formData != null && !formData.isEmpty()
				&& (method == HttpMethod.POST || method == HttpMethod.PUT))
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: PUT/POST with <formData>"));

		return request(
				toFormDataURI(uri, formData == null ? null : formData
						.entrySet().toArray(new Map.Entry[formData.size()])),
				method, resultType, (ResourceStreamer) null);
	}

	@Override
	@SuppressWarnings("rawtypes")
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final Map.Entry... formData)
	{
		if (formData != null && formData.length > 0
				&& (method == HttpMethod.POST || method == HttpMethod.PUT))
			return ResourceStreamer.error(new IllegalStateException(
					"NOT IMPLEMENTED: PUT/POST with <formData>"));

		return request(toFormDataURI(uri, formData), method, resultType,
				(ResourceStreamer) null);
	}

}
