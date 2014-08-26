/* $Id: ApacheHTTPClientService.java 358 2014-08-11 14:03:01Z krevelen $
 * $URL: https://dev.almende.com/svn/abms/coala-httpcomponents-adapter/src/main/java/io/coala/http/ApacheHTTPClientService.java $
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
import io.coala.capability.configure.ConfiguringCapability;
import io.coala.capability.online.BasicOnlineCapability;
import io.coala.log.LogUtil;
import io.coala.resource.ResourceStream;
import io.coala.resource.ResourceStreamer;
import io.coala.resource.ResourceType;
import io.coala.web.HttpMethod;
import io.coala.web.WebUtil;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.nio.charset.Charset;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.Args;
import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * {@link FluentHCOnlineCapability}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class FluentHCOnlineCapability extends BasicOnlineCapability
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(FluentHCOnlineCapability.class);

	@Inject
	public FluentHCOnlineCapability(final Binder binder)
	{
		super(binder);
	}

	@Override
	public void initialize() throws NoSuchAlgorithmException,
			KeyManagementException
	{
		if (!getBinder().inject(ConfiguringCapability.class)
				.getProperty(TRUST_MANAGER_DISABLED_PROPERTY_KEY)
				.getBoolean(TRUST_MANAGER_DISABLED_PROPERTY_DEFAULT))
			return;

		final SSLContext ctx = SSLContext.getInstance("TLS");
		ctx.init(new KeyManager[0],
				new TrustManager[] { new DummyTrustManager() },
				new SecureRandom());
		SSLContext.setDefault(ctx);

	}

	/**
	 * @param method
	 * @param uri
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Request getFluentRequest(final HttpMethod method,
			final URI uri, final Map.Entry... formData)
	{
		final Request request;
		switch (method)
		{
		case GET:
			request = Request.Get(toFormDataURI(uri, formData));
			break;
		case HEAD:
			request = Request.Head(toFormDataURI(uri, formData));
			break;
		case POST:
			request = Request.Post(uri);
			break;
		case PUT:
			request = Request.Put(uri);
			break;
		case DELETE:
			request = Request.Delete(uri);
			break;
		default:
			throw new IllegalStateException("UNSUPPORTED: " + method);
		}
		return request.useExpectContinue().version(HttpVersion.HTTP_1_1);
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
					.append(entry.getKey()).append('=')
					.append(entry.getValue());
		return URI.create(uri.toASCIIString()
				+ WebUtil.urlEncode(result.toString()));
	}

	public static HttpUriRequest getRequest(final HttpMethod method,
			final URI uri)
	{
		final HttpRequestBase request;
		switch (method)
		{
		case POST:
			request = new HttpPost(uri);// Request.Post(uri);
			break;
		case PUT:
			request = new HttpPut(uri);// Request.Put(uri);
			break;
		case DELETE:
			request = new HttpDelete(uri);// Request.Delete(uri);
			break;
		case GET:
			request = new HttpGet(uri);// Request.Get(uri);
			break;
		default:
			throw new IllegalStateException("UNSUPPORTED: " + method);
		}
		request.setProtocolVersion(HttpVersion.HTTP_1_1);
		final RequestConfig.Builder configBuilder = RequestConfig.custom();
		// TODO read (additional) HTTP client settings from external config
		configBuilder.setExpectContinueEnabled(true);
		request.setConfig(configBuilder.build());
		return request;
	}

	@SuppressWarnings("rawtypes")
	public static List<NameValuePair> toFormData(final Map.Entry... formData)
	{
		final List<NameValuePair> paramList = new ArrayList<>();
		for (Map.Entry entry : formData)
		{
			final String name = entry.getKey().toString();
			final String value = entry.getValue().toString();
			paramList.add(new NameValuePair()
			{
				@Override
				public String getName()
				{
					return name;
				}

				@Override
				public String getValue()
				{
					return value;
				}
			});
		}
		return paramList;
	}

	public static HttpEntity toFormEntity(final List<NameValuePair> paramList)
	{
		final ContentType contentType = ContentType.create(
				URLEncodedUtils.CONTENT_TYPE, Consts.ISO_8859_1);
		final Charset charset = contentType != null ? contentType.getCharset()
				: null;
		final String s = URLEncodedUtils.format(paramList,
				charset != null ? charset.name() : null);
		byte[] raw;
		try
		{
			raw = charset != null ? s.getBytes(charset.name()) : s.getBytes();
		} catch (UnsupportedEncodingException ex)
		{
			raw = s.getBytes();
		}
		return new ApacheInternalByteArrayEntity(raw, contentType);
	}

	public static HttpEntity toStreamEntity(final ResourceStream stream)
	{
		return new ApacheInternalInputStreamEntity(stream.getStream(), -1,
				ContentType.create(stream.getType().getMIMEType()));
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final ResourceStreamer content)
	{
		return doRequest(uri, method, resultType, content);
	}

	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final Map<String, ?> formData)
	{
		return doRequest(uri, method, resultType, null, formData.entrySet()
				.toArray(new Map.Entry[formData.size()]));
	}

	@SuppressWarnings("rawtypes")
	@Override
	public ResourceStreamer request(final URI uri, final HttpMethod method,
			final ResourceType resultType, final Map.Entry... formData)
	{
		return doRequest(uri, method, resultType, null, formData);
	}

	@SuppressWarnings("rawtypes")
	protected static ResourceStreamer doRequest(final URI uri,
			final HttpMethod method, final ResourceType resultType,
			final ResourceStreamer content, final Map.Entry... formData)
	{
		return ResourceStreamer.from(Observable
				.create(new OnSubscribe<ResourceStream>()
				{
					@Override
					public void call(
							final Subscriber<? super ResourceStream> sub)
					{
						final CountDownLatch latch = new CountDownLatch(1);
						final MyResponseHandler handler = new MyResponseHandler(
								uri, sub, latch);

						// final HttpUriRequest request = getRequest(method,
						// uri);
						// if (request instanceof HttpEntityEnclosingRequest)
						final Request request = getFluentRequest(method, uri,
								formData);
						if (method == HttpMethod.POST
								|| method == HttpMethod.PUT)
						{
							if (formData != null && formData.length > 0)
							{
								if (content != null)
									LOG.warn("IGNORING NON-EMPTY CONTENT, prioritizing non-empty form data");
								execute(request, handler, sub,
										toFormEntity(toFormData(formData)));
								latch.countDown();
							} else if (content == null)
							{
								execute(request, handler, sub, null);
								latch.countDown();
							} else
							{
								Schedulers.io().createWorker()
										.schedule(new Action0()
										{
											@Override
											public void call()
											{
												content.getStreams()
														.subscribe(
																new Observer<ResourceStream>()
																{
																	@Override
																	public void onCompleted()
																	{
																		sub.onCompleted();
																		Schedulers
																				.io()
																				.createWorker()
																				.schedule(
																						new Action0()
																						{
																							@Override
																							public void call()
																							{
																								latch.countDown();
																							}
																						});
																	}

																	@Override
																	public void onError(
																			final Throwable e)
																	{
																		sub.onError(e);
																		Schedulers
																				.io()
																				.createWorker()
																				.schedule(
																						new Action0()
																						{
																							@Override
																							public void call()
																							{
																								latch.countDown();
																							}
																						});
																	}

																	@Override
																	public void onNext(
																			final ResourceStream is)
																	{
																		execute(request,
																				handler,
																				sub,
																				toStreamEntity(is));
																	}
																});

											}
										});
							}
						} else
						// GET or DELETE
						{
							if (content != null)
								LOG.warn("IGNORING NON-EMPTY CONTENT, not applicable for HTTP request method: "
										+ method);
							execute(request, handler, sub, null);
							latch.countDown();
						}
					}
				}));
	}

	/**
	 * @param request
	 * @param handler
	 * @param sub
	 * @param entity
	 */
	protected static void execute(final Request request,
			final MyResponseHandler handler,
			final Subscriber<? super ResourceStream> sub,
			final HttpEntity entity)
	{
		try
		{
			if (entity != null)
				request.body(entity);
			request.execute().handleResponse(handler);
		} catch (final Throwable e)
		{
			sub.onError(e);
		}
	}

	/**
	 * @param request
	 * @param handler
	 * @param sub
	 * @param entity
	 */
	protected static void execute(final HttpUriRequest request,
			final ResponseHandler<?> handler, final Observer<?> sub,
			final HttpEntity entity)
	{
		// Schedulers.io().createWorker().schedule(new Action0()
		// {
		// @Override
		// public void call()
		// {
		try
		{
			if (entity != null)
				((HttpEntityEnclosingRequest) request).setEntity(entity);
			HttpClientBuilder.create().build().execute(request, handler);
		} catch (final Throwable e)
		{
			sub.onError(e);
		}
		// }
		// });
	}

	/**
	 * {@link DummyTrustManager}
	 * 
	 * @version $Revision: 358 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	static class DummyTrustManager implements X509TrustManager
	{

		@Override
		public void checkClientTrusted(final X509Certificate[] arg0,
				final String arg1) throws CertificateException
		{
		}

		@Override
		public void checkServerTrusted(final X509Certificate[] arg0,
				final String arg1) throws CertificateException
		{
		}

		@Override
		public X509Certificate[] getAcceptedIssuers()
		{
			return null;
		}
	}

	/**
	 * {@link MyResponseHandler}
	 * 
	 * @version $Revision: 358 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	static class MyResponseHandler implements ResponseHandler<Void>
	{
		/** */
		@JsonIgnore
		private final Subscriber<? super ResourceStream> sub;

		private final URI requestPath;

		private final CountDownLatch latch;

		public MyResponseHandler(final URI requestPath,
				final Subscriber<? super ResourceStream> sub,
				final CountDownLatch latch)
		{
			this.requestPath = requestPath;
			this.sub = sub;
			this.latch = latch;
		}

		@Override
		public Void handleResponse(final HttpResponse response)
				throws ClientProtocolException, IOException
		{
			final StatusLine statusLine = response.getStatusLine();
			if (statusLine.getStatusCode() >= 300)
				throw new HttpResponseException(statusLine.getStatusCode(),
						statusLine.getReasonPhrase());

			final HttpEntity entity = response.getEntity();
			if (entity == null)
			{
				this.sub.onError(new ClientProtocolException(
						"Response contains no content"));
				return null;
			}

			final ResourceType type = ResourceType.ofMIMEType(entity
					.getContentType().getValue());
			try
			{
				this.sub.onNext(ResourceStream.of(entity.getContent(), type,
						this.requestPath.toASCIIString()));
			} catch (final Exception e)
			{
				this.sub.onError(e);
			}

			int secs = 0;
			while (latch.getCount() > 0)
			{
				try
				{
					if (secs > 0)
						LOG.trace("Holding response Thread for "
								+ this.requestPath.toASCIIString() + "...");
					this.latch.await(1, TimeUnit.SECONDS);
					secs++;
				} catch (final InterruptedException e)
				{
					e.printStackTrace();
				}
			}
			return null;
		}
	}

	/**
	 * {@link ApacheInternalByteArrayEntity} copied from
	 * {@link org.apache.http.client.fluent.InternalByteArrayEntity}
	 * 
	 * @version $Revision: 358 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	static class ApacheInternalByteArrayEntity extends AbstractHttpEntity
			implements Cloneable
	{

		private final byte[] b;
		private final int off, len;

		public ApacheInternalByteArrayEntity(final byte[] b,
				final ContentType contentType)
		{
			super();
			Args.notNull(b, "Source byte array");
			this.b = b;
			this.off = 0;
			this.len = this.b.length;
			if (contentType != null)
			{
				setContentType(contentType.toString());
			}
		}

		public ApacheInternalByteArrayEntity(final byte[] b, final int off,
				final int len, final ContentType contentType)
		{
			super();
			Args.notNull(b, "Source byte array");
			if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) < 0)
					|| ((off + len) > b.length))
			{
				throw new IndexOutOfBoundsException("off: " + off + " len: "
						+ len + " b.length: " + b.length);
			}
			this.b = b;
			this.off = off;
			this.len = len;
			if (contentType != null)
			{
				setContentType(contentType.toString());
			}
		}

		public ApacheInternalByteArrayEntity(final byte[] b)
		{
			this(b, null);
		}

		public ApacheInternalByteArrayEntity(final byte[] b, final int off,
				final int len)
		{
			this(b, off, len, null);
		}

		public boolean isRepeatable()
		{
			return true;
		}

		public long getContentLength()
		{
			return this.len;
		}

		public InputStream getContent()
		{
			return new ByteArrayInputStream(this.b, this.off, this.len);
		}

		public void writeTo(final OutputStream outstream) throws IOException
		{
			Args.notNull(outstream, "Output stream");
			outstream.write(this.b, this.off, this.len);
			outstream.flush();
		}

		public boolean isStreaming()
		{
			return false;
		}

	}

	/**
	 * {@link ApacheInternalInputStreamEntity} adapted from
	 * {@link org.apache.http.client.fluent.InternalInputStreamEntity}
	 * 
	 * @version $Revision: 358 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	static class ApacheInternalInputStreamEntity extends AbstractHttpEntity
	{

		private final InputStream content;
		private final long length;

		public ApacheInternalInputStreamEntity(final InputStream instream,
				final long length, final ContentType contentType)
		{
			super();
			this.content = Args.notNull(instream, "Source input stream");
			this.length = length;
			if (contentType != null)
			{
				setContentType(contentType.toString());
			}
		}

		public boolean isRepeatable()
		{
			return false;
		}

		public long getContentLength()
		{
			return this.length;
		}

		public InputStream getContent() throws IOException
		{
			return this.content;
		}

		public void writeTo(final OutputStream outstream) throws IOException
		{
			Args.notNull(outstream, "Output stream");
			final InputStream instream = this.content;
			try
			{
				if (this.length < 0)
					IOUtils.copy(instream, outstream);
				else
					// should not occur
					IOUtils.copyLarge(instream, outstream, 0, this.length);
			} finally
			{
				instream.close();
			}
		}

		public boolean isStreaming()
		{
			return true;
		}

	}

}
