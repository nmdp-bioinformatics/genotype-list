/*

    gl-liftover-service  Genotype list liftover service.
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
package org.nmdp.gl.liftover.impl;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import com.google.common.collect.Table;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.nmdp.gl.Allele;
import org.nmdp.gl.Locus;

import org.nmdp.gl.client.GlClient;

import org.nmdp.gl.client.http.HttpClient;

import org.nmdp.gl.client.http.restassured.RestAssuredHttpClient;

import org.nmdp.gl.client.json.JsonGlClient;

import org.nmdp.gl.liftover.AlleleNames;
import org.nmdp.gl.liftover.LiftoverService;
import org.nmdp.gl.liftover.LocusNames;

/**
 * Liftover service module.
 */
public final class LiftoverServiceModule extends AbstractModule {

    @Override 
    protected void configure() {
        bind(LiftoverService.class).to(LiftoverServiceImpl.class);
    }

    @Provides
    LoadingCache<String, GlClient> createClients() {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, GlClient>() {
                @Override
                public GlClient load(final String namespace) {
                    HttpClient httpClient = new RestAssuredHttpClient();
                    JsonFactory jsonFactory = new JsonFactory(); // todo:  this could be injected in
                    Cache<String, Locus> loci = CacheBuilder.newBuilder().maximumSize(1000).build();
                    Cache<String, String> locusIds = CacheBuilder.newBuilder().maximumSize(1000).build();
                    Cache<String, Allele> alleles = CacheBuilder.newBuilder().maximumSize(10000).build();
                    Cache<String, String> alleleIds = CacheBuilder.newBuilder().maximumSize(10000).build();
                    return new JsonGlClient(namespace, jsonFactory, httpClient, loci, locusIds, alleles, alleleIds);
                }
            });
    }

    // todo: sucks to read Allelelist_history.txt twice

    @Provides @LocusNames @Singleton
    Table<String, String, String> createLocusNames() {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("https://gl.nmdp.org/imgt-hla/");
        try {
            allelelistHistoryReader.readAllelelistHistory();
            allelelistHistoryReader.readGgroups();
        }
        catch (IOException e) {
            throw new RuntimeException("could not read Allelelist_history.txt", e);
        }
        return allelelistHistoryReader.getLocusNames();
    }

    @Provides @AlleleNames @Singleton
    Table<String, String, String> createAlleleNames() {
        AllelelistHistoryReader allelelistHistoryReader = new AllelelistHistoryReader("https://gl.nmdp.org/imgt-hla/");
        try {
            allelelistHistoryReader.readAllelelistHistory();
            allelelistHistoryReader.readGgroups();
        }
        catch (IOException e) {
            throw new RuntimeException("could not read Allelelist_history.txt", e);
        }
        return allelelistHistoryReader.getAlleleNames();        
    }
}
