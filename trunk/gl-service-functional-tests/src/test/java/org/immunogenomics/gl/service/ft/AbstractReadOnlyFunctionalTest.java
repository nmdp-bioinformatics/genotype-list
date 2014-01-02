/*

    gl-service-functional-tests  Functional tests for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.ft;

import static com.jayway.restassured.RestAssured.expect;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStreamReader;

import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.CharStreams;
import com.jayway.restassured.http.ContentType;

/**
 * Abstract functional test for read-only content types for the URI-based RESTful service.
 */
public abstract class AbstractReadOnlyFunctionalTest {
    protected String uri;
    protected ContentType contentType;
    protected String fileExtension;
    protected String validId;
    protected String invalidId;
    protected String validResponse;

    @BeforeClass
    public static void functionalTestSetup() {
        FunctionalTestSetup.createValidGlResources();
    }

    @Test
    public final void testGet() {
        expect().statusCode(404).when().get(uri);
    }

    @Test
    public final void testGetIdentifier() {
        expect().statusCode(200).contentType(contentType).body(equalTo(validResponse))
            .when().get(uri + "/" + validId + fileExtension);
    }

    @Test
    public final void testGetInvalidIdentifier() {
        expect().statusCode(404).when().get(uri + "/" + invalidId);
    }

    @Test
    public final void testGetInvalidIdentifierFileExtension() {
        expect().statusCode(404).when().get(uri + "/" + invalidId + fileExtension);
    }

    @Test
    public final void testGetInvalidFileExtension() {
        expect().statusCode(404).when().get(uri + "/" + validId + ".invalidFileExtension");
    }

    @Test
    public final void testPostIdentifier() {
        expect().statusCode(404).when().post(uri + "/" + validId);
        expect().statusCode(404).when().post(uri + "/" + validId + fileExtension);
    }

    @Test
    public final void testPostInvalidIdentifier() {
        expect().statusCode(404).when().post(uri + "/" + invalidId);
        expect().statusCode(404).when().post(uri + "/" + invalidId + fileExtension);
    }

    @Test
    public final void testPut() {
        expect().statusCode(405).when().put(uri);
    }

    @Test
    public final void testPutIdentifier() {
        expect().statusCode(405).when().put(uri + "/" + validId);
        expect().statusCode(405).when().put(uri + "/" + validId + fileExtension);
    }

    @Test
    public final void testPutInvalidIdentifier() {
        expect().statusCode(405).when().put(uri + "/" + invalidId + fileExtension);
    }

    @Test
    public final void testDelete() {
        expect().statusCode(405).when().delete(uri);
    }

    @Test
    public final void testDeleteIdentifier() {
        expect().statusCode(405).when().delete(uri + "/" + validId);
        expect().statusCode(405).when().delete(uri + "/" + validId + fileExtension);
    }

    @Test
    public final void testDeleteInvalidIdentifier() {
        expect().statusCode(405).when().delete(uri + "/" + invalidId + fileExtension);
    }

    protected final String readResource(final String name) {
        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(getClass().getResourceAsStream(name));
            return CharStreams.toString(reader);
        }
        catch (IOException e) {
            fail("could not read resource " + name + ", caught " + e.getMessage());
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}