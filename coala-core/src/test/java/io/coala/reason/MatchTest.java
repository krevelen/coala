/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/MatchTest.java $
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
package io.coala.reason;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import io.coala.log.LogUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link MatchTest}
 * 
 * @version $Revision: 317 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class MatchTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(MatchTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting Matcher tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed Matcher tests!");
	}
	
	/**
	 * {@link IsNotANumber} inspired by <a
	 * href="https://code.google.com/p/hamcrest/wiki/Tutorial">tutorial</a>
	 * 
	 * @version $Revision: 317 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	public static class IsNotANumber extends TypeSafeMatcher<Double>
	{

		@Override
		public boolean matchesSafely(final Double number)
		{
			return number.isNaN();
		}

		@Override
		public void describeTo(final Description description)
		{
			description.appendText("not a number");
		}

		@Factory
		public static <T> Matcher<Double> notANumber()
		{
			return new IsNotANumber();
		}

	}

	/**
	 * {@link AreEvenNumbers} inspired by <a
	 * href="http://java.dzone.com/articles/using-hamcrest-and-junit">this
	 * blog</a>
	 * 
	 * @version $Revision: 317 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	public static class AreEvenNumbers extends
			TypeSafeMatcher<Collection<Integer>>
	{

		@Override
		public boolean matchesSafely(final Collection<Integer> numbers)
		{
			for (Integer number : numbers)
			{
				if (number % 2 != 0)
				{
					return false;
				}
			}
			return true;
		}

		@Override
		public void describeTo(final Description description)
		{
			description.appendText("even numbers");
		}

		@Factory
		public static <T> Matcher<Collection<Integer>> evenNumbers()
		{
			return new AreEvenNumbers();
		}
	}

	/**
	 * {@link AreEvenNumbers} inspired by <a
	 * href="http://java.dzone.com/articles/using-hamcrest-and-junit">this
	 * blog</a>
	 */
	@Test
	public void shouldHaveOnlyEvenNumbers()
	{
		final List<Integer> numbers = Arrays.asList(2, 4, 6, 8, 10);
		assertThat(numbers, is(AreEvenNumbers.evenNumbers()));
	}

	/**
	 * {@link AreEvenNumbers} inspired by <a
	 * href="http://java.dzone.com/articles/using-hamcrest-and-junit">this
	 * blog</a>
	 */
	@Test
	public void shouldNotHaveOddNumbers()
	{
		final List<Integer> numbers = Arrays.asList(1, 2, 4, 6, 8, 10);
		assertThat(numbers, not(AreEvenNumbers.evenNumbers()));
	}

	/**
	 * {@link IsNotANumber} inspired by <a
	 * href="https://code.google.com/p/hamcrest/wiki/Tutorial">tutorial</a>
	 */
	@Test
	public void testHamcrest() throws Exception
	{
		assertThat(Math.sqrt(-1), is(IsNotANumber.notANumber()));
	}
}
