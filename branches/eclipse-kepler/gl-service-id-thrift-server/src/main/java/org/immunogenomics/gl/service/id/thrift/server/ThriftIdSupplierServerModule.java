/*

    gl-service-id-thrift-server  Thrift distributed identifier supplier server.
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
package org.immunogenomics.gl.service.id.thrift.server;

import static com.facebook.swift.service.guice.ThriftServiceExporter.thriftServerBinder;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Scopes;

/**
 * Thrift identifier supplier server module.
 */
public final class ThriftIdSupplierServerModule implements Module {

    @Override
    public void configure(final Binder binder) {
        binder.bind(ThriftIdSupplierServer.class).in(Scopes.SINGLETON);
        thriftServerBinder(binder).exportThriftService(ThriftIdSupplierServer.class);
    }
}