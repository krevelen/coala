/* $Id: 4315b371d925dca6e1609359465a960d25eab26d $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/lifecycle/BeforeDestruction.java $
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
 * Copyright (c) 2010-2013 Almende B.V. 
 */
package io.coala.stage;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * {@link Staged} defines hooks similar to those in the JUnit framework
 * 
 * @BeforeClass, @Before, @After, @AfterClass, ...
 * 
 * @date $Date$
 * @version $Id: 4315b371d925dca6e1609359465a960d25eab26d $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Staged
{

	/**
	 * The {@link StageEvent}s at which the {@link Staged}-annotated method is
	 * to be invoked.
	 */
	StageEvent[] on() default {};

	/**
	 * List of stage names (as specified using the
	 * {@link InjectStaged#afterProvide()} attribute) <b>during</b> which the
	 * annotated method should be called
	 */
	String[] onCustom() default {};

	/**
	 * @return the (super)type(s) of {@link Throwable}s, all of whose (sub)types
	 *         are to be absorbed rather than thrown
	 */
	Class<? extends Throwable>[] ignore() default {};

	/**
	 * @return the priority of this method, smaller values ensure earlier
	 *         execution
	 */
	int priority() default 0;

	boolean returnsNextStage() default false;

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked when
	 * some managed stage in the (owner) {@link InjectStaged} throws a
	 * {@link Throwable}. If the method takes a relevant {@link Throwable} as
	 * (only) argument, it is passed along.
	 */
	// boolean onError() default false;

	/**
	 * Indicates that the {@link Staged}-annotated <b>static</b> method is to be
	 * invoked before the {@link InjectStaged} is constructed
	 */
	// boolean onBeforeConstruct() default false;

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked after
	 * the (owner) {@link InjectStaged} is constructed
	 */
	// boolean onAfterConstruct() default false;

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked
	 * before the (owner) {@link InjectStaged} performs some custom stage
	 */
	// boolean onBeforeStage() default false;

	/**
	 * Indicates that the {@link Staged}-annotated method is to be invoked after
	 * the (owner) {@link InjectStaged} performs some custom stage
	 */
	// boolean onAfterStage() default false;

	/**
	 * Indicates that the {@link Hook}-annotated method is to be invoked before
	 * the (owner) {@link Hookable} is destroyed (at discretion of the JVM's
	 * garbage collector).
	 */
	// boolean beforeDestructor() default false;

	/**
	 * Indicates that the {@link HookObserver}-annotated <b>static</b> method is
	 * to be invoked after the (owner) {@link HookSubject} is destroyed (via
	 * {@link Object#finalize()} as called at discretion of the JVM's garbage
	 * collector (may be forced by calling {@link System#gc()}).
	 * <p>
	 * WARNING: Any {@link Exception} will not be thrown, perhaps simply output
	 * to {@link System#err}
	 */
	// boolean onFinalize() default false;

	/**
	 * Specify the priority of this observer method. Methods with higher
	 * salience values are to be called <b>before</b> those with lower salience
	 * values, e.g. 10 gets called before 9.
	 */
	// int priority() default 0;
}
