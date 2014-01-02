/*

    gl-client-functional-tests  Functional tests for the client library for the
    URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client.ft;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import org.junit.BeforeClass;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.client.GlClient;

import org.immunogenomics.gl.client.http.HttpClient;
import org.immunogenomics.gl.client.http.restassured.RestAssuredHttpClient;
import org.immunogenomics.gl.client.json.JsonGlClient;

/**
 * Functional test for JsonGlClient.
 */
public final class JsonGlClientFT extends AbstractGlClientFT {
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
        jsonClient = new JsonGlClient("http://localhost:10080/gl/", jsonFactory, httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Override
    protected GlClient createGlClient() {
        return jsonClient;
    }
}