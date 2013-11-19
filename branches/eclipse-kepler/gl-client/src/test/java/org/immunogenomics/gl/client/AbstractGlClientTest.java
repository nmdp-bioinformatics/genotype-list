/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;

/**
 * Abstract unit test for implementations of GlClient.
 */
public abstract class AbstractGlClientTest {
    protected GlClient client;

    protected abstract GlClient createGlClient();

    @Before
    public void setUp() {
        client = createGlClient();
    }

    @Test
    public final void testCreateGlClient() {
        assertNotNull(client);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateLocusNullGlstring() throws GlClientException {
        client.createLocus((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetLocusNullIdentifier() {
        client.getLocus(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterLocusNullGlstring() throws GlClientException {
        client.registerLocus(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateAlleleNullLocus() throws GlClientException {
        client.createAllele(null, "HLA-A*01:01:01:01");
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateAlleleNullGlstring() throws GlClientException {
        Locus locus = new Locus("http://localhost:8080/locus/0", "HLA-A");
        client.createAllele(locus, null);
    }

    @Test(expected=IllegalArgumentException.class)
    public final void testCreateAlleleLocusAndGlstringDoNotMatch() throws GlClientException {
        Locus locus = new Locus("http://localhost:8080/locus/0", "HLA-A");
        client.createAllele(locus, "HLA-B*02:07:01");
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateAlleleGlstringNullGlstring() throws GlClientException {
        client.createAllele(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetAlleleNullIdentifier() throws GlClientException {
        client.getAllele(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterAlleleNullGlstring() throws GlClientException {
        client.registerAllele(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateAlleleListNullAlleles() throws GlClientException {
        client.createAlleleList((Allele) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateAlleleListNullGlstring() throws GlClientException {
        client.createAlleleList((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetAlleleListNullIdentifier() throws GlClientException {
        client.getAlleleList(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterAlleleListNullGlstring() throws GlClientException {
        client.registerAlleleList(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateHaplotypeNullAlleleLists() throws GlClientException {
        client.createHaplotype((AlleleList) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateHaplotypeNullGlstring() throws GlClientException {
        client.createHaplotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetHaplotypeNullIdentifier() {
        client.getHaplotype(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterHaplotypeNullGlstring() throws GlClientException {
        client.registerHaplotype(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateGenotypeNullHaplotypes() throws GlClientException {
        client.createGenotype((Haplotype) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateGenotypeNullGlstring() throws GlClientException {
        client.createGenotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetGenotypeNullIdentifier() {
        client.getGenotype(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterGenotypeNullGlstring() throws GlClientException {
        client.registerGenotype(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateGenotypeListNullGenotypes() throws GlClientException {
        client.createGenotypeList((Genotype) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateGenotypeListNullGlstring() throws GlClientException {
        client.createGenotypeList((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetGenotypeListNullIdentifier() throws GlClientException {
        client.getGenotypeList(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterGenotypeListNullGlstring() throws GlClientException {
        client.registerGenotypeList(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateMultilocusUnphasedGenotypeNullGenotypeLists() throws GlClientException {
        client.createMultilocusUnphasedGenotype((GenotypeList) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testCreateMultilocusUnphasedGenotypeNullGlstring() throws GlClientException {
        client.createMultilocusUnphasedGenotype((String) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetMultilocusUnphasedGenotypeNullIdentifier() throws GlClientException {
        client.getMultilocusUnphasedGenotype(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterMultilocusUnphasedGenotypeNullGlstring() throws GlClientException {
        client.registerMultilocusUnphasedGenotype(null);
    }
}
