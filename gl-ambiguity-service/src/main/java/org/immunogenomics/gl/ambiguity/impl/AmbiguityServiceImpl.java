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
package org.immunogenomics.gl.ambiguity.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.net.URI;

import java.util.Iterator;
import java.util.List;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ListMultimap;

import com.google.inject.Inject;

import org.dishevelled.bitset.ImmutableBitSet;
import org.dishevelled.bitset.MutableBitSet;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.ambiguity.AmbiguityService;
import org.immunogenomics.gl.ambiguity.AmbiguityServiceException;

import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.GlClientException;

/**
 * Genotype list ambiguity service.
 */
final class AmbiguityServiceImpl implements AmbiguityService {
    private final ImmutableListMultimap<Locus, Allele> alleles;
    private final ImmutableMap<Allele, ImmutableBitSet> alleleBitSets;
    private final ConcurrentMap<AlleleList, ImmutableBitSet> alleleListBitSets;
    private final ConcurrentMap<String, AlleleList> allelicAmbiguities;
    private static final int DEFAULT_CAPACITY = 100000;

    @Inject
    AmbiguityServiceImpl(final ListMultimap<Locus, Allele> alleles) {
        checkNotNull(alleles);
        this.alleles = ImmutableListMultimap.copyOf(alleles);

        ImmutableMap.Builder<Allele, ImmutableBitSet> builder = ImmutableMap.builder();
        for (Locus locus : alleles.keys()) {
            List<Allele> allelesPerLocus = alleles.get(locus);
            long size = (long) allelesPerLocus.size();
            for (int i = 0; i < size; i++) {
                MutableBitSet bits = new MutableBitSet(size);
                bits.set(i);
                builder.put(allelesPerLocus.get(i), bits.immutableCopy());
            }
        }
        alleleBitSets = builder.build();

        alleleListBitSets = new ConcurrentHashMap<AlleleList, ImmutableBitSet>(DEFAULT_CAPACITY);
        allelicAmbiguities = new ConcurrentHashMap<String, AlleleList>(DEFAULT_CAPACITY);
    }


    @Override
    public ImmutableBitSet bits(final Allele allele) {
        checkNotNull(allele);
        return alleleBitSets.get(allele);
    }

    @Override
    public ImmutableBitSet bits(final AlleleList alleleList) {
        checkNotNull(alleleList);
        return alleleListBitSets.get(alleleList);
    }

    @Override
    public AlleleList get(final String name) {
        checkNotNull(name);
        return allelicAmbiguities.get(name);
    }

    @Override
    public AlleleList register(final String name, final AlleleList alleleList) throws AmbiguityServiceException {
        checkNotNull(name);
        checkNotNull(alleleList);

        allelicAmbiguities.putIfAbsent(name, alleleList);

        if (!alleleListBitSets.containsKey(alleleList)) {
            Locus locus = findLocus(alleleList);
            MutableBitSet bits = new MutableBitSet(alleles.get(locus).size());
            for (Allele allele : alleleList.getAlleles()) {
                bits.or(alleleBitSets.get(allele));
            }
            alleleListBitSets.replace(alleleList, bits.immutableCopy());
        }

        return alleleList;
    }

    static final Locus findLocus(final AlleleList alleleList) throws AmbiguityServiceException {
        Iterator<Allele> iterator = alleleList.getAlleles().iterator();
        Locus locus = iterator.next().getLocus();
        while (iterator.hasNext()) {
            if (!locus.equals(iterator.next().getLocus())) {
                throw new AmbiguityServiceException("could not register allelic ambiguity, allele list contained alleles from more than one locus");
            }
        }
        return locus;
    }
}
