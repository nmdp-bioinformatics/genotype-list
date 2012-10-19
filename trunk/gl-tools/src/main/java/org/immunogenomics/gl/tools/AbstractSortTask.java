/*

    gl-tools  Genotype list tools.
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
package org.immunogenomics.gl.tools;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.client.GlClient;

/**
 * Abstract sort GL String task.
 */
abstract class AbstractSortTask implements Runnable {
    private final GlClient client;
    private final File identifierFile;
    private final File sortedGlstringFile;
    private final boolean outputIdentifiers;
    private final boolean outputOriginalGlstrings;
    private final boolean outputSortedGlstrings;


    /**
     * Create a new abstract sort task.
     *
     * @param client gl client, must not be null
     * @param identifierFile identifier file
     * @param sortedGlstringFile sorted glstring file
     * @param outputIdentifiers true to output identifiers
     * @param outputOriginalGlstrings true to output original glstrings
     * @param outputSortedGlstrings true to output sorted glstrings
     */
    protected AbstractSortTask(final GlClient client,
                               final File identifierFile,
                               final File sortedGlstringFile,
                               final boolean outputIdentifiers,
                               final boolean outputOriginalGlstrings,
                               final boolean outputSortedGlstrings) {
        checkNotNull(client);
        this.client = client;
        this.identifierFile = identifierFile;
        this.sortedGlstringFile = sortedGlstringFile;
        this.outputIdentifiers = outputIdentifiers;
        this.outputOriginalGlstrings = outputOriginalGlstrings;
        this.outputSortedGlstrings = outputSortedGlstrings;
    }


    protected abstract String sort(AlleleList alleleList);
    protected abstract String sort(Haplotype haplotype);
    protected abstract String sort(Genotype genotype);
    protected abstract String sort(GenotypeList genotypeList);
    protected abstract String sort(MultilocusUnphasedGenotype multilocusUnphasedGenotype);

    private void write(final String identifier, final String originalGlstring, final String sortedGlstring, final PrintWriter writer) {
        if (outputIdentifiers) {
            writer.print(identifier);
            writer.print("\t");
        }
        if (outputOriginalGlstrings) {
            writer.print(originalGlstring);
            writer.print("\t");
        }
        if (outputSortedGlstrings) {
            writer.print(sortedGlstring);
            writer.print("\t");
        }
        writer.print("\n");
    }

    @Override
    public final void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader((identifierFile == null) ? new InputStreamReader(System.in) : new FileReader(identifierFile));
            writer = new PrintWriter(new BufferedWriter(sortedGlstringFile == null ? new OutputStreamWriter(System.out) : new FileWriter(sortedGlstringFile)), true);

            while (reader.ready()) {
                String identifier = reader.readLine();
                if (identifier == null) {
                    break;
                }

                if (identifier.contains("/locus/")) {
                    Locus locus = client.getLocus(identifier);
                    if (locus != null) {
                        write(locus.getId(), locus.getGlstring(), locus.getGlstring(), writer);
                    }
                }
                else if (identifier.contains("/allele/")) {
                    Allele allele = client.getAllele(identifier);
                    if (allele != null) {
                        write(allele.getId(), allele.getGlstring(), allele.getGlstring(), writer);
                    }
                }
                else if (identifier.contains("/allele-list/")) {
                    AlleleList alleleList = client.getAlleleList(identifier);
                    if (alleleList != null) {
                        write(alleleList.getId(), alleleList.getGlstring(), sort(alleleList), writer);
                    }
                }
                else if (identifier.contains("/haplotype/")) {
                    Haplotype haplotype = client.getHaplotype(identifier);
                    if (haplotype != null) {
                        write(haplotype.getId(), haplotype.getGlstring(), sort(haplotype), writer);
                    }
                }
                else if (identifier.contains("/genotype/")) {
                    Genotype genotype = client.getGenotype(identifier);
                    if (genotype != null) {
                        write(genotype.getId(), genotype.getGlstring(), sort(genotype), writer);
                    }
                }
                else if (identifier.contains("/genotype-list/")) {
                    GenotypeList genotypeList = client.getGenotypeList(identifier);
                    if (genotypeList != null) {
                        write(genotypeList.getId(), genotypeList.getGlstring(), sort(genotypeList), writer);
                    }
                }
                else if (identifier.contains("/multilocus-unphased-genotype/")) {
                    MultilocusUnphasedGenotype multilocusUnphasedGenotype = client.getMultilocusUnphasedGenotype(identifier);
                    if (multilocusUnphasedGenotype != null) {
                        write(multilocusUnphasedGenotype.getId(), multilocusUnphasedGenotype.getGlstring(), sort(multilocusUnphasedGenotype), writer);
                    }
                }
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
            try {
                writer.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }
}