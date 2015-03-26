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

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;
import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Cache based genotype list registry.
 */
@Immutable
final class CacheGlRegistry implements GlRegistry {
    // id --> <T>
    private final Cache<String, Locus> loci;
    private final Cache<String, Allele> alleles;
    private final Cache<String, AlleleList> alleleLists;
    private final Cache<String, Haplotype> haplotypes;
    private final Cache<String, Genotype> genotypes;
    private final Cache<String, GenotypeList> genotypeLists;
    private final Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes;
    // glstring --> id
    private final LoadingCache<String, String> locusIds;
    private final LoadingCache<String, String> alleleIds;
    private final LoadingCache<String, String> alleleListIds;
    private final LoadingCache<String, String> haplotypeIds;
    private final LoadingCache<String, String> genotypeIds;
    private final LoadingCache<String, String> genotypeListIds;
    private final LoadingCache<String, String> multilocusUnphasedGenotypeIds;

    @Inject
    CacheGlRegistry(final Cache<String, Locus> loci,
                    final Cache<String, Allele> alleles,
                    final Cache<String, AlleleList> alleleLists,
                    final Cache<String, Haplotype> haplotypes,
                    final Cache<String, Genotype> genotypes,
                    final Cache<String, GenotypeList> genotypeLists,
                    final Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes,
                    @Named("locusIds") final LoadingCache<String, String> locusIds,
                    @Named("alleleIds") final LoadingCache<String, String> alleleIds,
                    @Named("alleleListIds") final LoadingCache<String, String> alleleListIds,
                    @Named("haplotypeIds") final LoadingCache<String, String> haplotypeIds,
                    @Named("genotypeIds") final LoadingCache<String, String> genotypeIds,
                    @Named("genotypeListIds") final LoadingCache<String, String> genotypeListIds,
                    @Named("multilocusUnphasedGenotypeIds") final LoadingCache<String, String> multilocusUnphasedGenotypeIds) {

        this.loci = loci;
        this.alleles = alleles;
        this.alleleLists = alleleLists;
        this.haplotypes = haplotypes;
        this.genotypes = genotypes;
        this.genotypeLists = genotypeLists;
        this.multilocusUnphasedGenotypes = multilocusUnphasedGenotypes;
        this.locusIds = locusIds;
        this.alleleIds = alleleIds;
        this.alleleListIds = alleleListIds;
        this.haplotypeIds = haplotypeIds;
        this.genotypeIds = genotypeIds;
        this.genotypeListIds = genotypeListIds;
        this.multilocusUnphasedGenotypeIds = multilocusUnphasedGenotypeIds;
    }


    @Override
    public void registerLocus(final Locus locus) {
        loci.put(locus.getId(), locus);
        locusIds.put(locus.getGlstring(), locus.getId());
    }

    @Override
    public void registerAllele(final Allele allele) {
        alleles.put(allele.getId(), allele);
        alleleIds.put(allele.getGlstring(), allele.getId());
    }

    @Override
    public void registerAlleleList(final AlleleList alleleList) {
        alleleLists.put(alleleList.getId(), alleleList);
        alleleListIds.put(alleleList.getGlstring(), alleleList.getId());
    }

    @Override
    public void registerHaplotype(final Haplotype haplotype) {
        haplotypes.put(haplotype.getId(), haplotype);
        haplotypeIds.put(haplotype.getGlstring(), haplotype.getId());
    }

    @Override
    public void registerGenotype(final Genotype genotype) {
        genotypes.put(genotype.getId(), genotype);
        genotypeIds.put(genotype.getGlstring(), genotype.getId());
    }

    @Override
    public void registerGenotypeList(final GenotypeList genotypeList) {
        genotypeLists.put(genotypeList.getId(), genotypeList);
        genotypeListIds.put(genotypeList.getGlstring(), genotypeList.getId());
    }

    @Override
    public void registerMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        multilocusUnphasedGenotypes.put(multilocusUnphasedGenotype.getId(), multilocusUnphasedGenotype);
        multilocusUnphasedGenotypeIds.put(multilocusUnphasedGenotype.getGlstring(), multilocusUnphasedGenotype.getId());
    }
}