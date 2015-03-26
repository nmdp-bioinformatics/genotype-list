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
 * The persistent class for the ALLELE_RLS_VER_DTL database table.
 *
 */
@Entity
@Table(name="ALLELE_RLS_VER_DTL")
public class AlleleRlsVerDtl implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="ALLELE_RLS_VER_DTL_ALLELERLSVERDETAILIID_GENERATOR", sequenceName="SEQ_ALLELE_RLS_VER_DTL")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLELE_RLS_VER_DTL_ALLELERLSVERDETAILIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ALLELE_RLS_VER_DTL_IID", unique=true, nullable=false, precision=9)
	private long alleleRlsVerDetailIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	@Column(name="RELEASE_VER_CONFIRMED_STATUS", length=20)
	private String releaseVerConfirmedStatus;

	@Column(name="RELEASE_VER_FIRST_RELEASED", length=10)
	private String releaseVerFirstReleased;

	@Column(name="RELEASE_VER_LAST_UPDATED", length=10)
	private String releaseVerLastUpdated;

	@Column(name="RELEASE_VER_STATUS", length=100)
	private String releaseVerStatus;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="ALLELE_IID", nullable=false)
	private Allele allele;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne/*(fetch=FetchType.LAZY)*/
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

    public AlleleRlsVerDtl() {
    }

	public long getAlleleRlsVerDetailIid() {
		return this.alleleRlsVerDetailIid;
	}

	public void setAlleleRlsVerDetailIid(long alleleRlsVerDetailIid) {
		this.alleleRlsVerDetailIid = alleleRlsVerDetailIid;
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

	public Allele getAllele() {
		return this.allele;
	}

	public void setAllele(Allele allele) {
		this.allele = allele;
	}

	public ReleaseVersion getReleaseVersion() {
		return this.releaseVersion;
	}

	public void setReleaseVersion(ReleaseVersion releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

}