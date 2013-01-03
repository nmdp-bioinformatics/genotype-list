/*

    gl-core  Core interfaces and classes for the gl project.
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
package org.immunogenomics.gl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for AlleleList.
 */
public final class AlleleListTest {
    private final Locus locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
    private final Allele allele0 = new Allele("http://immunogenomics.org/allele/0", "A00001", "HLA-A*01:01:01:01", locus);
    private final Allele allele1 = new Allele("http://immunogenomics.org/allele/1", "A00002", "HLA-A*02:01:01:01", locus);
    private final List<Allele> empty = Collections.emptyList();
    private final List<Allele> single = ImmutableList.of(allele0);
    private final List<Allele> alleles = ImmutableList.of(allele0, allele1);

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdentifierAllele() {
        new AlleleList(null, allele0);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAllele() {
        new AlleleList("http://immunogenomics.org/allele-list/0", (Allele) null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdentifierAlleles() {
        new AlleleList(null, alleles);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleles() {
        new AlleleList("http://immunogenomics.org/allele-list/0", (List<Allele>) null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleleInAlleleList() {
        List<Allele> allelesContainingNull = new ArrayList<Allele>();
        allelesContainingNull.add(allele0);
        allelesContainingNull.add(null);
        new AlleleList("http://immunogenomics.org/allele-list/0", allelesContainingNull);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testConstructorNotEmptyAlleles() {
        new AlleleList("http://immunogenomics.org/allele-list/0", empty);
    }

    @Test
    public void testConstructorSingleAlleles() {
        AlleleList alleleList = new AlleleList("http://immunogenomics.org/allele-list/0", single);
        assertEquals(single, alleleList.getAlleles());
    }

    @Test
    public void testId() {
        AlleleList alleleList = new AlleleList("http://immunogenomics.org/allele-list/0", alleles);
        assertEquals("http://immunogenomics.org/allele-list/0", alleleList.getId());
    }

    @Test
    public void testGlstring() {
        AlleleList alleleList = new AlleleList("http://immunogenomics.org/allele-list/0", alleles);
        assertEquals("HLA-A*01:01:01:01/HLA-A*02:01:01:01", alleleList.getGlstring());
    }

    @Test
    public void testAllele() {
        AlleleList alleleList = new AlleleList("http://immunogenomics.org/allele-list/0", allele0);
        assertEquals(1, alleleList.getAlleles().size());
        assertTrue(alleleList.getAlleles().contains(allele0));
    }

    @Test
    public void testAlleles() {
        AlleleList alleleList = new AlleleList("http://immunogenomics.org/allele-list/0", alleles);
        assertEquals(alleles, alleleList.getAlleles());
    }
}