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

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class IndelDto {

	private String alleleId;

	private String alleleName;

	private String cdnaIndelType;

	private Integer cdnaIndelStart;

	private Integer cdnaIndelEnd;

	private Integer cdnaIndelSize;

	private String nucSequence;

	public IndelDto(){

	}

	public IndelDto(String alleleId, String alleleName, String cdnaIndelType,
			Integer cdnaIndelStart, Integer cdnaIndelEnd, Integer cdnaIndelSize, String nucSequence){

		this.alleleId = alleleId;
		this.alleleName = alleleName;
		this.cdnaIndelType = cdnaIndelType;
		this.cdnaIndelStart = cdnaIndelStart;
		this.cdnaIndelEnd = cdnaIndelEnd;
		this.cdnaIndelSize = cdnaIndelSize;
		this.nucSequence = nucSequence;

	}

	/**
	 * @return the alleleId
	 */
	public String getAlleleId() {
		return alleleId;
	}

	/**
	 * @param alleleId the alleleId to set
	 */
	public void setAlleleId(String alleleId) {
		this.alleleId = alleleId;
	}

	/**
	 * @return the alleleName
	 */
	public String getAlleleName() {
		return alleleName;
	}

	/**
	 * @param alleleName the alleleName to set
	 */
	public void setAlleleName(String alleleName) {
		this.alleleName = alleleName;
	}

	/**
	 * @return the cdnaIndelStart
	 */
	public Integer getCdnaIndelStart() {
		return cdnaIndelStart;
	}

	/**
	 * @param cdnaIndelStart the cdnaIndelStart to set
	 */
	public void setCdnaIndelStart(Integer cdnaIndelStart) {
		this.cdnaIndelStart = cdnaIndelStart;
	}

	/**
	 * @return the cdnaIndelEnd
	 */
	public Integer getCdnaIndelEnd() {
		return cdnaIndelEnd;
	}

	/**
	 * @param cdnaIndelEnd the cdnaIndelEnd to set
	 */
	public void setCdnaIndelEnd(Integer cdnaIndelEnd) {
		this.cdnaIndelEnd = cdnaIndelEnd;
	}

	/**
	 * @return the cdnaIndelSize
	 */
	public Integer getCdnaIndelSize() {
		return cdnaIndelSize;
	}

	/**
	 * @param cdnaIndelSize the cdnaIndelSize to set
	 */
	public void setCdnaIndelSize(Integer cdnaIndelSize) {
		this.cdnaIndelSize = cdnaIndelSize;
	}

	/**
	 * @return the nucSequence
	 */
	public String getNucSequence() {
		return nucSequence;
	}

	/**
	 * @param nucSequence the nucSequence to set
	 */
	public void setNucSequence(String nucSequence) {
		this.nucSequence = nucSequence;
	}

	/**
	 * @return the cdnaIndelType
	 */
	public String getCdnaIndelType() {
		return cdnaIndelType;
	}

	/**
	 * @param cdnaIndelType the cdnaIndelType to set
	 */
	public void setCdnaIndelType(String cdnaIndelType) {
		this.cdnaIndelType = cdnaIndelType;
	}

}
