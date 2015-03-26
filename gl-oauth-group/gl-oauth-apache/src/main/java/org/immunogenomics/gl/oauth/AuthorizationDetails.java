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

import java.util.Set;
import java.util.TreeSet;

/**
 * Authorization properties.
 */
public class AuthorizationDetails implements Comparable<AuthorizationDetails>{
    
    public static final String ADMIN_SCOPE = "admin";
    public static final String[] ADMIN_SCOPES = {"admin"};
    
    private static final String SCOPE = "scope";
    private static final String REALM = "realm";
    private static final String ID = "id";
    private static final String DURATION = "duration";
    private String id; // user or system id for tracking purposes.
    private String realm;
    private Set<String> scopes = new TreeSet<String>();
    private int duration;

    public AuthorizationDetails() {}

    public AuthorizationDetails(String content) {
        String[] pairs = content.split(",");
        for (String pair : pairs) {
            int equalPos = pair.indexOf('=');
            if (equalPos > 0) {
                String name = pair.substring(0, equalPos).trim();
                String value = pair.substring(equalPos + 1).trim();
                setByName(name, value);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        checkArg(id);
        this.id = id;
    }

    private void checkArg(String arg) {
        if (arg.indexOf(',') > 0) {
            throw new IllegalArgumentException(arg + " is invalid");
        }

    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        checkArg(realm);
        this.realm = realm;
    }

    public String[] getScopes() {
        return scopes.toArray(new String[scopes.size()]);
    }

    public void setScopes(String[] scope) {
        this.scopes.clear();
        for (String name : scope) {
            addScope(name);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, ID, id);
        append(sb, DURATION, Long.toString(getDuration()));
        append(sb, REALM, realm);
        appendScopes(sb);
        return sb.toString();
    }
    
    private void appendScopes(StringBuilder sb) {
        append(sb, SCOPE, "");
        for (String scope : scopes) {
            sb.append(scope).append(" ");
        }
    }

    private void append(StringBuilder sb, String name, String value) {
        if (value == null)
            return;
        if (sb.length() > 0)
            sb.append(',');
        sb.append(name).append('=').append(value);
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setByName(String name, String value) {
        if (name.equals(ID)) {
            this.id = value;
        } else if (name.equals(DURATION)) {
            setDuration(Integer.parseInt(value));
        } else if (name.equals(REALM)) {
            setRealm(value);
        } else if (name.equals(SCOPE)) {
            setScopes(value.split(" "));
        }
    }

    public boolean hasScope(String scope) {
        return scopes.contains(scope);
    }

    @Override
    public int compareTo(AuthorizationDetails o) {
        return this.id.compareTo(o.id);
    }

    public boolean addScope(String scopeName) {
        if (scopeName == null || scopeName.length() == 0) {
            return false;
        }
        checkArg(scopeName);
        return scopes.add(scopeName);
    }
    
    public boolean removeScope(String scopeName) {
        if (scopeName == null || scopeName.length() == 0) {
            return false;
        }
        checkArg(scopeName);
        return scopes.remove(scopeName);
    }
}
