package org.immunogenomics.gl.oauth.openid.portal;

import org.immunogenomics.gl.oauth.JmxTokenValidator;
import org.immunogenomics.gl.oauth.TokenValidator;

/** Convenience class to perform configuration and
 * register the TokenValidator as a JMX Bean 
 * so that it may be used by other wars in the same JVM without configuration. */
public class OAuthInitializer {

    public OAuthInitializer(TokenValidator validator) {
        JmxTokenValidator.registerJmxTokenValidator(validator);
    }
}
