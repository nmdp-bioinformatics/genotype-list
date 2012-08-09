/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client;

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

/**
 * Unit test for GlClient.
 */
public final class GlClientTest {
    private GlClient client;

    @Before
    public void setUp() {
        client = new GlClient("http://localhost:8080/gl", null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(client);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullNamespace() {
        new GlClient(null, null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullJsonFactory() {
        new GlClient(null, null);
    }

    @Test
    public void testCreateLocus() {
        Locus locus = client.createLocus("HLA-A");

        assertNotNull(locus);
        assertNotNull(locus.getId());
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateLocusNullGlstring() {
        client.createLocus((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetLocusNullIdentifier() {
        client.getLocus(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterLocusNullGlstring() {
        client.registerLocus(null);
    }

    @Test
    public void testCreateAllele() {
        Locus locus = client.createLocus("HLA-A");
        Allele allele = client.createAllele(locus, "HLA-A*01:01:01:01");

        assertNotNull(allele);
        assertNotNull(allele.getId());
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertEquals(locus, allele.getLocus());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateAlleleNullLocus() {
        client.createAllele(null, "HLA-A*01:01:01:01");
    }

    @Test(expected=NullPointerException.class)
    public void testCreateAlleleNullGlstring() {
        Locus locus = client.createLocus("HLA-A");
        client.createAllele(locus, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testCreateAlleleLocusAndGlstringDoNotMatch() {
        Locus locus = client.createLocus("HLA-A");
        client.createAllele(locus, "HLA-B*02:07:01");
    }

    @Test
    public void testCreateAlleleGlstring() {
        Allele allele = client.createAllele("HLA-A*01:01:01:01");

        assertNotNull(allele);
        assertNotNull(allele.getId());
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertEquals("HLA-A", allele.getLocus().getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateAlleleGlstringNullGlstring() {
        client.createAllele(null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetAlleleNullIdentifier() {
        client.getAllele(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAlleleNullGlstring() {
        client.registerAllele(null);
    }

    @Test
    public void testCreateAlleleList() {
        Locus locus = client.createLocus("HLA-A");
        Allele a01 = client.createAllele(locus, "HLA-A*01:01:01:01");
        Allele a02n = client.createAllele(locus, "HLA-A*01:01:01:02N");
        AlleleList alleleList = client.createAlleleList(a01, a02n);

        assertNotNull(alleleList);
        assertNotNull(alleleList.getId());
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals(2, alleleList.getAlleles().size());
        assertEquals(a01, alleleList.getAlleles().get(0));
        assertEquals(a02n, alleleList.getAlleles().get(1));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateAlleleListNullAlleles() {
        client.createAlleleList((Allele) null);
    }

    @Test
    public void testCreateAlleleListGlstring() {
        AlleleList alleleList = client.createAlleleList("HLA-A*01:01:01:01/HLA-A*01:01:01:02N");

        assertNotNull(alleleList);
        assertNotNull(alleleList.getId());
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals(2, alleleList.getAlleles().size());
        assertEquals("HLA-A*01:01:01:01", alleleList.getAlleles().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:02N", alleleList.getAlleles().get(1).getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateAlleleListNullGlstring() {
        client.createAlleleList((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetAlleleListNullIdentifier() {
        client.getAlleleList(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAlleleListNullGlstring() {
        client.registerAlleleList(null);
    }

    @Test
    public void testCreateHaplotype() {
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
        assertEquals(aAlleles, haplotype.getAlleleLists().get(0));
        assertEquals(bAlleles, haplotype.getAlleleLists().get(1));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateHaplotypeNullAlleleLists() {
        client.createHaplotype((AlleleList) null);
    }

    @Test
    public void testCreateHaplotypeGlstring() {
        Haplotype haplotype = client.createHaplotype("HLA-A*01:01:01:01~HLA-B*02:07:01");

        assertNotNull(haplotype);
        assertNotNull(haplotype.getId());
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01", haplotype.getGlstring());
        assertEquals(2, haplotype.getAlleleLists().size());
        assertEquals("HLA-A*01:01:01:01", haplotype.getAlleleLists().get(0).getGlstring());
        assertEquals("HLA-B*02:07:01", haplotype.getAlleleLists().get(1).getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateHaplotypeNullGlstring() {
        client.createHaplotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetHaplotypeNullIdentifier() {
        client.getHaplotype(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterHaplotypeNullGlstring() {
        client.registerHaplotype(null);
    }

    @Test
    public void testCreateGenotype() {
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
        assertEquals(0, genotype.getHaplotypes().size());
        assertEquals(haplotype0, genotype.getHaplotypes().get(0));
        assertEquals(haplotype1, genotype.getHaplotypes().get(1));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateGenotypeNullHaplotypes() {
        client.createGenotype((Haplotype) null);
    }

    @Test
    public void testCreateGenotypeGlstring() {
        Genotype genotype = client.createGenotype("HLA-A*01:01:01:01+HLA-A*01:01:01:02N");

        assertNotNull(genotype);
        assertNotNull(genotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype.getGlstring());
        assertEquals(2, genotype.getHaplotypes().size());
        assertEquals("HLA-A*01:01:01:01", genotype.getHaplotypes().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:02N", genotype.getHaplotypes().get(1).getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateGenotypeNullGlstring() {
        client.createGenotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetGenotypeNullIdentifier() {
        client.getGenotype(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterGenotypeNullGlstring() {
        client.registerGenotype(null);
    }

    @Test
    public void testCreateGenotypeList() {
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
        assertEquals(genotype0, genotypeList.getGenotypes().get(0));
        assertEquals(genotype1, genotypeList.getGenotypes().get(1));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateGenotypeListNullGenotypes() {
        client.createGenotypeList((Genotype) null);
    }

    @Test
    public void testCreateGenotypeListGlstring() {
        GenotypeList genotypeList = client.createGenotypeList("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*01:01:01:01+HLA-A*02:01:01:01");

        assertNotNull(genotypeList);
        assertNotNull(genotypeList.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotypeList.getGlstring());
        assertEquals(2, genotypeList.getGenotypes().size());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotypeList.getGenotypes().get(0).getGlstring());
        assertEquals("HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotypeList.getGenotypes().get(1).getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateGenotypeListNullGlstring() {
        client.createGenotypeList((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetGenotypeListNullIdentifier() {
        client.getGenotypeList(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterGenotypeListNullGlstring() {
        client.registerGenotypeList(null);
    }

    @Test
    public void testCreateMultilocusUnphasedGenotype() {
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
        assertEquals(genotypeList0, multilocusUnphasedGenotype.getGenotypeLists().get(0));
        assertEquals(genotypeList1, multilocusUnphasedGenotype.getGenotypeLists().get(1));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateMultilocusUnphasedGenotypeNullGenotypeLists() {
        client.createMultilocusUnphasedGenotype((GenotypeList) null);
    }

    @Test
    public void testCreateMultilocusUnphasedGenotypeGlstring() {
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = client.createMultilocusUnphasedGenotype("HLA-A*01:01:01:01+HLA-A*01:01:01:02N^HLA-B*02:07:01+HLA-B*02:07:02");

        assertNotNull(multilocusUnphasedGenotype);
        assertNotNull(multilocusUnphasedGenotype.getId());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGlstring());
        assertEquals(2, multilocusUnphasedGenotype.getGenotypeLists().size());
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", multilocusUnphasedGenotype.getGenotypeLists().get(0).getGlstring());
        assertEquals("HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGenotypeLists().get(1).getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testCreateMultilocusUnphasedGenotypeNullGlstring() {
        client.createMultilocusUnphasedGenotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public void testGetMultilocusUnphasedGenotypeNullIdentifier() {
        client.getMultilocusUnphasedGenotype(null);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterMultilocusUnphasedGenotypeNullGlstring() {
        client.registerMultilocusUnphasedGenotype(null);
    }
}
