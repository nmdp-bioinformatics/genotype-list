package org.immunogenomics.gl.imgt.vcf.driver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.SimpleGapPenalty;
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

public class PairToVariants {
	// /usr/bin/env groovy

	/*
	 * Pairwise globally (Needleman-Wunsch) align and generate a VCF from it.
	 *
	 * e.g., pairToVariants.groovy /Volumes/bioxover/data/hla/references/GRCh38/chr6.fasta /Volumes/bioxover/data/ipd-imgt-hla/ftp.ebi.ac.uk/pub/databases/ipd/imgt/hla/A_gen.fasta /Volumes/bioxover/data/ipd-imgt-hla/variants/3.16.0/variants/GRCh38
	 *
	 * DPB1 and DRB1 require 30G of memory and take about 30 minutes.
	 * Other loci have desktop-level requirements.
	 *
	 * Using the RefSeq coordinates for full chromosome variants.
	 * Writes VCF 4.1.
	 *   http://www.1000genomes.org/wiki/Analysis/Variant%20Call%20Format/vcf-variant-call-format-version-41
	 *
	 * Jar requirements: biojava3-core, biojava3-alignment, ngs-tools
	 *
	 * @author Dave Roe
	 * @author Sean Landmann
	 * @todo replace NW with GU for lower memory requirements; make it be a command line option
	 *   http://www.biojava.org/docs/api/org/biojava3/alignment/routines/GuanUberbacher.html
	 * @todo add command line options with disevelled's commandline library or equivalent
	 *   handle args 0-2
	 *   add variantsOnly, gene, reference, openPenalty, extPenalty to command line option
	 * @todo use Heuer's alignment methods in ngs-tools? (BJ legacy)
	 *   I don't think GuanUberbacher is currently in BJ legacy
	 * @todo generate stand-alone executable with WrappingGroovyScript
	 * @todo add stand-alone executable to ngs-tools (?)
	 */
	// things that change per run: variantsOnly, gene, startIndexMap, endIndexMap
	Boolean variantsOnly = false;
	String gene = "HLA-A"; // for reverseComplementMap, startIndexMap, endIndexMap
	String reference = "hg38"; // 'gene' or 'hg38'
	boolean debugging = true;

	// EMBOSS's NW uses an opening penalty of 10 and 0.5 for extension.
	// http://www.ebi.ac.uk/Tools/psa/emboss_needle/nucleotide.html
	int openPenalty = 10;
	int extPenalty = 1;

	static PrintStream out = System.out;
	static PrintStream err = System.err;
	String tFileName = null;
	String qFileName = null;
	String outDir = null;
//	String dnaMatrix = "org/nmdp/ngs/tools/NUC.4.4.txt";
	String dnaMatrix = "org/immunogenomics/gl/imgt/vcf/NUC.4.4.txt";
	HashMap<String, Boolean> reverseComplementMap = null;

	Integer tStartIndex = null; // 1-based index
	Integer tEndIndex = null;
	boolean reverseComplement = false;

	String vcfHeader = null;

	FileWriter outStream = null;

	private class TargetSequence {
		String tDesc;
		DNASequence tSeq;

		public TargetSequence(java.lang.String tDesc2,
				org.biojava3.core.sequence.DNASequence tSeq2) {
			this.tDesc = tDesc2;
			this.tSeq = tSeq2;
		}

		public String gettDesc() {
			return tDesc;
		}

		public void settDesc(String tDesc) {
			this.tDesc = tDesc;
		}

		public DNASequence gettSeq() {
			return tSeq;
		}

		public void settSeq(DNASequence tSeq) {
			this.tSeq = tSeq;
		}
	}

	public static void main(String[] args) {
		PairToVariants ptv = new PairToVariants();

		ptv.tFileName = args[0];
		ptv.qFileName = args[1];
		ptv.outDir = args[2];

		ptv.reverseComplementMap = new HashMap<String, Boolean>();
		ptv.reverseComplementMap.put("HLA-A", false);
		ptv.reverseComplementMap.put("HLA-B", true);
		ptv.reverseComplementMap.put("HLA-C", true);
		ptv.reverseComplementMap.put("HLA-DPB1", true);
		ptv.reverseComplementMap.put("HLA-DQB1", true);
		ptv.reverseComplementMap.put("HLA-DRB1", true);

		// per-reference stuff
		// 1-based index
		if (ptv.reference == "gene") {
			ptv.tStartIndex = 1; // don't set tEndIndex with gene-specific reference
			ptv.reverseComplement = false;
		} else if (ptv.reference == "hg38") {	// GRCh38/hg38
			HashMap<String, Integer> startIndexMap = new HashMap<String, Integer>();
			startIndexMap.put("HLA-A", 29942170);
			startIndexMap.put("HLA-B", 31353572);
			startIndexMap.put("HLA-C", 31268749);
			startIndexMap.put("HLA-DPB1", 33075926);
			startIndexMap.put("HLA-DQB1", 32659464);
			startIndexMap.put("HLA-DRB1", 32578769);

			HashMap<String, Integer> endIndexMap = new HashMap<String, Integer>();
			endIndexMap.put("HLA-A", 29945884);
			endIndexMap.put("HLA-B", 31357212);
			endIndexMap.put("HLA-C", 31272136);
			endIndexMap.put("HLA-DPB1", 33089696);
			endIndexMap.put("HLA-DQB1", 32666689);
			endIndexMap.put("HLA-DRB1", 32589836);

			ptv.tStartIndex = startIndexMap.get(ptv.gene);
			ptv.tEndIndex = endIndexMap.get(ptv.gene);
			ptv.reverseComplement = ptv.reverseComplementMap.containsKey(ptv.gene);

			if (ptv.debugging) {
				err.println("start-end target indexes: " + ptv.tStartIndex.intValue() + "-" + ptv.tEndIndex.intValue());
				err.println("reverseComplement of query sequence: " + ptv.reverseComplement);
			}
		}

		// header line (CHROM...) must be tab delimited for IGV
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		ptv.vcfHeader = "##fileformat=VCFv4.1\n" +
		"##fileDate=" + formatter.format(new Date()) + "\n" +
		"##reference=file:" + ptv.tFileName + "\n" +
		"##INFO=<ID=SNP,Number=0,Type=Flag,Description=\"Variant is a SNP\">\n" +
		"##INFO=<ID=INS,Number=0,Type=Flag,Description=\"Variant is an insertion\">\n" +
		"##INFO=<ID=DEL,Number=0,Type=Flag,Description=\"Variant is a deletion\">\n" +
		"#CHROM	POS	ID	REF	ALT	QUAL	FILTER	INFO\n";

		ptv.createVCF();
	}

	private void createVCF() {
		InputStream matrixStream = this.getClass().getClassLoader().getResourceAsStream(this.dnaMatrix);
		InputStreamReader matrixReader = new InputStreamReader(matrixStream);
		SubstitutionMatrix<NucleotideCompound> matrix =
				new SimpleSubstitutionMatrix<NucleotideCompound>(
						new AmbiguityDNACompoundSet(), matrixReader, "NUC.4.4.txt");
		// read the reference sequence
		TargetSequence targetSequence =
				this.createTargetSequence(this.tFileName, this.tStartIndex, this.tEndIndex);
		String tDesc = targetSequence.gettDesc();
		DNASequence tSeq = targetSequence.gettSeq();

		// read the query sequences
		LinkedHashMap<String, DNASequence> sequences = this.createQuerySequences(this.qFileName);

		for (Entry<String, DNASequence> sequence : sequences.entrySet()) {
			String desc = sequence.getKey();
			DNASequence qSeq = sequence.getValue();

			Pattern p = Pattern.compile("(\\S+)\\s+(\\S+)");
			Matcher m = p.matcher(desc);
			m.find();
			String qDesc = m.group(2); // qDesc = (desc =~ /(\S+)\s+(\S+)/)[0][2]
			String outFullName = this.outDir + "/" + qDesc + ".vcf";

			try {
				this.outStream = new FileWriter(outFullName);
			}
			catch (IOException ioe) {
				err.println("Error when creating output file '" + outFullName + "': " + ioe.getMessage());
			}

			if (this.debugging) {
				err.println("aligning " + qDesc + "(" + qSeq.getLength() + " bp) to " + outFullName);
			}

			if (desc.length() == 0) {
				err.println("ERROR parsing fasta description '" + qDesc + "'");
				System.exit(1);
			}
			if (this.reverseComplement) {
				qSeq = new DNASequence(qSeq.getReverseComplement().getSequenceAsString(),
									   new AmbiguityDNACompoundSet());
			}

			try {
				this.outStream.write(this.vcfHeader);
			}
			catch (IOException ioe) {

			}

			// generate the alignment
			// qSeq is query, tSeq is target
			SequencePair<DNASequence, NucleotideCompound> pair =
			Alignments.getPairwiseAlignment(qSeq, tSeq, PairwiseSequenceAlignerType.GLOBAL,
											new SimpleGapPenalty((short)this.openPenalty,
																 (short)this.extPenalty),
											matrix);

/*			if (this.debugging) {
				err.println "target: " + pair.getTarget()
				err.println "query:  " + pair.getQuery()
			}
*/
			processAlignment(pair, tStartIndex, this.variantsOnly, tDesc);

			try {
				this.outStream.close();
			}
			catch (IOException ioe) {

			}

			processVCF(outFullName);
		} // each sequence in the fasta file

		err.println("done");
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
	TargetSequence createTargetSequence(String tFileName, Integer tStartIndex,
									   Integer tEndIndex) {
		File tFile = new File(tFileName);
		FastaReader<DNASequence, NucleotideCompound> fastaProxyReader = null;

		try {
			fastaProxyReader =
				new FastaReader<DNASequence, NucleotideCompound>(
						tFile,
						new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
						new FileProxyDNASequenceCreator(
								tFile,
								AmbiguityDNACompoundSet.getDNACompoundSet(),
								new FastaSequenceParser()));
		}
		catch (FileNotFoundException fnfe) {

		}

		String tDesc = null;
		DNASequence tSeq = null;

		try {
			LinkedHashMap<String, DNASequence> refSequences = fastaProxyReader.process();

			Entry<String, DNASequence> refSequence = refSequences.entrySet().iterator().next();
			tDesc = refSequence.getKey();
			tSeq = refSequence.getValue();

			if (this.debugging) {
				err.println("full reference " + tDesc + " is " + tSeq.getLength() + " bp long");
			}

			if (tEndIndex.intValue() != 0) {
				tEndIndex = new Integer(tSeq.getLength());
			}
			SequenceView<NucleotideCompound> sv = tSeq.getSubSequence(tStartIndex, tEndIndex); //indexed from 1
			tSeq = new DNASequence(sv.getSequenceAsString(), new AmbiguityDNACompoundSet());

			if (this.debugging) {
				err.println("trimmed reference " + tDesc + " is " + tSeq.getLength() + " bp long");
			}
		}
		catch (IOException ioe) {

		}

		return new TargetSequence(tDesc, tSeq);
	}

	/*
	 * Converts a fasta containing the query sequences (the sequences for which
	 * to generate the VCFs) into descriptions and sequences.
	 *
	 * @param qFileName fully qualified FASTA file name
	 *					containing the query sequences
	 * @return LinkedHashMap each entry is a map from a description to a sequence
	 */
	private LinkedHashMap<String, DNASequence> createQuerySequences(String qFileName) {
		File qFile = new File(qFileName);
		FastaReader<DNASequence, NucleotideCompound> fastaProxyReader = null;

		try {
			fastaProxyReader =
					new FastaReader<DNASequence, NucleotideCompound>(
							qFile,
							new GenericFastaHeaderParser<DNASequence, NucleotideCompound>(),
							new FileProxyDNASequenceCreator(
									qFile,
									AmbiguityDNACompoundSet.getDNACompoundSet(),
									new FastaSequenceParser()));
		}
		catch (FileNotFoundException fnfe) {

		}

		LinkedHashMap<String, DNASequence> sequences = null;

		try {
			sequences = fastaProxyReader.process();
		}
		catch (IOException ioe) {

		}

		return sequences;
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
	void processAlignment(SequencePair<DNASequence, NucleotideCompound> pair,
			Integer tStartIndex, Boolean variantsOnly, String tDesc) {
		NucleotideCompound hyphen = new NucleotideCompound("-",
				new AmbiguityDNACompoundSet(), "-");
		int offsetFromFullRef = tStartIndex - 1;
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
					this.outStream.write(tDesc + "\t" + fullRefBasesProcessed + "\t"
							+ "." + "\t" + refBase + "\t" + insertion
							+ "\t.\tPASS\tINS\n");
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
				int deletionSize = 0;
				while ((j < queryAlignment.length())
						&& queryAlignment.charAt(j) == '-') {
					deletion = deletion + refAlignment.charAt(j);
					deletionSize++;
					fullRefBasesProcessed++;
					refBasesProcessed++;
					j++;
				}
				try {
					outStream.write(tDesc + "\t" + deletionLoc + "\t" + "."
							+ "\t" + deletion + "\t" + queryBase
							+ "\t.\tPASS\tDEL\n");
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
				if (!variantsOnly
						|| (variantsOnly && (refAlignment.charAt(j) != queryAlignment
								.charAt(j)))) {

					try {
						this.outStream.write(tDesc + "\t" + fullRefBasesProcessed
								+ "\t" + "." + "\t" + refAlignment.charAt(j)
								+ "\t" + queryAlignment.charAt(j)
								+ "\t.\tPASS\tSNP\n");
					} catch (IOException ioe) {

					}
				}
			}
		} // for every base in the alignment
	}

	/*
	 * Compress the VCF file and generate an index for it.
	 *
	 * @param outFullName fully qualified path name to the VCF
	 */
	void processVCF(String outFullName) {
	    // zip the vcf
	    String cmd = "bgzip -f ${outFullName}";
	    if (this.debugging) {
	        err.println(cmd);
	    }
    	Process p = null;

	    try {
	    	p = Runtime.getRuntime().exec(cmd);
	    	p.waitFor();
		    if (p.exitValue() != 0) {
		        err.println("ERROR zipping ${outFullName}");
		        System.exit(p.exitValue());
		    }
	    }
	    catch (IOException ioe) {

	    }
	    catch (InterruptedException ie) {

	    }

	    // index the zipped vcf
	    outFullName = "${outFullName}.gz";
	    cmd = "tabix -f ${outFullName}";
	    if (this.debugging) {
	        err.println(cmd);
	    }

	    try {
		    p = Runtime.getRuntime().exec(cmd);
		    p.waitFor();
		    if (p.exitValue() != 0) {
		        err.println("ERROR indexing " + outFullName);
		        System.exit(p.exitValue());
		    }
	    }
	    catch (IOException ioe) {

	    }
	    catch (InterruptedException ie) {

	    }
	}

}
