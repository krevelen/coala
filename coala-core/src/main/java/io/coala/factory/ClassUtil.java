/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/util/ClassUtil.java $
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
package io.coala.factory;

import io.coala.exception.CoalaException;
import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.util.Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;

/**
 * {@link ClassUtil}
 * 
 * @date $Date: 2014-08-04 14:19:04 +0200 (Mon, 04 Aug 2014) $
 * @version $Revision: 336 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 * 
 */
public class ClassUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(ClassUtil.class);

	/** used for de/serialization to {@link String} */
	private static final Base64 CODER = new Base64();

	private ClassUtil()
	{
		// empty
	}

	/**
	 * @param primitive
	 * @param wrapper
	 * @return <tt>true</tt> if x is the primitive type wrapped by y,
	 *         <tt>false</tt> otherwise
	 */
	public static boolean isPrimitiveOf(final Class<?> primitive,
			final Class<?> wrapper)
	{
		if (!primitive.isPrimitive() || wrapper.isPrimitive())
			return false;
		// System.err.println( primitive+" =?= "+wrapper );

		if (primitive.equals(byte.class) && wrapper.equals(Byte.class))
			return true;

		if (primitive.equals(short.class) && wrapper.equals(Short.class))
			return true;

		if (primitive.equals(int.class) && wrapper.equals(Integer.class))
			return true;

		if (primitive.equals(long.class) && wrapper.equals(Long.class))
			return true;

		if (primitive.equals(double.class) && wrapper.equals(Double.class))
			return true;

		if (primitive.equals(float.class) && wrapper.equals(Float.class))
			return true;

		if (primitive.equals(boolean.class) && wrapper.equals(Boolean.class))
			return true;

		if (primitive.equals(char.class) && wrapper.equals(Character.class))
			return true;

		return false;
	}

	/**
	 * @param x
	 * @param y
	 * @return <tt>true</tt> if x is an ancestor of y or they wrap/represent the
	 *         same primitive type, <tt>false</tt> otherwise
	 */
	public static boolean isAssignableFrom(final Class<?> x, final Class<?> y)
	{
		return x.isAssignableFrom(y) || isPrimitiveOf(x, y)
				|| isPrimitiveOf(y, x);
	}

	/**
	 * @param returnType the type of the stored property value
	 * @param args the arguments for construction
	 * @return the property value's class instantiated
	 * @throws CoalaException if the property's value is no instance of
	 *             valueClass
	 */
	@SuppressWarnings("unchecked")
	public static <T> T instantiate(final Class<T> returnType,
			final Object... args) throws CoalaException
	{
		if (returnType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET.create("returnType");

		final Class<?>[] argTypes = new Class<?>[args.length];
		for (int i = 0; i < args.length; i++)
			argTypes[i] = args[i] == null ? null : args[i].getClass();

		for (Constructor<?> constructor : returnType.getConstructors())
		{
			final Class<?>[] paramTypes = constructor.getParameterTypes();
			if (paramTypes.length != args.length) // different argument count,
													// try next constructor
			{
				continue;
			}

			boolean match = true;
			for (int i = 0; match && i < paramTypes.length; i++)
			{
				if (args[i] == null && !paramTypes[i].isPrimitive())
					argTypes[i] = paramTypes[i];
				else if (!isAssignableFrom(paramTypes[i], argTypes[i]))
					match = false;
			}

			if (!match) // no matching parameter types, try next constructor
			{
				continue;
			}

			try
			{
				return (T) constructor.newInstance(args);
			} catch (final InvocationTargetException exception)
			{
				// exception caused by the constructor itself, pass cause thru
				throw exception.getCause() instanceof CoalaException ? (CoalaException) exception
						.getCause() : CoalaExceptionFactory.INCONSTRUCTIBLE
						.create(exception, returnType, Arrays.asList(argTypes));
			} catch (final Exception exception)
			{
				throw CoalaExceptionFactory.INCONSTRUCTIBLE.create(exception,
						returnType, Arrays.asList(argTypes));
			}
		}
		throw CoalaExceptionFactory.INCONSTRUCTIBLE.create(returnType,
				Arrays.asList(argTypes));
	}

	/**
	 * @param serializable the {@link String} representation of the
	 *            {@link Serializable} object
	 * @return the deserialized {@link Object}
	 * @throws CoalaException if value was not configured nor any default was
	 *             set
	 */
	public static <T> T deserialize(final String serializable,
			final Class<T> returnType) throws CoalaException
	{
		if (serializable == null || serializable.isEmpty())
			throw CoalaExceptionFactory.VALUE_NOT_SET.create("serializable");

		try (final ObjectInputStream in = new ObjectInputStream(
				new ByteArrayInputStream((byte[]) CODER.decode(serializable))))
		{
			return returnType.cast(in.readObject());
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.UNMARSHAL_FAILED.create(e,
					serializable, Object.class);
		}
	}

	/**
	 * Write the object to a Base64 string
	 * 
	 * @param object the {@link Serializable} object to serialize
	 * @return the {@link String} representation of the {@link Serializable}
	 *         object
	 * @throws CoalaException
	 */
	public static String serialize(final Serializable object)
			throws CoalaException
	{
		try (final ByteArrayOutputStream baos = new ByteArrayOutputStream();
				final ObjectOutputStream oos = new ObjectOutputStream(baos))
		{
			oos.writeObject(object);
			final byte[] data = baos.toByteArray();
			LOG.trace("Serialized:\n\n" + new String(data));
			return new String(CODER.encode(data));
		} catch (final Throwable e)
		{
			throw CoalaExceptionFactory.MARSHAL_FAILED.create(e, object,
					object.getClass());
		}
	}

	/**
	 * Get the underlying class for a type, or null if the type is a variable
	 * type. See <a
	 * href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860"
	 * >description</a>
	 * 
	 * @param type the type
	 * @return the underlying class
	 */
	public static Class<?> getClass(final Type type)
	{
		if (type instanceof Class)
		{
			// LOG.trace("Type is a class/interface: "+type);
			return (Class<?>) type;
		}

		if (type instanceof ParameterizedType)
		{
			// LOG.trace("Type is a ParameterizedType: "+type);
			return getClass(((ParameterizedType) type).getRawType());
		}

		if (type instanceof GenericArrayType)
		{
			// LOG.trace("Type is a GenericArrayType: "+type);
			final Type componentType = ((GenericArrayType) type)
					.getGenericComponentType();
			final Class<?> componentClass = getClass(componentType);
			if (componentClass != null)
				return Array.newInstance(componentClass, 0).getClass();
		}
		return null;
	}

	/**
	 * Get the actual type arguments a child class has used to extend a generic
	 * base class. See <a
	 * href="http://www.artima.com/weblogs/viewpost.jsp?thread=208860"
	 * >description</a>
	 * 
	 * @param genericAncestorType the base class
	 * @param concreteDescendantType the child class
	 * @return a list of the raw classes for the actual type arguments.
	 */
	public static <T> List<Class<?>> getTypeArguments(
			final Class<T> genericAncestorType,
			final Class<? extends T> concreteDescendantType)
	{
		// sanity check
		if (genericAncestorType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("genericAncestorType");
		if (concreteDescendantType == null)
			throw CoalaExceptionFactory.VALUE_NOT_SET
					.createRuntime("concreteDescendantType");

		Map<Type, Type> resolvedTypes = new HashMap<Type, Type>();
		Type type = concreteDescendantType;
		Class<?> typeClass = getClass(type);

		// start walking up the inheritance hierarchy until we hit parentClass
		while (!genericAncestorType.equals(typeClass))
		{
			if (type instanceof Class)
			{
				// there is no useful information for us in raw types, so just
				// keep going.

				if (genericAncestorType.isInterface())
				{
					Type intfType = null;
					for (Type intf : typeClass.getGenericInterfaces())
					{
						if (intf instanceof ParameterizedType
								&& genericAncestorType
										.equals(((ParameterizedType) intf)
												.getRawType()))
						{
							intfType = intf;
							break;
						}
					}
					if (intfType == null)
						type = typeClass.getGenericSuperclass();
					else
						type = intfType;
				} else
					type = typeClass.getGenericSuperclass();

				if (type == null)
				{
					if (!typeClass.isInterface())
					{
						LOG.warn("No generic super classes found for child class: "
								+ typeClass
								+ " of parent: "
								+ genericAncestorType);
						return Collections.emptyList();
					}
					for (Type intf : typeClass.getGenericInterfaces())
					{
						if (intf instanceof ParameterizedType)
						{
							type = intf;
							// TODO try other interfaces if this one fails?
							break;
						}
					}
					if (type == null)
					{
						LOG.warn("No generic ancestors found for child interface: "
								+ typeClass
								+ " of parent: "
								+ genericAncestorType);
						return Collections.emptyList();
					}
				}
				// LOG.trace(String.format("Trying generic super of %s: %s",
				// typeClass.getSimpleName(), type));
			} else
			{
				final ParameterizedType parameterizedType = (ParameterizedType) type;
				final Class<?> rawType = (Class<?>) parameterizedType
						.getRawType();

				final Type[] actualTypeArguments = parameterizedType
						.getActualTypeArguments();
				final TypeVariable<?>[] typeParameters = rawType
						.getTypeParameters();
				for (int i = 0; i < actualTypeArguments.length; i++)
				{
					resolvedTypes
							.put(typeParameters[i], actualTypeArguments[i]);
				}

				if (!genericAncestorType.equals(rawType))
				{
					type = rawType.getGenericSuperclass();
					// LOG.trace(String.format(
					// "Trying generic super of child %s: %s", rawType,
					// type));
				}
				// else // done climbing the hierarchy
				// LOG.trace("Matched generic " + type + " to ancestor: "
				// + genericAncestorType);
			}
			typeClass = getClass(type);
			// LOG.trace("Trying generic " + typeClass + " from: "
			// + Arrays.asList(typeClass.getGenericInterfaces()));
		}

		// finally, for each actual type argument provided to baseClass,
		// determine (if possible)
		// the raw class for that type argument.
		final Type[] actualTypeArguments;
		if (type instanceof Class)
		{
			actualTypeArguments = typeClass.getTypeParameters();
		} else
		{
			actualTypeArguments = ((ParameterizedType) type)
					.getActualTypeArguments();
		}

		// resolve types by chasing down type variables.
		final List<Class<?>> parentTypeArguments = new ArrayList<Class<?>>();
		for (Type baseType : actualTypeArguments)
		{
			while (resolvedTypes.containsKey(baseType))
				baseType = resolvedTypes.get(baseType);

			parentTypeArguments.add(getClass(baseType));
		}
		// LOG.trace(String.format(
		// "Got child %s's type arguments for %s: %s",
		// childClass.getName(), parentClass.getSimpleName(),
		// parentTypeArguments));
		return parentTypeArguments;
	}
}
