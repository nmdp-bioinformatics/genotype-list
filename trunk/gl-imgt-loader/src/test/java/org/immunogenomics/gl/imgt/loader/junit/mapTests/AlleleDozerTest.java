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
package org.immunogenomics.gl.imgt.loader.junit.mapTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Allele;
import org.immunogenomics.gl.imgt.xml.model.hla.Releaseversions;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AlleleDozerTest {


	// instance of mapper
	Mapper mapper;

	// input variables
	Allele xAllele1;
	Allele xAllele2;
	List<Allele> xAlleleList;



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
		xAllele1 = new Allele();
		xAllele2 = new Allele();
		xAlleleList = new ArrayList<Allele>();

		// output variables
		dbAllele = null;
		dbAlleleList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/

		// initialize a release version
		Releaseversions relVersions = new Releaseversions();
		relVersions.setCurrentrelease("3.5.0");
		relVersions.setFirstreleased("3.5.0");
		relVersions.setLastupdated("3.5.0");

		// initialize the xml Alleles
		xAllele1.setId("HLA0000");
		xAllele1.setDateassigned("1989-08-26"); // Date must be in this format
		xAllele1.setName("A*01:01:01");
		xAllele1.setReleaseversions(relVersions);

		xAllele2.setId("HLA0001");
		xAllele2.setDateassigned("1981-01-08");	// Date must be in this format
		xAllele2.setName("B*01:01:01");
		xAllele2.setReleaseversions(relVersions);

		xAlleleList.add(xAllele1);
		xAlleleList.add(xAllele2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xAllele1 = null;
		xAllele2 = null;
		xAlleleList = null;

		// output variables
		dbAllele = null;
		dbAlleleList = null;

		// instance of class to test
//		xToDb = null;

	}

	/**
	 * Test method
	 */
	@Test
	public void testXmlAlleleToDbAllele() {


		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");


		try{

			dbAllele = mapper.map(xAllele1, org.immunogenomics.gl.imgt.db.model.Allele.class, "AlleleMap");
			assertEquals(dbAllele.getAlleleId(), xAllele1.getId());
			assertEquals(dbAllele.getAlleleName(), xAllele1.getName());

			assertEquals(dbAllele.getReleaseVersion().getReleaseVerCurrentRelease(), xAllele1.getReleaseversions().getCurrentrelease());


		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Allele - Mapping exception thrown",true);
		}


		try{
			Date convertedDate = dateFormat.parse(xAllele1.getDateassigned());
			assertEquals(dbAllele.getAlleleDateAssigned(), convertedDate);
		}
		catch(ParseException e){
			e.printStackTrace();
			assertFalse("Allele - Parse exception on date format",false);
		}

	}




} // end class
