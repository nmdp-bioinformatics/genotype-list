/*

    gl-ambiguity-service-functional-tests  Functional tests for RESTful genotype list ambiguity service.
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
package org.immunogenomics.gl.ambiguity.ft;

import static com.jayway.restassured.RestAssured.given;

import static org.hamcrest.Matchers.startsWith;

import org.junit.Before;
import org.junit.Test;

/**
 * Functional tests for RESTful genotype list ambiguity service.
 */
public final class AmbiguityServiceFT {
    private String uri;
    private String textHtml;
    private String textPlain;
    private String applicationJson;
    private String emptyRequestBody;
    private String emptyJson;

    @Before
    public void setUp() {
        uri = "http://localhost:10080/ambiguity";
        textHtml = "text/html";
        textPlain = "text/plain";
        applicationJson = "application/json";
        emptyRequestBody = "";
        emptyJson = "{}";
    }

    @Test
    public void testGet() {
        given().expect().statusCode(200).contentType(textPlain).when().get(uri);
    }

    @Test
    public void testGetInvalidRequest() {
        given().expect().statusCode(404).when().get(uri + "/invalid");
    }

    @Test
    public void testPostInvalidRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(404)
            .when().post(uri + "/invalid");
    }


    @Test
    public void testPostEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/ambiguity");
    }

    @Test
    public void testPostEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/ambiguity");
    }

    @Test
    public void testPostMissingName() {
        String missingName = "{\"uri\":\"http://localhost:10080/gl/allele-list/0\"}";

        given().body(missingName).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/ambiguity");
    }

    @Test
    public void testPostMissingGlstringAndUri() {
        String missingGlstringAndUri = "{\"name\":\"name\"}";

        given().body(missingGlstringAndUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/ambiguity");
    }

    @Test
    public void testPostInvalidUriSyntax() {
        String invalidUriSyntax = "{\"name\":\"name\",\"uri\":\"invalid-uri-syntax\"}";

        given().body(invalidUriSyntax).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/ambiguity");
    }
}
