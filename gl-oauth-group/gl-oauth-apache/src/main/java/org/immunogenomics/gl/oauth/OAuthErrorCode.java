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
package org.immunogenomics.gl.oauth;

/**
 * When an OAuth request fails, the resource server responds using the
 * appropriate HTTP status code (typically, 400, 401, 403, or 405) and includes
 * one of the following error codes in the response: INVALID_REQUEST,
 * INVALID_TOKEN, or INSUFFICIENT_SCOPE
 * <p />
 * If the request lacks any authentication information (e.g., the client was
 * unaware that authentication is necessary or attempted using an unsupported
 * authentication method), the resource server SHOULD NOT include an error code
 * or other error information.
 */
public enum OAuthErrorCode {

    /**
     * The request requires higher privileges than provided by the access token.
     * The resource server SHOULD respond with the HTTP 403 (Forbidden) status
     * code and MAY include the "scope" attribute with the scope necessary to
     * access the protected resource.
     */
    INSUFFICIENT_SCOPE("insufficient_scope", 403, "Forbidden"),
    /**
     * The access token provided is expired, revoked, malformed, or invalid for
     * other reasons. The resource SHOULD respond with the HTTP 401
     * (Unauthorized) status code. The client MAY request a new access token and
     * retry the protected resource request
     */
    INVALID_TOKEN("invalid_token", 401, "Unauthorized"),
    /**
     * The request is missing a required parameter, includes an unsupported
     * parameter or parameter value, repeats the same parameter, uses more than
     * one method for including an access token, or is otherwise malformed. The
     * resource server SHOULD respond with the HTTP 400 (Bad Request) status
     * code.
     */
    INVALID_REQUEST("invalid_request", 400, "Bad Request");

    private final String error;
    private final int statusCode;
    private final String statusMessage;

    OAuthErrorCode(String error, int statusCode, String statusMessage) {
        this.error = error;
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    /** Return the error to use in OAuth error responses. */
    public String getError() {
        return error;
    }

    /** Return the HTTP status code to use in OAuth error responses. */
    public int getStatusCode() {
        return statusCode;
    }

    /** Return the HTTP status message to use in OAuth error responses. */
    public String getStatusMessage() {
        return statusMessage;
    }
}
