package org.immunogenomics.gl.oauth;

/**
 * Specifies the methods used to support a demo user (non authenticated)
 * running on the same jvm.
 * 
 * @author mgeorge
 *
 */
public interface DemoAuthMXBean {
   // Initially, this will be used to support the API Explorer
    
    String DEMO_USER_ID = "demo@localhost";
    
    /** Returns a new accessToken for a demo user.
     * @param realm the name of the authorization realm
     * @return new accessToken
     */
    String getDemoAccessToken(String realm);
}