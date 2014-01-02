/*

    gl-service-id-thrift-server  Thrift distributed identifier supplier server.
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
package org.immunogenomics.gl.service.id.thrift.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.service.IdSupplier;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for ThriftIdSupplierServer.
 */
public final class ThriftIdSupplierServerTest {
    private ThriftIdSupplierServer server;

    @Mock
    private IdSupplier idSupplier;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        server = new ThriftIdSupplierServer(idSupplier);
    }

    @Test
    public void testConstructor() {
        assertNotNull(server);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdSupplier() {
        new ThriftIdSupplierServer(null);
    }

    @Test
    public void testCreateLocusId() {
        when(idSupplier.createLocusId()).thenReturn("locusId");
        assertEquals("locusId", server.createLocusId());
    }

    @Test
    public void testCreateAlleleId() {
        when(idSupplier.createAlleleId()).thenReturn("alleleId");
        assertEquals("alleleId", server.createAlleleId());
    }

    @Test
    public void testCreateAlleleListId() {
        when(idSupplier.createAlleleListId()).thenReturn("alleleListId");
        assertEquals("alleleListId", server.createAlleleListId());
    }

    @Test
    public void testCreateHaplotypeId() {
        when(idSupplier.createHaplotypeId()).thenReturn("haplotypeId");
        assertEquals("haplotypeId", server.createHaplotypeId());
    }

    @Test
    public void testCreateGenotypeId() {
        when(idSupplier.createGenotypeId()).thenReturn("genotypeId");
        assertEquals("genotypeId", server.createGenotypeId());
    }

    @Test
    public void testCreateGenotypeListId() {
        when(idSupplier.createGenotypeListId()).thenReturn("genotypeListId");
        assertEquals("genotypeListId", server.createGenotypeListId());
    }

    @Test
    public void testCreateMultilocusUnphasedGenotypeId() {
        when(idSupplier.createMultilocusUnphasedGenotypeId()).thenReturn("multilocusUnphasedGenotypeId");
        assertEquals("multilocusUnphasedGenotypeId", server.createMultilocusUnphasedGenotypeId());
    }
}