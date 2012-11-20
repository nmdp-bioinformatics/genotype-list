/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter helper class for <a
 * href="http://tools.ietf.org/html/rfc6750">OAuth 2.0 Bearer Token</a>.
 */
public class BearerTokenFilterHelper {

    /** OAuth 2.0 authenticate request header. */
    public static final String AUTHENTICATE = "Authenticate";

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
        String authenticate = request.getHeader(AUTHENTICATE);
        try {
            if (authenticate != null) {
                authenticate = authenticate.trim();
                String bearerPrefix = "Bearer ";
                if (authenticate.startsWith(bearerPrefix)) {
                    String bearerToken = authenticate.substring(bearerPrefix.length()).trim();
                    return tokenValidator.validate(bearerToken);
                }
            }
        } catch (Exception e) {
            throw new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, "See log");
        }
        return null;
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
