/*

    openid-portal OpenID Authentication/Authorization server for the GL Service
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
package org.immunogenomics.gl.oauth.openid.portal;

import org.immunogenomics.gl.oauth.AccessTokenDetails;
import org.immunogenomics.gl.oauth.AuthorizationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/oauth/**")
@Controller
public class OauthController {

    @Autowired
    private AuthorizationManager authorizationManager;

    @RequestMapping(value="/validate", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String validate(@RequestParam String token) {
        AccessTokenDetails tokenDetails = authorizationManager.validate(token);
        if (tokenDetails == null) {
            SecurityLogger.recordInvalidToken(token);
            return "INVALID_TOKEN";
        } else {
            String result = tokenDetails.toString();
            SecurityLogger.recordValidated(result);
            return result;
        }
    }

}
