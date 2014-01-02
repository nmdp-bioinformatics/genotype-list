/*

    gl-oauth  OAuth related projects and samples.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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

import java.util.concurrent.TimeUnit;

/**
 * Details for an Access Token including a dynamic expiresIn property.
 * Currently, this result is obtained from an HTTP POST to the
 * AuthorizationServer with the bearer token as the "token" query parameter.
 */
public class AccessTokenDetails {

    private static final String TOKEN_ID = "tid";
    private static final String EXPIRES_IN = "expires_in";

    private String tokenId; // user or sytem id for tracking purposes.
    private long expiresAt = 0;
    private AuthorizationDetails authorization = new AuthorizationDetails();
    private transient boolean emptyFlag;

    public String getId() {
        return authorization.getId();
    }

    public void setId(String id) {
        authorization.setId(id);
    }

    public String getRealm() {
        return authorization.getRealm();
    }

    public void setRealm(String realm) {
        authorization.setRealm(realm);
    }

    public String[] getScopes() {
        return authorization.getScopes();
    }

    public void setScopes(String[] scope) {
        authorization.setScopes(scope);
    }

    public int getDuration() {
        return authorization.getDuration();
    }

    public void setDuration(int duration) {
        authorization.setDuration(duration);
    }

    public AccessTokenDetails() {}

    public AccessTokenDetails(String content) {
        String[] pairs = content.split(",");
        for (String pair : pairs) {
            int equalPos = pair.indexOf('=');
            if (equalPos > 0) {
                String name = pair.substring(0, equalPos).trim();
                String value = pair.substring(equalPos + 1).trim();
                if (name.equals(TOKEN_ID)) {
                    this.tokenId = value;
                } else if (name.equals(EXPIRES_IN)) {
                    setExpiresIn(Long.parseLong(value));
                } else {
                    authorization.setByName(name, value);
                }
            }
        }
    }

    /**
     * Returns remaining seconds dynamically calculated based on the current time and the expiresAt property.
     * @return number of seconds the token will be valid.
     */
    public long getExpiresIn() {
        return TimeUnit.MILLISECONDS.toSeconds(this.expiresAt - System.currentTimeMillis());
    }

    public void setExpiresIn(long seconds) {
        this.expiresAt = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(seconds);
    }

    public long getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(long expiresAt) {
        this.expiresAt = expiresAt;
    }

    public String getTokenId() {
        return tokenId;
    }

    public void setTokenId(String tokenId) {
        this.tokenId = tokenId;
    }

    public AuthorizationDetails getAuthorization() {
        return authorization;
    }

    public void setAuthorization(AuthorizationDetails authorization) {
        this.authorization = authorization;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, TOKEN_ID, tokenId);
        append(sb, EXPIRES_IN, Long.toString(getExpiresIn()));
        sb.append(',').append(authorization);
        return sb.toString();
    }

    private void append(StringBuilder sb, String name, String value) {
        if (value == null)
            return;
        if (sb.length() > 0)
            sb.append(',');
        sb.append(name).append('=').append(value);
    }

    public static AccessTokenDetails empty() {
        AccessTokenDetails details = new AccessTokenDetails();
        details.emptyFlag = true;
        return details;
    }
    
    public boolean isEmpty() {
        return this.emptyFlag;
    }

}
