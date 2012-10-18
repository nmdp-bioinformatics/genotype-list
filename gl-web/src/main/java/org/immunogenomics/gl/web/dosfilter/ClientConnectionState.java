package org.immunogenomics.gl.web.dosfilter;

import java.util.concurrent.TimeUnit;

public class ClientConnectionState {

    private final DenialOfServiceConfigMBean config;
    protected State state;

    private ClientConnectionState(DenialOfServiceConfigMBean config) {
        this.config = config;
        state = new FreeHitState();
    }

    public long getLastAccessTime() {
        return state.getLastAccessTime();
    }

    public boolean isBlocked(long requestTime) {
        state = state.nextState(requestTime, config);
        state.lastAccessTime = requestTime;
        return state.isBlocked();
    }

    public static ClientConnectionState create(DenialOfServiceConfigMBean config) {
        return new ClientConnectionState(config);
    }
    
    private static abstract class State {
        protected long lastAccessTime;
        public abstract State nextState(long requestTime, DenialOfServiceConfigMBean config);
        
        public String toString() {
            return getClass().getSimpleName();
        }
        public long getLastAccessTime() {
            return lastAccessTime;
        }
        
        public boolean isBlocked() {
            return false;
        }

    }
    
    private static class FreeHitState extends State {
        private int hits = 0;
        @Override
        public State nextState(long requestTime, DenialOfServiceConfigMBean config) {
            ++hits;
            if (hits > config.getFreeHitCount()) {
                return new ThrottledState(requestTime);
            }
            return this;
        }

    }
    
    private static class ThrottledState extends State {
        private int hits = 0;
        private final long endThrottleTime;
        public ThrottledState(long requestTime) {
            // set the time the throttle expires
            this.endThrottleTime = requestTime + TimeUnit.MINUTES.toMillis(1);
        }
        
        @Override
        public State nextState(long requestTime, DenialOfServiceConfigMBean config) {
            ++hits;
            if (requestTime > endThrottleTime) {
                return new FreeHitState();
            } else if (hits > config.getAnonymousHitsPerMinute()) {
                return new BlockedState();
            } else {
                // Delay response and continue throttling
                try {
                    Thread.sleep(config.getThrottleDelay());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return this;
            }
        }
    }
    
    private static class BlockedState extends State {

        @Override
        public State nextState(long requestTime, DenialOfServiceConfigMBean config) {
            return this;
        }

        @Override
        public boolean isBlocked() {
            return true;
        }
        
    }
}
