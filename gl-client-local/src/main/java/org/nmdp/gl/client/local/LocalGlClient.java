/*

    gl-client-local  Local client library for the URI-based RESTful service for the gl project.
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
package org.nmdp.gl.client.local;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.IOException;

import java.util.List;

import com.google.common.collect.ImmutableList;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;

import org.nmdp.gl.client.AbstractGlClient;
import org.nmdp.gl.client.GlClient;
import org.nmdp.gl.client.GlClientException;

import org.nmdp.gl.service.AllowNewAlleles;
import org.nmdp.gl.service.AllowNewLoci;
import org.nmdp.gl.service.GlReader;
import org.nmdp.gl.service.IdResolver;
import org.nmdp.gl.service.Namespace;
import org.nmdp.gl.service.Nomenclature;

import org.nmdp.gl.service.cache.CacheModule;
import org.nmdp.gl.service.id.IdModule;
import org.nmdp.gl.service.nomenclature.DefaultNomenclature;
import org.nmdp.gl.service.nomenclature.hla.ImgtHla3_32_0;
import org.nmdp.gl.service.reader.GlstringGlReader;

/**
 * Local implementation of GlClient.
 */
public final class LocalGlClient extends AbstractGlClient {
    private final GlReader reader;
    private final IdResolver idResolver;

    /**
     * Create a new local implementation of GlClient.
     */
    @Inject
    private LocalGlClient(final GlReader reader, final IdResolver idResolver) {
        checkNotNull(reader);
        checkNotNull(idResolver);
        this.reader = reader;
        this.idResolver = idResolver;
    }


    @Override
    public Locus getLocus(final String identifier) {
        return idResolver.findLocus(identifier);
    }

    @Override
    public String registerLocus(final String glstring) throws GlClientException {
        try {
            return reader.readLocus(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register locus, caught " + e.getMessage(), e);
        }
    }

    @Override
    public Allele getAllele(final String identifier) {
        return idResolver.findAllele(identifier);
    }

    @Override
    public String registerAllele(final String glstring) throws GlClientException {
        try {
            return reader.readAllele(glstring, "").getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register allele, caught " + e.getMessage(), e);
        }
    }

    @Override
    public AlleleList getAlleleList(final String identifier) {
        return idResolver.findAlleleList(identifier);
    }

    @Override
    public String registerAlleleList(final String glstring) throws GlClientException {
        try {
            return reader.readAlleleList(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register allele list, caught " + e.getMessage(), e);
        }
    }

    @Override
    public Haplotype getHaplotype(final String identifier) {
        return idResolver.findHaplotype(identifier);
    }

    @Override
    public String registerHaplotype(final String glstring) throws GlClientException {
        try {
            return reader.readHaplotype(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register haplotype, caught " + e.getMessage(), e);
        }
    }

    @Override
    public Genotype getGenotype(final String identifier) {
        return idResolver.findGenotype(identifier);
    }

    @Override
    public String registerGenotype(final String glstring) throws GlClientException {
        try {
            return reader.readGenotype(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register genotype, caught " + e.getMessage(), e);
        }
    }

    @Override
    public GenotypeList getGenotypeList(final String identifier) {
        return idResolver.findGenotypeList(identifier);
    }

    @Override
    public String registerGenotypeList(final String glstring) throws GlClientException {
        try {
            return reader.readGenotypeList(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register genotype list, caught " + e.getMessage(), e);
        }
    }

    @Override
    public MultilocusUnphasedGenotype getMultilocusUnphasedGenotype(final String identifier) {
        return idResolver.findMultilocusUnphasedGenotype(identifier);
    }

    @Override
    public String registerMultilocusUnphasedGenotype(final String glstring) throws GlClientException {
        try {
            return reader.readMultilocusUnphasedGenotype(glstring).getId();
        }
        catch (IOException e) {
            throw new GlClientException("could not register multilocus unphased genotype, caught " + e.getMessage(), e);
        }
    }


    /**
     * Create and return a new instance of LocalGlClient configured with the default nomenclature.
     *
     * @return a new instance of LocalGlClient configured with the default nomenclature
     */
    public static GlClient create() {
        Injector injector = Guice.createInjector(getLocalModules());
        return injector.getInstance(GlClient.class);
    }

    /**
     * Create and return a new instance of LocalGlClient configured in strict mode with the latest IMGT/HLA nomenclature.
     *
     * @return a new instance of LocalGlClient configured in strict mode with the latest IMGT/HLA nomenclature
     * @throws IOException if an I/O error occurs
     */
    public static GlClient createStrict() throws IOException {
        Injector injector = Guice.createInjector(getLocalStrictImgtHlaModules());
        Nomenclature nomenclature = injector.getInstance(Nomenclature.class);
        nomenclature.load();
        return injector.getInstance(GlClient.class);
    }

    /**
     * Create and return a new instance of LocalGlClient configured in strict mode with the specified nomenclature.
     *
     * @param nomenclatureClass nomenclature class, must not be null
     * @return a new instance of LocalGlClient configured in strict mode with the specified nomenclature
     * @throws IOException if an I/O error occurs
     */
    public static GlClient createStrict(final Class<? extends Nomenclature> nomenclatureClass) throws IOException {
        checkNotNull(nomenclatureClass);
        Injector injector = Guice.createInjector(getLocalStrictModules(nomenclatureClass));
        Nomenclature nomenclature = injector.getInstance(Nomenclature.class);
        nomenclature.load();
        return injector.getInstance(GlClient.class);
    }

    /**
     * Return the list of modules that configure LocalGlClient with the default nomenclature.
     *
     * @return the list of modules that configure LocalGlClient with the default nomenclature
     */
    public static List<AbstractModule> getLocalModules() {
        return ImmutableList.of(new LocalModule(), new CacheModule(), new IdModule());
    }

    /**
     * Return the list of modules that configure LocalGlClient in strict mode with the specified nomenclature.
     *
     * <p>
     * Note the nomenclature provided by an injector created from these modules must be loaded, e.g.
     * <pre>
     * Injector injector = Guice.createInjector(getLocalStrictModules(nomenclatureClass));
     * Nomenclature nomenclature = injector.getInstance(Nomenclature.class);
     * nomenclature.load();
     * </pre>
     * </p>
     *
     * @param nomenclatureClass nomenclature class, must ot be null
     * @return the list of modules that configure LocalGlClient in strict mode with the specified nomenclature
     */
    public static List<AbstractModule> getLocalStrictModules(final Class<? extends Nomenclature> nomenclatureClass) {
        checkNotNull(nomenclatureClass);
        return ImmutableList.of(new LocalStrictModule(), new CacheModule(), new IdModule(), new AbstractModule() {
                @Override
                protected void configure() {
                    bind(Nomenclature.class).to(nomenclatureClass);
                }
            });
    }

    /**
     * Return the list of modules that configure LocalGlClient in strict mode with the latest IMGT/HLA nomenclature.
     *
     * <p>
     * Note the nomenclature provided by an injector created from these modules must be loaded, e.g.
     * <pre>
     * Injector injector = Guice.createInjector(getLocalStrictImgtHlaModules());
     * Nomenclature nomenclature = injector.getInstance(Nomenclature.class);
     * nomenclature.load();
     * </pre>
     * </p>
     *
     * @return the list of modules that configure LocalGlClient in strict mode with the latest IMGT/HLA nomenclature
     */
    public static List<AbstractModule> getLocalStrictImgtHlaModules() {
        return ImmutableList.of(new LocalStrictImgtHlaModule(), new CacheModule(), new IdModule());
    }


    /**
     * Local module.
     */
    public static final class LocalModule extends AbstractModule {
        @Override 
        protected void configure() {
            bind(Boolean.class).annotatedWith(AllowNewLoci.class).toInstance(true);
            bind(Boolean.class).annotatedWith(AllowNewAlleles.class).toInstance(true);
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://localhost/");
            bind(GlReader.class).to(GlstringGlReader.class);
            bind(Nomenclature.class).to(DefaultNomenclature.class);
            bind(GlClient.class).to(LocalGlClient.class);
        }
    }

    /**
     * Local strict-mode module.
     */
    public static final class LocalStrictModule extends AbstractModule {
        @Override 
        protected void configure() {
            bind(Boolean.class).annotatedWith(AllowNewLoci.class).toInstance(false);
            bind(Boolean.class).annotatedWith(AllowNewAlleles.class).toInstance(false);
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://localhost/");
            bind(GlReader.class).to(GlstringGlReader.class);
            bind(GlClient.class).to(LocalGlClient.class);
        }
    }

    /**
     * Local strict-mode IMGT/HLA module.
     */
    public static final class LocalStrictImgtHlaModule extends AbstractModule {
        @Override 
        protected void configure() {
            bind(Boolean.class).annotatedWith(AllowNewLoci.class).toInstance(false);
            bind(Boolean.class).annotatedWith(AllowNewAlleles.class).toInstance(false);
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://localhost/");
            bind(GlReader.class).to(GlstringGlReader.class);
            bind(Nomenclature.class).to(ImgtHla3_32_0.class);
            bind(GlClient.class).to(LocalGlClient.class);
        }
    }
}
