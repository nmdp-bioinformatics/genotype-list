/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package org.immunogenomics.gl.client.http.restassured;

import static org.immunogenomics.gl.client.http.HttpClientUtils.isError;

import java.io.InputStream;

import org.immunogenomics.gl.client.http.HttpClient;
import org.immunogenomics.gl.client.http.HttpClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

/**
 * Implementation of HttpClient based on RestAssured.
 */
public final class RestAssuredHttpClient implements HttpClient {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public InputStream get(final String url) throws HttpClientException {
        long start = System.nanoTime();
        Response response = RestAssured.get(url);
        long elapsed = System.nanoTime() - start;
        int statusCode = response.statusCode();
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP GET {} status code {} took {} ns", new Object[] { url, statusCode, elapsed });
        }
        return response.body().asInputStream();
    }

    @Override
    public String post(final String url, final String body) throws HttpClientException {
        long start = System.nanoTime();
        Response response = RestAssured.with().body(body).contentType("text/plain").post(url);
        long elapsed = System.nanoTime() - start;
        int statusCode = response.statusCode();
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP POST {} status code {} took {} ns", new Object[] { url, statusCode, elapsed });
        }
        if (isError(statusCode)) {
            String message = response.getStatusLine();
            throw new HttpClientException(statusCode, message);
        }
        return response.header("Location");
    }
}
