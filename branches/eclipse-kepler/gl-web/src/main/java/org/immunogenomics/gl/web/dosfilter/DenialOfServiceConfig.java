/*

    gl-web  Reusable web components.
    Copyright (c) 2012-2013 National Marrow Donor Program (NMDP)

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

import java.util.concurrent.TimeUnit;

/**
 * Configuration for denial of service filter.
 */
public class DenialOfServiceConfig implements DenialOfServiceConfigMXBean {

    private int freeHitCount = 100;
    private long cleanInterval = TimeUnit.MINUTES.toMillis(5);
    private int anonymousHitsPerMinute = 100;
    private long throttleDelay = 100;
    private String authorizationAttribName = "signature";

    @Override
    public String toString() {
        return "DenialOfServiceConfig [freeHitCount=" + freeHitCount 
                + ", anonymousHitsPerMinute=" + anonymousHitsPerMinute
                + ", throttleDelay=" + throttleDelay
                + ", authorizationAttribName=" + authorizationAttribName
                + ", cleanInterval(min)=" + TimeUnit.MILLISECONDS.toMinutes(cleanInterval) + "]";
    }

    @Override
    public int getFreeHitCount() {
        return freeHitCount;
    }

    @Override
    public void setFreeHitCount(final int freeHitCount) {
        this.freeHitCount = freeHitCount;
    }

    @Override
    public int getAnonymousHitsPerMinute() {
        return anonymousHitsPerMinute;
    }

    @Override
    public void setAnonymousHitsPerMinute(final int anonymousHitsPerMinute) {
        this.anonymousHitsPerMinute = anonymousHitsPerMinute;
    }

    @Override
    public long getCleanInterval() {
        return cleanInterval;
    }

    @Override
    public void setCleanInterval(final long cleanInterval) {
        this.cleanInterval = cleanInterval;
    }

    @Override
    public void updateCleanIntervalInMinutes(final String cleanIntervalStr) throws NumberFormatException {
        cleanInterval = TimeUnit.MINUTES.toMillis(parseLong(cleanIntervalStr, cleanInterval));
    }

    private long parseLong(final String longStr, final long defaultValue) throws NumberFormatException {
        if (longStr != null) {
            return Long.parseLong(longStr);
        }
        return defaultValue;
    }

    @Override
    public void updateFreeHitCount(final String hitCountStr) {
        freeHitCount = (int) parseLong(hitCountStr, freeHitCount);
    }

    @Override
    public void updateAnonymousHitsPerMinute(final String hitCountStr) {
        anonymousHitsPerMinute = (int) parseLong(hitCountStr, anonymousHitsPerMinute);
    }

    @Override
    public long getThrottleDelay() {
        return throttleDelay;
    }

    @Override
    public void setThrottleDelay(final long throttleDelay) {
        this.throttleDelay = throttleDelay;
    }

    @Override
    public void updateThrottleDelay(final String msString) {
        throttleDelay = (int) parseLong(msString, throttleDelay);
    }

    public void setAuthorizationAttribName(final String attributeName) {
        this.authorizationAttribName = attributeName;
    }

    @Override
    public String getAuthorizationAttribName() {
        return this.authorizationAttribName;
    }
}
