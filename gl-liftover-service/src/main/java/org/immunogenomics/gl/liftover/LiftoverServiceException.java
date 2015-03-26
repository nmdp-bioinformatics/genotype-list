/*

    gl-liftover-service  Genotype list liftover service.
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
package org.immunogenomics.gl.liftover;

/**
 * Genotype list liftover service exception.
 */
public final class LiftoverServiceException extends Exception {

    /**
     * Create a new liftover service exception with the specified message.
     *
     * @param message message
     */
    public LiftoverServiceException(final String message) {
        super(message);
    }

    /**
     * Create a new liftover service exception with the specified message and cause.
     *
     * @param message message
     * @param cause cause
     */
    public LiftoverServiceException(final String message, final Throwable cause) {
        super(message, cause);
    }
}