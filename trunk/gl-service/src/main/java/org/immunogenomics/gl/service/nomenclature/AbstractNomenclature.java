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
package org.immunogenomics.gl.service.nomenclature;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;
import org.immunogenomics.gl.service.Nomenclature;

/**
 * Abstract implementation of Nomenclature.
 */
abstract class AbstractNomenclature implements Nomenclature {
    private static final Pattern LOCUS_PATTERN = Pattern.compile("^[a-zA-Z0-9-_]+$");
    private static final Pattern ALLELE_PATTERN = Pattern.compile("^([a-zA-Z0-9-_]+)\\*([a-zA-Z0-9:]+)$");

    private final GlstringResolver glstringResolver;
    private final IdResolver idResolver;
    private final GlRegistry glRegistry;

    protected AbstractNomenclature(final GlstringResolver glstringResolver,
                                   final IdResolver idResolver,
                                   final GlRegistry glRegistry) {
        checkNotNull(glstringResolver);
        checkNotNull(idResolver);
        checkNotNull(glRegistry);
        this.glstringResolver = glstringResolver;
        this.idResolver = idResolver;
        this.glRegistry = glRegistry;
    }


    // todo:  copied with mods from GlstringGlReader.java

    /**
     * Load and register the specified locus in GL String format.
     *
     * @param glstring locus in GL String format, must not be null or empty
     * @return the registered locus
     * @throws IOException if an I/O error occurs
     */
    protected final Locus loadLocus(final String glstring) throws IOException {
        final String id = glstringResolver.resolveLocus(glstring);
        Locus locus = idResolver.findLocus(id);
        if (locus == null) {
            Matcher m = LOCUS_PATTERN.matcher(glstring);
            if (m.matches()) {
                locus = new Locus(id, glstring);
                glRegistry.registerLocus(locus);
            }
            else {
                throw new IOException("locus \"" + glstring + "\" not a valid formatted glstring");
            }
        }
        return locus;
    }

    /**
     * Load and register the specified allele in GL String format.
     *
     * @param glstring allele in GL String format, must not be null or empty
     * @param accession allele accession, must not be null
     * @return the registered allele
     * @throws IOException if an I/O error occurs
     */
    protected final Allele loadAllele(final String glstring, final String accession) throws IOException {
        final String id = glstringResolver.resolveAllele(glstring);
        Allele allele = idResolver.findAllele(id);
        if (allele == null) {
            Matcher m = ALLELE_PATTERN.matcher(glstring);
            if (m.matches()) {
                String locusPart = m.group(1);
                Locus locus = loadLocus(locusPart);
                allele = new Allele(id, accession, glstring, locus);
                glRegistry.registerAllele(allele);
            }
            else {
                throw new IOException("allele \"" + glstring + "\" not a valid formatted glstring");
            }
        }
        return allele;
    }
}