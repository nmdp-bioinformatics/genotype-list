/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

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
     *
     * @param glstring locus in GL String format, must not be null
     * @return this gl resource builder configured with the specified locus
     */
    public GlResourceBuilder locus(final String glstring) {
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
     * be interspersed by calls to operator methods ({@link #allicAmbiguity()}, {@link #inPhase()},
     * {@link #xxx()}, {@link #genotypicAmbiguity()}, and {@link #locus(String)}).
     *
     * @param glstring allele in GL String format, must not be null
     * @return this gl resource builder configured with the specified allele
     */
    public GlResourceBuilder allele(final String glstring) {
        allele = client.createAllele(glstring);
        locus = allele.getLocus();
        return this;
    }

    /**
     * Return this gl resource builder configured with an allelic ambiguity operator ('<code>/</code>' character).
     *
     * @return this gl resource builder configured with an allelic ambiguity operator ('<code>/</code>' character)
     */
    public GlResourceBuilder allelicAmbiguity() {
        alleles.add(allele);
        alleleList = client.createAlleleList(alleles);
        return this;
    }

    /**
     * Return this gl resource builder configured with an in phase operator ('<code>~</code>' character).
     *
     * @return this gl resource builder configured with an in phase operator ('<code>~</code>' character)
     */
    public GlResourceBuilder inPhase() {
        alleles.clear();
        alleleLists.add(alleleList);
        haplotype = client.createHaplotype(alleleLists);
        return this;
    }

    /**
     * Return this gl resource builder configured with an xxx operator ('<code>+</code>' character).
     *
     * @return this gl resource builder configured with an xxx operator ('<code>+</code>' character)
     */
    public GlResourceBuilder xxx() {
        alleles.clear();
        alleleLists.clear();
        haplotypes.add(haplotype);
        genotype = client.createGenotype(haplotypes);
        return this;
    }

    /**
     * Return this gl resource builder configured with a genotypic ambiguity operator ('<code>|</code>' character).
     *
     * @return this gl resource builder configured with a genotypic ambiguity operator ('<code>|</code>' character)
     */
    public GlResourceBuilder genotypicAmbiguity() {
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
    public GlstringBuilder reset() {
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
    public Allele buildAllele() {
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
     */
    public AlleleList buildAlleleList() {
        return client.createAlleleList(alleles);
    }

    /**
     * Build and return a new haplotype configured from the properties of this gl resource builder.
     *
     * @return a new haplotype configured from the properties of this gl resource builder
     */
    public Haplotype buildHaplotype() {
        return client.createHaplotype(alleleLists);
    }

    /**
     * Build and return a new genotype configured from the properties of this gl resource builder.
     *
     * @return a new genotype configured from the properties of this gl resource builder
     */
    public Genotype buildGenotype() {
        return client.createGenotype(haplotypes);
    }

    /**
     * Build and return a new genotype list configured from the properties of this gl resource builder.
     *
     * @return a new genotype list configured from the properties of this gl resource builder
     */
    public GenotypeList buildGenotypeList() {
        return client.createGenotypeList(genotypes);
    }

    /**
     * Build and return a new multilocus unphased genotype configured from the properties of this gl resource builder.
     *
     * @return a new multilocus unphased genotype configured from the properties of this gl resource builder
     */
    public MultilocusUnphasedGenotype buildMultilocusUnphasedGenotype() {
        return client.createMultilocusUnphasedGenotype(genotypeLists);
    }
}