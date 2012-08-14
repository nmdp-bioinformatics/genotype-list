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
package org.immunogenomics.gl.client.cache;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.cache.CacheBuilder.newBuilder;

import com.google.common.cache.Cache;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.client.AbstractGlClient;
import org.immunogenomics.gl.client.GlClient;

/**
 * Decorator that provides caching of loci and alleles to a gl client.
 */
public final class CacheGlClient extends AbstractGlClient {
    private GlClient client;
    // todo:  inject these
    private Cache<String, Locus> loci;
    private Cache<String, String> locusIds;
    private Cache<String, Allele> alleles;
    private Cache<String, String> alleleIds;


    //@Inject
    public CacheGlClient(final GlClient client) {
        checkNotNull(client);
        this.client = client;

        loci = newBuilder().initialCapacity(1000).maximumSize(1000).build();
        locusIds = newBuilder().initialCapacity(1000).maximumSize(1000).build();
        alleles = newBuilder().initialCapacity(10000).maximumSize(10000).build();
        alleleIds = newBuilder().initialCapacity(10000).maximumSize(10000).build();
    }


    @Override
    public Locus getLocus(final String identifier) {
        checkNotNull(identifier);
        Locus locus = loci.getIfPresent(identifier);
        if (locus != null) {
            return locus;
        }
        locus = client.getLocus(identifier);
        loci.put(identifier, locus);
        return locus;
    }

    @Override
    public String registerLocus(final String glstring) {
        checkNotNull(glstring);
        String identifier = locusIds.getIfPresent(glstring);
        if (identifier != null) {
            return identifier;
        }
        identifier = client.registerLocus(glstring);
        locusIds.put(glstring, identifier);
        return identifier;
    }

    @Override
    public Allele getAllele(final String identifier) {
        checkNotNull(identifier);
        Allele allele = alleles.getIfPresent(identifier);
        if (allele != null) {
            return allele;
        }
        allele = client.getAllele(identifier);
        alleles.put(identifier, allele);
        return allele;
    }

    @Override
    public String registerAllele(final String glstring) {
        checkNotNull(glstring);
        String identifier = alleleIds.getIfPresent(glstring);
        if (identifier != null) {
            return identifier;
        }
        identifier = client.registerAllele(glstring);
        alleleIds.put(glstring, identifier);
        return identifier;
    }

    @Override
    public AlleleList getAlleleList(final String identifier) {
        return client.getAlleleList(identifier);
    }

    @Override
    public String registerAlleleList(final String glstring) {
        return client.registerAlleleList(glstring);
    }

    @Override
    public Haplotype getHaplotype(final String identifier) {
        return client.getHaplotype(identifier);
    }

    @Override
    public String registerHaplotype(final String glstring) {
        return client.registerHaplotype(glstring);
    }

    @Override
    public Genotype getGenotype(final String identifier) {
        return client.getGenotype(identifier);
    }
   
    @Override
    public String registerGenotype(final String glstring) {
        return client.registerGenotype(glstring);
    }

    @Override
    public GenotypeList getGenotypeList(final String identifier) {
        return client.getGenotypeList(identifier);
    }

    @Override
    public String registerGenotypeList(final String glstring) {
        return client.registerGenotypeList(glstring);
    }

    @Override
    public MultilocusUnphasedGenotype getMultilocusUnphasedGenotype(final String identifier) {
        return client.getMultilocusUnphasedGenotype(identifier);
    }

    @Override
    public String registerMultilocusUnphasedGenotype(final String glstring) {
        return client.registerMultilocusUnphasedGenotype(glstring);
    }
}