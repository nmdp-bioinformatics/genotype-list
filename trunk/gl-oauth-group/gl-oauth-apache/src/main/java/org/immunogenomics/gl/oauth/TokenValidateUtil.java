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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Utility methods to support OAuth 2.0 implementations.
 * 
 * @author mgeorge
 * 
 */
public final class TokenValidateUtil {

    /** OAuth 2.0 authenticate request header. */
    public static final String AUTHENTICATE = "Authenticate";
    /** OAuth 2.0 authenticate response header. */
    public static final String WWW_AUTHENTICATE = "WWW-Authenticate";

    /** Parameter name for passing the access token. */
    public static final String TOKEN_PARAM = "token";

    /**
     * Returns the token attached to the request by the toPost method.
     * 
     * @param request
     * @return
     */
    public static String extractToken(HttpServletRequest request) {
        return request.getParameter(TOKEN_PARAM);
    }

    /**
     * Returns text/plain content to be returned containing information about
     * the token.
     * 
     * @param request
     * @param validator
     * @return
     */
    public static String generateResponse(HttpServletRequest request, TokenValidator validator) {
        String token = TokenValidateUtil.extractToken(request);
        AccessTokenDetails response = validator.validate(token);
        return response.toString();
    }

    /**
     * Send an error on the response based on the specified RequestScope and
     * AuthorizationException.
     * 
     * @param response
     * @param scope
     * @param details
     * @throws IOException
     */
    public static void sendOAuthError(HttpServletResponse response, RequestScope scope, AuthorizationException details)
            throws IOException
    {
        StringBuilder sb = new StringBuilder();
        sb.append("Bearer");
        sb.append(" realm=\"").append(scope.getRealm()).append('"');
        if (scope.getScopeList() != null) {
            sb.append(", scope=\"").append(scope.getScope()).append('"');
        }
        OAuthErrorCode error = details.getError();
        if (error != null && exists(error.getError())) {
            sb.append(", error=\"").append(error.getError()).append('"');
        }
        if (exists(details.getErrorDescription())) {
            sb.append(", error_description=\"").append(details.getErrorDescription()).append('"');
        }
        response.setHeader(WWW_AUTHENTICATE, sb.toString());
        response.sendError(error.getStatusCode(), error.getStatusMessage());
    }

    public static boolean exists(String str) {
        return str != null && !str.isEmpty();
    }
    
    /** Returns the url used to validate a token. */
    public static String encodeValidateTokenUrl(String validateUrl, String token) {
        return validateUrl + "?" + TokenValidateUtil.TOKEN_PARAM + "=" + token;
    }
    

}