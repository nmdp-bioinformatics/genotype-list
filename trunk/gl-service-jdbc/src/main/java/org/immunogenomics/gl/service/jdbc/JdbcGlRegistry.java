/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.immunogenomics.gl.service.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.jdbc.JdbcUtils.hash;

import java.sql.SQLException;
import javax.sql.DataSource;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.dbutils.QueryRunner;
import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

/**
 * JDBC based genotype list registry.
 */
@Immutable
final class JdbcGlRegistry implements GlRegistry {
    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(JdbcGlRegistry.class);
    private static final String INSERT_LOCUS_SQL = "insert into locus (id, locus) values (?, ?)";
    private static final String INSERT_LOCUS_ID_SQL = "insert into locus_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_ALLELE_SQL = "insert into allele (id, allele) values (?, ?)";
    private static final String INSERT_ALLELE_ID_SQL = "insert into allele_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_ALLELE_LIST_SQL = "insert into allele_list (id, allele_list) values (?, ?)";
    private static final String INSERT_ALLELE_LIST_ID_SQL = "insert into allele_list_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_HAPLOTYPE_SQL = "insert into haplotype (id, haplotype) values (?, ?)";
    private static final String INSERT_HAPLOTYPE_ID_SQL = "insert into haplotype_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_GENOTYPE_SQL = "insert into genotype (id, genotype) values (?, ?)";
    private static final String INSERT_GENOTYPE_ID_SQL = "insert into genotype_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_GENOTYPE_LIST_SQL = "insert into genotype_list (id, genotype_list) values (?, ?)";
    private static final String INSERT_GENOTYPE_LIST_ID_SQL = "insert into genotype_list_id (glstring, glstring_hash, id) values (?, ?, ?)";
    private static final String INSERT_MULTILOCUS_UNPHASED_GENOTYPE_SQL = "insert into multilocus_unphased_genotype (id, multilocus_unphased_genotype) values (?, ?)";
    private static final String INSERT_MULTILOCUS_UNPHASED_GENOTYPE_ID_SQL = "insert into multilocus_unphased_genotype_id (glstring, glstring_hash, id) values (?, ?, ?)";


    @Inject
    JdbcGlRegistry(final DataSource dataSource) {
        checkNotNull(dataSource);
        this.dataSource = dataSource;
    }


    @Override
    public void registerLocus(final Locus locus) {
        checkNotNull(locus);
        // todo:  use transaction
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_LOCUS_SQL, locus.getId(), locus);
            queryRunner.update(INSERT_LOCUS_ID_SQL, locus.getGlstring(), hash(locus.getGlstring()), locus.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register locus " + locus.getId(), e);
        }
    }

    @Override
    public void registerAllele(final Allele allele) {
        checkNotNull(allele);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_ALLELE_SQL, allele.getId(), allele);
            queryRunner.update(INSERT_ALLELE_ID_SQL, allele.getGlstring(), hash(allele.getGlstring()), allele.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register allele " + allele.getId(), e);
        }
    }

    @Override
    public void registerAlleleList(final AlleleList alleleList) {
        checkNotNull(alleleList);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_ALLELE_LIST_SQL, alleleList.getId(), alleleList);
            queryRunner.update(INSERT_ALLELE_LIST_ID_SQL, alleleList.getGlstring(), hash(alleleList.getGlstring()), alleleList.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register allele list " + alleleList.getId(), e);
        }
    }

    @Override
    public void registerHaplotype(final Haplotype haplotype) {
        checkNotNull(haplotype);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_HAPLOTYPE_SQL, haplotype.getId(), haplotype);
            queryRunner.update(INSERT_HAPLOTYPE_ID_SQL, haplotype.getGlstring(), hash(haplotype.getGlstring()), haplotype.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register haplotype " + haplotype.getId(), e);
        }
    }

    @Override
    public void registerGenotype(final Genotype genotype) {
        checkNotNull(genotype);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_GENOTYPE_SQL, genotype.getId(), genotype);
            queryRunner.update(INSERT_GENOTYPE_ID_SQL, genotype.getGlstring(), hash(genotype.getGlstring()), genotype.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register genotype " + genotype.getId(), e);
        }
    }

    @Override
    public void registerGenotypeList(final GenotypeList genotypeList) {
        checkNotNull(genotypeList);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_GENOTYPE_LIST_SQL, genotypeList.getId(), genotypeList);
            queryRunner.update(INSERT_GENOTYPE_LIST_ID_SQL, genotypeList.getGlstring(), hash(genotypeList.getGlstring()), genotypeList.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register genotype list " + genotypeList.getId(), e);
        }
    }

    @Override
    public void registerMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        checkNotNull(multilocusUnphasedGenotype);
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(INSERT_MULTILOCUS_UNPHASED_GENOTYPE_SQL, multilocusUnphasedGenotype.getId(), multilocusUnphasedGenotype);
            queryRunner.update(INSERT_MULTILOCUS_UNPHASED_GENOTYPE_ID_SQL, multilocusUnphasedGenotype.getGlstring(), hash(multilocusUnphasedGenotype.getGlstring()), multilocusUnphasedGenotype.getId());
        }
        catch (SQLException e) {
            logger.warn("could not register multilocus unphased genotype " + multilocusUnphasedGenotype.getId(), e);
        }
    }
}