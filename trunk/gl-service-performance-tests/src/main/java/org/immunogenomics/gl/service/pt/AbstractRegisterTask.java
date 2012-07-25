/*

    gl-service-performance-tests  Performance tests for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.pt;

import java.io.File;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutorService;

/**
 * Abstract register task.
 */
abstract class AbstractRegisterTask implements Runnable {
    protected final int n;
    protected final String ns;
    protected final String type;
    protected final File glstringFile;
    protected final ExecutorService executorService;

    /**
     * Create a new abstract register task.
     *
     * @param ns namespace, must not be null
     * @param type type, must not be null
     * @param n number of threads, must be &gt;= zero
     * @param glstringFile glstring file
     */
    AbstractRegisterTask(final String ns, final String type, final int n, final File glstringFile) {
        if (ns == null) {
            throw new IllegalArgumentException("ns must not be null");
        }
        if (type == null) {
            throw new IllegalArgumentException("type must not be null");
        }
        if (n < 0) {
            throw new IllegalArgumentException("n must be at least zero");
        }
        this.ns = ns;
        this.type = type;
        this.n = n;
        this.glstringFile = glstringFile;
        this.executorService = Executors.newFixedThreadPool(n);
    }
}