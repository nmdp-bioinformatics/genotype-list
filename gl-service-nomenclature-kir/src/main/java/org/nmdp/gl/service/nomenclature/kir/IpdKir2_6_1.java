/*

    gl-service-nomenclature-ipd  IPD-KIR nomenclature.
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
package org.nmdp.gl.service.nomenclature.kir;

import org.nmdp.gl.service.GlRegistry;
import org.nmdp.gl.service.GlstringResolver;
import org.nmdp.gl.service.IdResolver;

import org.nmdp.gl.service.nomenclature.ClasspathNomenclature;

import com.google.inject.Inject;

/**
 * IPD-KIR version 2.6.1 nomenclature.
 */
public final class IpdKir2_6_1 extends ClasspathNomenclature {

    @Inject
    public IpdKir2_6_1(final GlstringResolver glstringResolver,
                       final IdResolver idResolver,
                       final GlRegistry glRegistry) {
        super("ipd-kir-2.6.1.txt", glstringResolver, idResolver, glRegistry);
    }

    @Override
    public String getName() {
        return "IPD-KIR Database";
    }

    @Override
    public String getVersion() {
        return "2.6.1";
    }

    @Override
    public String getURL() {
        return "https://www.ebi.ac.uk/ipd/kir/";
    }
}
