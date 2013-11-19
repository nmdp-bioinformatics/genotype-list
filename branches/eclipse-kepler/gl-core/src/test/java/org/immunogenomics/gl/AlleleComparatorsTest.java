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
import static org.immunogenomics.gl.AlleleComparators.BY_ACCESSION_ASCENDING;
import static org.immunogenomics.gl.AlleleComparators.BY_ACCESSION_DESCENDING;
import static org.immunogenomics.gl.AlleleComparators.BY_GLSTRING_ASCENDING;
import static org.immunogenomics.gl.AlleleComparators.BY_GLSTRING_DESCENDING;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for AlleleComparators.
 */
public final class AlleleComparatorsTest {
    private final Locus a = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
    private final Locus b = new Locus("http://immunogenomics.org/locus/1", "HLA-B");
    private final Allele a01010101 = new Allele("http://immunogenomics.org/allele/0", "HLA00001", "HLA-A*01:01:01:01", a);
    private final Allele a01010102N = new Allele("http://immunogenomics.org/allele/1", "HLA02169", "HLA-A*01:01:01:02N", a);
    private final Allele a010102 = new Allele("http://immunogenomics.org/allele/2", "HLA01244", "HLA-A*01:01:02", a);
    private final Allele b0710 = new Allele("http://immunogenomics.org/allele/3", "HLA00142", "HLA-B*07:10", b);
    private final Allele b0711G = new Allele("http://immunogenomics.org/allele/4", "HLA00143", "HLA-B*07:11G", b);
    private final Allele b0748 = new Allele("http://immunogenomics.org/allele/5", "HLA02514", "HLA-B*07:48", b);
    private final Allele b0749N = new Allele("http://immunogenomics.org/allele/6", "HLA02519", "HLA-B*07:49N", b);
    private final Allele b0750 = new Allele("http://immunogenomics.org/allele/7", "HLA02560", "HLA-B*07:50", b);
    private final Allele b0795 = new Allele("http://immunogenomics.org/allele/8", "HLA04233", "HLA-B*07:95", b);
    private final Allele b079601 = new Allele("http://immunogenomics.org/allele/9", "HLA4241", "HLA-B*07:96:01", b);
    private final Allele b079602 = new Allele("http://immunogenomics.org/allele/10", "HLA05512", "HLA-B*07:96:02", b);
    private final Allele b0799 = new Allele("http://immunogenomics.org/allele/11", "HLA04250", "HLA-B*07:99", b);
    private final Allele b07100 = new Allele("http://immunogenomics.org/allele/12", "HLA04861", "HLA-B*07:100", b);
    private final List<Allele> unsorted = ImmutableList.of(a01010101, a01010102N, a010102,
        b0710, b0711G, b0748, b0748, b0749N, b0750, b0795, b079601, b079602, b0799, b07100);

    @Test
    public void testByAccessionAscending() {
        List<Allele> sorted = new ArrayList<Allele>(unsorted);
        sorted.add(null);
        Collections.shuffle(sorted);
        Collections.sort(sorted, BY_ACCESSION_ASCENDING);

        assertEquals(a01010101, sorted.get(0));
        assertEquals(b0710, sorted.get(1));
        assertEquals(b0711G, sorted.get(2));
        assertEquals(a010102, sorted.get(3));
        assertEquals(a01010102N, sorted.get(4));
        assertEquals(b0748, sorted.get(5));
        assertEquals(b0748, sorted.get(6));
        assertEquals(b0749N, sorted.get(7));
        assertEquals(b0750, sorted.get(8));
        assertEquals(b0795, sorted.get(9));
        assertEquals(b079601, sorted.get(10));
        assertEquals(b0799, sorted.get(11));
        assertEquals(b07100, sorted.get(12));
        assertEquals(b079602, sorted.get(13));
        assertEquals(null, sorted.get(14));
    }

    @Test
    public void testByAccessionDescending() {
        List<Allele> sorted = new ArrayList<Allele>(unsorted);
        sorted.add(null);
        Collections.shuffle(sorted);
        Collections.sort(sorted, BY_ACCESSION_DESCENDING);

        assertEquals(null, sorted.get(0));
        assertEquals(b079602, sorted.get(1));
        assertEquals(b07100, sorted.get(2));
        assertEquals(b0799, sorted.get(3));
        assertEquals(b079601, sorted.get(4));
        assertEquals(b0795, sorted.get(5));
        assertEquals(b0750, sorted.get(6));
        assertEquals(b0749N, sorted.get(7));
        assertEquals(b0748, sorted.get(8));
        assertEquals(b0748, sorted.get(9));
        assertEquals(a01010102N, sorted.get(10));
        assertEquals(a010102, sorted.get(11));
        assertEquals(b0711G, sorted.get(12));
        assertEquals(b0710, sorted.get(13));
        assertEquals(a01010101, sorted.get(14));
    }

    @Test
    public void testByGlstringAscending() {
        List<Allele> sorted = new ArrayList<Allele>(unsorted);
        sorted.add(null);
        Collections.shuffle(sorted);
        Collections.sort(sorted, BY_GLSTRING_ASCENDING);

        assertEquals(a01010101, sorted.get(0));
        assertEquals(a01010102N, sorted.get(1));
        assertEquals(a010102, sorted.get(2));
        assertEquals(b0710, sorted.get(3));
        assertEquals(b0711G, sorted.get(4));
        assertEquals(b0748, sorted.get(5));
        assertEquals(b0748, sorted.get(6));
        assertEquals(b0749N, sorted.get(7));
        assertEquals(b0750, sorted.get(8));
        assertEquals(b0795, sorted.get(9));
        assertEquals(b079601, sorted.get(10));
        assertEquals(b079602, sorted.get(11));
        assertEquals(b0799, sorted.get(12));
        assertEquals(b07100, sorted.get(13));
        assertEquals(null, sorted.get(14));
    }

    @Test
    public void testByGlstringDescending() {
        List<Allele> sorted = new ArrayList<Allele>(unsorted);
        sorted.add(null);
        Collections.sort(sorted, BY_GLSTRING_DESCENDING);

        assertEquals(null, sorted.get(0));
        assertEquals(b07100, sorted.get(1));
        assertEquals(b0799, sorted.get(2));
        assertEquals(b079602, sorted.get(3));
        assertEquals(b079601, sorted.get(4));
        assertEquals(b0795, sorted.get(5));
        assertEquals(b0750, sorted.get(6));
        assertEquals(b0749N, sorted.get(7));
        assertEquals(b0748, sorted.get(8));
        assertEquals(b0748, sorted.get(9));
        assertEquals(b0711G, sorted.get(10));
        assertEquals(b0710, sorted.get(11));
        assertEquals(a010102, sorted.get(12));
        assertEquals(a01010102N, sorted.get(13));
        assertEquals(a01010101, sorted.get(14));
    }
}