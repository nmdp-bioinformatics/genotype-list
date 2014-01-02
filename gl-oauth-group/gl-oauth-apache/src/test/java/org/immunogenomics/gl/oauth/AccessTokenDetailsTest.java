/*

    gl-oauth  OAuth related projects and samples.
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
package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;

import org.junit.Test;

public class AccessTokenDetailsTest {

    @Test 
    public void testExpireProps() {
        AccessTokenDetails details = new AccessTokenDetails();
        details.setExpiresIn(60); // 1 min
        long expiresInSeconds = details.getExpiresIn();
        assertTrue("expiresInSeconds " + expiresInSeconds, expiresInSeconds > 56 && expiresInSeconds <= 60); 
        long expiresAt = details.getExpiresAt();
        assertEquals(expiresAt, details.getExpiresAt());
        expiresAt++;
        details.setExpiresAt(expiresAt);
        assertEquals("expiresAt", expiresAt, details.getExpiresAt());
    }
    
    @Test
    public void testTokenValidateResponse() {
        AccessTokenDetails response = new AccessTokenDetails();
        response.setExpiresAt(System.currentTimeMillis() - 2);
        checkToAndFromString(response);
        response.setExpiresIn(123);
        checkToAndFromString(response);
        response.setId("Fred");
        checkToAndFromString(response);
        response.setRealm("JUnit");
        checkToAndFromString(response);
        String[] scope = { "one", "two" };
        response.setScopes(scope);
        checkToAndFromString(response);

    }

    private void checkToAndFromString(AccessTokenDetails response) {
        String content = response.toString();
        AccessTokenDetails parsedResponse = new AccessTokenDetails(content);
        long delta = Math.abs(response.getExpiresIn() - parsedResponse.getExpiresIn());
        assertFalse("expiresIn delta is > 1 second: " + delta, delta > 1);
        long deltaMs = Math.abs(response.getExpiresAt() - parsedResponse.getExpiresAt());
        assertFalse("expiresAt time delta > 1010 ms: " + deltaMs, deltaMs > 1010);
        assertEquals(response.getId(), parsedResponse.getId());
        assertEquals(response.getRealm(), parsedResponse.getRealm());
        assertArrayEquals("scope array", response.getScopes(), parsedResponse.getScopes());
    }

}
