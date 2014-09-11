/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/util/WebUtil.java $
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
package io.coala.web;

import io.coala.log.LogUtil;
import io.coala.util.Util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.log4j.Logger;

/**
 * {@link WebUtil} contains internet-related utility methods
 * 
 * @version $Revision: 296 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 */
public class WebUtil implements Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(WebUtil.class);

	/** */
	private static final String DEFAULT_CHARSET = "UTF-8";

	/**
	 * @param data
	 * @return
	 */
	public static String urlEncode(final String data)
	{
		return urlEncode(data,DEFAULT_CHARSET);
	}

	/**
	 * @param data
	 * @return
	 */
	@SuppressWarnings("deprecation")
	public static String urlEncode(final String data, final String charset)
	{
		try
		{
			return URLEncoder.encode(data, charset);
		} catch (final UnsupportedEncodingException e)
		{
			LOG.warn("Problem encoding agent id using: " + DEFAULT_CHARSET, e);
			return URLEncoder.encode(data);
		}
	}

	/**
	 * {@link WebUtil} constructor
	 */
	private WebUtil()
	{
		// utility class should not produce protected/public instances
	}

}
