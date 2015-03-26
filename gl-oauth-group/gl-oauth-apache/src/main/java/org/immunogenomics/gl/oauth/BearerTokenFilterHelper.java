/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)

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
import java.util.logging.Logger;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter helper class for <a
 * href="http://tools.ietf.org/html/rfc6750">OAuth 2.0 Bearer Token</a>.
 */
public class BearerTokenFilterHelper {

    /** non-standard header. */
    private static final String AUTHENTICATE = "Authenticate";
    public static final String BEARER_TOKEN_REQUIRED = "Bearer token required";
    public static final String TOKEN_MISSING_OR_INVALID = "Bearer token missing or invalid";
    private final Logger logger = Logger.getLogger(getClass().getName());

    private TokenValidator tokenValidator;
    private ScopeEvaluator scopeEvaluator;
    private Authorizer authorizer;
    private AuthorizedHooks authorizedHooks = new NullAuthorizedHooks();

    public BearerTokenFilterHelper(TokenValidator tokenValidator, ScopeEvaluator scopeEvaluator, Authorizer authorizer)
    {
        super();
        this.tokenValidator = tokenValidator;
        this.scopeEvaluator = scopeEvaluator;
        this.authorizer = authorizer;
    }

    /** Filter.doFilter implementation */
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException
    {
        RequestScope scope = scopeEvaluator.analyzeRequest(request);
        try {
            AccessTokenDetails details = checkAuthorization(request);
            authorizer.checkAuthorized(scope, details);
            authorizedHooks.beginAuthorized(request, details);
            filterChain.doFilter(request, response);
        } catch (AuthorizationException ae) {
            TokenValidateUtil.sendOAuthError(response, scope, ae);
        } finally {
            authorizedHooks.endAuthorized();
        }
    }

    /**
     * Extract the Bearer token from the request's Authenticate header and
     * validate it.
     */
    protected AccessTokenDetails checkAuthorization(HttpServletRequest request) throws AuthorizationException {
        String authorization = request.getHeader(OAuthHeader.AUTHORIZATION);
        AuthorizationException exception = null;
        if (authorization == null) {
            // Try alternate
            authorization = request.getHeader(AUTHENTICATE);
        }
        try {
            if (authorization != null) {
                authorization = authorization.trim();
                String bearerPrefix = "Bearer ";
                if (authorization.startsWith(bearerPrefix)) {
                    String bearerToken = authorization.substring(bearerPrefix.length()).trim();
                    AccessTokenDetails validate = tokenValidator.validate(bearerToken);
                    if (validate != null) {
                        return validate;
                    }
                    exception = new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, TOKEN_MISSING_OR_INVALID);
                } else {
                    exception = new AuthorizationException(OAuthErrorCode.INVALID_REQUEST, BEARER_TOKEN_REQUIRED);
                }
            }
        } catch (Exception e) {
            logger.throwing(getClass().getName(), "checkAuthorization", e);
            throw new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, "See log");
        }
        if (exception != null) {
            throw exception;
        }
        logger.fine("no Authentication header");
        return AccessTokenDetails.empty();
    }

    public AuthorizedHooks getAuthorizedHooks() {
        return authorizedHooks;
    }

    public void setAuthorizedHooks(AuthorizedHooks authorizedHooks) {
        if (authorizedHooks == null) {
            authorizedHooks = new NullAuthorizedHooks();
        }
        this.authorizedHooks = authorizedHooks;
    }

    private static class NullAuthorizedHooks implements AuthorizedHooks {
        public void beginAuthorized(HttpServletRequest request, AccessTokenDetails authorization) {}

        public void endAuthorized() {}
    }

}
