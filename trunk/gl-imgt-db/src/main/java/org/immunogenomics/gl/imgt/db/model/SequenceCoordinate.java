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
 * The persistent class for the SEQUENCE_COORDINATE database table.
 *
 */
@Entity
@Table(name="SEQUENCE_COORDINATE")
public class SequenceCoordinate implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="SEQUENCE_COORDINATE_SEQUENCECOORDINATEIID_GENERATOR", sequenceName="SEQ_SEQUENCE_COORDINATE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="SEQUENCE_COORDINATE_SEQUENCECOORDINATEIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="SEQUENCE_COORDINATE_IID", unique=true, nullable=false, precision=9)
	private long sequenceCoordinateIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	@Column(name="SEQUENCE_COORDINATE_END", precision=9)
	private BigInteger sequenceCoordinateEnd;

	@Column(name="SEQUENCE_COORDINATE_START", precision=9)
	private BigInteger sequenceCoordinateStart;

	//bi-directional many-to-one association to Feature
    @ManyToOne
	@JoinColumn(name="FEATURE_IID", nullable=false)
	private Feature feature;

    public SequenceCoordinate() {
    }

	public long getSequenceCoordinateIid() {
		return this.sequenceCoordinateIid;
	}

	public void setSequenceCoordinateIid(long sequenceCoordinateIid) {
		this.sequenceCoordinateIid = sequenceCoordinateIid;
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

	public BigInteger getSequenceCoordinateEnd() {
		return this.sequenceCoordinateEnd;
	}

	public void setSequenceCoordinateEnd(BigInteger sequenceCoordinateEnd) {
		this.sequenceCoordinateEnd = sequenceCoordinateEnd;
	}

	public BigInteger getSequenceCoordinateStart() {
		return this.sequenceCoordinateStart;
	}

	public void setSequenceCoordinateStart(BigInteger sequenceCoordinateStart) {
		this.sequenceCoordinateStart = sequenceCoordinateStart;
	}

	public Feature getFeature() {
		return this.feature;
	}

	public void setFeature(Feature feature) {
		this.feature = feature;
	}

}