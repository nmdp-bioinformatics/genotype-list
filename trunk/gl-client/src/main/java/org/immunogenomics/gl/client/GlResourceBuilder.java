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

/**
 * Fluent builder-style client API for creating and registering gl resources.
 */
public final class GlResourceBuilder {
    /*
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

    GlResourceBuilder locus(final String glstring) {
        alleles.clear();
        alleleLists.clear();
        haplotypes.clear();
        genotypes.clear();
        genotypeLists.clear();
        locus = client.createLocus(glstring);
    }

    GlResourceBuilder allele(final String glstring) {
        allele = client.createAllele(glstring);
        locus = allele.getLocus();
    }

    GlResourceBuilder allelicAmbiguity() {
        alleles.add(allele);
        alleleList = client.createAlleleList(alleles);
    }

    GlResourceBuilder inPhase() {
        alleles.clear();
        alleleLists.add(alleleList);
        haplotype = client.createHaplotype(alleleLists);
    }

    GlResourceBuilder phaseAmbiguity() {
        alleles.clear();
        alleleLists.add(alleleList);
        haplotypes.add(haplotype);
        genotype = client.createGenotype(alleleLists, haplotypes);
    }

    GlResourceBuilder genotypicAmbiguity() {
        alleles.clear();
        alleleLists.clear();
        haplotypes.clear();
        genotypes.add(genotype);
        genotypeList = client.createGenotypeList(genotypes);
    }

    Locus buildLocus() {
        if (locus == null) {
            throw new IllegalStateException("must call locus(String) or allele(String) at least once");
        }
        return locus;
    }

    Allele buildAllele() {
        if (allele == null) {
            throw new IllegalStateException("must call allele(String) at least once");
        }
        return allele;
    }

    AlleleList buildAlleleList() {
        return client.createAlleleList(alleles);
    }

    Haplotype buildHaplotype() {
        return client.createHaplotype(alleleLists);
    }

    Genotype buildGenotype() {
        return client.createGenotype(alleleLists, haplotypes);
    }

    GenotypeList buildGenotypeList() {
        return client.createGenotypeList(genotypes);
    }

    MultilocusUnphasedGenotype buildMultilocusUnphasedGenotype() {
        return client.createMultilocusUnphasedGenotype(genotypeLists);
    }
    */
}