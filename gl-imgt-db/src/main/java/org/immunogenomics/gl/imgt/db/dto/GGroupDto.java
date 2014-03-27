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

import org.immunogenomics.gl.imgt.db.model.Allele;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class GGroupDto {

	String gGroupName;
	List<Allele> gGroup;
	String gid;

	/**
	 * @return the gGroupName
	 */
	public String getgGroupName() {
		return gGroupName;
	}
	/**
	 * @param gGroupName the gGroupName to set
	 */
	public void setgGroupName(String gGroupName) {
		this.gGroupName = gGroupName;
	}
	/**
	 * @return the gGroup
	 */
	public List<Allele> getgGroup() {
		return gGroup;
	}
	/**
	 * @param gGroup the gGroup to set
	 */
	public void setgGroup(List<Allele> gGroup) {
		this.gGroup = gGroup;
	}
	/**
	 * @return the gid
	 */
	public String getGid() {
		return gid;
	}
	/**
	 * @param gid the gid to set
	 */
	public void setGid(String gid) {
		this.gid = gid;
	}

} // end class
