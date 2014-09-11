/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/scheduler/AbstractJob.java $
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
package io.coala.process;

import io.coala.config.CoalaProperty;
import io.coala.name.Identifier;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * {@link AbstractJob}
 * 
 * @version $Revision: 309 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public abstract class AbstractJob<ID extends Identifier<?, ?>> extends
		AbstractProcess<ID> implements Job<ID>
{

	/** */
	private static final long serialVersionUID = 1L;

	/** */
	private final String stackTrace;

	/**
	 * {@link AbstractJob} zero-arg bean constructor
	 */
	protected AbstractJob()
	{
		super();
		this.stackTrace = null;
	}

	/**
	 * {@link AbstractJob} constructor
	 * 
	 * @param id
	 */
	public AbstractJob(final ID id)
	{
		super(id);
		if (CoalaProperty.addOriginatorStackTrace.value().getBoolean())
		{
			final StringWriter sw = new StringWriter();
			final PrintWriter pw = new PrintWriter(sw);
			new Exception().printStackTrace(pw);

			this.stackTrace = sw.toString();
		} else
			this.stackTrace = null;
	}

	/** @see Job#getStackTrace() */
	@Override
	public String getStackTrace()
	{
		return this.stackTrace;
	}

}
