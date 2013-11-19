/*

    gl-service-voldemort  Implementation of persistent cache for gl-service using Voldemort.
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
package org.immunogenomics.gl.service.voldemort;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.immunogenomics.gl.service.AbstractGlRegistryTest;
import org.immunogenomics.gl.service.GlRegistry;

import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;

/**
 * Unit test for VoldemortGlRegistry.
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public final class VoldemortGlRegistryTest extends AbstractGlRegistryTest {
    @Mock
    private StoreClient storeClient;
    @Mock
    private StoreClientFactory storeClientFactory;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(storeClientFactory.getStoreClient(anyString())).thenReturn(storeClient);
        super.setUp();
    }

    @Override
    protected GlRegistry createGlRegistry() {
        return new VoldemortGlRegistry(storeClientFactory);
    }
}