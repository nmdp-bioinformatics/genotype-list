/*

    gl-liftover-service-spark  Implementation of a RESTful genotype list liftover service using Spark.
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
package org.nmdp.gl.liftover.spark;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.StringWriter;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.google.inject.Inject;

import org.nmdp.gl.GlResource;

import org.nmdp.gl.liftover.LiftoverService;
import org.nmdp.gl.liftover.LiftoverServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

import spark.servlet.SparkApplication;

/**
 * Implementation of a RESTful genotype list liftover service using Spark.
 */
public final class SparkLiftoverService implements SparkApplication {
    private final JsonFactory jsonFactory;
    private final LiftoverService liftoverService;
    private final Logger logger = LoggerFactory.getLogger(SparkLiftoverService.class);


    @Inject
    SparkLiftoverService(final JsonFactory jsonFactory, final LiftoverService liftoverService) {
        checkNotNull(jsonFactory);
        checkNotNull(liftoverService);
        this.jsonFactory = jsonFactory;
        this.liftoverService = liftoverService;
    }


    @Override
    public void init() {
        get(new Route("/") {
                @Override
                public Object handle(final Request request, final Response response) {
                    response.status(200);
                    response.type("text/plain");
                    return "ok";
                }
            });

        post(new PostRoute("/locus") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverLocus(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/allele") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverAllele(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/allele-list") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverAlleleList(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/haplotype") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverHaplotype(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/genotype") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverGenotype(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/genotype-list") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverGenotypeList(sourceNamespace, sourceUri, targetNamespace);
                }
            });

        post(new PostRoute("/multilocus-unphased-genotype") {
                @Override
                public GlResource liftoverGlResource(final String sourceNamespace, final String sourceUri, final String targetNamespace) throws LiftoverServiceException {
                    return liftoverService.liftoverMultilocusUnphasedGenotype(sourceNamespace, sourceUri, targetNamespace);
                }
            });
    }

    /**
     * Abstract HTTP POST route.
     */
    private abstract class PostRoute extends Route {
        private final String name;

        private PostRoute(final String path) {
            super(path); // todo: accept only application/json
            this.name = path.replace("/", "").replace('-', ' ');
        }


        @Override
        public Object handle(final Request request, final Response response) {
            response.type("application/json");

            if (isNullOrEmpty(request.body())) {
                return badRequest(response, "request body was empty");
            }
            String sourceNamespace = null;
            String sourceUri = null;
            String targetNamespace = null;
            JsonParser parser = null;
            try {
                parser = jsonFactory.createJsonParser(request.body());
                parser.nextToken();
                while (parser.nextToken() != JsonToken.END_OBJECT) {
                    String field = parser.getCurrentName();
                    parser.nextToken();

                    if ("sourceNamespace".equals(field)) {
                        sourceNamespace = parser.getText();
                    }
                    else if ("sourceUri".equals(field)) {
                        sourceUri = parser.getText();
                    }
                    else if ("targetNamespace".equals(field)) {
                        targetNamespace = parser.getText();
                    }
                }

                if (isNullOrEmpty(sourceNamespace)) {
                    return badRequest(response, "missing source namespace");
                }
                if (isNullOrEmpty(sourceUri)) {
                    return badRequest(response, "missing source URI");
                }
                if (isNullOrEmpty(targetNamespace)) {
                    return badRequest(response, "missing target namespace");
                }
                GlResource glResource = liftoverGlResource(sourceNamespace, sourceUri, targetNamespace);
                response.status(200);
                logger.trace("Liftover (200) target URI {}", glResource.getId());
                return responseJson(sourceNamespace, sourceUri, targetNamespace, glResource.getId());
            }
            catch (IOException | LiftoverServiceException e) {
                return badRequest(response, "caught " + e.getMessage());
            }
        }

        private String badRequest(final Response response, final String message) {
            response.status(400);
            logger.warn("Unable to liftover {} (400), {}", name, message);
            return errorJson("Unable to liftover " + name + ", " + message);
        }

        /**
         * Liftover the specified source gl resource to the target namespace.
         *
         * @param sourceNamespace source namespace, must not be null
         * @param sourceUri source gl resource URI, must not be null
         * @param targetNamespace target namespace, must not be null
         * @return a new gl resource registered in the target namespace
         * @throws LiftoverServiceException if an error occurs
         */
        protected abstract GlResource liftoverGlResource(String sourceNamespace, String sourceUri, String targetNamespace) throws LiftoverServiceException;
    }

    private String errorJson(final String error) {
        StringWriter writer = new StringWriter();
        try {
            JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
            generator.writeStartObject();
            generator.writeStringField("error", error);
            generator.writeEndObject();
            generator.close();
        }
        catch (IOException e) {
            // ignore
        }
        return writer.toString();
    }

    private String responseJson(final String sourceNamespace, final String sourceUri, final String targetNamespace, final String targetUri) {
        StringWriter writer = new StringWriter();
        try {
            JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
            generator.writeStartObject();
            generator.writeStringField("sourceNamespace", sourceNamespace);
            generator.writeStringField("sourceUri", sourceUri);
            generator.writeStringField("targetNamespace", targetNamespace);
            generator.writeStringField("targetUri", targetUri);
            generator.writeEndObject();
            generator.close();
        }
        catch (IOException e) {
            // ignore
        }
        return writer.toString();
    }
}
