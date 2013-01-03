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

import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;
import static org.immunogenomics.gl.service.jdbc.JdbcUtils.serialize;

import java.sql.Connection;
import java.sql.ParameterMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.AbstractIdResolverTest;
import org.immunogenomics.gl.service.IdResolver;

import com.google.common.collect.ImmutableList;

/**
 * Unit test for JdbcIdResolver.
 */
public final class JdbcIdResolverTest extends AbstractIdResolverTest {
    private Locus locus;
    private Allele allele;
    private AlleleList alleleList0;
    private AlleleList alleleList1;
    private Haplotype haplotype;
    private Genotype genotype;
    private GenotypeList genotypeList0;
    private GenotypeList genotypeList1;
    private MultilocusUnphasedGenotype multilocusUnphasedGenotype;

    @Mock
    private Connection connection;
    @Mock
    private DataSource dataSource;
    @Mock
    private ParameterMetaData parameterMetaData;
    @Mock
    private PreparedStatement preparedStatement;
    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        locus = new Locus(validLocusId, "HLA-A");
        allele = new Allele(validAlleleId, "A01234", "HLA-A*01:01:01:01", locus);
        alleleList0 = new AlleleList(validAlleleListId, allele);
        alleleList1 = new AlleleList(validAlleleListId, allele);
        haplotype = new Haplotype(validHaplotypeId, ImmutableList.of(alleleList0, alleleList1));
        Haplotype haplotype0 = new Haplotype(validHaplotypeId, alleleList0);
        genotype = new Genotype(validGenotypeId, haplotype0);
        genotypeList0 = new GenotypeList(validGenotypeListId, genotype);
        genotypeList1 = new GenotypeList(validGenotypeListId, genotype);
        multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId, ImmutableList.of(genotypeList0, genotypeList1));

        try {
            when(dataSource.getConnection()).thenReturn(connection);
            when(connection.prepareStatement(anyString())).thenReturn(preparedStatement);
            when(preparedStatement.getParameterMetaData()).thenReturn(parameterMetaData);
            when(parameterMetaData.getParameterCount()).thenReturn(1);
            when(preparedStatement.executeQuery()).thenReturn(resultSet);
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.setUp();
    }

    @Override
    protected IdResolver createIdResolver() {
        return new JdbcIdResolver(dataSource);
    }

    @Override
    public void testFindLocus() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(locus));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindLocus();
    }

    @Override
    public void testFindAllele() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(allele));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindAllele();
    }

    @Override
    public void testFindAlleleList() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(alleleList0));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindAlleleList();
    }

    @Override
    public void testFindHaplotype() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(haplotype));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindHaplotype();
    }

    @Override
    public void testFindGenotype() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(genotype));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindGenotype();
    }

    @Override
    public void testFindGenotypeList() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(genotypeList0));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindGenotypeList();
    }

    @Override
    public void testFindMultilocusUnphasedGenotype() {
        try {
            when(resultSet.first()).thenReturn(true);
            when(resultSet.getBytes(1)).thenReturn(serialize(multilocusUnphasedGenotype));
        }
        catch (SQLException e) {
            fail(e.getMessage());
        }
        super.testFindMultilocusUnphasedGenotype();
    }
}