/*

    gl-ambiguity-service  Genotype list ambiguity service.
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
package org.immunogenomics.gl.ambiguity.service;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;

import com.google.common.collect.ListMultimap;
import com.google.common.collect.Maps;

import org.dishevelled.bitset.ImmutableBitSet;
import org.dishevelled.bitset.MutableBitSet;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.client.GlClient;

/**
 * Genotype list ambiguity service.
 */
public final class PerLocusGlAmbiguityService {
    private final GlClient client;
    private final ListMultimap<Locus, Allele> alleles;
    private final Map<Allele, ImmutableBitSet> alleleBitSets;
    private final Map<AlleleList, ImmutableBitSet> alleleListBitSets;
    private final Map<String, AlleleList> allelicAmbiguities;

    public PerLocusGlAmbiguityService(final GlClient client, final ListMultimap<Locus, Allele> alleles) {
        checkNotNull(client);
        this.client = client;

        checkNotNull(alleles);
        this.alleles = alleles;
        alleleBitSets = Maps.newHashMapWithExpectedSize(alleles.size());
        alleleListBitSets = Maps.newHashMapWithExpectedSize(100000);
        allelicAmbiguities = Maps.newHashMapWithExpectedSize(100000);

        /*
        // inject CacheBuilderSpec?
        this.alleleListBitSets = CacheBuilder.newBuilder().build(new CacheLoader<AlleleList, ImmutableBitSet>() {
                @Override
                public ImmutableBitSet load(final AlleleList alleleList) {
                    MutableBitSet bits = new MutableBitSet(size);
                    for (Allele allele : alleleList.getAlleles()) {
                        bits.or(alleleBitSets.get(allele));
                    }
                    return bits.immutableCopy();
                }
            });
        */

        // populate allele bit sets
        for (Locus locus : alleles.keys()) {
            List<Allele> allelesPerLocus = alleles.get(locus);
            long size = (long) allelesPerLocus.size();
            for (int i = 0; i < size; i++) {
                MutableBitSet bits = new MutableBitSet(size);
                bits.set(i);
                alleleBitSets.put(allelesPerLocus.get(i), bits.immutableCopy());
            }
        }
    }

    public ImmutableBitSet asBits(final Allele allele) {
        return alleleBitSets.get(allele);
    }

    public ImmutableBitSet asBits(final AlleleList alleleList) {
        return alleleListBitSets.get(alleleList);
    }

    public AlleleList registerAllelicAmbiguity(final Locus locus, final String name, final String glstring) {
        checkNotNull(name);

        if (allelicAmbiguities.containsKey(name)) {
            return allelicAmbiguities.get(name);
        }

        AlleleList alleleList = client.getAlleleList(glstring);
        allelicAmbiguities.put(name, alleleList);

        MutableBitSet bits = new MutableBitSet(alleles.get(locus).size());
        for (Allele allele : alleleList.getAlleles()) {
            // crossing the streams here would be bad
            bits.or(alleleBitSets.get(allele));
        }
        alleleListBitSets.put(alleleList, bits.immutableCopy());

        return alleleList;
    }

    public ImmutableBitSet registerAllelicAmbiguityAsBits(final Locus locus, final String name, final String glstring) {
        return asBits(registerAllelicAmbiguity(locus, name, glstring));
    }
}