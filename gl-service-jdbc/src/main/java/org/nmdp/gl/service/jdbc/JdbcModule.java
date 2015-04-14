/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.nmdp.gl.service.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.sql.DataSource;

import org.nmdp.gl.Allele;
import org.nmdp.gl.Locus;

import org.nmdp.gl.service.GlRegistry;
import org.nmdp.gl.service.GlstringResolver;
import org.nmdp.gl.service.IdResolver;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import com.google.inject.name.Names;

import com.jolbox.bonecp.BoneCPDataSource;

/**
 * JDBC module.
 */
public final class JdbcModule extends AbstractModule {

    @Override
    protected void configure() {
        Names.bindProperties(binder(), loadProperties());

        bind(IdResolver.class).to(JdbcIdResolver.class);
        bind(GlstringResolver.class).to(JdbcGlstringResolver.class);
        bind(GlRegistry.class).to(JdbcGlRegistry.class);
    }

    @Provides
    Cache<String, String> createIdCache() {
        return CacheBuilder.newBuilder().maximumSize(10000).build();
    }

    @Provides
    Cache<String, Locus> createLociCache() {
        return CacheBuilder.newBuilder().maximumSize(1000).build();
    }

    @Provides
    Cache<String, Allele> createAlleleCache() {
        return CacheBuilder.newBuilder().maximumSize(10000).build();
    }

    @Provides @Singleton
    DataSource createDataSource(@Named("jdbcDriver") final String driver,
                                @Named("jdbcUrl") final String url,
                                @Named("jdbcUsername") final String username,
                                @Named("jdbcPassword") final String password) {

        try {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e) {
            // ignore
        }
        BoneCPDataSource dataSource = new BoneCPDataSource();
        dataSource.setJdbcUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        // todo: configure pool
        return dataSource;
    }

    private Properties loadProperties() {
        Properties properties = new Properties();
        InputStream inputStream = null;
        try {
            inputStream = getClass().getResourceAsStream("/gl-service.properties");
            properties.load(inputStream);
        }
        catch (IOException e) {
            // ignore
        }
        finally {
            try {
                inputStream.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return properties;
    }
}