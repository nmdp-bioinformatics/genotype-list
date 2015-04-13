/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.nmdp.gl.service.spark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.nmdp.gl.service.AllowNewAlleles;
import org.nmdp.gl.service.AllowNewLoci;
import org.nmdp.gl.service.Namespace;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Unit test for SparkConfigurationModule.
 */
public class SparkConfigurationModuleTest {
    private SparkConfigurationModule sparkConfigurationModule;

    @Before
    public void setUp() {
        sparkConfigurationModule = new SparkConfigurationModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(sparkConfigurationModule);
    }

    @Test
    public void testSparkConfigurationModule() {
        Injector injector = Guice.createInjector(sparkConfigurationModule);
        assertNotNull(injector);
        assertEquals("http://localhost:4567/", injector.getInstance(Key.get(String.class, Namespace.class)));
        assertEquals(Boolean.TRUE, injector.getInstance(Key.get(Boolean.class, AllowNewAlleles.class)));
        assertEquals(Boolean.TRUE, injector.getInstance(Key.get(Boolean.class, AllowNewLoci.class)));
    }
}
