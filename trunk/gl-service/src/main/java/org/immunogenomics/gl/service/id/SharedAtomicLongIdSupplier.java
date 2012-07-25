/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.id;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.concurrent.Immutable;

import com.google.inject.Inject;

import org.immunogenomics.gl.service.IdSupplier;
import org.immunogenomics.gl.service.Namespace;

/**
 * Shared {@link AtomicLong} identifier supplier.
 */
@Immutable
final class SharedAtomicLongIdSupplier implements IdSupplier {
    private final String ns;
    private final AtomicLong id = new AtomicLong();

    @Inject
    SharedAtomicLongIdSupplier(@Namespace final String ns) {
        checkNotNull(ns);
        this.ns = ns;
    }

    @Override
    public String createLocusId() {
        return ns + "locus/" + id.getAndIncrement();
    }

    @Override
    public String createAlleleId() {
        return ns + "allele/" + id.getAndIncrement();
    }

    @Override
    public String createAlleleListId() {
        return ns + "allele-list/" + id.getAndIncrement();
    }

    @Override
    public String createHaplotypeId() {
        return ns + "haplotype/" + id.getAndIncrement();
    }

    @Override
    public String createGenotypeId() {
        return ns + "genotype/" + id.getAndIncrement();
    }

    @Override
    public String createGenotypeListId() {
        return ns + "genotype-list/" + id.getAndIncrement();
    }

    @Override
    public String createMultilocusUnphasedGenotypeId() {
        return ns + "multilocus-unphased-genotype/" + id.getAndIncrement();
    }
}