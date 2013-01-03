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

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.immunogenomics.gl.service.dynamodb.DynamoUtils.hash;

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

import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdSupplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * DynamoDB based glstring resolver.
 */
@Immutable
public final class DynamoGlstringResolver implements GlstringResolver {
    private final IdSupplier idSupplier;
    private final AmazonDynamoDBAsync dynamo;
    private final Logger logger = LoggerFactory.getLogger(DynamoGlstringResolver.class);

    @Inject
    public DynamoGlstringResolver(final IdSupplier idSupplier, final AmazonDynamoDBAsync dynamo) {
        checkNotNull(idSupplier);
        checkNotNull(dynamo);
        this.idSupplier = idSupplier;
        this.dynamo = dynamo;
    }


    @Override
    public String resolveLocus(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String locusId = getIdentifier("locus", glstring);
        if (locusId != null) {
            return locusId;
        }
        return idSupplier.createLocusId();
    }

    @Override
    public String resolveAllele(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String alleleId = getIdentifier("allele", glstring);
        if (alleleId != null) {
            return alleleId;
        }
        return idSupplier.createAlleleId();
    }

    @Override
    public String resolveAlleleList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String alleleListId = getIdentifier("alleleList", glstring);
        if (alleleListId != null) {
            return alleleListId;
        }
        return idSupplier.createAlleleListId();
    }

    @Override
    public String resolveHaplotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String haplotypeId = getIdentifier("haplotype", glstring);
        if (haplotypeId != null) {
            return haplotypeId;
        }
        return idSupplier.createHaplotypeId();
    }

    @Override
    public String resolveGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String genotypeId = getIdentifier("genotype", glstring);
        if (genotypeId != null) {
            return genotypeId;
        }
        return idSupplier.createGenotypeId();
    }

    @Override
    public String resolveGenotypeList(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String genotypeListId = getIdentifier("genotypeList", glstring);
        if (genotypeListId != null) {
            return genotypeListId;
        }
        return idSupplier.createGenotypeListId();
    }

    @Override
    public String resolveMultilocusUnphasedGenotype(final String glstring) {
        checkNotNull(glstring);
        checkArgument(!glstring.isEmpty());
        String multilocusUnphasedGenotypeId = getIdentifier("multilocusUnphasedGenotype", glstring);
        if (multilocusUnphasedGenotypeId != null) {
            return multilocusUnphasedGenotypeId;
        }
        return idSupplier.createMultilocusUnphasedGenotypeId();
    }

    private String getIdentifier(final String type, final String glstring) {
        byte[] glstringBytes = hash(type + ":" + glstring);
        ByteBuffer glstringByteBuffer = ByteBuffer.wrap(glstringBytes);

        Key key = new Key().withHashKeyElement(new AttributeValue().withB(glstringByteBuffer));
        GetItemRequest getItemRequest = new GetItemRequest().withTableName("identifiers").withKey(key);
        Future<GetItemResult> future = dynamo.getItemAsync(getItemRequest);

        long before = System.nanoTime();
        try {
            GetItemResult getItemResult = future.get();
            long after = System.nanoTime();
            logger.info("get identifier took {} ns and consumed {} capacity units",
                        new Object[] { (after - before), getItemResult.getConsumedCapacityUnits() });

            if (getItemResult.getItem() != null) {
                return getItemResult.getItem().get("id").getS();
            }
        }
        catch (InterruptedException e) {
            logger.warn("get identifier was interrupted", e);
        }
        catch (ExecutionException e) {
            logger.warn("get identifier caught ExecutionException", e);
        }
        return null;
    }
}