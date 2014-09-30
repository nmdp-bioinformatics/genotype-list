/*

    gl-tools  Genotype list tools.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;
import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.StringArgument;
import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.GlClientException;
import org.immunogenomics.gl.client.cache.CacheGlClientModule;
import org.immunogenomics.gl.client.http.BearerToken;
import org.immunogenomics.gl.client.http.HttpClient;
import org.immunogenomics.gl.client.http.restassured.RestAssuredHttpClient;
import org.immunogenomics.gl.client.http.restassured.RestAssuredOAuthHttpClient;
import org.immunogenomics.gl.client.json.JsonGlClient;
import org.immunogenomics.gl.client.xml.XmlGlClient;
import org.immunogenomics.gl.service.Namespace;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.util.Providers;

/**
 * Abstract register gl resources task.
 */
abstract class AbstractRegisterTask implements Runnable {
    private final File glstringFile;
    private final File identifierFile;
    protected final GlClient client;
    private static final String USAGE_PARAMS = " --namespace http://localhost:8080/gl/ [-g glstrings.txt] [-i identifiers.txt] [-t bearer-token] [-c json]";


    /**
     * Create a new abstract register task.
     *
     * @param glstringFile glstring file
     * @param identifierFile identifier file
     * @param client gl client, must not be null
     */
    protected AbstractRegisterTask(final File glstringFile, final File identifierFile, final GlClient client) {
        this.glstringFile = glstringFile;
        this.identifierFile = identifierFile;
        this.client = client;
    }


    /**
     * Register a gl resource described by the specified GL String and return its identifier.
     *
     * @param glstring locus in GL String format
     * @return the identifier of the gl resource described by the specified GL String
     * @throws GlClientException if an error occurs
     */
    protected abstract String register(String glstring) throws GlClientException;

    @Override
    public final void run() {
        BufferedReader reader = null;
        PrintWriter writer = null;
        try {
            reader = new BufferedReader((glstringFile == null) ? new InputStreamReader(System.in) : new FileReader(glstringFile));
            writer = new PrintWriter(new BufferedWriter((identifierFile == null) ? new OutputStreamWriter(System.out) : new FileWriter(identifierFile)), true);

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                try {
                    String identifier = register(line);
                    if (identifier != null) {
                        writer.println(identifier);
                    }
                }
                catch (GlClientException e) {
                    // ignore
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

    /**
     * Create and return an injector configured using the specified command line arguments.
     *
     * @param commandName command name, used in usage string
     * @param args command line arguments
     * @return an injector configured using the specified command line arguments
     */
    protected static final Injector init(final String commandName, final String[] args) {
        String usage = commandName + USAGE_PARAMS;
        Switch help = new Switch("h", "help", "display help message");
        final StringArgument namespace = new StringArgument("s", "namespace", "namespace", true);
        final FileArgument glstringFile = new FileArgument("g", "glstrings", "glstring input file", false);
        final FileArgument identifierFile = new FileArgument("i", "identifiers", "identifier output file", false);
        final StringArgument bearerToken = new StringArgument("t", "bearer-token", "OAuth 2.0 bearer token", false);
        final StringArgument client = new StringArgument("c", "client", "client implementation, json or xml, default json", false);

        ArgumentList arguments = new ArgumentList(help, namespace, glstringFile, identifierFile, bearerToken, client);
        CommandLine commandLine = new CommandLine(args);
        try
        {
            CommandLineParser.parse(commandLine, arguments);

            if (help.wasFound()) {
                Usage.usage(usage, null, commandLine, arguments, System.out);
            }
        }
        catch (CommandLineParseException e) {
            Usage.usage(usage, e, commandLine, arguments, System.err);
            System.exit(-1);
        }

        return Guice.createInjector(new CacheGlClientModule(), new AbstractModule() {
            @Override
            protected void configure() {
                bind(String.class).annotatedWith(Namespace.class).toInstance(namespace.getValue());

                if (glstringFile.wasFound()) {
                    bind(File.class).annotatedWith(GlstringFile.class).toInstance(glstringFile.getValue());
                }
                else {
                    bind(File.class).annotatedWith(GlstringFile.class).toProvider(Providers.of((File) null));
                }

                if (identifierFile.wasFound()) {
                    bind(File.class).annotatedWith(IdentifierFile.class).toInstance(identifierFile.getValue());
                }
                else {
                    bind(File.class).annotatedWith(IdentifierFile.class).toProvider(Providers.of((File) null));
                }

                if (bearerToken.wasFound()) {
                    bind(String.class).annotatedWith(BearerToken.class).toInstance(bearerToken.getValue());
                    bind(HttpClient.class).to(RestAssuredOAuthHttpClient.class);
                }
                else {
                    bind(HttpClient.class).to(RestAssuredHttpClient.class);
                }

                if ("xml".equals(client.getValue("json"))) {
                    bind(GlClient.class).to(XmlGlClient.class);
                }
                else {
                    bind(GlClient.class).to(JsonGlClient.class);
                }
            }
        });
    }
}
