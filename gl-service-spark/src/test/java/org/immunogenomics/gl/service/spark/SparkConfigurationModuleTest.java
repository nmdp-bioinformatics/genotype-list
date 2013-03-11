/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.immunogenomics.gl.service.spark;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.immunogenomics.gl.service.AllowNewAlleles;
import org.immunogenomics.gl.service.AllowNewLoci;
import org.immunogenomics.gl.service.Namespace;
import org.junit.Test;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;

/**
 * Unit test for SparkModule.
 */
public class SparkConfigurationModuleTest {

    private static final String TEST_BASE_URI = "http://localhost:2468";
    /** CONTEXT_PATH should start with a slash */
    private static final String CONTEXT_PATH = "/junit";

    @Test
    public void testConstructor() {
        assertNotNull("default", new SparkConfigurationModule());
        assertNotNull("contextName", new SparkConfigurationModule(CONTEXT_PATH));
    }

    @Test
    public void testDefaultModule() {
        Injector injector = Guice.createInjector(new SparkConfigurationModule());
        // required bindings
        String namespace = injector.getBinding(Key.get(String.class, Namespace.class)).getProvider().get();
        Boolean allowNewAlleles = injector.getBinding(Key.get(Boolean.class, AllowNewAlleles.class)).getProvider().get();
        Boolean allowNewLoci = injector.getBinding(Key.get(Boolean.class, AllowNewLoci.class)).getProvider().get();
        assertEquals("http://localhost:4567/", namespace);
        assertTrue("allowNewAlleles", allowNewAlleles);
        assertTrue("allowNewLoci", allowNewLoci);
    }
    
    @Test
    public void testModuleWithContextName() {
        String oldValue = System.setProperty(SparkConfigurationModule.BASE_URI_NAME, TEST_BASE_URI);
        Injector injector = Guice.createInjector(new SparkConfigurationModule(CONTEXT_PATH));
        checkWithContextPath(injector, oldValue);
    }

    @Test
    public void testModuleWithThreadContext() {
        String oldValue = System.setProperty(SparkConfigurationModule.BASE_URI_NAME, TEST_BASE_URI);
        SparkConfigurationModule.setThreadContext(CONTEXT_PATH);
        Injector injector = Guice.createInjector(new SparkConfigurationModule());
        checkWithContextPath(injector, oldValue);
    }

    private void checkWithContextPath(Injector injector, String oldBaseUri) {
        String namespace = injector.getBinding(Key.get(String.class, Namespace.class)).getProvider().get();
        try {
            Boolean allowNewAlleles = injector.getBinding(Key.get(Boolean.class, AllowNewAlleles.class)).getProvider().get();
            Boolean allowNewLoci = injector.getBinding(Key.get(Boolean.class, AllowNewLoci.class)).getProvider().get();
            String expectedNamespace = TEST_BASE_URI + CONTEXT_PATH + "/";
            assertEquals("namespace", expectedNamespace, namespace);
            assertTrue("allowNewAlleles", allowNewAlleles);
            assertTrue("allowNewLoci", allowNewLoci);
        } finally {
            SparkConfigurationModule.setThreadContext(null);
            if (oldBaseUri != null) {
                System.setProperty(SparkConfigurationModule.BASE_URI_NAME, oldBaseUri);
            }
        }
    }

}
