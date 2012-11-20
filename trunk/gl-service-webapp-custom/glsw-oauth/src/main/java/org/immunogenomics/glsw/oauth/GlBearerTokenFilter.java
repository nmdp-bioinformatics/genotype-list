package org.immunogenomics.glsw.oauth;

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

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.immunogenomics.gl.oauth.AccessTokenDetails;
import org.immunogenomics.gl.oauth.AuthorizationCache;
import org.immunogenomics.gl.oauth.AuthorizationException;
import org.immunogenomics.gl.oauth.AuthorizedHooks;
import org.immunogenomics.gl.oauth.BearerTokenFilterHelper;
import org.immunogenomics.gl.oauth.DefaultAuthorizer;
import org.immunogenomics.gl.oauth.MemoryTokenStore;
import org.immunogenomics.gl.oauth.RequestScope;
import org.immunogenomics.gl.oauth.ScopeEvaluator;
import org.immunogenomics.gl.oauth.TokenValidator;
import org.immunogenomics.gl.oauth.apache.RemoteTokenValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlBearerTokenFilter extends DefaultAuthorizer implements Filter, ScopeEvaluator, AuthorizedHooks {

    private String realm;
    private TokenValidator tokenValidator;
    private BearerTokenFilterHelper filterHelper;
    private Logger logger = LoggerFactory.getLogger(getClass());

    public void init(FilterConfig config) throws ServletException {
        realm = config.getInitParameter("realm");
        String validateUrl = config.getInitParameter("validateUrl");
        validateUrl = System.getProperty("oauth.validate.url", validateUrl);
        tokenValidator = new RemoteTokenValidator(validateUrl);
        tokenValidator = new AuthorizationCache(tokenValidator, new MemoryTokenStore());
        filterHelper = new BearerTokenFilterHelper(tokenValidator, this, this);
        logger.info("initialized realm={} with {}", realm, validateUrl);
    }

    public void destroy() {
        tokenValidator.close();
        logger.debug("destroyed.");
    }

    /* ScopeEvaluator */
    public RequestScope analyzeRequest(HttpServletRequest request) {
        String method = request.getMethod();
        String scope;
        String path = request.getServletPath();
        if (path == null) {
            scope = null;
        } else if (path.startsWith("/load")) {
            scope = "load";
        } else if (path.startsWith("/locus")) {
            scope = "locus";
        } else if (path.startsWith("/allele")) {
            scope = "allele";
        } else {
            scope = "level1";
        }
        RequestScope scopeDetails = new RequestScope(method, realm, scope);
        logger.debug("Path: {} \t {}", path, scopeDetails);
        return scopeDetails;
    }

    @Override
    public void checkAuthorized(RequestScope requestScope, AccessTokenDetails authorization)
            throws AuthorizationException
    {
        if ("GET".equals(requestScope.getMethod())) {
            // Unprotected
            return;
        }
        // POST and other methods require Authorization
        super.checkAuthorized(requestScope, authorization);
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
            ServletException
    {
        filterHelper.doFilter((HttpServletRequest) request, (HttpServletResponse) response, filterChain);
    }

    public void beginAuthorized(HttpServletRequest request, AccessTokenDetails authorization) {
        request.setAttribute("authorizationId", authorization.getId());
        request.setAttribute("authorizationScopes", authorization.getScopes());
    }

    public void endAuthorized() {
        // not needed since authorization info is attached to request.
    }

}
