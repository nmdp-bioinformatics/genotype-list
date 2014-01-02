/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.immunogenomics.gl.service.jdbc;

import javax.sql.DataSource;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.service.AbstractGlRegistryTest;
import org.immunogenomics.gl.service.GlRegistry;

/**
 * Unit test for JdbcGlRegistry.
 */
public final class JdbcGlRegistryTest extends AbstractGlRegistryTest {
    @Mock
    private DataSource dataSource;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        super.setUp();
    }

    @Override
    protected GlRegistry createGlRegistry() {
        return new JdbcGlRegistry(dataSource);
    }
}