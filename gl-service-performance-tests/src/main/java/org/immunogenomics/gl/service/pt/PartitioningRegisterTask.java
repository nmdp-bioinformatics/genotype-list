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

import static com.jayway.restassured.RestAssured.with;
import static org.immunogenomics.gl.service.pt.PerformanceTestUtils.readGlstrings;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.collect.Lists;

/**
 * Partitioning register task.  The entire glstring file is read into memory and
 * partitioned for processing across the available threads.
 */
class PartitioningRegisterTask extends AbstractRegisterTask {

    /**
     * Create a new partitioning register task.
     *
     * @param ns namespace, must not be null
     * @param type type, must not be null
     * @param n number of threads, must be &gt;= zero
     * @param glstringFile glstring file
     */
    PartitioningRegisterTask(final String ns, final String type, final int n, final File glstringFile) {
        super(ns, type, n, glstringFile);
    }

    @Override
    public void run() {
        try {
            List<String> glstrings = (glstringFile == null) ? readGlstrings(System.in) : readGlstrings(glstringFile);
            List<List<String>> partitions = Lists.partition(glstrings, glstrings.size() / n);
            List<Future<?>> futures = new LinkedList<Future<?>>();
            long start = System.nanoTime();
            final AtomicInteger count = new AtomicInteger();
            for (int i = 0; i < n; i++) {
                final List<String> partition = partitions.get(i);
                futures.add(executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            for (String glstring : partition) {
                                with().body(glstring).contentType("text/plain").post(ns + type);
                                if (count.getAndIncrement() % 1000 == 0) {
                                    System.out.print(".");
                                }
                            }
                        }
                    }));
            }
            for (Future<?> future : futures) {
                future.get();
            }
            long now = System.nanoTime();
            System.out.println("registered " + count.intValue() + " glstrings of type " + type + " in " + (now - start) + " ns (" + TimeUnit.SECONDS.convert(now - start, TimeUnit.NANOSECONDS) + " s) with " + n + " thread(s)");
            executorService.shutdownNow();
        }
        catch (ExecutionException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}