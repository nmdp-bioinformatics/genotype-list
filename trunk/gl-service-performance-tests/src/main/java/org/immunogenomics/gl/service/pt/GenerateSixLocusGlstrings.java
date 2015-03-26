/*

    gl-service-performance-tests  Performance tests for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.pt;

import static org.immunogenomics.gl.service.pt.PerformanceTestUtils.readAlleles;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Random;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.IntegerArgument;

import com.google.common.collect.ListMultimap;

/**
 * Generate six-locus (A, B, C, DRB1, DRB3/4/5, DQB1) multilocus unphased genotype glstrings for performance tests.
 */
public final class GenerateSixLocusGlstrings implements Runnable {
    private final int n;
    private final File alleleUriFile;
    private final File sixLocusGlstringsFile;
    private final Random random = new Random();
    private static final int DEFAULT_N = 10;
    private static final String USAGE = "java GenerateSixLocusGlstrings [-n 1000] [-a alleles.txt] [-o glstrings.txt]";

    /**
     * Create a new generate six-locus multilocus unphased genotype glstrings task.
     *
     * @param n number of threads, must be &gt;= zero
     */
    public GenerateSixLocusGlstrings(final int n) {
        this(n, null, null);
    }

    /**
     * Create a new generate six-locus multilocus unphased genotype glstrings task.
     *
     * @param n number of threads, must be &gt;= zero
     * @param alleleUriFile allele URI file
     * @param sixLocusGlstringsFile six locus glstrings file
     */
    public GenerateSixLocusGlstrings(final int n, final File alleleUriFile, final File sixLocusGlstringsFile) {
        if (n < 0) {
            throw new IllegalArgumentException("n must be at least zero");
        }
        this.n = n;
        this.alleleUriFile = alleleUriFile;
        this.sixLocusGlstringsFile = sixLocusGlstringsFile;
    }

    private String sample(final List<String> values) {
        return values.get(random.nextInt(values.size()));
    }

    private void appendDrbx(final StringBuilder sb, final ListMultimap<String, String> alleles) {
        int v = random.nextInt(16);
        if (v % 2 == 0) {
            sb.append("~");
            sb.append(sample(alleles.get("HLA-DRB3")));
        }
        if (v % 3 == 0) {
            sb.append("~");
            sb.append(sample(alleles.get("HLA-DRB4")));
        }
        if (v % 5 == 0) {
            sb.append("~");
            sb.append(sample(alleles.get("HLA-DRB5")));
        }
    }

    @Override
    public void run() {
        PrintWriter writer = null;
        try {
            ListMultimap<String, String> alleles = (alleleUriFile == null) ? readAlleles(System.in) : readAlleles(alleleUriFile);
            writer = new PrintWriter(new BufferedWriter(sixLocusGlstringsFile == null ? new OutputStreamWriter(System.out) : new FileWriter(sixLocusGlstringsFile)));

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; i++) {
                sb.delete(0, sb.length());

                sb.append(sample(alleles.get("HLA-A")));
                sb.append("+");
                sb.append(sample(alleles.get("HLA-A")));

                sb.append("^");

                sb.append(sample(alleles.get("HLA-B")));
                sb.append("+");
                sb.append(sample(alleles.get("HLA-B")));

                sb.append("^");

                sb.append(sample(alleles.get("HLA-C")));
                sb.append("+");
                sb.append(sample(alleles.get("HLA-C")));

                sb.append("^");

                sb.append(sample(alleles.get("HLA-DRB1")));
                appendDrbx(sb, alleles);
                sb.append("+");
                sb.append(sample(alleles.get("HLA-DRB1")));
                appendDrbx(sb, alleles);

                sb.append("^");

                sb.append(sample(alleles.get("HLA-DQB1")));
                sb.append("+");
                sb.append(sample(alleles.get("HLA-DQB1")));

                writer.println(sb.toString());
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }


    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static final void main(final String[] args) {
        CommandLine commandLine = null;
        ArgumentList arguments = null;
        try {
            Switch help = new Switch("h", "help", "display help message");
            IntegerArgument sixLocusGlstringCount = new IntegerArgument("n", "six-locus-glstring-count", "number of six locus glstrings to create, default " + DEFAULT_N, false);
            FileArgument alleleUriFile = new FileArgument("a", "allele-uris", "allele URI file", false);
            FileArgument sixLocusGlstringFile = new FileArgument("o", "six-locus-glstring-file", "six locus glstring file", false);

            arguments = new ArgumentList(help, sixLocusGlstringCount, alleleUriFile, sixLocusGlstringFile);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
            }
            else {
                int n = sixLocusGlstringCount.wasFound() ? sixLocusGlstringCount.getValue().intValue() : DEFAULT_N;
                new GenerateSixLocusGlstrings(n, alleleUriFile.getValue(), sixLocusGlstringFile.getValue()).run();
            }
        }
        catch (CommandLineParseException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
        }
    }
}