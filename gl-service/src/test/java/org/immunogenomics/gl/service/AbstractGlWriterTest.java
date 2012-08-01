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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.io.StringWriter;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import com.google.common.collect.ImmutableList;

/**
 * Abstract unit test for implementations of GlWriter.
 */
public abstract class AbstractGlWriterTest {
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
    protected GlWriter glWriter;
    protected abstract GlWriter createGlWriter();

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
        glWriter = createGlWriter();
    }

    @Test
    public void testCreateGlWriter() {
        assertNotNull(glWriter);
    }

    @Test
    public void testContentType() {
        assertNotNull(glWriter.getContentType());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteLocusNullLocus() throws Exception {
        glWriter.writeLocus(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteLocusNullWriter() throws Exception {
        glWriter.writeLocus(locus, null);
    }

    @Test
    public void testWriteLocus() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeLocus(locus, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteAlleleNullAllele() throws Exception {
        glWriter.writeAllele(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteAlleleNullWriter() throws Exception {
        glWriter.writeAllele(allele, null);
    }

    @Test
    public void testWriteAllele() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeAllele(allele, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteAlleleListNullAlleleList() throws Exception {
        glWriter.writeAlleleList(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteAlleleListNullWriter() throws Exception {
        glWriter.writeAlleleList(alleleList0, null);
    }

    @Test
    public void testWriteAlleleList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeAlleleList(alleleList0, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteHaplotypeNullHaplotype() throws Exception {
        glWriter.writeHaplotype(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteHaplotypeNullWriter() throws Exception {
        glWriter.writeHaplotype(haplotype, null);
    }

    @Test
    public void testWriteHaplotype() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeHaplotype(haplotype, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteGenotypeNullGenotype() throws Exception {
        glWriter.writeGenotype(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteGenotypeNullWriter() throws Exception {
        glWriter.writeGenotype(genotype, null);
    }

    @Test
    public void testWriteGenotype() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeGenotype(genotype, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteGenotypeListNullGenotypeList() throws Exception {
        glWriter.writeGenotypeList(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteGenotypeListNullWriter() throws Exception {
        glWriter.writeGenotypeList(genotypeList0, null);
    }

    @Test
    public void testWriteGenotypeList() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeGenotypeList(genotypeList0, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }

    @Test(expected=NullPointerException.class)
    public void testWriteMultilocusUnphasedGenotypeNullMultilocusUnphasedGenotype() throws Exception {
        glWriter.writeMultilocusUnphasedGenotype(null, new StringWriter());
    }

    @Test(expected=NullPointerException.class)
    public void testWriteMultilocusUnphasedGenotypeNullWriter() throws Exception {
        glWriter.writeMultilocusUnphasedGenotype(multilocusUnphasedGenotype, null);
    }

    @Test
    public void testWriteMultilocusUnphasedGenotype() throws Exception {
        StringWriter stringWriter = new StringWriter();
        glWriter.writeMultilocusUnphasedGenotype(multilocusUnphasedGenotype, stringWriter);
        String result = stringWriter.toString();
        assertNotNull(result);
        assertTrue(result.length() > 0);
    }
}