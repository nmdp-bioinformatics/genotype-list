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

import javax.persistence.*;

import java.util.Date;
import java.util.List;

/**
 * The persistent class for the SOURCE_MATERIAL database table.
 *
 */
@Entity
@Table(name="SOURCE_MATERIAL")
public class SourceMaterial implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="SOURCE_MATERIAL_SOURCEMATERIALIID_GENERATOR", sequenceName="SEQ_SOURCE_MATERIAL")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SOURCE_MATERIAL_SOURCEMATERIALIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SOURCE_MATERIAL_IID", unique=true, nullable=false, precision=9)
	private long sourceMaterialIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Ethnicity
	@OneToMany(mappedBy="sourceMaterial", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Ethnicity> ethnicities;

	//bi-directional many-to-one association to Sample
	@OneToMany(mappedBy="sourceMaterial", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Sample> samples;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="ALLELE_IID", nullable=false)
	private Allele allele;

	//bi-directional many-to-one association to Species
	@OneToMany(mappedBy="sourceMaterial", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Species> species;

    public SourceMaterial() {
    }

	public long getSourceMaterialIid() {
		return this.sourceMaterialIid;
	}

	public void setSourceMaterialIid(long sourceMaterialIid) {
		this.sourceMaterialIid = sourceMaterialIid;
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

	public List<Ethnicity> getEthnicities() {
		return this.ethnicities;
	}

	public void setEthnicities(List<Ethnicity> ethnicities) {
		this.ethnicities = ethnicities;
	}

	public List<Sample> getSamples() {
		return this.samples;
	}

	public void setSamples(List<Sample> samples) {
		this.samples = samples;
	}

	public Allele getAllele() {
		return this.allele;
	}

	public void setAllele(Allele allele) {
		this.allele = allele;
	}

	public List<Species> getSpecies() {
		return this.species;
	}

	public void setSpecies(List<Species> species) {
		this.species = species;
	}

}