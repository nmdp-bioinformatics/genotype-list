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
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Joiner;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterators;

/**
 * Genotype.
 */
@Immutable
public final class Genotype extends GlResource implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String glstring;
    private final List<AlleleList> alleleLists;
    private final List<Haplotype> haplotypes;


    /**
     * Create a new genotype with the specified identifier and allele list.
     *
     * @param id identifier for this genotype, must not be null
     * @param alleleList allele list for this genotype, must not be null
     */
    public Genotype(final String id, final AlleleList alleleList) {
        this(id, ImmutableList.of(alleleList), Collections.<Haplotype>emptyList());
    }

    /**
     * Create a new genotype with the specified identifier and haplotype.
     *
     * @param id identifier for this genotype, must not be null
     * @param haplotype haplotype for this genotype, must not be null
     */
    public Genotype(final String id, final Haplotype haplotype) {
        this(id, Collections.<AlleleList>emptyList(), ImmutableList.of(haplotype));
    }

    /**
     * Create a new genotype with the specified identifier, list of allele lists, and list of haplotypes.
     * The list of allele lists and list of haplotypes must not both be empty.
     *
     * @param id identifier for this genotype, must not be null
     * @param alleleLists list of allele lists for this genotype, must not be null
     * @param haplotypes list of haplotypes for this genotype, must not be null
     */
    public Genotype(final String id, final List<AlleleList> alleleLists, final List<Haplotype> haplotypes) {
        super(id);
        checkNotNull(alleleLists, "alleleLists must not be null");
        checkNotNull(haplotypes, "haplotypes must not be null");
        if (alleleLists.isEmpty() && haplotypes.isEmpty()) {
            throw new IllegalArgumentException("alleleLists and haplotypes must not both be empty");
        }
        this.alleleLists = ImmutableList.copyOf(alleleLists);
        this.haplotypes = ImmutableList.copyOf(haplotypes);
        this.glstring = Joiner.on("+").join(Iterators.concat(this.alleleLists.iterator(), this.haplotypes.iterator()));
    }


    @Override
    public String getGlstring() {
        return glstring;
    }

    /**
     * Return the list of allele lists for this genotype.
     *
     * @return the list of allele lists for this genotype
     */
    public List<AlleleList> getAlleleLists() {
        return alleleLists;
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
}