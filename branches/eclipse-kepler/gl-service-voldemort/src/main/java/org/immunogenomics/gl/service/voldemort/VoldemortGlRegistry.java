/*

    gl-service-voldemort  Implementation of persistent cache for gl-service using Voldemort.
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
import org.immunogenomics.gl.service.GlRegistry;

import com.google.inject.Inject;

import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

/**
 * Voldemort based genotype list registry.
 */
@Immutable
final class VoldemortGlRegistry implements GlRegistry {
    private final StoreClientFactory storeClientFactory;

    @Inject
    VoldemortGlRegistry(final StoreClientFactory storeClientFactory) {
        checkNotNull(storeClientFactory);
        this.storeClientFactory = storeClientFactory;
    }

    @Override
    public void registerLocus(final Locus locus) {
        checkNotNull(locus);
        StoreClient<String, Locus> locusClient = storeClientFactory.getStoreClient("locus");
        locusClient.put(locus.getId(), locus);
        StoreClient<String, String> locusIdClient = storeClientFactory.getStoreClient("locusId");
        locusIdClient.put(locus.getGlstring(), locus.getId());
    }

    @Override
    public void registerAllele(final Allele allele) {
        checkNotNull(allele);
        StoreClient<String, Allele> alleleClient = storeClientFactory.getStoreClient("allele");
        alleleClient.put(allele.getId(), allele);
        StoreClient<String, String> alleleIdClient = storeClientFactory.getStoreClient("alleleId");
        alleleIdClient.put(allele.getGlstring(), allele.getId());
    }

    @Override
    public void registerAlleleList(final AlleleList alleleList) {
        checkNotNull(alleleList);
        StoreClient<String, AlleleList> alleleListClient = storeClientFactory.getStoreClient("alleleList");
        alleleListClient.put(alleleList.getId(), alleleList);
        StoreClient<String, String> alleleListIdClient = storeClientFactory.getStoreClient("alleleListId");
        alleleListIdClient.put(alleleList.getGlstring(), alleleList.getId());
    }

    @Override
    public void registerHaplotype(final Haplotype haplotype) {
        checkNotNull(haplotype);
        StoreClient<String, Haplotype> haplotypeClient = storeClientFactory.getStoreClient("haplotype");
        haplotypeClient.put(haplotype.getId(), haplotype);
        StoreClient<String, String> haplotypeIdClient = storeClientFactory.getStoreClient("haplotypeId");
        haplotypeIdClient.put(haplotype.getGlstring(), haplotype.getId());
    }

    @Override
    public void registerGenotype(final Genotype genotype) {
        checkNotNull(genotype);
        StoreClient<String, Genotype> genotypeClient = storeClientFactory.getStoreClient("genotype");
        genotypeClient.put(genotype.getId(), genotype);
        StoreClient<String, String> genotypeIdClient = storeClientFactory.getStoreClient("genotypeId");
        genotypeIdClient.put(genotype.getGlstring(), genotype.getId());
    }

    @Override
    public void registerGenotypeList(final GenotypeList genotypeList) {
        checkNotNull(genotypeList);
        StoreClient<String, GenotypeList> genotypeListClient = storeClientFactory.getStoreClient("genotypeList");
        genotypeListClient.put(genotypeList.getId(), genotypeList);
        StoreClient<String, String> genotypeListIdClient = storeClientFactory.getStoreClient("genotypeListId");
        genotypeListIdClient.put(genotypeList.getGlstring(), genotypeList.getId());
    }

    @Override
    public void registerMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        checkNotNull(multilocusUnphasedGenotype);
        StoreClient<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypeClient = storeClientFactory.getStoreClient("multilocusUnphasedGenotype");
        multilocusUnphasedGenotypeClient.put(multilocusUnphasedGenotype.getId(), multilocusUnphasedGenotype);
        StoreClient<String, String> multilocusUnphasedGenotypeIdClient = storeClientFactory.getStoreClient("multilocusUnphasedGenotypeId");
        multilocusUnphasedGenotypeIdClient.put(multilocusUnphasedGenotype.getGlstring(), multilocusUnphasedGenotype.getId());
    }
}