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
 * The persistent class for the AMBIG_COMBO_ELEMENT database table.
 *
 */
@Entity
@Table(name="AMBIG_COMBO_ELEMENT")
public class AmbigComboElement implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="AMBIG_COMBO_ELEMENT_AMBIGCOMBOELEMENTIID_GENERATOR", sequenceName="SEQ_AMBIG_COMBO_ELEMENT")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="AMBIG_COMBO_ELEMENT_AMBIGCOMBOELEMENTIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="AMBIG_COMBO_ELEMENT_IID", unique=true, nullable=false, precision=9)
	private long ambigComboElementIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

	@Column(name="G_GROUP_PART1_IND", nullable=false, length=1)
	private String gGroupPart1Ind;

	@Column(name="G_GROUP_PART2_IND", nullable=false, length=1)
	private String gGroupPart2Ind;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="ALLELE_PART1_IID")
	private Allele allele1;

	//bi-directional many-to-one association to Allele
    @ManyToOne
	@JoinColumn(name="ALLELE_PART2_IID")
	private Allele allele2;

	//bi-directional many-to-one association to AmbigComboGroup
    @ManyToOne
	@JoinColumn(name="AMBIG_COMBO_GROUP_IID")
	private AmbigComboGroup ambigComboGroup;

	//bi-directional many-to-one association to GGroup
    @ManyToOne
	@JoinColumn(name="G_GROUP_PART1_IID")
	private GGroup GGroup1;

	//bi-directional many-to-one association to GGroup
    @ManyToOne
	@JoinColumn(name="G_GROUP_PART2_IID")
	private GGroup GGroup2;

    public AmbigComboElement(AmbigComboGroup ambigComboGroup, Allele allele1, Allele allele2, GGroup gGroup1, GGroup gGroup2) {

    	this.ambigComboGroup = ambigComboGroup;
    	this.allele1 = allele1;
    	this.allele2 = allele2;
    	this.GGroup1 = gGroup1;
    	this.GGroup2 = gGroup2;

    }

    public AmbigComboElement() {
    }

	public long getAmbigComboElementIid() {
		return this.ambigComboElementIid;
	}

	public void setAmbigComboElementIid(long ambigComboElementIid) {
		this.ambigComboElementIid = ambigComboElementIid;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getGGroupPart1Ind() {
		return this.gGroupPart1Ind;
	}

	public void setGGroupPart1Ind(String gGroupPart1Ind) {
		this.gGroupPart1Ind = gGroupPart1Ind;
	}

	public String getGGroupPart2Ind() {
		return this.gGroupPart2Ind;
	}

	public void setGGroupPart2Ind(String gGroupPart2Ind) {
		this.gGroupPart2Ind = gGroupPart2Ind;
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

	public AmbigComboGroup getAmbigComboGroup() {
		return this.ambigComboGroup;
	}

	public void setAmbigComboGroup(AmbigComboGroup ambigComboGroup) {
		this.ambigComboGroup = ambigComboGroup;
	}

	public GGroup getGGroup1() {
		return this.GGroup1;
	}

	public void setGGroup1(GGroup GGroup1) {
		this.GGroup1 = GGroup1;
	}

	public GGroup getGGroup2() {
		return this.GGroup2;
	}

	public void setGGroup2(GGroup GGroup2) {
		this.GGroup2 = GGroup2;
	}

}