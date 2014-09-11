/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/exception/CoalaExceptionFactory.java $
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
package io.coala.exception;

import io.coala.agent.AgentID;
import io.coala.factory.ClassUtil;
import io.coala.lifecycle.Machine;
import io.coala.lifecycle.MachineStatus;
import io.coala.name.Identifiable;

import java.util.Arrays;
import java.util.Collection;

/**
 * {@link CoalaExceptionFactory}
 * 
 * TODO apply argument pattern matching as in SLF4J logging ?
 * 
 * @date $Date: 2014-08-09 08:14:14 +0200 (Sat, 09 Aug 2014) $
 * @version $Revision: 354 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public enum CoalaExceptionFactory
{

	/**
	 * This {@link CoalaException} is thrown when an agent creation failed
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link Object} the agent identifier
	 * </ul>
	 */
	AGENT_CREATION_FAILED("Failed to create agent with id '%s'", Object.class),

	/**
	 * This {@link CoalaException} is thrown when an illegal agent creation is
	 * attempted
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link AgentID} the agent identifier
	 * </ul>
	 */
	AGENT_NOT_ALLOWED("Agent with id '%s' is not allowed in this model",
			AgentID.class),

	/**
	 * This {@link CoalaException} is thrown when an agent is unavailable
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link AgentID} the agent identifier
	 * </ul>
	 */
	AGENT_UNAVAILABLE("Agent with id '%s' is not available", AgentID.class),

	/**
	 * This {@link CoalaException} is thrown when a configuration property was
	 * not set
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link String} key
	 * <li>{@link String} config source
	 * </ul>
	 */
	VALUE_NOT_CONFIGURED("Property '%s' not set in configuration source '%s'",
			String.class, String.class),

	/**
	 * This {@link CoalaException} is thrown when a value/argument/parameter is
	 * null but shouldn't be
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link String} value/argument/parameter name
	 * </ul>
	 */
	VALUE_NOT_SET("Value/argument/parameter '%s' can't be null!", String.class),

	/**
	 * This {@link CoalaException} is thrown when a value/argument/parameter is
	 * not allowed
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link String} field/argument/parameter name
	 * <li>{@link Object} invalid value
	 * </ul>
	 */
	VALUE_NOT_ALLOWED("Value/argument/parameter not allowed: %s = '%s'",
			String.class, Object.class),

	/**
	 * This {@link CoalaException} is thrown when an object could not be
	 * marshaled
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link Object} value
	 * <li>{@link Class} type
	 * </ul>
	 */
	MARSHAL_FAILED("Problem marshalling '%s' of type '%s'", Object.class,
			Class.class),

	/**
	 * This {@link CoalaException} is thrown when an object could not be
	 * unmarshaled
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link String} value
	 * <li>{@link Class} result type
	 * </ul>
	 */
	UNMARSHAL_FAILED("Problem unmarshalling '%s' to %s", String.class,
			Class.class),

	/**
	 * This {@link CoalaException} is thrown when an object could not be
	 * instantiated with constructor of specified argument types
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link Class} constructible type
	 * <li>{@link Collection} constructor argument types
	 * </ul>
	 */
	INCONSTRUCTIBLE("No (public) constructor found for %s with arguments: %s",
			Class.class, Collection.class),

	/**
	 * This {@link CoalaException} is thrown when a {@link Machine}'s
	 * {@link MachineStatus} could not be managed
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link Machine} target being updated
	 * <li>{@link MachineStatus} new status
	 * </ul>
	 */
	STATUS_UPDATE_FAILED("Problem updating lifecycle status for %s to %s",
			Machine.class, MachineStatus.class),

	/**
	 * This {@link CoalaException} is thrown when a method could not be invoked
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link String} name of method being invoked
	 * <li>{@link Object} invocation target object
	 * </ul>
	 */
	INVOCATION_FAILED("Invocation failed for method name %s of target %s",
			String.class, Object.class),

	/**
	 * This {@link CoalaException} is thrown when an annotation could not be
	 * located
	 * <p>
	 * Message arguments:
	 * <ul>
	 * <li>{@link Class} the annotation (interface) type
	 * <li>{@link Object} the annotated target object
	 * </ul>
	 */
	ANNOTATION_NOT_FOUND("No annotation found of %s at target %s", Class.class,
			Object.class),

	/**
	 * This {@link CoalaException} is thrown when an operation or method failed
	 * 
	 * <p>
	 * Message argument:
	 * <ul>
	 * <li>{@link String} the target method/operation
	 * </ul>
	 */
	OPERATION_FAILED("Operation %s failed", String.class),

	;

	/** */
	private final String messageFormat;

	/** */
	private final Class<?>[] argumentTypes;

	/**
	 * {@link CoalaExceptionFactory} constructor
	 */
	private CoalaExceptionFactory()
	{
		this("An exception occurred");
	}

	/**
	 * {@link CoalaExceptionFactory} constructor
	 * 
	 * @param messageFormat
	 * @param argumentTypes
	 */
	private CoalaExceptionFactory(final String messageFormat,
			final Class<?>... argumentTypes)
	{
		this.messageFormat = messageFormat;
		this.argumentTypes = argumentTypes;
	}

	/**
	 * @param values
	 * @return
	 */
	private boolean checkArgumentTypes(final Object... values)
	{
		if (this.argumentTypes == null || this.argumentTypes.length == 0)
			return true;
		if (values == null || values.length < this.argumentTypes.length)
		{
			new IllegalArgumentException("Incorrect value count for " + name()
					+ ": " + Arrays.asList(values)).printStackTrace();
			return false;
		}
		for (int i = 0; i < this.argumentTypes.length; i++)
			if (values[i] == null)
			{
				new NullPointerException("Value " + i + " for " + name()
						+ " can't be null!").printStackTrace();
				return false;
			} else if (!ClassUtil.isAssignableFrom(this.argumentTypes[i],
					values[i].getClass()))
			{
				new IllegalArgumentException("Incorrect value type for "
						+ name() + ". Value " + i + " (" + values[i]
						+ ") should be of type: "
						+ this.argumentTypes[i].getName()).printStackTrace();
				return false;
			}
		return true;
	}

	protected static String formatMessage(final String messageFormat,
			final Object... values)
	{
		return values == null || values.length == 0 ? messageFormat : String
				.format(messageFormat, values);
	}

	protected String formatMessage(final Object... values)
	{
		if (values == null || values.length <= this.argumentTypes.length)
			return checkArgumentTypes(values) ? formatMessage(
					this.messageFormat, values) : this.messageFormat;

		final Object[] shortValues = new Object[this.argumentTypes.length];
		final Object[] remainingValues = new Object[values.length
				- this.argumentTypes.length];
		System.arraycopy(values, 0, shortValues, 0, shortValues.length);
		System.arraycopy(values, shortValues.length, remainingValues, 0,
				remainingValues.length);
		for (int i = 0; i < remainingValues.length; i++)
			remainingValues[i] = remainingValues[i] instanceof Identifiable ? ((Identifiable<?>) remainingValues[i])
					.getID() : remainingValues[i];
		final String result = this.messageFormat + " "
				+ Arrays.asList(remainingValues);
		return checkArgumentTypes(values) ? formatMessage(result, shortValues)
				: result;
	}

	/**
	 * @return
	 */
	public CoalaException create()
	{
		return new CoalaException(this.messageFormat);
	}

	/**
	 * @param values
	 * @return
	 */
	public CoalaException create(final Object... values)
	{
		return new CoalaException(formatMessage(values));
	}

	/**
	 * @param cause
	 * @param values
	 * @return
	 */
	public CoalaException create(final Throwable cause, Object... values)
	{
		return new CoalaException(formatMessage(values), cause);
	}

	/**
	 * @return
	 */
	public CoalaRuntimeException createRuntime()
	{
		return new CoalaRuntimeException(this.messageFormat);
	}

	/**
	 * @param values
	 * @return
	 */
	public CoalaRuntimeException createRuntime(final Object... values)
	{
		return new CoalaRuntimeException(formatMessage(values));
	}

	/**
	 * @param cause
	 * @param values
	 * @return
	 */
	public CoalaRuntimeException createRuntime(final Throwable cause,
			final Object... values)
	{
		return new CoalaRuntimeException(formatMessage(values), cause);
	}

}
