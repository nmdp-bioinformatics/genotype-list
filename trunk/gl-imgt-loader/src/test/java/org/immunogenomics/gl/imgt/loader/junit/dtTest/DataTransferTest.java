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
package org.immunogenomics.gl.imgt.loader.junit.dtTest;

//import java.io.File;
//import java.util.List;
//
//import org.dozer.DozerBeanMapperSingletonWrapper;
//import org.dozer.Mapper;
//import org.immunogenomics.gl.imgt.xml.model.hla.Allele;
//import org.immunogenomics.gl.imgt.xml.model.hla.Alleles;
//import org.immunogenomics.gl.imgt.xml.parser.XmlUnmarshaller;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class DataTransferTest {

	/**
	 * @author awalts
	 * @param args
	 *
	 * Must comment out either singleton or batch test so as to test
	 * one at a time.  This is a restriction from the database side.
	 *
	 * Must delete any rows added in the database before testing.
	 *
	 */
	public static void main(String[] args) {

		// TODO: set up test properly
//		XmlUnmarshaller x = new XmlUnmarshaller();
//		File xml = new File("../gl-imgt-xml/src/main/resources/org/immunogenomics/gl/imgt/xml/definition/v3_9_0/hla_small.xml");
//		Alleles alleles = null;
//		Mapper mapper = DozerBeanMapperSingletonWrapper.getInstance();
//
//		org.immunogenomics.gl.imgt.db.model.Allele dbAl = new org.immunogenomics.gl.imgt.db.model.Allele();
//
//		try{
//			// unmarshall the xml file
//			alleles = x.unmarshal(xml);
//
//			// get the List<Allele> from alleles
//			List<Allele> xAllelesList = alleles.getAllele();
//
//			/** SINGLETON TEST **/
//			int counter = 1;
//			for(Allele xAl : xAllelesList){
//
//				// print out the current allele
//				System.out.println(counter + ": " + xAl.getId());
//
//				// do the conversion from xml to db
//				dbAl = mapper.map(xAl, org.immunogenomics.gl.imgt.db.model.Allele.class, "AlleleMap");
//
//				// TODO: the insert
//
//				// print the new allele's id in the table to confirm insert
//				System.out.println("inserted allele's table id: " + dbAl.getAlleleIid());
//
//				counter++;
//			} // end foreach
//
//
//			/** BATCH TEST **/
//
//			// list for the db alleles type
//			List<org.immunogenomics.gl.imgt.db.model.Allele> dbAllelesList = null;
//
//			// do the conversion
//			dbAllelesList = alXToDb.xmlAlleleListToDbAlleleList(xAllelesList);
//
//			// do the insert
//			if (dbAllelesList != null){
//				alleleDT.insertAlleleBatch(dbAllelesList);
//			}
//		}
//		catch (Exception e) {
//			e.printStackTrace();
//			// TODO: handle exception
//		}
//
//		System.out.println("done!");
//
	} // end main

} // end class
