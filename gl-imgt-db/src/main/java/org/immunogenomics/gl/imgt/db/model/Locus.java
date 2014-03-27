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
import java.util.List;

/**
 * The persistent class for the LOCUS database table.
 *
 */
@Entity
@Table(name="LOCUS")
public class Locus implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="LOCUS_LOCUSIID_GENERATOR", sequenceName="SEQ_LOCUS")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="LOCUS_LOCUSIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="LOCUS_IID", unique=true, nullable=false, precision=9)
	private long locusIid;

	@Column(name="\"CLASS\"", length=8)
	private String class_;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

	@Column(name="GENE_SYSTEM", nullable=false, length=10)
	private String geneSystem;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	@Column(name="LOCUS_NAME", nullable=false, length=10)
	private String locusName;

	//bi-directional many-to-one association to Allele
	@OneToMany(mappedBy="locus", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Allele> alleles;

	//bi-directional many-to-one association to GGroup
	@OneToMany(mappedBy="locus", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGroup> GGroups;

	//bi-directional many-to-one association to GGrpRlsVerDtl
	@OneToMany(mappedBy="locus", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGrpRlsVerDtl> GGrpRlsVerDtls;

	//bi-directional many-to-one association to RemovedAllele
	@OneToMany(mappedBy="locus", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<RemovedAllele> removedAlleles;

    public Locus() {
    }

	public long getLocusIid() {
		return this.locusIid;
	}

	public void setLocusIid(long locusIid) {
		this.locusIid = locusIid;
	}

	public String getClass_() {
		return this.class_;
	}

	public void setClass_(String class_) {
		this.class_ = class_;
	}

	public Date getCreateDte() {
		return this.createDte;
	}

	public void setCreateDte(Date createDte) {
		this.createDte = createDte;
	}

	public String getGeneSystem() {
		return this.geneSystem;
	}

	public void setGeneSystem(String geneSystem) {
		this.geneSystem = geneSystem;
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

	public String getLocusName() {
		return this.locusName;
	}

	public void setLocusName(String locusName) {
		this.locusName = locusName;
	}

	public List<Allele> getAlleles() {
		return this.alleles;
	}

	public void setAlleles(List<Allele> alleles) {
		this.alleles = alleles;
	}

	public List<GGroup> getGGroups() {
		return this.GGroups;
	}

	public void setGGroups(List<GGroup> GGroups) {
		this.GGroups = GGroups;
	}

	public List<GGrpRlsVerDtl> getGGrpRlsVerDtls() {
		return this.GGrpRlsVerDtls;
	}

	public void setGGrpRlsVerDtls(List<GGrpRlsVerDtl> GGrpRlsVerDtls) {
		this.GGrpRlsVerDtls = GGrpRlsVerDtls;
	}

	public List<RemovedAllele> getRemovedAlleles() {
		return this.removedAlleles;
	}

	public void setRemovedAlleles(List<RemovedAllele> removedAlleles) {
		this.removedAlleles = removedAlleles;
	}

}