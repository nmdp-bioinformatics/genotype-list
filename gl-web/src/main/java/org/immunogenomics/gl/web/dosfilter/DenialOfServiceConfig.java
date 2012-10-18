package org.immunogenomics.gl.web.dosfilter;

import java.util.concurrent.TimeUnit;

/** Configuration for Denial of Service */
public class DenialOfServiceConfig implements DenialOfServiceConfigMBean {

    private int freeHitCount = 100;
    private long cleanInterval = TimeUnit.MINUTES.toMillis(5);
    private int anonymousHitsPerMinute = 100;
    private long throttleDelay = 100;
    private String authorizationParamName = "signature";

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#toString()
     */
    @Override
    public String toString() {
        return "DenialOfServiceConfig [freeHitCount=" + freeHitCount 
                + ", anonymousHitsPerMinute=" + anonymousHitsPerMinute
                + ", throttleDelay=" + throttleDelay
                + ", authorizationParamName=" + authorizationParamName
                + ", cleanInterval(min)=" + TimeUnit.MILLISECONDS.toMinutes(cleanInterval) + "]";
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#getFreeHitCount()
     */
    @Override
    public int getFreeHitCount() {
        return freeHitCount;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#setFreeHitCount(int)
     */
    @Override
    public void setFreeHitCount(int freeHitCount) {
        this.freeHitCount = freeHitCount;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#getAnonymousHitsPerMinute()
     */
    @Override
    public int getAnonymousHitsPerMinute() {
        return anonymousHitsPerMinute;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#setAnonymousHitsPerMinute(int)
     */
    @Override
    public void setAnonymousHitsPerMinute(int anonymousHitsPerMinute) {
        this.anonymousHitsPerMinute = anonymousHitsPerMinute;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#getCleanInterval()
     */
    @Override
    public long getCleanInterval() {
        return cleanInterval;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#setCleanInterval(long)
     */
    @Override
    public void setCleanInterval(long cleanInterval) {
        this.cleanInterval = cleanInterval;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#updateCleanInterval(java.lang.String)
     */
    @Override
    public void updateCleanIntervalInMinutes(String cleanIntervalStr) throws NumberFormatException {
        cleanInterval = TimeUnit.MINUTES.toMillis(parseLong(cleanIntervalStr, cleanInterval));
    }

    private long parseLong(String longStr, long defaultValue) throws NumberFormatException {
        if (longStr != null) {
            return Long.parseLong(longStr);
        }
        return defaultValue;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#updateFreeHitCount(java.lang.String)
     */
    @Override
    public void updateFreeHitCount(String hitCountStr) {
        freeHitCount = (int) parseLong(hitCountStr, freeHitCount);
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#updateAnonymousHitsPerMinute(java.lang.String)
     */
    @Override
    public void updateAnonymousHitsPerMinute(String hitCountStr) {
        anonymousHitsPerMinute = (int) parseLong(hitCountStr, anonymousHitsPerMinute);
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#getThrottleDelay()
     */
    @Override
    public long getThrottleDelay() {
        return throttleDelay;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#setThrottleDelay(long)
     */
    @Override
    public void setThrottleDelay(long throttleDelay) {
        this.throttleDelay = throttleDelay;
    }

    /* (non-Javadoc)
     * @see org.immunogenomics.gl.service.webapp.DenialOfServiceMBean#updateThrottleDelay(java.lang.String)
     */
    @Override
    public void updateThrottleDelay(String msString) {
        throttleDelay = (int) parseLong(msString, throttleDelay);
    }

    public void setAuthorizationParamName(String paramName) {
        this.authorizationParamName = paramName;
    }

    @Override
    public String getAuthorizationParamName() {
        return this.authorizationParamName;
    }
}
