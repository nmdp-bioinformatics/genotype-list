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
package org.nmdp.gl.client.xml;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;
import org.nmdp.gl.client.cache.CacheGlClient;
import org.nmdp.gl.client.cache.GlClientAlleleCache;
import org.nmdp.gl.client.cache.GlClientAlleleIdCache;
import org.nmdp.gl.client.cache.GlClientLocusCache;
import org.nmdp.gl.client.cache.GlClientLocusIdCache;
import org.nmdp.gl.client.http.HttpClient;
import org.nmdp.gl.service.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.inject.Inject;

/**
 * Implementation of GlClient that uses the XML representation and RestAssured as the HTTP client.
 */
public final class XmlGlClient extends CacheGlClient {
    private final String namespace;
    private final HttpClient httpClient;
    private final JAXBContext jaxbContext;
    private final XMLInputFactory xmlInputFactory;
    private final Logger logger = LoggerFactory.getLogger(XmlGlClient.class);


    /**
     * Create a new XML gl client with the specified parameters.
     *
     * @param namespace namespace for this XML gl client, must not be null
     * @param httpClient HTTP client for this XML gl client, must not be null
     * @param loci locus cache, must not be null
     * @param locusIds locus id cache, must not be null
     * @param alleles allele cache, must not be null
     * @param alleleIds allele id cache, must not be null
     */
    @Inject
    public XmlGlClient(@Namespace final String namespace,
            final HttpClient httpClient,
            @GlClientLocusCache final Cache<String, Locus> loci,
            @GlClientLocusIdCache final Cache<String, String> locusIds,
            @GlClientAlleleCache final Cache<String, Allele> alleles,
            @GlClientAlleleIdCache final Cache<String, String> alleleIds) {
        super(loci, locusIds, alleles, alleleIds);
        checkNotNull(namespace);
        checkNotNull(httpClient);
        this.namespace = namespace;
        this.httpClient = httpClient;
        try {
            jaxbContext = JAXBContext.newInstance("org.nmdp.gl.client.xml.jaxb");
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
            locus = toLocus(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.Locus.class).getValue());
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
            allele = toAllele(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.Allele.class).getValue());
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
            return toAlleleList(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.AlleleList.class).getValue());
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
            return toHaplotype(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.Haplotype.class).getValue());
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
            return toGenotype(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.Genotype.class).getValue());
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
            return toGenotypeList(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.GenotypeList.class).getValue());
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
            return toMultilocusUnphasedGenotype(unmarshaller.unmarshal(reader, org.nmdp.gl.client.xml.jaxb.MultilocusUnphasedGenotype.class).getValue());
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
        return httpClient.get(url);
    }

    private String post(final String url, final String body) {
        return httpClient.post(url, body);
    }

    // copy from jaxb resources --> core resources

    private Locus toLocus(final org.nmdp.gl.client.xml.jaxb.Locus jaxbLocus) {
        checkNotNull(jaxbLocus);
        return new Locus(jaxbLocus.getId(), jaxbLocus.getGlstring());
    }

    private Allele toAllele(final org.nmdp.gl.client.xml.jaxb.Allele jaxbAllele) {
        checkNotNull(jaxbAllele);
        Locus locus = toLocus(jaxbAllele.getLocus());
        return new Allele(jaxbAllele.getId(), jaxbAllele.getAccession(), jaxbAllele.getGlstring(), locus);
    }

    private AlleleList toAlleleList(final org.nmdp.gl.client.xml.jaxb.AlleleList jaxbAlleleList) {
        checkNotNull(jaxbAlleleList);
        List<Allele> alleles = new ArrayList<Allele>();
        for (org.nmdp.gl.client.xml.jaxb.Allele jaxbAllele : jaxbAlleleList.getAlleles().getAlleles()) {
            Allele allele = toAllele(jaxbAllele);
            alleles.add(allele);
        }
        return new AlleleList(jaxbAlleleList.getId(), alleles);
    }

    private Haplotype toHaplotype(final org.nmdp.gl.client.xml.jaxb.Haplotype jaxbHaplotype) {
        checkNotNull(jaxbHaplotype);
        List<AlleleList> alleleLists = new ArrayList<AlleleList>();
        for (org.nmdp.gl.client.xml.jaxb.AlleleList jaxbAlleleList : jaxbHaplotype.getAlleleLists().getAlleleLists()) {
            AlleleList alleleList = toAlleleList(jaxbAlleleList);
            alleleLists.add(alleleList);
        }
        return new Haplotype(jaxbHaplotype.getId(), alleleLists);
    }

    private Genotype toGenotype(final org.nmdp.gl.client.xml.jaxb.Genotype jaxbGenotype) {
        checkNotNull(jaxbGenotype);
        List<Haplotype> haplotypes = new ArrayList<Haplotype>();
        for (org.nmdp.gl.client.xml.jaxb.Haplotype jaxbHaplotype : jaxbGenotype.getHaplotypes().getHaplotypes()) {
            Haplotype haplotype = toHaplotype(jaxbHaplotype);
            haplotypes.add(haplotype);
        }
        return new Genotype(jaxbGenotype.getId(), haplotypes);
    }

    private GenotypeList toGenotypeList(final org.nmdp.gl.client.xml.jaxb.GenotypeList jaxbGenotypeList) {
        checkNotNull(jaxbGenotypeList);
        List<Genotype> genotypes = new ArrayList<Genotype>();
        for (org.nmdp.gl.client.xml.jaxb.Genotype jaxbGenotype : jaxbGenotypeList.getGenotypes().getGenotypes()) {
            Genotype genotype = toGenotype(jaxbGenotype);
            genotypes.add(genotype);
        }
        return new GenotypeList(jaxbGenotypeList.getId(), genotypes);
    }

    private MultilocusUnphasedGenotype toMultilocusUnphasedGenotype(final org.nmdp.gl.client.xml.jaxb.MultilocusUnphasedGenotype jaxbMultilocusUnphasedGenotype) {
        checkNotNull(jaxbMultilocusUnphasedGenotype);
        List<GenotypeList> genotypeLists = new ArrayList<GenotypeList>();
        for (org.nmdp.gl.client.xml.jaxb.GenotypeList jaxbGenotypeList : jaxbMultilocusUnphasedGenotype.getGenotypeLists().getGenotypeLists()) {
            GenotypeList genotypeList = toGenotypeList(jaxbGenotypeList);
            genotypeLists.add(genotypeList);
        }
        return new MultilocusUnphasedGenotype(jaxbMultilocusUnphasedGenotype.getId(), genotypeLists);
    }
}
