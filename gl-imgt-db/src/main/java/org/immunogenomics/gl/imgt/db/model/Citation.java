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
import java.math.BigInteger;

import javax.persistence.*;

import java.util.Date;

/**
 * The persistent class for the CITATION database table.
 *
 */
@Entity
@Table(name="CITATION")
public class Citation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="CITATION_CITATIONIID_GENERATOR", sequenceName="SEQ_CITATION")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CITATION_CITATIONIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CITATION_IID", unique=true, nullable=false, precision=9)
	private long citationIid;

	private String authors;

	@Column(name="CITATION_LOCATION")
	private String citationLocation;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	@Column(name="PUB_MED", precision=9)
	private BigInteger pubMed;

	private String title;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="ALLELE_IID", nullable=false)
	private Allele allele;

    public Citation() {
    }

	public long getCitationIid() {
		return this.citationIid;
	}

	public void setCitationIid(long citationIid) {
		this.citationIid = citationIid;
	}

	public String getAuthors() {
		return this.authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public String getCitationLocation() {
		return this.citationLocation;
	}

	public void setCitationLocation(String citationLocation) {
		this.citationLocation = citationLocation;
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

	public BigInteger getPubMed() {
		return this.pubMed;
	}

	public void setPubMed(BigInteger pubMed) {
		this.pubMed = pubMed;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Allele getAllele() {
		return this.allele;
	}

	public void setAllele(Allele allele) {
		this.allele = allele;
	}

}