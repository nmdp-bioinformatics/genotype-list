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

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Citation;
import org.immunogenomics.gl.imgt.xml.model.hla.Releaseversions;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class CitationDozerTest {

	// the mapping object
	Mapper mapper;

	// input variables
	Citation xCitation1;
	Citation xCitation2;
	List<Citation> xCitationList;



	// output variables
	org.immunogenomics.gl.imgt.db.model.Citation dbCitation;
	List<org.immunogenomics.gl.imgt.db.model.Citation> dbCitationList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {

		// add the mapping file to the list for the mapper.
//		List<String> myMappingFiles = new ArrayList<String>();
//		myMappingFiles.add("XmlToDb_mapping.xml");

		// input instances
		xCitation1 = new Citation();
		xCitation2 = new Citation();
		xCitationList = new ArrayList<Citation>();

		// output variables
		dbCitation = null;
		dbCitationList = null;

		// instance of class to test
		//			xToDb = new XCitationToDbCitation();
//		mapper = new DozerBeanMapper(myMappingFiles);
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/

		// initialize a release version
		Releaseversions relVersions = new Releaseversions();
		relVersions.setCurrentrelease("3.5.0");
		relVersions.setFirstreleased("3.5.0");
		relVersions.setLastupdated("3.5.0");

		// initialize the xml Citations
		xCitation1.setAuthors("author1, author2");
		xCitation1.setLocation("location 1");
		xCitation1.setPubmed(new BigInteger("1234"));
		xCitation1.setTitle("title 1");


		xCitation2.setAuthors("author3, author4");
		xCitation2.setLocation("location 2");
		xCitation2.setPubmed(new BigInteger("5678"));
		xCitation2.setTitle("title 2");

		xCitationList.add(xCitation1);
		xCitationList.add(xCitation2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xCitation1 = null;
		xCitation2 = null;
		xCitationList = null;

		// output variables
		dbCitation = null;
		dbCitationList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlCitationToDbCitation() {

		try{

			dbCitation = mapper.map(xCitation1, org.immunogenomics.gl.imgt.db.model.Citation.class, "CitationMap");
			assertEquals(dbCitation.getAuthors(), xCitation1.getAuthors());
			assertEquals(dbCitation.getCitationLocation(), xCitation1.getLocation());
			assertEquals(dbCitation.getPubMed(), xCitation1.getPubmed());
			assertEquals(dbCitation.getTitle(), xCitation1.getTitle());



		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Citation - Mapping exception thrown",true);
		}

	}

} // end class
