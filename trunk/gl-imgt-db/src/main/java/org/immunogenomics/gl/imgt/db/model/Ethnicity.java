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
package org.immunogenomics.gl.imgt.db.model;

import java.io.Serializable;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the ETHNICITY database table.
 *
 */
@Entity
@Table(name="ETHNICITY")
public class Ethnicity implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="ETHNICITY_ETHNICITYIID_GENERATOR", sequenceName="SEQ_ETHNICITY")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ETHNICITY_ETHNICITYIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ETHNICITY_IID", unique=true, nullable=false, precision=9)
	private long ethnicityIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

	@Column(name="KNOWN_ETHNICITY", length=256)
	private String knownEthnicity;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to SourceMaterial
    @ManyToOne
	@JoinColumn(name="SOURCE_MATERIAL_IID", nullable=false)
	private SourceMaterial sourceMaterial;

    public Ethnicity() {
    }

	public long getEthnicityIid() {
		return this.ethnicityIid;
	}

	public void setEthnicityIid(long ethnicityIid) {
		this.ethnicityIid = ethnicityIid;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getKnownEthnicity() {
		return this.knownEthnicity;
	}

	public void setKnownEthnicity(String knownEthnicity) {
		this.knownEthnicity = knownEthnicity;
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

	public SourceMaterial getSourceMaterial() {
		return this.sourceMaterial;
	}

	public void setSourceMaterial(SourceMaterial sourceMaterial) {
		this.sourceMaterial = sourceMaterial;
	}

}