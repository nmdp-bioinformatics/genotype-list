/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)

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
package org.nmdp.gl.client.json;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.InputStream;

import org.apache.http.HttpStatus;
import org.nmdp.gl.Allele;
import org.nmdp.gl.Locus;
import org.nmdp.gl.client.AbstractGlClientTest;
import org.nmdp.gl.client.GlClient;
import org.nmdp.gl.client.http.HttpClient;
import org.nmdp.gl.client.http.HttpClientException;
import org.nmdp.gl.client.http.restassured.RestAssuredHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Unit test for JsonGlClient.
 */
public final class JsonGlClientTest extends AbstractGlClientTest {
    private static JsonFactory jsonFactory;
    private static HttpClient httpClient;
    private static Cache<String, Locus> loci;
    private static Cache<String, String> locusIds;
    private static Cache<String, Allele> alleles;
    private static Cache<String, String> alleleIds;
    private static JsonGlClient jsonClient;

    @BeforeClass
    public static void staticSetUp() {
        jsonFactory = new JsonFactory();
        httpClient = new RestAssuredHttpClient();
        loci = CacheBuilder.newBuilder().initialCapacity(10).build();
        locusIds = CacheBuilder.newBuilder().initialCapacity(10).build();
        alleles = CacheBuilder.newBuilder().initialCapacity(1000).build();
        alleleIds = CacheBuilder.newBuilder().initialCapacity(1000).build();
        jsonClient = new JsonGlClient("http://localhost:8080/gl/", jsonFactory, httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Override
    protected GlClient createGlClient() {
        return jsonClient;
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullNamespace() {
        new JsonGlClient(null, jsonFactory, httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullJsonFactory() {
        new JsonGlClient("http://localhost:8080/gl/", null, httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHttpClient() {
        new JsonGlClient("http://localhost:8080/gl/", jsonFactory, null, loci, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLoci() {
        new JsonGlClient("http://localhost:8080/gl/", jsonFactory, httpClient, null, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLocusIds() {
        new JsonGlClient("http://localhost:8080/gl/", jsonFactory, httpClient, loci, null, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleles() {
        new JsonGlClient("http://localhost:8080/gl/", jsonFactory, httpClient, loci, locusIds, null, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleIds() {
        new JsonGlClient("http://localhost:8080/gl/", jsonFactory, httpClient, loci, locusIds, alleles, null);
    }

    @Test
    public void testBadRequest() {
        JsonGlClient jsonGlClient = createBadRequestClient(HttpStatus.SC_BAD_REQUEST, "bad request");
        try {
            jsonGlClient.getLocus("http://localhost:8080/gl/bad");
            fail("Should throw exception");
        } catch (HttpClientException ex) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, ex.getStatusCode());
        }
        try {
            jsonGlClient.registerLocus("HLA-BAD");
            fail("Should throw exception");
        } catch (HttpClientException ex) {
            assertEquals(HttpStatus.SC_BAD_REQUEST, ex.getStatusCode());
        }
    }

    private JsonGlClient createBadRequestClient(final int statusCode, final String msgPrefix) {
        HttpClient badRequestClient = new HttpClient() {
            @Override
            public String post(String url, String body) throws HttpClientException {
                throw new HttpClientException(HttpStatus.SC_BAD_REQUEST, msgPrefix + url);
            }
            
            @Override
            public InputStream get(String url) {
                throw new HttpClientException(HttpStatus.SC_BAD_REQUEST, msgPrefix + url);
            }
        };
        return new JsonGlClient("http://localhost:8080/gl/", jsonFactory, badRequestClient, loci, locusIds, alleles, alleleIds);
    }
}