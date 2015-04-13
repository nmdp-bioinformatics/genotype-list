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
package org.nmdp.gl.service;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;

/**
 * Genotype list registry.
 */
public interface GlRegistry {
    // todo:  or Notify this listener/observer the specified locus has been created

    /**
     * Register the specified locus.
     *
     * @param locus locus to register, must not be null
     */
    void registerLocus(Locus locus);

    /**
     * Register the specified allele.
     *
     * @param allele allele to register, must not be null
     */
    void registerAllele(Allele allele);

    /**
     * Register the specified allele list.
     *
     * @param alleleList allele list to register, must not be null
     */
    void registerAlleleList(AlleleList alleleList);

    /**
     * Register the specified haplotype.
     *
     * @param haplotype haplotype to register, must not be null
     */
    void registerHaplotype(Haplotype haplotype);

    /**
     * Register the specified genotype.
     *
     * @param genotype genotype to register, must not be null
     */
    void registerGenotype(Genotype genotype);

    /**
     * Register the specified genotype list.
     *
     * @param genotypeList genotype list to register, must not be null
     */
    void registerGenotypeList(GenotypeList genotypeList);

    /**
     * Register the specified multilocus unphased genotype.
     *
     * @param multilocusUnphasedGenotype multilocus unphased genotype to register, must not be null
     */
    void registerMultilocusUnphasedGenotype(MultilocusUnphasedGenotype multilocusUnphasedGenotype);
}