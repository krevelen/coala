/* $Id: a95a474f727b0f3e0003640546864abd5fedb848 $
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/ResourceTest.java $
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
package io.coala.stage;

import io.coala.agent.AgentID;
import io.coala.log.LogUtil;
import io.coala.name.AbstractIdentifiable;

import java.io.Serializable;

import javax.inject.Provider;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import rx.Observer;

/**
 * {@link StagedInjectorTest}
 * 
 * @version $Revision: 353 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class StagedInjectorTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(StagedInjectorTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed tests!");
	}

	@InjectStaged(afterProvide = { "active" })
	public static class StagedObject extends AbstractIdentifiable<AgentID> implements
			Serializable
	{
		/** */
		private static final long serialVersionUID = 1L;

		private StagedObject()
		{
			//super(UUID.nilUUID());
		}

		@Staged(on = StageEvent.AFTER_PROVIDE)
		void initialize() throws Exception
		{
			LOG.info("called initialize()");
		}

		@Staged(on = StageEvent.BEFORE_STAGE)
		void activate()
		{
			LOG.info("called activate()");
		}

		@Staged(onCustom = { "active" })
		void active()
		{
			LOG.info("called activate()");
		}

		@Staged(on = StageEvent.BEFORE_FAIL)
		void deactivate(final Throwable t)
		{
			LOG.info("called deactivate(" + t.getMessage() + ")");
			throw new IllegalStateException("Unhandleable exception 1");
		}

		@Staged(on = StageEvent.AFTER_STAGE)
		void deactivate()
		{
			LOG.info("called deactivate()");
			throw new IllegalStateException("Deliberate exception");
		}

		@Staged(on = StageEvent.BEFORE_RECYCLE)
		void finish() throws Exception
		{
			LOG.info("called finish()");
			throw new IllegalStateException("Unhandleable exception 2");
			// garbageCollected = true;
		}

		@Override
		public void finalize()
		{
			// LOG.trace("JVM runtime's garbage collector called finalize()");
			garbageCollected = true;
		}
	}

	static boolean garbageCollected = false;

	@Test(expected = IllegalStateException.class)
	public void testStagedObjectInjector() throws Throwable
	{
		StagedObject obj = null;
		try
		{
			obj = StageUtil.inject(new Provider<StagedObject>()
			{
				@Override
				public StagedObject get()
				{
					LOG.trace("Providing a " + StagedObject.class.getName());
					return new StagedObject();
				}
			}, new Observer<StageChange>()
			{
				@Override
				public void onCompleted()
				{
					LOG.info("Stages complete");
				}

				@Override
				public void onError(final Throwable e)
				{
					LOG.info("Stages failed", e);
				}

				@Override
				public void onNext(final StageChange t)
				{
					LOG.info("At stage: " + t);
				}
			});
		} catch (final Throwable t)
		{
			LOG.trace("Passing error", t);
			throw t;
		} finally
		{
			obj = null; // turn into garbage, ready for collection/recycling
			LOG.trace("finalize() imminent...");
			System.gc();
			while (!garbageCollected)
			{
				Thread.sleep(100);
				LOG.trace("object: " + obj + ", gc: " + garbageCollected);
			}
		}
	}
}
