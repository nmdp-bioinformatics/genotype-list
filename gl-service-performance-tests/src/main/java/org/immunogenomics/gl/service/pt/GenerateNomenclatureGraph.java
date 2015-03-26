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

import static com.jayway.restassured.RestAssured.get;
import static org.immunogenomics.gl.service.pt.PerformanceTestUtils.readAlleleUris;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

/**
 * Generate graph structure and node annotation files for import into Cytoscape for a nomenclature graph.
 */
public final class GenerateNomenclatureGraph implements Runnable {
    private final File alleleUriFile;
    private final File nomenclatureGraphFile;
    private static final String USAGE = "java GenerateNomenclatureGraph [-a alleles.txt] [-o nomenclature.txt]";


    /**
     * Create a new generate nomenclature graph task.
     */
    public GenerateNomenclatureGraph() {
        this(null, null);
    }

    /**
     * Create a new generate nomenclature graph task.
     *
     * @param alleleUriFile allele URI file
     * @param nomenclatureGraphFile nomenclature graph file
     */
    public GenerateNomenclatureGraph(final File alleleUriFile, final File nomenclatureGraphFile) {
        this.alleleUriFile = alleleUriFile;
        this.nomenclatureGraphFile = nomenclatureGraphFile;
    }


    @Override
    public void run() {
        PrintWriter writer = null;
        try {
            List<String> alleleUris = (alleleUriFile == null) ? readAlleleUris(System.in) : readAlleleUris(alleleUriFile);
            writer = new PrintWriter(new BufferedWriter(nomenclatureGraphFile == null ? new OutputStreamWriter(System.out) : new FileWriter(nomenclatureGraphFile)));

            StringBuilder sb = new StringBuilder();
            JsonFactory factory = new JsonFactory();
            Set<String> locusUris = new HashSet<String>();
 
            // allele columns are id \t accession \t glstring \t locus \t Allele
           for (String alleleUri : alleleUris) {
                sb.delete(0, sb.length());
                String json = get(alleleUri + ".json").body().asString();
                JsonParser parser = factory.createJsonParser(json);
                parser.nextToken();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String fieldName = parser.getCurrentName();
                    parser.nextToken();
                    if ("locus".equals(fieldName)) {
                        locusUris.add(parser.getText());
                    }
                    sb.append(parser.getText());
                    sb.append("\t");
                }
                parser.close();
                sb.append("\tAllele");
                writer.println(sb.toString());
           }

           // locus columns are id \t \t glstring \t \t Locus
           for (String locusUri : locusUris) {
                sb.delete(0, sb.length());
                sb.append(locusUri);
                sb.append("\t\t");
                String glstring = get(locusUri + ".gls").body().asString();
                sb.append(glstring);
                sb.append("\t\tLocus");
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
    public static void main(final String[] args) {
        CommandLine commandLine = null;
        ArgumentList arguments = null;
        try {
            Switch help = new Switch("h", "help", "display help message");
            FileArgument alleleUriFile = new FileArgument("a", "allele-uris", "allele URI file", false);
            FileArgument nomenclatureGraphFile = new FileArgument("o", "nomenclature-graph", "nomenclature graph file", false);

            arguments = new ArgumentList(help, alleleUriFile, nomenclatureGraphFile);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
            }
            else {
                new GenerateNomenclatureGraph(alleleUriFile.getValue(), nomenclatureGraphFile.getValue()).run();
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