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
 * The persistent class for the NUC_SEQUENCE database table.
 *
 */
@Entity
@Table(name="NUC_SEQUENCE")
public class NucSequence implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
//	@SequenceGenerator(name="NUC_SEQUENCE_NUCSEQUENCEIID_GENERATOR", sequenceName="SEQ_NUC_SEQUENCE")
//	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="NUC_SEQUENCE_NUCSEQUENCEIID_GENERATOR")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="NUC_SEQUENCE_IID", unique=true, nullable=false, precision=9)
	private long nucSequenceIid;

    @Temporal( TemporalType.DATE)
	@Column(name="CREATE_DTE", nullable=false)
	private Date createDte;

    @Temporal( TemporalType.DATE)
	@Column(name="LAST_UPDATE_DTE", nullable=false)
	private Date lastUpdateDte;

	@Column(name="LAST_UPDATE_USER_ID", nullable=false, length=10)
	private String lastUpdateUserId;

    @Lob()
	@Column(name="NUC_SEQUENCE")
	private String nucSequence;

	//bi-directional many-to-one association to Sequence
    @ManyToOne
	@JoinColumn(name="SEQUENCE_IID", nullable=false)
	private Sequence sequence;

    public NucSequence() {
    }

	public long getNucSequenceIid() {
		return this.nucSequenceIid;
	}

	public void setNucSequenceIid(long nucSequenceIid) {
		this.nucSequenceIid = nucSequenceIid;
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

	public String getNucSequence() {
		return this.nucSequence;
	}

	public void setNucSequence(String nucSequence) {
		this.nucSequence = nucSequence;
	}

	public Sequence getSequence() {
		return this.sequence;
	}

	public void setSequence(Sequence sequence) {
		this.sequence = sequence;
	}

}