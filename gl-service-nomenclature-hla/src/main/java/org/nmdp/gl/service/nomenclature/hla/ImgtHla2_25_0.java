/*

    gl-service-nomenclature-imgt  IMGT/HLA nomenclature.
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
package org.nmdp.gl.service.nomenclature.hla;

import org.nmdp.gl.service.GlRegistry;
import org.nmdp.gl.service.GlstringResolver;
import org.nmdp.gl.service.IdResolver;

import org.nmdp.gl.service.nomenclature.ClasspathNomenclature;

import com.google.inject.Inject;

/**
 * IMGT/HLA version 2.25.0 nomenclature.
 */
public final class ImgtHla2_25_0 extends ClasspathNomenclature {

    @Inject
    public ImgtHla2_25_0(final GlstringResolver glstringResolver,
                         final IdResolver idResolver,
                         final GlRegistry glRegistry) {
        super("imgt-hla-2.25.0.txt", glstringResolver, idResolver, glRegistry);
    }

    @Override
    public String getName() {
        return "IMGT/HLA Database";
    }

    @Override
    public String getVersion() {
        return "2.25.0";
    }

    @Override
    public String getURL() {
        return "http://www.ebi.ac.uk/ipd/imgt/hla/";
    }
}
