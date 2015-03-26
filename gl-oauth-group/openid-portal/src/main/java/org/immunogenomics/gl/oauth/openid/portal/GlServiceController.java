/*

    openid-portal OpenID Authentication/Authorization server for the GL Service
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
package org.immunogenomics.gl.oauth.openid.portal;

import java.security.Principal;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.immunogenomics.gl.oauth.AccessTokenDetails;
import org.immunogenomics.gl.oauth.AuthorizationManager;
import org.immunogenomics.gl.oauth.openid.portal.admin.ImmunogenomicsAuthorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RequestMapping("/glservice/**")
@Controller
public class GlServiceController {

    @Autowired
    private AuthorizationManager authorizationManager;

    @RequestMapping
    public String index(ModelMap modelMap, HttpServletRequest request) {
        String userName = "not logged in";
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            userName = userPrincipal.getName();
        }
        modelMap.addAttribute("username", userName);
        String token = authorizationManager.getAuthorization(userName, ImmunogenomicsAuthorization.REALM);
        modelMap.addAttribute("accessToken", token);
        AccessTokenDetails tokenDetails = authorizationManager.validate(token);
        if (tokenDetails == null) {
            tokenDetails = new AccessTokenDetails();
        }
        modelMap.addAttribute("tokenDetails", tokenDetails);
        modelMap.addAttribute("scope", Arrays.toString(tokenDetails.getScopes()));
        return "glservice/index";
    }
    
    @RequestMapping(value="/validate", method = RequestMethod.POST, produces = "text/plain")
    @ResponseBody
    public String validate(@RequestParam String token) {
        AccessTokenDetails tokenDetails = authorizationManager.validate(token);
        if (tokenDetails == null) {
            return "INVALID_TOKEN";
        }
        return tokenDetails.toString();
    }

}
