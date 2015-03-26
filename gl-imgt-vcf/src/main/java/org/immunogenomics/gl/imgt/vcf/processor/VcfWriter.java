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

import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

public class VcfWriter {
	private static final Logger LOGGER = Logger.getLogger(VcfWriter.class);

	private FileWriter fileWriter;

	public VcfWriter(String vcfPath) throws Exception {
		if (this.fileWriter != null) {
			try {
				this.fileWriter.close();
			}
			catch (IOException ioe) {
				;
			}
			finally {
				this.fileWriter = null;
			}
		}

		try {
			this.fileWriter = new FileWriter(vcfPath);
		}
		catch (IOException ioe) {
			LOGGER.error("Error when creating VCF output file '" + vcfPath + "': " + ioe.getMessage());
			throw new Exception();
		}
	}

	public void writeHeader(String fileName) throws IOException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String vcfHeader = "##fileformat=VCFv4.1\n" +
		"##fileDate=" + formatter.format(new Date()) + "\n" +
		"##reference=file:" + fileName + "\n" +
		"##INFO=<ID=SNP,Number=0,Type=Flag,Description=\"Variant is a SNP\">\n" +
		"##INFO=<ID=INS,Number=0,Type=Flag,Description=\"Variant is an insertion\">\n" +
		"##INFO=<ID=DEL,Number=0,Type=Flag,Description=\"Variant is a deletion\">\n" +
		"#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO\n";

		this.fileWriter.write(vcfHeader);
	}

	public void writeInsertionRecord(String targetDescription, int location,
			String referenceBase, String insertion) throws IOException {
		this.fileWriter.write(targetDescription + "\t"
				+ location + "\t"
				+ "." + "\t"
				+ referenceBase + "\t"
				+ insertion + "\t."
				+ "\tPASS\tINS\n");
	}

	public void writeDeletionRecord(String targetDescription, int location,
			String queryBase, String deletion) throws IOException {
		this.fileWriter.write(targetDescription + "\t"
				+ location + "\t"
				+ "." + "\t"
				+ deletion + "\t"
				+ queryBase	+ "\t"
				+ ".\tPASS\tDEL\n");
	}

	public void writeSNPRecord(String targetDescription, int location,
			String reference, String replacement) throws IOException {
		this.fileWriter.write(targetDescription + "\t"
				+ location + "\t"
				+ "." + "\t"
				+ reference + "\t"
				+ replacement + "\t"
				+ ".\tPASS\tSNP\n");
	}

	public void close() {
		try {
			this.fileWriter.close();
		}
		catch (IOException ioe) {
			;
		}
		finally {
			this.fileWriter = null;
		}
	}

}