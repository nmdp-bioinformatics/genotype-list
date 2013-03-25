/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client.http;

/**
 * RuntimeException used to report HTTP failure.
 *
 * @author mgeorge
 */
public final class GlClientHttpException extends RuntimeException {
    private final int statusCode;

    /**
     * Create a new gl client HTTP exception with the specified HTTP status code.
     *
     * @param statusCode HTTP status code
     */
    public GlClientHttpException(final int statusCode) {
        this.statusCode = statusCode;
    }

    /**
     * Create a new gl client HTTP exception with the specified HTTP status code and message.
     *
     * @param statusCode HTTP status code
     * @param message message
     */
    public GlClientHttpException(final int statusCode, final String message) {
        super(message);
        this.statusCode = statusCode;
    }

    /**
     * Create a new gl client HTTP exception with the specified HTTP status code and cause.
     *
     * @param statusCode HTTP status code
     * @param cause cause
     */
    public GlClientHttpException(final int statusCode, final Throwable cause) {
        super(cause);
        this.statusCode = statusCode;
    }

    /**
     * Create a new gl client HTTP exception with the specified HTTP status code, message, and cause.
     *
     * @param statusCode HTTP status code
     * @param message message
     * @param cause cause
     */
    public GlClientHttpException(final int statusCode, final String message, final Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    /**
     * Return the HTTP status code for this gl client HTTP exception.
     *
     * @return the HTTP status code for this gl client HTTP exception
     */
    public int getStatusCode() {
        return statusCode;
    }
}
