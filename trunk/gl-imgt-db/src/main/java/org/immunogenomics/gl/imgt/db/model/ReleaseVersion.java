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
 * The persistent class for the RELEASE_VERSION database table.
 *
 */
@Entity
@Table(name="RELEASE_VERSION")
public class ReleaseVersion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="RELEASE_VERSION_RELEASEVERSIONIID_GENERATOR", sequenceName="SEQ_RELEASE_VERSION")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="RELEASE_VERSION_RELEASEVERSIONIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="RELEASE_VERSION_IID", unique=true, nullable=false, precision=9)
	private long releaseVersionIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	@Column(name="RELEASE_VER_CURRENT_RELEASE", length=10)
	private String releaseVerCurrentRelease;

	//bi-directional many-to-one association to Allele
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Allele> alleles;

	//bi-directional many-to-one association to AlleleRlsVerDtl
	@OneToMany(mappedBy="releaseVersion", fetch=FetchType.LAZY ,cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AlleleRlsVerDtl> alleleRlsVerDtls;

	//bi-directional many-to-one association to AmbigComboGroup
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AmbigComboGroup> ambigComboGroups;

	//bi-directional many-to-one association to GGroup
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGroup> GGroups;

	//bi-directional many-to-one association to GGrpRlsVerDtl
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGrpRlsVerDtl> GGrpRlsVerDtls;

	//bi-directional many-to-one association to IncompleteAllele
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<IncompleteAllele> incompleteAlleles;

	//bi-directional many-to-one association to RemovedAllele
	@OneToMany(mappedBy="releaseVersion", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<RemovedAllele> removedAlleles;

    public ReleaseVersion() {
    }

	public long getReleaseVersionIid() {
		return this.releaseVersionIid;
	}

	public void setReleaseVersionIid(long releaseVersionIid) {
		this.releaseVersionIid = releaseVersionIid;
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

	public String getReleaseVerCurrentRelease() {
		return this.releaseVerCurrentRelease;
	}

	public void setReleaseVerCurrentRelease(String releaseVerCurrentRelease) {
		this.releaseVerCurrentRelease = releaseVerCurrentRelease;
	}

	public List<Allele> getAlleles() {
		return this.alleles;
	}

	public void setAlleles(List<Allele> alleles) {
		this.alleles = alleles;
	}

	public List<AlleleRlsVerDtl> getAlleleRlsVerDtls() {
		return this.alleleRlsVerDtls;
	}

	public void setAlleleRlsVerDtls(List<AlleleRlsVerDtl> alleleRlsVerDtls) {
		this.alleleRlsVerDtls = alleleRlsVerDtls;
	}

	public List<AmbigComboGroup> getAmbigComboGroups() {
		return this.ambigComboGroups;
	}

	public void setAmbigComboGroups(List<AmbigComboGroup> ambigComboGroups) {
		this.ambigComboGroups = ambigComboGroups;
	}

	public List<GGroup> getGGroups() {
		return this.GGroups;
	}

	public void setGGroups(List<GGroup> GGroups) {
		this.GGroups = GGroups;
	}

	public List<GGrpRlsVerDtl> getGGrpRlsVerDtls() {
		return this.GGrpRlsVerDtls;
	}

	public void setGGrpRlsVerDtls(List<GGrpRlsVerDtl> GGrpRlsVerDtls) {
		this.GGrpRlsVerDtls = GGrpRlsVerDtls;
	}

	public List<IncompleteAllele> getIncompleteAlleles() {
		return this.incompleteAlleles;
	}

	public void setIncompleteAlleles(List<IncompleteAllele> incompleteAlleles) {
		this.incompleteAlleles = incompleteAlleles;
	}

	public List<RemovedAllele> getRemovedAlleles() {
		return this.removedAlleles;
	}

	public void setRemovedAlleles(List<RemovedAllele> removedAlleles) {
		this.removedAlleles = removedAlleles;
	}

}