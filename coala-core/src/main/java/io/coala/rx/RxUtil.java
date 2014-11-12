/* $Id: 2b9e18395fa592123fbfee75edf41de5d7f1cf7c $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/util/RxUtil.java $
 *  
 * Part of the EU project All4Green, see http://www.all4green-project.eu/
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
 * Copyright © 2010-2013 Almende B.V.
 */
package io.coala.rx;

import io.coala.exception.CoalaExceptionFactory;
import io.coala.log.LogUtil;
import io.coala.util.Util;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Function;

/**
 * {@link RxUtil} provides some <a
 * href="https://github.com/Netflix/RxJava/wiki">RxJava</a>-related utilities
 * 
 * @date $Date: 2014-08-12 12:24:01 +0200 (Tue, 12 Aug 2014) $
 * @version $Revision: 359 $ $Author: krevelen $
 * @author <a href="mailto:rick@almende.org">Rick van Krevelen</a>
 */
public class RxUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(RxUtil.class);

	/**
	 * {@link RxUtil} constructor
	 */
	private RxUtil()
	{
		// empty
	}

	/**
	 * {@link ThrowingFunc1} TODO find a native way to map throwing functions in
	 * rxJava
	 * 
	 * @version $Revision: 359 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 * @param <S> the source type
	 * @param <T> the result type
	 */
	public static interface ThrowingFunc1<S, T> extends Function
	{

		/**
		 * @param t1
		 * @return
		 * @throws Throwable
		 */
		T call(S t1) throws Throwable;
	}

	/**
	 * @param source
	 * @param throwingFunc1
	 * @return
	 */
	public static <S, T> Observable<T> map(final Observable<S> source,
			final ThrowingFunc1<S, T> func)
	{
		return Observable.create(new OnSubscribe<T>()
		{
			@Override
			public void call(final Subscriber<? super T> sub)
			{
				source.subscribe(new Observer<S>()
				{
					@Override
					public void onCompleted()
					{
						sub.onCompleted();
					}

					@Override
					public void onError(final Throwable e)
					{
						sub.onError(e);
					}

					@Override
					public void onNext(final S s)
					{
						try
						{
							sub.onNext(func.call(s));
						} catch (final Throwable e)
						{
							sub.onError(e);
						}
					}
				});
			}
		});
	}

	/**
	 * @param source the {@link Observable} of which the first emitted object
	 *            will be returned, blocking the current thread until it does
	 * @return the first observed/emitted object
	 * @throws Throwable the first error that occurred before the first object
	 *             was emitted
	 */
	public static <T> T awaitFirst(final Observable<T> source)
	{
		return awaitFirst(source, 0, null);
	}

	/**
	 * @param source the {@link Observable} of which the first emitted object
	 *            will be returned, blocking the current thread until it does or
	 *            timeout occurs
	 * @param timeout the maximum time to wait, or <=0 for indefinite
	 * @param unit the {@link TimeUnit} of the {@code timeout} value
	 * @return the first observed/emitted object
	 * @throws Throwable the first error that was occurred before the first
	 *             object was observed
	 */
	public static <T> T awaitFirst(final Observable<T> source,
			final long timeout, final TimeUnit unit)
	{
		final List<T> list = awaitAll(source.first(), timeout, unit);
		if (list.isEmpty())
			throw new NullPointerException("No first element: nothing emitted");
		return list.get(0);
	}

	/**
	 * @param source the {@link Observable} of which all emitted objects will be
	 *            returned, blocking the current thread until it completes
	 * @return the @link List} of observed/emitted objects
	 * @throws Throwable the first error that occurred while observing the
	 *             objects
	 */
	public static <T> List<T> awaitAll(final Observable<T> source)
	{
		return awaitAll(source, 0, null);
	}

	/**
	 * @param source the {@link Observable} of which all emitted objects will be
	 *            returned, blocking the current thread until it completes or a
	 *            timeout occurs
	 * @param timeout the maximum time to wait, or {@code <=0} for never
	 * @param unit the {@link TimeUnit} of the {@code timeout} value
	 * @return the @link List} of observed/emitted objects
	 * @throws Throwable the first error that occurred while observing the
	 *             objects
	 */
	@SuppressWarnings("unchecked")
	public static <T> List<T> awaitAll(final Observable<T> source,
			final long timeout, final TimeUnit unit)
	{
		// the container object that will lock the current thread
		final Object[] container = new Object[] { null };
		final CountDownLatch latch = new CountDownLatch(1);
		final long startTime = System.currentTimeMillis();
		final long maxDuration = unit == null ? 0 : TimeUnit.MILLISECONDS
				.convert(timeout, unit);
		source.toList().subscribe(new Observer<List<T>>()
		{
			@Override
			public void onNext(final List<T> input)
			{
				synchronized (container)
				{
					container[0] = input;
					latch.countDown();
				}
			}

			@Override
			public void onCompleted()
			{
				latch.countDown();
			}

			@Override
			public void onError(final Throwable e)
			{
				container[0] = e;
				latch.countDown();
			}
		});

		int i = 0;
		long duration = 0;
		while (container[0] == null
				&& (maxDuration <= 0 || duration < maxDuration))
		{
			duration = System.currentTimeMillis() - startTime;
			try
			{
				if (maxDuration <= 0) // wait indefinitely
				{
					if (i++ > 0)
						LOG.trace(String.format(
								"awaiting first emitted item (t+%.3fs)...",
								(double) duration / 1000));
					latch.await();
				} else
				{
					if (i++ > 0)
						LOG.trace(String
								.format("awaiting first emitted item (remaining: %.3fs)...",
										(double) (maxDuration - duration) / 1000));
					latch.await(timeout, unit);
				}
			} catch (final InterruptedException e)
			{
				container[0] = e;
			}
		}

		if (container[0] instanceof RuntimeException)
			throw (RuntimeException) container[0];

		if (container[0] instanceof Throwable)
			throw CoalaExceptionFactory.OPERATION_FAILED.createRuntime(
					(Throwable) container[0], "awaitAll");

		if (container[0] == null)
			throw CoalaExceptionFactory.OPERATION_FAILED.createRuntime(
					(Throwable) container[0], "awaitAll", String.format(
							"Timeout occured at %.3fs",
							(double) duration / 1000));

		return (List<T>) container[0];
	}

}
