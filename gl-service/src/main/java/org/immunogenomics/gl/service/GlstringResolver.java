/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service;

/**
 * Genotype list GL String --&gt; identifier resolver.
 */
public interface GlstringResolver {

    /**
     * Resolve the specified locus in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring locus in GL String format, must not be null or empty
     * @return the identifier for the specified locus
     */
    String resolveLocus(String glstring);

    /**
     * Resolve the specified allele in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring allele in GL String format, must not be null or empty
     * @return the identifier for the specified allele
     */
    String resolveAllele(String glstring);

    /**
     * Resolve the specified allele list in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring allele list in GL String format, must not be null or empty
     * @return the identifier for the specified allele list
     */
    String resolveAlleleList(String glstring);

    /**
     * Resolve the specified haplotype in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring haplotype in GL String format, must not be null or empty
     * @return the identifier for the specified haplotype
     */
    String resolveHaplotype(String glstring);

    /**
     * Resolve the specified genotype in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring genotype in GL String format, must not be null or empty
     * @return the identifier for the specified genotype
     */
    String resolveGenotype(String glstring);

    /**
     * Resolve the specified genotype list in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring genotype list in GL String format, must not be null or empty
     * @return the identifier for the specified genotype list
     */
    String resolveGenotypeList(String glstring);

    /**
     * Resolve the specified multilocus unphased genotype in GL String format to an identifier, creating one if necessary.
     *
     * @param glstring multilocus unphased genotype in GL String format, must not be null or empty
     * @return the identifier for the specified multilocus unphased genotype
     */
    String resolveMultilocusUnphasedGenotype(String glstring);
}