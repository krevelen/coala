/* $Id$
 * $URL: https://dev.almende.com/svn/abms/coala-examples/src/test/java/io/coala/example/deliver/DealOAuth1Util.java $
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

import io.coala.log.LogUtil;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

import org.apache.log4j.Logger;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.InputStreamContent;
import com.google.api.client.http.javanet.NetHttpTransport;

/**
 * {@link DealOAuth1Util}
 * 
 * @version $Revision: 295 $
 * @author <a href="mailto:Rick@almende.org">Rick</a>
 *
 */
public class DealOAuth1Util
{

	/** */
	private static final Logger LOG = LogUtil.getLogger(DealOAuth1Util.class);

	private static final HttpTransport TRANSPORT = new NetHttpTransport();

	// Some static information on the DEAL platform, and the SECRET.
	private static final String APP_ID = "deal-deliver";
	private static final String CONSUMER_KEY = APP_ID + ".appspot.com";
	private static final String CONSUMER_SECRET = "FJYAAxMM7R6l_el8vrA268bv"; // DEAL-DELIVER
																				// CONSUMER
																				// SECRET

	private static final String PROTECTED_SERVICE_URL = "http://"
			+ CONSUMER_KEY + "/rest2/orders/amount";
	private static final String PROTECTED_ORDERS_URL = "http://" + CONSUMER_KEY
			+ "/rest2/orders/";

	private static final String OAUTH_REQUEST_URL = "https://" + CONSUMER_KEY
			+ "/_ah/OAuthGetRequestToken";
	private static final String OAUTH_AUTHORIZE_URL = "https://" + CONSUMER_KEY
			+ "/_ah/OAuthAuthorizeToken";
	private static final String OAUTH_ACCESS_URL = "https://" + CONSUMER_KEY
			+ "/_ah/OAuthGetAccessToken";

	private static final String JSON_IDENTIFIER = "application/json";

	/**
	 * Method to execute the 3-legged OAUTH authentication. As a test a
	 * protected resource is accessed.
	 * 
	 * @throws Exception on an exception occuring
	 */
	public static void consumeProtectedResource() throws Exception
	{

		// this signer will be used to sign all the requests in the
		// "oauth dance"
		final OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = CONSUMER_SECRET;

		// Step 1: Get a request token. This is a temporary token that is used
		// for
		// having the user authorize an access token and to sign the request to
		// obtain
		// said access token.
		final OAuthGetTemporaryToken requestToken = new OAuthGetTemporaryToken(
				OAUTH_REQUEST_URL);
		requestToken.consumerKey = CONSUMER_KEY;
		requestToken.transport = TRANSPORT;
		requestToken.signer = signer;

		final OAuthCredentialsResponse requestTokenResponse = requestToken
				.execute();

		System.out.println(String.format("Authorization Request:%n"
				+ "\t- oauth_token        = %s%n"
				+ "\t- oauth_token_secret = %s", requestTokenResponse.token,
				requestTokenResponse.tokenSecret));

		// updates signer's token shared secret
		signer.tokenSharedSecret = requestTokenResponse.tokenSecret;

		final OAuthAuthorizeTemporaryTokenUrl authorizeUrl = new OAuthAuthorizeTemporaryTokenUrl(
				OAUTH_AUTHORIZE_URL);
		authorizeUrl.temporaryToken = requestTokenResponse.token;

		// After the user has granted access to you, the consumer, the provider
		// will
		// redirect you to whatever URL you have told them to redirect to. You
		// can
		// usually define this in the oauth_callback argument as well.

		final URI uri = new URI(authorizeUrl.build());

		// TODO move to separate utility class
		try
		{
			if (Desktop.isDesktopSupported())
				Desktop.getDesktop().browse(uri);
			else
				System.out.println("Please authorize this app at URL:\n"
						+ uri.toASCIIString());
			// throw new OperationNotSupportedException("Desktop unsupported");
		} catch (final Throwable t)
		{
			// get OS name
			final String OS = System.getProperty("os.name").toLowerCase();

			// determine browser command - TODO test non-Windows commands
			String cmd = null;
			if (OS.indexOf("cyg") >= 0) // Cygwin
				cmd = String.format("cygstart %s", uri.toASCIIString());
			else if (OS.indexOf("win") >= 0) // MS Windows
				cmd = String.format("cmd /k explorer \"%s\"",
						uri.toASCIIString());
			else if (OS.indexOf("mac") >= 0) // MacOS
				cmd = String.format("open %s", uri.toASCIIString());
			else if (OS.indexOf("ix") >= 0 || OS.indexOf("nux") >= 0) // *nix
				cmd = String.format("xdg-open %s", uri.toASCIIString());
			else if (OS.indexOf("sunos") >= 0) // Solaris (if without xdg-utils)
				cmd = String.format("/usr/dt/bin/sdtwebclient %s",
						uri.toASCIIString());

			if (cmd == null)
				System.out.println("Please authorize this app at URL:\n"
						+ uri.toASCIIString());
			else
				try
				{
					Runtime.getRuntime().exec(cmd);
				} catch (final Throwable t1)
				{
					LOG.trace("Problem opening URL: " + uri.getHost(), t1);
					System.out.println("Please authorize this app at URL:\n"
							+ uri.toASCIIString());
				}
		}

		final InputStreamReader converter = new InputStreamReader(System.in);
		final BufferedReader in = new BufferedReader(converter);

		// TODO replace with connect trial loop
		while (in.readLine().equalsIgnoreCase("n"))
			System.out.println("Have you authorized me? (y/n)");

		// temporary disabled for debugging:

		// Step 3: Once the consumer has redirected the user back to the
		// oauth_callback
		// URL you can request the access token the user has approved. You use
		// the
		// request token to sign this request. After this is done you throw away
		// the
		// request token and use the access token returned. You should store
		// this
		// access token somewhere safe, like a database, for future use.
		final OAuthGetAccessToken accessToken = new OAuthGetAccessToken(
				OAUTH_ACCESS_URL);
		accessToken.consumerKey = CONSUMER_KEY;
		accessToken.signer = signer;
		accessToken.transport = TRANSPORT;
		accessToken.temporaryToken = requestTokenResponse.token;

		final OAuthCredentialsResponse accessTokenResponse = accessToken
				.execute();

		System.out.println(String.format("Authorization access for %s:%n"
				+ "\t- oauth_token        = %s%n"
				+ "\t- oauth_token_secret = %s", CONSUMER_KEY,
				accessTokenResponse.token, accessTokenResponse.tokenSecret));

		// updates signer's token shared secret
		signer.tokenSharedSecret = accessTokenResponse.tokenSecret;

		final OAuthParameters parameters = new OAuthParameters();
		parameters.consumerKey = CONSUMER_KEY;
		parameters.token = accessTokenResponse.token;
		parameters.signer = signer;

		// utilize accessToken to access protected resource in DEAL
		final HttpRequestFactory factory = TRANSPORT
				.createRequestFactory(parameters);
		final GenericUrl url = new GenericUrl(PROTECTED_SERVICE_URL);
		final HttpRequest req = factory.buildGetRequest(url);

		LOG.trace("Executing request to URL: " + req.getUrl());
		final HttpResponse resp = req.execute();
		System.out.println("Response Status Code: " + resp.getStatusCode());
		System.out.println("Response body:" + resp.parseAsString());

	}

	/**
	 * Post an order using the token and secret token to authenticate in DEAL.
	 * To demonstrate successful authentication a POST request is executed.
	 * 
	 * @param messageToBePosted the JSON string representing the order
	 * @return the result of the post action
	 * 
	 * @throws Exception on an exception occurring
	 */
	public static String PostOrderWithTokensOnly(String messageToBePosted)
			throws Exception
	{
		// utilize accessToken to access protected resource in DEAL
		HttpRequestFactory factory = TRANSPORT
				.createRequestFactory(getParameters());
		GenericUrl url = new GenericUrl(PROTECTED_ORDERS_URL);
		InputStream stream = new ByteArrayInputStream(
				messageToBePosted.getBytes());
		InputStreamContent content = new InputStreamContent(JSON_IDENTIFIER,
				stream);
		HttpRequest req = factory.buildPostRequest(url, content);

		HttpResponse resp = req.execute();
		String response = resp.parseAsString();

		// log the response
		if (resp.getStatusCode() != 200 && LOG.isInfoEnabled())
		{
			LOG.info("Response Status Code: " + resp.getStatusCode());
			LOG.info("Response body:" + response);
		}

		return response;
	}

	/**
	 * Put an order using the token and secret token to authenticate in DEAL. To
	 * demonstrate successful authentication a POST request is executed.
	 * 
	 * @param messageToBePosted the JSON string representing the order
	 * @param identifier the identifier of the order to Put
	 * 
	 * @throws Exception on an exception occurring
	 */
	public static void PutOrderWithTokensOnly(String messageToBePosted,
			String identifier) throws Exception
	{
		// utilize accessToken to access protected resource in DEAL
		HttpRequestFactory factory = TRANSPORT
				.createRequestFactory(getParameters());
		GenericUrl url = new GenericUrl(PROTECTED_ORDERS_URL + identifier);
		InputStream stream = new ByteArrayInputStream(
				messageToBePosted.getBytes());
		InputStreamContent content = new InputStreamContent(JSON_IDENTIFIER,
				stream);

		HttpRequest req = factory.buildPutRequest(url, content);

		req.getContent().writeTo(System.out);
		HttpResponse resp = req.execute();

		// Should we consider posting the data in case the put fails?

		// log the response
		if (LOG.isInfoEnabled())
		{
			LOG.info("Response Status Code: " + resp.getStatusCode());
			LOG.info("Response body:" + resp.parseAsString());
		}
	}

	/**
	 * get a message using the token and secret token to authenticate in DEAL.
	 * To demonstrate successfull authentication a POST request is executed.
	 * 
	 * @throws Exception on an exception occuring
	 */
	public static String getOrderWithTokensOnly(String urlToBeGet)
			throws Exception
	{
		// utilize accessToken to access protected resource in DEAL
		HttpRequestFactory factory = TRANSPORT
				.createRequestFactory(getParameters());
		GenericUrl url = new GenericUrl(urlToBeGet);
		HttpRequest req = factory.buildGetRequest(url);

		HttpResponse resp = req.execute();
		req.getContent().writeTo(System.out);
		String responseString = resp.parseAsString();

		// Log
		if (LOG.isInfoEnabled())
		{
			LOG.info("Response Status Code: " + resp.getStatusCode());
			LOG.info("Response body:" + responseString);
		}

		return responseString;
	}

	/**
	 * Method to retrieve the OAUTHParameters to be used for the HTTP connection
	 * 
	 * @return the OAUTHParameters to be used for the HTTP connection
	 */
	private static OAuthParameters getParameters()
	{
		// Tokens for suki@almende.org
		final String TOKEN = "1/uMD0EOMX_4IvJhbKL4EgropZedEZ1ipamLv12oK2F00";
		final String SECRETTOKEN = "IbQ5mS-0rOzWqXDngpYj_RhD";

		// TODO generate and use tokens for app/developer rick@almende.org

		// initiate the signer
		final OAuthHmacSigner signer = new OAuthHmacSigner();
		signer.clientSharedSecret = CONSUMER_SECRET;
		signer.tokenSharedSecret = SECRETTOKEN;

		// initiate the parameters for the HTTP request factory
		final OAuthParameters parameters = new OAuthParameters();
		parameters.consumerKey = CONSUMER_KEY;
		parameters.token = TOKEN;
		parameters.signer = signer;

		return parameters;
	}

	public static void main(final String[] args)
	{
		try
		{
			DealOAuth1Util.consumeProtectedResource();
		} catch (final Throwable e)
		{
			LOG.fatal("Problem running OAuth", e);
		}
	}

}