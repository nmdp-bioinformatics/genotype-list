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

import java.net.URI;

import org.dishevelled.bitset.ImmutableBitSet;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;

/**
 * Genotype list ambiguity service.
 */
public interface AmbiguityService2 {

    /**
     * Return the bit set representing the specified allele or <code>null</code>
     * if the allele is not recognized by this ambiguity service.
     *
     * @param allele allele, must not be null
     * @return the bit set representing the specified allele or <code>null</code>
     *    if the allele is not recognized by this ambiguity service
     */
    ImmutableBitSet bits(Allele allele);

    /**
     * Return the bit set representing the specified allele list or <code>null</code>
     * if the allele list is not recognized by this ambiguity service.
     *
     * @param alleleList allele list, must not be null
     * @return the bit set representing the specified allele list or <code>null</code>
     *    if the allele list is not recognized by this ambiguity service
     */
    ImmutableBitSet bits(AlleleList alleleList);

    /**
     * Return the allele list registered for the specified name or <code>null</code>
     * if no such allele list exists.
     *
     * @param name name, must not be null
     * @return the allele list registered for the specified name or <code>null</code>
     * if no such allele list exists.
     */
    AlleleList get(String name); // getAllelicAmbiguity?

    /**
     * Register the allele list at the specified identifier for the specified name.
     *
     * @param name name, must not be null
     * @param uri identifier of the allele list to register, must not be null
     * @return the allele list registered for the specified name
     * @throws AmbiguityServiceException if an error occurs
     */
    AlleleList register(String name, URI uri) throws AmbiguityServiceException;

    /**
     * Register the allele list with the specified glstring for the specified name.
     *
     * @param name name, must not be null
     * @param glstring glstring of the allele list to register, must not be null
     * @return the allele list registered for the specified name
     * @throws AmbiguityServiceException if an error occurs
     */
    AlleleList register(String name, String glstring) throws AmbiguityServiceException;

    /**
     * Register the specified allele list for the specified name.
     *
     * @param name name, must not be null
     * @param alleleList allele list to register, must not be null
     * @return the allele list registered for the specified name
     * @throws AmbiguityServiceException if an error occurs
     */
    AlleleList register(String name, AlleleList alleleList) throws AmbiguityServiceException;
}
