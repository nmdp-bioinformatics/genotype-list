/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.nmdp.gl.service.nomenclature;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.nmdp.gl.Allele;

import org.nmdp.gl.service.GlRegistry;
import org.nmdp.gl.service.GlstringResolver;
import org.nmdp.gl.service.IdResolver;

/**
 * Classpath nomenclature.
 */
public abstract class ClasspathNomenclature extends AbstractNomenclature {
    private final String resource;

    protected ClasspathNomenclature(final String resource,
                                    final GlstringResolver glstringResolver,
                                    final IdResolver idResolver,
                                    final GlRegistry glRegistry) {
        super(glstringResolver, idResolver, glRegistry);
        checkNotNull(resource);
        this.resource = resource;
    }

    @Override
    public final List<Allele> load() throws IOException {
        BufferedReader reader = null;
        List<Allele> alleles = new ArrayList<Allele>(10000);
        try {
            reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(resource)));
            int lineNumber = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] tokens = line.split("\t");
                if (tokens.length < 2) {
                    throw new IOException("line " + lineNumber + " in resource " + resource + " not valid format");
                }
                alleles.add(loadAllele(tokens[1], tokens[0]));
                lineNumber++;
            }
        }
        catch (IOException e) {
            throw e;
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return alleles;
    }
}
