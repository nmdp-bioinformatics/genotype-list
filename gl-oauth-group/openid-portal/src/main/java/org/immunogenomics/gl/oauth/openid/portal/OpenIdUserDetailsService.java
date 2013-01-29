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
package org.immunogenomics.gl.oauth.openid.portal;

import java.util.ArrayList;
import java.util.Collection;

import org.immunogenomics.gl.oauth.openid.portal.admin.ImmunogenomicsAuthorization;
import org.immunogenomics.gl.oauth.openid.portal.admin.ImmunogenomicsAuthorizationDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.AuthenticationUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.openid.OpenIDAttribute;
import org.springframework.security.openid.OpenIDAuthenticationToken;

/**
 * Spring UserDetailsService based on ImmunogenomicsAuthorization.
 */
public class OpenIdUserDetailsService implements UserDetailsService, AuthenticationUserDetailsService<OpenIDAuthenticationToken> {

    private static class MyGrantedAuthority implements GrantedAuthority {
        private static final long serialVersionUID = 1L;
        private String scope;
        public MyGrantedAuthority(String scope) {
            this.scope = scope;
        }

        @Override
        public String getAuthority() {
            return scope;
        }
    }
    
    static class MyUserDetails implements UserDetails {
        private static final long serialVersionUID = 1L;

        private ImmunogenomicsAuthorization authorization;

        public MyUserDetails(ImmunogenomicsAuthorization authorization2) {
            this.authorization = authorization2;
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            ArrayList<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
            if (authorization != null) {
                String[] scopes = authorization.getScopes();
                boolean isAdmin = false;
                if (scopes != null) {
                    for (String scope : scopes) {
                        isAdmin |= "admin".equals(scope);
                        list.add(new MyGrantedAuthority(scope));
                    }
                }
                if (isAdmin) {
                    list.add(new MyGrantedAuthority("ROLE_ADMIN"));
                }
                list.add(new MyGrantedAuthority("ROLE_USER"));
            }
            return list;
        }

        @Override
        public String getPassword() {
            return "no password";
        }

        @Override
        public String getUsername() {
            return authorization == null ? "not logged in" : authorization.getUsername();
        }
        
        @Override
        public boolean isAccountNonExpired() {
            return isEnabled();
        }

        @Override
        public boolean isAccountNonLocked() {
            return isEnabled();
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return isEnabled();
        }

        @Override
        public boolean isEnabled() {
            return authorization != null && authorization.getDuration() > 0;
        }
        
        /* properties newUser and name used in JSP */
        public boolean isNewUser() {
            return authorization != null && authorization.isNewUser();
        }
        public String getName() {
            return getUsername();
        }
        
    }
    @Autowired
    private ImmunogenomicsAuthorizationDao authorizationDetailsDao;

    
    public UserDetails loadUserByUsername(String openIdIdentifier) {
        ImmunogenomicsAuthorization authorization = authorizationDetailsDao.findByOpenid(openIdIdentifier);
        return new MyUserDetails(authorization);
    }


    @Override
    public UserDetails loadUserDetails(OpenIDAuthenticationToken token) throws UsernameNotFoundException {
        ImmunogenomicsAuthorization user;
        String id = token.getIdentityUrl();
        String email = null;
        String firstName = null;
        String lastName = null;
        String fullName = null;

        for (OpenIDAttribute attribute : token.getAttributes()) {
            String attribName = attribute.getName();
            String value = attribute.getValues().get(0);
            if (attribName.equals("email")) {
                email = value;
            }
            if (attribute.getName().equals("firstname")) {
                firstName = value;
            }
            if (attribute.getName().equals("lastname")) {
                lastName = value;
            }
            if (attribute.getName().equals("fullname")) {
                fullName = value;
            }
        }
        if (email == null) {
            user = authorizationDetailsDao.findByOpenid(id);
        } else {
            user = authorizationDetailsDao.findOrCreate(id, email);
        }
        SecurityLogger.recordLogin(email, user);

        return new MyUserDetails(user);
    }
}
