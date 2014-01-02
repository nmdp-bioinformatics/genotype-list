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

import com.facebook.swift.codec.guice.ThriftCodecModule;

import com.facebook.swift.service.ThriftServer;
import com.facebook.swift.service.guice.ThriftServerModule;

import com.google.common.collect.ImmutableMap;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Stage;
import com.google.inject.TypeLiteral;

import io.airlift.configuration.ConfigurationFactory;
import io.airlift.configuration.ConfigurationModule;

import org.immunogenomics.gl.service.Namespace;
import org.immunogenomics.gl.service.id.IdModule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Main.
 */
public final class Main {
    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Main.
     *
     * @param args command line arguments, ignored
     */
    public static void main(final String[] args) {
        Injector injector = Guice.createInjector(Stage.PRODUCTION,
                                                 new LocalhostModule(),
                                                 new IdModule(),
                                                 new ConfigurationModule(new ConfigurationFactory(ImmutableMap.<String, String>of())),
                                                 new ThriftCodecModule(),
                                                 new ThriftServerModule(),
                                                 new ThriftIdSupplierServerModule());

        ThriftServer thriftServer = injector.getInstance(ThriftServer.class);
        logger.info("server starting...");
        thriftServer.start();
        logger.info("server started");
    }

    /**
     * Localhost module.
     */
    private static class LocalhostModule extends AbstractModule {

        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://localhost:10080/gl");
        }
    }
}