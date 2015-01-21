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
package org.immunogenomics.gl.ambiguity;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Locus;

/**
 * Abstract unit test for implementations of AmbiguityService.
 */
public abstract class AbstractAmbiguityServiceTest {
    protected String name;
    protected Locus locus;
    protected Allele allele;
    protected AlleleList alleleList;
    protected AmbiguityService ambiguityService;

    /**
     * Create and return an instance of an implementation of AmbiguityService to test.
     *
     * @return an instance of an implementation of AmbiguityService to test
     */
    protected abstract AmbiguityService createAmbiguityService();

    @Before
    public void setUp() {
        name = "A*01";
        locus = new Locus("http://localhost/locus/0", "HLA-A");
        allele = new Allele("http://localhost/allele/0", "HLA00001", "HLA-A*01:01:01:01", locus);
        alleleList = new AlleleList("http://localhost/allele-list/0", ImmutableList.of(allele));

        ambiguityService = createAmbiguityService();
    }

    @Test
    public final void testCreateAmbiguityService() {
        assertNotNull(ambiguityService);
    }

    @Test(expected=NullPointerException.class)
    public final void testBitsNullAllele() {
        ambiguityService.bits((Allele) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testBitsNullAlleleList() {
        ambiguityService.bits((AlleleList) null);
    }

    @Test(expected=NullPointerException.class)
    public final void testGetNullName() {
        ambiguityService.get(null);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterNullName() throws Exception {
        ambiguityService.register(null, alleleList);
    }

    @Test(expected=NullPointerException.class)
    public final void testRegisterNullAlleleList() throws Exception {
        ambiguityService.register(name, null);
    }
}