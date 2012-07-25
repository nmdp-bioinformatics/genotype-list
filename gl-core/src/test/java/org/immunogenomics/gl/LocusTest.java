/*

    gl-core  Core interfaces and classes for the gl project.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Unit test for Locus.
 */
public final class LocusTest {
    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdentifier() {
        new Locus(null, "HLA-A");
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullGlstring() {
        new Locus("http://immunogenomics.org/locus/0", null);
    }

    @Test
    public void testId() {
        Locus locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        assertEquals("http://immunogenomics.org/locus/0", locus.getId());
    }

    @Test
    public void testGlstring() {
        Locus locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        assertEquals("HLA-A", locus.getGlstring());
    }

    @Test
    public void testToStringIsGlstring() {
        Locus locus = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        assertEquals(locus.getGlstring(), locus.toString());
    }
}