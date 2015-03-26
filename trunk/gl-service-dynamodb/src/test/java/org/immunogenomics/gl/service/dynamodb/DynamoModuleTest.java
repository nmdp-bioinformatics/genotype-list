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

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Test;

import org.immunogenomics.gl.service.Namespace;
import org.immunogenomics.gl.service.id.IdModule;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.AbstractModule;

/**
 * Unit test for DynamoModule.
 */
public final class DynamoModuleTest {
    private DynamoModule dynamoModule;

    @Before
    public void setUp() {
        dynamoModule = new DynamoModule();
    }

    @Test
    public void testConstructor() {
        assertNotNull(dynamoModule);
    }

    @Test
    public void testDynamoModule() {
        Injector injector = Guice.createInjector(new TestModule(), new IdModule(), dynamoModule);
        assertNotNull(injector);
    }

    /**
     * Test module.
     */
    private static class TestModule extends AbstractModule {
        @Override
        protected void configure() {
            bind(String.class).annotatedWith(Namespace.class).toInstance("http://immunogenomics.org/");
        }
    }
}