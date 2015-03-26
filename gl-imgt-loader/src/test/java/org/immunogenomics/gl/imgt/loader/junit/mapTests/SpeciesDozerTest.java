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

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Species;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class SpeciesDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -2414841264064040843L;


	// the mapping object
	Mapper mapper;

	// input variables
	Species xSpecies1;
	Species xSpecies2;
	List<Species> xSpeciesList;



	// output variables
	org.immunogenomics.gl.imgt.db.model.Species dbSpecies;
	List<org.immunogenomics.gl.imgt.db.model.Species> dbSpeciesList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		xSpecies1 = new Species();
		xSpecies2 = new Species();
		xSpeciesList = new ArrayList<Species>();

		// output variables
		dbSpecies = null;
		dbSpeciesList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/


		// initialize the xml Speciess
		xSpecies1.setCommonname("common 1");
		xSpecies1.setLatinname("Latin 1");
		xSpecies1.setNcbitaxon(new BigInteger("1234"));

		xSpecies2.setCommonname("common 2");
		xSpecies2.setLatinname("Latin 2");
		xSpecies2.setNcbitaxon(new BigInteger("5678"));

		xSpeciesList.add(xSpecies1);
		xSpeciesList.add(xSpecies2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xSpecies1 = null;
		xSpecies2 = null;
		xSpeciesList = null;

		// output variables
		dbSpecies = null;
		dbSpeciesList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlSpeciesToDbSpecies() {

		try{

			dbSpecies = mapper.map(xSpecies1, org.immunogenomics.gl.imgt.db.model.Species.class, "SpeciesMap");
			assertEquals(dbSpecies.getCommonName(), xSpecies1.getCommonname());
			assertEquals(dbSpecies.getLatinName(), xSpecies1.getLatinname());
			assertEquals(dbSpecies.getNcbiTaxon(), xSpecies1.getNcbitaxon());



		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("Species - Mapping exception thrown",true);
		}

	}


} // end class
