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

import static org.junit.Assert.fail;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

import java.util.concurrent.Future;

import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.model.PutItemRequest;
import com.amazonaws.services.dynamodb.model.PutItemResult;

import org.immunogenomics.gl.service.AbstractGlRegistryTest;
import org.immunogenomics.gl.service.GlRegistry;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for DynamoGlRegistry.
 */
public final class DynamoGlRegistryTest extends AbstractGlRegistryTest {
    @Mock
    private AmazonDynamoDBAsync dynamo;
    @Mock
    private Future<PutItemResult> future;
    @Mock
    private PutItemResult putItemResult;

    private Double consumedCapacityUnits = Double.valueOf(1.0d);

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(dynamo.putItemAsync(any(PutItemRequest.class))).thenReturn(future);
        try {
            when(future.get()).thenReturn(putItemResult);
        }
        catch (Exception e) {
            fail(e.getMessage());
        }
        when(putItemResult.getConsumedCapacityUnits()).thenReturn(consumedCapacityUnits);
        super.setUp();
    }

    @Override
    protected GlRegistry createGlRegistry() {
        return new DynamoGlRegistry(dynamo);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullDynamo() {
        new DynamoGlRegistry(null);
    }
}