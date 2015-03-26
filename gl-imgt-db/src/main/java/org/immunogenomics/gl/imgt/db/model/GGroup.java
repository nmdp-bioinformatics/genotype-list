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
import java.util.List;

/**
 * The persistent class for the G_GROUP database table.
 *
 */
@Entity
@Table(name="G_GROUP")
public class GGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="G_GROUP_GGROUPIID_GENERATOR", sequenceName="SEQ_G_GROUP")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="G_GROUP_GGROUPIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="G_GROUP_IID", unique=true, nullable=false, precision=9)
	private long gGroupIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

	@Column(name="G_GROUP_ID", nullable=false, length=20)
	private String gGroupId;

	@Column(name="G_GROUP_NAME", nullable=false, length=20)
	private String gGroupName;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to AmbigComboElement
	@OneToMany(mappedBy="GGroup1")
	private List<AmbigComboElement> ambigComboElements1;

	//bi-directional many-to-one association to AmbigComboElement
	@OneToMany(mappedBy="GGroup2")
	private List<AmbigComboElement> ambigComboElements2;

	//bi-directional many-to-one association to Locus
    @ManyToOne
	@JoinColumn(name="LOCUS_IID", nullable=false)
	private Locus locus;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

	//bi-directional many-to-one association to GGroupAllele
	@OneToMany(mappedBy="GGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGroupAllele> GGroupAlleles;

	//bi-directional many-to-one association to GGrpRlsVerDtl
	@OneToMany(mappedBy="GGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGrpRlsVerDtl> GGrpRlsVerDtls;

    public GGroup() {
    }

    public GGroup(Locus loc, ReleaseVersion rv, String gGroupName, String gId) {

    	this.locus = loc;
    	this.releaseVersion = rv;
    	this.gGroupName = gGroupName;
    	this.gGroupId = gId;

    }

	public long getGGroupIid() {
		return this.gGroupIid;
	}

	public void setGGroupIid(long gGroupIid) {
		this.gGroupIid = gGroupIid;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getGGroupId() {
		return this.gGroupId;
	}

	public void setGGroupId(String gGroupId) {
		this.gGroupId = gGroupId;
	}

	public String getGGroupName() {
		return this.gGroupName;
	}

	public void setGGroupName(String gGroupName) {
		this.gGroupName = gGroupName;
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

	public List<AmbigComboElement> getAmbigComboElements1() {
		return this.ambigComboElements1;
	}

	public void setAmbigComboElements1(List<AmbigComboElement> ambigComboElements1) {
		this.ambigComboElements1 = ambigComboElements1;
	}

	public List<AmbigComboElement> getAmbigComboElements2() {
		return this.ambigComboElements2;
	}

	public void setAmbigComboElements2(List<AmbigComboElement> ambigComboElements2) {
		this.ambigComboElements2 = ambigComboElements2;
	}

	public Locus getLocus() {
		return this.locus;
	}

	public void setLocus(Locus locus) {
		this.locus = locus;
	}

	public ReleaseVersion getReleaseVersion() {
		return this.releaseVersion;
	}

	public void setReleaseVersion(ReleaseVersion releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

	public List<GGroupAllele> getGGroupAlleles() {
		return this.GGroupAlleles;
	}

	public void setGGroupAlleles(List<GGroupAllele> GGroupAlleles) {
		this.GGroupAlleles = GGroupAlleles;
	}

	public List<GGrpRlsVerDtl> getGGrpRlsVerDtls() {
		return this.GGrpRlsVerDtls;
	}

	public void setGGrpRlsVerDtls(List<GGrpRlsVerDtl> GGrpRlsVerDtls) {
		this.GGrpRlsVerDtls = GGrpRlsVerDtls;
	}

}