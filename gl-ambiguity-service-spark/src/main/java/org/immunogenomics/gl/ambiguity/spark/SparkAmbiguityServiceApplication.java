/*

    gl-ambiguity-service-spark  Implementation of a RESTful genotype list ambiguity service using Spark.
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
package org.immunogenomics.gl.ambiguity.spark;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import org.immunogenomics.gl.ambiguity.impl.AmbiguityServiceModule;

import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.client.cache.CacheGlClientModule;
import org.immunogenomics.gl.client.http.HttpClient;
import org.immunogenomics.gl.client.http.restassured.RestAssuredHttpClient;
import org.immunogenomics.gl.client.json.JsonGlClient;

import org.immunogenomics.gl.service.Namespace;

import spark.servlet.SparkApplication;

/**
 * Wrapper for SparkAmbiguityService to allow Guice injection before initialization.
 */
public final class SparkAmbiguityServiceApplication implements SparkApplication {
    @Override
    public void init() {
        Injector injector = Guice.createInjector(new SparkAmbiguityServiceModule(),
                                                 new AmbiguityServiceModule(),
                                                 new CacheGlClientModule(),
                                                 new JsonGlClientModule());
        SparkApplication application = injector.getInstance(SparkApplication.class);
        application.init();
    }

    /**
     * JSON gl client module.
     */
    private class JsonGlClientModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(GlClient.class).to(JsonGlClient.class);
            bind(HttpClient.class).to(RestAssuredHttpClient.class);
            bind(String.class).annotatedWith(Namespace.class).toInstance("https://gl.immunogenomics.org/imgt-hla/3.18.0/");
        }
    }
}
