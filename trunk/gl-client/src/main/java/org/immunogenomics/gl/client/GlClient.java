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
import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.with;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.google.common.base.Joiner;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.service.Namespace;

/**
 * Client API for creating and registering gl resources.
 */
public final class GlClient {
    private final String namespace;
    private final JsonFactory jsonFactory;

    /**
     * Create a new gl client with the specified namespace.
     *
     * @param namespace namespace for this gl client, must not be null
     * @param jsonFactory JSON factory for this gl client, must not be null
     */
    public GlClient(@Namespace final String namespace, final JsonFactory jsonFactory) {
        checkNotNull(namespace);
        checkNotNull(jsonFactory);
        this.namespace = namespace;
        this.jsonFactory = jsonFactory;
    }


    /**
     * Create and register a locus described by the specified GL String.
     *
     * @param glstring locus in GL String format, must not be null
     * @return a locus described by the specified GL String
     */
    public Locus createLocus(final String glstring) {
        return getLocus(registerLocus(glstring));
    }

    /**
     * Return the locus identified by <code>identifier</code> or null if no such locus exists.
     *
     * @param identifier locus identifier, must not be null
     * @return the locus identified by <code>identifier</code> or null if no such locus exists
     */
    public Locus getLocus(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
            }
            return new Locus(identifier, glstring);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register a locus described by the specified GL String and return its identifier.
     *
     * @param glstring locus in GL String format
     * @return the identifier of the locus described by the specified GL String
     */
    public String registerLocus(final String glstring) {
        return register("locus", glstring);
    }

    /**
     * Create and register an allele described by the specified GL String.
     *
     * @param glstring allele in GL String format, must not be null
     * @return an allele described by the specified GL String
     */
    public Allele createAllele(final String glstring) {
        return getAllele(registerAllele(glstring));
    }

    /**
     * Create and register an allele described by the specified GL String.
     *
     * @param locus locus, must not be null
     * @param glstring allele in GL String format, must not be null
     * @return an allele described by the specified GL String
     */
    public Allele createAllele(final Locus locus, final String glstring) {
        checkNotNull(locus);
        checkNotNull(glstring);
        if (!glstring.startsWith(locus.getGlstring())) {
            throw new IllegalArgumentException("locus " + locus.getGlstring() + " and allele glstring " + glstring + " do not match");
        }
        return createAllele(glstring);
    }

    /**
     * Return the allele identified by <code>identifier</code> or null if no such allele exists.
     *
     * @param identifier allele identifier, must not be null
     * @return the allele identified by <code>identifier</code> or null if no such allele exists
     */
    public Allele getAllele(final String identifier) {
        checkNotNull(identifier);

        String accession = null;
        String glstring = null;
        Locus locus = null;
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("accession".equals(field)) {
                    accession = parser.getText();
                }
                else if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("locus".equals(field)) {
                    String locusId = parser.getText();
                    locus = getLocus(locusId);
                }
            }
            return new Allele(identifier, accession, glstring, locus);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register an allele described by the specified GL String and return its identifier.
     *
     * @param glstring allele in GL String format
     * @return the identifier of the allele described by the specified GL String
     */
    public String registerAllele(final String glstring) {
        return register("allele", glstring);
    }

    /**
     * Create and register an allele list described by the specified GL String.
     *
     * @param glstring allele list in GL String format, must not be null
     * @return an allele list described by the specified GL String
     */
    public AlleleList createAlleleList(final String glstring) {
        return getAlleleList(registerAlleleList(glstring));
    }

    /**
     * Create and register an allele list containing the specified alleles.
     *
     * @param alleles variable number of alleles, must not be null
     * @return an allele list containing the specified alleles
     */
    public AlleleList createAlleleList(final Allele... alleles) {
        return createAlleleList(Joiner.on("/").join(alleles));
    }

    /**
     * Create and register an allele list containing the specified alleles.
     *
     * @param alleles alleles, must not be null
     * @return an allele list containing the specified alleles
     */
    public AlleleList createAlleleList(final Iterable<Allele> alleles) {
        return createAlleleList(Joiner.on("/").join(alleles));
    }

    /**
     * Return the allele list identified by <code>identifier</code> or null if no such allele list exists.
     *
     * @param identifier allele list identifier, must not be null
     * @return the allele list identified by <code>identifier</code> or null if no such allele list exists
     */
    public AlleleList getAlleleList(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        List<Allele> alleles = new ArrayList<Allele>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("allele".equals(field)) {
                    String alleleId = parser.getText();
                    alleles.add(getAllele(alleleId));
                }
            }
            return new AlleleList(identifier, alleles);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register an allele list described by the specified GL String and return its identifier.
     *
     * @param glstring allele list in GL String format
     * @return the identifier of the allele list described by the specified GL String
     */
    public String registerAlleleList(final String glstring) {
        return register("allele-list", glstring);
    }

    /**
     * Create and register a haplotype described by the specified GL String.
     *
     * @param glstring haplotype in GL String format, must not be null
     * @return a haplotype described by the specified GL String
     */
    public Haplotype createHaplotype(final String glstring) {
        return getHaplotype(registerHaplotype(glstring));
    }

    /**
     * Create and register a haplotype containing the specified allele lists.
     *
     * @param alleleLists variable number of allele lists, must not be null
     * @return a haplotype containing the specified allele lists
     */
    public Haplotype createHaplotype(final AlleleList... alleleLists) {
        return createHaplotype(Joiner.on("~").join(alleleLists));
    }

    /**
     * Create and register a haplotype containing the specified allele lists.
     *
     * @param alleleLists allele lists, must not be null
     * @return a haplotype containing the specified allele lists
     */
    public Haplotype createHaplotype(final Iterable<AlleleList> alleleLists) {
        return createHaplotype(Joiner.on("~").join(alleleLists));
    }

    /**
     * Return the haplotype identified by <code>identifier</code> or null if no such haplotype exists.
     *
     * @param identifier haplotype identifier, must not be null
     * @return the haplotype identified by <code>identifier</code> or null if no such haplotype exists
     */
    public Haplotype getHaplotype(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        List<AlleleList> alleleLists = new ArrayList<AlleleList>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("alleleList".equals(field)) {
                    String alleleListId = parser.getText();
                    alleleLists.add(getAlleleList(alleleListId));
                }
            }
            return new Haplotype(identifier, alleleLists);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register a haplotype described by the specified GL String and return its identifier.
     *
     * @param glstring haplotype in GL String format
     * @return the identifier of the haplotype described by the specified GL String
     */
    public String registerHaplotype(final String glstring) {
        return register("haplotype", glstring);
    }

    /**
     * Create and register a genotype described by the specified GL String.
     *
     * @param glstring genotype in GL String format, must not be null
     * @return a genotype described by the specified GL String
     */
    public Genotype createGenotype(final String glstring) {
        return getGenotype(registerGenotype(glstring));
    }

    /**
     * Create and register a genotype containing the specified haplotypes.
     *
     * @param haplotypes variable number of haplotypes, must not be null
     * @return a genotype containing the specified haplotypes
     */
    public Genotype createGenotype(final Haplotype... haplotypes) {
        return createGenotype(Joiner.on("+").join(haplotypes));
    }

    /**
     * Create and register a genotype containing the specified haplotypes.
     *
     * @param haplotypes haplotypes, must not be null
     * @return a genotype containing the specified haplotypes
     */
    public Genotype createGenotype(final Iterable<Haplotype> haplotypes) {
        return createGenotype(Joiner.on("+").join(haplotypes));
    }

    /**
     * Return the genotype identified by <code>identifier</code> or null if no such genotype exists.
     *
     * @param identifier genotype identifier, must not be null
     * @return the genotype identified by <code>identifier</code> or null if no such genotype exists
     */
    public Genotype getGenotype(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        List<Haplotype> haplotypes = new ArrayList<Haplotype>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("haplotype".equals(field)) {
                    String haplotypeId = parser.getText();
                    haplotypes.add(getHaplotype(haplotypeId));
                }
            }
            return new Genotype(identifier, haplotypes);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register a genotype described by the specified GL String and return its identifier.
     *
     * @param glstring genotype in GL String format
     * @return the identifier of the genotype described by the specified GL String
     */
    public String registerGenotype(final String glstring) {
        return register("genotype", glstring);
    }

    /**
     * Create and register a genotype list described by the specified GL String.
     *
     * @param glstring genotype list in GL String format, must not be null
     * @return a genotype list described by the specified GL String
     */
    public GenotypeList createGenotypeList(final String glstring) {
        return getGenotypeList(registerGenotypeList(glstring));
    }

    /**
     * Create and register a genotype list containing the specified genotypes.
     *
     * @param genotypes variable number of genotypes, must not be null
     * @return a genotype list containing the specified genotypes
     */
    public GenotypeList createGenotypeList(final Genotype... genotypes) {
        return createGenotypeList(Joiner.on("|").join(genotypes));
    }

    /**
     * Create and register a genotype list containing the specified genotypes.
     *
     * @param genotypes genotypes, must not be null
     * @return a genotype list containing the specified genotypes
     */
    public GenotypeList createGenotypeList(final Iterable<Genotype> genotypes) {
        return createGenotypeList(Joiner.on("|").join(genotypes));
    }

    /**
     * Return the genotype list identified by <code>identifier</code> or null if no such genotype list exists.
     *
     * @param identifier genotype list identifier, must not be null
     * @return the genotype list identified by <code>identifier</code> or null if no such genotype list exists
     */
    public GenotypeList getGenotypeList(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        List<Genotype> genotypes = new ArrayList<Genotype>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("genotype".equals(field)) {
                    String genotypeId = parser.getText();
                    genotypes.add(getGenotype(genotypeId));
                }
            }
            return new GenotypeList(identifier, genotypes);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register a genotype list described by the specified GL String and return its identifier.
     *
     * @param glstring genotype list in GL String format
     * @return the identifier of the genotype list described by the specified GL String
     */
    public String registerGenotypeList(final String glstring) {
        return register("genotype-list", glstring);
    }

    /**
     * Create and register a multilocus unphased genotype described by the specified GL String.
     *
     * @param glstring multilocus unphased genotype in GL String format, must not be null
     * @return a multilocus unphased genotype described by the specified GL String
     */
    public MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final String glstring) {
        return getMultilocusUnphasedGenotype(registerMultilocusUnphasedGenotype(glstring));
    }

    /**
     * Create and register a multilocus unphased genotype containing the specified genotype lists.
     *
     * @param genotypeLists variable number of genotype lists, must not be null
     * @return a multilocus unphased genotype containing the specified genotype lists
     */
    public MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final GenotypeList... genotypeLists) {
        return createMultilocusUnphasedGenotype(Joiner.on("^").join(genotypeLists));
    }

    /**
     * Create and register a multilocus unphased genotype containing the specified genotype lists.
     *
     * @param genotypeLists genotype lists, must not be null
     * @return a multilocus unphased genotype containing the specified genotype lists
     */
    public MultilocusUnphasedGenotype createMultilocusUnphasedGenotype(final Iterable<GenotypeList> genotypeLists) {
        return createMultilocusUnphasedGenotype(Joiner.on("^").join(genotypeLists));
    }

    /**
     * Return the multilocus unphased genotype identified by <code>identifier</code> or null if no such multilocus unphased genotype exists.
     *
     * @param identifier multilocus unphased genotype identifier, must not be null
     * @return the multilocus unphased genotype identified by <code>identifier</code> or null if no such multilocus unphased genotype exists
     */
    public MultilocusUnphasedGenotype getMultilocusUnphasedGenotype(final String identifier) {
        checkNotNull(identifier);

        String glstring = null;
        List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json").body().asInputStream();
            parser = jsonFactory.createJsonParser(inputStream);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
                else if ("genotypeList".equals(field)) {
                    String genotypeListId = parser.getText();
                    genotypeLists.add(getGenotypeList(genotypeListId));
                }
            }
            return new MultilocusUnphasedGenotype(identifier, genotypeLists);
        }
        catch (IOException e) {
            // log e, might also want to catch IAE
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                parser.close();
            }
            catch (Exception e) {
                // ignored
            }
        }
        return null;
    }

    /**
     * Register a multilocus unphased genotype described by the specified GL String and return its identifier.
     *
     * @param glstring multilocus unphased genotype in GL String format
     * @return the identifier of the multilocus unphased genotype described by the specified GL String
     */
    public String registerMultilocusUnphasedGenotype(final String glstring) {
        return register("multilocus-unphased-genotype", glstring);
    }

    private String register(final String type, final String glstring) {
        checkNotNull(glstring);
        return with().body(glstring).contentType("text/plain").post(namespace + type).getHeader("Location");
    }
}