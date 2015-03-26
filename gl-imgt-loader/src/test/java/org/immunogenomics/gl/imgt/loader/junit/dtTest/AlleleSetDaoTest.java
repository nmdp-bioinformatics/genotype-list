/*

    gl-imgt-loader  IMGT/HLA database loader for the gl project.
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
package org.immunogenomics.gl.imgt.loader.junit.dtTest;




/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AlleleSetDaoTest {

	// TODO: Set up test properly

	/**
	 * @param args
	 */
	public static void main(String[] args) {

//		XmlUnmarshaller x = new XmlUnmarshaller();
//		File xml = new File("../gl-imgt-xml/src/main/resources/org/immunogenomics/gl/imgt/xml/definition/v3_12_0/hla.xml");
//		Alleles alleles = null;
//
//		//		AlleleDataTransfer alleleDT = new AlleleDataTransfer();
//		AlleleSetManager setManager = new AlleleSetManager();
//
//		// conversion classes
//		XAlleleToDbAllele xAlToDb = new XAlleleToDbAllele();
//		XLocusToDbLocus	 xLocToDb = new XLocusToDbLocus();
//		XRelease_VersionToDbRelease_Version xRvToDb = new XRelease_VersionToDbRelease_Version();
//		XCitationToDbCitation xCiteToDb = new XCitationToDbCitation();
//		XAlignmentReferenceToDbAlignmentReference xArToDb = new XAlignmentReferenceToDbAlignmentReference();
//		XSourcexrefToDbSourcexref xSxrfToDb = new XSourcexrefToDbSourcexref();
//		XFeatureToDbFeature xFeatToDb = new XFeatureToDbFeature();
//		XCdnacoordinateToDbCdnacoordinate xCdnaCToDb = new XCdnacoordinateToDbCdnacoordinate();
//		XCdnaindelToDbCdnaindel xIndelToDb = new XCdnaindelToDbCdnaindel();
//		XSequence_coordinateToDbSequence_coordinate xSeqCToDb = new XSequence_coordinateToDbSequence_coordinate();
//		XFeature_TransToDbFeature_Trans xFtToDb = new XFeature_TransToDbFeature_Trans();
//		XNucseqToDbNucseq xNucSeqToDb = new XNucseqToDbNucseq();
//		XSpeciesToDbSpecies xSpecToDb = new XSpeciesToDbSpecies();
//		XEthnicityToDbEthnicity xEthToDb = new XEthnicityToDbEthnicity();
//		XSampleToDbSample xSampleToDb = new XSampleToDbSample();
//
//		// db objects for insert
//		org.immunogenomics.gl.imgt.db.model.Allele1 dbAl = new org.immunogenomics.gl.imgt.db.model.Allele1();
//		AlignmentReference1 dbAr = null;
//		CdnaCoordinate1 dbCdnaC = null;
//		CdnaIndel1 dbIndel = null;
//		Citation1 dbCite = null;
//		Ethnicity1 dbEthnicity = null;
//		Feature1 dbFeature = null;
//		FeatureTranslation1 dbFt = null;
//		Locus1 dbLocus = null;
//		NucSequence1 dbNucSeq = null;
//		ReleaseVersion1 dbRv = null;
//		Sample1 dbSample = null;
//		SequenceCoordinate1 dbSeqC = null;
//		SourceXref1 dbSxref = null;
//		Species1 dbSpecies = null;
//
//		// list objects for insert
//		List<Citation1> dbCiteList = null;
//		List<Feature1> dbFeatureList = null;
//		List<Sample1> dbSampleList = null;
//		List<SourceXref1> dbSxrefList = null;
//
//		Map<Long,CdnaCoordinate1> cdnaCoordMap = new HashMap<Long,CdnaCoordinate1>();
//		Map<Long,CdnaIndel1> cdnaIndelMap = new HashMap<Long,CdnaIndel1>();
//		Map<Long,FeatureTranslation1> featTransMap = new HashMap<Long,FeatureTranslation1>();
//		Map<Long,SequenceCoordinate1> seqCoordMap = new HashMap<Long,SequenceCoordinate1>();
//
//
//
//
//		List<org.immunogenomics.gl.imgt.xml.model.hla.Feature> xFeatureList = null;
//
//
//		try{
//
//			alleles = x.unmarshal(xml);
//
//			List<Allele> xAllelesList = alleles.getAlleleList();
//			int counter = 1;
//
//			for(Allele xAl : xAllelesList){
//
//				System.out.println(counter + ": " + xAl.getId());
//
//				// allele
//				dbAl = xAlToDb.xmlAlleleToDbAllele(xAl);
//
//				// locus
//				dbLocus = xLocToDb.xmlLocusToDbLocus(xAl.getLocus());
//
//				// release version
//				dbRv = xRvToDb.xmlReleaseVersionsToDbReleaseVersions(xAl.getReleaseversions());
//
//				// citation
//				if(xAl.getCitations() != null){
//					dbCite = xCiteToDb.xmlCitationToDbCitation(xAl.getCitations().getCitationList().get(0));
//					dbCiteList = xCiteToDb.xmlCitationListToDbCitationList(xAl.getCitations().getCitationList());
//				}else{
//
//					dbCiteList = null;
//				}
//
//
//				// 5. xref
//				if(xAl.getSourcexrefs() != null){
//					dbSxref = xSxrfToDb.xmlSourcexrefToDbSourcexref(xAl.getSourcexrefs().getXrefList().get(0));
//					dbSxrefList = xSxrfToDb.xmlSourcexrefListToDbSourcexrefList(xAl.getSourcexrefs().getXrefList());
//				}
//				else{
//					dbSxrefList = null;
//				}
//
//				// source material and sub objects
//				if(xAl.getSourcematerial() != null){
//
//					// species
//					dbSpecies = xSpecToDb.xmlSpeciesToDbSpecies(xAl.getSourcematerial().getSpecies());
//
//					// ethnicity
//					dbEthnicity = xEthToDb.xmlEthnicityToDbEthnicity(xAl.getSourcematerial());
//
//					// sample
//					if(xAl.getSourcematerial().getSamples() != null){
//						dbSample = xSampleToDb.xmlSampleToDbSample(xAl.getSourcematerial().getSamples().getSampleList().get(0));
//						dbSampleList = xSampleToDb.xmlSampleListToDbSampleList(xAl.getSourcematerial().getSamples().getSampleList());
//					}
//					else{
//						dbSampleList = null;
//					}
//				} // source material
//
//
//				// sequence sub-objects
//				if(xAl.getSequence() != null){
//
//					// alignement Reference
//					dbAr = xArToDb.xmlAlignmentReferenceToDbAlignmentReference(xAl.getSequence().getAlignmentreference());
//
//					// 10. nuclear sequence
//					dbNucSeq = xNucSeqToDb.xmlNucseqToDbNucseq(xAl.getSequence());
//
//					// feature and sub objects
//					if(xAl.getSequence().getFeature() != null){
//
//						xFeatureList = xAl.getSequence().getFeature();
//
//						// feature
//						//						dbFeature = xFeatToDb.xmlFeatureToDbFeature(xAl.getSequence().getFeature().get(0));
//
//						dbFeatureList = xFeatToDb.xmlFeatureListToDbFeatureList(xFeatureList);
//
//						if(xFeatureList.size() == dbFeatureList.size()){
//
//							for(int i = 0; i < xFeatureList.size(); i++){
//
//								// set a fake id for use in the persistence layer
//								// we need to know which cdnaCoordinate, cdnaIndel, featureTranslation
//								// or sequenceCoordinate goes with which feature when we do the persistence
////								xFeatureList.get(i).setId(i);
//
////								dbFeatureList.get(i).setFeatureIid(i);
//								if(xFeatureList.get(i).getCDNACoordinates() != null){
//									dbCdnaC = xCdnaCToDb.xmlCdnaCoordinateToDbCdnaCoordinate(xFeatureList.get(i).getCDNACoordinates());
//									cdnaCoordMap.put(xFeatureList.get(i).getOrder(), dbCdnaC);
//								}
//
//								// cdna indel
//								if(xFeatureList.get(i).getCDNAindel() != null){
//									dbIndel = xIndelToDb.xmlCdnaindelToDbCdnaindel(xFeatureList.get(i).getCDNAindel());
//									cdnaIndelMap.put(xFeatureList.get(i).getOrder(), dbIndel);
//								}
//
//								// seq coord
//								if(xFeatureList.get(i).getSequenceCoordinates() != null){
//
//									dbSeqC = xSeqCToDb.xmlSequencecoordinateToDbSequencecoordinate(xFeatureList.get(i).getSequenceCoordinates());
//									seqCoordMap.put(xFeatureList.get(i).getOrder(), dbSeqC);
//
//								}
//
//								// 15. feature Translation
//								if(xFeatureList.get(i).getTranslation() != null){
//									dbFt = xFtToDb.xmlFeature_TransToDbFeature_Trans(xFeatureList.get(i));
//									featTransMap.put(xFeatureList.get(i).getOrder(), dbFt);
//								}
//
//
//
//							} // end foreach xml Feature
//
//
//						} // end if xfeaturelist and dbfeaturelist same size
//
//						//						// cdna coord
//						//						if(xAl.getSequence().getFeature().get(0).getCDNACoordinates() != null){
//						//							dbCdnaC = xCdnaCToDb.xmlCdnaCoordinateToDbCdnaCoordinate(xAl.getSequence().getFeature().get(0).getCDNACoordinates());
//						//						}
//						//
//						//						// cdna indel
//						//						if(xAl.getSequence().getFeature().get(0).getCDNAindel() != null){
//						//							dbIndel = xIndelToDb.xmlCdnaindelToDbCdnaindel(xAl.getSequence().getFeature().get(0).getCDNAindel());
//						//						}
//						//
//						//						// seq coord
//						//						if(xAl.getSequence().getFeature().get(0).getSequenceCoordinates() != null){
//						//
//						//							dbSeqC = xSeqCToDb.xmlSequencecoordinateToDbSequencecoordinate(xAl.getSequence().getFeature().get(0).getSequenceCoordinates());
//						//
//						//						}
//						//
//						//						// 15. feature Translation
//						//						if(xAl.getSequence().getFeature().get(0).getTranslation() != null){
//						//							dbFt = xFtToDb.xmlFeature_TransToDbFeature_Trans(xAl.getSequence().getFeature().get(0));
//						//						}
//
//					} // feature
//					else {
//						xFeatureList = null;
//					}
//
//				} // sequence
//
//
//
//				//				setManager.createAlleleSet(dbAl, dbAr, dbCdnaC, dbIndel, dbCite, dbEthnicity, dbFeature, dbFt,
//				//						dbLocus, dbNucSeq, dbRv, dbSample, dbSeqC, dbSxref, dbSpecies);
//
//
//				setManager.createAlleleSet(dbAl, dbAr, cdnaCoordMap, cdnaIndelMap, dbCiteList, dbEthnicity,
//						dbFeatureList, featTransMap, dbLocus, dbNucSeq, dbRv, dbSampleList,
//						seqCoordMap, dbSxrefList, dbSpecies);
//
//
//				System.out.println("inserted allele's table id: " + dbAl.getAlleleIid());
//
//
//				// clear the maps
//				cdnaCoordMap.clear();
//				cdnaIndelMap.clear();
//				featTransMap.clear();
//				seqCoordMap.clear();
//
//				// null the lists
//				dbCiteList = null;
//				dbFeatureList = null;
//				dbSampleList = null;
//				dbSxrefList = null;
//
//				// null the variables
//				dbAl = null;
//				dbAr = null;
//				dbLocus = null;
//				dbNucSeq = null;
//				dbRv = null;
//				dbSpecies = null;
//
//
//
//				counter++;
//			} // end foreach
//
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//		finally{
//
//			x = null;
//			xml = null;
//			alleles = null;
//
//			//			alleleDT
//			setManager = null;
//
//			// conversion classes
//			xAlToDb = null;
//			xLocToDb = null;
//			xRvToDb = null;
//			xCiteToDb = null;
//			xArToDb = null;
//			xSxrfToDb = null;
//			xFeatToDb = null;
//			xCdnaCToDb = null;
//			xIndelToDb = null;
//			xSeqCToDb = null;
//			xFtToDb = null;
//			xNucSeqToDb = null;
//			xSpecToDb = null;
//			xEthToDb = null;
//			xSampleToDb = null;
//
//			// db objects for insert
//			dbAl = null;
//			dbAr = null;
//			dbCdnaC = null;
//			dbIndel = null;
//			dbCite = null;
//			dbEthnicity = null;
//			dbFeature = null;
//			dbFt = null;
//			dbLocus = null;
//			dbNucSeq = null;
//			dbRv = null;
//			dbSample = null;
//			dbSeqC = null;
//			dbSxref = null;
//			dbSpecies = null;
//
//			// list objects for insert
//			dbCiteList = null;
//			dbFeatureList = null;
//			dbSampleList = null;
//			dbSxrefList = null;
//
//			xFeatureList = null;
//
//			cdnaCoordMap = null;
//			cdnaIndelMap = null;
//			featTransMap = null;
//			seqCoordMap = null;
//
//
//
//		}
//		System.out.println("done!");


	} // end main

} // end class
