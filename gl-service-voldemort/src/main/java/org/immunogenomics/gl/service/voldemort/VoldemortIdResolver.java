/*

    gl-service-voldemort  Implementation of persistent cache for gl-service using Voldemort.
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
package org.immunogenomics.gl.service.voldemort;

import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.IdResolver;

import com.google.inject.Inject;

import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

/**
 * Voldemort based identifier resolver.
 */
@Immutable
final class VoldemortIdResolver implements IdResolver {
    private final StoreClientFactory storeClientFactory;

    @Inject
    VoldemortIdResolver(final StoreClientFactory storeClientFactory) {
        checkNotNull(storeClientFactory);
        this.storeClientFactory = storeClientFactory;
    }

    @Override
    public Locus findLocus(final String id) {
        StoreClient<String, Locus> client = storeClientFactory.getStoreClient("locus");
        return client.getValue(id);
    }

    @Override
    public Allele findAllele(final String id) {
        StoreClient<String, Allele> client = storeClientFactory.getStoreClient("allele");
        return client.getValue(id);
    }

    @Override
    public AlleleList findAlleleList(final String id) {
        StoreClient<String, AlleleList> client = storeClientFactory.getStoreClient("alleleList");
        return client.getValue(id);
    }

    @Override
    public Haplotype findHaplotype(final String id) {
        StoreClient<String, Haplotype> client = storeClientFactory.getStoreClient("haplotype");
        return client.getValue(id);
    }

    @Override
    public Genotype findGenotype(final String id) {
        StoreClient<String, Genotype> client = storeClientFactory.getStoreClient("genotype");
        return client.getValue(id);
    }

    @Override
    public GenotypeList findGenotypeList(final String id) {
        StoreClient<String, GenotypeList> client = storeClientFactory.getStoreClient("genotypeList");
        return client.getValue(id);
    }

    @Override
    public MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(final String id) {
        StoreClient<String, MultilocusUnphasedGenotype> client = storeClientFactory.getStoreClient("multilocusUnphasedGenotype");
        return client.getValue(id);
    }
}