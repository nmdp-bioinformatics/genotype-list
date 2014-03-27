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
import java.util.List;

/**
 * The persistent class for the FEATURE database table.
 *
 */
@Entity
@Table(name="FEATURE")
public class Feature implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="FEATURE_FEATUREIID_GENERATOR", sequenceName="SEQ_FEATURE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FEATURE_FEATUREIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FEATURE_IID", unique=true, nullable=false, precision=9)
	private long featureIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

	@Column(name="FEATURE_NAME", length=20)
	private String featureName;

	@Column(name="FEATURE_ORDER", precision=9)
	// Changed from Integer to Long jklug 03/19/2014
	// Changed from Long to BigInteger jklug 03/21/2014
	private BigInteger featureOrder;

	@Column(name="FEATURE_STATUS", length=20)
	private String featureStatus;

	@Column(name="FEATURE_TYPE", length=20)
	private String featureType;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to CdnaCoordinate
	@OneToMany(mappedBy="feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<CdnaCoordinate> cdnaCoordinates;

	//bi-directional many-to-one association to CdnaIndel
	@OneToMany(mappedBy="feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<CdnaIndel> cdnaIndels;

	//bi-directional many-to-one association to Sequence
    @ManyToOne
	@JoinColumn(name="SEQUENCE_IID")
	private Sequence sequence;

	//bi-directional many-to-one association to FeatureTranslation
	@OneToMany(mappedBy="feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<FeatureTranslation> featureTranslations;

	//bi-directional many-to-one association to SequenceCoordinate
	@OneToMany(mappedBy="feature", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<SequenceCoordinate> sequenceCoordinates;

    public Feature() {
    }

	public long getFeatureIid() {
		return this.featureIid;
	}

	public void setFeatureIid(long featureIid) {
		this.featureIid = featureIid;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getFeatureName() {
		return this.featureName;
	}

	public void setFeatureName(String featureName) {
		this.featureName = featureName;
	}

	public BigInteger getFeatureOrder() {
		return this.featureOrder;
	}

	public void setFeatureOrder(BigInteger featureOrder) {
		this.featureOrder = featureOrder;
	}

	public String getFeatureStatus() {
		return this.featureStatus;
	}

	public void setFeatureStatus(String featureStatus) {
		this.featureStatus = featureStatus;
	}

	public String getFeatureType() {
		return this.featureType;
	}

	public void setFeatureType(String featureType) {
		this.featureType = featureType;
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

	public List<CdnaCoordinate> getCdnaCoordinates() {
		return this.cdnaCoordinates;
	}

	public void setCdnaCoordinates(List<CdnaCoordinate> cdnaCoordinates) {
		this.cdnaCoordinates = cdnaCoordinates;
	}

	public List<CdnaIndel> getCdnaIndels() {
		return this.cdnaIndels;
	}

	public void setCdnaIndels(List<CdnaIndel> cdnaIndels) {
		this.cdnaIndels = cdnaIndels;
	}

	public Sequence getSequence() {
		return this.sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

	public List<FeatureTranslation> getFeatureTranslations() {
		return this.featureTranslations;
	}

	public void setFeatureTranslations(List<FeatureTranslation> featureTranslations) {
		this.featureTranslations = featureTranslations;
	}

	public List<SequenceCoordinate> getSequenceCoordinates() {
		return this.sequenceCoordinates;
	}

	public void setSequenceCoordinates(List<SequenceCoordinate> sequenceCoordinates) {
		this.sequenceCoordinates = sequenceCoordinates;
	}

}