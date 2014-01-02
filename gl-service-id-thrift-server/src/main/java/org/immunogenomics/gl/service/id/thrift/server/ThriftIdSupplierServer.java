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

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.inject.Inject;

import org.immunogenomics.gl.service.IdSupplier;

import org.immunogenomics.gl.service.id.thrift.ThriftIdSupplier;

/**
 * Thrift identifier supplier server, adapts IdSupplier to ThriftIdSupplier.
 */
final class ThriftIdSupplierServer implements ThriftIdSupplier {
    private final IdSupplier idSupplier;

    @Inject
    ThriftIdSupplierServer(final IdSupplier idSupplier) {
        checkNotNull(idSupplier);
        this.idSupplier = idSupplier;
    }


    @Override
    public String createLocusId() {
        return idSupplier.createLocusId();
    }

    @Override
    public String createAlleleId() {
        return idSupplier.createAlleleId();
    }
    
    @Override
    public String createAlleleListId() {
        return idSupplier.createAlleleListId();
    }

    @Override
    public String createHaplotypeId() {
        return idSupplier.createHaplotypeId();
    }

    @Override
    public String createGenotypeId() {
        return idSupplier.createGenotypeId();
    }

    @Override
    public String createGenotypeListId() {
        return idSupplier.createGenotypeListId();
    }

    @Override
    public String createMultilocusUnphasedGenotypeId() {
        return idSupplier.createMultilocusUnphasedGenotypeId();
    }

    @Override
    public void close() {
        // empty
    }
}