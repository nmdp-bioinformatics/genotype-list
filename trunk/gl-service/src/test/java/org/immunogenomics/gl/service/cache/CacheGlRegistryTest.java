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

import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.AbstractGlRegistryTest;
import org.immunogenomics.gl.service.GlRegistry;

import com.google.common.cache.Cache;
import com.google.common.cache.LoadingCache;

/**
 * Unit test for CacheGlRegistry.
 */
public final class CacheGlRegistryTest extends AbstractGlRegistryTest {
    @Mock
    private Cache<String, Locus> loci;
    @Mock
    private Cache<String, Allele> alleles;
    @Mock
    private Cache<String, AlleleList> alleleLists;
    @Mock
    private Cache<String, Haplotype> haplotypes;
    @Mock
    private Cache<String, Genotype> genotypes;
    @Mock
    private Cache<String, GenotypeList> genotypeLists;
    @Mock
    private Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes;
    @Mock
    private LoadingCache<String, String> locusIds;
    @Mock
    private LoadingCache<String, String> alleleIds;
    @Mock
    private LoadingCache<String, String> alleleListIds;
    @Mock
    private LoadingCache<String, String> haplotypeIds;
    @Mock
    private LoadingCache<String, String> genotypeIds;
    @Mock
    private LoadingCache<String, String> genotypeListIds;
    @Mock
    private LoadingCache<String, String> multilocusUnphasedGenotypeIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }

    @Override
    protected GlRegistry createGlRegistry() {
        return new CacheGlRegistry(loci, alleles, alleleLists, haplotypes, genotypes, genotypeLists, multilocusUnphasedGenotypes,
                                   locusIds, alleleIds, alleleListIds, haplotypeIds, genotypeIds, genotypeListIds, multilocusUnphasedGenotypeIds);
    }
}