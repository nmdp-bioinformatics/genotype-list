/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client;

import static com.google.common.base.Preconditions.checkNotNull;

import com.google.common.base.Joiner;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

/**
 * Abstract gl client.
 */
public abstract class AbstractGlClient implements GlClient {

    @Override
    public final Locus createLocus(final String glstring) throws GlClientException {
        return getLocus(registerLocus(glstring));
    }

    @Override
    public final Allele createAllele(final String glstring) throws GlClientException {
        return getAllele(registerAllele(glstring));
    }

    @Override
    public final Allele createAllele(final Locus locus, final String glstring) throws GlClientException {
        checkNotNull(locus);
        checkNotNull(glstring);
        if (!glstring.startsWith(locus.getGlstring())) {
            throw new IllegalArgumentException("locus " + locus.getGlstring() + " and allele glstring " + glstring + " do not match");
        }
        return createAllele(glstring);
    }

    @Override
    public final AlleleList createAlleleList(final String glstring) throws GlClientException {
        return getAlleleList(registerAlleleList(glstring));
    }

    @Override
    public final AlleleList createAlleleList(final Allele... alleles) throws GlClientException {
        return createAlleleList(Joiner.on("/").join(alleles));
    }

    @Override
    public final AlleleList createAlleleList(final Iterable<Allele> alleles) throws GlClientException {
        return createAlleleList(Joiner.on("/").join(alleles));
    }

    @Override
    public final Haplotype createHaplotype(final String glstring) throws GlClientException {
        return getHaplotype(registerHaplotype(glstring));
    }

    @Override
    public final Haplotype createHaplotype(final AlleleList... alleleLists) throws GlClientException {
        return createHaplotype(Joiner.on("~").join(alleleLists));
    }

    @Override
    public final Haplotype createHaplotype(final Iterable<AlleleList> alleleLists) throws GlClientException {
        return createHaplotype(Joiner.on("~").join(alleleLists));
    }

    @Override
    public final Genotype createGenotype(final String glstring) throws GlClientException {
        return getGenotype(registerGenotype(glstring));
    }

    @Override
    public final Genotype createGenotype(final Haplotype... haplotypes) throws GlClientException {
        return createGenotype(Joiner.on("+").join(haplotypes));
    }

    @Override
    public final Genotype createGenotype(final Iterable<Haplotype> haplotypes) throws GlClientException {
        return createGenotype(Joiner.on("+").join(haplotypes));
    }

    @Override
    public final GenotypeList createGenotypeList(final String glstring) throws GlClientException {
        return getGenotypeList(registerGenotypeList(glstring));
    }

    @Override
    public final GenotypeList createGenotypeList(final Genotype... genotypes) throws GlClientException {
        return createGenotypeList(Joiner.on("|").join(genotypes));
    }

    @Override
    public final GenotypeList createGenotypeList(final Iterable<Genotype> genotypes) throws GlClientException {
        return createGenotypeList(Joiner.on("|").join(genotypes));
    }

    @Override
    public final MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final String glstring) throws GlClientException {
        return getMultilocusUnphasedGenotype(registerMultilocusUnphasedGenotype(glstring));
    }

    @Override
    public final MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final GenotypeList... genotypeLists) throws GlClientException {
        return createMultilocusUnphasedGenotype(Joiner.on("^").join(genotypeLists));
    }

    @Override
    public final MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final Iterable<GenotypeList> genotypeLists) throws GlClientException {
        return createMultilocusUnphasedGenotype(Joiner.on("^").join(genotypeLists));
    }
}