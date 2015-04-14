/*

    gl-liftover-service-spark  Implementation of a RESTful genotype list liftover service using Spark.
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
package org.nmdp.gl.liftover.spark;

import static org.junit.Assert.assertNotNull;

import com.fasterxml.jackson.core.JsonFactory;

import org.nmdp.gl.liftover.LiftoverService;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit test for SparkLiftoverService.
 */
public final class SparkLiftoverServiceTest {
    private JsonFactory jsonFactory;
    private SparkLiftoverService sparkLiftoverService;

    @Mock
    private LiftoverService liftoverService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonFactory = new JsonFactory();
        sparkLiftoverService = new SparkLiftoverService(jsonFactory, liftoverService);
    }

    @Test
    public void testConstructor() {
        assertNotNull(sparkLiftoverService);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullJsonFactory() {
        new SparkLiftoverService(jsonFactory, null);
    }

    @Test(expected=NullPointerException.class)
    public void testConstructorNullLiftoverService() {
        new SparkLiftoverService(null, liftoverService);
    }
}