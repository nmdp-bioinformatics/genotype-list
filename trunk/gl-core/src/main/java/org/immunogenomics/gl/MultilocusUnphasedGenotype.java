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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;

/**
 * Multilocus unphased genotype.
 */
@Immutable
public final class MultilocusUnphasedGenotype extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String glstring;
    private final List<GenotypeList> genotypeLists;

    /**
     * Create a new multilocus unphased genotype with the specified identifier and genotype list.
     *
     * @param id identifier for this multilocus unphased genotype, must not be null
     * @param genotypeList genotype list, must not be null
     */
    public MultilocusUnphasedGenotype(final String id, final GenotypeList genotypeList) {
        this(id, ImmutableList.of(genotypeList));
    }

    /**
     * Create a new multilocus unphased genotype with the specified identifier and list of genotype lists.
     *
     * @param id identifier for this multilocus unphased genotype, must not be null
     * @param genotypeLists list of genotype lists, must not be null,
     *    must contain at least one genotype list, and must not contain any null genotype lists
     */
    public MultilocusUnphasedGenotype(final String id, final List<GenotypeList> genotypeLists) {
        super(id);
        checkNotNull(genotypeLists, "genotypeLists must not be null");
        if (genotypeLists.isEmpty()) {
            throw new IllegalArgumentException("genotypeLists must contain at least one genotype lists");
        }
        this.genotypeLists = ImmutableList.copyOf(genotypeLists);
        this.glstring = Joiner.on("^").join(this.genotypeLists);
    }


    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the list of genotype lists for this multilocus unphased genotype.
     *
     * @return the list of genotype lists for this multilocus unphased genotype
     */
    public List<GenotypeList> getGenotypeLists() {
        return genotypeLists;
    }

    @Override
    public String toString() {
        return glstring;
    }
}