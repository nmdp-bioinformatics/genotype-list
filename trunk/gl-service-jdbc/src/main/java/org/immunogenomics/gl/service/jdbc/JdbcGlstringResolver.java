/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.immunogenomics.gl.service.jdbc;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.jdbc.JdbcUtils.hash;

import java.sql.SQLException;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.sql.DataSource;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;

import com.google.inject.Inject;

/**
 * JDBC based glstring resolver.
 */
@Immutable
final class JdbcGlstringResolver implements GlstringResolver {
    private final IdSupplier idSupplier;
    private final DataSource dataSource;
    private final Cache<String, String> locusIds;
    private final Cache<String, String> alleleIds;
    private final Logger logger = LoggerFactory.getLogger(JdbcGlstringResolver.class);
    private static final String LOCUS_ID_SQL = "select id from locus_id where glstring_hash = ?";
    private static final String ALLELE_ID_SQL = "select id from allele_id where glstring_hash = ?";
    private static final String ALLELE_LIST_ID_SQL = "select id from allele_list_id where glstring_hash = ?";
    private static final String HAPLOTYPE_ID_SQL = "select id from haplotype_id where glstring_hash = ?";
    private static final String GENOTYPE_ID_SQL = "select id from genotype_id where glstring_hash = ?";
    private static final String GENOTYPE_LIST_ID_SQL = "select id from genotype_list_id where glstring_hash = ?";
    private static final String MULTILOCUS_UNPHASED_GENOTYPE_ID_SQL = "select id from multilocus_unphased_genotype_id where glstring_hash = ?";


    @Inject
    JdbcGlstringResolver(final IdSupplier idSupplier,
                         final DataSource dataSource,
                         final Cache<String, String> locusIds,
                         final Cache<String, String> alleleIds) {

        checkNotNull(idSupplier);
        checkNotNull(dataSource);
        checkNotNull(locusIds);
        checkNotNull(alleleIds);
        this.idSupplier = idSupplier;
        this.dataSource = dataSource;
        this.locusIds = locusIds;
        this.alleleIds = alleleIds;
    }


    @Override
    public String resolveLocus(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        try {
            return locusIds.get(glstring, new Callable<String>()
                {
                    @Override
                    public String call() {
                        QueryRunner queryRunner = new QueryRunner(dataSource);
                        try {
                            String id = (String) queryRunner.query(LOCUS_ID_SQL, new ScalarHandler(), hash(glstring));
                            if (id != null) {
                                return id;
                            }
                        }
                        catch (SQLException e) {
                            logger.warn("could not resolve id for locus with glstring " + abbrev(glstring), e);
                        }
                        return idSupplier.createLocusId();
                    }
                });
        }
        catch (ExecutionException e) {
            logger.warn("could not resolve id for locus with glstring " + abbrev(glstring), e);
            return idSupplier.createLocusId();
        }
    }

    @Override
    public String resolveAllele(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        try {
            return alleleIds.get(glstring, new Callable<String>()
                {
                    @Override
                    public String call() {
                        QueryRunner queryRunner = new QueryRunner(dataSource);
                        try {
                            String id = (String) queryRunner.query(ALLELE_ID_SQL, new ScalarHandler(), hash(glstring));
                            if (id != null) {
                                return id;
                            }
                        }
                        catch (SQLException e) {
                            logger.warn("could not resolve id for allele with glstring " + abbrev(glstring), e);
                        }
                        return idSupplier.createAlleleId();
                    }
                });
        }
        catch (ExecutionException e) {
            logger.warn("could not resolve id for allele with glstring " + abbrev(glstring), e);
            return idSupplier.createLocusId();
        }
    }

    @Override
    public String resolveAlleleList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            String id = (String) queryRunner.query(ALLELE_LIST_ID_SQL, new ScalarHandler(), hash(glstring));
            if (id != null) {
                return id;
            }
        }
        catch (SQLException e) {
            logger.warn("could not resolve id for allele list with glstring " + abbrev(glstring), e);
        }
        return idSupplier.createAlleleListId();
    }

    @Override
    public String resolveHaplotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            String id = (String) queryRunner.query(HAPLOTYPE_ID_SQL, new ScalarHandler(), hash(glstring));
            if (id != null) {
                return id;
            }
        }
        catch (SQLException e) {
            logger.warn("could not resolve id for haplotype with glstring " + abbrev(glstring), e);
        }
        return idSupplier.createHaplotypeId();
    }

    @Override
    public String resolveGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            String id = (String) queryRunner.query(GENOTYPE_ID_SQL, new ScalarHandler(), hash(glstring));
            if (id != null) {
                return id;
            }
        }
        catch (SQLException e) {
            logger.warn("could not resolve id for genotype with glstring " + abbrev(glstring), e);
        }
        return idSupplier.createGenotypeId();
    }

    @Override
    public String resolveGenotypeList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            String id = (String) queryRunner.query(GENOTYPE_LIST_ID_SQL, new ScalarHandler(), hash(glstring));
            if (id != null) {
                return id;
            }
        }
        catch (SQLException e) {
            logger.warn("could not resolve id for genotype list with glstring " + abbrev(glstring), e);
        }
        return idSupplier.createGenotypeListId();
    }

    @Override
    public String resolveMultilocusUnphasedGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            String id = (String) queryRunner.query(MULTILOCUS_UNPHASED_GENOTYPE_ID_SQL, new ScalarHandler(), hash(glstring));
            if (id != null) {
                return id;
            }
        }
        catch (SQLException e) {
            logger.warn("could not resolve id for multilocus unphased genotype with glstring " + abbrev(glstring), e);
        }
        return idSupplier.createMultilocusUnphasedGenotypeId();
    }

    private static String abbrev(final String glstring) {
        if (glstring.length() < 64) {
            return glstring;
        }
        return glstring.substring(0, 61) + "...";
    }
}