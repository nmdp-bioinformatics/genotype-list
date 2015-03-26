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
 * The persistent class for the AMBIG_COMBO_GROUP database table.
 *
 */
@Entity
@Table(name="AMBIG_COMBO_GROUP")
public class AmbigComboGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="AMBIG_COMBO_GROUP_AMBIGCOMBOGROUPIID_GENERATOR", sequenceName="SEQ_AMBIG_COMBO_GROUP")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AMBIG_COMBO_GROUP_AMBIGCOMBOGROUPIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AMBIG_COMBO_GROUP_IID", unique=true, nullable=false, precision=9)
	private long ambigComboGroupIid;

    @Lob()
	@Column(name="AMBIG_SEQUENCE_CLOB")
	private String ambigSequenceClob;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to AmbigComboElement
	@OneToMany(mappedBy="ambigComboGroup", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AmbigComboElement> ambigComboElements;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne
	@JoinColumn(name="RELEASE_VERSION_IID")
	private ReleaseVersion releaseVersion;

    public AmbigComboGroup() {
    }

    public AmbigComboGroup(String ambigSequenceClob, ReleaseVersion rv) {

    	this.ambigSequenceClob = ambigSequenceClob;
    	this.releaseVersion = rv;

    }

	public long getAmbigComboGroupIid() {
		return this.ambigComboGroupIid;
	}

	public void setAmbigComboGroupIid(long ambigComboGroupIid) {
		this.ambigComboGroupIid = ambigComboGroupIid;
	}

	public String getAmbigSequenceClob() {
		return this.ambigSequenceClob;
	}

	public void setAmbigSequenceClob(String ambigSequenceClob) {
		this.ambigSequenceClob = ambigSequenceClob;
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

	public List<AmbigComboElement> getAmbigComboElements() {
		return this.ambigComboElements;
	}

	public void setAmbigComboElements(List<AmbigComboElement> ambigComboElements) {
		this.ambigComboElements = ambigComboElements;
	}

	public ReleaseVersion getReleaseVersion() {
		return this.releaseVersion;
	}

	public void setReleaseVersion(ReleaseVersion releaseVersion) {
		this.releaseVersion = releaseVersion;
	}

}