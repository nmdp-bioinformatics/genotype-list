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

import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Locus;
import org.immunogenomics.gl.imgt.xml.model.hla.Releaseversions;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class LocusDozerTest {

	// the mapping object
	Mapper mapper;

	// input variables
	Locus xLocus1;
	Locus xLocus2;
	List<Locus> xLocusList;



	// output variables
	org.immunogenomics.gl.imgt.db.model.Locus dbLocus;
	List<org.immunogenomics.gl.imgt.db.model.Locus> dbLocusList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		xLocus1 = new Locus();
		xLocus2 = new Locus();
		xLocusList = new ArrayList<Locus>();

		// output variables
		dbLocus = null;
		dbLocusList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/

		// initialize a release version
		Releaseversions relVersions = new Releaseversions();
		relVersions.setCurrentrelease("3.5.0");
		relVersions.setFirstreleased("3.5.0");
		relVersions.setLastupdated("3.5.0");

		// initialize the xml Locuss
		xLocus1.setClazz("I");
		xLocus1.setGenesystem("HLA");
		xLocus1.setLocusname("A");

		xLocus2.setClazz("II");
		xLocus2.setGenesystem("HLA");
		xLocus2.setLocusname("DRB");

		xLocusList.add(xLocus1);
		xLocusList.add(xLocus2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xLocus1 = null;
		xLocus2 = null;
		xLocusList = null;

		// output variables
		dbLocus = null;
		dbLocusList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlLocusToDbLocus() {

		try{

			dbLocus = mapper.map(xLocus1, org.immunogenomics.gl.imgt.db.model.Locus.class, "LocusMap");
			assertEquals(dbLocus.getClass_(), xLocus1.getClazz());
			assertEquals(dbLocus.getGeneSystem(), xLocus1.getGenesystem());
			assertEquals(dbLocus.getLocusName(), xLocus1.getLocusname());


		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Locus - Mapping exception thrown",true);
		}

	}

} // end class

