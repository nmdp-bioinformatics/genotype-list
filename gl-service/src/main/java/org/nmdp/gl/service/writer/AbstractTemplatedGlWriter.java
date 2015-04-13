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
package org.nmdp.gl.service.writer;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;
import java.io.Writer;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;
import org.nmdp.gl.service.GlWriter;

/**
 * Abstract templated writer.
 */
abstract class AbstractTemplatedGlWriter implements GlWriter {
    protected Template locusTemplate;
    protected Template alleleTemplate;
    protected Template alleleListTemplate;
    protected Template haplotypeTemplate;
    protected Template genotypeTemplate;
    protected Template genotypeListTemplate;
    protected Template multilocusUnphasedGenotypeTemplate;

    @Override
    public final void writeLocus(final Locus locus, final Writer writer) throws IOException {
        checkNotNull(locus);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("locus", locus);
        locusTemplate.merge(context, writer);
    }

    @Override
    public final void writeAllele(final Allele allele, final Writer writer) throws IOException {
        checkNotNull(allele);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("allele", allele);
        alleleTemplate.merge(context, writer);
    }

    @Override
    public final void writeAlleleList(final AlleleList alleleList, final Writer writer) throws IOException {
        checkNotNull(alleleList);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("alleleList", alleleList);
        alleleListTemplate.merge(context, writer);
    }

    @Override
    public final void writeHaplotype(final Haplotype haplotype, final Writer writer) throws IOException {
        checkNotNull(haplotype);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("haplotype", haplotype);
        haplotypeTemplate.merge(context, writer);
    }

    @Override
    public final void writeGenotype(final Genotype genotype, final Writer writer) throws IOException {
        checkNotNull(genotype);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("genotype", genotype);
        genotypeTemplate.merge(context, writer);
    }

    @Override
    public final void writeGenotypeList(final GenotypeList genotypeList, final Writer writer) throws IOException {
        checkNotNull(genotypeList);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("genotypeList", genotypeList);
        genotypeListTemplate.merge(context, writer);
    }

    @Override
    public final void writeMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype, final Writer writer) throws IOException {
        checkNotNull(multilocusUnphasedGenotype);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("multilocusUnphasedGenotype", multilocusUnphasedGenotype);
        multilocusUnphasedGenotypeTemplate.merge(context, writer);
    }
}