/*

    gl-service-id-thrift  Distributed identifier supplier implementation based on Thrift.
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
package org.immunogenomics.gl.service.id.thrift;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.net.HostAndPort.fromParts;

import java.util.concurrent.ExecutionException;

import com.facebook.nifty.client.FramedClientConnector;

import com.facebook.swift.service.ThriftClient;

import com.google.inject.Inject;

import org.immunogenomics.gl.service.IdSupplier;

/**
 * Thrift identifier supplier client.
 */
final class ThriftIdSupplierClient implements IdSupplier {
    private final ThriftClient<ThriftIdSupplier> thriftClient;
    private final FramedClientConnector connector = new FramedClientConnector(fromParts("localhost", 8899));

    @Inject
    ThriftIdSupplierClient(final ThriftClient<ThriftIdSupplier> thriftClient) {
        checkNotNull(thriftClient);
        this.thriftClient = thriftClient;
    }


    @Override
    public String createLocusId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createLocusId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        // this breaks the IdSupplier API contract
        return null;
    }

    @Override
    public String createAlleleId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createAlleleId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }
    
    @Override
    public String createAlleleListId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createAlleleListId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }

    @Override
    public String createHaplotypeId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createHaplotypeId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }

    @Override
    public String createGenotypeId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createGenotypeId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }

    @Override
    public String createGenotypeListId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createGenotypeListId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }

    @Override
    public String createMultilocusUnphasedGenotypeId() {
        try (ThriftIdSupplier idSupplier = thriftClient.open(connector).get()) {
            return idSupplier.createMultilocusUnphasedGenotypeId();
        }
        catch (ExecutionException e) {
            // ignore
        }
        catch (InterruptedException e) {
            // ignore
        }
        catch (Exception e) {
            // ignore
        }
        return null;
    }
}