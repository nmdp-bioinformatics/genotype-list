/*

    gl-web  Reusable web components.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.web;

import java.lang.management.ManagementFactory;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JMX utils.
 */
public final class JmxUtils {
    private static final Logger logger = LoggerFactory.getLogger(JmxUtils.class);

    public static <T> T getMXBean(final String name, final Class<T> mbeanClass) throws MalformedObjectNameException {
        T mbeanProxy = null;
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = toObjectName(name, mbeanClass);
        if (mBeanServer.isRegistered(mbeanName)) {
            mbeanProxy = JMX.newMXBeanProxy(mBeanServer, mbeanName, mbeanClass, true);
        }
        return mbeanProxy;
    }

    public static void registerMXBean(final String name, final Object mbean) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        try {
            Class<?> objClass = mbean.getClass();
            Class<?>[] interfaces = objClass.getInterfaces();
            Class<?> mbeanClass = objClass;
            for (Class<?> class1 : interfaces) {
                if (class1.getSimpleName().endsWith("MXBean")) {
                    mbeanClass = class1;
                }
            }
            ObjectName mbeanName = toObjectName(name, mbeanClass);
            mBeanServer.registerMBean(mbean, mbeanName);
        }
        catch (Exception e) {
            logger.warn("Failed registering MBean: " + name, e);
        }
    }

    private static ObjectName toObjectName(final String name, final Class<?> mbeanClass)
        throws MalformedObjectNameException
    {
        String packageName = mbeanClass.getPackage().getName();
        String className = mbeanClass.getSimpleName();
        className = className.replace("MBean", "");
        int breakPos = packageName.indexOf('.', 5);
        if (breakPos > 0) {
            // Don't use full package name
            packageName = packageName.substring(0, breakPos + 1);
        }
        String fullName = packageName + className + ":name=" + name;
        ObjectName mbeanName = new ObjectName(fullName);
        return mbeanName;
    }
}