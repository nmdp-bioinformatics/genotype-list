/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.reader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;
import org.immunogenomics.gl.service.reader.GlstringGlReader;

/**
 * Unit test for GlstringGlReader.
 */
public final class GlstringGlReaderTest {
    private GlstringGlReader reader;

    @Mock
    private GlstringResolver glstringResolver;
    @Mock
    private IdResolver idResolver;
    @Mock
    private GlRegistry glRegistry;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        reader = new GlstringGlReader(glstringResolver, idResolver, glRegistry);
    }

    @Test
    public void testConstructor() {
        assertNotNull(reader);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullGlstringResolver() {
        new GlstringGlReader(null, idResolver, glRegistry);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdResolver() {
        new GlstringGlReader(glstringResolver, null, glRegistry);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullGlRegisterer() {
        new GlstringGlReader(glstringResolver, idResolver, null);
    }

    // locus

    @Test
    public void testReadLocus() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("id");
        Locus locus = reader.readLocus("HLA-Z");
        assertNotNull(locus);
        assertEquals("id", locus.getId());
        assertEquals("HLA-Z", locus.getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testReadLocusNullGlstring() throws IOException {
        reader.readLocus(null);
    }

    @Test(expected=IOException.class)
    public void testReadLocusInvalidGlstring() throws IOException {
        reader.readLocus("invalid glstring");
    }

    // allele

    @Test
    public void testReadAllele() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        Allele allele = reader.readAllele("HLA-Z*01:01:01:01", "Z01234");
        assertNotNull(allele);
        assertEquals("allele/id", allele.getId());
        assertEquals("HLA-Z*01:01:01:01", allele.getGlstring());
        assertEquals("Z01234", allele.getAccession());
        assertNotNull(allele.getLocus());
        assertEquals("locus/id", allele.getLocus().getId());
        assertEquals("HLA-Z", allele.getLocus().getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testReadAlleleNullGlstring() throws IOException {
        reader.readAllele(null, "Z01234");
    }

    @Test(expected=NullPointerException.class)
    public void testReadAlleleNullAccession() throws IOException {
        reader.readAllele("HLA-Z:01:01:01:01", null);
    }

    @Test(expected=IOException.class)
    public void testReadAlleleInvalidGlstring() throws IOException {
        reader.readAllele("invalid glstring", "Z01234");
    }

    // allele list

    @Test
    public void testReadAlleleList() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");

        AlleleList alleleList = reader.readAlleleList("HLA-Z*01:01:01:01/HLA-Z*02:01:01:01");
        assertNotNull(alleleList);
        assertEquals("allele-list/id", alleleList.getId());
        assertEquals("HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", alleleList.getGlstring());
        assertNotNull(alleleList.getAlleles());
        assertEquals(2, alleleList.getAlleles().size());

        Allele allele0 = alleleList.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Allele allele1 = alleleList.getAlleles().get(1);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*02:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());
    }

    @Test
    public void testReadAlleleListSingleton() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        AlleleList alleleList = reader.readAlleleList("HLA-Z*01:01:01:01");
        assertNotNull(alleleList);
        assertEquals("allele-list/id", alleleList.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList.getGlstring());
        assertNotNull(alleleList.getAlleles());
        assertEquals(1, alleleList.getAlleles().size());
        Allele allele = alleleList.getAlleles().get(0);
        assertNotNull(allele);
        assertEquals("allele/id", allele.getId());
        assertEquals("HLA-Z*01:01:01:01", allele.getGlstring());
        assertNotNull(allele.getLocus());
        assertEquals("locus/id", allele.getLocus().getId());
        assertEquals("HLA-Z", allele.getLocus().getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testReadAlleleListNullREader() throws IOException {
        reader.readAlleleList(null);
    }

    @Test(expected=IOException.class)
    public void testReadAlleleListInvalidGlstring() throws IOException {
        reader.readAlleleList("invalid glstring");
    }

    // haplotype

    @Test
    public void testReadHaplotype() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");

        Haplotype haplotype = reader.readHaplotype("HLA-Y*01:01:01:01~HLA-Z*01:01:01:01/HLA-Z*02:01:01:01");
        assertNotNull(haplotype);
        assertEquals("haplotype/id", haplotype.getId());
        assertEquals("HLA-Y*01:01:01:01~HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", haplotype.getGlstring());
        assertNotNull(haplotype.getAlleleLists());
        assertEquals(2, haplotype.getAlleleLists().size());

        AlleleList alleleList0 = haplotype.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Y*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Y*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Y", allele0.getLocus().getGlstring());

        AlleleList alleleList1 = haplotype.getAlleleLists().get(1);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(2, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*01:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());

        Allele allele2 = alleleList1.getAlleles().get(1);
        assertNotNull(allele2);
        assertEquals("allele/id", allele2.getId());
        assertEquals("HLA-Z*02:01:01:01", allele2.getGlstring());
        assertNotNull(allele2.getLocus());
        assertEquals("locus/id", allele2.getLocus().getId());
        assertEquals("HLA-Z", allele2.getLocus().getGlstring());
    }

    @Test
    public void testReadHaplotypeSingletonAlleleLists() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");

        Haplotype haplotype = reader.readHaplotype("HLA-Y*01:01:01:01~HLA-Z*02:01:01:01");
        assertNotNull(haplotype);
        assertEquals("haplotype/id", haplotype.getId());
        assertEquals("HLA-Y*01:01:01:01~HLA-Z*02:01:01:01", haplotype.getGlstring());
        assertNotNull(haplotype.getAlleleLists());
        assertEquals(2, haplotype.getAlleleLists().size());

        AlleleList alleleList0 = haplotype.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Y*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Y*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Y", allele0.getLocus().getGlstring());

        AlleleList alleleList1 = haplotype.getAlleleLists().get(1);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(1, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*02:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testReadHaplotypeNullGlstring() throws IOException {
        reader.readHaplotype(null);
    }

    @Test(expected=IOException.class)
    public void testReadHaplotypeInvalidGlstring() throws IOException {
        reader.readHaplotype("invalid glstring");
    }

    // genotype

    @Test
    public void testReadGenotype() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");

        Genotype genotype = reader.readGenotype("HLA-Z*01:01:01:01+HLA-Z*01:01:01:01/HLA-Z*02:01:01:01");
        assertNotNull(genotype);
        assertEquals("genotype/id", genotype.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", genotype.getGlstring());
        assertNotNull(genotype.getHaplotypes());
        assertEquals(2, genotype.getHaplotypes().size());

        Haplotype haplotype0 = genotype.getHaplotypes().get(0);
        assertNotNull(haplotype0);
        assertEquals("haplotype/id", haplotype0.getId());
        assertEquals("HLA-Z*01:01:01:01", haplotype0.getGlstring());
        assertNotNull(haplotype0.getAlleleLists());
        assertEquals(1, haplotype0.getAlleleLists().size());

        AlleleList alleleList0 = haplotype0.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Haplotype haplotype1 = genotype.getHaplotypes().get(1);
        assertNotNull(haplotype1);
        assertEquals("haplotype/id", haplotype1.getId());
        assertEquals("HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", haplotype1.getGlstring());
        assertNotNull(haplotype1.getAlleleLists());
        assertEquals(1, haplotype1.getAlleleLists().size());

        AlleleList alleleList1 = haplotype1.getAlleleLists().get(0);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*01:01:01:01/HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(2, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*01:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());

        Allele allele2 = alleleList1.getAlleles().get(1);
        assertNotNull(allele2);
        assertEquals("allele/id", allele2.getId());
        assertEquals("HLA-Z*02:01:01:01", allele2.getGlstring());
        assertNotNull(allele2.getLocus());
        assertEquals("locus/id", allele2.getLocus().getId());
        assertEquals("HLA-Z", allele2.getLocus().getGlstring());
    }

    @Test
    public void testReadGenotypeWithHaplotype() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");

        Genotype genotype = reader.readGenotype("HLA-Z*01:01:01:01+HLA-Y*01:01:01:01~HLA-Z*02:01:01:01");
        assertNotNull(genotype);
        assertEquals("genotype/id", genotype.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Y*01:01:01:01~HLA-Z*02:01:01:01", genotype.getGlstring());
        assertNotNull(genotype.getHaplotypes());
        assertEquals(2, genotype.getHaplotypes().size());

        Haplotype haplotype0 = genotype.getHaplotypes().get(0);
        assertNotNull(haplotype0);
        assertEquals("haplotype/id", haplotype0.getId());
        assertEquals("HLA-Z*01:01:01:01", haplotype0.getGlstring());
        assertNotNull(haplotype0.getAlleleLists());
        assertEquals(1, haplotype0.getAlleleLists().size());

        AlleleList alleleList0 = haplotype0.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Haplotype haplotype1 = genotype.getHaplotypes().get(1);
        assertNotNull(haplotype1);
        assertEquals("haplotype/id", haplotype1.getId());
        assertEquals("HLA-Y*01:01:01:01~HLA-Z*02:01:01:01", haplotype1.getGlstring());
        assertNotNull(haplotype1.getAlleleLists());
        assertEquals(2, haplotype1.getAlleleLists().size());

        AlleleList alleleList1 = haplotype1.getAlleleLists().get(0);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Y*01:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(1, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Y*01:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Y", allele1.getLocus().getGlstring());

        AlleleList alleleList2 = haplotype1.getAlleleLists().get(1);
        assertNotNull(alleleList2);
        assertEquals("allele-list/id", alleleList2.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList2.getGlstring());
        assertNotNull(alleleList2.getAlleles());
        assertEquals(1, alleleList2.getAlleles().size());

        Allele allele2 = alleleList2.getAlleles().get(0);
        assertNotNull(allele2);
        assertEquals("allele/id", allele2.getId());
        assertEquals("HLA-Z*02:01:01:01", allele2.getGlstring());
        assertNotNull(allele2.getLocus());
        assertEquals("locus/id", allele2.getLocus().getId());
        assertEquals("HLA-Z", allele2.getLocus().getGlstring());
    }

    @Test
    public void testReadGenotypeSingletonHaplotype() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");

        Genotype genotype = reader.readGenotype("HLA-Y*01:01:01:01~HLA-Z*01:01:01:01");
        assertNotNull(genotype);
    }

    @Test(expected=NullPointerException.class)
    public void testReadGenotypeNullGlstring() throws IOException {
        reader.readGenotype(null);
    }

    @Test(expected=IOException.class)
    public void testReadGenotypeInvalidGlstring() throws IOException {
        reader.readGenotype("invalid glstring");
    }

    // genotype list

    @Test
    public void testReadGenotypeList() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");
        when(glstringResolver.resolveGenotypeList(anyString())).thenReturn("genotype-list/id");

        GenotypeList genotypeList = reader.readGenotypeList("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01|HLA-Z*02:01:01:01+HLA-Z*03:01:01:01");
        assertNotNull(genotypeList);
        assertEquals("genotype-list/id", genotypeList.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01|HLA-Z*02:01:01:01+HLA-Z*03:01:01:01", genotypeList.getGlstring());
        assertNotNull(genotypeList.getGenotypes());
        assertEquals(2, genotypeList.getGenotypes().size());

        Genotype genotype0 = genotypeList.getGenotypes().get(0);
        assertNotNull(genotype0);
        assertEquals("genotype/id", genotype0.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01", genotype0.getGlstring());
        assertNotNull(genotype0.getHaplotypes());
        assertEquals(2, genotype0.getHaplotypes().size());

        Haplotype haplotype0 = genotype0.getHaplotypes().get(0);
        assertNotNull(haplotype0);
        assertEquals("haplotype/id", haplotype0.getId());
        assertEquals("HLA-Z*01:01:01:01", haplotype0.getGlstring());
        assertNotNull(haplotype0.getAlleleLists());
        assertEquals(1, haplotype0.getAlleleLists().size());

        AlleleList alleleList0 = haplotype0.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Haplotype haplotype1 = genotype0.getHaplotypes().get(1);
        assertNotNull(haplotype1);
        assertEquals("haplotype/id", haplotype1.getId());
        assertEquals("HLA-Z*02:01:01:01", haplotype1.getGlstring());
        assertNotNull(haplotype1.getAlleleLists());
        assertEquals(1, haplotype1.getAlleleLists().size());

        AlleleList alleleList1 = haplotype1.getAlleleLists().get(0);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(1, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*02:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());

        Genotype genotype1 = genotypeList.getGenotypes().get(1);
        assertNotNull(genotype1);
        assertEquals("genotype/id", genotype1.getId());
        assertEquals("HLA-Z*02:01:01:01+HLA-Z*03:01:01:01", genotype1.getGlstring());
        assertNotNull(genotype1.getHaplotypes());
        assertEquals(2, genotype1.getHaplotypes().size());

        Haplotype haplotype2 = genotype1.getHaplotypes().get(0);
        assertNotNull(haplotype2);
        assertEquals("haplotype/id", haplotype2.getId());
        assertEquals("HLA-Z*02:01:01:01", haplotype2.getGlstring());
        assertNotNull(haplotype2.getAlleleLists());
        assertEquals(1, haplotype2.getAlleleLists().size());

        AlleleList alleleList2 = haplotype2.getAlleleLists().get(0);
        assertNotNull(alleleList2);
        assertEquals("allele-list/id", alleleList2.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList2.getGlstring());
        assertNotNull(alleleList2.getAlleles());
        assertEquals(1, alleleList2.getAlleles().size());

        Allele allele2 = alleleList2.getAlleles().get(0);
        assertNotNull(allele2);
        assertEquals("allele/id", allele2.getId());
        assertEquals("HLA-Z*02:01:01:01", allele2.getGlstring());
        assertNotNull(allele2.getLocus());
        assertEquals("locus/id", allele2.getLocus().getId());
        assertEquals("HLA-Z", allele2.getLocus().getGlstring());

        Haplotype haplotype3 = genotype1.getHaplotypes().get(1);
        assertNotNull(haplotype3);
        assertEquals("haplotype/id", haplotype3.getId());
        assertEquals("HLA-Z*03:01:01:01", haplotype3.getGlstring());
        assertNotNull(haplotype3.getAlleleLists());
        assertEquals(1, haplotype3.getAlleleLists().size());

        AlleleList alleleList3 = haplotype3.getAlleleLists().get(0);
        assertNotNull(alleleList3);
        assertEquals("allele-list/id", alleleList3.getId());
        assertEquals("HLA-Z*03:01:01:01", alleleList3.getGlstring());
        assertNotNull(alleleList3.getAlleles());
        assertEquals(1, alleleList3.getAlleles().size());

        Allele allele3 = alleleList3.getAlleles().get(0);
        assertNotNull(allele3);
        assertEquals("allele/id", allele3.getId());
        assertEquals("HLA-Z*03:01:01:01", allele3.getGlstring());
        assertNotNull(allele3.getLocus());
        assertEquals("locus/id", allele3.getLocus().getId());
        assertEquals("HLA-Z", allele3.getLocus().getGlstring());
    }

    @Test
    public void testReadGenotypeListSingleton() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");
        when(glstringResolver.resolveGenotypeList(anyString())).thenReturn("genotype-list/id");

        GenotypeList genotypeList = reader.readGenotypeList("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01");
        assertNotNull(genotypeList);
        assertEquals("genotype-list/id", genotypeList.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01", genotypeList.getGlstring());
        assertNotNull(genotypeList.getGenotypes());
        assertEquals(1, genotypeList.getGenotypes().size());

        Genotype genotype0 = genotypeList.getGenotypes().get(0);
        assertNotNull(genotype0);
        assertEquals("genotype/id", genotype0.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01", genotype0.getGlstring());
        assertNotNull(genotype0.getHaplotypes());
        assertEquals(2, genotype0.getHaplotypes().size());

        Haplotype haplotype0 = genotype0.getHaplotypes().get(0);
        assertNotNull(haplotype0);
        assertEquals("haplotype/id", haplotype0.getId());
        assertEquals("HLA-Z*01:01:01:01", haplotype0.getGlstring());
        assertNotNull(haplotype0.getAlleleLists());
        assertEquals(1, haplotype0.getAlleleLists().size());

        AlleleList alleleList0 = haplotype0.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Haplotype haplotype1 = genotype0.getHaplotypes().get(1);
        assertNotNull(haplotype1);
        assertEquals("haplotype/id", haplotype1.getId());
        assertEquals("HLA-Z*02:01:01:01", haplotype1.getGlstring());
        assertNotNull(haplotype1.getAlleleLists());
        assertEquals(1, haplotype1.getAlleleLists().size());

        AlleleList alleleList1 = haplotype1.getAlleleLists().get(0);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(1, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*02:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());
    }

    @Test(expected=NullPointerException.class)
    public void testReadGenotypeListNullGlstring() throws IOException {
        reader.readGenotypeList(null);
    }

    @Test(expected=IOException.class)
    public void testReadGenotypeListInvalidGlstring() throws IOException {
        reader.readGenotypeList("invalid glstring");
    }

    // multilocus unphased genotype

    @Test
    public void testReadMultilocusUnphasedGenotype() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");
        when(glstringResolver.resolveGenotypeList(anyString())).thenReturn("genotype-list/id");
        when(glstringResolver.resolveMultilocusUnphasedGenotype(anyString())).thenReturn("multilocus-unphased-genotype/id");

        MultilocusUnphasedGenotype multilocusUnphasedGenotype = reader.readMultilocusUnphasedGenotype("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01^HLA-Y*01:01:01:01+HLA-Y*02:01:01:01");
        assertNotNull(multilocusUnphasedGenotype);
        assertEquals("multilocus-unphased-genotype/id", multilocusUnphasedGenotype.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01^HLA-Y*01:01:01:01+HLA-Y*02:01:01:01", multilocusUnphasedGenotype.getGlstring());
        assertNotNull(multilocusUnphasedGenotype.getGenotypeLists());
        assertEquals(2, multilocusUnphasedGenotype.getGenotypeLists().size());

        GenotypeList genotypeList0 = multilocusUnphasedGenotype.getGenotypeLists().get(0);
        assertNotNull(genotypeList0);
        assertEquals("genotype-list/id", genotypeList0.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01", genotypeList0.getGlstring());
        assertNotNull(genotypeList0.getGenotypes());
        assertEquals(1, genotypeList0.getGenotypes().size());

        Genotype genotype0 = genotypeList0.getGenotypes().get(0);
        assertNotNull(genotype0);
        assertEquals("genotype/id", genotype0.getId());
        assertEquals("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01", genotype0.getGlstring());
        assertNotNull(genotype0.getHaplotypes());
        assertEquals(2, genotype0.getHaplotypes().size());

        Haplotype haplotype0 = genotype0.getHaplotypes().get(0);
        assertNotNull(haplotype0);
        assertEquals("haplotype/id", haplotype0.getId());
        assertEquals("HLA-Z*01:01:01:01", haplotype0.getGlstring());
        assertNotNull(haplotype0.getAlleleLists());
        assertEquals(1, haplotype0.getAlleleLists().size());

        AlleleList alleleList0 = haplotype0.getAlleleLists().get(0);
        assertNotNull(alleleList0);
        assertEquals("allele-list/id", alleleList0.getId());
        assertEquals("HLA-Z*01:01:01:01", alleleList0.getGlstring());
        assertNotNull(alleleList0.getAlleles());
        assertEquals(1, alleleList0.getAlleles().size());

        Allele allele0 = alleleList0.getAlleles().get(0);
        assertNotNull(allele0);
        assertEquals("allele/id", allele0.getId());
        assertEquals("HLA-Z*01:01:01:01", allele0.getGlstring());
        assertNotNull(allele0.getLocus());
        assertEquals("locus/id", allele0.getLocus().getId());
        assertEquals("HLA-Z", allele0.getLocus().getGlstring());

        Haplotype haplotype1 = genotype0.getHaplotypes().get(1);
        assertNotNull(haplotype1);
        assertEquals("haplotype/id", haplotype1.getId());
        assertEquals("HLA-Z*02:01:01:01", haplotype1.getGlstring());
        assertNotNull(haplotype1.getAlleleLists());
        assertEquals(1, haplotype1.getAlleleLists().size());

        AlleleList alleleList1 = haplotype1.getAlleleLists().get(0);
        assertNotNull(alleleList1);
        assertEquals("allele-list/id", alleleList1.getId());
        assertEquals("HLA-Z*02:01:01:01", alleleList1.getGlstring());
        assertNotNull(alleleList1.getAlleles());
        assertEquals(1, alleleList1.getAlleles().size());

        Allele allele1 = alleleList1.getAlleles().get(0);
        assertNotNull(allele1);
        assertEquals("allele/id", allele1.getId());
        assertEquals("HLA-Z*02:01:01:01", allele1.getGlstring());
        assertNotNull(allele1.getLocus());
        assertEquals("locus/id", allele1.getLocus().getId());
        assertEquals("HLA-Z", allele1.getLocus().getGlstring());

        GenotypeList genotypeList1 = multilocusUnphasedGenotype.getGenotypeLists().get(1);
        assertNotNull(genotypeList1);
        assertEquals("genotype-list/id", genotypeList1.getId());
        assertEquals("HLA-Y*01:01:01:01+HLA-Y*02:01:01:01", genotypeList1.getGlstring());
        assertNotNull(genotypeList1.getGenotypes());
        assertEquals(1, genotypeList1.getGenotypes().size());

        Genotype genotype1 = genotypeList1.getGenotypes().get(0);
        assertNotNull(genotype1);
        assertEquals("genotype/id", genotype1.getId());
        assertEquals("HLA-Y*01:01:01:01+HLA-Y*02:01:01:01", genotype1.getGlstring());
        assertNotNull(genotype1.getHaplotypes());
        assertEquals(2, genotype1.getHaplotypes().size());

        Haplotype haplotype2 = genotype1.getHaplotypes().get(0);
        assertNotNull(haplotype2);
        assertEquals("haplotype/id", haplotype2.getId());
        assertEquals("HLA-Y*01:01:01:01", haplotype2.getGlstring());
        assertNotNull(haplotype2.getAlleleLists());
        assertEquals(1, haplotype2.getAlleleLists().size());

        AlleleList alleleList2 = haplotype2.getAlleleLists().get(0);
        assertNotNull(alleleList2);
        assertEquals("allele-list/id", alleleList2.getId());
        assertEquals("HLA-Y*01:01:01:01", alleleList2.getGlstring());
        assertNotNull(alleleList2.getAlleles());
        assertEquals(1, alleleList2.getAlleles().size());

        Allele allele2 = alleleList2.getAlleles().get(0);
        assertNotNull(allele2);
        assertEquals("allele/id", allele2.getId());
        assertEquals("HLA-Y*01:01:01:01", allele2.getGlstring());
        assertNotNull(allele2.getLocus());
        assertEquals("locus/id", allele2.getLocus().getId());
        assertEquals("HLA-Y", allele2.getLocus().getGlstring());

        Haplotype haplotype3 = genotype1.getHaplotypes().get(1);
        assertNotNull(haplotype3);
        assertEquals("haplotype/id", haplotype3.getId());
        assertEquals("HLA-Y*02:01:01:01", haplotype3.getGlstring());
        assertNotNull(haplotype3.getAlleleLists());
        assertEquals(1, haplotype3.getAlleleLists().size());

        AlleleList alleleList3 = haplotype3.getAlleleLists().get(0);
        assertNotNull(alleleList3);
        assertEquals("allele-list/id", alleleList3.getId());
        assertEquals("HLA-Y*02:01:01:01", alleleList3.getGlstring());
        assertNotNull(alleleList3.getAlleles());
        assertEquals(1, alleleList3.getAlleles().size());

        Allele allele3 = alleleList3.getAlleles().get(0);
        assertNotNull(allele3);
        assertEquals("allele/id", allele3.getId());
        assertEquals("HLA-Y*02:01:01:01", allele3.getGlstring());
        assertNotNull(allele3.getLocus());
        assertEquals("locus/id", allele3.getLocus().getId());
        assertEquals("HLA-Y", allele3.getLocus().getGlstring());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testReadMultilocusUnphasedGenotypeSingleton() throws IOException {
        when(glstringResolver.resolveLocus(anyString())).thenReturn("locus/id");
        when(glstringResolver.resolveAllele(anyString())).thenReturn("allele/id");
        when(glstringResolver.resolveAlleleList(anyString())).thenReturn("allele-list/id");
        when(glstringResolver.resolveHaplotype(anyString())).thenReturn("haplotype/id");
        when(glstringResolver.resolveGenotype(anyString())).thenReturn("genotype/id");
        when(glstringResolver.resolveGenotypeList(anyString())).thenReturn("genotype-list/id");
        when(glstringResolver.resolveMultilocusUnphasedGenotype(anyString())).thenReturn("multilocus-unphased-genotype/id");

        // todo: remove this restriction . . .
        // genotypeLists must contain at least two genotype lists
        reader.readMultilocusUnphasedGenotype("HLA-Z*01:01:01:01+HLA-Z*02:01:01:01");
    }

    @Test(expected=NullPointerException.class)
    public void testReadMultilocusUnphasedGenotypeListNullGlstring() throws IOException {
        reader.readMultilocusUnphasedGenotype(null);
    }

    @Test(expected=IOException.class)
    public void testReadMultilocusUnphasedGenotypeListInvalidGlstring() throws IOException {
        reader.readMultilocusUnphasedGenotype("invalid glstring");
    }
}