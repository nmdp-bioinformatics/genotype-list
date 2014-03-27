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
 * The persistent class for the FEATURE_TRANSLATION database table.
 *
 */
@Entity
@Table(name="FEATURE_TRANSLATION")
public class FeatureTranslation implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="FEATURE_TRANSLATION_FEATURETRANSLATIONIID_GENERATOR", sequenceName="SEQ_FEATURE_TRANSLATION")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="FEATURE_TRANSLATION_FEATURETRANSLATIONIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="FEATURE_TRANSLATION_IID", unique=true, nullable=false, precision=9)
	private long featureTranslationIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Lob()
	@Column(name="FEATURE_TRANSLATION", nullable=false)
	private String featureTranslation;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Feature
    @ManyToOne
	@JoinColumn(name="FEATURE_IID", nullable=false)
	private Feature feature;

    public FeatureTranslation() {
    }

	public long getFeatureTranslationIid() {
		return this.featureTranslationIid;
	}

	public void setFeatureTranslationIid(long featureTranslationIid) {
		this.featureTranslationIid = featureTranslationIid;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getFeatureTranslation() {
		return this.featureTranslation;
	}

	public void setFeatureTranslation(String featureTranslation) {
		this.featureTranslation = featureTranslation;
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