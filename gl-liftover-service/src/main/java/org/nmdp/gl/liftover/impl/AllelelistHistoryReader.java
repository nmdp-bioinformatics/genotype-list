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

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.base.Splitter;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reader for <code>Allele_history.txt</code>.
 */
public final class AllelelistHistoryReader {
    /** Namespace prefix. */
    private final String namespacePrefix;

    /** Table of locus names keyed by namespace and glstring. */
    private final Table<String, String, String> locusNames;

    /** Table of allele names keyed by namespace and accession number. */
    private final Table<String, String, String> alleleNames;

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(AllelelistHistoryReader.class);

    /** Non-HLA loci. */
    private static final List<String> nonHlaLoci = Arrays.asList(new String[] { "MICA", "MICB", "MICC", "MICD", "MICE", "PSMB9", "PSMB8", "TAP1", "TAP2" });


    //@Inject
    public AllelelistHistoryReader(final String namespacePrefix) {
        checkNotNull(namespacePrefix);
        this.namespacePrefix = namespacePrefix;

        locusNames = HashBasedTable.create();
        alleleNames = HashBasedTable.create();

        // readAlleleHistory();
    }


    static String fixVersion(final String version) throws IOException {
        switch (version.length()) {
        case 3:
            return version.charAt(0) + "." + version.charAt(1) + "." + version.charAt(2);
        case 4:
            return version.charAt(0) + "." + ('0' == version.charAt(1) ? "" : String.valueOf(version.charAt(1))) + version.charAt(2) + "." + version.charAt(3);
        default:
            throw new IOException("unidentified version " + version);
        }
    }

    static String fixAllele(final String allele) throws IOException {
        return nonHlaLoci.contains(locus(allele)) ? allele : "HLA-" + allele;
    }

    static String locus(final String allele) throws IOException {
        if (allele.contains("*")) {
            return allele.substring(0, allele.indexOf("*"));
        }
        else {
            throw new IOException("incorrectly formatted allele glstring " + allele);
        }
    }

    public void readAllelelistHistory() throws IOException {
        BufferedReader reader = null;
        boolean readHeaderLine = false;
        List<String> namespaces = new ArrayList<String>();
        namespaces.add("ignore");
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("Allelelist_history.txt")));

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }

                // skip metadata header lines
                if (line.startsWith("#")) {
                    logger.info("Allelelist_history.txt: " + line);
                }
                else {

                    // Allelelist_history.txt was tab-delimited, comma-separated as of version 3.32.0
                    String[] tokens = line.split(",");
                    if (!readHeaderLine) {
                        for (int i = 1; i < tokens.length; i++) {
                            String version = tokens[i];
                            namespaces.add(namespacePrefix + fixVersion(version) + "/");
                        }
                        logger.info("Allelelist_history.txt: " + line);
                        readHeaderLine = true;
                    }
                    else {
                        String accession = tokens[0];
                        for (int i = 1; i < tokens.length; i++) {
                            String glstring = tokens[i];
                            if (!("NA".equals(glstring))) {
                                alleleNames.put(namespaces.get(i), accession, fixAllele(glstring));
                            }
                        }
                    }
                }
            }

            for (String namespace : alleleNames.rowKeySet()) {
                for (String allele : alleleNames.row(namespace).values()) {
                    String locus = locus(allele);
                    locusNames.put(namespace, locus, locus);
                    if ("HLA-C".equals(locus)) {
                        // add mapping Cw --> C
                        locusNames.put(namespace, "HLA-Cw", locus);
                    }
                    if ("HLA-Cw".equals(locus)) {
                        // add mapping C --> Cw
                        locusNames.put(namespace, "HLA-C", locus);
                    }
                }
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }


    public Map<String, String> readGgroupIds() throws IOException {
        BufferedReader reader = null;
        Map<String, String> ggroupIds = new HashMap<String, String>();
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("g-group-ids.txt")));

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                List<String> tokens = Splitter.on("\t").omitEmptyStrings().splitToList(line);
                String alleleToken = tokens.get(0);
                String accession = tokens.get(1);

                ggroupIds.put(alleleToken, accession);
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return ggroupIds;
    }

    public void readGgroups() throws IOException {
        Map<String, String> ggroupIds = readGgroupIds();

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(this.getClass().getResourceAsStream("g-groups.txt")));

            while (reader.ready()) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                List<String> tokens = Splitter.on(" ").omitEmptyStrings().splitToList(line);
                String ggroupToken = tokens.get(0);
                String ggroup = fixAllele(tokens.get(0));
                String accession = ggroupIds.get(ggroupToken);

                if (accession == null) {
                    logger.warn("could not find accession for " + ggroupToken);
                }
                else {
                    for (String alleleToken : Splitter.on("/").split(tokens.get(1))) {
                        String allele = locus(ggroup) + "*" + alleleToken;
                        for (String namespace : alleleNames.rowKeySet()) {
                            if (alleleNames.row(namespace).values().contains(allele)) {
                                alleleNames.put(namespace, accession, ggroup);
                            }
                        }
                    }
                }
            }
        }
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }


    public Table<String, String, String> getLocusNames() {
        return locusNames;
    }

    public Table<String, String, String> getAlleleNames() {
        return alleleNames;
    }
}
