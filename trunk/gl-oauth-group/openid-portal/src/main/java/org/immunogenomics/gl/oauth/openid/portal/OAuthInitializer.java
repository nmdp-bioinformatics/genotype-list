package org.immunogenomics.gl.oauth.openid.portal;

import org.immunogenomics.gl.oauth.AuthorizationManager;
import org.immunogenomics.gl.oauth.JmxDemoAuth;
import org.immunogenomics.gl.oauth.JmxTokenValidator;

/** Convenience class to perform configuration and
 * register the service JMX Beans for JmxTokenValidator and JmxDemoAuth 
 * so that it may be used by other wars in the same JVM without configuration. */
public class OAuthInitializer {

    public OAuthInitializer(AuthorizationManager authorizationManager) {
        JmxTokenValidator.registerJmxTokenValidator(authorizationManager);
        JmxDemoAuth.registerJmxBean(authorizationManager);
    }
}
