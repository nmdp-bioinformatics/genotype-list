/*

    gl-core  Core interfaces and classes for the gl project.
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
package org.immunogenomics.gl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

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

    @Test
    public void testHashCode() {
        Locus a = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        Locus sameA = new Locus("http://immunogenomics.org/locus/0", "HLA-A");

        assertEquals(a.hashCode(), sameA.hashCode());
    }

    @Test
    public void testEquals() {
        Locus a = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        Locus altA = new Locus("http://alt.immunogenomics.org/locus/0", "HLA-A");
        Locus differentGlstring = new Locus("http://immunogenomics.org/locus/0", "HLA-B");
        Locus sameA = new Locus("http://immunogenomics.org/locus/0", "HLA-A");
        Locus b = new Locus("http://immunogenomics.org/locus/1", "HLA-B");

        assertFalse(a.equals(null));
        assertFalse(a.equals(new Object()));
        assertTrue(a.equals(a));
        assertFalse(a.equals(b));
        assertFalse(a.equals(altA));
        assertFalse(a.equals(differentGlstring));
        assertTrue(a.equals(sameA));
    }
}
