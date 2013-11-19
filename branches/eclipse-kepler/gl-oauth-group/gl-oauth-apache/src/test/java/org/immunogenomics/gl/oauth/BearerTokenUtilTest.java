package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.Test;

public class BearerTokenUtilTest {

    @Test
    public void testRandomToken() {
        HashSet<String> set = new HashSet<String>();
        for (int i = 0; i < 1000; i++) {
            String token = BearerTokenUtil.randomToken();
            //System.out.println(token);
            assertTrue("duplicate token " + i, set.add(token));
            BearerTokenUtil.checkToken(token);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckNullToken() {
        BearerTokenUtil.checkToken(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCheckEmpyToken() {
        BearerTokenUtil.checkToken("");
    }
    
    @Test
    public void testCheckToken() {
        String[] goodTokens = { "09aBzZ-_.+/==", "abdf~", "mF_9.B5f-4.1JqM"};
        for (String token : goodTokens) {
            BearerTokenUtil.checkToken(token);
        }
        String[] badTokens = {"aDFsdf?", "?bad"};
        for (String token : badTokens) {
            try {
                BearerTokenUtil.checkToken(token);
                fail("token is invalid: " + token);
            } catch (IllegalArgumentException e) {
                // success
            }
        }
    }

    
}
