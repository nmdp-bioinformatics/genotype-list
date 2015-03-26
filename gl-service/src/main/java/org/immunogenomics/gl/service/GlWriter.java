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
import java.io.Writer;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Genotype list writer.
 */
public interface GlWriter {

    /**
     * Return the content type for this writer.
     *
     * @return the content type for this writer
     */
    String getContentType();

    /**
     * Write the specified locus to the specified writer.
     *
     * @param locus locus to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeLocus(Locus locus, Writer writer) throws IOException;

    /**
     * Write the specified allele to the specified writer.
     *
     * @param allele allele to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeAllele(Allele allele, Writer writer) throws IOException;

    /**
     * Write the specified allele list to the specified writer.
     *
     * @param alleleList allele list to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeAlleleList(AlleleList alleleList, Writer writer) throws IOException;

    /**
     * Write the specified haplotype to the specified writer.
     *
     * @param haplotype haplotype to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeHaplotype(Haplotype haplotype, Writer writer) throws IOException;

    /**
     * Write the specified genotype to the specified writer.
     *
     * @param genotype genotype to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeGenotype(Genotype genotype, Writer writer) throws IOException;

    /**
     * Write the specified genotype list to the specified writer.
     *
     * @param genotypeList genotype list to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeGenotypeList(GenotypeList genotypeList, Writer writer) throws IOException;

    /**
     * Write the specified multilocus unphased genotype to the specified writer.
     *
     * @param multilocusUnphasedGenotype multilocus unphased genotype to write, must not be null
     * @param writer writer to write to, must not be null
     * @throws IOException if an I/O error occurs
     */
    void writeMultilocusUnphasedGenotype(MultilocusUnphasedGenotype multilocusUnphasedGenotype, Writer writer) throws IOException;
}