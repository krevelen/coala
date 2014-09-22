/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-common/src/main/java/com/almende/coala/service/resource/ResourceType.java $
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
package io.coala.resource;

import io.coala.log.LogUtil;

import org.apache.log4j.Logger;

/**
 * {@link ResourceType} TOTO use some common library for MIMETypes
 * 
 * @version $Revision: 355 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public enum ResourceType
{
	/** Plain text */
	TEXT("text/plain"),

	/** Extensible Mark-up Language */
	XML("application/xml"),

	/** JavaScript Object Notation */
	JSON("application/json"),

	/**
	 * JavaScript, see
	 * http://stackoverflow.com/questions/4101394/javascript-mime-type
	 */
	JS("application/javascript"),

	;

	/** */
	private static final Logger LOG = LogUtil.getLogger(ResourceType.class);

	/** */
	private final String[] mimeTypes;

	/**
	 * {@link ResourceType} enum constant constructor
	 * 
	 * @param mimeTypes
	 */
	private ResourceType(final String... mimeTypes)
	{
		this.mimeTypes = mimeTypes;
	}

	/**
	 * @param mimeType
	 * @return
	 */
	public static ResourceType ofMIMEType(final String mimeType)
	{
		/*
		public static final ContentType APPLICATION_ATOM_XML = create(
		        "application/atom+xml", Consts.ISO_8859_1);
		public static final ContentType APPLICATION_FORM_URLENCODED = create(
		        "application/x-www-form-urlencoded", Consts.ISO_8859_1);
		public static final ContentType APPLICATION_JSON = create(
		        "application/json", Consts.UTF_8);
		public static final ContentType APPLICATION_OCTET_STREAM = create(
		        "application/octet-stream", (Charset) null);
		public static final ContentType APPLICATION_SVG_XML = create(
		        "application/svg+xml", Consts.ISO_8859_1);
		public static final ContentType APPLICATION_XHTML_XML = create(
		        "application/xhtml+xml", Consts.ISO_8859_1);
		public static final ContentType APPLICATION_XML = create(
		        "application/xml", Consts.ISO_8859_1);
		public static final ContentType MULTIPART_FORM_DATA = create(
		        "multipart/form-data", Consts.ISO_8859_1);
		public static final ContentType TEXT_HTML = create(
		        "text/html", Consts.ISO_8859_1);
		public static final ContentType TEXT_PLAIN = create(
		        "text/plain", Consts.ISO_8859_1);
		public static final ContentType TEXT_XML = create(
		        "text/xml", Consts.ISO_8859_1);
		*/
		final int pos = mimeType.indexOf(';');
		final String canonical = pos < 0 ? mimeType : mimeType
				.substring(0, pos);
		for (ResourceType value : values())
			if (value.mimeTypes != null && value.mimeTypes.length != 0)
				for (String type : value.mimeTypes)
					if (type.equals(canonical))
						return value;

		LOG.warn("No matching type for MIME type: " + mimeType
				+ ", using default");
		return TEXT;
	}

	/**
	 * @return the MIME type of this {@link ResourceType} constant
	 */
	public String getMIMEType()
	{
		return this.mimeTypes == null || this.mimeTypes.length == 0 ? null
				: this.mimeTypes[0];
	}
}