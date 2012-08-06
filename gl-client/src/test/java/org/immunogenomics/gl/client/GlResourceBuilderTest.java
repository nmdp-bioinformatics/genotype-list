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
 * Unit test for GlResourceBuilder.
 */
public final class GlResourceBuilderTest {
    private GlClient client;
    private GlResourceBuilder builder;

    @Before
    public void setUp() {
        client = new GlClient("http://localhost:8080/gl");
        builder = new GlResourceBuilder(client);
    }

    @Test
    public void testConstructor() {
        assertNotNull(builder);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullClient() {
        new GlResourceBuilder(null);
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildLocusNoLocus() {
        builder.buildLocus();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildAlleleNoLocus() {
        builder.buildAllele();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildAlleleListNoLocus() {
        builder.buildAlleleList();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildHaplotypeNoLocus() {
        builder.buildHaplotype();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildGenotypeNoLocus() {
        builder.buildGenotype();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildGenotypeListNoLocus() {
        builder.buildGenotypeList();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildMultilocusUnphasedGenotypeNoLocus() {
        builder.buildMultilocusUnphasedGenotype();
    }

    @Test
    public void testBuildLocus() {
        Locus locus = builder.locus("HLA-A").buildLocus();
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public void testBuildAllele() {
        Allele allele = builder.allele("HLA-A*01:01:01:01").buildAllele();
        assertNotNull(allele);
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertNotNull(allele.getLocus());
        assertEquals("HLA-A", allele.getLocus().getGlstring());

        Locus locus = builder.buildLocus();
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public void testBuildSingletonAlleleList() {
        AlleleList alleleList = builder.allele("HLA-A*01:01:01:01").buildAlleleList();
        assertNotNull(alleleList);
        assertEquals("HLA-A*01:01:01:01", alleleList.getGlstring());
        assertNotNull(alleleList.getAlleles());
        assertEquals(1, alleleList.getAlleles().size());
        assertNotNull(alleleList.getAlleles().get(0));
        assertEquals("HLA-A*01:01:01:01", alleleList.getAlleles().get(0).getGlstring());
        assertNotNull(alleleList.getAlleles().get(0).getLocus());
        assertEquals("HLA-A", alleleList.getAlleles().get(0).getLocus().getGlstring());

        Allele allele = builder.buildAllele();
        assertNotNull(allele);
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertNotNull(allele.getLocus());
        assertEquals("HLA-A", allele.getLocus().getGlstring());

        Locus locus = builder.buildLocus();
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public void testBuildAlleleList() {
        AlleleList alleleList = builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().allele("HLA-A*01:01:01:02N").buildAlleleList();
        assertNotNull(alleleList);
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertNotNull(alleleList.getAlleles());
        assertEquals(2, alleleList.getAlleles().size());
        assertNotNull(alleleList.getAlleles().get(0));
        assertEquals("HLA-A*01:01:01:01", alleleList.getAlleles().get(0).getGlstring());
        assertNotNull(alleleList.getAlleles().get(0).getLocus());
        assertEquals("HLA-A", alleleList.getAlleles().get(1).getLocus().getGlstring());
        assertNotNull(alleleList.getAlleles().get(1));
        assertEquals("HLA-A*01:01:01:02N", alleleList.getAlleles().get(1).getGlstring());
        assertNotNull(alleleList.getAlleles().get(1).getLocus());
        assertEquals("HLA-A", alleleList.getAlleles().get(1).getLocus().getGlstring());

        Allele allele = builder.buildAllele();
        assertNotNull(allele);
        assertEquals("HLA-A*01:01:01:02N", allele.getGlstring());
        assertNotNull(allele.getLocus());
        assertEquals("HLA-A", allele.getLocus().getGlstring());

        Locus locus = builder.buildLocus();
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public void testBuildHaplotype() {
        Haplotype haplotype = builder.allele("HLA-A*01:01:01:01").inPhase().allele("HLA-B*02:07:01").allelicAmbiguity().allele("HLA-B*02:07:02").buildHaplotype();
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01/HLA-B*02:07:02", haplotype.getGlstring());
    }

    @Test
    public void testBuildGenotype() {
        Genotype genotype = builder.allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").buildGenotype();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype.getGlstring());
    }

    @Test
    public void testBuildGenotypeList() {
        GenotypeList genotypeList = builder.allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").xxx().allele("HLA-A*02:01:01:02").buildGenotypeList();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02", genotypeList.getGlstring());
    }

    @Test
    public void testBuildMultilocusUnphasedGenotype() {
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = builder
            .locus("HLA-A").allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").xxx().allele("HLA-A*02:01:01:02")
            .locus("HLA-B").allele("HLA-B*02:07:01").xxx().allele("HLA-B*02:07:02")
            .buildMultilocusUnphasedGenotype();

        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype.getGlstring());
    }
}