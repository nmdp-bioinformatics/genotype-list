package org.immunogenomics.gl.service.webapp;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.immunogenomics.gl.service.spark.SparkConfigurationModule;
import org.immunogenomics.gl.service.spark.SparkGlServiceApplication;

import spark.servlet.SparkFilter;

public class GlSparkFilter extends SparkFilter {

    
    private String namespace;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        String contextName = servletContext.getContextPath();
        servletContext.log("ServletContext: " + contextName);
        SparkConfigurationModule.setThreadContext(contextName);
        super.init(filterConfig);
        SparkConfigurationModule.setThreadContext(null);
        namespace = SparkGlServiceApplication.getNamespace();
        servletContext.log("namespace: " + namespace);
    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
            ServletException
    {
        request.setAttribute("Namespace", namespace); // Make it easy for JSP to use
        super.doFilter(request, response, chain);
    }
}
