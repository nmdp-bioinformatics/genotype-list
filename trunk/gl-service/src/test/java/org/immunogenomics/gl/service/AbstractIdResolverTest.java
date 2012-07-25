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
package org.immunogenomics.gl.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Abstract unit test for implementations of IdResolver.
 */
public abstract class AbstractIdResolverTest {
    protected IdResolver idResolver;
    protected String validLocusId = "http://immunogenomics.org/locus/0";
    protected String invalidLocusId = "http://immunogenomics.org/locus/99";
    protected String validAlleleId = "http://immunogenomics.org/allele/0";
    protected String invalidAlleleId = "http://immunogenomics.org/allele/99";
    protected String validAlleleListId = "http://immunogenomics.org/allele-list/0";
    protected String invalidAlleleListId = "http://immunogenomics.org/allele-list/99";
    protected String validHaplotypeId = "http://immunogenomics.org/haplotype/0";
    protected String invalidHaplotypeId = "http://immunogenomics.org/haplotype/99";
    protected String validGenotypeId = "http://immunogenomics.org/genotype/0";
    protected String invalidGenotypeId = "http://immunogenomics.org/genotype/99";
    protected String validGenotypeListId = "http://immunogenomics.org/genotype-list/0";
    protected String invalidGenotypeListId = "http://immunogenomics.org/genotype-list/99";
    protected String validMultilocusUnphasedGenotypeId = "http://immunogenomics.org/multilocus-unphased-genotype/0";
    protected String invalidMultilocusUnphasedGenotypeId  = "http://immunogenomics.org/multilocus-unphased-genotype/99";
    protected abstract IdResolver createIdResolver();

    @Before
    public void setUp() {
        idResolver = createIdResolver();
    }

    @Test
    public void testCreateIdResolver() {
        assertNotNull(idResolver);
    }

    @Test
    public void testFindLocus() {
        Locus locus = idResolver.findLocus(validLocusId);
        assertNotNull(locus);
        assertEquals(validLocusId, locus.getId());
    }

    @Test
    public void testFindLocusDoesNotExist() {
        assertNull(idResolver.findLocus(invalidLocusId));
    }

    @Test
    public void testFindAllele() {
        Allele allele = idResolver.findAllele(validAlleleId);
        assertNotNull(allele);
        assertEquals(validAlleleId, allele.getId());
    }

    @Test
    public void testFindAlleleDoesNotExist() {
        assertNull(idResolver.findAllele(invalidAlleleId));
    }

    @Test
    public void testFindAlleleList() {
        AlleleList alleleList = idResolver.findAlleleList(validAlleleListId);
        assertNotNull(alleleList);
        assertEquals(validAlleleListId, alleleList.getId());
    }

    @Test
    public void testFindAlleleListDoesNotExist() {
        assertNull(idResolver.findAlleleList(invalidAlleleListId));
    }

    @Test
    public void testFindHaplotype() {
        Haplotype haplotype = idResolver.findHaplotype(validHaplotypeId);
        assertNotNull(haplotype);
        assertEquals(validHaplotypeId, haplotype.getId());
    }

    @Test
    public void testFindHaplotypeDoesNotExist() {
        assertNull(idResolver.findHaplotype(invalidHaplotypeId));
    }

    @Test
    public void testFindGenotype() {
        Genotype genotype = idResolver.findGenotype(validGenotypeId);
        assertNotNull(genotype);
        assertEquals(validGenotypeId, genotype.getId());
    }

    @Test
    public void testFindGenotypeDoesNotExist() {
        assertNull(idResolver.findGenotype(invalidGenotypeId));
    }

    @Test
    public void testFindGenotypeList() {
        GenotypeList genotypeList = idResolver.findGenotypeList(validGenotypeListId);
        assertNotNull(genotypeList);
        assertEquals(validGenotypeListId, genotypeList.getId());
    }

    @Test
    public void testFindGenotypeListDoesNotExist() {
        assertNull(idResolver.findGenotypeList(invalidGenotypeListId));
    }

    @Test
    public void testFindMultilocusUnphasedGenotype() {
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = idResolver.findMultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId);
        assertNotNull(multilocusUnphasedGenotype);
        assertEquals(validMultilocusUnphasedGenotypeId, multilocusUnphasedGenotype.getId());
    }

    @Test
    public void testFindMultilocusUnphasedGenotypeDoesNotExist() {
        assertNull(idResolver.findMultilocusUnphasedGenotype(invalidMultilocusUnphasedGenotypeId));
    }
}