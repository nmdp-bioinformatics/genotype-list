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

import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.dynamodb.DynamoUtils.deserialize;

import java.nio.ByteBuffer;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import javax.annotation.concurrent.Immutable;

import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.model.AttributeValue;
import com.amazonaws.services.dynamodb.model.Key;
import com.amazonaws.services.dynamodb.model.GetItemRequest;
import com.amazonaws.services.dynamodb.model.GetItemResult;

import com.google.inject.Inject;

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

/**
 * DynamoDB based identifier resolver.
 */
@Immutable
public final class DynamoIdResolver implements IdResolver {
    private final AmazonDynamoDBAsync dynamo;
    private final Logger logger = LoggerFactory.getLogger(DynamoIdResolver.class);

    @Inject
    public DynamoIdResolver(final AmazonDynamoDBAsync dynamo) {
        checkNotNull(dynamo);
        this.dynamo = dynamo;
    }


    @Override
    public Locus findLocus(final String id) {
        return (Locus) getGlResource(id);
    }

    @Override
    public Allele findAllele(final String id) {
        return (Allele) getGlResource(id);
    }

    @Override
    public AlleleList findAlleleList(final String id) {
        return (AlleleList) getGlResource(id);
    }

    @Override
    public Haplotype findHaplotype(final String id) {
        return (Haplotype) getGlResource(id);
    }

    @Override
    public Genotype findGenotype(final String id) {
        return (Genotype) getGlResource(id);
    }

    @Override
    public GenotypeList findGenotypeList(final String id) {
        return (GenotypeList) getGlResource(id);
    }

    @Override
    public MultilocusUnphasedGenotype findMultilocusUnphasedGenotype(final String id) {
        return (MultilocusUnphasedGenotype) getGlResource(id);
    }

    private Object getGlResource(final String id) {
        // note: max key length is 2048 bytes
        Key key = new Key().withHashKeyElement(new AttributeValue().withS(id));
        GetItemRequest getItemRequest = new GetItemRequest().withTableName("glResources").withKey(key);
        Future<GetItemResult> future = dynamo.getItemAsync(getItemRequest);

        long before = System.nanoTime();
        try {
            GetItemResult getItemResult = future.get();
            long after = System.nanoTime();
            logger.info("get gl resource {} took {} ns and consumed {} capacity units",
                        new Object[] { id, (after - before), getItemResult.getConsumedCapacityUnits() });

            if (getItemResult.getItem() != null) {
                ByteBuffer byteBuffer = getItemResult.getItem().get("glResource").getB();
                byte[] bytes = new byte[byteBuffer.remaining()];
                byteBuffer.get(bytes);
                return deserialize(bytes);
            }
        }
        catch (InterruptedException e) {
            logger.warn("get gl resource " + id + " was interrupted", e);
        }
        catch (ExecutionException e) {
            logger.warn("get gl resource " + id + " caught ExecutionException", e);
        }
        return null;
    }
}