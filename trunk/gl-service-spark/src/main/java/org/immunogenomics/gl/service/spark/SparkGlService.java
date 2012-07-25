/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.immunogenomics.gl.service.spark;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static com.google.common.base.Strings.isNullOrEmpty;
import static spark.Spark.get;
import static spark.Spark.post;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.GlResource;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlReader;
import org.immunogenomics.gl.service.GlWriter;
import org.immunogenomics.gl.service.IdResolver;
import org.immunogenomics.gl.service.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.servlet.SparkApplication;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;

/**
 * Webapp implementation of a URI-based RESTful service for the gl project using Spark.
 */
public final class SparkGlService implements SparkApplication {
    private static final Pattern ID_PATTERN = Pattern.compile("^([a-zA-Z0-9]+)\\.([a-zA-Z0-9]+)$");
    private final String ns;
    private final GlReader glReader;
    private final IdResolver idResolver;
    private final Map<String, GlWriter> glWriters;
    private final Logger logger = LoggerFactory.getLogger(SparkGlService.class);

    @Inject
    public SparkGlService(@Namespace final String ns, final GlReader glReader, final IdResolver idResolver, final Map<String, GlWriter> glWriters) {
        this.ns = ns;
        this.glReader = glReader;
        this.idResolver = idResolver;
        this.glWriters = glWriters;
    }


    @Override
    public void init() {
        get(new GetRoute<Locus>("/locus/:id") {
                @Override
                protected Locus findGlResource(final String id) {
                    return idResolver.findLocus(id);
                }

                @Override
                protected void writeGlResource(final GlWriter glWriter, final Locus locus, final Writer writer) throws IOException {
                    glWriter.writeLocus(locus, writer);
                }
            });

        post(new PostRoute("/locus") {
                @Override
                protected GlResource readGlResource(final String value) throws IOException {
                    return glReader.readLocus(value);
                }
            });

        get(new GetRoute<Allele>("/allele/:id") {
            @Override
            protected Allele findGlResource(final String id) {
                return idResolver.findAllele(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final Allele allele, final Writer writer) throws IOException {
                glWriter.writeAllele(allele, writer);
            }
        });

        post(new PostRoute("/allele") {
                @Override
                protected GlResource readGlResource(final String value) throws IOException {
                    return glReader.readAllele(value, "");
                }
            });

        get(new GetRoute<AlleleList>("/allele-list/:id") {
            @Override
            protected AlleleList findGlResource(final String id) {
                return idResolver.findAlleleList(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final AlleleList alleleList, final Writer writer) throws IOException {
                glWriter.writeAlleleList(alleleList, writer);
            }
        });

        post(new PostRoute("/allele-list") {
            @Override
            protected GlResource readGlResource(final String value) throws IOException {
                return glReader.readAlleleList(value);
            }
        });

        get(new GetRoute<Haplotype>("/haplotype/:id") {
            @Override
            protected Haplotype findGlResource(final String id) {
                return idResolver.findHaplotype(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final Haplotype haplotype, final Writer writer) throws IOException {
                glWriter.writeHaplotype(haplotype, writer);
            }
        });

        post(new PostRoute("/haplotype") {
            @Override
            protected GlResource readGlResource(final String value) throws IOException {
                return glReader.readHaplotype(value);
            }
        });

        get(new GetRoute<Genotype>("/genotype/:id") {
            @Override
            protected Genotype findGlResource(final String id) {
                return idResolver.findGenotype(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final Genotype genotype, final Writer writer) throws IOException {
                glWriter.writeGenotype(genotype, writer);
            }
        });

        post(new PostRoute("/genotype") {
            @Override
            protected GlResource readGlResource(final String value) throws IOException {
                return glReader.readGenotype(value);
            }
        });

        get(new GetRoute<GenotypeList>("/genotype-list/:id") {
            @Override
            protected GenotypeList findGlResource(final String id) {
                return idResolver.findGenotypeList(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final GenotypeList genotypeList, final Writer writer) throws IOException {
                glWriter.writeGenotypeList(genotypeList, writer);
            }
        });

        post(new PostRoute("/genotype-list") {
            @Override
            protected GlResource readGlResource(final String value) throws IOException {
                return glReader.readGenotypeList(value);
            }
        });

        get(new GetRoute<MultilocusUnphasedGenotype>("/multilocus-unphased-genotype/:id") {
            @Override
            protected MultilocusUnphasedGenotype findGlResource(final String id) {
                return idResolver.findMultilocusUnphasedGenotype(id);
            }

            @Override
            protected void writeGlResource(final GlWriter glWriter, final MultilocusUnphasedGenotype multilocusUnphasedGenotype, final Writer writer) throws IOException {
                glWriter.writeMultilocusUnphasedGenotype(multilocusUnphasedGenotype, writer);
            }
        });

        post(new PostRoute("/multilocus-unphased-genotype") {
            @Override
            protected GlResource readGlResource(final String value) throws IOException {
                return glReader.readMultilocusUnphasedGenotype(value);
            }
        });

        // todo:  replace with proper IMGT load mechanism
        post(new Route("/load-imgt-alleles") {
                @Override
                public Object handle(final Request request, final Response response) {
                    try {
                        //long locusCount = loci.size();
                        //long alleleCount = alleles.size();
                        loadImgtAlleles();
                        response.status(307);
                        response.redirect(".");
                        return "Redirect";
                        //response.status(200);
                        //response.type("text/plain");
                        //StringBuilder sb = new StringBuilder();
                        //sb.append("Loaded " + (loci.size() - locusCount) + " IMGT loci\n");
                        //sb.append("Loaded " + (alleles.size() - alleleCount) + " IMGT alleles\n");
                        //logger.trace("Loaded " + (loci.size() - locusCount) + " IMGT loci\n");
                        //logger.trace("Loaded " + (alleles.size() - alleleCount) + " IMGT alleles\n");
                        //return sb.toString();
                    }
                    catch (IOException e) {
                        response.status(400);
                        response.type("text/plain");
                        logger.warn("Failed to load IMGT alleles, caught {}", e.getMessage());
                        return "Failed to load IMGT alleles";
                    }
                }
            });
    }

    /**
     * Abstract HTTP GET route.
     *
     * @param <T> gl resource type
     */
    private abstract class GetRoute<T extends GlResource> extends Route {
        private final String type;
        private final String name;

        private GetRoute(final String path) {
            super(path);
            this.type = path.replace("/", "").replace(":id", "");
            this.name = type.replace("/", "").replace('-', ' ');
        }

        @Override
        public Object handle(final Request request, final Response response) {
            Matcher m = ID_PATTERN.matcher(request.params(":id"));
            if (m.matches()) {
                String id = m.group(1);
                String fileExtension = m.group(2);
                T glResource = findGlResource(ns + type + "/" + id);
                if (glResource != null) {
                    // hack to support qrcode redirect
                    if ("png".equals(fileExtension)) {
                        response.status(307);
                        response.redirect("http://chart.apis.google.com/chart?cht=qr&chs=128x128&chld=L&choe=UTF-8&chl=" + encode(glResource.getId()));
                        return "Redirect";
                    }
                    GlWriter writer = glWriters.get(fileExtension);
                    if (writer == null) {
                        response.status(404);
                        response.type("text/plain");
                        logger.warn("Invalid file extension (404) {}, file extension {}", request.params(":id"), fileExtension);
                        return "Invalid file extension";
                    }
                    StringWriter sw = new StringWriter();
                    try {
                        writeGlResource(writer, glResource, sw);
                    }
                    catch (IOException e) {
                        response.status(500);
                        response.type("text/plain");
                        logger.warn("Could not write {} (500) {}, caught {}", new Object[] { name, request.params(":id"), e.getMessage() });
                        return "Could not write " + name;
                    }
                    response.status(200);
                    response.type(writer.getContentType());
                    logger.trace("OK (200) {}", request.params(":id"));
                    return sw.toString();
                }
                else {
                    response.status(404);
                    response.type("text/plain");
                    logger.warn("{} not found (404) {}", capitalize(name), request.params(":id"));
                    return capitalize(name) + " not found";
                }
            }
            response.status(404);
            response.type("text/plain");
            logger.warn("Invalid identifier or file extension (404) {}", request.params(":id"));
            return "Invalid identifier or file extension";
        }

        /**
         * Resolve the specified identifier to a gl resource, if such exists.
         *
         * @param id gl resource identifier to resolve
         * @return the gl resource for the specified identifier, or <code>null</code> if no such gl resource exists
         */
        protected abstract T findGlResource(String id);

        /**
         * Write the specified gl resource to the specified writer.
         *
         * @param glWriter gl writer
         * @param glResource gl resource to write
         * @param writer writer to write to
         * @throws IOException if an I/O error occurs
         */
        protected abstract void writeGlResource(GlWriter glWriter, T glResource, Writer writer) throws IOException;
    }

    /**
     * Abstract HTTP POST route.
     */
    private abstract class PostRoute extends Route {
        private final String name;

        private PostRoute(final String path) {
            super(path);
            this.name = path.replace("/", "").replace('-', ' ');
        }

        @Override
        public Object handle(final Request request, final Response response) {
            if (isNullOrEmpty(request.body())) {
                response.status(400);
                response.type("text/plain");
                logger.warn("Unable to create {} (400), request body was empty", name);
                return "Unable to create " + name;
            }
            try {
                GlResource glResource = readGlResource(request.body());
                response.status(201);
                response.type("text/plain");
                response.header("Location", glResource.getId());
                logger.trace("Created (201) Location {}", glResource.getId());
                return glResource.getGlstring();
            }
            catch (IOException e) {
                response.status(400);
                response.type("text/plain");
                logger.warn("Unable to create {} (400), caught {}", name, e.getMessage());
                return "Unable to create " + name;
            }
        }

        /**
         * Read a gl resource from the specified string.
         *
         * @param value gl resource to read
         * @return a gl resource read from the specified string
         * @throws IOException if an I/O error occurs
         */
        protected abstract GlResource readGlResource(final String value) throws IOException;
    }

    private static String encode(final String id) {
        try {
            return URLEncoder.encode(id, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            // ignore
        }
        return "";
    }

    // todo:  replace with proper IMGT load mechanism
    private static final List<String> nonHlaLoci = ImmutableList.of("MICA", "MICB", "MICC", "MICD", "MICE", "PSMB9", "PSMB8", "TAP1", "TAP2");

    private void loadImgtAlleles() throws IOException {
        // save ftp://ftp.ebi.ac.uk/pub/databases/imgt/mhc/hla/Allelelist.txt to src/main/resources/org/immunogenomics/gl/service/Allelelist.txt
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(SparkGlService.class.getResourceAsStream("Allelelist.txt")));
            while (reader.ready()) {
                String line = reader.readLine();
                if (line != null) {
                    String[] tokens = line.split(" ");
                    String accession = tokens[0];
                    String glstring = nonHlaLoci.contains(tokens[1].substring(0, 4)) ? tokens[1] : "HLA-" + tokens[1];
                    Allele allele = glReader.readAllele(glstring, accession);
                    logger.trace("Loaded IMGT allele {} {}", allele.getId(), allele.getGlstring());
                }
            }
        }
        catch (IOException e) {
            throw e;
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
}