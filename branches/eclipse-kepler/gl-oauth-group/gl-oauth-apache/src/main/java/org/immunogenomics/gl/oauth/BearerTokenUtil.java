package org.immunogenomics.gl.oauth;

import java.security.SecureRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utilities methods for <a href="http://tools.ietf.org/html/rfc6750">RFC 6750 Bearer Token</a> 
 */
public class BearerTokenUtil {

    private static final int TOKEN_SIZE = 16;
    private static SecureRandom secureRandom = new SecureRandom();

    private static final String TOKEN_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz._";
    
    private static final Pattern BEARER_TOKEN_PATTERN = Pattern.compile("[A-Za-z0-9/_~\\.\\+\\-]+=*");

    /**
     * Generate a random token that conforms to 
     *  <a href="http://tools.ietf.org/html/rfc6750">RFC 6750 Bearer Token</a> 
     * @return a new 16 character token that is URL Safe (no '+' or '/' characters). */
    public static String randomToken() {
        byte[] bytes = new byte[TOKEN_SIZE];
        secureRandom.nextBytes(bytes);
        StringBuilder sb = new StringBuilder(TOKEN_SIZE);
        for (byte b : bytes) {
            sb.append(TOKEN_CHARS.charAt(b & 0x3F));
        }
        return sb.toString();
    }
    
    /**
     * Verifies the syntax for Bearer token is as follows:
<pre>
     b64token    = 1*( ALPHA / DIGIT / "-" / "." / "_" / "~" / "+" / "/" ) *"="
     credentials = "Bearer" 1*SP b64token
</pre> 
     * @throws exception for an invalid OAuth token. */
    public static void checkToken(String token) throws IllegalArgumentException {
        if (token == null) throw new IllegalArgumentException("token is null");
        int length = token.length();
        if (length == 0) throw new IllegalArgumentException("token is empty");
        Matcher matcher = BEARER_TOKEN_PATTERN.matcher(token);
        if (!matcher.matches()) {
            throw new IllegalArgumentException("Invalid token: " + token);
        }
    }

}
