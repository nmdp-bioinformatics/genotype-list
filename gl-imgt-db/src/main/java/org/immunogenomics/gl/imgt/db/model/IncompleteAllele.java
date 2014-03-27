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

/**
 * The persistent class for the INCOMPLETE_ALLELE database table.
 *
 */
@Entity
@Table(name="INCOMPLETE_ALLELE")
public class IncompleteAllele implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="INCOMPLETE_ALLELE_INCOMPLETEALLELEIID_GENERATOR", sequenceName="SEQ_INCOMPLETE_ALLELE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="INCOMPLETE_ALLELE_INCOMPLETEALLELEIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="INCOMPLETE_ALLELE_IID", unique=true, nullable=false, precision=9)
	private long incompleteAlleleIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="EXTENSION_ALLELE_IID", nullable=false)
	private Allele allele1;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="PARTIAL_ALLELE_IID", nullable=false)
	private Allele allele2;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

    public IncompleteAllele() {
    }

    public IncompleteAllele(Allele partialAllele, Allele extensionAllele, ReleaseVersion rv) {

    	this.allele1 = extensionAllele;
    	this.allele2 = partialAllele;
    	this.releaseVersion = rv;

    }

	public long getIncompleteAlleleIid() {
		return this.incompleteAlleleIid;
	}

	public void setIncompleteAlleleIid(long incompleteAlleleIid) {
		this.incompleteAlleleIid = incompleteAlleleIid;
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

	public Allele getAllele1() {
		return this.allele1;
	}

	public void setAllele1(Allele allele1) {
		this.allele1 = allele1;
	}

	public Allele getAllele2() {
		return this.allele2;
	}

	public void setAllele2(Allele allele2) {
		this.allele2 = allele2;
	}

	public ReleaseVersion getReleaseVersion() {
		return this.releaseVersion;
	}

	public void setReleaseVersion(ReleaseVersion releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

}