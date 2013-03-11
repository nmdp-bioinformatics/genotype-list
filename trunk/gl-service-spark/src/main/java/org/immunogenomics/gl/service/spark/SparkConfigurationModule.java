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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.immunogenomics.gl.config.ConfigurationModule;
import org.immunogenomics.gl.service.AllowNewAlleles;
import org.immunogenomics.gl.service.AllowNewLoci;
import org.immunogenomics.gl.service.Namespace;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Spark configuration module.
 */
public final class SparkConfigurationModule extends ConfigurationModule {
    
    private Logger logger = LoggerFactory.getLogger(getClass());

    /** Name of SystemProperty used to control base URI of Namespace. */
    public static final String BASE_URI_NAME = "BaseURI";
    private static final String GL_SERVICE_PROPERTIES = "gl-service.properties";
    private String contextName;

    /** Workaround until new Spark dependency used. 
     * We need the context path when we initialize, and we can't extend current SparkFilter 
     * and provide it.
     * The workaround is have a GLSparkFilter which attaches the contextName to the thread during initialization.
     * */
    private static ThreadLocal<String> contextNameThreadLocal = new ThreadLocal<String>();

    /** Default configuration pulled from gl-service.properties file in classpath. */
    public SparkConfigurationModule() {
        this.contextName = contextNameThreadLocal.get();
    }
    
    /** Configuration based on some System Properties and the specified contextName. */
    public SparkConfigurationModule(String contextName) {
        this.contextName = contextName;
    }

    
    
    @Override
    protected void bindConfigurations() {
        if (contextName == null) {
            // default behavior
            bindProperties(GL_SERVICE_PROPERTIES);
            bindSystemProperties();
        } else {
            // Use configuration contextName and some properties
            Properties fileProperties = loadPropertiesFile();
            Boolean allowNewAlleles = isTrue(fileProperties, AllowNewAlleles.class);
            Boolean allowNewLoci = isTrue(fileProperties, AllowNewLoci.class);
            String namespace = fileProperties.getProperty(Namespace.class.getName());
            String baseURI = System.getProperty(BASE_URI_NAME);
            if (baseURI != null) {
                namespace = baseURI + contextName + "/";
            }
            logger.debug("namespace = {}", namespace);
            logger.debug("allowNewLoci = {} allowNewAlleles", allowNewLoci, allowNewAlleles);
            bind(String.class).annotatedWith(Namespace.class).toInstance(namespace);
            bind(Boolean.class).annotatedWith(AllowNewAlleles.class).toInstance(allowNewAlleles);
            bind(Boolean.class).annotatedWith(AllowNewLoci.class).toInstance(allowNewLoci);
        }
    }

    private boolean isTrue(Properties fileProperties, Class<?> cls) {
        return "TRUE".equalsIgnoreCase(fileProperties.getProperty(cls.getName()));
    }

    private Properties loadPropertiesFile() {
        Properties properties = new Properties();
        InputStream inputStream = getClass().getResourceAsStream("/" + GL_SERVICE_PROPERTIES);
        try {
            properties.load(inputStream);
            return properties;
        } catch (IOException ex) {
            throw new RuntimeException("could not load " + GL_SERVICE_PROPERTIES, ex);
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setThreadContext(String contextName2) {
        contextNameThreadLocal.set(contextName2);
    }
    
}