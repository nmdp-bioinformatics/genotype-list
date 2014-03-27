/*

    gl-imgt-db  IMGT/HLA database persistence domain and data access objects for the gl project.
    Copyright (c) 2012-2014 National Marrow Donor Program (NMDP)

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

import java.util.List;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AmbigGroupDto {

	List<AmbigComboElementDto> ambigComboElementsList;
	String ambigSequence;

	/**
	 * @return the ambigComboElementsList
	 */
	public List<AmbigComboElementDto> getAmbigComboElementsList() {
		return ambigComboElementsList;
	}

	/**
	 * @param ambigComboElementsList the ambigComboElementsList to set
	 */
	public void setAmbigComboElementsList(
			List<AmbigComboElementDto> ambigComboElementsList) {
		this.ambigComboElementsList = ambigComboElementsList;
	}

	/**
	 * @return the ambigSequence
	 */
	public String getAmbigSequence() {
		return ambigSequence;
	}

	/**
	 * @param ambigSequence the ambigSequence to set
	 */
	public void setAmbigSequence(String ambigSequence) {
		this.ambigSequence = ambigSequence;
	}

}
