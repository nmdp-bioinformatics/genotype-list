/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.immunogenomics.gl.service.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.jdbc.JdbcUtils.deserialize;

import java.sql.ResultSet;
import java.sql.SQLException;
import javax.sql.DataSource;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.ResultSetHandler;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.IdResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;

import com.google.inject.Inject;

/**
 * JDBC based identifier resolver.
 */
@Immutable
final class JdbcIdResolver implements IdResolver {
    private final DataSource dataSource;
    private final Cache<String, Locus> loci;
    private final Cache<String, Allele> alleles;
    private static final Logger logger = LoggerFactory.getLogger(JdbcIdResolver.class);
    private static final String LOCUS_SQL = "select locus from locus where id = ?";
    private static final String ALLELE_SQL = "select allele from allele where id = ?";
    private static final String ALLELE_LIST_SQL = "select allele_list from allele_list where id = ?";
    private static final String HAPLOTYPE_SQL = "select haplotype from haplotype where id = ?";
    private static final String GENOTYPE_SQL = "select genotype from genotype where id = ?";
    private static final String GENOTYPE_LIST_SQL = "select genotype_list from genotype_list where id = ?";
    private static final String MULTILOCUS_UNPHASED_GENOTYPE_SQL = "select multilocus_unphased_genotype from multilocus_unphased_genotype where id = ?";


    @Inject
    JdbcIdResolver(final DataSource dataSource, final Cache<String, Locus> loci, final Cache<String, Allele> alleles) {
        checkNotNull(dataSource);
        checkNotNull(loci);
        checkNotNull(alleles);
        this.dataSource = dataSource;
        this.loci = loci;
        this.alleles = alleles;
    }


    @Override
    public Locus findLocus(final String id) {
        Locus locus = loci.getIfPresent(id);
        if (locus != null) {
            return locus;
        }
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            locus = queryRunner.query(LOCUS_SQL, new LocusHandler(), id);
            if (locus != null) {
                loci.put(id, locus);
            }
            return locus;
        }
        catch (SQLException e) {
            logger.warn("could not find locus for id " + id, e);
            return null;
        }
    }

    @Override
    public Allele findAllele(final String id) {
        Allele allele = alleles.getIfPresent(id);
        if (allele != null) {
            return allele;
        }
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            allele = queryRunner.query(ALLELE_SQL, new AlleleHandler(), id);
            if (allele != null) {
                alleles.put(id, allele);
            }
            return allele;
        }
        catch (SQLException e) {
            logger.warn("could not find allele for id " + id, e);
            return null;
        }
    }

    @Override
    public AlleleList findAlleleList(final String id) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(ALLELE_LIST_SQL, new AlleleListHandler(), id);
        }
        catch (SQLException e) {
            logger.warn("could not find allele list for id " + id, e);
            return null;
        }
    }

    @Override
    public Haplotype findHaplotype(final String id) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(HAPLOTYPE_SQL, new HaplotypeHandler(), id);
        }
        catch (SQLException e) {
            logger.warn("could not find haplotype for id " + id, e);
            return null;
        }
    }

    @Override
    public Genotype findGenotype(final String id) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(GENOTYPE_SQL, new GenotypeHandler(), id);
        }
        catch (SQLException e) {
            logger.warn("could not find genotype for id " + id, e);
            return null;
        }
    }

    @Override
    public GenotypeList findGenotypeList(final String id) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(GENOTYPE_LIST_SQL, new GenotypeListHandler(), id);
        }
        catch (SQLException e) {
            logger.warn("could not find genotype list for id " + id, e);
            return null;
        }
    }

    @Override
    public MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(final String id) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            return queryRunner.query(MULTILOCUS_UNPHASED_GENOTYPE_SQL, new MultilocusUnphasedGenotypeHandler(), id);
        }
        catch (SQLException e) {
            logger.warn("could not find multilocus unphased genotype for id " + id, e);
            return null;
        }
    }

    /**
     * Locus handler.
     */
    static class LocusHandler implements ResultSetHandler<Locus> {
        @Override
        public Locus handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize locus, bytes were null");
                return null;
            }
            else {
                return (Locus) deserialize(bytes);
            }
        }
    }

    /**
     * Allele handler.
     */
    static class AlleleHandler implements ResultSetHandler<Allele> {
        @Override
        public Allele handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize allele, bytes were null");
                return null;
            }
            else {
                return (Allele) deserialize(bytes);
            }
        }
    }

    /**
     * Allele list handler.
     */
    static class AlleleListHandler implements ResultSetHandler<AlleleList> {
        @Override
        public AlleleList handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize allele list, bytes were null");
                return null;
            }
            else {
                return (AlleleList) deserialize(bytes);
            }
        }
    }

    /**
     * Haplotype handler.
     */
    static class HaplotypeHandler implements ResultSetHandler<Haplotype> {
        @Override
        public Haplotype handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize haplotype, bytes were null");
                return null;
            }
            else {
                return (Haplotype) deserialize(bytes);
            }
        }
    }

    /**
     * Genotype handler.
     */
    static class GenotypeHandler implements ResultSetHandler<Genotype> {
        @Override
        public Genotype handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize genotype, bytes were null");
                return null;
            }
            else {
                return (Genotype) deserialize(bytes);
            }
        }
    }

    /**
     * Genotype list handler.
     */
    static class GenotypeListHandler implements ResultSetHandler<GenotypeList> {
        @Override
        public GenotypeList handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize genotype list, bytes were null");
                return null;
            }
            else {
                return (GenotypeList) deserialize(bytes);
            }
        }
    }

    /**
     * Multilocus unphased genotype handler.
     */
    static class MultilocusUnphasedGenotypeHandler implements ResultSetHandler<MultilocusUnphasedGenotype> {
        @Override
        public MultilocusUnphasedGenotype handle(final ResultSet resultSet) throws SQLException {
            if (!resultSet.first())
            {
                return null;
            }
            byte[] bytes = resultSet.getBytes(1);
            if (bytes == null) {
                logger.warn("could not deserialize multilocus unphased genotype, bytes were null");
                return null;
            }
            else {
                return (MultilocusUnphasedGenotype) deserialize(bytes);
            }
        }
    }
}