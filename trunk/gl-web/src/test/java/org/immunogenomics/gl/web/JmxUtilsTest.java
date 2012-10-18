package org.immunogenomics.gl.web;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import javax.management.MalformedObjectNameException;

import org.immunogenomics.gl.web.JmxUtils;
import org.immunogenomics.gl.web.dosfilter.DenialOfServiceConfig;
import org.immunogenomics.gl.web.dosfilter.DenialOfServiceConfigMBean;
import org.junit.Test;

public class JmxUtilsTest {

    @Test
    public void test() throws MalformedObjectNameException {
        DenialOfServiceConfigMBean mbean = JmxUtils.getMBean("one", DenialOfServiceConfigMBean.class);
        assertNull("registered mbean one", mbean);
        DenialOfServiceConfig denialOfServiceConfig = new DenialOfServiceConfig();
        JmxUtils.registerMBean("one", denialOfServiceConfig);
        DenialOfServiceConfig denialOfServiceConfig2 = new DenialOfServiceConfig();
        JmxUtils.registerMBean("two", denialOfServiceConfig2);
        mbean = JmxUtils.getMBean("one", DenialOfServiceConfigMBean.class);
        assertNotNull("No registered mbean one", mbean);
        assertEquals(denialOfServiceConfig.getCleanInterval(), mbean.getCleanInterval());
        mbean.updateCleanIntervalInMinutes("1");
        assertEquals(denialOfServiceConfig.getCleanInterval(), mbean.getCleanInterval());
    }

    public static void main(String[] args) throws MalformedObjectNameException, InterruptedException {
        // Create some JMX MBeans and leave process running
        new JmxUtilsTest().test();
        Thread.sleep(TimeUnit.MINUTES.toMillis(10));
        
    }
}
