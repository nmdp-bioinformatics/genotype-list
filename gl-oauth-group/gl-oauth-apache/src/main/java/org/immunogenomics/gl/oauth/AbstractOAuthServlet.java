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

import java.io.IOException;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Responsible for processing HTTP messages and
 * forwarding on to the 
 * AuthorizationManager.
 */
public abstract class AbstractOAuthServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;


	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String content;
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			content = "No tokens are currently authorized.\n";
		} else {
			content = "no operations configured for pathInfo = " + pathInfo + "\n";
		}
		sendPlainText(resp, content);
	}

	protected void sendPlainText(HttpServletResponse resp, String content)
			throws IOException {
		byte[] bytes = content.getBytes(Charset.forName("UTF-8"));
		resp.setContentType("text/plain; charset=UTF-8");
		resp.setContentLength(bytes.length);
		ServletOutputStream outputStream = resp.getOutputStream();
		outputStream.write(bytes);
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String content;
		String pathInfo = req.getPathInfo();
		if (pathInfo == null) {
			content = "No tokens are currently authorized";
		} else if (pathInfo.startsWith("/validate")) {
			String token = TokenValidateUtil.extractToken(req);
			content = validateToken(token);
		} else if (pathInfo.startsWith("/get_token")) {
			String realm = req.getParameter("realm");
			String userid = req.getParameter("userid");
			content = getToken(realm, userid);
		} else {
			content = "no operations configured for pathInfo=" + pathInfo;
		}
		sendPlainText(resp, content);
	}

	/**
	 * Process a get_token request.
	 * @param realm
	 * @param userid
	 * @return token for the user ID.
	 */
	protected String getToken(String realm, String userid) {
		return getAuthorizationManager().getAuthorization(userid, realm);
	}

	/**
	 * Process a token validate request.
	 * @param req
	 * @return String representation of AuthorizationDetails
	 */
	protected String validateToken(String token) {
		AccessTokenDetails response = getAuthorizationManager().validate(token);
		if (response == null) {
			return "INVALID_TOKEN";
		} else {
			return response.toString();
		}
	}

	public abstract AuthorizationManager getAuthorizationManager();

}
