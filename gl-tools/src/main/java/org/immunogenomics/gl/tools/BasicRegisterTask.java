/*

    gl-client  Genotype client.
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
 * Basic register command implementation.
 */
public final class BasicRegisterTask extends AbstractRegisterTask {
    private final GlClient client;
    private static final String USAGE_PARAMS = " --namespace http://localhost:8080/gl [-g glstrings.txt] [-i identifiers.txt] [-t bearer-token]";
    private RegisterCallback registerCallback;

    public interface RegisterCallback {
        /** Return the URI for the new glstring. */
        String register(GlClient client, String glstring);
    }

    /**
     * Create a new register loci task.
     *
     * @param client gl client, must not be null
     * @param glstringFile glstring file
     * @param identifierFile identifier file
     * @param registerCallback 
     */
    private BasicRegisterTask(final GlClient client, final File glstringFile, final File identifierFile, RegisterCallback registerCallback) {
        super(glstringFile, identifierFile);
        this.registerCallback = registerCallback;
        if (client == null) {
            throw new IllegalArgumentException("client must not be null");
        }
        this.client = client;
    }


    @Override
    protected String register(final String glstring) {
        return registerCallback.register(client, glstring);
    }

    /**
     * Parse the command's arguments and report any usage issues.
     * 
     * @param args command line arguments
     */
    public static final void register(final String commandName, final String[] args, RegisterCallback registerCallback) {
        CommandLine commandLine = null;
        ArgumentList arguments = null;
        String usage = commandName + USAGE_PARAMS;
        try
        {
            Switch help = new Switch("h", "help", "display help message");
            StringArgument namespace = new StringArgument("s", "namespace", "namespace", true);
            FileArgument glstringFile = new FileArgument("g", "glstrings", "glstring input file", false);
            FileArgument identifierFile = new FileArgument("i", "identifiers", "identifier output file", false);
            StringArgument bearerToken = new StringArgument("t", "token", "bearer token", false);

            arguments = new ArgumentList(help, namespace, glstringFile, identifierFile, bearerToken);
            commandLine = new CommandLine(args);
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(usage, null, commandLine, arguments, System.out);
            }
            else {
                JsonGlClient client = new JsonGlClient(namespace.getValue(), new JsonFactory());
                client.setBearerToken(bearerToken.getValue());
                new BasicRegisterTask(client, glstringFile.getValue(), identifierFile.getValue(), registerCallback).run();
            }
        }
        catch (CommandLineParseException e) {
            Usage.usage(usage, e, commandLine, arguments, System.err);
        }
        catch (IllegalArgumentException e) {
            Usage.usage(usage, e, commandLine, arguments, System.err);
        }
        catch (RuntimeException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
