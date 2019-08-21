/*

    gl-liftover-service  Genotype list liftover service.
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
package org.nmdp.gl.liftover.impl;

import static org.junit.Assert.assertEquals;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Table;

import org.junit.Test;

/**
 * Unit test for AllelelistHistoryReader.
 */
public final class AllelelistHistoryReaderTest {

    @Test
    public void testAllelelistHistoryReader() throws Exception {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("https://gl.nmdp.org/imgt-hla/");
        allelelistHistoryReader.readAllelelistHistory();
        allelelistHistoryReader.readGgroups();
        Table<String, String, String> locusNames = allelelistHistoryReader.getLocusNames();
        Table<String, String, String> alleleNames = allelelistHistoryReader.getAlleleNames();

        assertEquals("HLA-A*01:01:01:01", alleleNames.get("https://gl.nmdp.org/imgt-hla/3.17.0/", "HLA00001"));
        assertEquals("HLA-A*010101", alleleNames.get("https://gl.nmdp.org/imgt-hla/2.0.0/", "HLA00001"));
        assertEquals("HLA-A*02:07:01G", alleleNames.get("https://gl.nmdp.org/imgt-hla/3.17.0/", "HGG00006"));
        assertEquals("HLA-C", locusNames.get("https://gl.nmdp.org/imgt-hla/3.17.0/", "HLA-C"));
        assertEquals("HLA-C", locusNames.get("https://gl.nmdp.org/imgt-hla/3.17.0/", "HLA-Cw"));
        assertEquals("HLA-Cw", locusNames.get("https://gl.nmdp.org/imgt-hla/2.0.0/", "HLA-C"));
        assertEquals("HLA-Cw", locusNames.get("https://gl.nmdp.org/imgt-hla/2.0.0/", "HLA-Cw"));
    }


    /*

      run this to generate all.txt file

     */
    void testWriteAll() throws Exception {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("https://gl.nmdp.org/imgt-hla/");
        allelelistHistoryReader.readAllelelistHistory();
        allelelistHistoryReader.readGgroups();
        Table<String, String, String> locusNames = allelelistHistoryReader.getLocusNames();
        Table<String, String, String> alleleNames = allelelistHistoryReader.getAlleleNames();

        PrintWriter writer = null;
        try {
            writer = new PrintWriter(new BufferedWriter(new FileWriter(new File("all.txt"))), true);

            writer.print("accession\t");
            for (String namespace : alleleNames.rowKeySet()) {
                writer.print(namespace + "\t");
            }
            writer.print("\n");
            for (String accession : alleleNames.columnKeySet()) {
                writer.print(accession + "\t");
                for (String namespace : alleleNames.rowKeySet()) {
                    writer.print((alleleNames.get(namespace, accession) == null ? "NA" : alleleNames.get(namespace, accession)) + "\t");
                }
                writer.print("\n");
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

    /*

      run this to generate all imgt-hla-${version}.txt files
      copy to ../gl-service-nomenclature-hla/src/main/resources/org/nmdp/gl/service/nomenclature/hla

     */
    void testWriteLoadFiles() throws Exception {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("imgt-hla-");
        allelelistHistoryReader.readAllelelistHistory();
        allelelistHistoryReader.readGgroups();
        Table<String, String, String> alleleNames = allelelistHistoryReader.getAlleleNames();

        for (String namespace : alleleNames.rowKeySet()) {
            PrintWriter writer = null;
            try {
                writer = new PrintWriter(new BufferedWriter(new FileWriter(new File(namespace.replace("/", "") + ".txt"))), true);

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
    }
}
