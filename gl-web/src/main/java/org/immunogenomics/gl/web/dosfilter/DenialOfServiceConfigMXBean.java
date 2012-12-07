package org.immunogenomics.gl.web.dosfilter;

public interface DenialOfServiceConfigMXBean {

    String toString();

    /** Return the number of hits that are not restricted. */
    int getFreeHitCount();

    void setFreeHitCount(int freeHitCount);

    /** Return maximum number of hits per minute for anonymous access. */
    int getAnonymousHitsPerMinute();

    void setAnonymousHitsPerMinute(int anonymousHitsPerMinute);

    long getCleanInterval();

    void setCleanInterval(long cleanInterval);

    void updateCleanIntervalInMinutes(String cleanIntervalMin) throws NumberFormatException;

    void updateFreeHitCount(String hitCountStr);

    void updateAnonymousHitsPerMinute(String hitCountStr);

    /** Return the delay in milliseconds to wait before providing a response. */
    long getThrottleDelay();

    void setThrottleDelay(long throttleDelay);

    void updateThrottleDelay(String msString);
    
    /** Returns the name of a request attribute that is required for authorized access. 
     * If present, the client is considered authorized.  
     * A separate SecurityFilter should actually test authorization and set the attribute. */
    String getAuthorizationAttribName();

}