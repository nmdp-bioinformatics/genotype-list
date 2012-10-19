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

import java.io.File;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.fasterxml.jackson.core.JsonFactory;

import com.google.common.base.Joiner;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.StringArgument;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleComparators;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.json.JsonGlClient;

/**
 * Smart sort.
 */
public final class SmartSort extends AbstractSortTask {
    private static final String USAGE = "smart-sort [-i identifiers.txt] [-o sorted-glstrings.txt]";

    /**
     * Create a new smart sort task.
     *
     * @param client gl client, must not be null
     */
    public SmartSort(final GlClient client) {
        this(client, null, null, false, false, true);
    }

    /**
     * Create a new smart sort task.
     *
     * @param client gl client, must not be null
     * @param identifierFile identifier file
     * @param sortedGlstringFile sorted glstring file
     * @param outputIdentifiers true to output identifiers
     * @param outputOriginalGlstrings true to output original glstrings
     * @param outputSortedGlstrings true to output sorted glstrings
     */
    public SmartSort(final GlClient client,
                     final File identifierFile,
                     final File sortedGlstringFile,
                     final boolean outputIdentifiers,
                     final boolean outputOriginalGlstrings,
                     final boolean outputSortedGlstrings) {
        super(client, identifierFile, sortedGlstringFile, outputIdentifiers, outputOriginalGlstrings, outputSortedGlstrings);
    }

    @Override
    protected String sort(final AlleleList alleleList) {
        List<Allele> alleles = new ArrayList<Allele>(alleleList.getAlleles());
        Collections.sort(alleles, AlleleComparators.BY_GLSTRING_ASCENDING);
        return Joiner.on("/").join(alleles);
    }

    @Override
    protected String sort(final Haplotype haplotype) {
        List<String> alleleListGlstrings = new ArrayList<String>();
        for (AlleleList alleleList : haplotype.getAlleleLists()) {
            alleleListGlstrings.add(sort(alleleList));
        }
        return Joiner.on("~").join(alleleListGlstrings);
    }

    @Override
    protected String sort(final Genotype genotype) {
        List<String> haplotypeGlstrings = new ArrayList<String>();
        for (Haplotype haplotype : genotype.getHaplotypes()) {
            haplotypeGlstrings.add(sort(haplotype));
        }
        return Joiner.on("+").join(haplotypeGlstrings);
    }

    @Override
    protected String sort(final GenotypeList genotypeList) {
        List<String> genotypeGlstrings = new ArrayList<String>();
        for (Genotype genotype : genotypeList.getGenotypes()) {
            genotypeGlstrings.add(sort(genotype));
        }
        return Joiner.on("|").join(genotypeGlstrings);
    }

    @Override
    protected String sort(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        List<String> genotypeListGlstrings = new ArrayList<String>();
        for (GenotypeList genotypeList : multilocusUnphasedGenotype.getGenotypeLists()) {
            genotypeListGlstrings.add(sort(genotypeList));
        }
        return Joiner.on("^").join(genotypeListGlstrings);
    }

    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static final void main(final String[] args) {
        CommandLine commandLine = null;
        ArgumentList arguments = null;
        try
        {
            Switch help = new Switch("h", "help", "display help message");
            FileArgument identifierFile = new FileArgument("i", "identifiers", "identifier input file", false);
            FileArgument sortedGlstringFile = new FileArgument("o", "glstrings", "sorted glstring output file", false);
            Switch outputIdentifiers = new Switch("d", "output-identifiers", "include identifiers in output");
            Switch outputOriginalGlstrings = new Switch("g", "output-glstrings", "include original glstrings in output");
            Switch outputSortedGlstrings = new Switch("s", "output-sorted-glstrings", "include sorted glstrings in output");

            arguments = new ArgumentList(help, identifierFile, sortedGlstringFile, outputIdentifiers, outputOriginalGlstrings, outputSortedGlstrings);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
            }
            else {
                GlClient client = new JsonGlClient("http://localhost/", new JsonFactory());

                if (!(outputIdentifiers.wasFound() || outputOriginalGlstrings.wasFound() || outputSortedGlstrings.wasFound())) {
                    // default output sorted glstrings if nothing specified
                    new SmartSort(client, identifierFile.getValue(), sortedGlstringFile.getValue(), false, false, true).run();
                }
                else {
                    new SmartSort(client, identifierFile.getValue(), sortedGlstringFile.getValue(), outputIdentifiers.wasFound(),
                                  outputOriginalGlstrings.wasFound(), outputSortedGlstrings.wasFound()).run();
                }
            }
        }
        catch (CommandLineParseException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}