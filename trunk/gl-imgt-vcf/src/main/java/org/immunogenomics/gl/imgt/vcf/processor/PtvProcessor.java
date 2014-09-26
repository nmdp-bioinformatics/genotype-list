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
package org.immunogenomics.gl.imgt.vcf.processor;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.log4j.Logger;
import org.biojava3.alignment.SimpleSubstitutionMatrix;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;
import org.biojava3.core.sequence.io.FastaReader;
import org.biojava3.core.sequence.io.FastaSequenceParser;
import org.biojava3.core.sequence.io.FileProxyDNASequenceCreator;
import org.biojava3.core.sequence.io.GenericFastaHeaderParser;
import org.biojava3.core.sequence.template.SequenceView;

public class PtvProcessor {

	private static final Logger LOGGER = Logger.getLogger(PtvProcessor.class);

	private Options defineCommandLineOptions() {
		Options options = new Options();
		// target query path
		Option help = new Option("help", "print this message");

		Option variantsOnly = new Option("variantsOnly", "variants only"); // false
		Option debug = new Option("debug", "debug mode"); // true
		Option queryReverseComplement = new Option("queryReverseComplement", "query reverse complement"); // false
		Option targetReverseComplement = new Option("targetReverseComplement", "target reverse complement"); // false

		OptionBuilder.withArgName("referenceValue"); // 'gene' or 'hg38'
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("reference type, either 'gene' or 'hg38', defaults to hg38");
		Option reference = OptionBuilder.create("reference");

		// EMBOSS's NW uses an opening penalty of 10 and 0.5 for extension.
		// http://www.ebi.ac.uk/Tools/psa/emboss_needle/nucleotide.html
		OptionBuilder.withArgName("openPenaltyValue"); // 10
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("gap open penalty");
		Option openPenalty = OptionBuilder.create("openPenalty");

		OptionBuilder.withArgName("extensionPenaltyValue"); // 1
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("gap extension penalty");
		Option extensionPenalty = OptionBuilder.create("extensionPenalty");

		OptionBuilder.withArgName("matrixPathValue"); // 'org/immunogenomics/gl/imgt/vcf/NUC.4.4.txt'
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("matrix path, defaults to './NUC.4.4.txt'");
		Option matrixPath = OptionBuilder.create("matrixPath");

		OptionBuilder.withArgName("targetStartIndexValue"); // 0
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("target start index");
		Option targetStartIndex = OptionBuilder.create("targetStartIndex");

		OptionBuilder.withArgName("targetEndIndexValue"); // 0
		OptionBuilder.hasArg();
		OptionBuilder.withDescription("target end index");
		Option targetEndIndex = OptionBuilder.create("targetEndIndex");

		options.addOption(help);
		options.addOption(variantsOnly);
		options.addOption(debug);
		options.addOption(queryReverseComplement);
		options.addOption(targetReverseComplement);
		options.addOption(reference);
		options.addOption(openPenalty);
		options.addOption(extensionPenalty);
		options.addOption(matrixPath);
		options.addOption(targetStartIndex);
		options.addOption(targetEndIndex);

		return options;
	}

	private CommandLine parseCommandLineArgs(Options options, String[] args)
			throws Exception {
		// create the parser
		CommandLineParser parser = new BasicParser();
	    CommandLine line = null;

	    try {
	    	// parse the command line arguments
	    	line = parser.parse(options, args);
	    }
	    catch (ParseException exp) {
	    	LOGGER.error("Parsing failed. Reason: " + exp.getMessage());
	    	throw new Exception(exp);
	    }

	    return line;
    }

	public PtvContext initialize(String[] args) throws Exception {

		PtvContext context = new PtvContext();

		Options options = defineCommandLineOptions();
		CommandLine line = parseCommandLineArgs(options, args);

		if (line.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp("pairToVariant [options] target query [output path]", options);
		}

		if (line.hasOption("variantsOnly")) {
			context.setVariantsOnly(true);
		}
		else {
			context.setVariantsOnly(false);
		}

		if (line.hasOption("reference")) {
			context.setReference(line.getOptionValue("reference"));
		}
		else {
			context.setReference("hg38");
		}

		if (line.hasOption("debug")) {
			context.setDebugging(true);
		}
		else {
			context.setDebugging(false);
		}

		if (line.hasOption("openPenalty")) {
			context.setOpenPenalty(Integer.parseInt(line.getOptionValue("openPenalty")));
		}
		else {
			context.setOpenPenalty(10);
		}

		if (line.hasOption("extensionPenalty")) {
			context.setExtPenalty(Integer.parseInt(line.getOptionValue("extensionPenalty")));
		}
		else {
			context.setExtPenalty(1);
		}

		if (line.hasOption("matrixPath")) {
			context.setDnaMatrix(line.getOptionValue("matrixPath"));
		}
		else {
			context.setDnaMatrix("./NUC.4.4.txt");
		}

		// per-reference stuff
		// 1-based index
		if ("gene".equalsIgnoreCase(context.getReference())) {
			// if targetStartIndex, targetEndIndex, queryReverseComplement or targetReverseComplement
			// specified, error
			if (line.hasOption("targetStartIndex") ||
					line.hasOption("targetEndIndex") ||
					line.hasOption("queryReverseComplement") ||
					line.hasOption("targetReverseComplement")) {
				LOGGER.error("When refrence is " + context.getReference() +
						", target start index, target end index and reverse complement options cannot be specified");
				throw new Exception();
			}

			context.setTargetStartIndex(1); // don't set targetEndIndex with gene-specific reference
			context.setTargetEndIndex(0);
			context.setQueryReverseComplement(false);
			context.setTargetReverseComplement(false);
		}
		else if ("hg38".equals(context.getReference())) { // GRCh38/hg38
			if (!line.hasOption("targetStartIndex") || !line.hasOption("targetEndIndex")) {
				LOGGER.error("When refrence is " + context.getReference() +
						", target start index and target end index must be specified");
				throw new Exception();
			}
			context.setTargetStartIndex(Integer.parseInt(line.getOptionValue("targetStartIndex")));
			context.setTargetEndIndex(Integer.parseInt(line.getOptionValue("targetEndIndex")));

			if (line.hasOption("queryReverseComplement")) {
				context.setQueryReverseComplement(true);
			}
			else {
				context.setQueryReverseComplement(false);
			}

			if (line.hasOption("targetReverseComplement")) {
				context.setTargetReverseComplement(true);
			}
			else {
				context.setTargetReverseComplement(false);
			}
		}
		else {
			LOGGER.error("Reference value '" + context.getReference() + "' is invalid");
			throw new Exception();
		}

		String[] leftoverArgs = line.getArgs();
		File target = null;
		File query = null;
		File outDirectory = null;

		context.setTargetFileName(leftoverArgs[0]); // validate as path
		target = new File(context.getTargetFileName());

		if (!target.exists() || !target.isFile()) {
			LOGGER.error("Invalid target file: '" + context.getTargetFileName() + "'");
			throw new Exception();
		}

		context.setQueryFileName(leftoverArgs[1]); // validate as path
		query = new File(context.getQueryFileName());

		if (!query.exists() || !query.isFile()) {
			LOGGER.error("Invalid query file: '" + context.getQueryFileName() + "'");
			throw new Exception();
		}

		if (leftoverArgs[2] == null) {
			context.setOutDir("./");
		}
		else {
			context.setOutDir(leftoverArgs[2]); // validate as path
			outDirectory = new File(context.getOutDir());

			if (!outDirectory.exists() || !outDirectory.isDirectory()) {
				LOGGER.error("Invalid output directory path: '" + context.getOutDir() + "'");
				throw new Exception();
			}
		}

		if (context.isDebugging()) {
			LOGGER.info("start-end target indexes: " + context.getTargetStartIndex() + "-" + context.getTargetEndIndex());
			LOGGER.info("reverseComplement of query sequence: " + context.isQueryReverseComplement());
			LOGGER.info("reverseComplement of target sequence: " + context.isTargetReverseComplement());
		}

		return context;
	}

	public void loadSubstitutionMatrix(PtvContext context) throws FileNotFoundException {
		File matrixFile = new File(context.getDnaMatrix());
		SubstitutionMatrix<NucleotideCompound> matrix =
				new SimpleSubstitutionMatrix<NucleotideCompound>(new AmbiguityDNACompoundSet(), matrixFile);
		context.setSubstitutionMatrix(matrix);
	}


	/*
	 * Converts a fasta containing nucleotide sequences into descriptions and sequences.
	 *
	 * @param fastaFileName fully qualified FASTA file name
	 *					containing the desired sequences
	 * @return LinkedHashMap each entry is a map from a description to a sequence
	 */
	private LinkedHashMap<String, DNASequence> createSequencesFromFastaFile(String fastafileName) {
		File fastaFile = new File(fastafileName);
		FastaReader<DNASequence, NucleotideCompound> fastaReader = null;

		try {
			fastaReader =
					new FastaReader<DNASequence, NucleotideCompound>(
							fastaFile,
							new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
							new FileProxyDNASequenceCreator(
									fastaFile,
									AmbiguityDNACompoundSet.getDNACompoundSet(),
									new FastaSequenceParser()));
		}
		catch (FileNotFoundException fnfe) {

		}

		LinkedHashMap<String, DNASequence> sequences = null;

		try {
			sequences = fastaReader.process();
		}
		catch (IOException ioe) {

		}

		return sequences;
	}

	/*
	 * Converts a fasta containing the reference sequence into
	 * a description and sequence.
	 *
	 * @param tFileName fully qualified FASTA file name
	 *					containing the reference sequence
	 * @param tStartIndex 1-based index to the start of the reference
	 * @param endIndex 1-based index to the end of the reference; null if none
	 * @return ArrayList containing a String(the description) and DNASequence
	 */
	public ReferenceSequence createTargetSequence(PtvContext context) {
		LinkedHashMap<String, DNASequence> referenceSequences = createSequencesFromFastaFile(context.getTargetFileName());

		Entry<String, DNASequence> referenceSequence = referenceSequences.entrySet().iterator().next();
		String targetDescription = referenceSequence.getKey();
		DNASequence targetSequence = referenceSequence.getValue();

		if (context.isDebugging()) {
			LOGGER.info("full reference " + targetDescription + " is " + targetSequence.getLength() + " bp long");
		}

		if (context.getTargetEndIndex() != 0) {
			context.setTargetEndIndex(new Integer(targetSequence.getLength()));
		}

		SequenceView<NucleotideCompound> sequenceView =
				targetSequence.getSubSequence(context.getTargetStartIndex(), context.getTargetEndIndex()); //indexed from 1
		targetSequence = new DNASequence(sequenceView.getSequenceAsString(), new AmbiguityDNACompoundSet());

		if (context.isDebugging()) {
			LOGGER.info("trimmed reference " + targetDescription + " is " + targetSequence.getLength() + " bp long");
		}

		return new ReferenceSequence(targetDescription, targetSequence);
	}

	/*
	 * Converts a fasta containing the query sequences (the sequences for which
	 * to generate the VCFs) into descriptions and sequences.
	 *
	 * @param context
	 * @return LinkedHashMap each entry is a map from a description to a sequence
	 */
	public LinkedHashMap<String, DNASequence> createQuerySequences(PtvContext context) {
		LinkedHashMap<String, DNASequence> querySequences = createSequencesFromFastaFile(context.getQueryFileName());

		return querySequences;
	}

	private String buildVcfPath(PtvContext context,
			String queryDescription, DNASequence querySequence) throws Exception {
		if (queryDescription.length() == 0) {
			LOGGER.error("ERROR parsing fasta description");
			throw new Exception("ERROR parsing fasta description");
		}

		Pattern fileNamePattern = Pattern.compile("(\\S+)\\s+(\\S+)");
		Matcher matcher = fileNamePattern.matcher(queryDescription);
		matcher.find();
		String embeddedFileName = matcher.group(2); // qDesc = (desc =~ /(\S+)\s+(\S+)/)[0][2]
		String vcfPath = context.getOutDir() + "/" + embeddedFileName + ".vcf";


		if (context.isDebugging()) {
			LOGGER.info("aligning " + embeddedFileName + "(" + querySequence.getLength() + " bp) to " + vcfPath);
		}

		return vcfPath;
	}


	public void createVcfFileWriter(PtvContext context, String queryDescription,
			DNASequence querySequence) throws Exception {
		String vcfPath = buildVcfPath(context, queryDescription, querySequence);
		context.setVcfPath(vcfPath);

		VcfWriter vcfWriter = new VcfWriter(vcfPath);
		context.setVcfWriter(vcfWriter);
	}

	public void writeHeader(PtvContext context) throws Exception {
		try {
			context.getVcfWriter().writeHeader(context.getTargetFileName());
		}
		catch (IOException ioe) {
			throw new Exception();
		}
	}

	public void destroyVcfFileWriter(PtvContext context) {
		context.getVcfWriter().close();
		context.setVcfWriter(null);
	}

	/*
	 * Creates the VCF given an alignment.
	 * From the 4.1 spec, "the POS field should specify the 1-based coordinate of
	 * the base before the variant or the best estimate thereof"
	 *
	 * @param pair a pairwise alignment
	 * @param tStartIndex 1-based index to the start of the reference
	 * @param variantsOnly output all positions if true, variants only if false
	 * @param outFullName the VCF file being generated
	 * @todo is tStartIndex always 1? if so, can remove
	 */
	public void processAlignment(PtvContext context, SequencePair<DNASequence, NucleotideCompound> pair,
			String targetDescription) {
		VcfWriter vcfWriter = context.getVcfWriter();
		int offsetFromFullRef = context.getTargetStartIndex() - 1;
		int refOffset = 1;
		int queryOffset = 1;
		int fullRefBasesProcessed = (offsetFromFullRef + refOffset - 1);
		int refBasesProcessed = refOffset - 1;
		int queryBasesProcessed = queryOffset - 1;
		String refAlignment = pair.getTarget().toString();
		String queryAlignment = pair.getQuery().toString();

		// iterate through the alignment
		for (int j = 0; j < refAlignment.length(); j++) {
			if (refAlignment.charAt(j) != '-') {
				fullRefBasesProcessed++;
				refBasesProcessed++;
			}

			if (queryAlignment.charAt(j) != '-') {
				queryBasesProcessed++;
			}

			if (refAlignment.charAt(j) == '-') { // start of insertion
				if (j == 0) {
					continue;
				}
				String insertion = "" + queryAlignment.charAt(j - 1);
				char refBase = refAlignment.charAt(j - 1);
				while ((j < refAlignment.length())
						&& refAlignment.charAt(j) == '-') {
					insertion = insertion + queryAlignment.charAt(j);
					queryBasesProcessed++;
					j++;
				}

				try {
					vcfWriter.writeInsertionRecord(targetDescription, fullRefBasesProcessed,
							String.valueOf(refBase), insertion);
				} catch (IOException ioe) {

				}

				queryBasesProcessed--;
				j--;
			} else if (queryAlignment.charAt(j) == '-') { // start of deletion
				if (j == 0) {
					continue;
				}
				String deletion = "" + refAlignment.charAt(j - 1);
				int deletionLoc = fullRefBasesProcessed - 1;
				char queryBase = queryAlignment.charAt(j - 1);
				while ((j < queryAlignment.length())
						&& queryAlignment.charAt(j) == '-') {
					deletion = deletion + refAlignment.charAt(j);
					fullRefBasesProcessed++;
					refBasesProcessed++;
					j++;
				}

				try {
					vcfWriter.writeDeletionRecord(targetDescription, deletionLoc,
							String.valueOf(queryBase), deletion);
				} catch (IOException ioe) {

				}

				fullRefBasesProcessed--;
				refBasesProcessed--;
				j--;
			} else if ((j + 2 > refAlignment.length())
					|| ((refAlignment.charAt(j + 1) != '-') && (queryAlignment
							.charAt(j + 1) != '-'))) {
				// don't output if an insertion or deletion is coming up
				// otherwise, write if outputting all bases or ref doesn't match
				// query
				boolean variantsOnly = context.getVariantsOnly();
				if (!variantsOnly
						|| (variantsOnly && (refAlignment.charAt(j) != queryAlignment
								.charAt(j)))) {

					try {
						vcfWriter.writeSNPRecord(targetDescription, fullRefBasesProcessed,
								String.valueOf(refAlignment.charAt(j)), String.valueOf(queryAlignment.charAt(j)));
					} catch (IOException ioe) {

					}
				}
			}
		} // for every base in the alignment

		LOGGER.info("Query bases processed: " + queryBasesProcessed);
		LOGGER.info("Reference bases processed: " + refBasesProcessed);
	}

	/*
	 * Compress the VCF file and generate an index for it.
	 *
	 * @param outFullName fully qualified path name to the VCF
	 */
	public void processVCF(PtvContext context) {
	    // zip the vcf
	    String cmd = "bgzip -f " + context.getVcfPath();
	    if (context.isDebugging()) {
	        LOGGER.info(cmd);
	    }
    	Process p = null;

	    try {
	    	p = Runtime.getRuntime().exec(cmd);
	    	p.waitFor();
		    if (p.exitValue() != 0) {
		        LOGGER.error("ERROR zipping ${outFullName}");
		        System.exit(p.exitValue());
		    }
	    }
	    catch (IOException ioe) {

	    }
	    catch (InterruptedException ie) {

	    }

	    // index the zipped vcf
	    String gzPath = context.getVcfPath() + ".gz";
	    cmd = "tabix -f " + gzPath;
	    if (context.isDebugging()) {
	        LOGGER.info(cmd);
	    }

	    try {
		    p = Runtime.getRuntime().exec(cmd);
		    p.waitFor();
		    if (p.exitValue() != 0) {
		        LOGGER.error("ERROR indexing " + gzPath);
		        System.exit(p.exitValue());
		    }
	    }
	    catch (IOException ioe) {

	    }
	    catch (InterruptedException ie) {

	    }
	}

}
