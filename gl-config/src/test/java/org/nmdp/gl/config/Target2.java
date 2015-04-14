/*

    gl-config  Property and environment variable configuration module.
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
package org.nmdp.gl.config;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Property injection target.
 */
public final class Target2 {
    private final String value;
    private final int intValue;
    private final String inner1Value;
    private final String inner2Value;
    private final String namedValue;

    @Inject
    public Target2(@Property final String value,
                   @IntegerProperty final int intValue,
                   @Outer.Inner1 final String inner1Value,
                   @Outer.Inner2 final String inner2Value,
                   @Named("named") final String namedValue,
                   @Named("does-not-exist") final String doesNotExist) {

        this.value = value;
        this.intValue = intValue;
        this.inner1Value = inner1Value;
        this.inner2Value = inner2Value;
        this.namedValue = namedValue;
    }

    public String getValue() {
        return value;
    }

    public int getIntegerValue() {
        return intValue;
    }

    public String getInner1Value() {
        return inner1Value;
    }

    public String getInner2Value() {
        return inner2Value;
    }

    public String getNamedValue() {
        return namedValue;
    }
}