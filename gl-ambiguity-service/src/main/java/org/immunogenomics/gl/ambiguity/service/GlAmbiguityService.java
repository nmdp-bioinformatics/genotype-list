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

import com.google.common.collect.Maps;

import org.dishevelled.bitset.ImmutableBitSet;
import org.dishevelled.bitset.MutableBitSet;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;

import org.immunogenomics.gl.client.GlClient;

/**
 * Genotype list ambiguity service.
 */
public final class GlAmbiguityService {
    private final GlClient client;
    private final long size;
    private final Map<Allele, ImmutableBitSet> alleleBitSets;
    private final Map<AlleleList, ImmutableBitSet> alleleListBitSets;
    private final Map<String, AlleleList> allelicAmbiguities;

    public GlAmbiguityService(final GlClient client, final List<Allele> alleles) {
        checkNotNull(client);
        this.client = client;

        checkNotNull(alleles);
        size = (long) alleles.size();
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
        for (int i = 0; i < size; i++) {
            MutableBitSet bits = new MutableBitSet(size);
            bits.set(i);
            alleleBitSets.put(alleles.get(i), bits.immutableCopy());
        }
    }

    public ImmutableBitSet asBits(final Allele allele) {
        checkNotNull(allele);
        return alleleBitSets.get(allele);
    }

    public ImmutableBitSet asBits(final AlleleList alleleList) {
        checkNotNull(alleleList);

        if (!alleleListBitSets.containsKey(alleleList)) {
            MutableBitSet bits = new MutableBitSet(size);
            for (Allele allele : alleleList.getAlleles()) {
                bits.or(alleleBitSets.get(allele));
            }
            alleleListBitSets.put(alleleList, bits.immutableCopy());
        }
        return alleleListBitSets.get(alleleList);
    }

    public AlleleList registerAllelicAmbiguity(final String name, final String glstring) {
        checkNotNull(name);
        checkNotNull(glstring);

        if (!allelicAmbiguities.containsKey(name)) {
            AlleleList alleleList = client.getAlleleList(glstring);
            allelicAmbiguities.put(name, alleleList);

            MutableBitSet bits = new MutableBitSet(size);
            for (Allele allele : alleleList.getAlleles()) {
                bits.or(alleleBitSets.get(allele));
            }
            alleleListBitSets.put(alleleList, bits.immutableCopy());
        }
        return allelicAmbiguities.get(name);
    }

    public ImmutableBitSet registerAllelicAmbiguityAsBits(final String name, final String glstring) {
        return asBits(registerAllelicAmbiguity(name, glstring));
    }
}