/*

    gl-service-id-jdbc  Identifier supplier implementation based on JDBC.
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
package org.nmdp.gl.service.id.jdbc;

import static com.google.common.base.Preconditions.checkNotNull;

import java.sql.SQLException;

import javax.annotation.concurrent.Immutable;

import javax.sql.DataSource;

import com.google.inject.Inject;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import org.nmdp.gl.service.IdSupplier;
import org.nmdp.gl.service.Namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MySQL JDBC identifier supplier.
 */
@Immutable
final class MysqlJdbcIdSupplier implements IdSupplier {
    private final String ns;
    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(MysqlJdbcIdSupplier.class);
    // todo: this works but happens in two separate statements
    private static final String UPDATE_SQL = "update sequence set sequence_value = (sequence_value + 1) where sequence_name = ?";
    private static final String SELECT_SQL = "select sequence_value from sequence where sequence_name = ?";

    @Inject
    MysqlJdbcIdSupplier(@Namespace final String ns, final DataSource dataSource) {
        checkNotNull(ns);
        checkNotNull(dataSource);
        this.ns = ns;
        this.dataSource = dataSource;
    }


    private Long nextSequence(final String name) {
        QueryRunner queryRunner = new QueryRunner(dataSource);
        try {
            queryRunner.update(UPDATE_SQL, name);
            return (Long) queryRunner.query(SELECT_SQL, new ScalarHandler(), name);
        }
        catch (SQLException e) {
            logger.warn("could not select next sequence value for sequence " + name, e);
        }
        // todo:  return random value if fails?
        return null;
    }

    @Override
    public String createLocusId() {
        return ns + "locus/" + encode(nextSequence("locus"));
    }

    @Override
    public String createAlleleId() {
        return ns + "allele/" + encode(nextSequence("allele"));
    }

    @Override
    public String createAlleleListId() {
        return ns + "allele-list/" + encode(nextSequence("allele-list"));
    }

    @Override
    public String createHaplotypeId() {
        return ns + "haplotype/" + encode(nextSequence("haplotype"));
    }

    @Override
    public String createGenotypeId() {
        return ns + "genotype/" + encode(nextSequence("genotype"));
    }

    @Override
    public String createGenotypeListId() {
        return ns + "genotype-list/" + encode(nextSequence("genotype-list"));
    }

    @Override
    public String createMultilocusUnphasedGenotypeId() {
        return ns + "multilocus-unphased-genotype/" + encode(nextSequence("multilocus-unphased-genotype"));
    }

    static String encode(final Long value) {
        return Long.toString(value, 36);
    }
}
