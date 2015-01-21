/*

    gl-ambiguity-client  Client library for RESTful genotype list ambiguity service.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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

import org.dishevelled.bitset.ImmutableBitSet;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.POST;

/**
 * RESTful genotype list ambiguity service.
 */
public interface AmbiguityService {

    @GET("/bits/{name}")
    ImmutableBitSet bits(String name);

    @POST("/bits")
    ImmutableBitSet bits(@Body AmbiguityRequest request);

    @GET("/ambiguity/{name}")
    AmbiguityResponse ambiguity(@Path("name") String name);

    @POST("/ambiguity")
    AmbiguityResponse ambiguity(@Body AmbiguityRequest request);
}
