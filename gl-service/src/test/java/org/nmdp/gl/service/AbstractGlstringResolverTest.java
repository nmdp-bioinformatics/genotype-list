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
package org.nmdp.gl.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

/**
 * Abstract unit test for implementations of GlstringResolver.
 */
public abstract class AbstractGlstringResolverTest {
    protected GlstringResolver glstringResolver;
    protected String locusGlstring = "HLA-A";
    protected String alleleGlstring = "HLA-A*01:01:01:01";
    protected String alleleListGlstring = "HLA-A*01:01:01:01/HLA-A*01:01:01:02";
    protected String haplotypeGlstring = "HLA-A*01:01:01:01~HLA-B*01:01:01:01";
    protected String genotypeGlstring = "HLA-A*01:01:01:01+HLA-A:01:01:01:02";
    protected String genotypeListGlstring = "HLA-A:01:01:01:01+HLA-A*01:01:01:02|HLA-A:01:01:01:01+HLA-A*01:01:01:02";
    protected String multilocusUnphasedGenotypeGlstring = "HLA-A*01:01:01:01+HLA-A*01:01:01:02^HLA-B:01:01:01:01+HLA-B:01:01:01:02";
    protected abstract GlstringResolver createGlstringResolver();

    @Before
    public void setUp() {
        glstringResolver = createGlstringResolver();
    }

    @Test
    public void testCreateGlstringResolver() {
        assertNotNull(glstringResolver);
    }

    @Test
    public void testResolveLocus() {
        assertNotNull(glstringResolver.resolveLocus(locusGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullLocus() {
        glstringResolver.resolveLocus(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyLocus() {
        glstringResolver.resolveLocus("");
    }

    @Test
    public void testResolveAllele() {
        assertNotNull(glstringResolver.resolveAllele(alleleGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullAllele() {
        glstringResolver.resolveAllele(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyAllele() {
        glstringResolver.resolveAllele("");
    }

    @Test
    public void testResolveAlleleList() {
        assertNotNull(glstringResolver.resolveAlleleList(alleleListGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullAlleleList() {
        glstringResolver.resolveAlleleList(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyAlleleList() {
        glstringResolver.resolveAlleleList("");
    }

    @Test
    public void testResolveHaplotype() {
        assertNotNull(glstringResolver.resolveHaplotype(haplotypeGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullHaplotype() {
        glstringResolver.resolveHaplotype(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyHaplotype() {
        glstringResolver.resolveHaplotype("");
    }

    @Test
    public void testResolveGenotype() {
        assertNotNull(glstringResolver.resolveGenotype(genotypeGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullGenotype() {
        glstringResolver.resolveGenotype(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyGenotype() {
        glstringResolver.resolveGenotype("");
    }

    @Test
    public void testResolveGenotypeList() {
        assertNotNull(glstringResolver.resolveGenotypeList(genotypeListGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullGenotypeList() {
        glstringResolver.resolveGenotypeList(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyGenotypeList() {
        glstringResolver.resolveGenotypeList("");
    }

    @Test
    public void testResolveMultilocusUnphasedGenotype() {
        assertNotNull(glstringResolver.resolveMultilocusUnphasedGenotype(multilocusUnphasedGenotypeGlstring));
    }

    @Test(expected=NullPointerException.class)
    public void testResolveNullMultilocusUnphasedGenotype() {
        glstringResolver.resolveMultilocusUnphasedGenotype(null);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testResolveEmptyMultilocusUnphasedGenotype() {
        glstringResolver.resolveMultilocusUnphasedGenotype("");
    }
}