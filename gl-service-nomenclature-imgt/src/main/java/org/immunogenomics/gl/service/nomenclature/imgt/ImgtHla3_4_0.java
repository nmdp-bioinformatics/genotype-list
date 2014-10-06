/*

    gl-service-nomenclature-imgt  IMGT/HLA nomenclature.
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
package org.immunogenomics.gl.service.nomenclature.imgt;

import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;

import org.immunogenomics.gl.service.nomenclature.ClasspathNomenclature;

import com.google.inject.Inject;

/**
 * IMGT/HLA version 3.4.0 nomenclature.
 */
public final class ImgtHla3_4_0 extends ClasspathNomenclature {

    @Inject
    public ImgtHla3_4_0(final GlstringResolver glstringResolver,
                         final IdResolver idResolver,
                         final GlRegistry glRegistry) {
        super("imgt-hla-3.4.0.txt", glstringResolver, idResolver, glRegistry);
    }

    @Override
    public String getName() {
        return "IMGT/HLA Database";
    }

    @Override
    public String getVersion() {
        return "3.4.0";
    }

    @Override
    public String getURL() {
        return "http://www.ebi.ac.uk/ipd/imgt/hla/";
    }
}
