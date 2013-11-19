/*

    gl-service-spark  Implementation of a URI-based RESTful service for the gl project using Spark.
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
package org.immunogenomics.gl.service.spark;

import com.google.inject.Guice;
import com.google.inject.Injector;

import org.immunogenomics.gl.service.cache.CacheModule;
import org.immunogenomics.gl.service.id.IdModule;

import spark.servlet.SparkApplication;

/**
 * Wrapper for SparkGlService to allow Guice injection before initialization.
 */
public final class SparkGlServiceApplication implements SparkApplication {
    @Override
    public void init() {
        Injector injector = Guice.createInjector(new SparkConfigurationModule(), new SparkModule(),
                                                 new CacheModule(), new IdModule());
        SparkApplication application = injector.getInstance(SparkApplication.class);
        application.init();
    }
}