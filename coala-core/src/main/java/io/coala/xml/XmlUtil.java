/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/xml/XmlUtil.java $
 * 
 * Part of the EU project INERTIA, see http://www.inertia-project.eu/
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
package io.coala.xml;

import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.resource.ResourceStream;
import io.coala.resource.ResourceStreamer;
import io.coala.util.Util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.Duration;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Interval;
import org.joda.time.Period;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * {@link XmlUtil}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class XmlUtil implements Util
{

	/** */
	public static String SUN_NAMESPACE_PREFIX_MAPPER = "com.sun.xml.bind.namespacePrefixMapper";

	/** */
	// private static final Logger LOG = LogUtil.getLogger(XmlUtil.class);

	/** */
	private static DocumentBuilderFactory domFactory = null;

	/** */
	private static DatatypeFactory _datatypeFactory = null;

	/** */
	private static final Map<XmlContextID<?>, XmlContext<?>> JAXB_CONTEXT_CACHE = new HashMap<>();

	/**
	 * @param contextID
	 * @return the {@link XmlContext}
	 */
	@SuppressWarnings("unchecked")
	public synchronized static <T> XmlContext<T> getContextCache(
			final XmlContextID<T> contextID)
	{
		if (!JAXB_CONTEXT_CACHE.containsKey(contextID))
			JAXB_CONTEXT_CACHE
					.put(contextID, contextID
							.newContextCacheEntry(getValidationEventHandler()));

		return (XmlContext<T>) JAXB_CONTEXT_CACHE.get(contextID);
	}

	/** */
	private static XmlValidationEventHandler VALIDATION_EVENT_HANDLER = null;

	/** @return the custom {@link ValidationEventHandler} */
	protected static XmlValidationEventHandler getValidationEventHandler()
	{
		if (VALIDATION_EVENT_HANDLER == null)
			VALIDATION_EVENT_HANDLER = new XmlValidationEventHandler();

		return VALIDATION_EVENT_HANDLER;
	}

	/** @return an {@link Observable} source of {@link ValidationEvent}s */
	public static Observable<ValidationEvent> getValidationErrors()
	{
		return getValidationEventHandler().events();
	}

	/**
	 * @param derModel
	 * @return
	 */
	public static JAXBContext getJAXBContext(final XmlContextID<?> contextID)
	{
		return getContextCache(contextID).getJAXBContext();
	}

	/**
	 * @param derModel
	 * @return
	 */
	public static Unmarshaller getUnmarshaller(final XmlContextID<?> contextID)
	{
		return getContextCache(contextID).getUnmarshaller();
	}

	/**
	 * @param derModel
	 * @return
	 */
	public static Marshaller getMarshaller(final XmlContextID<?> contextID)
	{
		return getContextCache(contextID).getMarshaller();
	}

	/**
	 * @param derModel
	 * @return
	 */
	public static <T> T getObjectFactory(final XmlContextID<T> contextID)
	{
		return getContextCache(contextID).getObjectFactory();
	}

	/**
	 * @param xml
	 * @throws Exception
	 */
	public static String toString(final XmlContext<?> contextCache,
			final Object xml)
	{
		final StringWriter sw = new StringWriter();
		try
		{
			contextCache.getMarshaller().marshal(xml, sw);
		} catch (final JAXBException e)
		{
			throw CoalaExceptionFactory.MARSHAL_FAILED.createRuntime(e, xml,
					xml.getClass());
		}
		return sw.toString();
	}

	/**
	 * @param xml
	 * @throws Exception
	 */
	public static void toOutputStream(final XmlContext<?> contextCache,
			final Object xml, final OutputStream os)
	{
		try
		{
			contextCache.getMarshaller().marshal(xml, os);
		} catch (final JAXBException e)
		{
			throw CoalaExceptionFactory.MARSHAL_FAILED.createRuntime(e, xml,
					xml.getClass());
		}
	}

	/**
	 * @param xml
	 * @throws Exception
	 */
	public static String toString(final XmlContextID<?> contextID,
			final Object xml)
	{
		return toString(getContextCache(contextID), xml);
	}

	/**
	 * @return
	 */
	public static DocumentBuilderFactory getDOMBuilderFactory()
	{
		if (domFactory == null)
		{
			domFactory = DocumentBuilderFactory.newInstance();
			domFactory.setNamespaceAware(true);
		}
		return domFactory;
	}

	/**
	 * @return the cached {@link DatatypeFactory} instance, configured per the
	 *         {@link DatatypeFactory#DATATYPEFACTORY_PROPERTY} system property
	 */
	public static DatatypeFactory getDatatypeFactory()
	{
		if (_datatypeFactory == null)
		{
			final String dfType = System.getProperty(
					DatatypeFactory.DATATYPEFACTORY_PROPERTY,
					DatatypeFactory.DATATYPEFACTORY_IMPLEMENTATION_CLASS);
			try
			{
				_datatypeFactory = DatatypeFactory.newInstance(dfType, Thread
						.currentThread().getContextClassLoader());
			} catch (final DatatypeConfigurationException e)
			{
				throw CoalaExceptionFactory.VALUE_NOT_ALLOWED.createRuntime(e,
						DatatypeFactory.DATATYPEFACTORY_PROPERTY, dfType);
			}
		}

		return _datatypeFactory;
	}

	/** */
	private static final XMLInputFactory inputFactory = XMLInputFactory
			.newFactory();
	{
		inputFactory.setProperty(
				XMLInputFactory.IS_REPLACING_ENTITY_REFERENCES, Boolean.FALSE);
		inputFactory.setProperty(
				XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, Boolean.FALSE);
		inputFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
	}

	/** */
	private static final Logger LOG = LogUtil.getLogger(XmlUtil.class);

	/**
	 * See also XSLT streaming transformation (STX), e.g. with <a
	 * href="http://www.saxonica.com/">SAXON HE</a>
	 * 
	 * {@code
	 * <dependency>
	 * 	<groupId>net.sf.saxon</groupId>
	 * 	<artifactId>Saxon-HE</artifactId>
	 * 	<version>9.5.1-5</version>
	 * </dependency> }
	 * 
	 * @param restCall
	 * @param jaxbElementType
	 * @param elemNames TODO allow wildcards or xPath 1, 2, etc. ?
	 * @return
	 * @throws Exception
	 */
	public static <T> Observable<T> getStream(final ResourceStreamer resource,
			final XmlContext<?> context, final Class<T> jaxbElementType,
			final String... elemNames)
	{
		final List<String> elemPath = Arrays.asList(elemNames);
		return Observable.merge(resource.getStreams().map(
				new Func1<ResourceStream, Observable<T>>()
				{
					@Override
					public Observable<T> call(final ResourceStream stream)
					{
						return Observable.create(new OnSubscribe<T>()
						{
							@Override
							public void call(final Subscriber<? super T> sub)
							{
								parseStream(context, stream.getStream(),
										jaxbElementType, elemPath, sub);
							}
						});
					}
				}));
	}

	protected static <T> void parseStream(final XmlContext<?> context,
			final InputStream is, final Class<T> jaxbElementType,
			final List<String> elemPath, final Subscriber<? super T> sub)
	{
		XMLStreamReader xmlReader = null;
		try
		{
			xmlReader = inputFactory.createXMLStreamReader(is);
			final Deque<String> path = new LinkedList<>();
			while (xmlReader.getEventType() != XMLStreamConstants.END_DOCUMENT)
			{
				switch (xmlReader.next())
				{
				case XMLStreamConstants.START_ELEMENT:
					path.offerLast(xmlReader.getLocalName());
					// System.err.println("xml stream event: "
					// + XMLStreamEventTypeEnum
					// .values()[xmlReader
					// .getEventType() - 1]
					// + (xmlReader.hasName() ? " "
					// + xmlReader
					// .getLocalName()
					// : "") + ", path: "
					// + path);
					while (path.equals(elemPath))
					{
						final JAXBElement<T> elem = (JAXBElement<T>) context
								.getUnmarshaller().unmarshal(xmlReader,
										jaxbElementType);
						sub.onNext(elem.getValue());
						path.pollLast();
						if (xmlReader.hasName())
							path.offerLast(xmlReader.getLocalName());
						// System.err.println("xml stream event: PARSED, NEXT: "
						// + (xmlReader.hasName() ? " "
						// + xmlReader
						// .getLocalName()
						// : "") + ", path: "
						// + path);
					}
					break;
				case XMLStreamConstants.END_ELEMENT:
					path.pollLast();
					// System.err.println("xml stream event: "
					// + XMLStreamEventTypeEnum
					// .values()[xmlReader
					// .getEventType() - 1]
					// + (xmlReader.hasName() ? " "
					// + xmlReader
					// .getLocalName()
					// : "") + ", path: "
					// + path);
				}
			}
		} catch (final Exception e)
		{
			sub.onError(e);
		} finally
		{
			if (xmlReader != null)
				try
				{
					xmlReader.close();
				} catch (final XMLStreamException e)
				{
					LOG.trace("Problem closing reader", e);
				}

			try
			{
				is.close();
			} catch (final IOException e)
			{
				LOG.trace("Problem closing stream", e);
			}
		}
		sub.onCompleted();
	}

	/**
	 * @param date
	 * @return
	 */
	public static Date toDate(final XMLGregorianCalendar date)
	{
		return toDateTime(date).toDate();
	}

	/**
	 * @param date
	 * @return
	 */
	public static DateTime toDateTime(final XMLGregorianCalendar date)
	{
		final DateTimeZone timeZone = date.getTimezone() == DatatypeConstants.FIELD_UNDEFINED ? DateTimeZone
				.getDefault() : DateTimeZone
				.forOffsetMillis(date.getTimezone() * 60 * 1000);
		return new DateTime(date.getYear(), date.getMonth(), date.getDay(),
				date.getHour(), date.getMinute(), date.getSecond(),
				date.getMillisecond(), timeZone);
	}

	/**
	 * @param calendar
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar toDateTime(final Calendar calendar)
	{
		return toDateTime(new DateTime(calendar));
	}

	/**
	 * @param date
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar toDateTime(final Date date)
	{
		return toDateTime(new DateTime(date, DateTimeZone.getDefault()));
	}

	/**
	 * @param date
	 * @return
	 * @throws DatatypeConfigurationException
	 */
	public static XMLGregorianCalendar toDateTime(final DateTime date)
	{
		final XMLGregorianCalendar result = getDatatypeFactory()
				.newXMLGregorianCalendar();
		result.setYear(date.getYear());
		result.setMonth(date.getMonthOfYear());
		result.setDay(date.getDayOfMonth());
		result.setTime(date.getHourOfDay(), date.getMinuteOfHour(),
				date.getSecondOfMinute(), date.getMillisOfSecond());
		result.setTimezone(date.getZone().toTimeZone().getRawOffset() / 1000 / 60);
		// result.setTimezone(DatatypeConstants.FIELD_UNDEFINED);
		return result;
	}

	/**
	 * @param duration
	 * @param startInstant
	 * @return the {@link org.joda.time.Duration}
	 */
	public static org.joda.time.Duration toDuration(
			final javax.xml.datatype.Duration duration,
			final XMLGregorianCalendar startInstant)
	{
		return toInterval(duration, startInstant).toDuration();
	}

	/**
	 * @param duration
	 * @param startInstant
	 * @return the {@link Interval}
	 */
	public static Interval toInterval(
			final javax.xml.datatype.Duration duration,
			final XMLGregorianCalendar startInstant)
	{
		return toInterval(duration, toDateTime(startInstant));
	}

	/**
	 * @param duration
	 * @param startInstant
	 * @return the {@link Interval}
	 */
	public static Interval toInterval(
			final javax.xml.datatype.Duration duration, final DateTime offset)
	{
		return new Interval(offset, offset.plus(duration.getTimeInMillis(offset
				.toDate())));
	}

	/**
	 * @param interval
	 * @return
	 */
	public static javax.xml.datatype.Duration toDuration(final Interval interval)
	{
		return toDuration(interval.toPeriod());
	}

	/**
	 * @param period
	 * @return
	 */
	public static Duration toDuration(final Period period)
	{
		return getDatatypeFactory().newDuration(true, period.getYears(),
				period.getMonths(), period.getDays(), period.getHours(),
				period.getMinutes(), period.getSeconds());
	}

	/**
	 * @param duration the {@link org.joda.time.Duration} to convert
	 * @return
	 */
	public static Duration toDuration(final org.joda.time.Duration duration)
	{
		return toDuration(duration.getMillis());
	}

	/**
	 * @param millis
	 * @return
	 */
	public static Duration toDuration(final long millis)
	{
		return getDatatypeFactory().newDuration(millis);
	}

	/**
	 * @param duration
	 * @return
	 */
	public static long toLong(final javax.xml.datatype.Duration duration)
	{
		return duration.getTimeInMillis(new Date(0));
	}

	/**
	 * {@link XmlUtil} constructor
	 */
	protected XmlUtil()
	{
		// should not be instantiated;
	}

}
