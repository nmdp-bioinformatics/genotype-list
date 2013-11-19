package org.immunogenomics.gl.oauth;

import static org.junit.Assert.*;

import org.junit.Test;

public class JmxDemoAuthTest implements OAuthProvider {

    private static final String TEST_REALM = JmxDemoAuthTest.class.getName();
    public static final String[] TEST_SCOPES = {"junit"};

    @Test
    public void testDemoAuth() {
        MemoryTokenStore tokenStore = new MemoryTokenStore();
        AuthorizationManager authorizationManager = new AuthorizationManager(this, tokenStore);
        JmxDemoAuth.registerJmxBean(authorizationManager);
        JmxDemoAuth clientDemoAuth = new JmxDemoAuth();
        String accessToken = clientDemoAuth.getDemoAccessToken(TEST_REALM);
        AccessTokenDetails accessTokenDetails = tokenStore.get(accessToken);
        assertEquals("userid", DemoAuthMXBean.DEMO_USER_ID, accessTokenDetails.getId());
        assertEquals("realm", TEST_REALM, accessTokenDetails.getRealm());
        assertEquals("realm", TEST_REALM, accessTokenDetails.getRealm());
        String badToken = clientDemoAuth.getDemoAccessToken("bad realm");
        AccessTokenDetails badDetails = tokenStore.get(badToken);
        assertNull("badDetails", badDetails);
        assertEquals("badToken", "NO_AUTHORIZATION", badToken);
    }

    @Override
    public AuthorizationDetails getAuthorization(String userid, String realm) {
        if (TEST_REALM.equals(realm)) {
            AuthorizationDetails details = new AuthorizationDetails();
            details.setId(userid);
            details.setRealm(realm);
            details.setScopes(TEST_SCOPES);
            details.setDuration(100);
            return details;
        } else {
            return null;
        }
    }

}
