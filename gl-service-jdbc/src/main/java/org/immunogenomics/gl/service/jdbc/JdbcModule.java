/*

    gl-service-jdbc  Implementation of persistent cache for gl-service using JDBC.
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
package org.immunogenomics.gl.service.jdbc;

import javax.sql.DataSource;

import org.immunogenomics.gl.service.GlRegistry;
import org.immunogenomics.gl.service.GlstringResolver;
import org.immunogenomics.gl.service.IdResolver;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

/**
 * JDBC module.
 */
public final class JdbcModule extends AbstractModule {

    @Override
    protected void configure() {
        bind(IdResolver.class).to(JdbcIdResolver.class);
        bind(GlstringResolver.class).to(JdbcGlstringResolver.class);
        bind(GlRegistry.class).to(JdbcGlRegistry.class);
    }

    @Provides
    DataSource createDataSource() {
        return null;
    }
    // configure and create connection/connection pool
}