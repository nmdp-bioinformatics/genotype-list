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

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Genotype list identifier --&gt; &lt;T&gt; resolver.
 */
public interface IdResolver {

    /**
     * Resolve the specified identifier to a locus, if such exists.
     *
     * @param id locus identifier to resolve
     * @return the locus for the specified identifier, or <code>null</code> if no such locus exists
     */
    Locus findLocus(String id);

    /**
     * Resolve the specified identifier to an allele, if such exists.
     *
     * @param id allele identifier to resolve
     * @return the allele for the specified identifier, or <code>null</code> if no such allele exists
     */
    Allele findAllele(String id);

    /**
     * Resolve the specified identifier to an allele list, if such exists.
     *
     * @param id allele list identifier to resolve
     * @return the allele list for the specified identifier, or <code>null</code> if no such allele list exists
     */
    AlleleList findAlleleList(String id);

    /**
     * Resolve the specified identifier to a locus, if such exists.
     *
     * @param id locus identifier to resolve
     * @return the locus for the specified identifier, or <code>null</code> if no such locus exists
     */
    Haplotype findHaplotype(String id);

    /**
     * Resolve the specified identifier to a genotype, if such exists.
     *
     * @param id genotype identifier to resolve
     * @return the genotype for the specified identifier, or <code>null</code> if no such genotype exists
     */
    Genotype findGenotype(String id);

    /**
     * Resolve the specified identifier to a genotype list, if such exists.
     *
     * @param id genotype list identifier to resolve
     * @return the genotype list for the specified identifier, or <code>null</code> if no such genotype list exists
     */
    GenotypeList findGenotypeList(String id);

    /**
     * Resolve the specified identifier to a multilocus unphased genotype, if such exists.
     *
     * @param id multilocus unphased genotype identifier to resolve
     * @return the multilocus unphased genotype for the specified identifier, or <code>null</code> if no such multilocus
     *    unphased genotype exists
     */
    MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(String id);
}