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
import static org.immunogenomics.gl.service.redis.JedisUtils.deserialize;
import static org.immunogenomics.gl.service.redis.JedisUtils.encode;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.IdResolver;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.inject.Inject;

/**
 * Redis+jedis based identifier resolver.
 */
@Immutable
final class JedisIdResolver implements IdResolver {
    private final JedisPool jedisPool;

    @Inject
    JedisIdResolver(final JedisPool jedisPool) {
        checkNotNull(jedisPool);
        this.jedisPool = jedisPool;
    }


    @Override
    public Locus findLocus(final String id) {
        return (Locus) find(id);
    }

    @Override
    public Allele findAllele(final String id) {
        return (Allele) find(id);
    }

    @Override
    public AlleleList findAlleleList(final String id) {
        return (AlleleList) find(id);
    }

    @Override
    public Haplotype findHaplotype(final String id) {
        return (Haplotype) find(id);
    }

    @Override
    public Genotype findGenotype(final String id) {
        return (Genotype) find(id);
    }

    @Override
    public GenotypeList findGenotypeList(final String id) {
        return (GenotypeList) find(id);
    }

    @Override
    public MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(final String id) {
        return (MultilocusUnphasedGenotype) find(id);
    }

    private Object find(final String id) {
        Jedis jedis = jedisPool.getResource();
        try {
            return deserialize(jedis.get(encode(id)));
            // jedis post version 2.1.0
            //byte[] key = encode(id);
            //jedis.get(key);
            //return deserialize(jedis.getBinaryBulkReply());
        }
        finally {
            jedisPool.returnResource(jedis);
        }
    }
}