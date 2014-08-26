/* $Id$
 * $URL$
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

/**
 * {@link HttpMethod}
 * 
 * @version $Revision: 358 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public enum HttpMethod
{
	/**
	 * The HTTP GET method is defined in section 9.3 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The GET method means retrieve whatever information (in the form of an
	 * entity) is identified by the Request-URI. If the Request-URI refers
	 * to a data-producing process, it is the produced data which shall be
	 * returned as the entity in the response and not the source text of the
	 * process, unless that text happens to be the output of the process.
	 * </blockquote>
	 */
	GET,

	/**
	 * The HTTP HEAD method is defined in section 9.4 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The HEAD method is identical to GET except that the server MUST NOT
	 * return a message-body in the response. The metainformation contained
	 * in the HTTP headers in response to a HEAD request SHOULD be identical
	 * to the information sent in response to a GET request. This method can
	 * be used for obtaining metainformation about the entity implied by the
	 * request without transferring the entity-body itself. This method is
	 * often used for testing hypertext links for validity, accessibility,
	 * and recent modification. </blockquote>
	 */
	HEAD,

	/**
	 * The HTTP POST method is defined in section 9.5 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The POST method is used to request that the origin server accept the
	 * entity enclosed in the request as a new subordinate of the resource
	 * identified by the Request-URI in the Request-Line. POST is designed
	 * to allow a uniform method to cover the following functions:
	 * <ul>
	 * <li>Annotation of existing resources</li>
	 * <li>Posting a message to a bulletin board, newsgroup, mailing list,
	 * or similar group of articles</li>
	 * <li>Providing a block of data, such as the result of submitting a
	 * form, to a data-handling process</li>
	 * <li>Extending a database through an append operation</li>
	 * </ul>
	 * </blockquote>
	 */
	POST,

	/**
	 * The HTTP PUT method is defined in section 9.6 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The PUT method requests that the enclosed entity be stored under the
	 * supplied Request-URI. If the Request-URI refers to an already
	 * existing resource, the enclosed entity SHOULD be considered as a
	 * modified version of the one residing on the origin server.
	 * </blockquote>
	 */
	PUT,

	/**
	 * The HTTP TRACE method is defined in section 9.6 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The TRACE method is used to invoke a remote, application-layer loop-
	 * back of the request message. The final recipient of the request
	 * SHOULD reflect the message received back to the client as the
	 * entity-body of a 200 (OK) response. The final recipient is either the
	 * origin server or the first proxy or gateway to receive a Max-Forwards
	 * value of zero (0) in the request (see section 14.31). A TRACE request
	 * MUST NOT include an entity. </blockquote>
	 */
	TRACE,

	/**
	 * The HTTP DELETE method is defined in section 9.7 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The DELETE method requests that the origin server delete the resource
	 * identified by the Request-URI. [...] The client cannot be guaranteed
	 * that the operation has been carried out, even if the status code
	 * returned from the origin server indicates that the action has been
	 * completed successfully. </blockquote>
	 */
	DELETE,

	/**
	 * The HTTP OPTIONS method is defined in section 9.2 of <a
	 * href="http://www.ietf.org/rfc/rfc2616.txt">RFC2616</a>: <blockquote>
	 * The OPTIONS method represents a request for information about the
	 * communication options available on the request/response chain
	 * identified by the Request-URI. This method allows the client to
	 * determine the options and/or requirements associated with a resource,
	 * or the capabilities of a server, without implying a resource action
	 * or initiating a resource retrieval. </blockquote>
	 */
	OPTIONS,

	;
}