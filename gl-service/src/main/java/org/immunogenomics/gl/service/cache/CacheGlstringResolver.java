/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.cache;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.service.GlstringResolver;

import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Cache based glstring resolver.
 */
@Immutable
final class CacheGlstringResolver implements GlstringResolver {
    private final LoadingCache<String, String> locusIds;
    private final LoadingCache<String, String> alleleIds;
    private final LoadingCache<String, String> alleleListIds;
    private final LoadingCache<String, String> haplotypeIds;
    private final LoadingCache<String, String> genotypeIds;
    private final LoadingCache<String, String> genotypeListIds;
    private final LoadingCache<String, String> multilocusUnphasedGenotypeIds;

    @Inject
    CacheGlstringResolver(@Named("locusIds") final LoadingCache<String, String> locusIds,
                          @Named("alleleIds") final LoadingCache<String, String> alleleIds,
                          @Named("alleleListIds") final LoadingCache<String, String> alleleListIds,
                          @Named("haplotypeIds") final LoadingCache<String, String> haplotypeIds,
                          @Named("genotypeIds") final LoadingCache<String, String> genotypeIds,
                          @Named("genotypeListIds") final LoadingCache<String, String> genotypeListIds,
                          @Named("multilocusUnphasedGenotypeIds") final LoadingCache<String, String> multilocusUnphasedGenotypeIds) {

        this.locusIds = locusIds;
        this.alleleIds = alleleIds;
        this.alleleListIds = alleleListIds;
        this.haplotypeIds = haplotypeIds;
        this.genotypeIds = genotypeIds;
        this.genotypeListIds = genotypeListIds;
        this.multilocusUnphasedGenotypeIds = multilocusUnphasedGenotypeIds;
    }


    @Override
    public String resolveLocus(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return locusIds.getUnchecked(glstring);
    }

    @Override
    public String resolveAllele(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return alleleIds.getUnchecked(glstring);
    }

    @Override
    public String resolveAlleleList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return alleleListIds.getUnchecked(glstring);
    }

    @Override
    public String resolveHaplotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return haplotypeIds.getUnchecked(glstring);
    }

    @Override
    public String resolveGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return genotypeIds.getUnchecked(glstring);
    }

    @Override
    public String resolveGenotypeList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return genotypeListIds.getUnchecked(glstring);
    }

    @Override
    public String resolveMultilocusUnphasedGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        return multilocusUnphasedGenotypeIds.getUnchecked(glstring);
    }
}