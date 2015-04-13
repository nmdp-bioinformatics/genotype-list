/*

    gl-service-nomenclature-ipd  IPD-KIR nomenclature.
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
package org.immunogenomics.gl.service.nomenclature.kir;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Script to generate presence/absence alleles.
 */
public final class GeneratePresenceAbsenceAlleles implements Runnable {

    @Override
    public void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        AtomicInteger id = new AtomicInteger(1);
        try {
            reader = new BufferedReader(new InputStreamReader(System.in));
            writer = new PrintWriter(new BufferedWriter(new OutputStreamWriter(System.out)));

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                if (line.startsWith("KIR")) {
                    writer.printf("NMDP%04d\t%s*%s\n", id.getAndIncrement(), line, "POS");
                    writer.printf("NMDP%04d\t%s*%s\n", id.getAndIncrement(), line, "NEG");
                }
            }
        }
        catch (IOException e) {
            // ignore
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


    /**
     * Main.
     *
     * @param args command line arguments
     */
    // $ cut -f 2 ipd-kir-2.6.1.txt | cut -f 1 -d "*" | sort -u | java GeneratePresenceAbsenceAlleles >> ipd-kir-2.6.1.txt
    public static void main(final String[] args) {
        new GeneratePresenceAbsenceAlleles().run();
    }
}
