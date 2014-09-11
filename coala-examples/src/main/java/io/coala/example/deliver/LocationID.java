/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/main/java/io/coala/example/deliver/LocationID.java $
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
package io.coala.example.deliver;

import io.coala.model.ModelComponentID;
import io.coala.model.ModelID;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * {@link LocationID}
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class LocationID extends ModelComponentID<String>
{
	/** */
	private static final long serialVersionUID = 1L;

	/** */
	public List<Coordinate> geoFence = null;

	/**
	 * {@link LocationID} zero-arg bean constructor
	 */
	protected LocationID()
	{
	}

	/**
	 * {@link LocationID} constructor
	 * 
	 * @param companyID
	 * @param zipCode
	 * @param geoFence
	 */
	public LocationID(final ModelID companyID, final String zipCode,
			final Coordinate... geoFence)
	{
		super(companyID, zipCode);
		if (geoFence == null || geoFence.length == 0)
			this.geoFence = Collections.emptyList();
		else
			this.geoFence = Arrays.asList(geoFence);
	}

	/**
	 * @return {@code true} if specified {@code coordinate} is within this
	 *         {@link LocationID}'s geoFence/bounding box, {@code false}
	 *         otherwise
	 */
	public boolean contains(final Coordinate coordinate)
	{
		float minLat = Float.MAX_VALUE, minLon = Float.MAX_VALUE, minAlt = Float.MAX_VALUE, maxLat = Float.MIN_VALUE, maxLon = Float.MIN_VALUE, maxAlt = Float.MIN_VALUE;
		for (Coordinate coord : this.geoFence)
		{
			minLat = Math.min(minLat, coord.latitude);
			minLon = Math.min(minLon, coord.longitude);
			minAlt = Math.min(minAlt, coord.altitude);
			maxLat = Math.max(maxLat, coord.latitude);
			maxLon = Math.max(maxLon, coord.longitude);
			maxAlt = Math.max(maxAlt, coord.altitude);
		}
		return !(coordinate.latitude < minLat || coordinate.latitude > maxLat
				|| coordinate.longitude < minLon
				|| coordinate.longitude > maxLon
				|| coordinate.altitude < minAlt || coordinate.altitude > maxAlt);
	}

	// FIXME extend equals to check if geoFences match
}