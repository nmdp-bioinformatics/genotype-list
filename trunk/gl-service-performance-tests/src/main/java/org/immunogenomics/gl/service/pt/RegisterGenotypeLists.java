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

import java.io.File;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.IntegerArgument;
import org.dishevelled.commandline.argument.StringArgument;

/**
 * Register genotype lists.
 */
public final class RegisterGenotypeLists extends PartitioningRegisterTask {
    private static final int DEFAULT_N = 2;
    private static final String USAGE = "java RegisterGenotypeLists --namespace http://localhost:8080/gl [-n 4] [-g glstrings.txt]";

    /**
     * Create a new register genotype lists task.
     *
     * @param ns namespace, must not be null
     * @param n number of threads, must be &gt;= zero
     */
    public RegisterGenotypeLists(final String ns, final int n) {
        this(ns, n, null);
    }

    /**
     * Create a new register genotype lists task.
     *
     * @param ns namespace, must not be null
     * @param n number of threads, must be &gt;= zero
     * @param glstringFile glstring file
     */
    public RegisterGenotypeLists(final String ns, final int n, final File glstringFile) {
        super(ns, "genotype-list", n, glstringFile);
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
            StringArgument namespace = new StringArgument("s", "namespace", "namespace", true);
            IntegerArgument threadCount = new IntegerArgument("n", "thread-count", "number of threads, default " + DEFAULT_N, false);
            FileArgument glstringFile = new FileArgument("g", "glstrings", "glstring file", false);

            arguments = new ArgumentList(help, namespace, threadCount, glstringFile);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
            }
            else {
                int n = threadCount.wasFound() ? threadCount.getValue().intValue() : DEFAULT_N;
                new RegisterGenotypeLists(namespace.getValue(), n, glstringFile.getValue()).run();
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