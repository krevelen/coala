/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/DynaBeanTest.java $
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
package io.coala.experimental.dynabean;

import io.coala.json.JsonUtil;
import io.coala.log.LogUtil;

import java.lang.reflect.Proxy;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

import javax.tools.ToolProvider;

import org.apache.commons.beanutils.BeanPropertyValueChangeClosure;
import org.apache.commons.beanutils.BeanPropertyValueEqualsPredicate;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.LazyDynaBean;
import org.apache.commons.beanutils.MutableDynaClass;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link DynaBeanTest} tests the DynaBean capability of Apache's
 * commons-beanutils
 * <p>
 * TODO Perhaps a lightweight implementation based on Jackson would suffice (see
 * http://stackoverflow.com/questions/3939849/commons-beanutils-alternative)
 * <p>
 * See also 1) {@link ToolProvider#getSystemJavaCompiler()} with example <a
 * href="http://www.rgagnon.com/javadetails/java-0039.html">here</a> (since
 * 1.6), 2) a custom {@link ClassLoader} with hints <a href=
 * "http://stackoverflow.com/questions/15512740/generating-dynamic-class-java"
 * >here</a> (since 1.0) or 3) Java's Dynamic {@link Proxy} with example <a
 * href="http://javahowto.blogspot.nl/2011/12/java-dynamic-proxy-example.html"
 * >here</a> (since 1.3). Forget about Java's innate <a href=
 * "http://www.technobits.net/articles/46024/java-synthetic-class-method-field/"
 * >synthetic class/method/field</a>
 * 
 * <p>
 * for builder generators see <a
 * href="https://github.com/mkarneim/pojobuilder">pojobuilder</a>, <a href=
 * "http://maciejwalkowiak.pl/blog/2012/06/18/java-code-generation-with-jannocessor/"
 * >jannocessor</a>, Jackson-based <a
 * href="http://avro.apache.org/docs/1.7.6/gettingstartedjava.html"
 * >Apache Avro</a>, etc.
 * 
 * @version $Revision: 320 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DynaBeanTest
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DynaBeanTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting DynaBean tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed DynaBean tests!");
	}

	@Test
	public void testDynaBeans() throws Exception
	{
		// for usage, see
		// http://commons.apache.org/proper/commons-beanutils/javadocs/v1.9.2/apidocs/org/apache/commons/beanutils/package-summary.html#package_description

		final DynaBean dynaBean = new LazyDynaBean(); // Create LazyDynaBean
		final MutableDynaClass dynaClass = (MutableDynaClass) dynaBean
				.getDynaClass(); // get DynaClass

		dynaClass.add("amount", java.lang.Integer.class); // add property
		dynaClass.add("myMap", java.util.TreeMap.class); // add mapped property

		final DynaBean employee = dynaClass.newInstance();

		// TODO experiment with Jackson's AnnotationIntrospector to annotate
		// DynaBean#get(...) method with @JsonAnyGetter and #set(...) method
		// with @JsonAnySetter

		employee.set("address", new HashMap<String, String>());
		employee.set("activeEmployee", Boolean.FALSE);
		employee.set("firstName", "Fred");
		employee.set("lastName", "Flintstone");

		LOG.trace("Employee: " + JsonUtil.toPrettyJSON(employee));

		// set all <activeEmployee> attribute values to <true>
		final BeanPropertyValueChangeClosure closure = new BeanPropertyValueChangeClosure(
				"activeEmployee", Boolean.TRUE);

		final Collection<?> employees = Collections.singleton(employee);
		LOG.trace("Mutated employees: " + JsonUtil.toPrettyJSON(employees));

		// update the Collection
		CollectionUtils.forAllDo(employees, closure);

		// filter for beans with <activeEmployee> set to <false>
		final BeanPropertyValueEqualsPredicate predicate = new BeanPropertyValueEqualsPredicate(
				"lastName", "Flintstone");

		// filter the Collection
		CollectionUtils.filter(employees, predicate);
		LOG.trace("Filtered employees: " + JsonUtil.toPrettyJSON(employees));
	}

}
