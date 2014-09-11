/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/test/java/io/coala/WeightedProductComparatorTest.java $
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

import io.coala.log.LogUtil;
import io.coala.optimize.WeightedComparable;
import io.coala.optimize.WeightedCriterion;
import io.coala.optimize.WeightedNavigableMap;
import io.coala.optimize.WeightedSkipListMap;

import java.util.EnumMap;

import org.apache.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * {@link WeightedProductComparatorTest}
 * 
 * @version $Revision: 323 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class WeightedProductComparatorTest
{

	/** */
	private static final Logger LOG = LogUtil
			.getLogger(WeightedProductComparatorTest.class);

	@BeforeClass
	public static void logStart()
	{
		LOG.trace("Starting WeightedProductComparator tests!");
	}

	@AfterClass
	public static void logEnd()
	{
		LOG.trace("Completed WeightedProductComparator tests!");
	}

	/**
	 * {@link MyCriterion}
	 * 
	 * @version $Revision: 323 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 */
	public enum MyCriterion implements WeightedCriterion
	{
		/** */
		MIN_OCCUPANT_DISTANCE(
				"minimize: Summed travel distance (time) of all occupants to target space"),

		/** */
		MIN_OCCUPANT_LOAD(
				"minimize: Expected target space load (fraction of capacity used) if all occupants were allocated there"),

		;

		private final String description;

		private MyCriterion(final String description)
		{
			this.description = description;
		}

		/** @see WeightedCriterion#getID() */
		@Override
		public String getID()
		{
			return name();
		}

		/** @see WeightedCriterion#getDescription() */
		@Override
		public String getDescription()
		{
			return this.description;
		}
	}

	/**
	 * {@link Alternative}
	 * 
	 * @version $Revision: 323 $
	 * @author <a href="mailto:Rick@almende.org">Rick</a>
	 *
	 */
	public static class Alternative implements WeightedComparable<MyCriterion>
	{

		/** */
		private static final long serialVersionUID = 1L;

		/** */
		private final Number totalOccupantDistance;

		/** */
		private final Number totalOccupantLoad;

		/**
		 * {@link Alternative} constructor
		 * 
		 * @param totalOccupantDistance
		 * @param totalOccupantLoad
		 */
		public Alternative(final Number totalOccupantDistance,
				final Number totalOccupantLoad)
		{
			this.totalOccupantDistance = totalOccupantDistance;
			this.totalOccupantLoad = totalOccupantLoad;
		}

		@Override
		public String toString()
		{
			return String.format("(totDist=%1.1f, totLoad=%1.1f)",
					this.totalOccupantDistance.doubleValue(),
					this.totalOccupantLoad.doubleValue());
		}

		/** @see WeightedComparable#getValue(WeightedCriterion) */
		@Override
		public Number getValue(final MyCriterion criterion)
		{
			switch (criterion)
			{
			case MIN_OCCUPANT_DISTANCE:
				return this.totalOccupantDistance;
			case MIN_OCCUPANT_LOAD:
				return this.totalOccupantLoad;
			default:
				throw new IllegalStateException("Unexcpected criterion: "
						+ criterion);
			}
		}
	}

	@Test
	public void testComparator() throws Exception
	{
		final EnumMap<MyCriterion, Number> weights = new EnumMap<>(
				MyCriterion.class);
		// set weight for minimizing occupant distance to target area to 40%
		weights.put(MyCriterion.MIN_OCCUPANT_DISTANCE, .4);
		// set weight for minimizing occupant load in target area to 60%
		weights.put(MyCriterion.MIN_OCCUPANT_LOAD, .6);

		final WeightedNavigableMap<MyCriterion, Alternative, String> alternatives = WeightedSkipListMap
				.forWeights(weights, Alternative.class, String.class);
		alternatives.put(new Alternative(14, 0.8), "action for alternative1");
		alternatives.put(new Alternative(12.5, 1.1), "action for alternative2");

		LOG.trace("Initial ordering: " + alternatives);
		LOG.info("Best action (inital weights): "
				+ alternatives.firstEntry().getValue());

		// set occupant distance weight to 200% (will be renormalized against
		// total of 200%+60%=260%)
		alternatives.putWeight(MyCriterion.MIN_OCCUPANT_DISTANCE, 2);
		LOG.trace("Reweighted ordering: " + alternatives);
		LOG.info("Best action (reweighted): "
				+ alternatives.firstEntry().getValue());
	}

}
