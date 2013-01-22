package org.immunogenomics.gl.oauth;

import java.util.List;


public interface AuthorizationDetailsDao extends OAuthProvider {

    void addOrUpdate(AuthorizationDetails details);
    
    /** @return the current total count of records. */
    int count(String realm);
    
    List<AuthorizationDetails> subrange(String realm, int start, int end);
    
}
