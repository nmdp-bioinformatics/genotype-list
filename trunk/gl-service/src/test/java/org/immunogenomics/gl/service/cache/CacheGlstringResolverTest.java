/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.cache;

import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

import org.junit.Before;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.immunogenomics.gl.service.AbstractGlstringResolverTest;
import org.immunogenomics.gl.service.GlstringResolver;

import com.google.common.cache.LoadingCache;

/**
 * Unit test for CacheGlstringResolver.
 */
public final class CacheGlstringResolverTest extends AbstractGlstringResolverTest {
    @Mock
    private LoadingCache<String, String> locusIds;
    @Mock
    private LoadingCache<String, String> alleleIds;
    @Mock
    private LoadingCache<String, String> alleleListIds;
    @Mock
    private LoadingCache<String, String> haplotypeIds;
    @Mock
    private LoadingCache<String, String> genotypeIds;
    @Mock
    private LoadingCache<String, String> genotypeListIds;
    @Mock
    private LoadingCache<String, String> multilocusUnphasedGenotypeIds;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(locusIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/locus/0");
        when(alleleIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/allele/0");
        when(alleleListIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/allele-list/0");
        when(haplotypeIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/haplotype/0");
        when(genotypeIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/genotype/0");
        when(genotypeListIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/genotype-list/0");
        when(multilocusUnphasedGenotypeIds.getUnchecked(anyString())).thenReturn("http://immunogenomics.org/multilocus-unphased-genotype/0");
        super.setUp();
    }

    @Override
    protected GlstringResolver createGlstringResolver() {
        return new CacheGlstringResolver(locusIds, alleleIds, alleleListIds, haplotypeIds, genotypeIds, genotypeListIds, multilocusUnphasedGenotypeIds);
    }
}