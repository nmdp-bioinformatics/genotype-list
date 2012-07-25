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
package org.immunogenomics.gl.service.writer;

import java.io.IOException;
import java.io.Writer;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlWriter;

/**
 * Writer for glstring format.
 */
public final class GlstringGlWriter implements GlWriter {
    private static final String CONTENT_TYPE = "text/plain";

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public void writeLocus(final Locus locus, final Writer writer) throws IOException {
        writer.append(locus.getGlstring());
    }

    @Override
    public void writeAllele(final Allele allele, final Writer writer) throws IOException {
        writer.append(allele.getGlstring());
    }

    @Override
    public void writeAlleleList(final AlleleList alleleList, final Writer writer) throws IOException {
        writer.append(alleleList.getGlstring());
    }

    @Override
    public void writeHaplotype(final Haplotype haplotype, final Writer writer) throws IOException {
        writer.append(haplotype.getGlstring());
    }

    @Override
    public void writeGenotype(final Genotype genotype, final Writer writer) throws IOException {
        writer.append(genotype.getGlstring());
    }

    @Override
    public void writeGenotypeList(final GenotypeList genotypeList, final Writer writer) throws IOException {
        writer.append(genotypeList.getGlstring());
    }

    @Override
    public void writeMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype, final Writer writer) throws IOException {
        writer.append(multilocusUnphasedGenotype.getGlstring());
    }
}