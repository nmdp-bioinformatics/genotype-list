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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Responsible for determining the configuration for namespace related values.
 * Currently, it only provides the base URL for the GL services.
 */
public class NamespaceConfig {
    private Logger log = LoggerFactory.getLogger(getClass());

    public String readNamespace() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("/gl-service.properties");
            properties.load(inputStream);
        }
        catch (IOException e) {
            log.error("failed to read properties", e);
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                log.warn("readNamespace ignoring " + e);
            }
        }
        String namespace = properties.getProperty("org.immunogenomics.gl.service.spark.namespace");
        String servletPort = System.getProperty("servlet.port");
        if (servletPort != null) {
            namespace = namespace.replace("8080", servletPort);
            System.err.println("servlet.port Namespace: " + namespace);
            return namespace;
        }
        log.info("readNamespace() = {}", namespace);
        log.warn("readNamespace() = {}", namespace);
        return namespace;
    }
}
