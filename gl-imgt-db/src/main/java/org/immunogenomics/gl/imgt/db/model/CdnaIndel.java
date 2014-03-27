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
 * The persistent class for the CDNA_INDEL database table.
 *
 */
@Entity
@Table(name="CDNA_INDEL")
public class CdnaIndel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="CDNA_INDEL_CDNAINDELIID_GENERATOR", sequenceName="SEQ_CDNA_INDEL")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="CDNA_INDEL_CDNAINDELIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CDNA_INDEL_IID", unique=true, nullable=false, precision=9)
	private long cdnaIndelIid;

	@Column(name="CDNA_INDEL_END", precision=9)
	private BigInteger cdnaIndelEnd;

	@Column(name="CDNA_INDEL_EXPRESSED", length=256)
	private String cdnaIndelExpressed;

	@Column(name="CDNA_INDEL_SIZE", precision=9)
	private BigInteger cdnaIndelSize;

	@Column(name="CDNA_INDEL_START", nullable=false, precision=9)
	private BigInteger cdnaIndelStart;

	@Column(name="CDNA_INDEL_TYPE", length=256)
	private String cdnaIndelType;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Feature
    @ManyToOne
	@JoinColumn(name="FEATURE_IID", nullable=false)
	private Feature feature;

    public CdnaIndel() {
    }

	public long getCdnaIndelIid() {
		return this.cdnaIndelIid;
	}

	public void setCdnaIndelIid(long cdnaIndelIid) {
		this.cdnaIndelIid = cdnaIndelIid;
	}

	public BigInteger getCdnaIndelEnd() {
		return this.cdnaIndelEnd;
	}

	public void setCdnaIndelEnd(BigInteger cdnaIndelEnd) {
		this.cdnaIndelEnd = cdnaIndelEnd;
	}

	public String getCdnaIndelExpressed() {
		return this.cdnaIndelExpressed;
	}

	public void setCdnaIndelExpressed(String cdnaIndelExpressed) {
		this.cdnaIndelExpressed = cdnaIndelExpressed;
	}

	public BigInteger getCdnaIndelSize() {
		return this.cdnaIndelSize;
	}

	public void setCdnaIndelSize(BigInteger cdnaIndelSize) {
		this.cdnaIndelSize = cdnaIndelSize;
	}

	public BigInteger getCdnaIndelStart() {
		return this.cdnaIndelStart;
	}

	public void setCdnaIndelStart(BigInteger cdnaIndelStart) {
		this.cdnaIndelStart = cdnaIndelStart;
	}

	public String getCdnaIndelType() {
		return this.cdnaIndelType;
	}

	public void setCdnaIndelType(String cdnaIndelType) {
		this.cdnaIndelType = cdnaIndelType;
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

	public Feature getFeature() {
		return this.feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}