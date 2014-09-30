/*

    gl-ambiguity-service-spark  Implementation of a RESTful genotype list ambiguity service using Spark.
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
package org.immunogenomics.gl.ambiguity.spark;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

import static spark.Spark.get;
import static spark.Spark.post;

import java.io.StringWriter;
import java.io.IOException;

import java.net.URI;
import java.net.URISyntaxException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;

import com.google.common.net.UrlEscapers;

import com.google.inject.Inject;

import org.immunogenomics.gl.AlleleList;

import org.immunogenomics.gl.ambiguity.AmbiguityService2;
import org.immunogenomics.gl.ambiguity.AmbiguityServiceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.Request;
import spark.Response;
import spark.Route;

import spark.servlet.SparkApplication;

/**
 * Implementation of a RESTful genotype list ambiguity service using Spark.
 */
final class SparkAmbiguityService implements SparkApplication {
    private final JsonFactory jsonFactory;
    private final AmbiguityService2 ambiguityService;
    private final Logger logger = LoggerFactory.getLogger(SparkAmbiguityService.class);


    @Inject
    SparkAmbiguityService(final JsonFactory jsonFactory, final AmbiguityService2 ambiguityService) {
        checkNotNull(jsonFactory);
        checkNotNull(ambiguityService);
        this.jsonFactory = jsonFactory;
        this.ambiguityService = ambiguityService;
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

        get(new Route("ambiguity/${name}") {
                @Override
                public Object handle(final Request request, final Response response) {
                    response.type("application/json");
                    String name = request.params(":name");

                    AlleleList alleleList = ambiguityService.get(name);
                    if (alleleList == null) {
                        response.status(404);
                        return errorJson("Not found");
                    }
                    response.status(200);
                    return responseJson(name, alleleList.getGlstring(), alleleList.getId(), request.url());
                }
            });

        post(new Route("ambiguity") {
                @Override
                public Object handle(final Request request, final Response response) {
                    response.type("application/json");

                    if (isNullOrEmpty(request.body())) {
                        response.status(400);
                        logger.warn("Unable to register allelic ambiguity (400), request body was empty");
                        return errorJson("Unable to register allelic ambiguity, request body was empty");
                    }
                    String name = null;
                    String glstring = null;
                    URI uri = null;
                    JsonParser parser = null;
                    try {
                        parser = jsonFactory.createJsonParser(request.body());
                        parser.nextToken();
                        while (parser.nextToken() != JsonToken.END_OBJECT) {
                            String field = parser.getCurrentName();
                            parser.nextToken();

                            if ("name".equals(field)) {
                                name = parser.getText();
                            }
                            else if ("glstring".equals(field)) {
                                glstring = parser.getText();
                            }
                            else if ("uri".equals(field)) {
                                try {
                                    uri = new URI(parser.getText());
                                }
                                catch (URISyntaxException e) {
                                    throw new IOException("invalid uri", e);
                                }
                            }
                        }

                        if (isNullOrEmpty(name)) {
                            response.status(400);
                            logger.warn("Unable to register allelic ambiguity (400), missing name");
                            return errorJson("Unable to register alleleic ambiguity, missing name");
                        }
                        if (isNullOrEmpty(glstring) && (uri == null)) {
                            response.status(400);
                            logger.warn("Unable to register allelic ambiguity (400), at least one of { glstring, uri } is required");
                            return errorJson("Unable to register alleleic ambiguity, at least one of { glstring, uri } is required");
                        }

                        AlleleList alleleList = null;
                        // use uri if both are specificed
                        if (uri != null) {
                            alleleList = ambiguityService.register(name, uri);
                        }
                        else {
                            alleleList = ambiguityService.register(name, glstring);
                        }

                        if (alleleList == null) {
                            response.status(400);
                            logger.warn("Unable to register allelic ambiguity (400)");
                            return errorJson("Unable to register alleleic ambiguity");
                        }
 
                        response.status(201);
                        String location = request.url() + "/" + urlEncode(name);
                        response.header("Location", location);
                        logger.trace("Registered allelic ambiguity (201) {} Location {}", alleleList.getId(), location);
                        return responseJson(name, glstring, alleleList.getId(), location);
                    }
                    catch (IOException e) {
                        response.status(400);
                        logger.warn("Unable to register allelic ambiguity (400), caught {}", e.getMessage());
                        return errorJson( "Unable to register allelic ambiguity");
                    }
                    catch (AmbiguityServiceException e) {
                        response.status(400);
                        logger.warn("Unable to register allelic ambiguity (400), caught {}", e.getMessage());
                        return errorJson( "Unable to register allelic ambiguity");
                    }
                }
            });
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

    private String responseJson(final String name, final String glstring, final String uri, final String location) {
        StringWriter writer = new StringWriter();
        try {
            JsonGenerator generator = jsonFactory.createJsonGenerator(writer);
            generator.writeStartObject();
            generator.writeStringField("name", name);
            generator.writeEndObject();
            generator.writeStartObject();
            generator.writeStringField("glstring", glstring);
            generator.writeEndObject();
            generator.writeStartObject();
            generator.writeStringField("uri", uri);
            generator.writeEndObject();
            generator.writeStartObject();
            generator.writeStringField("location", location);
            generator.writeEndObject();
            generator.close();
        }
        catch (IOException e) {
            // ignore
        }
        return writer.toString();
    }

    private static String urlEncode(final String name) {
        return UrlEscapers.urlPathSegmentEscaper().escape(name);
    }
}
