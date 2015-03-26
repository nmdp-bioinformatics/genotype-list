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

/**
 * Exceptions that may occur attempting to access a resource.
 */
public final class AuthorizationException extends Exception {

    private static final long serialVersionUID = 1L;

    private final OAuthErrorCode errorCode;

    /**
     * 
     * @param errorCode
     *            value from ReponseErrorCode
     * @param errorDescription
     */
    public AuthorizationException(OAuthErrorCode errorCode, String errorDescription) {
        super(errorDescription);
        this.errorCode = errorCode;
    }

    /**
     * 
     * @param errorCode
     * @param e
     */
    public AuthorizationException(OAuthErrorCode errorCode, Exception e) {
        super(e);
        this.errorCode = errorCode;
    }

    public AuthorizationException(OAuthErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public OAuthErrorCode getError() {
        return errorCode;
    }

    @Override
    public String toString() {
        String errorDescription = getErrorDescription();
        if (errorDescription == null || errorDescription.isEmpty()) {
            return errorCode.toString();
        } else {
            return errorCode + ":" + errorDescription;
        }
    }

    public String getErrorDescription() {
        return getMessage();
    }
}
