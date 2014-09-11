/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/MockTest.java $
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
package io.coala.capability;

import static org.junit.Assert.assertNotNull;
import static org.easymock.EasyMock.*;
import io.coala.log.LogUtil;

import java.math.BigDecimal;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link MockTest} TODO compare JMock 2 (based on Hamcrest) vs EasyMock 2 (with
 * Hamcrest adapters)
 * 
 * @version $Revision: 317 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class MockTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(MockTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting Mock tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed Mock tests!");
	}

	public interface DataAccess
	{
		BigDecimal getPriceBySku(String sku);
	}

	public interface PricingService
	{
		void setDataAccess(DataAccess dataAccess);

		BigDecimal getPrice(String sku) throws Exception;
	}

	public class PricingServiceImpl implements PricingService
	{
		private DataAccess dataAccess;

		public void setDataAccess(DataAccess dataAccess)
		{
			this.dataAccess = dataAccess;
		}

		public BigDecimal getPrice(String sku) throws Exception
		{
			BigDecimal price = this.dataAccess.getPriceBySku(sku);
			if (price == null)
			{
				throw new Exception("SKU not found.");
			}
			return price;
		}
	}

	private static final String SKU = "3283947";
	private static final String BAD_SKU = "-9999993434";

	private PricingService systemUnderTest;
	private DataAccess mockedDependency;

	@Before
	public void doBeforeEachTestCase()
	{
		systemUnderTest = new PricingServiceImpl();
		mockedDependency = createMock(DataAccess.class);
		systemUnderTest.setDataAccess(mockedDependency);
	}

	@Test
	public void getPrice() throws Exception
	{

		// Set expectations on mocks.
		expect(mockedDependency.getPriceBySku(SKU)).andReturn(
				new BigDecimal(100));

		// Set mocks into testing mode.
		replay(mockedDependency);

		final BigDecimal price = systemUnderTest.getPrice(SKU);
		assertNotNull(price);

		// Verify behavior.
		verify(mockedDependency);
	}

	@Test(expected = Exception.class)
	public void getPriceNonExistentSkuThrowsException() throws Exception
	{

		// Set expectations on mocks.
		expect(mockedDependency.getPriceBySku(BAD_SKU)).andReturn(null);

		// Set mocks into testing mode.
		replay(mockedDependency);

		final BigDecimal price = systemUnderTest.getPrice(BAD_SKU);
		LOG.error("OOPS! Got price: " + price);
	}

	@Test(expected = RuntimeException.class)
	public void getPriceDataAccessThrowsRuntimeException() throws Exception
	{

		// Set expectations on mocks.
		expect(mockedDependency.getPriceBySku(SKU)).andThrow(
				new RuntimeException("Fatal data access exception."));

		// Set mocks into testing mode.
		replay(mockedDependency);

		final BigDecimal price = systemUnderTest.getPrice(SKU);
		LOG.error("OOPS! Got price: " + price);
	}
}
