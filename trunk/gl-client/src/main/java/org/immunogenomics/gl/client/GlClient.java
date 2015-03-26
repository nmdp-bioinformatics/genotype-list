/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Client API for creating and registering gl resources.
 */
public interface GlClient {

    /**
     * Create and register a locus described by the specified GL String.
     *
     * @param glstring locus in GL String format, must not be null
     * @return a locus described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    Locus createLocus(String glstring) throws GlClientException;

    /**
     * Return the locus identified by <code>identifier</code> or null if no such locus exists.
     *
     * @param identifier locus identifier, must not be null
     * @return the locus identified by <code>identifier</code> or null if no such locus exists
     */
    Locus getLocus(String identifier);

    /**
     * Register a locus described by the specified GL String and return its identifier.
     *
     * @param glstring locus in GL String format
     * @return the identifier of the locus described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerLocus(String glstring) throws GlClientException;

    /**
     * Create and register an allele described by the specified GL String.
     *
     * @param glstring allele in GL String format, must not be null
     * @return an allele described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    Allele createAllele(String glstring) throws GlClientException;

    /**
     * Create and register an allele described by the specified GL String.
     *
     * @param locus locus, must not be null
     * @param glstring allele in GL String format, must not be null
     * @return an allele described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    Allele createAllele(Locus locus, String glstring) throws GlClientException;

    /**
     * Return the allele identified by <code>identifier</code> or null if no such allele exists.
     *
     * @param identifier allele identifier, must not be null
     * @return the allele identified by <code>identifier</code> or null if no such allele exists
     */
    Allele getAllele(String identifier);

    /**
     * Register an allele described by the specified GL String and return its identifier.
     *
     * @param glstring allele in GL String format
     * @return the identifier of the allele described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerAllele(String glstring) throws GlClientException;

    /**
     * Create and register an allele list described by the specified GL String.
     *
     * @param glstring allele list in GL String format, must not be null
     * @return an allele list described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    AlleleList createAlleleList(String glstring) throws GlClientException;

    /**
     * Create and register an allele list containing the specified alleles.
     *
     * @param alleles variable number of alleles, must not be null
     * @return an allele list containing the specified alleles
     * @throws GlClientException if an error occurs
     */
    AlleleList createAlleleList(Allele... alleles) throws GlClientException;

    /**
     * Create and register an allele list containing the specified alleles.
     *
     * @param alleles alleles, must not be null
     * @return an allele list containing the specified alleles
     * @throws GlClientException if an error occurs
     */
    AlleleList createAlleleList(Iterable<Allele> alleles) throws GlClientException;

    /**
     * Return the allele list identified by <code>identifier</code> or null if no such allele list exists.
     *
     * @param identifier allele list identifier, must not be null
     * @return the allele list identified by <code>identifier</code> or null if no such allele list exists
     */
    AlleleList getAlleleList(String identifier);

    /**
     * Register an allele list described by the specified GL String and return its identifier.
     *
     * @param glstring allele list in GL String format
     * @return the identifier of the allele list described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerAlleleList(String glstring) throws GlClientException;

    /**
     * Create and register a haplotype described by the specified GL String.
     *
     * @param glstring haplotype in GL String format, must not be null
     * @return a haplotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    Haplotype createHaplotype(String glstring) throws GlClientException;

    /**
     * Create and register a haplotype containing the specified allele lists.
     *
     * @param alleleLists variable number of allele lists, must not be null
     * @return a haplotype containing the specified allele lists
     * @throws GlClientException if an error occurs
     */
    Haplotype createHaplotype(AlleleList... alleleLists) throws GlClientException;

    /**
     * Create and register a haplotype containing the specified allele lists.
     *
     * @param alleleLists allele lists, must not be null
     * @return a haplotype containing the specified allele lists
     * @throws GlClientException if an error occurs
     */
    Haplotype createHaplotype(Iterable<AlleleList> alleleLists) throws GlClientException;

    /**
     * Return the haplotype identified by <code>identifier</code> or null if no such haplotype exists.
     *
     * @param identifier haplotype identifier, must not be null
     * @return the haplotype identified by <code>identifier</code> or null if no such haplotype exists
     */
    Haplotype getHaplotype(String identifier);

    /**
     * Register a haplotype described by the specified GL String and return its identifier.
     *
     * @param glstring haplotype in GL String format
     * @return the identifier of the haplotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerHaplotype(String glstring) throws GlClientException;

    /**
     * Create and register a genotype described by the specified GL String.
     *
     * @param glstring genotype in GL String format, must not be null
     * @return a genotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    Genotype createGenotype(String glstring) throws GlClientException;

    /**
     * Create and register a genotype containing the specified haplotypes.
     *
     * @param haplotypes variable number of haplotypes, must not be null
     * @return a genotype containing the specified haplotypes
     * @throws GlClientException if an error occurs
     */
    Genotype createGenotype(Haplotype... haplotypes) throws GlClientException;

    /**
     * Create and register a genotype containing the specified haplotypes.
     *
     * @param haplotypes haplotypes, must not be null
     * @return a genotype containing the specified haplotypes
     * @throws GlClientException if an error occurs
     */
    Genotype createGenotype(Iterable<Haplotype> haplotypes) throws GlClientException;

    /**
     * Return the genotype identified by <code>identifier</code> or null if no such genotype exists.
     *
     * @param identifier genotype identifier, must not be null
     * @return the genotype identified by <code>identifier</code> or null if no such genotype exists
     */
    Genotype getGenotype(String identifier);
   
    /**
     * Register a genotype described by the specified GL String and return its identifier.
     *
     * @param glstring genotype in GL String format
     * @return the identifier of the genotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerGenotype(String glstring) throws GlClientException;

    /**
     * Create and register a genotype list described by the specified GL String.
     *
     * @param glstring genotype list in GL String format, must not be null
     * @return a genotype list described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    GenotypeList createGenotypeList(String glstring) throws GlClientException;

    /**
     * Create and register a genotype list containing the specified genotypes.
     *
     * @param genotypes variable number of genotypes, must not be null
     * @return a genotype list containing the specified genotypes
     * @throws GlClientException if an error occurs
     */
    GenotypeList createGenotypeList(Genotype... genotypes) throws GlClientException;

    /**
     * Create and register a genotype list containing the specified genotypes.
     *
     * @param genotypes genotypes, must not be null
     * @return a genotype list containing the specified genotypes
     * @throws GlClientException if an error occurs
     */
    GenotypeList createGenotypeList(Iterable<Genotype> genotypes) throws GlClientException;

    /**
     * Return the genotype list identified by <code>identifier</code> or null if no such genotype list exists.
     *
     * @param identifier genotype list identifier, must not be null
     * @return the genotype list identified by <code>identifier</code> or null if no such genotype list exists
     */
    GenotypeList getGenotypeList(String identifier);

    /**
     * Register a genotype list described by the specified GL String and return its identifier.
     *
     * @param glstring genotype list in GL String format
     * @return the identifier of the genotype list described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerGenotypeList(String glstring) throws GlClientException;

    /**
     * Create and register a multilocus unphased genotype described by the specified GL String.
     *
     * @param glstring multilocus unphased genotype in GL String format, must not be null
     * @return a multilocus unphased genotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(String glstring) throws GlClientException;

    /**
     * Create and register a multilocus unphased genotype containing the specified genotype lists.
     *
     * @param genotypeLists variable number of genotype lists, must not be null
     * @return a multilocus unphased genotype containing the specified genotype lists
     * @throws GlClientException if an error occurs
     */
    MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(GenotypeList... genotypeLists) throws GlClientException;

    /**
     * Create and register a multilocus unphased genotype containing the specified genotype lists.
     *
     * @param genotypeLists genotype lists, must not be null
     * @return a multilocus unphased genotype containing the specified genotype lists
     * @throws GlClientException if an error occurs
     */
    MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(Iterable<GenotypeList> genotypeLists) throws GlClientException;

    /**
     * Return the multilocus unphased genotype identified by <code>identifier</code> or null if no such multilocus unphased genotype exists.
     *
     * @param identifier multilocus unphased genotype identifier, must not be null
     * @return the multilocus unphased genotype identified by <code>identifier</code> or null if no such multilocus unphased genotype exists
     */
    MultilocusUnphasedGenotype getMultilocusUnphasedGenotype(String identifier);

    /**
     * Register a multilocus unphased genotype described by the specified GL String and return its identifier.
     *
     * @param glstring multilocus unphased genotype in GL String format
     * @return the identifier of the multilocus unphased genotype described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    String registerMultilocusUnphasedGenotype(String glstring) throws GlClientException;
}