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

import static org.mockito.Mockito.when;

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
import org.immunogenomics.gl.service.AbstractIdResolverTest;
import org.immunogenomics.gl.service.IdResolver;

import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for VoldemortIdResolver.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class VoldemortIdResolverTest extends AbstractIdResolverTest {
    @Mock
    private StoreClient locusClient;
    @Mock
    private StoreClient alleleClient;
    @Mock
    private StoreClient alleleListClient;
    @Mock
    private StoreClient haplotypeClient;
    @Mock
    private StoreClient genotypeClient;
    @Mock
    private StoreClient genotypeListClient;
    @Mock
    private StoreClient multilocusUnphasedGenotypeClient;
    @Mock
    private StoreClientFactory storeClientFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Locus locus = new Locus(validLocusId, "HLA-A");
        Allele allele = new Allele(validAlleleId, "A01234", "HLA-A*01:01:01:01", locus);
        AlleleList alleleList0 = new AlleleList(validAlleleListId, allele);
        AlleleList alleleList1 = new AlleleList(validAlleleListId, allele);
        Haplotype haplotype = new Haplotype(validHaplotypeId, ImmutableList.of(alleleList0, alleleList1));
        Haplotype haplotype0 = new Haplotype(validHaplotypeId, alleleList0);
        Genotype genotype = new Genotype(validGenotypeId, haplotype0);
        GenotypeList genotypeList0 = new GenotypeList(validGenotypeListId, genotype);
        GenotypeList genotypeList1 = new GenotypeList(validGenotypeListId, genotype);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId, ImmutableList.of(genotypeList0, genotypeList1));

        when(storeClientFactory.getStoreClient("locus")).thenReturn(locusClient);
        when(storeClientFactory.getStoreClient("allele")).thenReturn(alleleClient);
        when(storeClientFactory.getStoreClient("alleleList")).thenReturn(alleleListClient);
        when(storeClientFactory.getStoreClient("haplotype")).thenReturn(haplotypeClient);
        when(storeClientFactory.getStoreClient("genotype")).thenReturn(genotypeClient);
        when(storeClientFactory.getStoreClient("genotypeList")).thenReturn(genotypeListClient);
        when(storeClientFactory.getStoreClient("multilocusUnphasedGenotype")).thenReturn(multilocusUnphasedGenotypeClient);

        when(locusClient.getValue(validLocusId)).thenReturn(locus);
        when(alleleClient.getValue(validAlleleId)).thenReturn(allele);
        when(alleleListClient.getValue(validAlleleListId)).thenReturn(alleleList0);
        when(haplotypeClient.getValue(validHaplotypeId)).thenReturn(haplotype);
        when(genotypeClient.getValue(validGenotypeId)).thenReturn(genotype);
        when(genotypeListClient.getValue(validGenotypeListId)).thenReturn(genotypeList0);
        when(multilocusUnphasedGenotypeClient.getValue(validMultilocusUnphasedGenotypeId)).thenReturn(multilocusUnphasedGenotype);
        super.setUp();
    }

    @Override
    protected IdResolver createIdResolver() {
        return new VoldemortIdResolver(storeClientFactory);
    }
}