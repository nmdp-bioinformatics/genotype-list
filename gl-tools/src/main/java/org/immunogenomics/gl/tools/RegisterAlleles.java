/*

    gl-tools  Genotype list tools.
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
package org.immunogenomics.gl.tools;

import org.immunogenomics.gl.client.GlClient;
import org.immunogenomics.gl.tools.BasicRegisterTask.RegisterCallback;

/**
 * Register alleles task.
 */
public final class RegisterAlleles implements RegisterCallback {


    @Override
    public String register(GlClient client, final String glstring) {
        return client.registerAllele(glstring);
    }

    /**
     * Main.
     *
     * @param args command line arguments
     */
    public static final void main(final String[] args) {
        BasicRegisterTask.register("gl-register-alleles", args, new RegisterAlleles());
    }

}
