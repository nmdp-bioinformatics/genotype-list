/*

    gl-web  Reusable web components.
    Copyright (c) 2012-2015 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl.web.dosfilter;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.immunogenomics.gl.web.JmxUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Servlet filter to prevent denial of service attacks.
 *
 * <bold>Sample Configuration:</bold>
 * <pre>
 * &lt;filter>
 *    &lt;filter-name>DenialOfServiceFilter&lt;/filter-name>
 *     &lt;filter-class>org.immunogenomics.gl.web.dosfilter.DenialOfServiceFilter&lt;/filter-class>
 *     &lt;init-param>
 *         &lt;param-name>cleanInterval&lt;/param-name>
 *         &lt;param-value>5&lt;/param-value>
 *         &lt;description>Minimum minutes before a clean occurs&lt;/description>
 *     &lt;/init-param>
 *     &lt;init-param>
 *         &lt;param-name>freeHitCount&lt;/param-name>
 *         &lt;param-value>50&lt;/param-value>
 *         &lt;description>the number of hits that are not restricted&lt;/description>
 *     &lt;/init-param>
 *     &lt;init-param>
 *         &lt;param-name>anonymousHitsPerMinute&lt;/param-name>
 *         &lt;param-value>10&lt;/param-value>
 *         &lt;description>maximum number of hits per minute for anonymous access&lt;/description>
 *     &lt;/init-param>
 *     &lt;init-param>
 *         &lt;param-name>throttleDelay&lt;/param-name>
 *         &lt;param-value>51&lt;/param-value>
 *         &lt;description>the delay in milliseconds to wait before providing a response&lt;/description>
 *     &lt;/init-param>
 *     &lt;init-param>
 *         &lt;param-name>authorizedParamName&lt;/param-name>
 *         &lt;param-value>signature&lt;/param-value>
 *         &lt;description>the name of a request parameter that is required for authorized access&lt;/description>
 *     &lt;/init-param>
 * &lt;/filter>
 * </pre>
 */
public class DenialOfServiceFilter implements Filter {

    private static final ConcurrentHashMap<String, ClientConnectionState> ipToInfo = new ConcurrentHashMap<String, ClientConnectionState>();
    private static long nextCleanTime = 0;
    private DenialOfServiceConfigMXBean config = null;
    private static final Logger logger = LoggerFactory.getLogger(DenialOfServiceFilter.class);

    @Override
    public void init(final FilterConfig filterConfig) throws ServletException {
        ServletContext servletContext = filterConfig.getServletContext();
        String contextPath = servletContext.getContextPath();
        try {
            String filterName = filterConfig.getFilterName() + contextPath;
            config = JmxUtils.getMXBean(filterName, DenialOfServiceConfigMXBean.class);
            if (config == null) {
                config = createConfig(filterConfig);
                JmxUtils.registerMXBean(filterName, config);
            }
            servletContext.log("DenialOfServiceFilter initialized.");
        } catch (Exception ex) {
            servletContext.log("Unable to configure MBeans", ex);
            config = createConfig(filterConfig);
        }
    }

    private DenialOfServiceConfigMXBean createConfig(final FilterConfig filterConfig) {
        DenialOfServiceConfig newConfig = new DenialOfServiceConfig();
        try {
            newConfig.updateCleanIntervalInMinutes(filterConfig.getInitParameter("cleanInterval"));
            newConfig.updateFreeHitCount(filterConfig.getInitParameter("freeHitCount"));
            newConfig.updateAnonymousHitsPerMinute(filterConfig.getInitParameter("anonymousHitsPerMinute"));
            newConfig.updateThrottleDelay(filterConfig.getInitParameter("throttleDelay"));
            newConfig.setAuthorizationAttribName(filterConfig.getInitParameter("authorizationAttribName"));
        }
        catch (RuntimeException ex) {
            filterConfig.getServletContext().log("Unable to configure " + getClass().getName(), ex);
        }
        return newConfig;
    }

    @Override
    public void destroy() {
        // empty
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response, final FilterChain chain)
        throws IOException, ServletException {

        if (isAuthorized(request) || ! isBlocked(request)) {
            chain.doFilter(request, response);
        }
        else {
            sendBlocked((HttpServletResponse) response);
        }
    }

    private void sendBlocked(final HttpServletResponse servletResponse) throws IOException {
        servletResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Contact site adminitrator to get additional permissions or bandwidth");
    }
    
    private boolean isAuthorized(final ServletRequest request) {
        String authorizationAttribName = config.getAuthorizationAttribName();
        Object attribute = request.getAttribute(authorizationAttribName);
        logger.debug("isAuthorized {} = {}", authorizationAttribName, attribute);
        return attribute != null;
    }

    private boolean isBlocked(final ServletRequest request) {
        long requestTime = System.currentTimeMillis();
        String remoteIpAddr = request.getRemoteAddr();
        ClientConnectionState state = ipToInfo.get(remoteIpAddr);
        if (state == null) {
            state = ClientConnectionState.create(config);
            ipToInfo.put(remoteIpAddr, state);
        }
        cleanUp(requestTime);
        return state.isBlocked(requestTime);
    }

    /**
     * Remove old IP States to prevent memory issues.
     */
    private void cleanUp(final long requestTime) {
        if (requestTime > nextCleanTime) {
            nextCleanTime = requestTime + config.getCleanInterval();
            ipToInfo.clear();
        }
    }
}
