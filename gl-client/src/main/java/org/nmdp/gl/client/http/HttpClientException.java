/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.nmdp.gl.client.http;

/**
 * Checked exception thrown in the event of a HTTP client error.
 */
public final class HttpClientException extends RuntimeException {
    private final int statusCode;

    /**
     * Create a new HTTP client exception with the specified HTTP status code and message.
     *
     * @param statusCode HTTP status code
     * @param message message
     */
    public HttpClientException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Return the HTTP status code for this HTTP client exception.
     *
     * @return the HTTP status code for this HTTP client exception
     */
    public int getStatusCode() {
        return statusCode;
    }
}
