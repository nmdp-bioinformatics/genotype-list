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
 * The persistent class for the G_GRP_RLS_VER_DTL database table.
 *
 */
@Entity
@Table(name="G_GRP_RLS_VER_DTL")
public class GGrpRlsVerDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="G_GRP_RLS_VER_DTL_GGRPRLSVERDETAILIID_GENERATOR", sequenceName="SEQ_G_GRP_RLS_VER_DTL")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="G_GRP_RLS_VER_DTL_GGRPRLSVERDETAILIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="G_GRP_RLS_VER_DETAIL_IID", unique=true, nullable=false, precision=9)
	private long gGrpRlsVerDetailIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	@Column(name="RELEASE_VER_CONFIRMED_STATUS", length=20)
	private String releaseVerConfirmedStatus;

	@Column(name="RELEASE_VER_FIRST_RELEASED", length=10)
	private String releaseVerFirstReleased;

	@Column(name="RELEASE_VER_LAST_UPDATED", length=10)
	private String releaseVerLastUpdated;

	@Column(name="RELEASE_VER_STATUS", length=100)
	private String releaseVerStatus;

	//bi-directional many-to-one association to GGroup
    @ManyToOne
	@JoinColumn(name="G_GROUP_IID", nullable=false)
	private GGroup GGroup;

	//bi-directional many-to-one association to Locus
    @ManyToOne
	@JoinColumn(name="LOCUS_IID", nullable=false)
	private Locus locus;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

    public GGrpRlsVerDtl() {
    }

    public GGrpRlsVerDtl(Locus locus, ReleaseVersion rv, GGroup gGroup) {
    	this.locus = locus;
    	this.releaseVersion = rv;
    	this.GGroup = gGroup;
    }

	public long getGGrpRlsVerDetailIid() {
		return this.gGrpRlsVerDetailIid;
	}

	public void setGGrpRlsVerDetailIid(long gGrpRlsVerDetailIid) {
		this.gGrpRlsVerDetailIid = gGrpRlsVerDetailIid;
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

	public String getReleaseVerConfirmedStatus() {
		return this.releaseVerConfirmedStatus;
	}

	public void setReleaseVerConfirmedStatus(String releaseVerConfirmedStatus) {
		this.releaseVerConfirmedStatus = releaseVerConfirmedStatus;
	}

	public String getReleaseVerFirstReleased() {
		return this.releaseVerFirstReleased;
	}

	public void setReleaseVerFirstReleased(String releaseVerFirstReleased) {
		this.releaseVerFirstReleased = releaseVerFirstReleased;
	}

	public String getReleaseVerLastUpdated() {
		return this.releaseVerLastUpdated;
	}

	public void setReleaseVerLastUpdated(String releaseVerLastUpdated) {
		this.releaseVerLastUpdated = releaseVerLastUpdated;
	}

	public String getReleaseVerStatus() {
		return this.releaseVerStatus;
	}

	public void setReleaseVerStatus(String releaseVerStatus) {
		this.releaseVerStatus = releaseVerStatus;
	}

	public GGroup getGGroup() {
		return this.GGroup;
	}

	public void setGGroup(GGroup GGroup) {
		this.GGroup = GGroup;
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

}