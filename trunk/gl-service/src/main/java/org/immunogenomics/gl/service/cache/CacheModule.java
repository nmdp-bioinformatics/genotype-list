/*

    gl-service  URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.cache;

import javax.annotation.concurrent.Immutable;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;
import org.immunogenomics.gl.service.IdSupplier;
import org.immunogenomics.gl.service.Namespace;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

/**
 * Cache module.
 */
@Immutable
public final class CacheModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IdResolver.class).to(CacheIdResolver.class);
        bind(GlstringResolver.class).to(CacheGlstringResolver.class);
        bind(GlRegistry.class).to(CacheGlRegistry.class);
    }

    @Provides @Singleton
    Cache<String, Locus> createLocusIdCache() {
        Cache<String, Locus> loci = CacheBuilder.newBuilder().build();
        return loci;
    }

    @Provides @Singleton
    Cache<String, Allele> createAlleleIdCache() {
        Cache<String, Allele> alleles = CacheBuilder.newBuilder().build();
        return alleles;
    }

    @Provides @Singleton
    Cache<String, AlleleList> createAlleleListIdCache() {
        Cache<String, AlleleList> alleleLists = CacheBuilder.newBuilder().build();
        return alleleLists;
    }

    @Provides @Singleton
    Cache<String, Haplotype> createHaplotypeIdCache() {
        Cache<String, Haplotype> haplotypes = CacheBuilder.newBuilder().build();
        return haplotypes;
    }

    @Provides @Singleton
    Cache<String, Genotype> createGenotypeIdCache() {
        Cache<String, Genotype> genotypes = CacheBuilder.newBuilder().build();
        return genotypes;
    }

    @Provides @Singleton
    Cache<String, GenotypeList> createGenotypeListIdCache() {
        Cache<String, GenotypeList> genotypeLists = CacheBuilder.newBuilder().build();
        return genotypeLists;
    }

    @Provides @Singleton
    Cache<String, MultilocusUnphasedGenotype> createMultilocusUnphasedGenotypeIdCache() {
        Cache<String, MultilocusUnphasedGenotype> multilocusUnphasedGenotypes = CacheBuilder.newBuilder().build();
        return multilocusUnphasedGenotypes;
    }

    @Provides @Singleton @Named("locusIds")
    LoadingCache<String, String> createLocusGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createLocusId();
                }
            });
    }

    @Provides @Singleton @Named("alleleIds")
    LoadingCache<String, String> createAlleleGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createAlleleId();
                }
            });
    }

    @Provides @Singleton @Named("alleleListIds")
    LoadingCache<String, String> createAlleleListGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createAlleleListId();
                }
            });
    }

    @Provides @Singleton @Named("haplotypeIds")
    LoadingCache<String, String> createHaplotypeGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createHaplotypeId();
                }
            });
    }

    @Provides @Singleton @Named("genotypeIds")
    LoadingCache<String, String> createGenotypeGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createGenotypeId();
                }
            });
    }

    @Provides @Singleton @Named("genotypeListIds")
    LoadingCache<String, String> createGenotypeListGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createGenotypeListId();
                }
            });
    }

    @Provides @Singleton @Named("multilocusUnphasedGenotypeIds")
    LoadingCache<String, String> createMultilocusUnphasedGenotypeGlstringCache(@Namespace final String ns, final IdSupplier idSupplier) {
        return CacheBuilder.newBuilder().build(new CacheLoader<String, String>() {
                @Override
                public String load(final String glstring) {
                    return idSupplier.createMultilocusUnphasedGenotypeId();
                }
            });
    }
}