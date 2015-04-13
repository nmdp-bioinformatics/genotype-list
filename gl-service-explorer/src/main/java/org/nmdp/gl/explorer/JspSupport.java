package org.nmdp.gl.explorer;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class JspSupport {
    public static synchronized String getServerUrl(HttpServletRequest request) {
        return getPropertyServerUrl();
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
