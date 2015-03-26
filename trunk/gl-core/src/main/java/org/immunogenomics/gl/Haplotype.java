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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * A haplotype is an unordered collection of one or more allele lists in phase separated
 * in GL String format by the character '<code>~<code>'.
 */
@Immutable
public final class Haplotype extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String glstring;
    private final List<AlleleList> alleleLists;


    /**
     * Create a new haplotype with the specified identifier and allele list.
     *
     * @param id identifier for this haplotype, must not be null
     * @param alleleList allele list for this haplotype, must not be null
     */
    public Haplotype(final String id, final AlleleList alleleList) {
        this(id, ImmutableList.of(alleleList));
    }

    /**
     * Create a new haplotype with the specified identifier and list of allele lists.
     *
     * @param id identifier for this haplotype, must not be null
     * @param alleleLists list of allele lists, must not be null,
     *    must contain at least one allele list, and must not contain any null allele lists
     */
    public Haplotype(final String id, final List<AlleleList> alleleLists) {
        super(id);
        checkNotNull(alleleLists, "alleleLists must not be null");
        if (alleleLists.isEmpty()) {
            throw new IllegalArgumentException("alleles must contain at least one allele list");
        }
        this.alleleLists = ImmutableList.copyOf(alleleLists);
        this.glstring = Joiner.on("~").join(this.alleleLists);
    }


    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the list of allele lists for this haplotype.
     *
     * @return the list of allele lists for this haplotype
     */
    public List<AlleleList> getAlleleLists() {
        return alleleLists;
    }

    @Override
    public String toString() {
        return glstring;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGlstring());
    }

    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Haplotype)) {
            return false;
        }
        Haplotype haplotype = (Haplotype) o;
        return Objects.equals(getId(), haplotype.getId())
            && Objects.equals(getGlstring(), haplotype.getGlstring());
    }
}
