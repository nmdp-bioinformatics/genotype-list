/*

    gl-service-dynamodb  Implementation of persistent cache for gl-service using DynamoDB.
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
package org.immunogenomics.gl.service.dynamodb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.Map;
import java.util.concurrent.Future;

import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;

import com.google.common.collect.ImmutableMap;

import org.immunogenomics.gl.service.AbstractGlstringResolverTest;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for DynamoGlstringResolver.
 */
public final class DynamoGlstringResolverTest extends AbstractGlstringResolverTest {
    @Mock
    private IdSupplier idSupplier;
    @Mock
    private AmazonDynamoDBAsync dynamo;
    @Mock
    private Future<GetItemResult> future;
    @Mock
    private GetItemResult getItemResult;

    private String locusId = "http://immunogenomics.org/locus/0";
    private String alleleId = "http://immunogenomics.org/allele/0";
    private String alleleListId = "http://immunogenomics.org/allele-list/0";
    private String haplotypeId = "http://immunogenomics.org/haplotype/0";
    private String genotypeId = "http://immunogenomics.org/genotype/0";
    private String genotypeListId = "http://immunogenomics.org/genotype-list/0";
    private String multilocusUnphasedGenotypeId = "http://immunogenomics.org/multilocus-unphased-genotype/0";

    private Map<String, AttributeValue> locusItem;
    private Map<String, AttributeValue> alleleItem;
    private Map<String, AttributeValue> alleleListItem;
    private Map<String, AttributeValue> haplotypeItem;
    private Map<String, AttributeValue> genotypeItem;
    private Map<String, AttributeValue> genotypeListItem;
    private Map<String, AttributeValue> multilocusUnphasedGenotypeItem;
    private Double consumedCapacityUnits = Double.valueOf(1.0d);

    @Override
    protected GlstringResolver createGlstringResolver() {
        MockitoAnnotations.initMocks(this);
        when(dynamo.getItemAsync(any(GetItemRequest.class))).thenReturn(future);
        try {
            when(future.get()).thenReturn(getItemResult);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        locusItem = ImmutableMap.of("id", new AttributeValue().withS(locusId));
        alleleItem = ImmutableMap.of("id", new AttributeValue().withS(alleleId));
        alleleListItem = ImmutableMap.of("id", new AttributeValue().withS(alleleListId));
        haplotypeItem = ImmutableMap.of("id", new AttributeValue().withS(haplotypeId));
        genotypeItem = ImmutableMap.of("id", new AttributeValue().withS(genotypeId));
        genotypeListItem = ImmutableMap.of("id", new AttributeValue().withS(genotypeListId));
        multilocusUnphasedGenotypeItem = ImmutableMap.of("id", new AttributeValue().withS(multilocusUnphasedGenotypeId));
        when(getItemResult.getConsumedCapacityUnits()).thenReturn(consumedCapacityUnits);
        return new DynamoGlstringResolver(idSupplier, dynamo);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullDynamo() {
        new DynamoGlstringResolver(idSupplier, null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullIdSupplier() {
        new DynamoGlstringResolver(null, dynamo);
    }

    @Test
    public void testResolveLocusNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createLocusId()).thenReturn("http://immunogenomics.org/locus/1");
        assertEquals("http://immunogenomics.org/locus/1", glstringResolver.resolveLocus("HLA-Z"));
    }

    @Test
    public void testResolveAlleleNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createAlleleId()).thenReturn("http://immunogenomics.org/allele/1");
        assertEquals("http://immunogenomics.org/allele/1", glstringResolver.resolveAllele("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveAlleleListNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createAlleleListId()).thenReturn("http://immunogenomics.org/allele-list/1");
        assertEquals("http://immunogenomics.org/allele-list/1", glstringResolver.resolveAlleleList("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveHaplotypeNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createHaplotypeId()).thenReturn("http://immunogenomics.org/haplotype/1");
        assertEquals("http://immunogenomics.org/haplotype/1", glstringResolver.resolveHaplotype("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveGenotypeNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createGenotypeId()).thenReturn("http://immunogenomics.org/genotype/1");
        assertEquals("http://immunogenomics.org/genotype/1", glstringResolver.resolveGenotype("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveGenotypeListNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createGenotypeListId()).thenReturn("http://immunogenomics.org/genotype-list/1");
        assertEquals("http://immunogenomics.org/genotype-list/1", glstringResolver.resolveGenotypeList("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveMultilocusUnphasedGenotypeNotFoundInDynamoDB() {
        when(getItemResult.getItem()).thenReturn(null);
        when(idSupplier.createMultilocusUnphasedGenotypeId()).thenReturn("http://immunogenomics.org/multilocus-unphased-genotype/1");
        assertEquals("http://immunogenomics.org/multilocus-unphased-genotype/1", glstringResolver.resolveMultilocusUnphasedGenotype("HLA-Z*01:01:01:01"));
    }

    @Test
    public void testResolveLocus() {
        when(getItemResult.getItem()).thenReturn(locusItem);
        super.testResolveLocus();
    }

    @Test
    public void testResolveAllele() {
        when(getItemResult.getItem()).thenReturn(alleleItem);
        super.testResolveAllele();
    }

    @Test
    public void testResolveAlleleList() {
        when(getItemResult.getItem()).thenReturn(alleleListItem);
        super.testResolveAlleleList();
    }

    @Test
    public void testResolveHaplotype() {
        when(getItemResult.getItem()).thenReturn(haplotypeItem);
        super.testResolveHaplotype();
    }

    @Test
    public void testResolveGenotype() {
        when(getItemResult.getItem()).thenReturn(genotypeItem);
        super.testResolveGenotype();
    }

    @Test
    public void testResolveGenotypeList() {
        when(getItemResult.getItem()).thenReturn(genotypeListItem);
        super.testResolveGenotypeList();
    }

    @Test
    public void testResolveMultilocusUnphasedGenotype() {
        when(getItemResult.getItem()).thenReturn(multilocusUnphasedGenotypeItem);
        super.testResolveMultilocusUnphasedGenotype();
    }
}