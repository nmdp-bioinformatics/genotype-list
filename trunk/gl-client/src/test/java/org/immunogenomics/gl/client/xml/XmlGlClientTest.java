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
package org.immunogenomics.gl.client.xml;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.client.AbstractGlClientTest;
import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.http.HttpClient;
import org.immunogenomics.gl.client.http.restassured.RestAssuredHttpClient;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * Unit test for XmlGlClient.
 */
public final class XmlGlClientTest extends AbstractGlClientTest {
    private static HttpClient httpClient;
    private static Cache<String, Locus> loci;
    private static Cache<String, String> locusIds;
    private static Cache<String, Allele> alleles;
    private static Cache<String, String> alleleIds;
    private static XmlGlClient xmlClient;

    @BeforeClass
    public static void staticSetUp() {
        httpClient = new RestAssuredHttpClient();
        loci = CacheBuilder.newBuilder().initialCapacity(10).build();
        locusIds = CacheBuilder.newBuilder().initialCapacity(10).build();
        alleles = CacheBuilder.newBuilder().initialCapacity(1000).build();
        alleleIds = CacheBuilder.newBuilder().initialCapacity(1000).build();
        xmlClient = new XmlGlClient("http://localhost:8080/gl/", httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Override
    protected GlClient createGlClient() {
        return xmlClient;
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullNamespace() {
        new XmlGlClient(null, httpClient, loci, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHttpClient() {
        new XmlGlClient("http://localhost:8080/gl/", null, loci, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLoci() {
        new XmlGlClient("http://localhost:8080/gl/", httpClient, null, locusIds, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLocusIds() {
        new XmlGlClient("http://localhost:8080/gl/", httpClient, loci, null, alleles, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleles() {
        new XmlGlClient("http://localhost:8080/gl/", httpClient, loci, locusIds, null, alleleIds);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleIds() {
        new XmlGlClient("http://localhost:8080/gl/", httpClient, loci, locusIds, alleles, null);
    }
}