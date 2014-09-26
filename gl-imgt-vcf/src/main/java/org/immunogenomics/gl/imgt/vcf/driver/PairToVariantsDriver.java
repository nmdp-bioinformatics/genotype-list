/*

    gl-imgt-vcf  IMGT/HLA  VCF file generator for the gl project.
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
package org.immunogenomics.gl.imgt.vcf.driver;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;
import org.immunogenomics.gl.imgt.vcf.processor.PtvContext;
import org.immunogenomics.gl.imgt.vcf.processor.PtvProcessor;
import org.immunogenomics.gl.imgt.vcf.processor.ReferenceSequence;

public class PairToVariantsDriver {

	private static final Logger LOGGER = Logger.getLogger(PairToVariantsDriver.class);

	public static void main(String[] args) {

		PtvProcessor process = new PtvProcessor();
		PtvContext context = null;

		try {
			context = process.initialize(args);
		}
		catch (Exception e) {
			System.exit(1);
		}

		try {
			process.loadSubstitutionMatrix(context);
		}
		catch (FileNotFoundException fnfe) {
			LOGGER.info("Source file for substitution matrix not found: '" + context.getDnaMatrix() + "'");
			System.exit(1);
		}

		// Create the reference sequence
		ReferenceSequence referenceSequence =
				process.createTargetSequence(context);

		String targetDescription = referenceSequence.getReferenceDescription();
		DNASequence targetSequence = referenceSequence.getReferenceSequence();

		// Reverse complement
		if (context.isTargetReverseComplement()) {
			targetSequence = new DNASequence(targetSequence.getReverseComplement().getSequenceAsString(),
					new AmbiguityDNACompoundSet());
		}

		// Create the query sequences
		LinkedHashMap<String, DNASequence> querySequences =
				process.createQuerySequences(context);

		try {
			for (Entry<String, DNASequence> sequence : querySequences.entrySet()) {
				String queryDescription = sequence.getKey();
				DNASequence querySequence = sequence.getValue();

				// Open VCF file
				process.createVcfFileWriter(context, queryDescription, querySequence);

				// Reverse complement
				if (context.isQueryReverseComplement()) {
					querySequence = new DNASequence(querySequence.getReverseComplement().getSequenceAsString(),
							new AmbiguityDNACompoundSet());
				}

				// Write header to VCF file
				process.writeHeader(context);

				// generate the alignment
				SequencePair<DNASequence, NucleotideCompound> sequencePair =
						Alignments.getPairwiseAlignment(querySequence, targetSequence, PairwiseSequenceAlignerType.GLOBAL,
								new SimpleGapPenalty((short)context.getOpenPenalty(), (short)context.getExtPenalty()),
								context.getSubstitutionMatrix());

				// Process alignment and write record to VCF file
				process.processAlignment(context, sequencePair, targetDescription);

				// Close VCF File
				process.destroyVcfFileWriter(context);

				// Run bgzip on VCF file to create a compressed version,
				// Run tabix to create index on result
				process.processVCF(context);
			} // each sequence in the fasta file
		}
		catch (Exception e) {
			LOGGER.error("Pair to variants process failed.");
		}
		finally {
			process.destroyVcfFileWriter(context);
		}
	}

	// Notes:
	//
	// Simplify open, write, close and post-process steps, eliminate reference to vcfWriter, individual try/catch blocks
	// Make Method calls more straightforward, limit action of each method to one process step
	// Make each process step a clear method call from the for loop
}
