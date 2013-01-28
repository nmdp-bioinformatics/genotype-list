package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;

import org.junit.Test;

public class JmxTokenValidatorTest implements TokenValidator {

    private static final String TEST_TOKEN ="1a2b3c";
    public static final String[] TEST_SCOPES = {"junit"};

    @Test
    public void testValidate() {
        JmxTokenValidator.registerJmxTokenValidator(this);
        JmxTokenValidator jmxOAuthProvider = new JmxTokenValidator();
        AccessTokenDetails jmxAuth = jmxOAuthProvider.validate(TEST_TOKEN);
        assertArrayEquals(TEST_SCOPES, jmxAuth.getScopes());
        
        jmxAuth = jmxOAuthProvider.validate("BAD-TOKEN");
        assertNull("id", jmxAuth.getId());
        assertEquals(0, jmxAuth.getScopes().length);

    }

    @Override
    public AccessTokenDetails validate(String accessToken) {
        AccessTokenDetails details = new AccessTokenDetails();
        if (TEST_TOKEN.equals(accessToken)) {
            details.setId("user1");
            details.setRealm(getClass().getName());
            details.setScopes(TEST_SCOPES);
        }
        return details;
    }

    @Override
    public void close() {
        throw new RuntimeException("Close shoud not be called");
    }

}
