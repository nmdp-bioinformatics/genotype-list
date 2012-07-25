/*

    gl-service-functional-tests  Functional tests for the URI-based RESTful service for the gl project.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.service.ft;

import static com.jayway.restassured.RestAssured.given;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.jayway.restassured.http.ContentType;

/**
 * Functional test for /locus glstring content type.
 */
public final class LocusGlstringFT extends AbstractFunctionalTest {
    private final List<String> invalidLoci = ImmutableList.of("/", "+", "~", "|", "^", "*", "HLA-A/", "HLA-A+", "HLA-A~", "HLA-A|", "HLA-A^", "HLA-A*", "HLA-A*01:01:01:01");

    @Before
    public void setUp() {
        uri = "http://localhost:10080/gl/locus";
        contentType = ContentType.TEXT;
        fileExtension = ".glstring";
        validId = "0";
        invalidId = "invalid id";
        validResponse = readResource("locus.glstring");
        validRequestBody = "HLA-B";
        invalidRequestBody = "invalid glstring";
        location = "http://localhost:10080/gl/locus/";
    }

    @Test
    public void testPostInvalidLoci() {
        for (String invalidLocus : invalidLoci) {
            given().body(invalidLocus).contentType(contentType).expect().statusCode(400).when().post(uri);
        }
    }
}