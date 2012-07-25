/*

    gl-core  Core interfaces and classes for the gl project.
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
package org.immunogenomics.gl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for Genotype.
 */
public final class GenotypeTest {
    private final Locus locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
    private final Allele allele0 = new Allele("http://immunogenomics.org/allele/0", "A00001", "HLA-A*01:01:01:01", locus);
    private final Allele allele1 = new Allele("http://immunogenomics.org/allele/1", "A00002", "HLA-A*02:01:01:01", locus);

    private final AlleleList alleleList0 = new AlleleList("http://immunogenomics.org/allele-list/0", ImmutableList.of(allele0));
    private final AlleleList alleleList1 = new AlleleList("http://immunogenomics.org/allele-list/1", ImmutableList.of(allele1));
    private final List<AlleleList> emptyAlleleLists = Collections.emptyList();
    private final List<AlleleList> singleAlleleList = ImmutableList.of(alleleList0);
    private final List<AlleleList> alleleLists = ImmutableList.of(alleleList0, alleleList1);

    private final Haplotype haplotype0 = new Haplotype("http://immunogenomics.org/haplotype/0", alleleLists);
    private final Haplotype haplotype1 = new Haplotype("http://immunogenomics.org/haplotype/1", alleleLists);
    private final List<Haplotype> emptyHaplotypes = Collections.emptyList();
    private final List<Haplotype> singleHaplotype = ImmutableList.of(haplotype0);
    private final List<Haplotype> haplotypes = ImmutableList.of(haplotype0, haplotype1);

    @Test(expected=NullPointerException.class)
    public void testConstructorAlleleListNullIdentifier() {
        new Genotype(null, alleleList0);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleList() {
        new Genotype("http://immunogenomics.org/genotype/0", (AlleleList) null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorHaplotypeNullIdentifier() {
        new Genotype(null, haplotype0);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHaplotype() {
        new Genotype("http://immunogenomics.org/genotype/0", (Haplotype) null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdentifier() {
        new Genotype(null, alleleLists, haplotypes);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleLists() {
        new Genotype("http://immunogenomics.org/genotype/0", null, haplotypes);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullHaplotypes() {
        new Genotype("http://immunogenomics.org/genotype/0", alleleLists, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEmpty() {
        new Genotype("http://immunogenomics.org/genotype/0", emptyAlleleLists, emptyHaplotypes);
    }

    @Test
    public void testConstructorNotEnoughAlleleLists() {
        // cardinality of 1 is ok
        assertNotNull(new Genotype("http://immunogenomics.org/genotype/0", singleAlleleList, emptyHaplotypes));
    }

    @Test
    public void testConstructorNotEnoughHaplotypes() {
        // cardinality of 1 is ok
        assertNotNull(new Genotype("http://immunogenomics.org/genotype/0", emptyAlleleLists, singleHaplotype));
    }

    @Test
    public void testMixedAlleleListAndHaplotype() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", singleAlleleList, singleHaplotype);
        assertEquals(singleAlleleList, genotype.getAlleleLists());
        assertEquals(singleHaplotype, genotype.getHaplotypes());
    }

    @Test
    public void testAlleleListOnly() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", alleleList0);
        assertNotNull(genotype.getAlleleLists());
        assertEquals(1, genotype.getAlleleLists().size());
        assertTrue(genotype.getAlleleLists().contains(alleleList0));
        assertNotNull(genotype.getHaplotypes());
        assertTrue(genotype.getHaplotypes().isEmpty());
    }

    @Test
    public void testHaplotypeOnly() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", haplotype0);
        assertNotNull(genotype.getAlleleLists());
        assertTrue(genotype.getAlleleLists().isEmpty());
        assertNotNull(genotype.getHaplotypes());
        assertEquals(1, genotype.getHaplotypes().size());
        assertTrue(genotype.getHaplotypes().contains(haplotype0));
    }

    @Test
    public void testId() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", alleleLists, emptyHaplotypes);
        assertEquals("http://immunogenomics.org/genotype/0", genotype.getId());
    }

    @Test
    public void testGlstring() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", alleleLists, emptyHaplotypes);
        assertEquals("HLA-A*01:01:01:01+HLA-A*02:01:01:01", genotype.getGlstring());
    }

    @Test
    public void testAlleleLists() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", alleleLists, haplotypes);
        assertEquals(alleleLists, genotype.getAlleleLists());
    }

    @Test
    public void testHaplotypes() {
        Genotype genotype = new Genotype("http://immunogenomics.org/genotype/0", alleleLists, haplotypes);
        assertEquals(haplotypes, genotype.getHaplotypes());
    }
}