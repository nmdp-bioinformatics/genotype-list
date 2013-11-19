/*

    gl-service-dynamodb  Implementation of persistent cache for gl-service using DynamoDB.
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
package org.immunogenomics.gl.service.dynamodb;

import static org.immunogenomics.gl.service.dynamodb.DynamoUtils.serialize;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.nio.ByteBuffer;

import java.util.Map;
import java.util.concurrent.Future;

import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;

import com.google.common.collect.ImmutableMap;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.AbstractIdResolverTest;
import org.immunogenomics.gl.service.IdResolver;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for DynamoIdResolver.
 */
public final class DynamoIdResolverTest extends AbstractIdResolverTest {
    @Mock
    private AmazonDynamoDBAsync dynamo;
    @Mock
    private Future<GetItemResult> future;
    @Mock
    private GetItemResult getItemResult;

    private Map<String, AttributeValue> locusItem;
    private Map<String, AttributeValue> alleleItem;
    private Map<String, AttributeValue> alleleListItem;
    private Map<String, AttributeValue> haplotypeItem;
    private Map<String, AttributeValue> genotypeItem;
    private Map<String, AttributeValue> genotypeListItem;
    private Map<String, AttributeValue> multilocusUnphasedGenotypeItem;
    private Double consumedCapacityUnits = Double.valueOf(1.0d);

    @Override
    protected IdResolver createIdResolver() {
        MockitoAnnotations.initMocks(this);
        when(dynamo.getItemAsync(any(GetItemRequest.class))).thenReturn(future);
        try {
            when(future.get()).thenReturn(getItemResult);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        when(getItemResult.getConsumedCapacityUnits()).thenReturn(consumedCapacityUnits);

        Locus locus = new Locus(validLocusId, "HLA-A");
        Allele allele = new Allele(validAlleleId, "A01234", "HLA-A*01:01:01:01", locus);
        AlleleList alleleList = new AlleleList(validAlleleListId, allele);
        Haplotype haplotype = new Haplotype(validHaplotypeId, alleleList);
        Genotype genotype = new Genotype(validGenotypeId, haplotype);
        GenotypeList genotypeList = new GenotypeList(validGenotypeListId, genotype);
        MultilocusUnphasedGenotype multilocusUnphasedGenotype = new MultilocusUnphasedGenotype(validMultilocusUnphasedGenotypeId, genotypeList);
        locusItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(locus))));
        alleleItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(allele))));
        alleleListItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(alleleList))));
        haplotypeItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(haplotype))));
        genotypeItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(genotype))));
        genotypeListItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(genotypeList))));
        multilocusUnphasedGenotypeItem = ImmutableMap.of("glResource", new AttributeValue().withB(ByteBuffer.wrap(serialize(multilocusUnphasedGenotype))));

        return new DynamoIdResolver(dynamo);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullDynamo() {
        new DynamoIdResolver(null);
    }

    @Test
    public void testFindLocus() {
        when(getItemResult.getItem()).thenReturn(locusItem);
        super.testFindLocus();
    }

    @Test
    public void testFindLocusDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindLocusDoesNotExist();
    }

    @Test
    public void testFindAllele() {
        when(getItemResult.getItem()).thenReturn(alleleItem);
        super.testFindAllele();
    }

    @Test
    public void testFindAlleleDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindAlleleDoesNotExist();
    }

    @Test
        public void testFindAlleleList() {
        when(getItemResult.getItem()).thenReturn(alleleListItem);
        super.testFindAlleleList();
    }

    @Test
    public void testFindAlleleListDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindAlleleListDoesNotExist();
    }

    @Test
    public void testFindHaplotype() {
        when(getItemResult.getItem()).thenReturn(haplotypeItem);
        super.testFindHaplotype();
    }

    @Test
    public void testFindHaplotypeDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindHaplotypeDoesNotExist();
    }

    @Test
    public void testFindGenotype() {
        when(getItemResult.getItem()).thenReturn(genotypeItem);
        super.testFindGenotype();
    }

    @Test
    public void testFindGenotypeDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindGenotypeDoesNotExist();
    }

    @Test
    public void testFindGenotypeList() {
        when(getItemResult.getItem()).thenReturn(genotypeListItem);
        super.testFindGenotypeList();
    }

    @Test
    public void testFindGenotypeListDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindGenotypeListDoesNotExist();
    }

    @Test
    public void testFindMultilocusUnphasedGenotype() {
        when(getItemResult.getItem()).thenReturn(multilocusUnphasedGenotypeItem);
        super.testFindMultilocusUnphasedGenotype();
    }

    @Test
    public void testFindMultilocusUnphasedGenotypeDoesNotExist() {
        when(getItemResult.getItem()).thenReturn(null);
        super.testFindMultilocusUnphasedGenotypeDoesNotExist();
    }
}