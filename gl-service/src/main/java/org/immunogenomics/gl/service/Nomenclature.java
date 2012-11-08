/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service;

import java.io.IOException;
import java.util.List;

import org.immunogenomics.gl.Allele;

/**
 * Nomenclature.
 */
public interface Nomenclature {

    /**
     * Return the name of this nomenclature.
     *
     * @return the name of this nomenclature
     */
    String getName();

    /**
     * Return the version of this nomenclature.
     *
     * @return the version of this nomenclature
     */
    String getVersion();

    /**
     * Return the URL for this nomenclature.
     *
     * @return the URL for this nomenclature
     */
    String getURL();

    /**
     * Load and register all the loci and alleles defined in this nomenclature.
     *
     * @return list of registered alleles
     * @throws IOException if an I/O error occurs
     */
    List<Allele> load() throws IOException;
}