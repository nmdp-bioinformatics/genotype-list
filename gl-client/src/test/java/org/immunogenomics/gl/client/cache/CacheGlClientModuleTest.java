/*

    gl-client  Client library for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.client.cache;

import static org.junit.Assert.assertNotNull;

import com.google.common.cache.Cache;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.Locus;

import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for CacheGlClientModule.
 */
public class CacheGlClientModuleTest {
    private CacheGlClientModule cacheGlClientModule;

    @Before
    public void setUp() {
        cacheGlClientModule = new CacheGlClientModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(cacheGlClientModule);
    }

    @Test
    public void testCacheModule() {
        Injector injector = Guice.createInjector(cacheGlClientModule);
        assertNotNull(injector);
        assertNotNull(injector.getInstance(Key.get(new TypeLiteral<Cache<String, Locus>>() {;}, GlClientLocusCache.class)));
        assertNotNull(injector.getInstance(Key.get(new TypeLiteral<Cache<String, String>>() {;}, GlClientLocusIdCache.class)));
        assertNotNull(injector.getInstance(Key.get(new TypeLiteral<Cache<String, Allele>>() {;}, GlClientAlleleCache.class)));
        assertNotNull(injector.getInstance(Key.get(new TypeLiteral<Cache<String, String>>() {;}, GlClientAlleleIdCache.class)));
    }
}
