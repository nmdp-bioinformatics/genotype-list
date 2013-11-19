/*

    gl-core  Core interfaces and classes for the gl project.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

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
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * An allele list is an unordered collection of one or more alleles representing alleleic
 * ambiguity separated in GL String format by the character '<code>/</code>'.
 */
@Immutable
public final class AlleleList extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String glstring;
    private final List<Allele> alleles;


    /**
     * Create a new allele list with the specified identifier and allele.
     *
     * @param id identifier for this allele list, must not be null
     * @param allele allele for this allele list, must not be null
     */
    public AlleleList(final String id, final Allele allele) {
        this(id, ImmutableList.of(allele));
    }

    /**
     * Create a new allele list with the specified identifier and list of alleles.
     *
     * @param id identifier for this allele list, must not be null
     * @param alleles list of alleles for this allele list, must not be null,
     *    must contain at least one allele, and must not contain any null alleles
     */
    public AlleleList(final String id, final List<Allele> alleles) {
        super(id);
        checkNotNull(alleles, "alleles must not be null");
        if (alleles.isEmpty()) {
            throw new IllegalArgumentException("alleles must contain at least one allele");
        }
        this.alleles = ImmutableList.copyOf(alleles);
        this.glstring = Joiner.on("/").join(this.alleles);
    }


    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the list of alleles for this allele list.
     *
     * @return the list of alleles for this allele list
     */
    public List<Allele> getAlleles() {
        return alleles;
    }

    @Override
    public String toString() {
        return glstring;
    }
}