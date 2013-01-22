/*

    gl-oauth  OAuth related projects and samples.
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
package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;

import java.io.File;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;

public class FileOAuthProviderTest {

    public static final String TEST_USERID = "test@immunogenomics.org";
    public static final String TEST_REALM = "FileOAuthProviderTest";
    private Random rand;
    
    @Before
    public void setupTest() {
        rand = new Random();
    }

    @Test
    public void test() {
        FileOAuthProvider provider = new FileOAuthProvider("/userid-scopes-test.txt");
        AuthorizationDetails details = newDetails();
        provider.add(details);
        System.out.println(provider);

        AuthorizationDetails authorization = provider.getAuthorization(details.getId(), details.getRealm());
        assertEquals(details.getId(), authorization.getId());
    }

    private AuthorizationDetails newDetails() {
        AuthorizationDetails details = new AuthorizationDetails();
        details.setId(TEST_USERID);
        details.setRealm(TEST_REALM);
        details.setDuration(100);
        String randomStr = "rand"+ rand.nextInt(1000);
        details.setScopes(new String[]{randomStr, "junit"});
        return details;
    }
    
    @Test
    public void testLoadAndStore() {
        File authDetailsFile = new File("target/test/auth-details.txt");
        authDetailsFile.mkdirs();
        authDetailsFile.delete();  // make sure no previous file
        FileOAuthProvider testProvider = new FileOAuthProvider(authDetailsFile);
        AuthorizationDetails details = newDetails();
        testProvider.addOrUpdate(details);
        
        // reload data
        testProvider = new FileOAuthProvider(authDetailsFile);
        AuthorizationDetails authorization = testProvider.getAuthorization(details.getId(), details.getRealm());
        assertNotNull("authorization", authorization);
        assertEquals(details.getId(), authorization.getId());

    }

    @Test
    public void testUserHome() {
        //NOTE: The user home file will likely already exist, 
        // so we'll just try a couple small modifications to avoid potential conflicts.
        AuthorizationDetailsDao userHomeProvider1 = new FileOAuthProvider();
        AuthorizationDetails details = newDetails();
        userHomeProvider1.addOrUpdate(details);
        AuthorizationDetails adminUser = newDetails();
        adminUser.setId("admin@immunogenomics.org");
        adminUser.setScopes(AuthorizationDetails.ADMIN_SCOPES);
        userHomeProvider1.addOrUpdate(adminUser);

        
        AuthorizationDetailsDao userHomeProvider2 = new FileOAuthProvider();
        AuthorizationDetails authorization = userHomeProvider2.getAuthorization(details.getId(), details.getRealm());
        assertNotNull("authorization", authorization);
        String[] scopes = authorization.getScopes();
        assertArrayEquals(details.getScopes(), scopes);
        
    }
}
