/*

    gl-liftover-service-build-tools  Genotype list liftover service build tools.
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
package org.nmdp.gl.liftover.build.tools;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Properties;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

import org.apache.velocity.app.Velocity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Create IMGT/HLA version class.
 */
public final class CreateImgtHlaVersion {

    /** Logger. */
    private static final Logger logger = LoggerFactory.getLogger(CreateImgtHlaVersion.class);


    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static void main(final String[] args) throws Exception {
        if (args.length < 1) {
            throw new IllegalArgumentException("specify version in args, e.g. create-imgt-hla-version 3.99.0");
        }
        String version = args[0];
        logger.info("Creating nomenclature class for IMGT/HLA version " + version + "...");

        FileWriter fw = null;
        String fileName = "ImgtHla" + version.replace(".", "_") + ".java";
        try {
            Properties p = new Properties();
            p.setProperty("resource.loader", "class");
            p.setProperty("class.resource.loader.class",
                          "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            Velocity.init(p);

            fw = new FileWriter(fileName);

            VelocityContext context = new VelocityContext();
            context.put("version", version);

            Template template = Velocity.getTemplate("org/nmdp/gl/liftover/build/tools/ImgtHlaVersion.wm");
            template.merge(context, fw);

            logger.info("Wrote file " + fileName + ".");
        }
        catch (Exception e) {
            logger.error("could not write file " + fileName, e);
            System.exit(1);
        }
        finally {
            try {
                fw.close();
            }
            catch (Exception e) {
                // empty
            }
        }
    }
}
