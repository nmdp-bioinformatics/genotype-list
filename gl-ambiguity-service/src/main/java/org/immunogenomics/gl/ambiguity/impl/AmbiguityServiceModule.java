/*

    gl-ambiguity-service  Genotype list ambiguity service.
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
package org.immunogenomics.gl.ambiguity.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;

import org.immunogenomics.gl.ambiguity.AmbiguityService2;

/**
 * Genotype list ambiguity service module.
 */
public final class AmbiguityServiceModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(AmbiguityService2.class).to(AmbiguityService2Impl.class);
    }

    @Provides @Singleton
    ListMultimap<Locus, Allele> createAlleles() {
        return ArrayListMultimap.create();
    }
}

