/*

    gl-service-redis  Implementation of persistent cache for gl-service using Redis+jedis.
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
package org.immunogenomics.gl.service.redis;

import static org.mockito.Mockito.when;
import static org.immunogenomics.gl.service.redis.JedisUtils.encode;
import static org.immunogenomics.gl.service.redis.JedisUtils.serialize;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.service.AbstractIdResolverTest;
import org.immunogenomics.gl.service.IdResolver;

import com.google.common.collect.ImmutableList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Unit test for JedisIdResolver.
 */
public final class JedisIdResolverTest extends AbstractIdResolverTest {
    @Mock
    private Jedis jedis;
    @Mock
    private JedisPool jedisPool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Locus locus = new Locus(validLocusId, "HLA-A");
        Allele allele = new Allele(validAlleleId, "A01234", "HLA-A*01:01:01:01", locus);
        AlleleList alleleList0 = new AlleleList(validAlleleListId, allele);
        AlleleList alleleList1 = new AlleleList(validAlleleListId, allele);
        Haplotype haplotype = new Haplotype(validHaplotypeId, ImmutableList.of(alleleList0, alleleList1));
        Haplotype haplotype0 = new Haplotype(validHaplotypeId, alleleList0);
        Genotype genotype = new Genotype(validGenotypeId, haplotype0);
        GenotypeList genotypeList0 = new GenotypeList(validGenotypeListId, genotype);
        GenotypeList genotypeList1 = new GenotypeList(validGenotypeListId, genotype);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId, ImmutableList.of(genotypeList0, genotypeList1));

        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.get(encode(validLocusId))).thenReturn(serialize(locus));
        when(jedis.get(encode(validAlleleId))).thenReturn(serialize(allele));
        when(jedis.get(encode(validAlleleListId))).thenReturn(serialize(alleleList0));
        when(jedis.get(encode(validHaplotypeId))).thenReturn(serialize(haplotype));
        when(jedis.get(encode(validGenotypeId))).thenReturn(serialize(genotype));
        when(jedis.get(encode(validGenotypeListId))).thenReturn(serialize(genotypeList0));
        when(jedis.get(encode(validMultilocusUnphasedGenotypeId))).thenReturn(serialize(multilocusUnphasedGenotype));
        super.setUp();
    }

    @Override
    protected IdResolver createIdResolver() {
        return new JedisIdResolver(jedisPool);
    }
}