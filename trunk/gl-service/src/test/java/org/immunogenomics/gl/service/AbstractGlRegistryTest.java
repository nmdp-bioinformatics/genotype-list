/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import com.google.common.collect.ImmutableList;

/**
 * Abstract unit test for implementations of GlRegistry.
 */
public abstract class AbstractGlRegistryTest {
    protected Locus locus;
    protected Allele allele;
    protected AlleleList alleleList0;
    protected AlleleList alleleList1;
    protected Haplotype haplotype0;
    protected Haplotype haplotype1;
    protected Haplotype haplotype;
    protected Genotype genotype;
    protected GenotypeList genotypeList0;
    protected GenotypeList genotypeList1;
    protected MultilocusUnphasedGenotype multilocusUnphasedGenotype;
    protected GlRegistry glRegistry;
    protected abstract GlRegistry createGlRegistry();

    @Before
    public void setUp() {
        locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        allele = new Allele("http://immunogenomics.org/allele/0", "A01234", "HLA-A*01:01:01:01", locus);
        alleleList0 = new AlleleList("http://immunogenomics.org/allele-list/0", allele);
        alleleList1 = new AlleleList("http://immunogenomics.org/allele-list/1", allele);
        haplotype0 = new Haplotype("http://immunogenomics.org/haplotype/0", alleleList0);
        haplotype1 = new Haplotype("http://immunogenomics.org/haplotype/1", alleleList1);
        haplotype = new Haplotype("http://immunogenomics.org/haplotype/2", ImmutableList.of(alleleList0, alleleList1));
        genotype = new Genotype("http://immunogenomics.org/genotype/0", haplotype0);
        genotypeList0 = new GenotypeList("http://immunogenomics.org/genotype-list/0", genotype);
        genotypeList1 = new GenotypeList("http://immunogenomics.org/genotype-list/1", genotype);
        multilocusUnphasedGenotype = new MultilocusUnphasedGenotype("http://immunogenomics.org/multilocus-unphased-genotype/0", ImmutableList.of(genotypeList0, genotypeList1));
        glRegistry = createGlRegistry();
    }

    @Test
    public void testCreateGlRegistry() {
        assertNotNull(glRegistry);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullLocus() {
        glRegistry.registerLocus(null);
    }

    @Test
    public void testRegisterLocus() {
        glRegistry.registerLocus(locus);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullAllele() {
        glRegistry.registerAllele(null);
    }

    @Test
    public void testRegisterAllele() {
        glRegistry.registerAllele(allele);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullAlleleList() {
        glRegistry.registerAlleleList(null);
    }

    @Test
    public void testRegisterAlleleList() {
        glRegistry.registerAlleleList(alleleList0);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullHaplotype() {
        glRegistry.registerHaplotype(null);
    }

    @Test
    public void testRegisterHaplotype() {
        glRegistry.registerHaplotype(haplotype);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullGenotype() {
        glRegistry.registerGenotype(null);
    }

    @Test
    public void testRegisterGenotype() {
        glRegistry.registerGenotype(genotype);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullGenotypeList() {
        glRegistry.registerGenotypeList(null);
    }

    @Test
    public void testRegisterGenotypeList() {
        glRegistry.registerGenotypeList(genotypeList0);
    }

    @Test(expected=NullPointerException.class)
    public void testRegisterNullMultilocusUnphasedGenotype() {
        glRegistry.registerMultilocusUnphasedGenotype(null);
    }

    @Test
    public void testRegisterMultilocusUnphasedGenotype() {
        glRegistry.registerMultilocusUnphasedGenotype(multilocusUnphasedGenotype);
    }
}