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
package org.immunogenomics.gl.client.json;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.client.cache.CacheGlClient;

import org.immunogenomics.gl.service.Namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Implementation of GlClient that uses the JSON representation and RestAssured as the HTTP client.
 */
public final class JsonGlClient extends CacheGlClient {
    private final String namespace;
    private final JsonFactory jsonFactory;
    private final Logger logger = LoggerFactory.getLogger(JsonGlClient.class);


    /**
     * Create a new JSON gl client with the specified namespace.
     *
     * @param namespace namespace for this JSON gl client, must not be null
     * @param jsonFactory JSON factory for this JSON gl client, must not be null
     */
    //@Inject
    public JsonGlClient(@Namespace final String namespace, final JsonFactory jsonFactory) {
        checkNotNull(namespace);
        checkNotNull(jsonFactory);
        this.namespace = namespace;
        this.jsonFactory = jsonFactory;
    }


    @Override
    public Locus getLocus(final String identifier) {
        checkNotNull(identifier);

        Locus locus = getLocusIfPresent(identifier);
        if (locus != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("locus {} retrieved from cache", identifier);
            }
            return locus;
        }

        String glstring = null;
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("glstring".equals(field)) {
                    glstring = parser.getText();
                }
            }
            locus = new Locus(identifier, glstring);
            putLocus(identifier, locus);
            return locus;
        }
        catch (IOException e) {
            logger.warn("could not get locus " + identifier, e);
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

    @Override
    public String registerLocus(final String glstring) {
        checkNotNull(glstring);

        String identifier = getLocusIdIfPresent(glstring);
        if (identifier != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("locus id for glstring {} retrieved from cache", glstring);
            }
            return identifier;
        }

        identifier = register("locus", glstring);
        putLocusId(glstring, identifier);
        return identifier;
    }

    @Override
    public Allele getAllele(final String identifier) {
        checkNotNull(identifier);

        Allele allele = getAlleleIfPresent(identifier);
        if (allele != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("allele {} retrieved from cache", identifier);
            }
            return allele;
        }

        String accession = null;
        String glstring = null;
        Locus locus = null;
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
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
            allele = new Allele(identifier, accession, glstring, locus);
            putAllele(identifier, allele);
            return allele;
        }
        catch (IOException e) {
            logger.warn("could not get allele " + identifier, e);
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

    @Override
    public String registerAllele(final String glstring) {
        checkNotNull(glstring);

        String identifier = getAlleleIdIfPresent(glstring);
        if (identifier != null) {
            if (logger.isTraceEnabled()) {
                logger.trace("allele id for glstring {} retrieved from cache", glstring);
            }
            return identifier;
        }

        identifier = register("allele", glstring);
        putAlleleId(glstring, identifier);
        return identifier;
    }

    @Override
    public AlleleList getAlleleList(final String identifier) {
        checkNotNull(identifier);

        List<Allele> alleles = new ArrayList<Allele>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("alleles".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String alleleField = parser.getCurrentName();
                            parser.nextToken();
                            if ("allele".equals(alleleField)) {
                                String alleleId = parser.getText();
                                alleles.add(getAllele(alleleId));
                            }
                        }
                    }
                }
            }
            return new AlleleList(identifier, alleles);
        }
        catch (IOException e) {
            logger.warn("could not get allele list " + identifier, e);
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

    @Override
    public String registerAlleleList(final String glstring) {
        return register("allele-list", glstring);
    }

    @Override
    public Haplotype getHaplotype(final String identifier) {
        checkNotNull(identifier);

        List<AlleleList> alleleLists = new ArrayList<AlleleList>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("alleleLists".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String alleleListField = parser.getCurrentName();
                            parser.nextToken();
                            if ("alleleList".equals(alleleListField)) {
                                String alleleListId = parser.getText();
                                alleleLists.add(getAlleleList(alleleListId));
                            }
                        }
                    }
                }
            }
            return new Haplotype(identifier, alleleLists);
        }
        catch (IOException e) {
            logger.warn("could not get haplotype " + identifier, e);
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

    @Override
    public String registerHaplotype(final String glstring) {
        return register("haplotype", glstring);
    }

    @Override
    public Genotype getGenotype(final String identifier) {
        checkNotNull(identifier);

        List<Haplotype> haplotypes = new ArrayList<Haplotype>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("haplotypes".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String haplotypeField = parser.getCurrentName();
                            parser.nextToken();
                            if ("haplotype".equals(haplotypeField)) {
                                String haplotypeId = parser.getText();
                                haplotypes.add(getHaplotype(haplotypeId));
                            }
                        }
                    }
                }
            }
            return new Genotype(identifier, haplotypes);
        }
        catch (IOException e) {
            logger.warn("could not get genotype " + identifier, e);
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
   
    @Override
    public String registerGenotype(final String glstring) {
        return register("genotype", glstring);
    }

    @Override
    public GenotypeList getGenotypeList(final String identifier) {
        checkNotNull(identifier);

        List<Genotype> genotypes = new ArrayList<Genotype>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("genotypes".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String genotypeField = parser.getCurrentName();
                            parser.nextToken();
                            if ("genotype".equals(genotypeField)) {
                                String genotypeId = parser.getText();
                                genotypes.add(getGenotype(genotypeId));
                            }
                        }
                    }
                }
            }
            return new GenotypeList(identifier, genotypes);
        }
        catch (IOException e) {
            logger.warn("could not get genotype list " + identifier, e);
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

    @Override
    public String registerGenotypeList(final String glstring) {
        return register("genotype-list", glstring);
    }

    @Override
    public MultilocusUnphasedGenotype getMultilocusUnphasedGenotype(final String identifier) {
        checkNotNull(identifier);

        List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>();
        InputStream inputStream = null;
        JsonParser parser = null;
        try {
            inputStream = get(identifier + ".json");
            parser = jsonFactory.createJsonParser(inputStream);
            parser.nextToken();
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String field = parser.getCurrentName();
                parser.nextToken();
                if ("genotypeLists".equals(field)) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String genotypeListField = parser.getCurrentName();
                            parser.nextToken();
                            if ("genotypeList".equals(genotypeListField)) {
                                String genotypeListId = parser.getText();
                                genotypeLists.add(getGenotypeList(genotypeListId));
                            }
                        }
                    }
                }
            }
            return new MultilocusUnphasedGenotype(identifier, genotypeLists);
        }
        catch (IOException e) {
            logger.warn("could not get multilocus unphased genotype " + identifier, e);
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

    @Override
    public String registerMultilocusUnphasedGenotype(final String glstring) {
        return register("multilocus-unphased-genotype", glstring);
    }

    private String register(final String type, final String glstring) {
        checkNotNull(glstring);
        return post(namespace + type, glstring);
    }

    private InputStream get(final String url) {
        long start = System.nanoTime();
        Response response = RestAssured.get(url);
        long elapsed = System.nanoTime() - start;
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP GET {} status code {} took {} ns", new Object[] { url, response.statusCode(), elapsed });
        }
        // todo:  check status code
        return response.body().asInputStream();
    }

    private String post(final String url, final String body) {
        long start = System.nanoTime();
        Response response = RestAssured.with().body(body).contentType("text/plain").post(url);
        long elapsed = System.nanoTime() - start;
        if (logger.isTraceEnabled()) {
            logger.trace("HTTP POST {} status code {} took {} ns", new Object[] { url, response.statusCode(), elapsed });
        }
        // todo:  check status code
        return response.getHeader("Location");
    }
}