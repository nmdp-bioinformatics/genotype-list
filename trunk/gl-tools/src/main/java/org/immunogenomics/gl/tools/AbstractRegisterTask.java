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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

/**
 * Abstract register gl resources task.
 */
abstract class AbstractRegisterTask implements Runnable {
    private final File glstringFile;
    private final File identifierFile;


    /**
     * Create a new abstract register task.
     *
     * @param glstringFile glstring file
     * @param identifierFile identifier file
     */
    protected AbstractRegisterTask(final File glstringFile, final File identifierFile) {
        this.glstringFile = glstringFile;
        this.identifierFile = identifierFile;
    }


    protected abstract String register(String glstring); // throws IOException?

    @Override
    public final void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader((glstringFile == null) ? new InputStreamReader(System.in) : new FileReader(glstringFile));
            writer = new PrintWriter(new BufferedWriter(identifierFile == null ? new OutputStreamWriter(System.out) : new FileWriter(identifierFile)), true);

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                String identifier = register(line);
                if (identifier != null) {
                    writer.println(identifier);
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