/*

    gl-service-performance-tests  Performance tests for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.pt;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.jayway.restassured.RestAssured.get;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.common.io.InputSupplier;

/**
 * Performance test utility methods.
 */
final class PerformanceTestUtils {

    /**
     * Private no-arg constructor.
     */
    private PerformanceTestUtils() {
        // empty
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the
     * specified list of allele glstrings.
     *
     * @param alleleGlstrings list of allele glstrings, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the
     *    specified list of allele glstrings
     */
    static ListMultimap<String, String> readAlleleGlstrings(final List<String> alleleGlstrings) {
        checkNotNull(alleleGlstrings);
        ListMultimap<String, String> alleles = ArrayListMultimap.create(100, alleleGlstrings.size());
        for (String alleleGlstring : alleleGlstrings) {
            String lociGlstring = alleleGlstring.substring(0, alleleGlstring.indexOf("*"));
            alleles.put(lociGlstring, alleleGlstring);
        }
        return alleles;        
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the
     * specified file of allele glstrings.
     *
     * @param file file of allele glstrings, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the
     *    specified file of allele glstrings
     * @throws IOException if an I/O error occurs
     */
    static ListMultimap<String, String> readAlleleGlstrings(final File file) throws IOException {
        checkNotNull(file);
        return readAlleleGlstrings(Files.readLines(file, Charset.forName("UTF-8")));
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the
     * specified input stream of allele glstrings.
     *
     * @param inputStream input stream of allele glstrings, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the
     *    specified input stream of allele glstrings
     * @throws IOException if an I/O error occurs
     */
    static ListMultimap<String, String> readAlleleGlstrings(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        return readAlleleGlstrings(CharStreams.readLines(new InputSupplier<InputStreamReader>() {
                    @Override
                    public InputStreamReader getInput() {
                        return new InputStreamReader(inputStream);
                    }
                }));
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     * list of allele URIs.
     *
     * @param alleleUris list of allele URIs, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     *    list of allele URIs
     */
    static ListMultimap<String, String> readAlleles(final List<String> alleleUris) {
        checkNotNull(alleleUris);
        ListMultimap<String, String> alleles = ArrayListMultimap.create(100, alleleUris.size());
        for (String alleleUri : alleleUris) {
            String alleleGlstring = get(alleleUri + ".gls").getBody().asString();
            String lociGlstring = alleleGlstring.substring(0, alleleGlstring.indexOf("*"));
            alleles.put(lociGlstring, alleleGlstring);
        }
        return alleles;
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     * file of allele URIs.
     *
     * @param file file of allele URIs, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     *    file of allele URIs
     * @throws IOException if an I/O error occurs
     */
    static ListMultimap<String, String> readAlleles(final File file) throws IOException {
        checkNotNull(file);
        return readAlleles(Files.readLines(file, Charset.forName("UTF-8")));
    }

    /**
     * Return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     * input stream of allele URIs.
     *
     * @param inputStream input stream of allele URIs, must not be null
     * @return a multimap of alleles in glstring format keyed by locus in glstring format for the specified
     *    input stream of allele URIs
     * @throws IOException if an I/O error occurs
     */
    static ListMultimap<String, String> readAlleles(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        return readAlleles(CharStreams.readLines(new InputSupplier<InputStreamReader>() {
                    @Override
                    public InputStreamReader getInput() {
                        return new InputStreamReader(inputStream);
                    }
                }));
    }

    /**
     * Return a list of alleleURIs read from the specified file.  Note the entire file is read into memory.
     *
     * @param file file of allele URIs to read from, must not be null
     * @return a list of allele URIs read from the specified file
     * @throws IOException if an I/O error occurs
     */
    static List<String> readAlleleUris(final File file) throws IOException {
        checkNotNull(file);
        return Files.readLines(file, Charset.forName("UTF-8"));
    }

    /**
     * Return a list of allele URIs read from the specified input stream.  Note the entire input stream is read into
     * memory.
     *
     * @param inputStream input stream of allele URIs to read from, must not be null
     * @return a list of allele URIs read from the specified input stream
     * @throws IOException if an I/O error occurs
     */
    static List<String> readAlleleUris(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        return CharStreams.readLines(new InputSupplier<InputStreamReader>() {
                @Override
                public InputStreamReader getInput() {
                    return new InputStreamReader(inputStream);
                }
            });
    }

    /**
     * Return a list of glstrings read from the specified file.  Note the entire file is read into memory.
     *
     * @param file file of glstrings to read from, must not be null
     * @return a list of glstrings read from the specified file
     * @throws IOException if an I/O error occurs
     */
    static List<String> readGlstrings(final File file) throws IOException {
        checkNotNull(file);
        return Files.readLines(file, Charset.forName("UTF-8"));
    }

    /**
     * Return a list of glstrings read from the specified input stream.  Note the entire input stream is read into
     * memory.
     *
     * @param inputStream input stream of glstrings to read from, must not be null
     * @return a list of glstrings read from the specified input stream
     * @throws IOException if an I/O error occurs
     */
    static List<String> readGlstrings(final InputStream inputStream) throws IOException {
        checkNotNull(inputStream);
        return CharStreams.readLines(new InputSupplier<InputStreamReader>() {
                @Override
                public InputStreamReader getInput() {
                    return new InputStreamReader(inputStream);
                }
            });
    }
}