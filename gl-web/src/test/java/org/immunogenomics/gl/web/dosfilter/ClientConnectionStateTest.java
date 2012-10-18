package org.immunogenomics.gl.web.dosfilter;

import static org.junit.Assert.*;

import java.util.concurrent.TimeUnit;

import org.immunogenomics.gl.web.dosfilter.ClientConnectionState;
import org.immunogenomics.gl.web.dosfilter.DenialOfServiceConfig;
import org.junit.Before;
import org.junit.Test;

public class ClientConnectionStateTest {

    private ClientConnectionState connectionState;
    private long startTime;
    private DenialOfServiceConfig config;

    @Before
    public void setupTest() {
        config = new DenialOfServiceConfig();
        config.setFreeHitCount(10);
        config.setThrottleDelay(5);
        config.setAnonymousHitsPerMinute(15);
        connectionState = ClientConnectionState.create(config);
        startTime = System.currentTimeMillis();
    }
    
    @Test
    public void testFreeHits() {
        long requestTime = startTime;
        for (int i = 0; i <= config.getFreeHitCount(); ++i) {
            ++requestTime;
            boolean blocked = connectionState.isBlocked(requestTime);
            assertFalse("freeHit " + i, blocked);
        }
        long time = System.currentTimeMillis();
        assertTrue("not delayed", startTime + 2 > time);
    }

    @Test
    public void testThrottle() {
        testFreeHits();
        boolean blocked = connectionState.isBlocked(startTime);
        assertFalse("throttled should not be blocked", blocked);
        long time = System.currentTimeMillis();
        long delta = time - startTime;
        assertTrue("delayed", delta >= config.getThrottleDelay());
    }
    
    @Test
    public void testThrottleAndReturn() {
        testThrottle();
        long futureTime = startTime + TimeUnit.MINUTES.toMillis(2);
        // Use future time to trigger return to normal processing
        boolean blocked = connectionState.isBlocked(futureTime);
        assertFalse("throttled should not be blocked", blocked);
        long beginTime = System.currentTimeMillis();
        blocked = connectionState.isBlocked(futureTime);
        assertFalse("freeHit after throttle", blocked);
        long endTime = System.currentTimeMillis();
        long delta = endTime - beginTime;
        assertTrue("not throttled " + delta, delta < config.getThrottleDelay());
    }
    
    @Test
    public void testThrottleAndBlock() {
        testThrottle();
        long requestTime = startTime;
        config.setThrottleDelay(1);  // Shorten throttle delay to speed JUnit test
        for (int i = 1; i < config.getAnonymousHitsPerMinute(); ++i) {
            if (connectionState.isBlocked(requestTime)) {
                fail("throttled should not be blocked " + i);
            }
        }
        for (int i = 1; i < 10; ++i) {
            if (! connectionState.isBlocked(requestTime)) {
                fail("connection should not be blocked " + i);
            }
        }
    }

}
