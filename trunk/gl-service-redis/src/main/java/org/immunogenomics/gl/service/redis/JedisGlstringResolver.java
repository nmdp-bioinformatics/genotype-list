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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.google.inject.Inject;

/**
 * Redis+jedis based glstring resolver.
 */
@Immutable
final class JedisGlstringResolver implements GlstringResolver {
    private final IdSupplier idSupplier;
    private final JedisPool jedisPool;

    @Inject
    JedisGlstringResolver(final IdSupplier idSupplier, final JedisPool jedisPool) {
        checkNotNull(idSupplier);
        checkNotNull(jedisPool);
        this.idSupplier = idSupplier;
        this.jedisPool = jedisPool;
    }


    @Override
    public String resolveLocus(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            // prepend type to key since glstrings cannot uniquely identify type
            String uri = jedis.get("locus/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createLocusId();
    }

    @Override
    public String resolveAllele(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("allele/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createAlleleId();
    }

    @Override
    public String resolveAlleleList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("allele-list/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createAlleleListId();
    }

    @Override
    public String resolveHaplotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("haplotype/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createHaplotypeId();
    }

    @Override
    public String resolveGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("genotype/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createGenotypeId();
    }

    @Override
    public String resolveGenotypeList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("genotype-list/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createGenotypeListId();
    }

    @Override
    public String resolveMultilocusUnphasedGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        Jedis jedis = jedisPool.getResource();
        try {
            String uri = jedis.get("multilocus-unphased-genotype/" + glstring);
            if (uri != null) {
                return uri;
            }
        }
        finally {
            jedisPool.returnResource(jedis);
        }
        return idSupplier.createMultilocusUnphasedGenotypeId();
    }
}