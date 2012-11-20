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
package org.immunogenomics.gl.oauth.toy.portal;

import java.util.concurrent.TimeUnit;

import org.immunogenomics.gl.oauth.AccessTokenDetails;
import org.immunogenomics.gl.oauth.TokenValidator;

public class ToyTokenValidator implements TokenValidator {

    public AccessTokenDetails validate(String token) {
        AccessTokenDetails response = new AccessTokenDetails();
        response.setRealm("toy-realm");
        response.setExpiresIn(TimeUnit.HOURS.toSeconds(1));
        if ("Xexpired".equals(token)) {
            response.setId("old-user");
            response.setExpiresIn(-1);
        } else if ("Xvalid".equals(token)) {
            response.setId("valid-user");
            response.setScopes(new String[] { "POST" });
        } else if ("Xadmin".equals(token)) {
            response.setId("admin-user");
            String[] adminScope = { "admin", "high", "POST" };
            response.setScopes(adminScope);
        } else if ("Xhigh".equals(token)) {
            response.setId("high-user");
            String[] highScope = { "high" };
            response.setScopes(highScope);

        } else {
            response.setRealm(null);
            response.setExpiresIn(0);
        }
        return response;
    }

    public void close() {}

}
