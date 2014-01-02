/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service;

/**
 * Identifier supplier.
 */
public interface IdSupplier {

    /**
     * Create and return a new locus identifier.
     *
     * @return a new locus identifier
     */
    String createLocusId();

    /**
     * Create and return a new allele identifier.
     *
     * @return a new allele identifier
     */
    String createAlleleId();

    /**
     * Create and return a new allele list identifier.
     *
     * @return a new allele list identifier
     */
    String createAlleleListId();

    /**
     * Create and return a new haplotype identifier.
     *
     * @return a new haplotype identifier
     */
    String createHaplotypeId();

    /**
     * Create and return a new genotype identifier.
     *
     * @return a new genotype identifier
     */
    String createGenotypeId();

    /**
     * Create and return a new genotype list identifier.
     *
     * @return a new genotype list identifier
     */
    String createGenotypeListId();

    /**
     * Create and return a new multilocus unphased genotype identifier.
     *
     * @return a new multilocus unphased genotype identifier
     */
    String createMultilocusUnphasedGenotypeId();
}