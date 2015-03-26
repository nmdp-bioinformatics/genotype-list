/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.immunogenomics.gl.service.spark;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.service.AllowNewAlleles;
import org.immunogenomics.gl.service.AllowNewLoci;
import org.immunogenomics.gl.service.Namespace;
import org.immunogenomics.gl.service.cache.CacheModule;
import org.immunogenomics.gl.service.id.IdModule;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * Unit test for SparkModule.
 */
public class SparkModuleTest {
    private SparkModule sparkModule;

    @Before
    public void setUp() {
        sparkModule = new SparkModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(sparkModule);
    }

    @Test
    public void testSparkModule() {
        Injector injector = Guice.createInjector(new ConfigurationModule(), new IdModule(), new CacheModule(), sparkModule);
        assertNotNull(injector);
    }

    /**
     * Configuration module.
     */
    private static class ConfigurationModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Namespace.class).toInstance("namespace");
            bind(Boolean.class).annotatedWith(AllowNewAlleles.class).toInstance(Boolean.TRUE);
            bind(Boolean.class).annotatedWith(AllowNewLoci.class).toInstance(Boolean.TRUE);
        }
    }
}
