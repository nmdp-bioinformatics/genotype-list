/*

    gl-service-dynamodb  Implementation of persistent cache for gl-service using DynamoDB.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.dynamodb.DynamoUtils.hash;
import static org.immunogenomics.gl.service.dynamodb.DynamoUtils.serialize;

import java.nio.ByteBuffer;

import java.util.Map;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.concurrent.Immutable;

import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;

import com.google.common.collect.ImmutableMap;

import com.google.inject.Inject;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.GlResource;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;
import org.immunogenomics.gl.service.GlRegistry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamoDB based genotype list registry.
 */
@Immutable
public final class DynamoGlRegistry implements GlRegistry {
    private final AmazonDynamoDBAsync dynamo;
    private final Logger logger = LoggerFactory.getLogger(DynamoGlRegistry.class);

    @Inject
    public DynamoGlRegistry(final AmazonDynamoDBAsync dynamo) {
        checkNotNull(dynamo);
        this.dynamo = dynamo;
    }

    @Override
    public void registerLocus(final Locus locus) {
        checkNotNull(locus);
        putIdentifier("locus", locus);
        putGlResource("locus", locus);
    }

    @Override
    public void registerAllele(final Allele allele) {
        checkNotNull(allele);
        putIdentifier("allele", allele);
        putGlResource("allele", allele);
    }

    @Override
    public void registerAlleleList(final AlleleList alleleList) {
        checkNotNull(alleleList);
        putIdentifier("alleleList", alleleList);
        putGlResource("alleleList", alleleList);
    }

    @Override
    public void registerHaplotype(final Haplotype haplotype) {
        checkNotNull(haplotype);
        putIdentifier("haplotype", haplotype);
        putGlResource("haplotype", haplotype);
    }

    @Override
    public void registerGenotype(final Genotype genotype) {
        checkNotNull(genotype);
        putIdentifier("genotype", genotype);
        putGlResource("genotype", genotype);
    }

    @Override
    public void registerGenotypeList(final GenotypeList genotypeList) {
        checkNotNull(genotypeList);
        putIdentifier("genotypeList", genotypeList);
        putGlResource("genotypeList", genotypeList);
    }

    @Override
    public void registerMultilocusUnphasedGenotype(final MultilocusUnphasedGenotype multilocusUnphasedGenotype) {
        checkNotNull(multilocusUnphasedGenotype);
        putIdentifier("multilocusUnphasedGenotype", multilocusUnphasedGenotype);
        putGlResource("multilocusUnphasedGenotype", multilocusUnphasedGenotype);
    }

    // todo:  handle runtime exceptions?

    private void putIdentifier(final String type, final GlResource glResource) {
        AttributeValue id = new AttributeValue(type + ":" + glResource.getId());
        Map<String, AttributeValue> item = ImmutableMap.of("id", id);
        PutItemRequest putItemRequest = new PutItemRequest("identifiers", item);
        Future<PutItemResult> future = dynamo.putItemAsync(putItemRequest);

        long before = System.nanoTime();
        try {
            PutItemResult putItemResult = future.get();
            long after = System.nanoTime();
            logger.info("put identifier {} took {} ns and consumed {} capacity units",
                        new Object[] { glResource.getId(), (after - before), putItemResult.getConsumedCapacityUnits() });
        }
        catch (InterruptedException e) {
            logger.warn("put identifier " + glResource.getId() + " was interrupted", e);
        }
        catch (ExecutionException e) {
            logger.warn("put identifier " + glResource.getId() + " caught ExecutionException", e);
        }
    }

    private void putGlResource(final String type, final GlResource glResource) {
        byte[] glstringBytes = hash(type + ":" + glResource.getGlstring());
        ByteBuffer glstringByteBuffer = ByteBuffer.wrap(glstringBytes);

        byte[] glResourceBytes = serialize(glResource);
        ByteBuffer glResourceByteBuffer = ByteBuffer.wrap(glResourceBytes);

        Map<String, AttributeValue> item = ImmutableMap.of("glstring", new AttributeValue().withB(glstringByteBuffer),
                                                           type, new AttributeValue().withB(glResourceByteBuffer));
        PutItemRequest putItemRequest = new PutItemRequest("glResources", item);
        Future<PutItemResult> future = dynamo.putItemAsync(putItemRequest);

        long before = System.nanoTime();
        try {
            PutItemResult putItemResult = future.get();
            long after = System.nanoTime();
            logger.info("put gl resource {} took {} ns and consumed {} capacity units",
                        new Object[] { glResource.getId(), (after - before), putItemResult.getConsumedCapacityUnits() });
        }
        catch (InterruptedException e) {
            logger.warn("put gl resource " + glResource.getId() + " was interrupted", e);
        }
        catch (ExecutionException e) {
            logger.warn("put gl resource " + glResource.getId() + " caught ExecutionException", e);
        }
    }
}