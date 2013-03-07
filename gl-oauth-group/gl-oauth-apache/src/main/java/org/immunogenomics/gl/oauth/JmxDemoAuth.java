package org.immunogenomics.gl.oauth;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * DemoAuthMXBean that uses JMX to communicate between client and server instance.
 * The server should  perform registerJmxBean.
 * The client instantiates a new JmxDemoAuth using the same JMX container.
 */
public class JmxDemoAuth implements DemoAuthMXBean {

    private static final String MXBEAN_FULL_NAME = "org.immunogenomics.gl.oauth:name=DemoAuth";

    private static class DemoAuthBean implements DemoAuthMXBean {
        private AuthorizationManager delegate = null;
        public DemoAuthBean(AuthorizationManager delegate) {
            this.delegate = delegate;
        }
        @Override
        public String getDemoAccessToken(String realm) {
            return delegate.getAuthorization(DEMO_USER_ID, realm);
        }
    }

    private DemoAuthMXBean proxy;

    /** Authorization provided by registered JMXBean. */
    public JmxDemoAuth() {
        // lazy initialized to limit race conditions.
    }
    
    /** Authorization provided by delegate. */
    public static void registerJmxBean(AuthorizationManager delegate) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName mbeanName = new ObjectName(MXBEAN_FULL_NAME);
            mBeanServer.registerMBean(new DemoAuthBean(delegate), mbeanName);
        } catch (Exception e) {
            Logger.getLogger(JmxDemoAuth.class.getName()).warning("JMXBean " + MXBEAN_FULL_NAME + " not registered due to " + e);
        }
    }

    private synchronized DemoAuthMXBean useProxy() {
        if (proxy == null) {
            // Attempt to use JMX to configure delegate
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            try {
                ObjectName mbeanName = new ObjectName(MXBEAN_FULL_NAME);
                proxy = JMX.newMXBeanProxy(mBeanServer, mbeanName, DemoAuthMXBean.class);
            } catch (Exception e) {
                throw new RuntimeException("JMXBean " + MXBEAN_FULL_NAME + " not proxied", e);
            }
        }
        return proxy;
    }

    @Override
    public String getDemoAccessToken(String realm) {
        return useProxy().getDemoAccessToken(realm);
    }

}
