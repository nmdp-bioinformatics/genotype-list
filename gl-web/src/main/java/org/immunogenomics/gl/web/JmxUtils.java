package org.immunogenomics.gl.web;

import java.lang.management.ManagementFactory;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JmxUtils {

    private static Logger logger = LoggerFactory.getLogger(JmxUtils.class);

    public static  <T> T getMBean(String name, Class<T> mbeanClass) throws MalformedObjectNameException {
        T mbeanProxy = null;
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        ObjectName mbeanName = toObjectName(name, mbeanClass);
        if (mBeanServer.isRegistered(mbeanName)) {
            mbeanProxy = JMX.newMBeanProxy(mBeanServer, mbeanName, mbeanClass, true);
        }
        return  mbeanProxy;
    }

    public static void registerMBean(String name, Object mbean) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        
        try {
            Class<?> objClass = mbean.getClass();
            Class<?>[] interfaces = objClass.getInterfaces();
            Class<?> mbeanClass = objClass;
            for (Class<?> class1 : interfaces) {
                if (class1.getSimpleName().endsWith("MBean")) {
                    mbeanClass = class1;
                }
            }
            ObjectName mbeanName = toObjectName(name, mbeanClass);
            mBeanServer.registerMBean(mbean, mbeanName);
        } catch (Exception e) {
            logger.warn("Failed registering MBean: " + name, e);
        }
    }

    private static ObjectName toObjectName(String name, Class<?> mbeanClass)
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
