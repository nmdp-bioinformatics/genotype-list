/*

    gl-imgt-biosql  IMGT/HLA in biosql.
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
package org.immunogenomics.gl.imgt.biosql;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.IOException;

import org.biojava.bio.BioException;
import org.biojava.bio.seq.*;
import org.biojava.bio.symbol.*;
import org.biojava.bio.seq.io.SymbolTokenization;

import org.biojavax.*;
import org.biojavax.bio.seq.*;
import org.biojavax.bio.seq.io.*;
import org.biojavax.bio.db.*;
import org.biojavax.bio.db.biosql.*;

import org.dishevelled.commandline.ArgumentList;
import org.dishevelled.commandline.CommandLine;
import org.dishevelled.commandline.CommandLineParseException;
import org.dishevelled.commandline.CommandLineParser;
import org.dishevelled.commandline.Switch;
import org.dishevelled.commandline.Usage;

import org.dishevelled.commandline.argument.FileArgument;
import org.dishevelled.commandline.argument.StringArgument;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import org.hibernate.cfg.Configuration;

/**
 * Load IMGT/HLA from the file <code>hla.dat</code> into biosql.
 */
public final class LoadImgtHla implements Runnable {
    private final String namespace;
    private final File hlaDatFile;
    private static final String USAGE = "java LoadImgtHla --namespace http://localhost/3.15.0 [--hla-dat-file hla.dat]";

    public LoadImgtHla(final String namespace, final File hlaDatFile) {
        checkNotNull(namespace);
        this.namespace = namespace;
        this.hlaDatFile = hlaDatFile;
    }

    @Override
    public void run() {
        Session session = null;
        SessionFactory sessionFactory = null;
        Transaction tx = null;
        BufferedReader reader = null;
        try {
            reader = (hlaDatFile == null) ? new BufferedReader(new InputStreamReader(System.in)) : new BufferedReader(new FileReader(hlaDatFile));
            sessionFactory = new Configuration().configure().buildSessionFactory();
            session = sessionFactory.openSession();

            //RichObjectFactory.connectToBioSQL(session);
            BioSQLRichObjectBuilder richObjectBuilder = new BioSQLRichObjectBuilder(session);
            RichObjectFactory.setRichObjectBuilder(richObjectBuilder);
            RichSequenceDB db = new BioSQLRichSequenceDB(session);
            RichSequenceFormat imgtHlaEmbl = new ImgtHlaEmblFormat();
            RichSequenceBuilderFactory factory = RichSequenceBuilderFactory.THRESHOLD; 
            Namespace ns = (Namespace) RichObjectFactory.getObject(SimpleNamespace.class, new Object[]{ namespace });
            SymbolTokenization dna = DNATools.getDNA().getTokenization("token");
            RichStreamReader streamReader = new RichStreamReader(reader, imgtHlaEmbl, dna, factory, ns);

            System.out.println("reading...");
            int count = 0;
            while (streamReader.hasNext()) {
                RichSequence richSequence = streamReader.nextRichSequence();

                try {
                    tx = session.beginTransaction();
                    db.addRichSequence(richSequence);
                    tx.commit();
                    count++;
                }
                catch (Exception e) {
                    if (tx != null) {
                        System.out.println("rolling back transaction...");
                        tx.rollback();
                    }
                    throw new IOException("could not persist sequence " + count + " " + richSequence, e);
                }

                // show progress
                if ((count % 10) == 0) {
                    System.out.print(".");
                }
                if ((count % 50) == 0) {
                    System.out.print("\n");
                }
                System.out.println("done");
            }
        }
        catch (BioException e) {
            e.printStackTrace();
            System.exit(1);
        }
        catch (IOException e) {
            e.printStackTrace();
            System.exit(2);
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
            try {
                session.close();
            }
            catch (Exception e) {
                // ignore
            }
            try {
                sessionFactory.close();
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
    public static void main(final String[] args) {
        Switch help = new Switch("h", "help", "display help message");
        StringArgument namespace = new StringArgument("n", "namespace", "namespace", true);
        FileArgument hlaDatFile = new FileArgument("i", "hla-dat-file", "hla.dat file", false);

        ArgumentList arguments = new ArgumentList(help, namespace, hlaDatFile);
        CommandLine commandLine = new CommandLine(args);
        try
        {
            CommandLineParser.parse(commandLine, arguments);
            if (help.wasFound()) {
                Usage.usage(USAGE, null, commandLine, arguments, System.out);
                System.exit(-2);
            }
            new LoadImgtHla(namespace.getValue(), hlaDatFile.getValue()).run();
        }
        catch (CommandLineParseException e) {
            Usage.usage(USAGE, e, commandLine, arguments, System.err);
            System.exit(-1);
        }
    }
}
