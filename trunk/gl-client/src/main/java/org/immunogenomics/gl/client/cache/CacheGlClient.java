/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client.cache;

import static com.google.common.cache.CacheBuilder.newBuilder;

import com.google.common.cache.Cache;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.client.AbstractGlClient;

/**
 * Abstract gl client that provides caching of loci and alleles.
 */
public abstract class CacheGlClient extends AbstractGlClient {
    // todo:  inject these
    private Cache<String, Locus> loci;
    private Cache<String, String> locusIds;
    private Cache<String, Allele> alleles;
    private Cache<String, String> alleleIds;


    //@Inject
    protected CacheGlClient() {
        loci = newBuilder().initialCapacity(1000).build();
        locusIds = newBuilder().initialCapacity(1000).build();
        alleles = newBuilder().initialCapacity(10000).build();
        alleleIds = newBuilder().initialCapacity(10000).build();
    }


    protected final void putLocus(final String identifier, final Locus locus) {
        loci.put(identifier, locus);
    }

    protected final void putLocusId(final String glstring, final String identifier) {
        locusIds.put(glstring, identifier);
    }

    protected final Locus getLocusIfPresent(final String identifier) {
        return loci.getIfPresent(identifier);
    }

    protected final String getLocusIdIfPresent(final String glstring) {
        return locusIds.getIfPresent(glstring);
    }

    protected final void putAllele(final String identifier, final Allele allele) {
        alleles.put(identifier, allele);
    }

    protected final void putAlleleId(final String glstring, final String identifier) {
        alleleIds.put(glstring, identifier);
    }

    protected final Allele getAlleleIfPresent(final String identifier) {
        return alleles.getIfPresent(identifier);
    }

    protected final String getAlleleIdIfPresent(final String glstring) {
        return alleleIds.getIfPresent(glstring);
    }
}