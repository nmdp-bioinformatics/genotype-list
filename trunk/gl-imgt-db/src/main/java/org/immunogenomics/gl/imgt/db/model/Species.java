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
package org.immunogenomics.gl.imgt.db.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the SPECIES database table.
 *
 */
@Entity
@Table(name="SPECIES")
public class Species implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="SPECIES_SPECIESIID_GENERATOR", sequenceName="SEQ_SPECIES")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SPECIES_SPECIESIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SPECIES_IID", unique=true, nullable=false, precision=9)
	private long speciesIid;

	@Column(name="COMMON_NAME", length=256)
	private String commonName;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	@Column(name="LATIN_NAME", length=256)
	private String latinName;

	@Column(name="NCBI_TAXON", precision=9)
	private BigInteger ncbiTaxon;

	//bi-directional many-to-one association to SourceMaterial
    @ManyToOne
	@JoinColumn(name="SOURCE_MATERIAL_IID", nullable=false)
	private SourceMaterial sourceMaterial;

    public Species() {
    }

	public long getSpeciesIid() {
		return this.speciesIid;
	}

	public void setSpeciesIid(long speciesIid) {
		this.speciesIid = speciesIid;
	}

	public String getCommonName() {
		return this.commonName;
	}

	public void setCommonName(String commonName) {
		this.commonName = commonName;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public Date getLastUpdateDte() {
		return this.lastUpdateDte;
	}

	public void setLastUpdateDte(Date lastUpdateDte) {
		this.lastUpdateDte = lastUpdateDte;
	}

	public String getLastUpdateUserId() {
		return this.lastUpdateUserId;
	}

	public void setLastUpdateUserId(String lastUpdateUserId) {
		this.lastUpdateUserId = lastUpdateUserId;
	}

	public String getLatinName() {
		return this.latinName;
	}

	public void setLatinName(String latinName) {
		this.latinName = latinName;
	}

	public BigInteger getNcbiTaxon() {
		return this.ncbiTaxon;
	}

	public void setNcbiTaxon(BigInteger ncbiTaxon) {
		this.ncbiTaxon = ncbiTaxon;
	}

	public SourceMaterial getSourceMaterial() {
		return this.sourceMaterial;
	}

	public void setSourceMaterial(SourceMaterial sourceMaterial) {
		this.sourceMaterial = sourceMaterial;
	}

}