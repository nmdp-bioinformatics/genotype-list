/*

    gl-service-id-thrift  Distributed identifier supplier implementation based on Thrift.
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
package org.immunogenomics.gl.service.id.thrift;

import static com.facebook.swift.service.guice.ThriftClientBinder.thriftClientBinder;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Singleton;

import org.immunogenomics.gl.service.IdSupplier;

/**
 * Thrift identifier supplier module.
 */
public final class ThriftIdSupplierModule implements Module {

    @Override
    public void configure(final Binder binder) {
        // client
        thriftClientBinder(binder).bindThriftClient(ThriftIdSupplier.class);

        // client --> IdSupplier
        binder.bind(IdSupplier.class).to(ThriftIdSupplierClient.class).in(Singleton.class);
    }
}