/*

    openid-portal OpenID Authentication/Authorization server for the GL Service
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

package org.immunogenomics.gl.oauth.openid.portal.admin;

import java.util.Arrays;
import java.util.List;

import org.immunogenomics.gl.oauth.AuthorizationDetails;

public class ImmunogenomicsAuthorization {
    private static final String WRITE_SCOPE = "write";
    private static final String ALLELE_SCOPE = "allele";
    private static final String SILVER_SCOPE = "silver";
    public static final String REALM = "immunogenomics";
    public static final List<String> SCOPES = Arrays.asList(new String[]{AuthorizationDetails.ADMIN_SCOPE, WRITE_SCOPE, SILVER_SCOPE, ALLELE_SCOPE});

    private AuthorizationDetails details;
    private String id = null;
    private String openid = null;
    private boolean newUser;
    
    
    public ImmunogenomicsAuthorization() {
        details = new AuthorizationDetails();
    }
    
    public ImmunogenomicsAuthorization(String id, AuthorizationDetails authorization, boolean isNew) {
        details = authorization;
        newUser = isNew;
        this.setId(id);
    }

    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return getDetails().getId();
    }

    public void setEmail(String email) {
        getDetails().setId(email);
    }
    
    public boolean isAdmin() {
        return getDetails().hasScope(AuthorizationDetails.ADMIN_SCOPE);
    }
    
    public void setAdmin(boolean admin) {
        setScope(admin, AuthorizationDetails.ADMIN_SCOPE);
    }
    
    public boolean getWriteScope() {
        return getDetails().hasScope(WRITE_SCOPE);
    }
    
    public void setWriteScope(boolean writeScope) {
        setScope(writeScope, WRITE_SCOPE);
    }

    public boolean getAlleleScope() {
        return getDetails().hasScope(ALLELE_SCOPE);
    }
    
    public void setAlleleScope(boolean alleleScope) {
        setScope(alleleScope, ALLELE_SCOPE);
    }
    
    public boolean getSilverScope() {
        return getDetails().hasScope(SILVER_SCOPE);
    }
    
    public void setSilverScope(boolean silverScope) {
        setScope(silverScope, SILVER_SCOPE);
    }
    
    private void setScope(boolean doAdd, String scopeName) {
        if (doAdd) {
            getDetails().addScope(scopeName);
        } else {
            getDetails().removeScope(scopeName);
        }
    }

    public AuthorizationDetails getDetails() {
        if (details == null) {
            details = new AuthorizationDetails();
        }
        return details;
    }
    
    public int getDuration() {
        return details.getDuration();
    }
    
    public void setDuration(int duration) {
        details.setDuration(duration);
    }

    public String[] getScopes() {
        return details.getScopes();
    }

    @Override
    public String toString() {
        return "ImmunogenomicsAuthorization [id=" + id + ", openid=" + openid + ", details=[" + details + "]]";
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUsername() {
        String email = getEmail();
        return email == null ? openid : email;
    }

    public boolean isNewUser() {
        return this.newUser;
    }

    public void setNewUser(boolean newUser) {
        this.newUser = newUser;
    }

}
