/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/resource/ResourceStreamer.java $
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
package io.coala.resource;

import io.coala.exception.CoalaException;
import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;
import io.coala.rx.RxUtil;
import io.coala.rx.RxUtil.ThrowingFunc1;
import io.coala.xml.XmlContext;
import io.coala.xml.XmlUtil;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import javax.xml.bind.JAXBElement;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Node;

import rx.Observable;
import rx.Observer;
import rx.functions.Func1;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * {@link ResourceStreamer}
 * 
 * @version $Revision: 359 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ResourceStreamer
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(ResourceStreamer.class);

	/** */
	@JsonIgnore
	private final Observable<ResourceStream> streams;

	/**
	 * {@link ResourceStreamer} constructor
	 * 
	 * @param type
	 * @param streams
	 */
	protected ResourceStreamer(final Observable<ResourceStream> streams)
	{
		this.streams = streams;
	}

	/**
	 * @return an {@link Observable} of the {@link InputStream}(s).
	 *         <p>
	 *         <b>NOTE</b>: Don't forget to {@link InputStream#close() close()}
	 *         them!
	 */
	public Observable<ResourceStream> getStreams()
	{
		return this.streams;
	}

	/** @return the first emitted {@link ResourceStream} */
	public ResourceStream first()
	{
		return RxUtil.awaitFirst(getStreams());
	}

	/** @return the first {@code num} emitted {@link ResourceStream}s */
	public List<ResourceStream> first(final int num)
	{
		return RxUtil.awaitAll(getStreams().take(num));
	}

	/** @return the lst emitted {@link ResourceStream} */
	public ResourceStream last()
	{
		return RxUtil.awaitFirst(getStreams().last());
	}

	/** @return the first {@code num} emitted {@link ResourceStream}s */
	public List<ResourceStream> last(final int num)
	{
		return RxUtil.awaitAll(getStreams().takeLast(num));
	}

	/** @return a {@link List} of all emitted {@link ResourceStream} */
	public List<ResourceStream> all()
	{
		return RxUtil.awaitAll(getStreams());
	}

	private static final String SEP = ", ";

	@Override
	public String toString()
	{
		final StringBuilder result = new StringBuilder();
		final CountDownLatch latch = new CountDownLatch(1);
		getStreams().subscribe(new Observer<ResourceStream>()
		{
			@Override
			public void onCompleted()
			{
				latch.countDown();
			}

			@Override
			public void onError(final Throwable e)
			{
				LOG.warn("Problem while observing streams", e);
				if (result.length() > 0)
					result.append(SEP);
				result.append(e.getMessage());
				latch.countDown();
			}

			@Override
			public void onNext(final ResourceStream t)
			{
				if (result.length() > 0)
					result.append(SEP);
				result.append(t.toString());
			}
		});
		return String.format("%s <<< %s", getClass().getSimpleName(),
				result.length() == 0 ? "<empty>" : result);
	}

	/*	
		public void copySync(final OutputStream output) throws Throwable
		{
			final InputStream input = getSync();
			copy(input, output);
			input.close();
		}

		public void copySync(final Writer output) throws Throwable
		{
			final InputStream input = getSync();
			write(input, output);
			input.close();
		}

		public static void write(final InputStream input, final Writer output)
				throws IOException
		{
			final byte[] buffer = new byte[1024]; // Adjust if you want
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1)
				output.append(new String(buffer, 0, bytesRead));
		}

		public static void copy(final InputStream input, final OutputStream output)
				throws IOException
		{
			final byte[] buffer = new byte[1024]; // Adjust if you want
			int bytesRead;
			while ((bytesRead = input.read(buffer)) != -1)
				output.write(buffer, 0, bytesRead);
		}
		
		public static void copy(final OutputStream input, final InputStream output)
				throws IOException
		{
			// final ByteArrayOutputStream baos = new ByteArrayOutputStream();
			// copy();
			// final byte[] buffer = new byte[1024]; // Adjust if you want
			// input.input.write(buffer);
			// int bytesRead;
			// while ((bytesRead = input.write(buffer)) != -1)
			// output.write(buffer, 0, bytesRead);
			throw new IOException("NOT IMPLEMENTED");
		}

		public String toString(final InputStream is)
		{
			try
			{
				final StringWriter writer = new StringWriter();
				copySync(writer);
				return writer.toString();
			} catch (final Throwable e)
			{
				throw new RuntimeException("Problem while copying " + getType(), e);
			}
		}
	*/

	/**
	 * @return an {@link Observable} of the {@link InputStream}s written to
	 *         {@link String}s
	 */
	public Observable<String> toStrings()
	{
		return RxUtil.map(getStreams(),
				new ThrowingFunc1<ResourceStream, String>()
				{
					@Override
					public String call(final ResourceStream stream)
							throws Throwable
					{
						return IOUtils.toString(stream.getStream());
					}
				});
	}

	/**
	 * @return an {@link Observable} of the {@link InputStream}s unmarshalled
	 *         into objects of the specified {@code resultType}
	 */
	public <T> Observable<T> toJSON(final Class<T> resultType)
	{
		return RxUtil.map(getStreams(), new ThrowingFunc1<ResourceStream, T>()
		{
			@Override
			public T call(final ResourceStream stream) throws Throwable
			{
				return stream.toJSON(resultType);
			}
		});
	}

	/**
	 * @return an {@link Observable} of the {@link InputStream}s unmarshalled
	 *         using the specified {@link XmlContext} ito objects of the
	 *         specified {@code resultType}
	 */
	public <T> Observable<T> toXML(final XmlContext<?> context,
			final Class<T> resultType)
	{
		return RxUtil.map(getStreams(), new ThrowingFunc1<ResourceStream, T>()
		{
			@Override
			public T call(final ResourceStream stream) throws Throwable
			{
				return stream.toXML(context, resultType);
			}
		});
	}

	/**
	 * @return the {@link ResourceStreamer} that emits/calls
	 *         {@link Observer#onComplete()}
	 */
	public static ResourceStreamer empty()
	{
		final Observable<ResourceStream> obs = Observable.empty();
		return from(obs);
	}

	/**
	 * @param type the {@link ResourceType}, or {@code null} if irrelevant
	 * @param t the {@link Throwable}
	 * @return the {@link ResourceStreamer} that emits/calls
	 *         {@link Observer#onError()}
	 */
	public static ResourceStreamer error(final Throwable t)
	{
		final Observable<ResourceStream> obs = Observable.error(t);
		return from(obs);
	}

	/**
	 * @param path
	 * @param type
	 * @param stream
	 * @return
	 */
	public static ResourceStreamer from(final Observable<ResourceStream> obs)
	{
		return new ResourceStreamer(obs);
	}

	/**
	 * @param path
	 * @param type
	 * @param is
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final InputStream is, final String path)
	{
		return from(Observable.from(ResourceStream.of(is, type, path)));
	}

	/**
	 * @param path
	 * @param type
	 * @param content
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final byte[] content, final String path)
	{
		return from(Observable.from(ResourceStream.of(content, type, path)));
	}

	/**
	 * @param path
	 * @param type
	 * @param content
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final String content, final String path)
	{
		return from(Observable.from(ResourceStream.of(content, type, path)));
	}

	/**
	 * @param type
	 * @param content
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final String... content)
	{
		if (content == null || content.length == 0)
			return empty();

		return from(Observable.from(Arrays.asList(content)).map(
				new Func1<String, ResourceStream>()
				{
					@Override
					public ResourceStream call(final String s)
					{
						return ResourceStream.of(s, type);
					}
				}));
	}

	/**
	 * @param content
	 * @return
	 */
	public static ResourceStreamer from(final String... content)
	{
		return from(null, content);
	}

	/**
	 * @param type
	 * @param contentPath
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final URL... contentPath)
	{
		if (contentPath == null || contentPath.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(contentPath)),
				new RxUtil.ThrowingFunc1<URL, ResourceStream>()
				{
					@Override
					public ResourceStream call(final URL p) throws Throwable
					{
						return ResourceStream.of(
								FileUtil.getFileAsInputStream(p), type,
								p.toExternalForm());
					}
				}));
	}

	/**
	 * @param contentPath
	 * @return
	 */
	public static ResourceStreamer from(final URL... contentPath)
	{
		return from(null, contentPath);
	}

	/**
	 * @param contentPath
	 * @param type
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final URI... contentPath)
	{
		if (contentPath == null || contentPath.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(contentPath)),
				new RxUtil.ThrowingFunc1<URI, ResourceStream>()
				{
					@Override
					public ResourceStream call(final URI p) throws Throwable
					{
						return ResourceStream.of(
								FileUtil.getFileAsInputStream(p), type,
								p.toASCIIString());
					}
				}));
	}

	/**
	 * @param type
	 * @return
	 */
	public static ResourceStreamer from(final URI... contentPath)
	{
		return from(null, contentPath);
	}

	/**
	 * @param contentPath
	 * @param type
	 * @return
	 */
	public static ResourceStreamer from(final ResourceType type,
			final File... contentPath)
	{
		if (contentPath == null || contentPath.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(contentPath)),
				new RxUtil.ThrowingFunc1<File, ResourceStream>()
				{
					@Override
					public ResourceStream call(final File p) throws Throwable
					{
						return ResourceStream.of(
								FileUtil.getFileAsInputStream(p), type,
								p.getPath());
					}
				}));
	}

	/**
	 * @param contentPath
	 * @return
	 */
	public static ResourceStreamer from(final File... contentPath)
	{
		return from(null, contentPath);
	}

	/**
	 * @param path
	 * @param type
	 * @return
	 */
	public static ResourceStreamer fromPath(final ResourceType type,
			final String... contentPath)
	{
		if (contentPath == null || contentPath.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(contentPath)),
				new RxUtil.ThrowingFunc1<String, ResourceStream>()
				{
					@Override
					public ResourceStream call(final String p) throws Throwable
					{
						return ResourceStream.of(
								FileUtil.getFileAsInputStream(p), type, p);
					}
				}));
	}

	/**
	 * @param path
	 * @return
	 */
	public static ResourceStreamer fromPath(final String... path)
	{
		return fromPath(null, path);
	}

	/**
	 * @param jdbcDriver
	 * @param dbURL
	 * @param query
	 * @return an {@link Observable} of the {@link ResourceStreamer} for
	 *         specified JDBC driver and {@link URI} (including user, pass, db)
	 *         and (SQL) {@code query}
	 * @throws CoalaException
	 */
	public static ResourceStreamer fromJDBC(final Class<?> jdbcDriver,
			final URI dbURL, final String... query)
	{
		return fromJDBC(null, jdbcDriver, dbURL, query);
	}

	/**
	 * @param jdbcDriver
	 * @param dbURL
	 * @param query
	 * @param type
	 * @return an {@link Observable} of the {@link ResourceStreamer} for
	 *         specified JDBC driver and {@link URI} (including user, pass, db)
	 *         and (SQL) {@code query}
	 * @throws CoalaException
	 */
	public static ResourceStreamer fromJDBC(final ResourceType type,
			final Class<?> jdbcDriver, final URI dbURL, final String... query)
	{
		return error(new IllegalStateException("NOT IMPLEMENTED"));
	}

	/**
	 * @param objexts the {@link Object} to JSONify
	 * @return the {@link ObjectMapper}'s {@link Writer} buffered into a
	 *         {@link String} to read the {@link ResourceStreamer}'s new
	 *         {@link InputStream} from
	 */
	public static ResourceStreamer fromObjectAsJSON(final Object... objects)
	{
		if (objects == null || objects.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(objects)),
				new RxUtil.ThrowingFunc1<Object, ResourceStream>()
				{
					@Override
					public ResourceStream call(final Object obj)
							throws Throwable
					{
						return ResourceStream.of(obj, ResourceType.JSON,
								JsonUtil.toJSONString(obj));
					}
				}));
	}

	/**
	 * @param context the {@link XmlContext}
	 * @param elements the {@link JAXBElement} or {@link Node} to marshal
	 * @return the {@link JAXBMarshaller}'s {@link OutputStream} buffered into a
	 *         {@link String} to read the {@link ResourceStreamer}'s new
	 *         {@link InputStream} from
	 */
	@SafeVarargs
	public static <T> ResourceStreamer fromObjectAsXML(
			final XmlContext<?> context, final T... elements)
	{
		if (elements == null || elements.length == 0)
			return empty();

		return from(RxUtil.map(Observable.from(Arrays.asList(elements)),
				new RxUtil.ThrowingFunc1<T, ResourceStream>()
				{
					@Override
					public ResourceStream call(final T elem) throws Throwable
					{
						final StringWriter sw = new StringWriter();
						context.getMarshaller().marshal(elem, sw);
						return ResourceStream.of(elem, ResourceType.XML,
								sw.toString());
					}
				}));
	}

	/**
	 * @param context
	 * @param jaxbElementType
	 * @param elemNames
	 * @return
	 */
	public <T> Observable<T> toXMLStream(final XmlContext<?> context,
			final Class<T> jaxbElementType, final String... elemNames)
	{
		return XmlUtil.getStream(this, context, jaxbElementType, elemNames);
	}
}