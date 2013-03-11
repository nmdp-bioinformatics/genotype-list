package org.immunogenomics.gl.explorer;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.immunogenomics.gl.oauth.DemoAuthMXBean;
import org.immunogenomics.gl.oauth.JmxDemoAuth;

public class JspSupport {
    private static DemoAuthMXBean demoAuth;
    
    /** DemoAuthMXBean used if no valid one available. */
    private static class NullDemoAuth implements DemoAuthMXBean {
        @Override
        public String getDemoAccessToken(String realm) {
            return "not-available";
        }
    }

    public static synchronized String getAccessToken() {
        try {
            if (demoAuth == null) {
                demoAuth = new JmxDemoAuth();
            }
            return demoAuth.getDemoAccessToken("immunogenomics");
        } catch (Exception e) {
            System.err.println("Deactivating DemoAuthMXBean");
            e.printStackTrace();
            demoAuth = new NullDemoAuth();
            return demoAuth.getDemoAccessToken("not-available");
        }
    }
    
    public static synchronized String getServerUrl(HttpServletRequest request) {
        String baseUri = System.getProperty("BaseURI");
        if (baseUri == null) {
            return getPropertyServerUrl();
        }
        String contextPath = request.getContextPath();
        String glPath = contextPath.replaceAll("-explorer", "");
        return baseUri + glPath + "/";
    }
    
    private static String getPropertyServerUrl() {
        Properties properties = new Properties();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try {
            properties.load(classLoader.getResourceAsStream("/gl-service.properties"));
            String serverURL = properties.getProperty("org.immunogenomics.gl.service.Namespace");
            return serverURL;
        } catch (IOException e) {
            e.printStackTrace();
            return "http://localhost:8080/gl/";
        }
    }
}
