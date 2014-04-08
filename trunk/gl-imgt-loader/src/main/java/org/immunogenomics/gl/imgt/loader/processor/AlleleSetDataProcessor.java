/*

    gl-imgt-loader  IMGT/HLA database loader for the gl project.
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
package org.immunogenomics.gl.imgt.loader.processor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.immunogenomics.gl.imgt.db.dao.impl.AlleleSetDaoImpl;
import org.immunogenomics.gl.imgt.db.dao.intf.AlleleSetDaoIntf;
import org.immunogenomics.gl.imgt.db.model.AlignmentReference;
import org.immunogenomics.gl.imgt.db.model.Allele;
import org.immunogenomics.gl.imgt.db.model.AlleleRlsVerDtl;
import org.immunogenomics.gl.imgt.db.model.CdnaCoordinate;
import org.immunogenomics.gl.imgt.db.model.CdnaIndel;
import org.immunogenomics.gl.imgt.db.model.Citation;
import org.immunogenomics.gl.imgt.db.model.Ethnicity;
import org.immunogenomics.gl.imgt.db.model.Feature;
import org.immunogenomics.gl.imgt.db.model.FeatureTranslation;
import org.immunogenomics.gl.imgt.db.model.Locus;
import org.immunogenomics.gl.imgt.db.model.NucSequence;
import org.immunogenomics.gl.imgt.db.model.ReleaseVersion;
import org.immunogenomics.gl.imgt.db.model.Sample;
import org.immunogenomics.gl.imgt.db.model.Sequence;
import org.immunogenomics.gl.imgt.db.model.SequenceCoordinate;
import org.immunogenomics.gl.imgt.db.model.SourceMaterial;
import org.immunogenomics.gl.imgt.db.model.SourceXref;
import org.immunogenomics.gl.imgt.db.model.Species;
import org.immunogenomics.gl.imgt.db.dto.AmbigComboElementDto;
import org.immunogenomics.gl.imgt.db.dto.AmbigGroupDto;
import org.immunogenomics.gl.imgt.db.dto.GGroupDto;
import org.immunogenomics.gl.imgt.db.dto.IncompleteAlleleDto;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbiguousComboElement;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbiguousComboGroup;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.GGroup;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.GGroupAllele;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.Gene;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.IncompleteAllele;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.RemovedAllele;

/**
 * @author Anthony Barber (abarber) <abarber@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AlleleSetDataProcessor {

	AlleleSetDaoIntf alleleSetDao;
	Mapper mapper;
	private static final Logger LOGGER = Logger
			.getLogger(AlleleSetDataProcessor.class);

	public AlleleSetDataProcessor(){
		alleleSetDao = new AlleleSetDaoImpl();
		mapper = DozerBeanMapperSingletonWrapper.getInstance();
	}

	public void insertAlleleSet(org.immunogenomics.gl.imgt.xml.model.hla.Allele allele) throws RollbackException{

		// Database objects
		Allele alleleDb = null;
		ReleaseVersion rvDb = null;
		Locus locusDb = null;
		Species speciesDb = null;
		AlignmentReference arDb = null;
		NucSequence nucSeqDb = null;
		SourceMaterial sourceMatDb = null;
		AlleleRlsVerDtl arvDtlDb = null;
		Sequence seqDb = null;
		Feature featureDb = null;
		SequenceCoordinate seqCoordDb = null;
		CdnaCoordinate cdnaCoordDb = null;
		FeatureTranslation translationDb = null;
		CdnaIndel cdnaIndelDb = null;

		List<Feature> featureListDb = null;
		List<Citation> citeListDb = null;
		List<SourceXref> sXrefListDb = null;
		List<Sample> sampleListDb = null;
		List<Ethnicity> ethnicityDbList = new ArrayList<Ethnicity>();
		List<SourceMaterial> sourceMatDbList = new ArrayList<SourceMaterial>();
		List<Sequence> seqDbList = new ArrayList<Sequence>();
		List<AlleleRlsVerDtl> alleleRelVerDtlDbList = new ArrayList<AlleleRlsVerDtl>();
		List<Species> speciesDbList = new ArrayList<Species>();
		List<AlignmentReference> arDbList = new ArrayList<AlignmentReference>();
		List<NucSequence> nucSeqDblist = new ArrayList<NucSequence>();
		List<SequenceCoordinate> seqCoordDbList = new ArrayList<SequenceCoordinate>();
		List<CdnaCoordinate> cdnaCoordDbList = null;
		List<FeatureTranslation> ftDbList = null;
		List<CdnaIndel> cdnaIndelDbList = null;
		List<Allele> alleleDbList = new ArrayList<Allele>();

		String lastUpdateUserId = "imgt_user";
		Date d = new Date();

		// convert the xml objects to database objects via dozer mappings
		if(allele != null){

			alleleDb = mapper.map(allele, org.immunogenomics.gl.imgt.db.model.Allele.class, "AlleleMap");
			alleleDb.setCreateDte(d);
			alleleDb.setLastUpdateDte(d);
			alleleDb.setLastUpdateUserId(lastUpdateUserId);

			// release verions
			if(allele.getReleaseversions() != null){
				rvDb = mapper.map(allele.getReleaseversions(), org.immunogenomics.gl.imgt.db.model.ReleaseVersion.class, "ReleaseVersionsMap");
				arvDtlDb = mapper.map(allele.getReleaseversions(), AlleleRlsVerDtl.class, "AlleleRelVerDtlMap");

				rvDb.setCreateDte(d);
				rvDb.setLastUpdateDte(d);
				rvDb.setLastUpdateUserId(lastUpdateUserId);

				// Check if length of ReleaseVerStatus > 100, truncate if so
				if (arvDtlDb.getReleaseVerStatus().length() > 100) {
					arvDtlDb.setReleaseVerStatus(arvDtlDb.getReleaseVerStatus().substring(0, 100));
				}

				arvDtlDb.setCreateDte(d);
				arvDtlDb.setLastUpdateDte(d);
				arvDtlDb.setLastUpdateUserId(lastUpdateUserId);

//				arvDtlDb.setReleaseVersion(rvDb);
				arvDtlDb.setAllele(alleleDb);
				alleleRelVerDtlDbList.add(arvDtlDb);

//				rvDb.setAlleleReleaseVersionDtls(alleleRelVerDtlDbList);

				alleleDb.setReleaseVersion(rvDb);
				alleleDb.setAlleleRlsVerDtls(alleleRelVerDtlDbList);

			}

			if(allele.getLocus() != null){
				locusDb = mapper.map(allele.getLocus(), org.immunogenomics.gl.imgt.db.model.Locus.class, "LocusMap");

				// The locusname specified in Locus schema is incomplete. To get the complete locusname, the
				// Allele.name attribute is parsed by taking the string found to the left of the '*'.
				locusDb.setLocusName(allele.getName().substring(0, allele.getName().indexOf("*")));

				locusDb.setLastUpdateDte(d);
				locusDb.setCreateDte(d);
				locusDb.setLastUpdateUserId(lastUpdateUserId);

				alleleDb.setLocus(locusDb);
			}

			// citations
			try {
				if((allele.getCitations().getCitation() != null) && (!allele.getCitations().getCitation().isEmpty())){
					citeListDb = new ArrayList<Citation>(allele.getCitations().getCitation().size());

					if(citeListDb != null){
						for(org.immunogenomics.gl.imgt.xml.model.hla.Citation c : allele.getCitations().getCitation()){

							citeListDb.add(mapper.map(c, org.immunogenomics.gl.imgt.db.model.Citation.class, "CitationMap"));

						}
					}

					// set each citation's allele
					for(Citation cite : citeListDb){
						cite.setAllele(alleleDb);
						cite.setCreateDte(d);
						cite.setLastUpdateDte(d);
						cite.setLastUpdateUserId(lastUpdateUserId);
					}
					//set the allele's citation list
					alleleDb.setCitations(citeListDb);
				}
			} catch (Exception e) {
				LOGGER.warn("AlleleSetDataProcessor.insertAlleleSet:: There are no Citations for Alllele: " + allele.getId() + " " + allele.getName() + " " + e);
			}

			// source xrefs
			try {
				if((allele.getSourcexrefs().getXref() != null) && (!allele.getSourcexrefs().getXref().isEmpty())){
					sXrefListDb = new ArrayList<SourceXref>(allele.getSourcexrefs().getXref().size());

					if(sXrefListDb != null){
						for(org.immunogenomics.gl.imgt.xml.model.hla.Xref x : allele.getSourcexrefs().getXref()){
							sXrefListDb.add(mapper.map(x, org.immunogenomics.gl.imgt.db.model.SourceXref.class, "SourceXrefMap"));
						}
					}

					// set each xref's allele
					for(SourceXref x : sXrefListDb){
						x.setAllele(alleleDb);
						x.setCreateDte(d);
						x.setLastUpdateDte(d);
						x.setLastUpdateUserId(lastUpdateUserId);
					}
					//set the allele's list
					alleleDb.setSourceXrefs(sXrefListDb);

				}
			} catch (Exception e) {
				LOGGER.warn("AlleleSetDataProcessor.insertAlleleSet:: There are no Source Xrefs for Alllele: " + allele.getId() + " " + allele.getName() + " " + e);
			}

			// source material, species, ethnicity, and samples
			try {
				if((allele.getSourcematerial() != null)){

					// map the source material. set its allele
					sourceMatDb = mapper.map(allele.getSourcematerial(), SourceMaterial.class, "SourceMaterialMap");
					sourceMatDb.setAllele(alleleDb);

					sourceMatDb.setCreateDte(d);
					sourceMatDb.setLastUpdateDte(d);
					sourceMatDb.setLastUpdateUserId(lastUpdateUserId);

					// species
					if((allele.getSourcematerial().getSpecies() != null)){

						speciesDb = mapper.map(allele.getSourcematerial().getSpecies(), org.immunogenomics.gl.imgt.db.model.Species.class, "SpeciesMap");
						speciesDb.setSourceMaterial(sourceMatDb);

						speciesDb.setCreateDte(d);
						speciesDb.setLastUpdateDte(d);
						speciesDb.setLastUpdateUserId(lastUpdateUserId);

						speciesDbList.add(speciesDb);
						sourceMatDb.setSpecies(speciesDbList);

					} // end species

					// ethnicity
					if((allele.getSourcematerial().getEthnicity() != null)
							&& (allele.getSourcematerial().getEthnicity().getSampleEthnicity() != null)){

						// map each ethnicity, set each ethnicity's source material
						for(String s : allele.getSourcematerial().getEthnicity().getSampleEthnicity()){

							Ethnicity eth = new Ethnicity();
							eth.setKnownEthnicity(s);
							eth.setSourceMaterial(sourceMatDb);

							eth.setCreateDte(d);
							eth.setLastUpdateDte(d);
							eth.setLastUpdateUserId(lastUpdateUserId);

							ethnicityDbList.add(eth);

						} // end foreach

						sourceMatDb.setEthnicities(ethnicityDbList);

					} // end ethnicity

					// samples
					if((allele.getSourcematerial().getSamples() != null) && (!allele.getSourcematerial().getSamples().getSample().isEmpty())){
						sampleListDb = new ArrayList<Sample>(allele.getSourcematerial().getSamples().getSample().size());

						// map the samples
						if(sampleListDb != null){
							for(org.immunogenomics.gl.imgt.xml.model.hla.Sample s : allele.getSourcematerial().getSamples().getSample()){
								sampleListDb.add(mapper.map(s, org.immunogenomics.gl.imgt.db.model.Sample.class, "SampleMap"));
							}
						}

						// set each sample's source material
						for(Sample x : sampleListDb){
							x.setSourceMaterial(sourceMatDb);

							x.setCreateDte(d);
							x.setLastUpdateDte(d);
							x.setLastUpdateUserId(lastUpdateUserId);
						}

					} // end samples
//					else{
//						// we want to add the source material object even if there are no samples
//						// as it contains the ethnicity and species
//						sourceMatDbList.add(sourceMatDb);
//						alleleDb.setSourceMaterials(sourceMatDbList);
//					}

					sourceMatDb.setSamples(sampleListDb);

					sourceMatDbList.add(sourceMatDb);
					alleleDb.setSourceMaterials(sourceMatDbList);

				} // end if source material null
			} catch (Exception e) {
				LOGGER.warn("AlleleSetDataProcessor.insertAlleleSet:: There are no Source Material Samples for Alllele: " + allele.getId() + " " + allele.getName() + " " + e);
			}

			// sequence, feature, alignment reference, feature translation
			// cdna coordinate, cdna indel, sequence coordinate
			if(allele.getSequence() != null) {

				// create the new sequence object. (There is nothing to map.)
				// set the allele
				seqDb = new Sequence();
				seqDb.setAllele(alleleDb);

				seqDb.setCreateDte(d);
				seqDb.setLastUpdateDte(d);
				seqDb.setLastUpdateUserId(lastUpdateUserId);

				try {
					if(!allele.getSequence().getFeature().isEmpty()){
						featureListDb = new ArrayList<Feature>(allele.getSequence().getFeature().size());

						if(featureListDb != null){

							for(org.immunogenomics.gl.imgt.xml.model.hla.Feature f : allele.getSequence().getFeature()){

								// create new lists each pass
								seqCoordDbList = new ArrayList<SequenceCoordinate>();
								cdnaCoordDbList = new ArrayList<CdnaCoordinate>();
								ftDbList = new ArrayList<FeatureTranslation>();
								cdnaIndelDbList = new ArrayList<CdnaIndel>();

								// feature
								featureDb = mapper.map(f, org.immunogenomics.gl.imgt.db.model.Feature.class, "FeatureMap");
								featureDb.setSequence(seqDb);

								featureDb.setCreateDte(d);
								featureDb.setLastUpdateDte(d);
								featureDb.setLastUpdateUserId(lastUpdateUserId);

								if(f.getSequenceCoordinates() != null){
									seqCoordDb = mapper.map(f, org.immunogenomics.gl.imgt.db.model.SequenceCoordinate.class, "FeatureSeqCoordMap");
									seqCoordDb.setFeature(featureDb);

									seqCoordDb.setCreateDte(d);
									seqCoordDb.setLastUpdateDte(d);
									seqCoordDb.setLastUpdateUserId(lastUpdateUserId);

									seqCoordDbList.add(seqCoordDb);

									featureDb.setSequenceCoordinates(seqCoordDbList);

								}

								if(f.getCDNACoordinates() != null){
									cdnaCoordDb = mapper.map(f, org.immunogenomics.gl.imgt.db.model.CdnaCoordinate.class, "FeatureCDNACoordMap");
									cdnaCoordDb.setFeature(featureDb);

									cdnaCoordDb.setCreateDte(d);
									cdnaCoordDb.setLastUpdateDte(d);
									cdnaCoordDb.setLastUpdateUserId(lastUpdateUserId);

									cdnaCoordDbList.add(cdnaCoordDb);

									featureDb.setCdnaCoordinates(cdnaCoordDbList);

								}

								if(f.getTranslation() != null){
									translationDb = mapper.map(f, org.immunogenomics.gl.imgt.db.model.FeatureTranslation.class, "FeatureTranslationMap");
									translationDb.setFeature(featureDb);

									translationDb.setCreateDte(d);
									translationDb.setLastUpdateDte(d);
									translationDb.setLastUpdateUserId(lastUpdateUserId);

									ftDbList.add(translationDb);

									featureDb.setFeatureTranslations(ftDbList);
								}

								if(f.getCDNAindel() != null){
									cdnaIndelDb = mapper.map(f, org.immunogenomics.gl.imgt.db.model.CdnaIndel.class, "FeatureCDNAindelMap");
									cdnaIndelDb.setFeature(featureDb);

									cdnaIndelDb.setCreateDte(d);
									cdnaIndelDb.setLastUpdateDte(d);
									cdnaIndelDb.setLastUpdateUserId(lastUpdateUserId);

									cdnaIndelDbList.add(cdnaIndelDb);

									featureDb.setCdnaIndels(cdnaIndelDbList);

								}

								featureListDb.add(featureDb);

							} // end foreach feature


						} // end if featureListDb != null

						seqDb.setFeatures(featureListDb);

					}
				} catch (Exception e) {
					LOGGER.warn("AlleleSetDataProcessor.insertAlleleSet:: There are no Features for Alllele: " + allele.getId() + " " + allele.getName() + " " + e);
				}

				// alignment reference
				if((allele.getSequence().getAlignmentreference() != null)){
					arDb = mapper.map(allele.getSequence().getAlignmentreference(), org.immunogenomics.gl.imgt.db.model.AlignmentReference.class, "AlignmentReferenceMap");
					arDb.setSequence(seqDb);

					arDb.setCreateDte(d);
					arDb.setLastUpdateDte(d);
					arDb.setLastUpdateUserId(lastUpdateUserId);

					arDbList.add(arDb);
					seqDb.setAlignmentReferences(arDbList);
				}

				// nucleotide sequence
				if((allele.getSequence().getNucsequence() != null)){
					nucSeqDb = mapper.map(allele.getSequence(), org.immunogenomics.gl.imgt.db.model.NucSequence.class, "SequenceNucSeqMap");
					nucSeqDb.setSequence(seqDb);

					nucSeqDb.setCreateDte(d);
					nucSeqDb.setLastUpdateDte(d);
					nucSeqDb.setLastUpdateUserId(lastUpdateUserId);

					nucSeqDblist.add(nucSeqDb);
					seqDb.setNucSequences(nucSeqDblist);
				}

			seqDbList.add(seqDb);
			alleleDb.setSequences(seqDbList);

			} // end sequence !null

			alleleDbList.add(alleleDb);
			alleleDb.getLocus().setAlleles(alleleDbList);
			alleleDb.getReleaseVersion().setAlleles(alleleDbList);


		} // end if allele != null



		try{

			// create the allele set
			alleleSetDao.persistAllele(alleleDb);
		}
		catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("AlleleSetDataProcessor.insertAlleleSet:: Exception for Allele: " + allele.getId() + " " + allele.getName() + " " + e);

		}
		finally{

			// clear the objects
			alleleDb = null;
			arDb = null;
			citeListDb = null;
			ethnicityDbList = null;
			featureListDb = null;
			locusDb = null;
			nucSeqDb = null;
			rvDb = null;
			sampleListDb = null;
			sXrefListDb = null;
			speciesDb = null;
		}


	} // end insertAlleleSet


	public void insertAmbiguousAlleleSet(org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbiguityData ambigData) throws RollbackException{

		// xml objects
		List<Gene> geneList = ambigData.getGeneList().getGene();
		ReleaseVersion releaseVer = null;

		List<GGroup> listOfGGroups = null;
		List<GGroupAllele> listOfAllelesInGGroup = null;
		List<RemovedAllele> listOfRemovedAlleles = null;
		List<IncompleteAllele> listOfIncompleteAlleles = null;
		List<AmbiguousComboGroup> listOfAmbigComboGroups = null;
		List<AmbiguousComboElement> listOfAmbigElements = null;
		StringBuilder gGroupName = new StringBuilder();

		// Database objects
		Allele alleleDb = null;
		Locus locusDb = null;

		List<Allele> gGroupDbAlleles = new ArrayList<Allele>();
		//		Map<String, List<Allele>> ggroupMap = new HashMap<String, List<Allele>>();
		List<GGroupDto> gGroups = new ArrayList<GGroupDto>();
		GGroupDto gGroupDto = null;

		List<Allele> removedDbAlleles = new ArrayList<Allele>();

		List<IncompleteAlleleDto> incompleteAlleles = new ArrayList<IncompleteAlleleDto>();

		List<AmbigComboElementDto> ambigElementCombos = null;
		List<AmbigGroupDto> ambigGroups = new ArrayList<AmbigGroupDto>();
		IncompleteAlleleDto incompAlleleDto = null;
		AmbigComboElementDto ambigComboElementDto = null;
		AmbigGroupDto ambigGroupDto = null;

		try{
			// convert the release version
			releaseVer = mapper.map(ambigData.getReleaseVersion(), org.immunogenomics.gl.imgt.db.model.ReleaseVersion.class, "AmbigReleaseVersionMap");

			// iterate over the genes
			for(Gene gene : geneList){

				/** G Groups **/
				listOfGGroups = gene.getGGroupsList().getGGroup();
				locusDb = new Locus();
				locusDb.setLocusName(gene.getName());
				locusDb.setGeneSystem(gene.getGeneSystem());

				for(GGroup ggroup : listOfGGroups){

					gGroupDto = new GGroupDto();
					gGroupName.append(ggroup.getName());
					gGroupDto.setgGroupName(gGroupName.toString());
					gGroupDto.setGid(ggroup.getGid());

					listOfAllelesInGGroup = ggroup.getGGroupAllele();

					// iterate over the alleles in the ggroup
					for(GGroupAllele gAllele : listOfAllelesInGGroup){

						// convert to a database Allele
						alleleDb = mapper.map(gAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "GGroupAlleleMap");

						// add the convert alleles to a list
						gGroupDbAlleles.add(alleleDb);
					} // for alleles in group

					// make an new arraylist to put in the gGroup
					gGroupDto.setgGroup(new ArrayList<Allele>(gGroupDbAlleles));

					// create the list of G groups
					gGroups.add(gGroupDto);

					// clear our temporary containers
					gGroupName.delete(0, gGroupName.length());
					gGroupDbAlleles.clear();

				} // end for list of groups


				/** Removed Alleles **/
				listOfRemovedAlleles = gene.getRemovedAllelesList().getRemovedAllele();
				// Get the removed alleles
				for(RemovedAllele rAllele : listOfRemovedAlleles){

					alleleDb = new Allele();

					// convert to a database Allele
					alleleDb = mapper.map(rAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "RemovedAlleleMap");
					removedDbAlleles.add(alleleDb);

				} // end for RemovedAllele


				/** Incomplete Alleles **/
				listOfIncompleteAlleles = gene.getIncompleteAllelesList().getIncompleteAllele();
				// Get the incomplete alleles
				for(IncompleteAllele iAllele : listOfIncompleteAlleles){

					incompAlleleDto = new IncompleteAlleleDto();

					alleleDb = mapper.map(iAllele.getPartialAllele(), org.immunogenomics.gl.imgt.db.model.Allele.class, "PartialAlleleMap");
					incompAlleleDto.setPartialAllele(alleleDb);

					alleleDb = mapper.map(iAllele.getExtensionAllele(), org.immunogenomics.gl.imgt.db.model.Allele.class, "ExtensionAlleleMap");
					incompAlleleDto.setExtensionAllele(alleleDb);

					incompleteAlleles.add(incompAlleleDto);

				} // end for IncompleteAllele

				/** Ambiguous Alleles **/
				listOfAmbigComboGroups = gene.getAmbigCombosList().getAmbiguousComboGroup();

				for(AmbiguousComboGroup ambigComboGroup : listOfAmbigComboGroups){

					// new ambiguous group dto
					// and new list of elements
					ambigGroupDto = new AmbigGroupDto();
					ambigElementCombos = new ArrayList<AmbigComboElementDto>();

					ambigGroupDto.setAmbigSequence(ambigComboGroup.getDiploidsequence().getAmbigsequence());

					listOfAmbigElements = ambigComboGroup.getAmbiguousComboElement();

					for(AmbiguousComboElement ambigElement : listOfAmbigElements){

						// new a new ambig combo element
						ambigComboElementDto = new AmbigComboElementDto();

						// convert the ambiguous alleles
						alleleDb = mapper.map(ambigElement.getAmbigAllele1(), org.immunogenomics.gl.imgt.db.model.Allele.class, "AmbigAllele1Map");
						ambigComboElementDto.setAmbigAllele1(alleleDb);

						alleleDb = mapper.map(ambigElement.getAmbigAllele2(), org.immunogenomics.gl.imgt.db.model.Allele.class, "AmbigAllele2Map");
						ambigComboElementDto.setAmbigAllele2(alleleDb);

						// add the ambiguous combination
						ambigElementCombos.add(ambigComboElementDto);

					} // end for

					// add the ambiguous elements to the group object (so we know they're a group)
					ambigGroupDto.setAmbigComboElementsList(new ArrayList<AmbigComboElementDto>(ambigElementCombos));

					// add new group to the full list of Ambiguous Groups
					ambigGroups.add(ambigGroupDto);

				} // end for AmbiguousComboGroup

				alleleSetDao.createAmbiguousAlleleSet(locusDb, releaseVer, gGroups, removedDbAlleles, incompleteAlleles, ambigGroups);

				removedDbAlleles.clear();
				incompleteAlleles.clear();
				ambigGroups.clear();

			} // end for gene

		}
		catch(MappingException e){
			e.printStackTrace();
		}
		finally{

			geneList = null;
			releaseVer = null;

			listOfGGroups = null;
			listOfAllelesInGGroup = null;
			listOfRemovedAlleles = null;
			listOfIncompleteAlleles = null;
			listOfAmbigComboGroups = null;
			listOfAmbigElements = null;
			gGroupName = null;

			alleleDb = null;

			gGroupDbAlleles = null;

			gGroups = null;
			gGroupDto = null;

			removedDbAlleles = null;

			incompleteAlleles = null;

			ambigElementCombos = null;
			ambigGroups = null;
			incompAlleleDto = null;
			ambigComboElementDto = null;
			ambigGroupDto = null;

		}

	} // end insertAmbiguousAlleleSet

	public void insertAlleleSetBatch(List<org.immunogenomics.gl.imgt.xml.model.hla.Allele> alleleList) throws Exception {

		int count = 0;

		// loop over the alleles
		for(org.immunogenomics.gl.imgt.xml.model.hla.Allele allele : alleleList){

			try{

				// insert the current allele
				insertAlleleSet(allele);
				count++;

			}
			catch (RollbackException e) {

				LOGGER.error("AlleleSetDataProcessor.insertAlleleSetBatch:: RollbackException" + e);
				throw e;

			}

		} // end foreach

		LOGGER.info("AlleleSetDataProcessor.insertAlleleSetBatch:: Number of alleles inserted: " + count);

	} // end insertAlleleSetBatch

}