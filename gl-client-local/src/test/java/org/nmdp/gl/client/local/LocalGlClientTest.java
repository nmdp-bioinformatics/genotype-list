/*

    gl-client-local  Local client library for the URI-based RESTful service for the gl project.
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
package org.nmdp.gl.client.local;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.inject.Injector;
import com.google.inject.Guice;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Locus;

import org.nmdp.gl.client.AbstractGlClientTest;
import org.nmdp.gl.client.GlClient;

import org.nmdp.gl.service.nomenclature.hla.ImgtHla3_19_0;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit test for LocalGlClient.
 */
public final class LocalGlClientTest extends AbstractGlClientTest {

    @Override
    protected GlClient createGlClient() {
        return LocalGlClient.create();
    }

    @Test
    public void testCreate() throws Exception {
        assertEquals(null, client.getLocus("http://localhost/locus/0"));
        Locus locus = client.createLocus("HLA-A");
        assertEquals("HLA-A", locus.getGlstring());
        assertEquals("http://localhost/locus/0", locus.getId());
        assertEquals(locus, client.getLocus("http://localhost/locus/0"));
        
        assertNull(client.getAllele("http://localhost/allele/0"));
        Allele allele = client.createAllele("HLA-A*01:01:01:01");
        assertEquals("HLA-A*01:01:01:01", allele.getGlstring());
        assertEquals("http://localhost/allele/0", allele.getId());
        assertEquals(allele, client.getAllele("http://localhost/allele/0"));

        assertNull(client.getAlleleList("http://localhost/allele-list/0"));
        AlleleList alleleList = client.createAlleleList("HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
        assertNotNull(alleleList);
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals("http://localhost/allele-list/0", alleleList.getId());
        assertEquals(alleleList, client.getAlleleList("http://localhost/allele-list/0"));
    }

    @Test(expected=NullPointerException.class)
    public void testCreateStrictNullNomenclatureClass() throws Exception {
        LocalGlClient.createStrict(null);
    }

    @Test
    public void testCreateStrictNomenclature() throws Exception {
        GlClient strict = LocalGlClient.createStrict(ImgtHla3_19_0.class);

        Locus locus = strict.getLocus("http://localhost/locus/0");
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
        assertEquals("http://localhost/locus/0", locus.getId());
        assertEquals(locus, strict.getLocus("http://localhost/locus/0"));
        
        Allele allele = strict.getAllele("http://localhost/allele/0");
        assertNotNull(allele);
        assertEquals("HLA-A*01:01:01G", allele.getGlstring());
        assertEquals("http://localhost/allele/0", allele.getId());
        assertEquals(allele, strict.getAllele("http://localhost/allele/0"));

        assertNull(strict.getAlleleList("http://localhost/allele-list/0"));
        AlleleList alleleList = strict.createAlleleList("HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
        assertNotNull(alleleList);
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals("http://localhost/allele-list/0", alleleList.getId());
        Assert.assertEquals(alleleList, strict.getAlleleList("http://localhost/allele-list/0"));
    }

    @Test
    public void testCreateStrict() throws Exception {
        GlClient strict = LocalGlClient.createStrict();

        Locus locus = strict.getLocus("http://localhost/locus/0");
        assertNotNull(locus);
        assertEquals("HLA-A", locus.getGlstring());
        assertEquals("http://localhost/locus/0", locus.getId());
        assertEquals(locus, strict.getLocus("http://localhost/locus/0"));
        
        Allele allele = strict.getAllele("http://localhost/allele/0");
        assertNotNull(allele);
        assertEquals("HLA-A*01:01:01G", allele.getGlstring());
        assertEquals("http://localhost/allele/0", allele.getId());
        assertEquals(allele, strict.getAllele("http://localhost/allele/0"));

        assertNull(strict.getAlleleList("http://localhost/allele-list/0"));
        AlleleList alleleList = strict.createAlleleList("HLA-A*01:01:01:01/HLA-A*01:01:01:02N");
        assertNotNull(alleleList);
        assertEquals("HLA-A*01:01:01:01/HLA-A*01:01:01:02N", alleleList.getGlstring());
        assertEquals("http://localhost/allele-list/0", alleleList.getId());
        Assert.assertEquals(alleleList, strict.getAlleleList("http://localhost/allele-list/0"));
    }

    @Test
    public void testGetLocalModules() {
        Injector injector = Guice.createInjector(LocalGlClient.getLocalModules());
        assertNotNull(injector.getInstance(GlClient.class));
    }

    @Test(expected=NullPointerException.class)
    public void testGetLocalStrictModulesNullNomenclatureClass() {
        LocalGlClient.getLocalStrictModules(null);
    }

    @Test
    public void testGetLocalStrictModules() {
        Injector injector = Guice.createInjector(LocalGlClient.getLocalStrictModules(ImgtHla3_19_0.class));
        assertNotNull(injector.getInstance(GlClient.class));
    }

    @Test
    public void testGetLocalStrictImgtHlaModules() {
        Injector injector = Guice.createInjector(LocalGlClient.getLocalStrictImgtHlaModules());
        assertNotNull(injector.getInstance(GlClient.class));
    }
}
