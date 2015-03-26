/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service;

import java.io.IOException;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Genotype list reader.
 */
public interface GlReader {

    /**
     * Read a locus from the specified string.
     *
     * @param value locus to read, must not be null
     * @return a locus read from the specified string
     * @throws IOException if an I/O error occurs
     */
    Locus readLocus(String value) throws IOException;

    /**
     * Read an allele from the specified string.
     *
     * @param value allele to read, must not be null
     * @param accession allele accession, must not be null
     * @return an allele read from the specified string
     * @throws IOException if an I/O error occurs
     */
    Allele readAllele(String value, String accession) throws IOException;

    /**
     * Read an allele list from the specified string.
     *
     * @param value allele list to read, must not be null
     * @return an allele list read from the specified string
     * @throws IOException if an I/O error occurs
     */
    AlleleList readAlleleList(String value) throws IOException;

    /**
     * Read a haplotype from the specified string.
     *
     * @param value haplotype to read, must not be null
     * @return a haplotype read from the specified string
     * @throws IOException if an I/O error occurs
     */
    Haplotype readHaplotype(String value) throws IOException;

    /**
     * Read a genotype from the specified string.
     *
     * @param value genotype to read, must not be null
     * @return a genotype read from the specified string
     * @throws IOException if an I/O error occurs
     */
    Genotype readGenotype(String value) throws IOException;

    /**
     * Read a genotype list from the specified string.
     *
     * @param value genotype list to read, must not be null
     * @return a genotype list read from the specified string
     * @throws IOException if an I/O error occurs
     */
    GenotypeList readGenotypeList(String value) throws IOException;

    /**
     * Read a multilocus unphased genotype from the specified string.
     *
     * @param value multilocus unphased genotype to read, must not be null
     * @return a multilocus unphased genotype read from the specified string
     * @throws IOException if an I/O error occurs
     */
    MultilocusUnphasedGenotype readMultilocusUnphasedGenotype(String value) throws IOException;
}