/*

    gl-service  URI-based RESTful service for the gl project.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class to fix Allelelist.txt files downloaded from IMGT.
 */
public final class fixAllelelist {
    private static final List<String> nonHlaLoci = Arrays.asList(new String[] { "MICA", "MICB", "MICC", "MICD", "MICE", "PSMB9", "PSMB8", "TAP1", "TAP2" });

    public static void main(final String[] args) {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)), true);

            int lineNumber = 0;
            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String[] tokens = line.split(" ");
                if (tokens.length == 1) {
                    // G group, add empty accession
                    String glstring = nonHlaLoci.contains(tokens[0].substring(0, 4)) ? tokens[0] : "HLA-" + tokens[0];
                    writer.println("\t" + glstring);
                }
                else if (tokens.length == 2) {
                    String accession = tokens[0];
                    String glstring = nonHlaLoci.contains(tokens[1].substring(0, 4)) ? tokens[1] : "HLA-" + tokens[1];
                    writer.println(accession + "\t" + glstring);
                }
                else {
                    throw new IOException("I/O error at line " + lineNumber);
                }
                lineNumber++;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
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