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

import java.io.InputStream;

/**
 * Interface to decouple GlClient from HTTP communication.
 */
public interface HttpClient {

    /**
     * Return the content body as an InputStream.
     *
     * @param url location to get
     * @return InputStream for content body which should be closed
     */
    InputStream get(String url);

    /**
     * Return the "Location" header as a string.
     *
     * @param url location to get
     * @param body "text/plain" content sent to the server
     * @return the "Location" header
     * @throws HttpClientException when status code is not OK
     */
    String post(String url, String body) throws HttpClientException;
}
