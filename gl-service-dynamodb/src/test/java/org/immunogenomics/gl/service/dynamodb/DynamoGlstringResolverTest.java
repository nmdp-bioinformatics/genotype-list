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

    private AttributeValue attributeValue;
    private Map<String, AttributeValue> item;
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
        attributeValue = new AttributeValue().withS("http://immunogenomics.org/locus/0");
        item = ImmutableMap.of("id", attributeValue);
        when(getItemResult.getItem()).thenReturn(item);
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
}