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
package org.immunogenomics.gl.imgt.loader.junit.mapTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbigAllele1;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbigAllele2;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.ExtensionAllele;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.GGroupAllele;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.PartialAllele;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.RemovedAllele;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AmbiguousAllelesDozerTest {


	// instance of mapper
	Mapper mapper;

	// input variables
	PartialAllele partialAllele;
	ExtensionAllele extensionAllele;
	RemovedAllele removedAllele;
	GGroupAllele gGroupAllele;
	AmbigAllele1 ambigAllele1;
	AmbigAllele2 ambigAllele2;




	// output variables
	org.immunogenomics.gl.imgt.db.model.Allele dbAllele;
	List<org.immunogenomics.gl.imgt.db.model.Allele> dbAlleleList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		partialAllele = new PartialAllele();
		extensionAllele = new ExtensionAllele();
		removedAllele = new RemovedAllele();
		gGroupAllele = new GGroupAllele();
		ambigAllele1 = new AmbigAllele1();
		ambigAllele2 = new AmbigAllele2();

		// output variables
		dbAllele = null;
		dbAlleleList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/

		// initialize a release version
//		Releaseversions relVersions = new Releaseversions();
//		relVersions.setCurrentrelease("3.5.0");
//		relVersions.setFirstreleased("3.5.0");
//		relVersions.setLastupdated("3.5.0");

//		PartialAllele pa = new PartialAllele();
//		ExtensionAllele ea = new ExtensionAllele();
//
//		pa.setAlleleid("HLA0000");
//		pa.setName("A*01:01:01");
//
//		ea.setAlleleid("HLA0001");
//		ea.setName("A*02:02:02");

		// initialize the xml Alleles
		partialAllele.setAlleleid("HLA0001");
		partialAllele.setName("A*01:01:01");

//		pa.setAlleleid("HLA0003");
//		pa.setName("A*03:03:03");
//
//		ea.setAlleleid("HLA0004");
//		ea.setName("A*04:04:04");

		extensionAllele.setAlleleid("HLA0002");
		extensionAllele.setName("A*02:02:02");

//		xAlleleList.add(partialAllele);
//		xAlleleList.add(extensionAllele);


		removedAllele.setAlleleid("HLA0003");
		removedAllele.setName("A*03:03:03");

		gGroupAllele.setAlleleid("HLA0004");
		gGroupAllele.setName("A*04:04:04");

		ambigAllele1.setAlleleid("HLA0005");
		ambigAllele1.setName("A*05:05:05");

		ambigAllele2.setAlleleid("HLA0006");
		ambigAllele2.setName("A*06:06:06");

	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		partialAllele = null;
		extensionAllele = null;
		removedAllele = null;
		gGroupAllele = null;
		ambigAllele1 = null;
		ambigAllele2 = null;


		// output variables
		dbAllele = null;

		// instance of class to test
//		xToDb = null;

	}

	/**
	 * Test method
	 */
	@Test
	public void testXmlPartialAlleleToDbAllele() {


		try{

			dbAllele = mapper.map(partialAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "PartialAlleleMap");
			assertEquals(dbAllele.getAlleleId(), partialAllele.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), partialAllele.getName());



		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}


	@Test
	public void testXmlExtensionAlleleToDbAllele() {


		try{

			dbAllele = mapper.map(extensionAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "ExtensionAlleleMap");
			assertEquals(dbAllele.getAlleleId(), extensionAllele.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), extensionAllele.getName());



		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}

	@Test
	public void testXmRemovedAlleleToDbAllele() {

		try{

			dbAllele = mapper.map(removedAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "RemovedAlleleMap");
			assertEquals(dbAllele.getAlleleId(), removedAllele.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), removedAllele.getName());

		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}



	@Test
	public void testXmGGroupAlleleToDbAllele() {

		try{

			dbAllele = mapper.map(gGroupAllele, org.immunogenomics.gl.imgt.db.model.Allele.class, "GGroupAlleleMap");
			assertEquals(dbAllele.getAlleleId(), gGroupAllele.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), gGroupAllele.getName());

		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}

	@Test
	public void testXmAmbigAllele1ToDbAllele() {

		try{

			dbAllele = mapper.map(ambigAllele1, org.immunogenomics.gl.imgt.db.model.Allele.class, "AmbigAllele1Map");
			assertEquals(dbAllele.getAlleleId(), ambigAllele1.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), ambigAllele1.getName());

		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}

	@Test
	public void testXmAmbigAllele2ToDbAllele() {

		try{

			dbAllele = mapper.map(ambigAllele2, org.immunogenomics.gl.imgt.db.model.Allele.class, "AmbigAllele2Map");
			assertEquals(dbAllele.getAlleleId(), ambigAllele2.getAlleleid());
			assertEquals(dbAllele.getAlleleName(), ambigAllele2.getName());

		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}

	}

} // end class
