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

import static org.junit.Assert.assertNotNull;

import com.facebook.swift.codec.guice.ThriftCodecModule;

import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.guice.ThriftServerModule;

import com.google.common.collect.ImmutableMap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;

import io.airlift.configuration.ConfigurationFactory;
import io.airlift.configuration.ConfigurationModule;

import org.junit.Before;
import org.junit.Test;

import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.service.Namespace;
import org.immunogenomics.gl.service.id.IdModule;

/**
 * Unit test for ThriftIdSupplierServerModule.
 */
public final class ThriftIdSupplierServerModuleTest {
    private ThriftIdSupplierServerModule thriftIdSupplierServerModule;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        thriftIdSupplierServerModule = new ThriftIdSupplierServerModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(thriftIdSupplierServerModule);
    }

    @Test
    public void testThriftIdSupplierServerModule() {
        Injector injector = Guice.createInjector(Stage.PRODUCTION,
                                                 new TestModule(),
                                                 new IdModule(),
                                                 new ConfigurationModule(new ConfigurationFactory(ImmutableMap.<String, String>of())),
                                                 new ThriftCodecModule(),
                                                 new ThriftServerModule(),
                                                 thriftIdSupplierServerModule);
        assertNotNull(injector);

        ThriftServer thriftServer = injector.getInstance(ThriftServer.class);
        assertNotNull(thriftServer);
    }

    /**
     * Test module.
     */
    private static class TestModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://localhost:10080/gl");
        }
    }
}