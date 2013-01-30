/*

    gl-oauth-integration-tests - integration tests for gl-oauth-group.
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
package org.immunogenomics.gl.oauth.it;

import static org.junit.Assert.*;
import static com.jayway.restassured.RestAssured.*;
import static com.jayway.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;

/** Web app Integration Test. */
public class SimpleWebIT {

    
    private static final String HLA_Z = "HLA-Z";
    private String url;
    @Before
    public void setupTest() {
        // Port should match servlet.port property in pom.xml
        url = "http://localhost:10080";
    }
    
    private String glUrl(String path) {
        return url + "/gl" + path;
    }
    
    protected String getImmunogenomicToken(String userid) {
        String token = post(url + "/toy/file/get_token?realm=immunogenomics&userid=" + userid).asString();
        assertNotSame("NO_AUTHORIZATION", token);
        return token;
    }

    @Test
    public void testAccess() {
        expect().contentType(ContentType.HTML).when().get(url);
        expect().body(containsString("IMGT")).when().get(glUrl(""));
    }
    
    @Test
    public void testNoAccessToken() {
        expect().statusCode(HttpStatus.SC_BAD_REQUEST)
        .header("WWW-Authenticate", containsString("authorization is required"))
        .when().request().body(HLA_Z)
        .post(glUrl("/locus"));
    }

    @Test
    public void testToyAccessTokenNoWriteAccess() {
        String token = getImmunogenomicToken("eve");
        expect().statusCode(HttpStatus.SC_FORBIDDEN)
        .header("WWW-Authenticate", containsString("scope=\"write\""))
        .when().request().header("Authenticate", "Bearer " + token)
        .contentType(ContentType.TEXT)
        .body(HLA_Z).post(glUrl("/locus"));
    }
    
    @Test
    public void testToyAccessTokenWriteAccess() {
        String token = getImmunogenomicToken("adam");
        
        Response response = expect().statusCode(HttpStatus.SC_CREATED)
        .header("location", containsString("/gl/locus/"))
        .when().request().header("Authenticate", "Bearer " + token)
        .contentType(ContentType.TEXT)
        .body(HLA_Z).post(glUrl("/locus"));
        
        String location = response.header("location");
        
        expect().content(equalTo(HLA_Z)).when().get(location);
    }
    
}
