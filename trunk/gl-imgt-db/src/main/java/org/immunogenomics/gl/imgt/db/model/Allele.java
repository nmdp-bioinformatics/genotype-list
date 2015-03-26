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
 * The persistent class for the ALLELE database table.
 *
 */
//
//" indel.cdnaIndelStart, indel.cdnaIndelEnd, indel.cdnaIndelSize, nucSeq.nucSequence"
@NamedQueries({
    @NamedQuery(name="getIndels", query="select new org.immunogenomics.gl.imgt.db.dto.IndelDto(al.alleleId, al.alleleName, indel.cdnaIndelType," +
    		" indel.cdnaIndelStart, indel.cdnaIndelEnd, indel.cdnaIndelSize, nucSeq.nucSequence)" +
    		" from Allele al  join al.sequences s" +
    		"  join s.features f" +
    		"  join f.cdnaIndels indel" +
    		"  join s.nucSequences nucSeq" +
    		" order by al.alleleName")


})
@Entity
@Table(name="ALLELE")
public class Allele implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="ALLELE_ALLELEIID_GENERATOR", sequenceName="SEQ_ALLELE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="ALLELE_ALLELEIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ALLELE_IID", unique=true, nullable=false, precision=9)
	private long alleleIid;

    @Temporal( TemporalType.DATE)
	@Column(name="ALLELE_DATE_ASSIGNED")
	private Date alleleDateAssigned;

	@Column(name="ALLELE_ID", nullable=false, length=8)
	private String alleleId;

	@Column(name="ALLELE_NAME", nullable=false, length=50)
	private String alleleName;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", length=10)
	private String lastUpdateUserId;

	//bi-directional many-to-one association to Locus
    @ManyToOne (cascade = {CascadeType.MERGE})
	@JoinColumn(name="LOCUS_IID", nullable=false)
	private Locus locus;

	//bi-directional many-to-one association to ReleaseVersion
    @ManyToOne (cascade = {CascadeType.MERGE})
	@JoinColumn(name="RELEASE_VERSION_IID", nullable=false)
	private ReleaseVersion releaseVersion;

	//bi-directional many-to-one association to AlleleRlsVerDtl
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AlleleRlsVerDtl> alleleRlsVerDtls;

	//bi-directional many-to-one association to AmbigComboElement
	@OneToMany(mappedBy="allele1", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AmbigComboElement> ambigComboElements1;

	//bi-directional many-to-one association to AmbigComboElement
	@OneToMany(mappedBy="allele2", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<AmbigComboElement> ambigComboElements2;

	//bi-directional many-to-one association to Citation
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Citation> citations;

	//bi-directional many-to-one association to GGroupAllele
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<GGroupAllele> GGroupAlleles;

	//bi-directional many-to-one association to IncompleteAllele
	@OneToMany(mappedBy="allele1", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<IncompleteAllele> incompleteAlleles1;

	//bi-directional many-to-one association to IncompleteAllele
	@OneToMany(mappedBy="allele2", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<IncompleteAllele> incompleteAlleles2;

	//bi-directional many-to-one association to RemovedAllele
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<RemovedAllele> removedAlleles;

	//bi-directional many-to-one association to Sequence
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<Sequence> sequences;

	//bi-directional many-to-one association to SourceMaterial
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<SourceMaterial> sourceMaterials;

	//bi-directional many-to-one association to SourceXref
	@OneToMany(mappedBy="allele", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH})
	private List<SourceXref> sourceXrefs;

    public Allele() {
    }

	public long getAlleleIid() {
		return this.alleleIid;
	}

	public void setAlleleIid(long alleleIid) {
		this.alleleIid = alleleIid;
	}

	public Date getAlleleDateAssigned() {
		return this.alleleDateAssigned;
	}

	public void setAlleleDateAssigned(Date alleleDateAssigned) {
		this.alleleDateAssigned = alleleDateAssigned;
	}

	public String getAlleleId() {
		return this.alleleId;
	}

	public void setAlleleId(String alleleId) {
		this.alleleId = alleleId;
	}

	public String getAlleleName() {
		return this.alleleName;
	}

	public void setAlleleName(String alleleName) {
		this.alleleName = alleleName;
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

	public List<AlleleRlsVerDtl> getAlleleRlsVerDtls() {
		return this.alleleRlsVerDtls;
	}

	public void setAlleleRlsVerDtls(List<AlleleRlsVerDtl> alleleRlsVerDtls) {
		this.alleleRlsVerDtls = alleleRlsVerDtls;
	}

	public List<AmbigComboElement> getAmbigComboElements1() {
		return this.ambigComboElements1;
	}

	public void setAmbigComboElements1(List<AmbigComboElement> ambigComboElements1) {
		this.ambigComboElements1 = ambigComboElements1;
	}

	public List<AmbigComboElement> getAmbigComboElements2() {
		return this.ambigComboElements2;
	}

	public void setAmbigComboElements2(List<AmbigComboElement> ambigComboElements2) {
		this.ambigComboElements2 = ambigComboElements2;
	}

	public List<Citation> getCitations() {
		return this.citations;
	}

	public void setCitations(List<Citation> citations) {
		this.citations = citations;
	}

	public List<GGroupAllele> getGGroupAlleles() {
		return this.GGroupAlleles;
	}

	public void setGGroupAlleles(List<GGroupAllele> GGroupAlleles) {
		this.GGroupAlleles = GGroupAlleles;
	}

	public List<IncompleteAllele> getIncompleteAlleles1() {
		return this.incompleteAlleles1;
	}

	public void setIncompleteAlleles1(List<IncompleteAllele> incompleteAlleles1) {
		this.incompleteAlleles1 = incompleteAlleles1;
	}

	public List<IncompleteAllele> getIncompleteAlleles2() {
		return this.incompleteAlleles2;
	}

	public void setIncompleteAlleles2(List<IncompleteAllele> incompleteAlleles2) {
		this.incompleteAlleles2 = incompleteAlleles2;
	}

	public List<RemovedAllele> getRemovedAlleles() {
		return this.removedAlleles;
	}

	public void setRemovedAlleles(List<RemovedAllele> removedAlleles) {
		this.removedAlleles = removedAlleles;
	}

	public List<Sequence> getSequences() {
		return this.sequences;
	}

	public void setSequences(List<Sequence> sequences) {
		this.sequences = sequences;
	}

	public List<SourceMaterial> getSourceMaterials() {
		return this.sourceMaterials;
	}

	public void setSourceMaterials(List<SourceMaterial> sourceMaterials) {
		this.sourceMaterials = sourceMaterials;
	}

	public List<SourceXref> getSourceXrefs() {
		return this.sourceXrefs;
	}

	public void setSourceXrefs(List<SourceXref> sourceXrefs) {
		this.sourceXrefs = sourceXrefs;
	}

}