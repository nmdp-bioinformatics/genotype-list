/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.nmdp.gl.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for GlstringBuilder.
 */
public final class GlstringBuilderTest {
    private GlstringBuilder builder;

    @Before
    public void setUp() {
        builder = new GlstringBuilder();
    }

    @Test
    public void testConstructor() {
        assertNotNull(builder);
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildEmpty() {
        builder.build();
    }

    @Test
    public void testBuildLocus() {
        String locus = builder.locus("HLA-A").build();
        assertEquals("HLA-A", locus);
    }

    @Test
    public void testBuildLocusTwice() {
        String locus = builder.locus("HLA-A").locus("HLA-B").build();
        assertEquals("HLA-B", locus);
    }

    @Test
    public void testBuildAllele() {
        String allele = builder.allele("HLA-A*01:01:01:01").build();
        assertEquals("HLA-A*01:01:01:01", allele);
    }

    @Test(expected=IllegalStateException.class)
    public void testAlleleTwice() {
        builder.allele("HLA-A*01:01:01:01").allele("HLA-A*01:01:01:02N");
    }

    @Test
    public void testBuildAlleleList() {
        String alleleList = builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().allele("HLA-A*01:01:01:02N").build();
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList);
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguity() {
        builder.allelicAmbiguity();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildDanglingAllelicAmbiguity() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().build();
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguityTwice() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().allelicAmbiguity();
    }

    @Test
    public void testBuildHaplotype() {
        String haplotype = builder.allele("HLA-A*01:01:01:01").inPhase().allele("HLA-B*02:07:01").allelicAmbiguity().allele("HLA-B*02:07:02").build();
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01/HLA-B*02:07:02", haplotype);
    }

    @Test
    public void testBuildSimpleHaplotype() {
        String haplotype = builder.allele("HLA-A*01:01:01:01").inPhase().allele("HLA-B*02:07:01").build();
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01", haplotype);
    }

    @Test
    public void testBuildThreeTermHaplotype() {
        String haplotype = builder.allele("HLA-A*01:01:01:01").inPhase().allele("HLA-B*02:07:01").inPhase().allele("HLA-C*01:01:01:01").build();
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01~HLA-C*01:01:01:01", haplotype);
    }

    @Test(expected=IllegalStateException.class)
    public void testInPhase() {
        builder.inPhase();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildDanglingInPhase() {
        builder.allele("HLA-A*01:01:01:01").inPhase().build();
    }

    @Test(expected=IllegalStateException.class)
    public void testInPhaseTwice() {
        builder.allele("HLA-A*01:01:01:01").inPhase().inPhase();
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguityInPhase() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().inPhase();
    }

    @Test
    public void testBuildGenotype() {
        String genotype = builder.allele("HLA-A*01:01:01:01").plus().allele("HLA-A*01:01:01:02N").build();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype);
    }

    @Test(expected=IllegalStateException.class)
    public void testPlus() {
        builder.plus();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildDanglingPlus() {
        builder.allele("HLA-A*01:01:01:01").plus().build();
    }

    @Test(expected=IllegalStateException.class)
    public void testPlusTwice() {
        builder.allele("HLA-A*01:01:01:01").plus().plus();
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguityPlus() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().plus();
    }

    @Test(expected=IllegalStateException.class)
    public void testInPhasePlus() {
        builder.allele("HLA-A*01:01:01:01").inPhase().plus();
    }

    @Test
    public void testBuildGenotypeList() {
        String genotypeList = builder.allele("HLA-A*01:01:01:01").plus().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").plus().allele("HLA-A*02:01:01:02").build();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02", genotypeList);
    }

    @Test(expected=IllegalStateException.class)
    public void testGenotypicAmbiguity() {
        builder.genotypicAmbiguity();
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildDanglingGenotypicAmbiguity() {
        builder.allele("HLA-A*01:01:01:01").genotypicAmbiguity().build();
    }

    @Test(expected=IllegalStateException.class)
    public void testGenotypicAmbiguityTwice() {
        builder.allele("HLA-A*01:01:01:01").genotypicAmbiguity().genotypicAmbiguity();
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguityGenotypicAmbiguity() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().genotypicAmbiguity();
    }

    @Test(expected=IllegalStateException.class)
    public void testInPhaseGenotypicAmbiguity() {
        builder.allele("HLA-A*01:01:01:01").inPhase().genotypicAmbiguity();
    }

    @Test(expected=IllegalStateException.class)
    public void testPlusGenotypicAmbiguity() {
        builder.allele("HLA-A*01:01:01:01").plus().genotypicAmbiguity();
    }

    @Test
    public void testBuildMultilocusUnphasedGenotype() {
        String multilocusUnphasedGenotype = builder
            .locus("HLA-A").allele("HLA-A*01:01:01:01").plus().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").plus().allele("HLA-A*02:01:01:02")
            .locus("HLA-B").allele("HLA-B*02:07:01").plus().allele("HLA-B*02:07:02")
            .build();

        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype);
    }

    @Test(expected=IllegalStateException.class)
    public void testBuildDanglingLocusOperator() {
        builder.allele("HLA-A*01:01:01:01").locus("HLA-B").build();
    }

    @Test(expected=IllegalStateException.class)
    public void testLocusOperatorTwice() {
        builder.allele("HLA-A*01:01:01:01").locus("HLA-B").locus("HLA-C");
    }

    @Test(expected=IllegalStateException.class)
    public void testAllelicAmbiguityLocus() {
        builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().locus("HLA-C");
    }

    @Test(expected=IllegalStateException.class)
    public void testInPhaseLocus() {
        builder.allele("HLA-A*01:01:01:01").inPhase().locus("HLA-C");
    }

    @Test(expected=IllegalStateException.class)
    public void testPlusLocus() {
        builder.allele("HLA-A*01:01:01:01").plus().locus("HLA-C");
    }

    @Test(expected=IllegalStateException.class)
    public void testGenotypicAmbiguityLocus() {
        builder.allele("HLA-A*01:01:01:01").genotypicAmbiguity().locus("HLA-C");
    }

}