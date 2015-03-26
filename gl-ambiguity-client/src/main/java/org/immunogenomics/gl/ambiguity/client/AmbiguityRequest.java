/*

    gl-ambiguity-client  Client library for RESTful genotype list ambiguity service.
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
package org.immunogenomics.gl.ambiguity.client;

import javax.annotation.concurrent.Immutable;

/**
 * Ambiguity request.
 */
@Immutable
public final class AmbiguityRequest {
    private final String name;
    private final String uri;
    private final String glstring;

    public AmbiguityRequest(final String name, final String uri, final String glstring) {
        this.name = name;
        this.uri = uri;
        this.glstring = glstring;
    }

    public String getName() {
        return name;
    }

    public String getUri() {
        return uri;
    }

    public String getGlstring() {
        return glstring;
    }

    public static AmbiguityRequest name(final String name) {
        return new AmbiguityRequest(name, null, null);
    }

    public static AmbiguityRequest uri(final String uri) {
        return new AmbiguityRequest(null, uri, null);
    }

    public static AmbiguityRequest uri(final String name, final String uri) {
        return new AmbiguityRequest(name, uri, null);
    }

    public static AmbiguityRequest glstring(final String glstring) {
        return new AmbiguityRequest(null, null, glstring);
    }

    public static AmbiguityRequest glstring(final String name, final String glstring) {
        return new AmbiguityRequest(name, null, glstring);
    }
}
