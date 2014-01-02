/*

    gl-ambiguity-service  Genotype list ambiguity service.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.ambiguity.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import com.google.common.collect.ImmutableList;

import org.dishevelled.bitset.ImmutableBitSet;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.client.GlClient;

/**
 * Unit test for GlAmbiguityService.
 */
public final class GlAmbiguityServiceTest {
    private Locus locus = new Locus("http://localhost:10080/gl/locus/0", "HLA-A");
    private Allele allele0 = new Allele("http://localhost:10080/gl/allele/0", "A1234", "HLA-A*01:01:01:01", locus);
    private Allele allele1 = new Allele("http://localhost:10080/gl/allele/1", "A1235", "HLA-A*01:01:01:02N", locus);
    private Allele allele2 = new Allele("http://localhost:10080/gl/allele/2", "A1236", "HLA-A*01:01:02:01", locus);
    private Allele allele3 = new Allele("http://localhost:10080/gl/allele/3", "A1237", "HLA-A*01:01:02:02", locus);
    private List<Allele> alleles = ImmutableList.of(allele0, allele1, allele2, allele3);
    private GlAmbiguityService ambiguityService;

    @Mock
    private GlClient client;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        ambiguityService = new GlAmbiguityService(client, alleles);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullClient() {
        new GlAmbiguityService(null, alleles);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullAlleles() {
        new GlAmbiguityService(client, null);
    }

    @Test
    public void testConstructor() {
        assertNotNull(ambiguityService);
    }

    @Test(expected=NullPointerException.class)
    public void testAsBitsNullAllele() {
        ambiguityService.asBits((Allele) null);
    }

    @Test
    public void testAsBitsUnknownAllele() {
        Allele unknown = new Allele("http://localhost:10080/gl/allele/99", "A9999", "HLA-A*99:99:99:99", locus);
        // or should return empty bits?
        assertNull(ambiguityService.asBits(unknown));
    }

    @Test
    public void testAllelesAsBits() {
        assertTrue(ambiguityService.asBits(allele0).get(0));
        assertFalse(ambiguityService.asBits(allele0).get(1));
        assertFalse(ambiguityService.asBits(allele0).get(2));
        assertFalse(ambiguityService.asBits(allele0).get(3));

        assertFalse(ambiguityService.asBits(allele1).get(0));
        assertTrue(ambiguityService.asBits(allele1).get(1));
        assertFalse(ambiguityService.asBits(allele1).get(2));
        assertFalse(ambiguityService.asBits(allele1).get(3));

        assertFalse(ambiguityService.asBits(allele2).get(0));
        assertFalse(ambiguityService.asBits(allele2).get(1));
        assertTrue(ambiguityService.asBits(allele2).get(2));
        assertFalse(ambiguityService.asBits(allele2).get(3));

        assertFalse(ambiguityService.asBits(allele3).get(0));
        assertFalse(ambiguityService.asBits(allele3).get(1));
        assertFalse(ambiguityService.asBits(allele3).get(2));
        assertTrue(ambiguityService.asBits(allele3).get(3));
    }

    @Test(expected=NullPointerException.class)
    public void testAsBitsNullAlleleList() {
        ambiguityService.asBits((AlleleList) null);
    }

    @Test
    public void testSingletonAlleleListsAsBits() {
        AlleleList alleleList0 = new AlleleList("http://localhost:10080/allele-list/0", ImmutableList.of(allele0));

        assertTrue(ambiguityService.asBits(alleleList0).get(0));
        assertFalse(ambiguityService.asBits(alleleList0).get(1));
        assertFalse(ambiguityService.asBits(alleleList0).get(2));
        assertFalse(ambiguityService.asBits(alleleList0).get(3));

        AlleleList alleleList1 = new AlleleList("http://localhost:10080/allele-list/1", ImmutableList.of(allele1));

        assertFalse(ambiguityService.asBits(alleleList1).get(0));
        assertTrue(ambiguityService.asBits(alleleList1).get(1));
        assertFalse(ambiguityService.asBits(alleleList1).get(2));
        assertFalse(ambiguityService.asBits(alleleList1).get(3));

        AlleleList alleleList2 = new AlleleList("http://localhost:10080/allele-list/2", ImmutableList.of(allele2));

        assertFalse(ambiguityService.asBits(alleleList2).get(0));
        assertFalse(ambiguityService.asBits(alleleList2).get(1));
        assertTrue(ambiguityService.asBits(alleleList2).get(2));
        assertFalse(ambiguityService.asBits(alleleList2).get(3));

        AlleleList alleleList3 = new AlleleList("http://localhost:10080/allele-list/3", ImmutableList.of(allele3));

        assertFalse(ambiguityService.asBits(alleleList3).get(0));
        assertFalse(ambiguityService.asBits(alleleList3).get(1));
        assertFalse(ambiguityService.asBits(alleleList3).get(2));
        assertTrue(ambiguityService.asBits(alleleList3).get(3));
    }

    @Test
    public void testAlleleListsAsBits() {
        AlleleList alleleList0 = new AlleleList("http://localhost:10080/allele-list/0", ImmutableList.of(allele0, allele1));

        assertTrue(ambiguityService.asBits(alleleList0).get(0));
        assertTrue(ambiguityService.asBits(alleleList0).get(1));
        assertFalse(ambiguityService.asBits(alleleList0).get(2));
        assertFalse(ambiguityService.asBits(alleleList0).get(3));

        AlleleList alleleList1 = new AlleleList("http://localhost:10080/allele-list/1", ImmutableList.of(allele0, allele1, allele2));

        assertTrue(ambiguityService.asBits(alleleList1).get(0));
        assertTrue(ambiguityService.asBits(alleleList1).get(1));
        assertTrue(ambiguityService.asBits(alleleList1).get(2));
        assertFalse(ambiguityService.asBits(alleleList1).get(3));

        AlleleList alleleList2 = new AlleleList("http://localhost:10080/allele-list/2", ImmutableList.of(allele0, allele1, allele2, allele3));

        assertTrue(ambiguityService.asBits(alleleList2).get(0));
        assertTrue(ambiguityService.asBits(alleleList2).get(1));
        assertTrue(ambiguityService.asBits(alleleList2).get(2));
        assertTrue(ambiguityService.asBits(alleleList2).get(3));
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAllelicAmbiguityNullName() {
        ambiguityService.registerAllelicAmbiguity(null, "HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAllelicAmbiguityNullGlstring() {
        ambiguityService.registerAllelicAmbiguity("HLA-A*01", null);
    }

    @Test
    public void testRegisterAllelicAmbiguity() {
        when(client.getAlleleList(eq("HLA-A*01:01:01:01/HLA-A*01:01:01:02N"))).thenReturn(new AlleleList("http://localhost:10080/allele-list/0", ImmutableList.of(allele0, allele1)));

        AlleleList alleleList = ambiguityService.registerAllelicAmbiguity("HLA-A*01", "HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
        assertNotNull(alleleList);
        assertTrue(alleleList.getAlleles().contains(allele0));
        assertTrue(alleleList.getAlleles().contains(allele1));
        assertFalse(alleleList.getAlleles().contains(allele2));
        assertFalse(alleleList.getAlleles().contains(allele3));
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAllelicAmbiguityAsBitsNullName() {
        ambiguityService.registerAllelicAmbiguityAsBits(null, "HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterAllelicAmbiguityAsBitsNullGlstring() {
        ambiguityService.registerAllelicAmbiguityAsBits("HLA-A*01", null);
    }

    @Test
    public void testRegisterAllelicAmbiguityAsBits() {
        when(client.getAlleleList(eq("HLA-A*01:01:01:01/HLA-A*01:01:01:02N"))).thenReturn(new AlleleList("http://localhost:10080/allele-list/0", ImmutableList.of(allele0, allele1)));

        ImmutableBitSet bits = ambiguityService.registerAllelicAmbiguityAsBits("HLA-A*01", "HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
        assertNotNull(bits);
        assertTrue(bits.get(0));
        assertTrue(bits.get(1));
        assertFalse(bits.get(2));
        assertFalse(bits.get(3));
    }
}