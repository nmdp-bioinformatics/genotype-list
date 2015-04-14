/*

    gl-liftover-service  Genotype list liftover service.
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
package org.nmdp.gl.liftover.impl;

import com.google.common.cache.LoadingCache;

import com.google.common.collect.Table;

import org.nmdp.gl.client.GlClient;

import org.nmdp.gl.liftover.AbstractLiftoverServiceTest;
import org.nmdp.gl.liftover.LiftoverService;

import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for LiftoverServiceImpl.
 */
public final class LiftoverServiceImplTest extends AbstractLiftoverServiceTest {
    @Mock
    private LoadingCache<String, GlClient> clients;

    @Mock
    private Table<String, String, String> locusNames;

    @Mock
    private Table<String, String, String> alleleNames;

    @Override
    protected LiftoverService createLiftoverService() {
        return new LiftoverServiceImpl(clients, locusNames, alleleNames);
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }
}