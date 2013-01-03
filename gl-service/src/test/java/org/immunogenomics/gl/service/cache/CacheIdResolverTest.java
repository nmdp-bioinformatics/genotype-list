/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.cache;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.AbstractIdResolverTest;
import org.immunogenomics.gl.service.IdResolver;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;

/**
 * Unit test for CacheIdResolver.
 */
public final class CacheIdResolverTest extends AbstractIdResolverTest {

    @Override
    protected IdResolver createIdResolver() {
        Cache<String, Locus> loci = CacheBuilder.newBuilder().build();
        Cache<String, Allele> alleles = CacheBuilder.newBuilder().build();
        Cache<String, AlleleList> alleleLists = CacheBuilder.newBuilder().build();
        Cache<String, Haplotype> haplotypes = CacheBuilder.newBuilder().build();
        Cache<String, Genotype> genotypes = CacheBuilder.newBuilder().build();
        Cache<String, GenotypeList> genotypeLists = CacheBuilder.newBuilder().build();
        Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes = CacheBuilder.newBuilder().build();

        Locus locus = new Locus(validLocusId, "HLA-A");
        Allele allele = new Allele(validAlleleId, "A01234", "HLA-A*01:01:01:01", locus);
        AlleleList alleleList0 = new AlleleList(validAlleleListId, allele);
        AlleleList alleleList1 = new AlleleList(validAlleleListId, allele);
        Haplotype haplotype0 = new Haplotype(validHaplotypeId, alleleList0);
        Haplotype haplotype = new Haplotype(validHaplotypeId, ImmutableList.of(alleleList0, alleleList1));
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", haplotype0);
        GenotypeList genotypeList0 = new GenotypeList(validGenotypeListId, genotype);
        GenotypeList genotypeList1 = new GenotypeList(validGenotypeListId, genotype);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId, ImmutableList.of(genotypeList0, genotypeList1));

        loci.asMap().put(validLocusId, locus);
        alleles.asMap().put(validAlleleId, allele);
        alleleLists.asMap().put(validAlleleListId, alleleList0);
        haplotypes.asMap().put(validHaplotypeId, haplotype);
        genotypes.asMap().put(validGenotypeId, genotype);
        genotypeLists.asMap().put(validGenotypeListId, genotypeList0);
        multilocusUnphasedGenotypes.asMap().put(validMultilocusUnphasedGenotypeId, multilocusUnphasedGenotype);

        return new CacheIdResolver(loci, alleles, alleleLists, haplotypes, genotypes, genotypeLists, multilocusUnphasedGenotypes);
    }
}