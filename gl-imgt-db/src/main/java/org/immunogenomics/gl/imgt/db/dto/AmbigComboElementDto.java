/*

    gl-imgt-db  IMGT/HLA database persistence domain and data access objects for the gl project.
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
package org.immunogenomics.gl.imgt.db.dto;

import org.immunogenomics.gl.imgt.db.model.Allele;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AmbigComboElementDto {

	private Allele ambigAllele1;
	private Allele ambigAllele2;

	/**
	 * @return the ambigAllele1
	 */
	public Allele getAmbigAllele1() {
		return ambigAllele1;
	}
	/**
	 * @param ambigAllele1 the ambigAllele1 to set
	 */
	public void setAmbigAllele1(Allele ambigAllele1) {
		this.ambigAllele1 = ambigAllele1;
	}
	/**
	 * @return the ambigAllele2
	 */
	public Allele getAmbigAllele2() {
		return ambigAllele2;
	}
	/**
	 * @param ambigAllele2 the ambigAllele2 to set
	 */
	public void setAmbigAllele2(Allele ambigAllele2) {
		this.ambigAllele2 = ambigAllele2;
	}

} // end class
