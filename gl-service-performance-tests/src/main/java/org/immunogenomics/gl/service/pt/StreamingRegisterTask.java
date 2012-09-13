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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Streaming register task.
 */
class StreamingRegisterTask extends AbstractRegisterTask {

    /**
     * Create a new streaming register task.
     *
     * @param ns namespace, must not be null
     * @param type type, must not be null
     * @param n number of threads, must be &gt;= zero
     * @param glstringFile glstring file
     */
    StreamingRegisterTask(final String ns, final String type, final int n, final File glstringFile) {
        super(ns, type, n, glstringFile);
    }

    @Override
    public void run() {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader((glstringFile == null) ? new InputStreamReader(System.in) : new FileReader(glstringFile));
            BlockingQueue<Future<?>> futures = new ArrayBlockingQueue<Future<?>>(n);
            long start = System.nanoTime();
            final AtomicInteger count = new AtomicInteger();
            while (reader.ready()) {
                final String line = reader.readLine();
                if (line == null) {
                    break;
                }
                futures.add(executorService.submit(new Runnable() {
                        @Override
                        public void run() {
                            with().body(line).contentType("text/plain").post(ns + type);
                            if (count.getAndIncrement() % 1000 == 0) {
                                System.out.print(".");
                            }
                        }
                    }));
                if (futures.size() == n) {
                    while (!futures.isEmpty()) {
                        Future<?> future = futures.take();
                        future.get();
                    }
                }
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
        finally {
            try {
                reader.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
    }
}