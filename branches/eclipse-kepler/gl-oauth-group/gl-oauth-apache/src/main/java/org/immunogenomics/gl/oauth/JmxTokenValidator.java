package org.immunogenomics.gl.oauth;

import java.lang.management.ManagementFactory;
import java.util.logging.Logger;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * TokenValidator that uses JMX to communicate between client and server instance.
 * The server should  perform registerJmxTokenValidator.
 * The client instantiates a new JmxTokenValidator using the same JMX container.
 */
public class JmxTokenValidator implements TokenValidator {

    private static final String MXBEAN_FULL_NAME = "org.immunogenomics.gl.oauth:name=TokenValidator";

    public interface ValidatorMXBean {
        /** @return string representation of AccessTokenDetails. */
        String validate(String accessToken);
    }
    
    private static class ValidatorBean implements ValidatorMXBean {
        private TokenValidator delegate = null;
        public ValidatorBean(TokenValidator delegate) {
            this.delegate = delegate;
        }
        @Override
        public String validate(String accessToken) {
            AccessTokenDetails details = delegate.validate(accessToken);
            return (details == null) ? "" : details.toString();
        }
    }

    private ValidatorMXBean proxy;

    /** Authorization provided by registered JMXBean. */
    public JmxTokenValidator() {
        // lazy initialized to limit race conditions.
    }
    
    /** Authorization provided by delegate. */
    public static void registerJmxTokenValidator(TokenValidator delegate) {
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
        try {
            ObjectName mbeanName = new ObjectName(MXBEAN_FULL_NAME);
            if (mBeanServer.isRegistered(mbeanName)) {
                mBeanServer.unregisterMBean(mbeanName);
            }
            mBeanServer.registerMBean(new ValidatorBean(delegate), mbeanName);
        } catch (Exception e) {
            Logger.getLogger(JmxTokenValidator.class.getName()).warning("JMXBean " + MXBEAN_FULL_NAME + " not registered due to " + e);
        }
    }

    private synchronized ValidatorMXBean useProxy() {
        if (proxy == null) {
            // Attempt to use JMX to configure delegate
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();
            try {
                ObjectName mbeanName = new ObjectName(MXBEAN_FULL_NAME);
                proxy = JMX.newMXBeanProxy(mBeanServer, mbeanName, ValidatorMXBean.class);
            } catch (Exception e) {
                throw new RuntimeException("JMXBean " + MXBEAN_FULL_NAME + " not proxied", e);
            }
        }
        return proxy;
    }

    @Override
    public void close() {
        // nothing to do since this class doesn't own the resources.
    }

    @Override
    public AccessTokenDetails validate(String accessToken) {
        String detailsString = useProxy().validate(accessToken);
        return new AccessTokenDetails(detailsString);
    }

}
