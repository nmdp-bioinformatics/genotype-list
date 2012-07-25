/*

    gl-core  Core interfaces and classes for the gl project.
    Copyright (c) 2012 National Marrow Donor Program (NMDP)

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
package org.immunogenomics.gl;

import static java.util.Collections.reverseOrder;

import java.io.Serializable;
import java.util.Comparator;

import javax.annotation.concurrent.Immutable;

/**
 * Allele comparators.
 */
@Immutable
public final class AlleleComparators {
    public static final Comparator<Allele> BY_ACCESSION_ASCENDING = new ByAccession();
    public static final Comparator<Allele> BY_ACCESSION_DESCENDING = reverseOrder(BY_ACCESSION_ASCENDING);
    public static final Comparator<Allele> BY_GLSTRING_ASCENDING = new ByGlstring();
    public static final Comparator<Allele> BY_GLSTRING_DESCENDING = reverseOrder(BY_GLSTRING_ASCENDING);

    /**
     * Allele comparator by accession in ascending order.
     */
    private static class ByAccession implements Comparator<Allele>, Serializable {
        private static final long serialVersionUID = 1L;

        private int indexOf(final String accession) {
            if (accession.startsWith("HLA")) {
                return Integer.parseInt(accession.substring(3));
            }
            return Integer.parseInt(accession);
        }

        @Override
        public int compare(final Allele allele0, final Allele allele1) {
            if (allele0 == allele1) {
                return 0;
            }
            if (allele0 != null && allele1 == null) {
                return -1;
            }
            if (allele0 == null) {
                return 1;
            }
            return indexOf(allele0.getAccession()) - indexOf(allele1.getAccession());
        }
    }

    /**
     * Allele comparator by glstring in ascending order.
     */
    private static class ByGlstring implements Comparator<Allele>, Serializable {
        private static final long serialVersionUID = 1L;

        private int valueOf(final String value) {
            return Integer.parseInt(value.replaceAll("[ACLNQ]+", ""));
        }

        @Override
        public int compare(final Allele allele0, final Allele allele1) {
            if (allele0 == allele1) {
                return 0;
            }
            if (allele0 != null && allele1 == null) {
                return -1;
            }
            if (allele0 == null) {
                return 1;
            }

            // order by locus glstring first
            int locus = allele0.getLocus().getGlstring().compareTo(allele1.getLocus().getGlstring());
            if (locus != 0) {
                return locus;
            }

            // split by ':' and sort numerically within each grouping
            String[] split0 = allele0.getGlstring().substring(allele0.getGlstring().indexOf("*") + 1).split(":");
            String[] split1 = allele1.getGlstring().substring(allele1.getGlstring().indexOf("*") + 1).split(":");

            int diff = 0;
            for (int i = 0, size = Math.min(split0.length, split1.length); i < size; i++) {
                diff = valueOf(split0[i]) - valueOf(split1[i]);
                if (diff != 0) {
                    break;
                }
            }
            return diff;
        }
    }
}