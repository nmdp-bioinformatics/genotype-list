/*

    gl-core  Core interfaces and classes for the gl project.
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
package org.immunogenomics.gl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;

import javax.annotation.concurrent.Immutable;

/**
 * An allele describes variation at a particular locus.  The allele representation
 * in GL String format must not contain any of the following characters: '<code>/</code>',
 * '<code>~</code>', '<code>+</code>', '<code>|</code>', or '<code>^</code>'.
 */
@Immutable
public final class Allele extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String accession;
    private final String glstring;
    private final Locus locus;


    /**
     * Create a new allele with the specified identifier, accession, glstring, and locus.
     *
     * @param id identifier for this allele, must not be null
     * @param accession accession for this allele, must not be null
     * @param glstring representation of this allele in GL String format, must not be null
     * @param locus locus for this allele, must not be null
     */
    public Allele(final String id, final String accession, final String glstring, final Locus locus) {
        super(id);
        checkNotNull(accession, "accession must not be null");
        checkNotNull(glstring, "glstring must not be null");
        checkNotNull(locus, "locus must not be null");
        this.accession = accession;
        this.glstring = glstring;
        this.locus = locus;
    }


    /**
     * Return the accession for this allele.
     *
     * @return the accession for this allele
     */
    public String getAccession() {
        return accession;
    }

    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the locus for this allele.
     *
     * @return the locus for this allele
     */
    public Locus getLocus() {
        return locus;
    }

    @Override
    public String toString() {
        return glstring;
    }
}