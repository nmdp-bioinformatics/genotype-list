/*

    gl-service-redis  Implementation of persistent cache for gl-service using Redis+jedis.
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
package org.immunogenomics.gl.service.redis;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.redis.JedisUtils.encode;
import static org.immunogenomics.gl.service.redis.JedisUtils.serialize;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.inject.Inject;

/**
 * Redis+jedis based genotype list registry.
 */
@Immutable
final class JedisGlRegistry implements GlRegistry {
    private final JedisPool jedisPool;

    @Inject
    JedisGlRegistry(final JedisPool jedisPool) {
        checkNotNull(jedisPool);
        this.jedisPool = jedisPool;
    }


    @Override
    public void registerLocus(final Locus locus) {
        checkNotNull(locus);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(locus.getId()), serialize(locus));
            // prepend type to key since glstrings cannot uniquely identify type
            jedis.set("locus/" + locus.getGlstring(), locus.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerAllele(final Allele allele) {
        checkNotNull(allele);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(allele.getId()), serialize(allele));
            jedis.set("allele/" + allele.getGlstring(), allele.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerAlleleList(final AlleleList alleleList) {
        checkNotNull(alleleList);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(alleleList.getId()), serialize(alleleList));
            jedis.set("allele-list/" + alleleList.getGlstring(), alleleList.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerHaplotype(final Haplotype haplotype) {
        checkNotNull(haplotype);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(haplotype.getId()), serialize(haplotype));
            jedis.set("haplotype/" + haplotype.getGlstring(), haplotype.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerGenotype(final Genotype genotype) {
        checkNotNull(genotype);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(genotype.getId()), serialize(genotype));
            jedis.set("genotype/" + genotype.getGlstring(), genotype.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerGenotypeList(final GenotypeList genotypeList) {
        checkNotNull(genotypeList);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(genotypeList.getId()), serialize(genotypeList));
            jedis.set("genotypeList/" + genotypeList.getGlstring(), genotypeList.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }

    @Override
    public void registerMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        checkNotNull(multilocusUnphasedGenotype);
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(encode(multilocusUnphasedGenotype.getId()), serialize(multilocusUnphasedGenotype));
            jedis.set("multilocus-unphased-genotype/" + multilocusUnphasedGenotype.getGlstring(), multilocusUnphasedGenotype.getId());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }
}