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
        assertNotNull(locus);
        assertEquals("HLA-A", locus);
    }

    @Test
    public void testBuildAllele() {
        String allele = builder.allele("HLA-A*01:01:01:01").build();
        assertEquals("HLA-A*01:01:01:01", allele);
    }

    @Test
    public void testBuildAlleleList() {
        String alleleList = builder.allele("HLA-A*01:01:01:01").allelicAmbiguity().allele("HLA-A*01:01:01:02N").build();
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList);
    }

    @Test
    public void testBuildHaplotype() {
        String haplotype = builder.allele("HLA-A*01:01:01:01").inPhase().allele("HLA-B*02:07:01").allelicAmbiguity().allele("HLA-B*02:07:02").build();
        assertEquals("HLA-A*01:01:01:01~HLA-B*02:07:01/HLA-B*02:07:02", haplotype);
    }

    @Test
    public void testBuildGenotype() {
        String genotype = builder.allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").build();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N", genotype);
    }

    @Test
    public void testBuildGenotypeList() {
        String genotypeList = builder.allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").xxx().allele("HLA-A*02:01:01:02").build();
        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02", genotypeList);
    }

    @Test
    public void testBuildMultilocusUnphasedGenotype() {
        String multilocusUnphasedGenotype = builder
            .locus("HLA-A").allele("HLA-A*01:01:01:01").xxx().allele("HLA-A*01:01:01:02N").genotypicAmbiguity().allele("HLA-A*02:01:01:01").xxx().allele("HLA-A*02:01:01:02")
            .locus("HLA-B").allele("HLA-B*02:07:01").xxx().allele("HLA-B*02:07:02")
            .build();

        assertEquals("HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-A*02:01:01:01+HLA-A*02:01:01:02^HLA-B*02:07:01+HLA-B*02:07:02", multilocusUnphasedGenotype);
    }
}