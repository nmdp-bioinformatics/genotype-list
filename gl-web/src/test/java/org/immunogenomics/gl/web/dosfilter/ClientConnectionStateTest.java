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
        int throttleDelayInMs = 20;
        config.setThrottleDelay(throttleDelayInMs + 1);
        testFreeHits();
        connectionState.isBlocked(startTime);
        boolean blocked = connectionState.isBlocked(startTime);
        assertFalse("throttled should not be blocked", blocked);
        long time = System.currentTimeMillis();
        long delta = time - startTime;
        assertTrue("delayed", delta >= throttleDelayInMs);
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
        int hitCount = config.getAnonymousHitsPerMinute() * 3 / 2;
        int blockedCount = 0;
        int throttleCount = 0;
        for (int i = 1; i < hitCount; ++i) {
            if (connectionState.isBlocked(requestTime)) {
                blockedCount += 1;
                if (throttleCount == 0) {
                    fail("throttled should not be blocked " + i);
                }
            } else {
                throttleCount += 1;
                if (blockedCount > 0) {
                    fail("Shouldn't go from blocked to throttled state");
                }
            }
        }
        assertTrue("blocked hit", blockedCount > 0);
    }

}
