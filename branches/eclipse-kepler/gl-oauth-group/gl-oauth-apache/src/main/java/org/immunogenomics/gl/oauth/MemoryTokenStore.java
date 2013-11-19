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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

public class MemoryTokenStore implements TokenStore {
    private ConcurrentHashMap<String, AccessTokenDetails> tokenToAuthorization =
            new ConcurrentHashMap<String, AccessTokenDetails>();
    private int cleanAfterCount = 100;
    private int cleanUpCountDown = cleanAfterCount;

    private void cleanUp() {
        if (cleanUpCountDown == 0) {
            cleanUpCountDown = cleanAfterCount;
            // Remove any old-expired Authorizations
            long oldTime = System.currentTimeMillis() - TimeUnit.MINUTES.toMillis(10);
            for (String token : tokenToAuthorization.keySet()) {
                AccessTokenDetails details = tokenToAuthorization.get(token);
                if (oldTime > details.getExpiresAt()) {
                    tokenToAuthorization.remove(token);
                }
            }
        }
    }

    public AccessTokenDetails get(String token) {
        return (token == null) ? null : tokenToAuthorization.get(token);
    }

    public String add(AccessTokenDetails authorization) {
        cleanUp();
        String token = createToken();
        put(token, authorization);
        return token;
    }

    String createToken() {
        String token = BearerTokenUtil.randomToken();
        // Extra check to make sure a duplicate is never returned.
        while (tokenToAuthorization.get(token) != null) {
            token = BearerTokenUtil.randomToken();
        }
        return token;
    }

    public void put(String token, AccessTokenDetails authorization) {
        tokenToAuthorization.put(token, authorization);
    }

    public void dispose() {
        tokenToAuthorization.clear();
        tokenToAuthorization = null;
    }
}
