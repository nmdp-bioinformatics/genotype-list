/*

    gl-service-voldemort  Implementation of persistent cache for gl-service using Voldemort.
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
package org.immunogenomics.gl.service.voldemort;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;

import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

import com.google.inject.Inject;

/**
 * Voldemort based glstring resolver.
 */
@Immutable
final class VoldemortGlstringResolver implements GlstringResolver {
    private final IdSupplier idSupplier;
    private final StoreClientFactory storeClientFactory;

    @Inject
    VoldemortGlstringResolver(final IdSupplier idSupplier, final StoreClientFactory storeClientFactory) {
        checkNotNull(idSupplier);
        checkNotNull(storeClientFactory);
        this.idSupplier = idSupplier;
        this.storeClientFactory = storeClientFactory;
    }


    @Override
    public String resolveLocus(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("locusId");
        String locusId = client.getValue(glstring);
        if (locusId != null) {
            return locusId;
        }
        return idSupplier.createLocusId();
    }

    @Override
    public String resolveAllele(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("alleleId");
        String alleleId = client.getValue(glstring);
        if (alleleId != null) {
            return alleleId;
        }
        return idSupplier.createAlleleId();
    }

    @Override
    public String resolveAlleleList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("alleleListId");
        String alleleListId = client.getValue(glstring);
        if (alleleListId != null) {
            return alleleListId;
        }
        return idSupplier.createAlleleListId();
    }

    @Override
    public String resolveHaplotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("haplotypeId");
        String haplotypeId = client.getValue(glstring);
        if (haplotypeId != null) {
            return haplotypeId;
        }
        return idSupplier.createHaplotypeId();
    }

    @Override
    public String resolveGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("genotypeId");
        String genotypeId = client.getValue(glstring);
        if (genotypeId != null) {
            return genotypeId;
        }
        return idSupplier.createGenotypeId();
    }

    @Override
    public String resolveGenotypeList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("genotypeListId");
        String genotypeListId = client.getValue(glstring);
        if (genotypeListId != null) {
            return genotypeListId;
        }
        return idSupplier.createGenotypeListId();
    }

    @Override
    public String resolveMultilocusUnphasedGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        StoreClient<String, String> client = storeClientFactory.getStoreClient("multilocusUnphasedGenotypeId");
        String multilocusUnphasedGenotypeId = client.getValue(glstring);
        if (multilocusUnphasedGenotypeId != null) {
            return multilocusUnphasedGenotypeId;
        }
        return idSupplier.createMultilocusUnphasedGenotypeId();
    }
}