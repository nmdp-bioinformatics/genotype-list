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
 * The persistent class for the REMOVED_ALLELE database table.
 *
 */
@Entity
@Table(name="REMOVED_ALLELE")
public class RemovedAllele implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="REMOVED_ALLELE_REMOVEDALLELEIID_GENERATOR", sequenceName="SEQ_REMOVED_ALLELE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="REMOVED_ALLELE_REMOVEDALLELEIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="REMOVED_ALLELE_IID", unique=true, nullable=false, precision=9)
	private long removedAlleleIid;

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
	@JoinColumn(name="ALLELE_IID", nullable=false)
	private Allele allele;

	//bi-directional many-to-one association to Locus
    @ManyToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name="LOCUS_IID", nullable=false)
	private Locus locus;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

    public RemovedAllele() {
    	this.createDte = new Date();
    }

    public RemovedAllele(Allele allele, Locus locus, ReleaseVersion releaseVersion) {
    	this.allele = allele;
    	this.locus = locus;
    	this.releaseVersion = releaseVersion;
    }

	public long getRemovedAlleleIid() {
		return this.removedAlleleIid;
	}

	public void setRemovedAlleleIid(long removedAlleleIid) {
		this.removedAlleleIid = removedAlleleIid;
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

	public Allele getAllele() {
		return this.allele;
	}

	public void setAllele(Allele allele) {
		this.allele = allele;
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