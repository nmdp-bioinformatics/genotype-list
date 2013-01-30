/*

    gl-oauth  OAuth related projects and samples.
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
package org.immunogenomics.gl.oauth.toy.portal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.immunogenomics.gl.oauth.AbstractOAuthServlet;
import org.immunogenomics.gl.oauth.AuthorizationManager;
import org.immunogenomics.gl.oauth.TokenValidator;

/**
 * Validates tokens using a static algorithm.
 * during testing and development.
 */
public class ToyOAuthServlet extends AbstractOAuthServlet {

    private static final long serialVersionUID = 1L;
    private TokenValidator tokenValidator;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        tokenValidator = new ToyTokenValidator();
    }

    @Override
    protected String validateToken(String token) {
        return tokenValidator.validate(token).toString();
    }

    @Override
    public AuthorizationManager getAuthorizationManager() {
        throw new UnsupportedOperationException("getAuthorizationManager not supported by " + getClass());
    }

}
