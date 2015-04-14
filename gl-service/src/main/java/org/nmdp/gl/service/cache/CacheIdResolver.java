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
package org.nmdp.gl.service.cache;

import javax.annotation.concurrent.Immutable;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;
import org.nmdp.gl.service.IdResolver;

import com.google.common.cache.Cache;
import com.google.inject.Inject;

/**
 * Cache based identifier resolver.
 */
@Immutable
final class CacheIdResolver implements IdResolver {
    private final Cache<String, Locus> loci;
    private final Cache<String, Allele> alleles;
    private final Cache<String, AlleleList> alleleLists;
    private final Cache<String, Haplotype> haplotypes;
    private final Cache<String, Genotype> genotypes;
    private final Cache<String, GenotypeList> genotypeLists;
    private final Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes;

    @Inject
    CacheIdResolver(final Cache<String, Locus> loci,
                    final Cache<String, Allele> alleles,
                    final Cache<String, AlleleList> alleleLists,
                    final Cache<String, Haplotype> haplotypes,
                    final Cache<String, Genotype> genotypes,
                    final Cache<String, GenotypeList> genotypeLists,
                    final Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes) {

        this.loci = loci;
        this.alleles = alleles;
        this.alleleLists = alleleLists;
        this.haplotypes = haplotypes;
        this.genotypes = genotypes;
        this.genotypeLists = genotypeLists;
        this.multilocusUnphasedGenotypes = multilocusUnphasedGenotypes;
    }


    @Override
    public Locus findLocus(final String id) {
        return loci.getIfPresent(id);
    }

    @Override
    public Allele findAllele(final String id) {
        return alleles.getIfPresent(id);
    }

    @Override
    public AlleleList findAlleleList(final String id) {
        return alleleLists.getIfPresent(id);
    }

    @Override
    public Haplotype findHaplotype(final String id) {
        return haplotypes.getIfPresent(id);
    }

    @Override
    public Genotype findGenotype(final String id) {
        return genotypes.getIfPresent(id);
    }

    @Override
    public GenotypeList findGenotypeList(final String id) {
        return genotypeLists.getIfPresent(id);
    }

    @Override
    public MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(final String id) {
        return multilocusUnphasedGenotypes.getIfPresent(id);
    }
}