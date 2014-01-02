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

import java.util.LinkedList;
import java.util.List;

public final class RequestScope {

    private String realm;
    private List<String> scopeList = new LinkedList<String>();
    private String method;

    public RequestScope(String method, String realm, String scope) {
        super();
        this.method = method;
        this.realm = realm;
        addScope(scope);
    }

    public String getMethod() {
        return method;
    }

    public String getRealm() {
        return realm;
    }

    public List<String> getScopeList() {
        return scopeList;
    }

    public void addScope(String scope) {
        if (scope == null) return;
        for (String s : scope.split(" ")) {
            if (!scopeList.contains(scope)) {
                scopeList.add(s);
            }
        }
    }

    public boolean hasRealm(String realm2) {
        return (realm != null) && realm.equals(realm2);
    }

    @Override
    public String toString() {
        return "RequestScope [realm=" + realm + ", scope=" + getScope() + ", method=" + method + "]";
    }

    /**
     * @return a blank delimited String of scopes. 
     */
    public String getScope() {
        StringBuilder sb = new StringBuilder();
        for (String scope : scopeList) {
            sb.append(sb.length() == 0 ? "" : " ").append(scope);
        }
        return sb.toString();
    }
    
}
