package org.immunogenomics.gl.oauth.openid.portal;

import org.immunogenomics.gl.oauth.openid.portal.admin.ImmunogenomicsAuthorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SecurityLogger {

    private static final Logger logger = LoggerFactory.getLogger(SecurityLogger.class);
    
    public static void recordLogin(String email, ImmunogenomicsAuthorization user) {
        logger.info("{} was authenticated with {}", email, user);
    }

    public static void recordInvalidToken(String token) {
        logger.info("Invalid token: {}", token);
    }

    public static void recordValidated(String result) {
        logger.info("Token validated:  {}", result);
    }
    
}
