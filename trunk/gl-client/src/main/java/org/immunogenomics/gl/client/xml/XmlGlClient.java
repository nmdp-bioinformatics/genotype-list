/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.client.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

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
 * Implementation of GlClient that uses the XML representation and RestAssured as the HTTP client.
 */
public final class XmlGlClient extends CacheGlClient {
    private final String namespace;
    private final JAXBContext jaxbContext;
    private final XMLInputFactory xmlInputFactory;
    private final Logger logger = LoggerFactory.getLogger(XmlGlClient.class);


    /**
     * Create a new XML gl client with the specified namespace.
     *
     * @param namespace namespace for this XML gl client, must not be null
     */
    //@Inject
    public XmlGlClient(@Namespace final String namespace) {
        checkNotNull(namespace);
        this.namespace = namespace;
        try {
            jaxbContext = JAXBContext.newInstance("org.immunogenomics.gl.client.xml.jaxb");
        }
        catch (JAXBException e) {
            throw new RuntimeException(e);
        }
        xmlInputFactory = XMLInputFactory.newFactory();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            locus = toLocus(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.Locus.class).getValue());
            putLocus(identifier, locus);
            return locus;
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get locus " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get locus " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get locus " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            allele = toAllele(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.Allele.class).getValue());
            putAllele(identifier, allele);
            return allele;
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get allele " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get allele " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get allele " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return toAlleleList(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.AlleleList.class).getValue());
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get allele list " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get allele list " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get allele list " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return toHaplotype(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.Haplotype.class).getValue());
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get haplotype " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get haplotype " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get haplotype " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return toGenotype(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.Genotype.class).getValue());
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get genotype " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get genotype " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get genotype " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return toGenotypeList(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.GenotypeList.class).getValue());
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get genotype list " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get genotype list " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get genotype list " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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
        XMLStreamReader reader = null;
        try {
            inputStream = get(identifier + ".xml");
            reader = xmlInputFactory.createXMLStreamReader(inputStream);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            return toMultilocusUnphasedGenotype(unmarshaller.unmarshal(reader, org.immunogenomics.gl.client.xml.jaxb.MultilocusUnphasedGenotype.class).getValue());
        }
        catch (UnmarshalException ue) {
            logger.warn("could not get multilocus unphased genotype " + identifier, ue);
        }
        catch (XMLStreamException xse) {
            logger.warn("could not get multilocus unphased genotype " + identifier, xse);
        }
        catch (JAXBException e) {
            logger.warn("could not get multilocus unphased genotype " + identifier, e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignored
            }
            try {
                inputStream.close();
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


    // copy from jaxb resources --> core resources

    private Locus toLocus(final org.immunogenomics.gl.client.xml.jaxb.Locus jaxbLocus) {
        checkNotNull(jaxbLocus);
        return new Locus(jaxbLocus.getId(), jaxbLocus.getGlstring());
    }

    private Allele toAllele(final org.immunogenomics.gl.client.xml.jaxb.Allele jaxbAllele) {
        checkNotNull(jaxbAllele);
        Locus locus = toLocus(jaxbAllele.getLocus());
        return new Allele(jaxbAllele.getId(), jaxbAllele.getAccession(), jaxbAllele.getGlstring(), locus);
    }

    private AlleleList toAlleleList(final org.immunogenomics.gl.client.xml.jaxb.AlleleList jaxbAlleleList) {
        checkNotNull(jaxbAlleleList);
        List<Allele> alleles = new ArrayList<Allele>();
        for (org.immunogenomics.gl.client.xml.jaxb.Allele jaxbAllele : jaxbAlleleList.getAlleles().getAlleles()) {
            Allele allele = toAllele(jaxbAllele);
            alleles.add(allele);
        }
        return new AlleleList(jaxbAlleleList.getId(), alleles);
    }

    private Haplotype toHaplotype(final org.immunogenomics.gl.client.xml.jaxb.Haplotype jaxbHaplotype) {
        checkNotNull(jaxbHaplotype);
        List<AlleleList> alleleLists = new ArrayList<AlleleList>();
        for (org.immunogenomics.gl.client.xml.jaxb.AlleleList jaxbAlleleList : jaxbHaplotype.getAlleleLists().getAlleleLists()) {
            AlleleList alleleList = toAlleleList(jaxbAlleleList);
            alleleLists.add(alleleList);
        }
        return new Haplotype(jaxbHaplotype.getId(), alleleLists);
    }

    private Genotype toGenotype(final org.immunogenomics.gl.client.xml.jaxb.Genotype jaxbGenotype) {
        checkNotNull(jaxbGenotype);
        List<Haplotype> haplotypes = new ArrayList<Haplotype>();
        for (org.immunogenomics.gl.client.xml.jaxb.Haplotype jaxbHaplotype : jaxbGenotype.getHaplotypes().getHaplotypes()) {
            Haplotype haplotype = toHaplotype(jaxbHaplotype);
            haplotypes.add(haplotype);
        }
        return new Genotype(jaxbGenotype.getId(), haplotypes);
    }

    private GenotypeList toGenotypeList(final org.immunogenomics.gl.client.xml.jaxb.GenotypeList jaxbGenotypeList) {
        checkNotNull(jaxbGenotypeList);
        List<Genotype> genotypes = new ArrayList<Genotype>();
        for (org.immunogenomics.gl.client.xml.jaxb.Genotype jaxbGenotype : jaxbGenotypeList.getGenotypes().getGenotypes()) {
            Genotype genotype = toGenotype(jaxbGenotype);
            genotypes.add(genotype);
        }
        return new GenotypeList(jaxbGenotypeList.getId(), genotypes);
    }

    private MultilocusUnphasedGenotype toMultilocusUnphasedGenotype(final org.immunogenomics.gl.client.xml.jaxb.MultilocusUnphasedGenotype jaxbMultilocusUnphasedGenotype) {
        checkNotNull(jaxbMultilocusUnphasedGenotype);
        List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>();
        for (org.immunogenomics.gl.client.xml.jaxb.GenotypeList jaxbGenotypeList : jaxbMultilocusUnphasedGenotype.getGenotypeLists().getGenotypeLists()) {
            GenotypeList genotypeList = toGenotypeList(jaxbGenotypeList);
            genotypeLists.add(genotypeList);
        }
        return new MultilocusUnphasedGenotype(jaxbMultilocusUnphasedGenotype.getId(), genotypeLists);
    }
}