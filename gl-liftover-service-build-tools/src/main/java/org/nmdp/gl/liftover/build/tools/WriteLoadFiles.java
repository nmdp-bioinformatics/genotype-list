/*

    gl-liftover-service-build-tools  Genotype list liftover service build tools.
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
package org.nmdp.gl.liftover.build.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Table;

import org.nmdp.gl.liftover.impl.AllelelistHistoryReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Write load files.
 */
public final class WriteLoadFiles {

    /** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(WriteLoadFiles.class);


    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) throws Exception {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("imgt-hla-");

        logger.info("Reading Allelelist_history.txt from gl-liftover-service classpath...");
        allelelistHistoryReader.readAllelelistHistory();

        logger.info("Reading G groups from g-group-ids.txt and g-groups.txt from gl-liftover-service classpath...");
        allelelistHistoryReader.readGgroups();

        Table<String, String, String> alleleNames = allelelistHistoryReader.getAlleleNames();

        int count = 0;
        for (String namespace : alleleNames.rowKeySet()) {
            PrintWriter writer = null;
            try {
                count++;
                String fileName = namespace.replace("/", "") + ".txt";

                logger.info("Writing to " + fileName + "...");
                writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(fileName))), true);

                List<String> accessions = new ArrayList<String>(alleleNames.columnKeySet());
                Collections.sort(accessions);

                for (String accession : accessions) {
                    String allele = alleleNames.get(namespace, accession);
                    if (allele != null) {
                        writer.println(accession + "\t" + allele);
                    }
                }
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
        logger.info("Wrote " + count + " files.");
    }
}
