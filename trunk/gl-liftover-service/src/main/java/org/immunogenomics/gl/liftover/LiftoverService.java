/*

    gl-liftover-service  Genotype list liftover service.
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
package org.immunogenomics.gl.liftover;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Genotype list liftover service.
 */
public interface LiftoverService {

    /**
     * Liftover the specified source locus to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceLocusUri source locus URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new locus registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    Locus liftoverLocus(String sourceNamespace, String sourceLocusUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source allele to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceAlleleUri source allele URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new allele registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    Allele liftoverAllele(String sourceNamespace, String sourceAlleleUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source allele list to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceAlleleListUri source allele list URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new allele list registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    AlleleList liftoverAlleleList(String sourceNamespace, String sourceAlleleListUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source haplotype to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceHaplotypeUri source haplotype URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new haplotype registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    Haplotype liftoverHaplotype(String sourceNamespace, String sourceHaplotypeUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source genotype to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceGenotypeUri source genotype URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new genotype registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    Genotype liftoverGenotype(String sourceNamespace, String sourceGenotypeUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source genotype list to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceGenotypeListUri source genotype list URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new genotype list registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    GenotypeList liftoverGenotypeList(String sourceNamespace, String sourceGenotypeListUri, String targetNamespace) throws LiftoverServiceException;

    /**
     * Liftover the specified source multilocus unphased genotype to the target namespace.
     *
     * @param sourceNamespace source namespace, must not be null
     * @param sourceMultilocusUnphasedGenotypeUri source multilocus unphased genotype URI, must not be null
     * @param targetNamespace target namespace, must not be null
     * @return a new multilocus unphased genotype registered in the target namespace
     * @throws LiftoverServiceException if an error occurs
     */
    MultilocusUnphasedGenotype liftoverMultilocusUnphasedGenotype(String sourceNamespace, String sourceMultilocusUnphasedGenotypeUri, String targetNamespace) throws LiftoverServiceException;
}