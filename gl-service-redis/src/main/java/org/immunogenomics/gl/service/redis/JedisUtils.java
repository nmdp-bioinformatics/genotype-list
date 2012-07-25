/*

    gl-service-redis  Implementation of persistent cache for gl-service using Redis+jedis.
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
package org.immunogenomics.gl.service.redis;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.annotation.concurrent.Immutable;

import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Static utility methods.
 */
@Immutable
final class JedisUtils {
    private static final Logger logger = LoggerFactory.getLogger(JedisUtils.class);

    static final byte[] encode(final String key) {
        return StringUtils.getBytesUtf8(key);
    }

    static final byte[] serialize(final Object value) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(buffer);
            out.writeObject(value);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("could not serialize value {}, caught", value, e.getMessage());
        }
        finally {
            try {
                out.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return buffer.toByteArray();
    }

    static final Object deserialize(final byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return in.readObject();
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            logger.error("could not deserialize value", e);
        }
        catch (IOException e) {
            e.printStackTrace();
            logger.error("could not deserialize value", e);
        }
        finally {
            try {
                in.close();
            }
            catch (Exception e) {
                // ignore
            }
        }
        return null;
    }
}