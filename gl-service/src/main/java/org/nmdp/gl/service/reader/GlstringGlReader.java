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
package org.nmdp.gl.service.reader;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;
import org.nmdp.gl.service.AllowNewAlleles;
import org.nmdp.gl.service.AllowNewLoci;
import org.nmdp.gl.service.GlReader;
import org.nmdp.gl.service.GlRegistry;
import org.nmdp.gl.service.GlstringResolver;
import org.nmdp.gl.service.IdResolver;

import com.google.inject.Inject;

/**
 * Reader for glstring format.
 */
public final class GlstringGlReader implements GlReader {
    private static final Pattern LOCUS_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]+$");
    private static final Pattern ALLELE_PATTERN = Pattern.compile("^([a-zA-Z0-9-_]+)\\*([a-zA-Z0-9:]+)$");

    private final boolean allowNewLoci;
    private final boolean allowNewAlleles;
    private final GlstringResolver glstringResolver;
    private final IdResolver idResolver;
    private final GlRegistry glRegistry;

    @Inject
    public GlstringGlReader(@AllowNewLoci final boolean allowNewLoci,
                            @AllowNewAlleles final boolean allowNewAlleles,
                            final GlstringResolver glstringResolver,
                            final IdResolver idResolver,
                            final GlRegistry glRegistry) {
        checkNotNull(glstringResolver);
        checkNotNull(idResolver);
        checkNotNull(glRegistry);
        this.allowNewLoci = allowNewLoci;
        this.allowNewAlleles = allowNewAlleles;
        this.glstringResolver = glstringResolver;
        this.idResolver = idResolver;
        this.glRegistry = glRegistry;
    }

    @Override
    public Locus readLocus(final String glstring) throws IOException {
        final String id = glstringResolver.resolveLocus(glstring);
        Locus locus = idResolver.findLocus(id);
        if (locus == null) {
            if (!allowNewLoci) {
                throw new IOException("locus \"" + glstring + "\" not a valid locus");
            }
            Matcher m = LOCUS_PATTERN.matcher(glstring);
            if (m.matches()) {
                locus = new Locus(id, glstring);
                glRegistry.registerLocus(locus);
            }
            else {
                throw new IOException("locus \"" + glstring + "\" not a valid formatted glstring");
            }
        }
        return locus;
    }

    @Override
    public Allele readAllele(final String glstring, final String accession) throws IOException {
        checkNotNull(accession);
        final String id = glstringResolver.resolveAllele(glstring);
        Allele allele = idResolver.findAllele(id);
        if (allele == null) {
            if (!allowNewAlleles) {
                throw new IOException("allele \"" + glstring + "\" not a valid allele");
            }
            Matcher m = ALLELE_PATTERN.matcher(glstring);
            if (m.matches()) {
                String locusPart = m.group(1);
                Locus locus = readLocus(locusPart);
                allele = new Allele(id, accession, glstring, locus);
                glRegistry.registerAllele(allele);
            }
            else {
                throw new IOException("allele \"" + glstring + "\" not a valid formatted glstring");
            }
        }
        return allele;
    }

    @Override
    public AlleleList readAlleleList(final String glstring) throws IOException {
        final String id = glstringResolver.resolveAlleleList(glstring);
        AlleleList alleleList = idResolver.findAlleleList(id);
        if (alleleList == null) {
            String[] split = glstring.split("/");
            List<Allele> alleles = new ArrayList<Allele>(split.length);
            for (String allelePart : split) {
                alleles.add(readAllele(allelePart, ""));
            }
            alleleList = new AlleleList(id, alleles);
            glRegistry.registerAlleleList(alleleList);
        }
        return alleleList;
    }

    @Override
    public Haplotype readHaplotype(final String glstring) throws IOException {
        final String id = glstringResolver.resolveHaplotype(glstring);
        Haplotype haplotype = idResolver.findHaplotype(id);
        if (haplotype == null) {
            String[] split = glstring.split("~");
            List<AlleleList> alleleLists = new ArrayList<AlleleList>(split.length);
            for (String alleleListPart : split) {
                alleleLists.add(readAlleleList(alleleListPart));
            }
            haplotype = new Haplotype(id, alleleLists);
            glRegistry.registerHaplotype(haplotype);
        }
        return haplotype;
    }

    @Override
    public Genotype readGenotype(final String glstring) throws IOException {
        final String id = glstringResolver.resolveGenotype(glstring);
        Genotype genotype = idResolver.findGenotype(id);
        if (genotype == null) {
            String[] split = glstring.split("\\+");
            List<Haplotype> haplotypes = new ArrayList<Haplotype>(split.length);
            for (String part : split) {
                haplotypes.add(readHaplotype(part));
            }
            genotype = new Genotype(id, haplotypes);
            glRegistry.registerGenotype(genotype);
        }
        return genotype;
    }

    @Override
    public GenotypeList readGenotypeList(final String glstring) throws IOException {
        final String id = glstringResolver.resolveGenotypeList(glstring);
        GenotypeList genotypeList = idResolver.findGenotypeList(id);
        if (genotypeList == null) {
            String[] split = glstring.split("\\|");
            List<Genotype> genotypes = new ArrayList<Genotype>(split.length);
            for (String genotypePart : split) {
                genotypes.add(readGenotype(genotypePart));
            }
            genotypeList = new GenotypeList(id, genotypes);
            glRegistry.registerGenotypeList(genotypeList);
        }
        return genotypeList;
    }

    @Override
    public MultilocusUnphasedGenotype readMultilocusUnphasedGenotype(final String glstring) throws IOException {
        final String id = glstringResolver.resolveMultilocusUnphasedGenotype(glstring);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = idResolver.findMultilocusUnphasedGenotype(id);
        if (multilocusUnphasedGenotype == null) {
            String[] split = glstring.split("\\^");
            List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>(split.length);
            for (String genotypeListPart : split) {
                genotypeLists.add(readGenotypeList(genotypeListPart));
            }
            multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(id, genotypeLists);
            glRegistry.registerMultilocusUnphasedGenotype(multilocusUnphasedGenotype);
        }
        return multilocusUnphasedGenotype;
    }
}