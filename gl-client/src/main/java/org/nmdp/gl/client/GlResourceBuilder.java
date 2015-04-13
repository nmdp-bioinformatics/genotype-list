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
package org.nmdp.gl.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;

/**
 * Fluent builder-style client API for creating and registering gl resources.
 */
public final class GlResourceBuilder {
    private GlClient client;
    private Locus locus;
    private Allele allele;
    private List<Allele> alleles = new ArrayList<Allele>();
    private AlleleList alleleList;
    private List<AlleleList> alleleLists = new ArrayList<AlleleList>();
    private Haplotype haplotype;
    private List<Haplotype> haplotypes = new ArrayList<Haplotype>();
    private Genotype genotype;
    private List<Genotype> genotypes = new ArrayList<Genotype>();
    private GenotypeList genotypeList;
    private List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>();


    /**
     * Create a new gl resource builder with the specified client.
     *
     * @param client gl client, must not be null
     */
    public GlResourceBuilder(final GlClient client) {
        checkNotNull(client);
        this.client = client;
    }


    /**
     * Return this gl resource builder configured with the specified locus.  If an allele has
     * already been added to this builder, this call provides the locus operator ('<code>^</code>' character).
     * The locus operator combines two or more genotype lists into a multilocus unphased genotype.
     *
     * @param glstring locus in GL String format, must not be null
     * @return this gl resource builder configured with the specified locus
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder locus(final String glstring) throws GlClientException {
        alleles.clear();
        alleleLists.clear();
        haplotypes.clear();
        genotypes.clear();
        genotypeLists.clear();
        locus = client.createLocus(glstring);
        return this;
    }

    /**
     * Return this gl resource builder configured with the specified allele. Calls to this method must
     * be interspersed by calls to operator methods ({@link #allelicAmbiguity()}, {@link #inPhase()},
     * {@link #plus()}, {@link #genotypicAmbiguity()}, and {@link #locus(String)}).
     *
     * @param glstring allele in GL String format, must not be null
     * @return this gl resource builder configured with the specified allele
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder allele(final String glstring) throws GlClientException {
        allele = client.createAllele(glstring);
        locus = allele.getLocus();
        return this;
    }

    /**
     * Return this gl resource builder configured with an allelic ambiguity operator ('<code>/</code>' character).
     * The allelic ambiguity operator combines two or more alleles into an allele list.
     *
     * @return this gl resource builder configured with an allelic ambiguity operator ('<code>/</code>' character)
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder allelicAmbiguity() throws GlClientException {
        alleles.add(allele);
        alleleList = client.createAlleleList(alleles);
        return this;
    }

    /**
     * Return this gl resource builder configured with an in phase operator ('<code>~</code>' character).
     * The in phase operator combines two or more allele lists into a haplotype.
     *
     * @return this gl resource builder configured with an in phase operator ('<code>~</code>' character)
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder inPhase() throws GlClientException {
        alleles.clear();
        alleleLists.add(alleleList);
        haplotype = client.createHaplotype(alleleLists);
        return this;
    }

    /**
     * Return this gl resource builder configured with an plus operator ('<code>+</code>' character).  The
     * plus operator combines two ore more haplotypes into a genotype.
     *
     * @return this gl resource builder configured with an plus operator ('<code>+</code>' character)
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder plus() throws GlClientException {
        alleles.clear();
        alleleLists.clear();
        haplotypes.add(haplotype);
        genotype = client.createGenotype(haplotypes);
        return this;
    }

    /**
     * Return this gl resource builder configured with a genotypic ambiguity operator ('<code>|</code>' character).
     * The genotypic ambiguity operator combines two or more genotypes into a genotype list.
     *
     * @return this gl resource builder configured with a genotypic ambiguity operator ('<code>|</code>' character)
     * @throws GlClientException if an error occurs
     */
    public GlResourceBuilder genotypicAmbiguity() throws GlClientException {
        alleles.clear();
        alleleLists.clear();
        haplotypes.clear();
        genotypes.add(genotype);
        genotypeList = client.createGenotypeList(genotypes);
        return this;
    }

    /**
     * Return this gl resource builder with its configuration reset.
     *
     * @return this gl resource builder with its configuration reset
     */
    public GlResourceBuilder reset() {
        alleles.clear();
        alleleLists.clear();
        haplotypes.clear();
        genotypes.clear();
        genotypeLists.clear();
        locus = null;
        allele = null;
        alleleList = null;
        haplotype = null;
        genotype = null;
        genotypeList = null;
        return this;
    }

    /**
     * Build and return a new locus configured from the properties of this gl resource builder.
     *
     * @return a new locus configured from the properties of this gl resource builder
     */
    public Locus buildLocus() {
        if (locus == null) {
            throw new IllegalStateException("must call locus(String) or allele(String) at least once");
        }
        return locus;
    }

    /**
     * Build and return a new allele configured from the properties of this gl resource builder.
     *
     * @return a new allele configured from the properties of this gl resource builder
     */
    public Allele buildAllele() throws GlClientException {
        if (allele == null) {
            throw new IllegalStateException("must call allele(String) at least once");
        }
        return allele;
    }

    // todo:  higher level build methods don't create singletons as necessary
    /**
     * Build and return a new allele list configured from the properties of this gl resource builder.
     *
     * @return a new allele list configured from the properties of this gl resource builder
     * @throws GlClientException if an error occurs
     */
    public AlleleList buildAlleleList() throws GlClientException {
        return client.createAlleleList(alleles);
    }

    /**
     * Build and return a new haplotype configured from the properties of this gl resource builder.
     *
     * @return a new haplotype configured from the properties of this gl resource builder
     * @throws GlClientException if an error occurs
     */
    public Haplotype buildHaplotype() throws GlClientException {
        return client.createHaplotype(alleleLists);
    }

    /**
     * Build and return a new genotype configured from the properties of this gl resource builder.
     *
     * @return a new genotype configured from the properties of this gl resource builder
     * @throws GlClientException if an error occurs
     */
    public Genotype buildGenotype() throws GlClientException {
        return client.createGenotype(haplotypes);
    }

    /**
     * Build and return a new genotype list configured from the properties of this gl resource builder.
     *
     * @return a new genotype list configured from the properties of this gl resource builder
     * @throws GlClientException if an error occurs
     */
    public GenotypeList buildGenotypeList() throws GlClientException {
        return client.createGenotypeList(genotypes);
    }

    /**
     * Build and return a new multilocus unphased genotype configured from the properties of this gl resource builder.
     *
     * @return a new multilocus unphased genotype configured from the properties of this gl resource builder
     * @throws GlClientException if an error occurs
     */
    public MultilocusUnphasedGenotype buildMultilocusUnphasedGenotype() throws GlClientException {
        return client.createMultilocusUnphasedGenotype(genotypeLists);
    }
}