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

/**
 * Currently, this result is obtained from an HTTP POST to the
 * AuthorizationServer with the bearer token as the "token" query parameter.
 * 
 */
public class AuthorizationDetails {
    private static final String SCOPE = "scope";
    private static final String REALM = "realm";
    private static final String ID = "id";
    private static final String DURATION = "duration";
    private String id; // user or sytem id for tracking purposes.
    private String realm;
    private String[] scopes;
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
        return scopes;
    }

    public void setScopes(String[] scope) {
        for (String name : scope) {
            checkArg(name);
        }
        this.scopes = scope;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        append(sb, ID, id);
        append(sb, DURATION, Long.toString(getDuration()));
        append(sb, REALM, realm);
        append(sb, SCOPE, scopes);
        return sb.toString();
    }

    private void append(StringBuilder sb, String name, String[] array) {
        if (array == null || array.length == 0)
            return;
        if (sb.length() > 0)
            sb.append(',');
        sb.append(name).append('=').append(array[0]);
        for (int i = 1; i < array.length; i++) {
            sb.append(' ').append(array[i]);
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

}
