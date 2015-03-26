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
package org.immunogenomics.gl.oauth.toy.portal;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.immunogenomics.gl.oauth.AbstractOAuthServlet;
import org.immunogenomics.gl.oauth.AuthorizationManager;
import org.immunogenomics.gl.oauth.FileOAuthProvider;
import org.immunogenomics.gl.oauth.JmxDemoAuth;
import org.immunogenomics.gl.oauth.JmxTokenValidator;
import org.immunogenomics.gl.oauth.MemoryTokenStore;

/**
 * OAuthServlet backed by a static FileOAuthProvider.
 * 
 */
public class FileOAuthServlet extends AbstractOAuthServlet {

    private static final long serialVersionUID = 1L;
    private static AuthorizationManager authorizationManager;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (authorizationManager == null) {
            FileOAuthProvider fileOAuthProvider = new FileOAuthProvider("/userid-scopes.txt");
            authorizationManager = new AuthorizationManager(fileOAuthProvider, new MemoryTokenStore());
            JmxTokenValidator.registerJmxTokenValidator(authorizationManager);
            JmxDemoAuth.registerJmxBean(authorizationManager);
        }
    }

    @Override
    public AuthorizationManager getAuthorizationManager() {
        return authorizationManager;
    }

}
