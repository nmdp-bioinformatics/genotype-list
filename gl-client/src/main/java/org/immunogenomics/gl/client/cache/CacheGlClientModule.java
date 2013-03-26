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
package org.immunogenomics.gl.client.cache;

import org.immunogenomics.gl.Locus;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * CacheGlClient module.
 */
public final class CacheGlClientModule extends AbstractModule {

    @Override
    protected void configure() {
        // empty
    }

    @Provides @GlClientLocusCache
    protected Cache<String, Locus> createLocusCache() {
        return CacheBuilder.newBuilder().initialCapacity(1000).build();
    }

    @Provides @GlClientLocusIdCache
    protected Cache<String, Locus> createLocusIdCache() {
        return CacheBuilder.newBuilder().initialCapacity(1000).build();
    }

    @Provides @GlClientAlleleCache
    protected Cache<String, Locus> createAlleleCache() {
        return CacheBuilder.newBuilder().initialCapacity(10000).build();
    }

    @Provides @GlClientAlleleIdCache
    protected Cache<String, Locus> createAlleleIdCache() {
        return CacheBuilder.newBuilder().initialCapacity(10000).build();
    }
}
