/*

    gl-client-functional-tests  Functional tests for the client library for the
    URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client.ft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.client.GlClient;

/**
 * Abstract functional test for implementations of GlClient.
 */
public abstract class AbstractGlClientFT { //extends AbstractGlClientTest {
    protected GlClient client;

    protected abstract GlClient createGlClient();

    @Before
    public void setUp() {
        client = createGlClient();
    }

    @Test
    public final void testCreateGlClient() {
        assertNotNull(client);
    }

    @Test
    public final void testCreateLocus() {
        Locus locus = client.createLocus("HLA-A");

        assertNotNull(locus);
        assertNotNull(locus.getId());
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public final void testCreateAllele() {
        Locus locus = client.createLocus("HLA-A");
        Allele allele = client.createAllele(locus, "HLA-A*01:01:01:01");

        assertNotNull(allele);
        assertNotNull(allele.getId());
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertEquals(locus.getId(), allele.getLocus().getId());
        assertEquals(locus.getGlstring(), allele.getLocus().getGlstring());
    }

    @Test
    public final void testCreateAlleleGlstring() {
        Allele allele = client.createAllele("HLA-A*01:01:01:01");

        assertNotNull(allele);
        assertNotNull(allele.getId());
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertEquals("HLA-A", allele.getLocus().getGlstring());
    }

    @Test
    public final void testCreateAlleleList() {
        Locus locus = client.createLocus("HLA-A");
        Allele a01 = client.createAllele(locus, "HLA-A*01:01:01:01");
        Allele a02n = client.createAllele(locus, "HLA-A*01:01:01:02N");
        AlleleList alleleList = client.createAlleleList(a01, a02n);

        assertNotNull(alleleList);
        assertNotNull(alleleList.getId());
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals(2, alleleList.getAlleles().size());
        assertEquals(a01.getId(), alleleList.getAlleles().get(0).getId());
        assertEquals(a01.getGlstring(), alleleList.getAlleles().get(0).getGlstring());
        assertEquals(a02n.getId(), alleleList.getAlleles().get(1).getId());
        assertEquals(a02n.getGlstring(), alleleList.getAlleles().get(1).getGlstring());
    }

    @Test
    public final void testCreateAlleleListGlstring() {
        AlleleList alleleList = client.createAlleleList("HLA-A*01:01:01:01/HLA-A*01:01:01:02N");

        assertNotNull(alleleList);
        assertNotNull(alleleList.getId());
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals(2, alleleList.getAlleles().size());
        assertEquals("HLA-A*01:01:01:01", alleleList.getAlleles().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:02N", alleleList.getAlleles().get(1).getGlstring());
    }

    @Test
    public final void testCreateHaplotype() {
        Locus a = client.createLocus("HLA-A");
        Locus b = client.createLocus("HLA-B");
        Allele a01 = client.createAllele(a, "HLA-A*01:01:01:01");
        Allele b02 = client.createAllele(b, "HLA-B*02:07:01");
        AlleleList aAlleles = client.createAlleleList(a01);
        AlleleList bAlleles = client.createAlleleList(b02);
        Haplotype haplotype = client.createHaplotype(aAlleles, bAlleles);

        assertNotNull(haplotype);
        assertNotNull(haplotype.getId());
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01", haplotype.getGlstring());
        assertEquals(2, haplotype.getAlleleLists().size());
        assertEquals(aAlleles.getId(), haplotype.getAlleleLists().get(0).getId());
        assertEquals(aAlleles.getGlstring(), haplotype.getAlleleLists().get(0).getGlstring());
        assertEquals(bAlleles.getId(), haplotype.getAlleleLists().get(1).getId());
        assertEquals(bAlleles.getGlstring(), haplotype.getAlleleLists().get(1).getGlstring());
    }

    @Test
    public final void testCreateHaplotypeGlstring() {
        Haplotype haplotype = client.createHaplotype("HLA-A*01:01:01:01~HLA-B*02:07:01");

        assertNotNull(haplotype);
        assertNotNull(haplotype.getId());
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01", haplotype.getGlstring());
        assertEquals(2, haplotype.getAlleleLists().size());
        assertEquals("HLA-A*01:01:01:01", haplotype.getAlleleLists().get(0).getGlstring());
        assertEquals("HLA-B*02:07:01", haplotype.getAlleleLists().get(1).getGlstring());
    }

    @Test
    public final void testCreateGenotype() {
        Locus locus = client.createLocus("HLA-A");
        Allele a01 = client.createAllele(locus, "HLA-A*01:01:01:01");
        Allele a02n = client.createAllele(locus, "HLA-A*01:01:01:02N");
        AlleleList alleleList0 = client.createAlleleList(a01);
        AlleleList alleleList1 = client.createAlleleList(a02n);
        Haplotype haplotype0 = client.createHaplotype(alleleList0);
        Haplotype haplotype1 = client.createHaplotype(alleleList1);
        Genotype genotype = client.createGenotype(haplotype0, haplotype1);

        assertNotNull(genotype);
        assertNotNull(genotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype.getGlstring());
        assertEquals(2, genotype.getHaplotypes().size());
        assertEquals(haplotype0.getId(), genotype.getHaplotypes().get(0).getId());
        assertEquals(haplotype0.getGlstring(), genotype.getHaplotypes().get(0).getGlstring());
        assertEquals(haplotype1.getId(), genotype.getHaplotypes().get(1).getId());
        assertEquals(haplotype1.getGlstring(), genotype.getHaplotypes().get(1).getGlstring());
    }

    @Test
    public final void testCreateGenotypeGlstring() {
        Genotype genotype = client.createGenotype("HLA-A*01:01:01:01+HLA-A*01:01:01:02N");

        assertNotNull(genotype);
        assertNotNull(genotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype.getGlstring());
        assertEquals(2, genotype.getHaplotypes().size());
        assertEquals("HLA-A*01:01:01:01", genotype.getHaplotypes().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:02N", genotype.getHaplotypes().get(1).getGlstring());
    }

    @Test
    public final void testCreateGenotypeList() {
        Locus locus = client.createLocus("HLA-A");
        Allele a01 = client.createAllele(locus, "HLA-A*01:01:01:01");
        Allele a02n = client.createAllele(locus, "HLA-A*01:01:01:02N");
        Allele a02 = client.createAllele(locus, "HLA-A*02:01:01:01");
        AlleleList alleleList0 = client.createAlleleList(a01);
        AlleleList alleleList1 = client.createAlleleList(a02n);
        AlleleList alleleList2 = client.createAlleleList(a02);
        Haplotype haplotype0 = client.createHaplotype(alleleList0);
        Haplotype haplotype1 = client.createHaplotype(alleleList1);
        Haplotype haplotype2 = client.createHaplotype(alleleList2);
        Genotype genotype0 = client.createGenotype(haplotype0, haplotype1);
        Genotype genotype1 = client.createGenotype(haplotype0, haplotype2);
        GenotypeList genotypeList = client.createGenotypeList(genotype0, genotype1);

        assertNotNull(genotypeList);
        assertNotNull(genotypeList.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotypeList.getGlstring());
        assertEquals(2, genotypeList.getGenotypes().size());
        assertEquals(genotype0.getId(), genotypeList.getGenotypes().get(0).getId());
        assertEquals(genotype0.getGlstring(), genotypeList.getGenotypes().get(0).getGlstring());
        assertEquals(genotype1.getId(), genotypeList.getGenotypes().get(1).getId());
        assertEquals(genotype1.getGlstring(), genotypeList.getGenotypes().get(1).getGlstring());
    }

    @Test
    public final void testCreateGenotypeListGlstring() {
        GenotypeList genotypeList = client.createGenotypeList("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*01:01:01:01+HLA-A*02:01:01:01");

        assertNotNull(genotypeList);
        assertNotNull(genotypeList.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotypeList.getGlstring());
        assertEquals(2, genotypeList.getGenotypes().size());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotypeList.getGenotypes().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotypeList.getGenotypes().get(1).getGlstring());
    }

    @Test
    public final void testCreateMultilocusUnphasedGenotype() {
        Locus a = client.createLocus("HLA-A");
        Locus b = client.createLocus("HLA-B");
        Allele a01 = client.createAllele(a, "HLA-A*01:01:01:01");
        Allele a02n = client.createAllele(a, "HLA-A*01:01:01:02N");
        Allele b02 = client.createAllele(b, "HLA-B*02:07:01");
        Allele b0202 = client.createAllele(b, "HLA-B*02:07:02");
        AlleleList alleleList0 = client.createAlleleList(a01);
        AlleleList alleleList1 = client.createAlleleList(a02n);
        AlleleList alleleList2 = client.createAlleleList(b02);
        AlleleList alleleList3 = client.createAlleleList(b0202);
        Haplotype haplotype0 = client.createHaplotype(alleleList0);
        Haplotype haplotype1 = client.createHaplotype(alleleList1);
        Haplotype haplotype2 = client.createHaplotype(alleleList2);
        Haplotype haplotype3 = client.createHaplotype(alleleList3);
        Genotype genotype0 = client.createGenotype(haplotype0, haplotype1);
        Genotype genotype1 = client.createGenotype(haplotype2, haplotype3);
        GenotypeList genotypeList0 = client.createGenotypeList(genotype0);
        GenotypeList genotypeList1 = client.createGenotypeList(genotype1);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = client.createMultilocusUnphasedGenotype(genotypeList0, genotypeList1);

        assertNotNull(multilocusUnphasedGenotype);
        assertNotNull(multilocusUnphasedGenotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGlstring());
        assertEquals(2, multilocusUnphasedGenotype.getGenotypeLists().size());
        assertEquals(genotypeList0.getId(), multilocusUnphasedGenotype.getGenotypeLists().get(0).getId());
        assertEquals(genotypeList0.getGlstring(), multilocusUnphasedGenotype.getGenotypeLists().get(0).getGlstring());
        assertEquals(genotypeList1.getId(), multilocusUnphasedGenotype.getGenotypeLists().get(1).getId());
        assertEquals(genotypeList1.getGlstring(), multilocusUnphasedGenotype.getGenotypeLists().get(1).getGlstring());
    }

    @Test
    public final void testCreateMultilocusUnphasedGenotypeGlstring() {
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = client.createMultilocusUnphasedGenotype("HLA-A*01:01:01:01+HLA-A*01:01:01:02N^HLA-B*02:07:01+HLA-B*02:07:02");

        assertNotNull(multilocusUnphasedGenotype);
        assertNotNull(multilocusUnphasedGenotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGlstring());
        assertEquals(2, multilocusUnphasedGenotype.getGenotypeLists().size());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", multilocusUnphasedGenotype.getGenotypeLists().get(0).getGlstring());
        assertEquals("HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGenotypeLists().get(1).getGlstring());
    }
}
