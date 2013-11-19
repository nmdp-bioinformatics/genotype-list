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

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.EnvironmentVariableCredentialsProvider;
import com.amazonaws.services.dynamodb.AmazonDynamoDBAsync;
import com.amazonaws.services.dynamodb.AmazonDynamoDBAsyncClient;

import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

/**
 * DynamoDB module.
 */
public final class DynamoModule extends AbstractModule {

    @Override
    public void configure() {
        bind(IdResolver.class).to(DynamoIdResolver.class);
        bind(GlstringResolver.class).to(DynamoGlstringResolver.class);
        bind(GlRegistry.class).to(DynamoGlRegistry.class);
    }

    @Provides @Singleton
    protected AWSCredentials createAWSCredentials() {
        // AWS_ACCESS_KEY_ID and AWS_SECRET_KEY environment variables
        return new EnvironmentVariableCredentialsProvider().getCredentials();
    }

    @Provides @Singleton
    protected AmazonDynamoDBAsync createDynamo(final AWSCredentials awsCredentials) {
        return new AmazonDynamoDBAsyncClient(awsCredentials);
    }
}