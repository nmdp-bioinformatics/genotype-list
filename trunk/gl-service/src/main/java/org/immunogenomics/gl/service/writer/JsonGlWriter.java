/*

    gl-service  URI-based RESTful service for the gl project.
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

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.google.inject.Inject;

/**
 * Writer for JSON format using Jackson streaming APIs.
 */
public final class JsonGlWriter implements GlWriter {
    private static final String CONTENT_TYPE = "application/json";
    private final JsonFactory jsonFactory;

    @Inject
    public JsonGlWriter(final JsonFactory jsonFactory) {
        this.jsonFactory = jsonFactory;
    }

    @Override
    public String getContentType() {
        return CONTENT_TYPE;
    }

    @Override
    public final void writeLocus(final Locus locus, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", locus.getId());
        generator.writeStringField("glstring", locus.getGlstring());
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeAllele(final Allele allele, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", allele.getId());
        generator.writeStringField("accession", allele.getAccession());
        generator.writeStringField("glstring", allele.getGlstring());
        generator.writeStringField("locus", allele.getLocus().getId());
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeAlleleList(final AlleleList alleleList, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", alleleList.getId());
        generator.writeStringField("glstring", alleleList.getGlstring());
        generator.writeFieldName("alleles");
        generator.writeStartArray();
        for (Allele allele : alleleList.getAlleles()) {
            generator.writeStartObject();
            generator.writeStringField("allele", allele.getId());
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeHaplotype(final Haplotype haplotype, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", haplotype.getId());
        generator.writeStringField("glstring", haplotype.getGlstring());
        generator.writeFieldName("alleleLists");
        generator.writeStartArray();
        for (AlleleList alleleList : haplotype.getAlleleLists()) {
            generator.writeStartObject();
            generator.writeStringField("alleleList", alleleList.getId());
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeGenotype(final Genotype genotype, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", genotype.getId());
        generator.writeStringField("glstring", genotype.getGlstring());
        generator.writeFieldName("haplotypes");
        generator.writeStartArray();
        for (Haplotype haplotype : genotype.getHaplotypes()) {
            generator.writeStartObject();
            generator.writeStringField("haplotype", haplotype.getId());
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeGenotypeList(final GenotypeList genotypeList, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", genotypeList.getId());
        generator.writeStringField("glstring", genotypeList.getGlstring());
        generator.writeFieldName("genotypes");
        generator.writeStartArray();
        for (Genotype genotype : genotypeList.getGenotypes()) {
            generator.writeStartObject();
            generator.writeStringField("genotype", genotype.getId());
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
    }

    @Override
    public final void writeMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype, final Writer writer) throws IOException {
        JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
        generator.writeStartObject();
        generator.writeStringField("id", multilocusUnphasedGenotype.getId());
        generator.writeStringField("glstring", multilocusUnphasedGenotype.getGlstring());
        generator.writeFieldName("genotypeLists");
        generator.writeStartArray();
        for (GenotypeList genotypeList : multilocusUnphasedGenotype.getGenotypeLists()) {
            generator.writeStartObject();
            generator.writeStringField("genotypeList", genotypeList.getId());
            generator.writeEndObject();
        }
        generator.writeEndArray();
        generator.writeEndObject();
        generator.close();
    }
}