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

import java.util.Arrays;
import java.util.List;

public class DefaultAuthorizer implements Authorizer {

    /**
     * Verifies the authorization is valid for the
     * 
     * @param requestScope
     * @param authorization
     * @throws AuthorizationException
     *             if authorization is not valid.
     */
    public void checkAuthorized(RequestScope requestScope, AccessTokenDetails authorization)
            throws AuthorizationException
    {
        if (authorization == null) {
            throw new AuthorizationException(OAuthErrorCode.INVALID_REQUEST, "authorization is required");
        }
        if (!requestScope.hasRealm(authorization.getRealm())) {
            throw new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, "token does not have realm "
                    + requestScope.getRealm());
        }
        checkExpiration(authorization.getExpiresIn());
        checkScope(requestScope.getScopeList(), authorization.getScopes());
    }

    /**
     * Check if the expiresIn has passed.
     * 
     * @param expiresIn
     * @throws AuthorizationException
     */
    protected void checkExpiration(long expiresIn) throws AuthorizationException {
        if (expiresIn <= 0) {
            throw new AuthorizationException(OAuthErrorCode.INVALID_TOKEN, "token has expired");
        }
    }

    /**
     * Make sure that no scope has been requested or the desiredScope is in the
     * allowedScopes.
     * 
     * @param desiredScopes
     * @param allowedScopes
     * @throws AuthorizationException
     */
    protected void checkScope(List<String> desiredScopes, String[] allowedScopes) throws AuthorizationException {
        if (desiredScopes == null)
            return;
        List<String> allowedList = Arrays.asList(allowedScopes);
        for (String desiredScope : desiredScopes) {
            if (!allowedList.contains(desiredScope)) {
                throw new AuthorizationException(OAuthErrorCode.INSUFFICIENT_SCOPE, "requires additional scope " + desiredScope);
            }
        }
    }

}
