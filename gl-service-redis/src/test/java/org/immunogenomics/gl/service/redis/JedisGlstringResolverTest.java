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

import static org.mockito.Mockito.when;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.immunogenomics.gl.service.AbstractGlstringResolverTest;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Unit test for JedisGlstringResolver.
 */
public final class JedisGlstringResolverTest extends AbstractGlstringResolverTest {
    @Mock
    private IdSupplier idSupplier;
    @Mock
    private Jedis jedis;
    @Mock
    private JedisPool jedisPool;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(jedisPool.getResource()).thenReturn(jedis);
        when(idSupplier.createLocusId()).thenReturn("http://immunogenomics.org/locus/0");
        when(idSupplier.createAlleleId()).thenReturn("http://immunogenomics.org/allele/0");
        when(idSupplier.createAlleleListId()).thenReturn("http://immunogenomics.org/allele-list/0");
        when(idSupplier.createHaplotypeId()).thenReturn("http://immunogenomics.org/haplotype/0");
        when(idSupplier.createGenotypeId()).thenReturn("http://immunogenomics.org/genotype/0");
        when(idSupplier.createGenotypeListId()).thenReturn("http://immunogenomics.org/genotype-list/0");
        when(idSupplier.createMultilocusUnphasedGenotypeId()).thenReturn("http://immunogenomics.org/multilocus-unphased-genotype/0");
        super.setUp();
    }

    @Override
    protected GlstringResolver createGlstringResolver() {
        return new JedisGlstringResolver(idSupplier, jedisPool);
    }
}