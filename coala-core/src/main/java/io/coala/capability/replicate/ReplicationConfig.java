/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/ReplicationConfig.java $
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
package io.coala.capability.replicate;

import io.coala.model.ModelID;
import io.coala.time.ClockID;
import io.coala.time.SimTimeFactory;
import io.coala.time.TimeUnit;

import org.aeonbits.owner.Config;
import org.joda.time.Interval;

/**
 * {@link ReplicationConfig} TODO switch to <A
 * href="http://owner.aeonbits.org/">OWNER API</a>
 * 
 * @version $Revision: 324 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public interface ReplicationConfig extends Config
{
	ModelID getReplicationID();

	ClockID getClockID();

	Interval getInterval();

	TimeUnit getBaseTimeUnit();

	long getSeed();

	SimTimeFactory newTime();
}