/*

    gl-imgt-vcf  IMGT/HLA  VCF file generator for the gl project.
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
package org.immunogenomics.gl.imgt.vcf.processor;

import java.util.HashMap;

import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.compound.NucleotideCompound;

public class PtvContext {

	private boolean variantsOnly;
	private String reference;
	private boolean debugging;

	private int openPenalty;
	private int extPenalty;

	private String targetFileName;
	private String queryFileName;
	private String outDir;
	private String dnaMatrix;
	private HashMap<String, Boolean> reverseComplementMap;

	private int targetStartIndex;
	private int targetEndIndex;
	private boolean queryReverseComplement;
	private boolean targetReverseComplement;

	private HashMap<String, Integer> startIndexMap;
	private HashMap<String, Integer> endIndexMap;

	private SubstitutionMatrix<NucleotideCompound> SubstitutionMatrix;

	private String vcfHeader;

	private String vcfPath;
	private VcfWriter vcfWriter;

	public PtvContext() {
		super();
	}

	public boolean getVariantsOnly() {
		return variantsOnly;
	}

	public void setVariantsOnly(boolean variantsOnly) {
		this.variantsOnly = variantsOnly;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public boolean isDebugging() {
		return debugging;
	}

	public void setDebugging(boolean debugging) {
		this.debugging = debugging;
	}

	public int getOpenPenalty() {
		return openPenalty;
	}

	public void setOpenPenalty(int openPenalty) {
		this.openPenalty = openPenalty;
	}

	public int getExtPenalty() {
		return extPenalty;
	}

	public void setExtPenalty(int extPenalty) {
		this.extPenalty = extPenalty;
	}

	public String getTargetFileName() {
		return targetFileName;
	}

	public void setTargetFileName(String targetFileName) {
		this.targetFileName = targetFileName;
	}

	public String getQueryFileName() {
		return queryFileName;
	}

	public void setQueryFileName(String queryFileName) {
		this.queryFileName = queryFileName;
	}

	public String getOutDir() {
		return outDir;
	}

	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}

	public String getDnaMatrix() {
		return dnaMatrix;
	}

	public void setDnaMatrix(String dnaMatrix) {
		this.dnaMatrix = dnaMatrix;
	}

	public HashMap<String, Boolean> getReverseComplementMap() {
		return reverseComplementMap;
	}

	public void setReverseComplementMap(
			HashMap<String, Boolean> reverseComplementMap) {
		this.reverseComplementMap = reverseComplementMap;
	}

	public int getTargetStartIndex() {
		return targetStartIndex;
	}

	public void setTargetStartIndex(int targetStartIndex) {
		this.targetStartIndex = targetStartIndex;
	}

	public int getTargetEndIndex() {
		return targetEndIndex;
	}

	public void setTargetEndIndex(int targetEndIndex) {
		this.targetEndIndex = targetEndIndex;
	}

	public boolean isQueryReverseComplement() {
		return queryReverseComplement;
	}

	public void setQueryReverseComplement(boolean queryReverseComplement) {
		this.queryReverseComplement = queryReverseComplement;
	}

	public boolean isTargetReverseComplement() {
		return targetReverseComplement;
	}

	public void setTargetReverseComplement(boolean targetReverseComplement) {
		this.targetReverseComplement = targetReverseComplement;
	}

	public HashMap<String, Integer> getStartIndexMap() {
		return startIndexMap;
	}

	public void setStartIndexMap(HashMap<String, Integer> startIndexMap) {
		this.startIndexMap = startIndexMap;
	}

	public HashMap<String, Integer> getEndIndexMap() {
		return endIndexMap;
	}

	public void setEndIndexMap(HashMap<String, Integer> endIndexMap) {
		this.endIndexMap = endIndexMap;
	}

	public SubstitutionMatrix<NucleotideCompound> getSubstitutionMatrix() {
		return SubstitutionMatrix;
	}

	public void setSubstitutionMatrix(
			SubstitutionMatrix<NucleotideCompound> substitutionMatrix) {
		SubstitutionMatrix = substitutionMatrix;
	}

	public String getVcfHeader() {
		return vcfHeader;
	}

	public void setVcfHeader(String vcfHeader) {
		this.vcfHeader = vcfHeader;
	}

	public String getVcfPath() {
		return vcfPath;
	}

	public void setVcfPath(String vcfPath) {
		this.vcfPath = vcfPath;
	}

	public VcfWriter getVcfWriter() {
		return vcfWriter;
	}

	public void setVcfWriter(VcfWriter vcfWriter) {
		this.vcfWriter = vcfWriter;
	}

}
