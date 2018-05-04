/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.nmdp.gl.service.spark;

import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.nmdp.gl.service.GlReader;
import org.nmdp.gl.service.GlWriter;
import org.nmdp.gl.service.Nomenclature;
import org.nmdp.gl.service.nomenclature.hla.ImgtHla3_32_0;
import org.nmdp.gl.service.reader.GlstringGlReader;
import org.nmdp.gl.service.writer.GlstringGlWriter;
import org.nmdp.gl.service.writer.HtmlGlWriter;
import org.nmdp.gl.service.writer.JsonGlWriter;
import org.nmdp.gl.service.writer.N3GlWriter;
import org.nmdp.gl.service.writer.RdfGlWriter;
import org.nmdp.gl.service.writer.XmlGlWriter;
import org.nmdp.gl.service.writer.XlinkXmlGlWriter;

import spark.servlet.SparkApplication;

import com.fasterxml.jackson.core.JsonFactory;
import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * Spark module.
 */
@Immutable
final class SparkModule extends AbstractModule {

    @Override 
    protected void configure() {
        bind(GlReader.class).to(GlstringGlReader.class);
        bind(Nomenclature.class).to(ImgtHla3_32_0.class);
        bind(SparkApplication.class).to(SparkGlService.class);
    }

    @Provides @Singleton
    JsonFactory createJsonFactory() {
        return new JsonFactory();
    }

    @Provides @Singleton
    VelocityEngine createVelocityEngine() {
        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.setProperty("runtime.log.logsystem", new NoopLogChute());
        velocityEngine.init();
        return velocityEngine;
    }

    @Provides @Singleton
    Map<String, GlWriter> createGlWriters(final JsonFactory jsonFactory, final VelocityEngine velocityEngine) {
        GlstringGlWriter glstringWriter = new GlstringGlWriter();
        HtmlGlWriter htmlWriter = new HtmlGlWriter(velocityEngine);
        return new ImmutableMap.Builder<String, GlWriter>()
            .put("gls", glstringWriter)
            .put("glstring", glstringWriter)
            .put("html", htmlWriter)
            .put("htm", htmlWriter)
            .put("rdf", new RdfGlWriter(velocityEngine))
            .put("json", new JsonGlWriter(jsonFactory))
            .put("n3", new N3GlWriter(velocityEngine))
            .put("xml", new XmlGlWriter(velocityEngine))
            .put("xlinkxml", new XlinkXmlGlWriter(velocityEngine)) 
            .build();
    }

    /**
     * No-op log chute to suppress Velocity logging.
     */
    private static class NoopLogChute implements LogChute {

        @Override
        public void init(final RuntimeServices runtimeServices) throws Exception {
            // empty
        }

        @Override
        public boolean isLevelEnabled(final int level) {
            return false;
        }

        @Override
        public void log(final int level, final String value, final Throwable throwable) {
            // empty
        }

        @Override
        public void log(final int level, final String value) {
            // empty
        }
    }
}
