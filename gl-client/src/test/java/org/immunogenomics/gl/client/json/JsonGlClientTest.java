/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.client.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.fasterxml.jackson.core.JsonFactory;

import java.io.InputStream;

import org.apache.http.HttpStatus;

import org.junit.BeforeClass;
import org.junit.Test;

import org.immunogenomics.gl.client.AbstractGlClientTest;
import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.GlClientHttpException;
import org.immunogenomics.gl.client.HttpGetOrPost;

/**
 * Unit test for JsonGlClient.
 */
public final class JsonGlClientTest extends AbstractGlClientTest {
    private static JsonFactory jsonFactory;
    private static JsonGlClient jsonClient;

    @BeforeClass
    public static void staticSetUp() {
        jsonFactory = new JsonFactory();
        jsonClient = new JsonGlClient("http://localhost:8080/gl/", jsonFactory);
    }

    @Override
    protected GlClient createGlClient() {
        return jsonClient;
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullNamespace() {
        new JsonGlClient(null, jsonFactory);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullJsonFactory() {
        new JsonGlClient("http://localhost:8080/gl", null);
    }
    
    @Test
    public void testBadRequest() {
        JsonGlClient jsonGlClient = createBadRequestClient(HttpStatus.SC_BAD_REQUEST, "bad request");
        try {
            jsonGlClient.getLocus("http://localhost:8080/gl/bad");
            fail("Should throw exception");
        } catch (GlClientHttpException ex) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, ex.getStatusCode());
        }
        try {
            jsonGlClient.registerLocus("HLA-BAD");
            fail("Should throw exception");
        } catch (GlClientHttpException ex) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, ex.getStatusCode());
        }
    }

    private JsonGlClient createBadRequestClient(final int statusCode, final String msgPrefix) {
        JsonGlClient jsonGlClient = new JsonGlClient("http://localhost:8080/gl", jsonFactory);
        jsonGlClient.http = new HttpGetOrPost() {
            @Override
            public InputStream get(String url, String bearerToken) throws GlClientHttpException {
                throw new GlClientHttpException(HttpStatus.SC_BAD_REQUEST, msgPrefix + url);
            }
            @Override
            public String post(String url, String body, String bearerToken) throws GlClientHttpException {
                throw new GlClientHttpException(HttpStatus.SC_BAD_REQUEST, msgPrefix + url);
            }
        };
        return jsonGlClient;
    }
    
}