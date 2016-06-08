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
package org.nmdp.gl.service.nomenclature.kir;

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
public final class GeneratePseudoAlleles implements Runnable {

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
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DS4*FUL");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DS4*DEL");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0010101");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0010102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0010103");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00103");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00104");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00105");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0050101");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0050102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0050103");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0050104");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01201");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01202");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*014");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*015");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020101");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020103");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020104");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020105");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020106");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0020107");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00202");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00203");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*003");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*004");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00601");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00602");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00603");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0070101");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0070102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0080101");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*0080102");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00802");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00803");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*00804");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*009");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*010");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*011");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01301");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01302");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01303");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*01304");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*016");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*017");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*018");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*019");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*020");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*POS");
            writer.printf("NMDP%04d\t%s\n", id.getAndIncrement(), "KIR2DL5*NEG");
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
    // $ cut -f 2 ipd-kir-2.6.1.txt | cut -f 1 -d "*" | sort -u | java GeneratePseudoAlleles >> ipd-kir-2.6.1.txt
    public static void main(final String[] args) {
        new GeneratePseudoAlleles().run();
    }
}
