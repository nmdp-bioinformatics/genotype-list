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

import java.util.ArrayList;
import java.util.List;

import org.immunogenomics.gl.oauth.AuthorizationDetails;
import org.immunogenomics.gl.oauth.AuthorizationDetailsDao;
import org.springframework.beans.factory.annotation.Autowired;

public class ImmunogenomicsAuthorizationDao {

    private static final int ITEM_ID_RADIX = 36;
    @Autowired
    private AuthorizationDetailsDao authorizationDetailsDao;

    private static class Identity {
        String id; //required
        String email;  // required
        String openid;
    }
    private List<Identity> identityList = new ArrayList<Identity>();

    public ImmunogenomicsAuthorization getAuthorization(String itemId) {
        Identity identity = byId(itemId);
        if (identity == null) {
            return findByEmail("");
        }
        return loadOrCreate(identity);
    }

    public ImmunogenomicsAuthorization findByEmail(String email) {
        if (email == null) return null;
        return loadOrCreate(byEmail(email));
    }
    
    /**
     * @return matching Identity or null.
     */
    private synchronized Identity byId(String itemId) {
        if (itemId == null || itemId.isEmpty()) return null;
        int index = 0;
        try {
            index = Integer.parseInt(itemId, 36);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        }
        if (index < 0 || index >= identityList.size()) return null;
        return identityList.get(index);
    }

    /**
     * Find or create an Identify for the specified email.
     * @param email
     * @return a valid Identify
     */
    private synchronized Identity byEmail(String email) {
        if (email == null) return null;
        for (Identity identity : identityList) {
            if (email.equals(identity.email)) {
                return identity;
            }
        }
        Identity identity = new Identity();
        identity.email = email;
        identity.id = Integer.toString(identityList.size(), ITEM_ID_RADIX);
        identityList.add(identity);
        return identity;
    }

    /**
     * @return the Identify for openid or null if not found
     */
    private synchronized Identity byOpenid(String openid) {
        if (openid == null) return null;
        for (Identity identity : identityList) {
            if (openid.equals(identity.openid)) {
                return identity;
            }
        }
        return null;
    }
    
    private ImmunogenomicsAuthorization loadOrCreate(Identity identity) {
        boolean isNew = false;
        AuthorizationDetails authorization = authorizationDetailsDao.getAuthorization(identity.email, ImmunogenomicsAuthorization.REALM);
        if (authorization == null) {
            isNew = true;
            authorization = buildDefaultAuthorizationDetails(identity.email);
            authorizationDetailsDao.addOrUpdate(authorization);
            authorization = authorizationDetailsDao.getAuthorization(identity.email, ImmunogenomicsAuthorization.REALM);
        }
        ImmunogenomicsAuthorization immunogenomicsAuthorization = new ImmunogenomicsAuthorization(identity.id, authorization, isNew);
        immunogenomicsAuthorization.setOpenid(identity.openid);
        return immunogenomicsAuthorization;
    }
    
    public static AuthorizationDetails buildDefaultAuthorizationDetails(String userid) {
        AuthorizationDetails authorization = new AuthorizationDetails();
        authorization.setId(userid);
        authorization.setDuration(1000);
        authorization.setRealm(ImmunogenomicsAuthorization.REALM);
        return authorization;
    }

    public List<ImmunogenomicsAuthorization> subrange(int firstResult, int sizeNo) {
        List<AuthorizationDetails> subrange = authorizationDetailsDao.subrange(ImmunogenomicsAuthorization.REALM, firstResult, sizeNo);
        List<ImmunogenomicsAuthorization> resultList = new ArrayList<ImmunogenomicsAuthorization>(subrange.size());
        for (AuthorizationDetails authorizationDetails : subrange) {
            resultList.add(findByEmail(authorizationDetails.getId()));
        }
        return resultList;
    }

    public int count() {
        return authorizationDetailsDao.count(ImmunogenomicsAuthorization.REALM);
    }

    public void addOrUpdate(ImmunogenomicsAuthorization authorizationBean) {
        String email = authorizationBean.getEmail();
        AuthorizationDetails details = authorizationDetailsDao.getAuthorization(email, ImmunogenomicsAuthorization.REALM);
        if (details == null) {
            details = authorizationBean.getDetails();
            details.setRealm(ImmunogenomicsAuthorization.REALM);
        } else {
            // update persisted information
            details.setScopes(authorizationBean.getDetails().getScopes());
            details.setDuration(authorizationBean.getDuration());
        }
        authorizationDetailsDao.addOrUpdate(details);
    }

    public void delete(String itemId) {
        Identity identity = byId(itemId);
        if (identity == null || identity.email == null) {
            // nothing to delete
            return;
        }
        AuthorizationDetails details = authorizationDetailsDao.getAuthorization(identity.email, ImmunogenomicsAuthorization.REALM);
        if (details == null) {
            // nothing to delete
        }
        // Inactivate record
        details.setScopes(new String[0]);
        details.setDuration(-1);
        authorizationDetailsDao.addOrUpdate(details);
    }

    public ImmunogenomicsAuthorization findOrCreate(String openid, String email) {
        Identity identity = byEmail(email);
        if (identity.openid == null) {
            identity.openid = openid;
        }
        return loadOrCreate(identity);
    }

    public ImmunogenomicsAuthorization findByOpenid(String openid) {
        Identity identity = byOpenid(openid);
        if (identity == null || identity.email == null) return null;
        return loadOrCreate(identity);
    }
    

}
