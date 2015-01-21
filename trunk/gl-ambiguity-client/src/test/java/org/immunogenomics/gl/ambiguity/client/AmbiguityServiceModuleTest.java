/*

    gl-ambiguity-client  Client library for RESTful genotype list ambiguity service.
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
package org.immunogenomics.gl.ambiguity.client;

import static org.junit.Assert.assertNotNull;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Guice;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for AmbiguityServiceModule.
 */
public final class AmbiguityServiceModuleTest {
    private AmbiguityServiceModule module;

    @Before
    public void setUp() {
        module = new AmbiguityServiceModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(module);
    }

    @Test
    public void testModule() {
        Injector injector = Guice.createInjector(new TestModule(), new AmbiguityServiceModule());
        assertNotNull(injector.getInstance(AmbiguityService.class));
    }

    /**
     * Test module.
     */
    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(EndpointUrl.class).toInstance("http://localhost");
        }
    }
}
