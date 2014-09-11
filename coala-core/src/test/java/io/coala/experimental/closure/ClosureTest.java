/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/ClosureTest.java $
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
package io.coala.experimental.closure;

import static ch.lambdaj.Lambda.closure;
import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.join;
import static ch.lambdaj.Lambda.of;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static ch.lambdaj.Lambda.var;
import static org.hamcrest.Matchers.containsString;
import io.coala.log.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections.ClosureUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ch.lambdaj.function.closure.Closure;
import ch.lambdaj.function.closure.Closure1;

/**
 * {@link ClosureTest} TODO see <a
 * href="http://www.massapi.com/class/cl/Closure.html">these examples</a>
 * compare (1) Quercus, (2) Microworkflow, (3) ORB (4) Groovy, (5) Apache
 * commons closure, (6) GStreamer; but not (7) LambdaJ
 * 
 * @version $Revision: 317 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class ClosureTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(ClosureTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting Closures tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed Closures tests!");
	}

	private final String fileName = "log4j.properties";

	public void testClosureUtils() throws Exception
	{
		ClosureUtils.forClosure(0, null);
	}

	/**
	 * closure tests inspired by the <a href=
	 * "https://code.google.com/p/lambdaj/wiki/Closures" >lambdaJ wiki</a>
	 */
	@Test
	public void testClosures() throws Exception
	{
		final Closure println = closure();
		{
			// binds object to the last Closure created in the current Thread
			of(System.err).println(var(String.class));
		}

		println.apply("one");
		LOG.trace("Applied once");

		println.each("one", "two", "three");
		LOG.trace("Applied each()");

		final Closure1<String> lineOutputter = closure(String.class);
		{
			of(System.err).println(var(String.class));
		}
		readFileByLine(this.fileName, lineOutputter);
		LOG.trace("Outputted file to console");

		StringWriter sw = new StringWriter();
		final Closure1<String> lineReader = closure(String.class);
		{
			of(sw).write(var(String.class));
		}
		readFileByLine(this.fileName, lineReader);
		LOG.trace("Merged lines: " + sw.toString());

		this.lineCounter = 0;
		final Closure1<String> lineCounter = closure(String.class);
		{
			of(this).countNonEmptyLine(var(String.class));
		}
		readFileByLine(this.fileName, lineCounter);
		LOG.trace("Counted non-empty lines: " + this.lineCounter);
	}

	private int lineCounter = 0;

	/**
	 * closure helper-method, inspired by the <a href=
	 * "https://code.google.com/p/lambdaj/wiki/Closures" >lambdaJ wiki</a>
	 */
	// can't be private!!
	protected void countNonEmptyLine(final String line)
	{
		if (line == null)
			return;

		final String trimmed = line.trim();
		if (trimmed.length() > 0)
			this.lineCounter++;
	}

	/**
	 * reusable code example, inspired by the <a href=
	 * "https://code.google.com/p/lambdaj/wiki/Closures" >lambdaJ wiki</a>
	 */
	private static void readFileByLine(final String fileName,
			final Closure1<String> lineReader)
	{
		BufferedReader reader = null;
		try
		{
			final InputStream stream = Thread.currentThread()
					.getContextClassLoader().getResourceAsStream(fileName);
			reader = new BufferedReader(new InputStreamReader(stream));
			for (String line = reader.readLine(); line != null; line = reader
					.readLine())
			{
				lineReader.apply(line);
			}
		} catch (final IOException ioe)
		{
			throw new RuntimeException("Error while reading file " + fileName,
					ioe);
		} finally
		{
			try
			{
				if (reader != null)
					reader.close();
			} catch (final IOException ioe)
			{
				throw new RuntimeException("Error while closing file reader",
						ioe);
			}
		}
	}

	/**
	 * inspired by <a href=
	 * "http://kristantohans.wordpress.com/2011/08/06/lambdaj-as-an-alternative-to-query-out-from-collections-stop-iterations/"
	 * >this blog</a>
	 */
	@Test
	public void test()
	{
		final List<String> listOfNames = Arrays.asList("Orange", "Mango",
				"Guava", "Banana", "Papaya", "Strawberry");

		final List<String> listOfNewFruit = select(listOfNames,
				having(on(String.class), containsString("o")));

		final String names = join(listOfNewFruit, "~");

		LOG.trace("Merged selected fruit: " + names);
	}
}
