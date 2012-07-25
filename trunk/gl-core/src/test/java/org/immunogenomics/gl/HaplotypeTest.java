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

import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for Haplotype.
 */
public final class HaplotypeTest {
    private final Locus drb1 = new Locus("http://immunogenomics.org/locus/0", "HLA-DRB1");
    private final Locus drb5 = new Locus("http://immunogenomics.org/locus/1", "HLA-DRB5");
    private final Allele allele0 = new Allele("http://immunogenomics.org/allele/0", "DRB100001", "HLA-DRB1*01:01:01:01", drb1);
    private final Allele allele1 = new Allele("http://immunogenomics.org/allele/1", "DRB500002", "HLA-DRB5*02:01:01:01", drb5);
    private final AlleleList alleleList0 = new AlleleList("http://immunogenomics.org/allele-list/0", ImmutableList.of(allele0));
    private final AlleleList alleleList1 = new AlleleList("http://immunogenomics.org/allele-list/1", ImmutableList.of(allele1));
    private final List<AlleleList> empty = Collections.emptyList();
    private final List<AlleleList> single = ImmutableList.of(alleleList0);
    private final List<AlleleList> alleleLists = ImmutableList.of(alleleList0, alleleList1);

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdentifier() {
        new Haplotype(null, alleleLists);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleLists() {
        new Haplotype("http://immunogenomics.org/haplotype/0", null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorEmptyAlleleLists() {
        new Haplotype("http://immunogenomics.org/haplotype/0", empty);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNotEnough() {
        new Haplotype("http://immunogenomics.org/haplotype/0", single);
    }

    @Test
    public void testId() {
        Haplotype haplotype = new Haplotype("http://immunogenomics.org/haplotype/0", alleleLists);
        assertEquals("http://immunogenomics.org/haplotype/0", haplotype.getId());
    }

    @Test
    public void testGlstring() {
        Haplotype haplotype = new Haplotype("http://immunogenomics.org/haplotype/0", alleleLists);
        assertEquals("HLA-DRB1*01:01:01:01~HLA-DRB5*02:01:01:01", haplotype.getGlstring());
    }

    @Test
    public void testAlleleLists() {
        Haplotype haplotype = new Haplotype("http://immunogenomics.org/haplotype/0", alleleLists);
        assertEquals(alleleLists, haplotype.getAlleleLists());
    }
}