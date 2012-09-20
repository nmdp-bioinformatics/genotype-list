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

import com.fasterxml.jackson.core.JsonFactory;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.StringArgument;

import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.json.JsonGlClient;

/**
 * Register alleles task.
 */
public final class RegisterAlleles extends AbstractRegisterTask {
    private final GlClient client;
    private static final String USAGE = "gl-register-alleles --namespace http://localhost:8080/gl [-g glstrings.txt] [-i identifiers.txt]";


    /**
     * Create a new register alleles task.
     *
     * @param client gl client, must not be null
     */
    public RegisterAlleles(final GlClient client) {
        this(client, null, null);
    }

    /**
     * Create a new register alleles task.
     *
     * @param client gl client, must not be null
     * @param glstringFile glstring file
     * @param identifierFile identifier file
     */
    public RegisterAlleles(final GlClient client, final File glstringFile, final File identifierFile) {
        super(glstringFile, identifierFile);
        if (client == null) {
            throw new IllegalArgumentException("client must not be null");
        }
        this.client = client;
    }


    @Override
    protected String register(final String glstring) {
        return client.registerAllele(glstring);
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
            FileArgument glstringFile = new FileArgument("g", "glstrings", "glstring file", false);
            FileArgument identifierFile = new FileArgument("i", "identifiers", "identifier file", false);

            arguments = new ArgumentList(help, namespace, glstringFile, identifierFile);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
            }
            else {
                GlClient client = new JsonGlClient(namespace.getValue(), new JsonFactory());
                new RegisterAlleles(client, glstringFile.getValue(), identifierFile.getValue()).run();
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
