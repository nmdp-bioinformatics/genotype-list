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
import java.util.List;
import java.util.Objects;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * A genotype is an unordered collection of one or more haplotypes separated in
 * GL String format by the character '<code>+<code>'.
 */
@Immutable
public final class Genotype extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String glstring;
    private final List<Haplotype> haplotypes;


    /**
     * Create a new genotype with the specified identifier and haplotype.
     *
     * @param id identifier for this genotype, must not be null
     * @param haplotype haplotype for this genotype, must not be null
     */
    public Genotype(final String id, final Haplotype haplotype) {
        this(id, ImmutableList.of(haplotype));
    }

    /**
     * Create a new genotype with the specified identifier and list of haplotypes.
     *
     * @param id identifier for this genotype, must not be null
     * @param haplotypes list of haplotypes for this genotype, must not be null,
     *    must contain at least one haplotype, and must not contain any null haplotypes
     */
    public Genotype(final String id, final List<Haplotype> haplotypes) {
        super(id);
        checkNotNull(haplotypes, "haplotypes must not be null");
        if (haplotypes.isEmpty()) {
            throw new IllegalArgumentException("haplotypes must contain at least one haplotype");
        }
        this.haplotypes = ImmutableList.copyOf(haplotypes);
        this.glstring = Joiner.on("+").join(this.haplotypes);
    }


    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the list of haplotypes for this genotype.
     *
     * @return the list of haplotypes for this genotype
     */
    public List<Haplotype> getHaplotypes() {
        return haplotypes;
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
        if (!(o instanceof Genotype)) {
            return false;
        }
        Genotype genotype = (Genotype) o;
        return Objects.equals(getId(), genotype.getId())
            && Objects.equals(getGlstring(), genotype.getGlstring());
    }
}
