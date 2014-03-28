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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Xref;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class SourceXrefDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = 5494590814905098868L;

	// the mapping object
	Mapper mapper;

	// input variables
	Xref xXref1;
	Xref xXref2;
	List<Xref> xXrefList;



	// output variables
	org.immunogenomics.gl.imgt.db.model.SourceXref dbSourceXref;
	List<org.immunogenomics.gl.imgt.db.model.SourceXref> dbSourceXrefList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		xXref1 = new Xref();
		xXref2 = new Xref();
		xXrefList = new ArrayList<Xref>();

		// output variables
		dbSourceXref = null;
		dbSourceXrefList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/


		// initialize the xml SourceXrefs
		xXref1.setAcc("acc 1");
		xXref1.setPid("pid 1");

		xXref2.setAcc("acc 2");
		xXref2.setPid("pid 2");

		xXrefList.add(xXref1);
		xXrefList.add(xXref2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xXref1 = null;
		xXref2 = null;
		xXrefList = null;

		// output variables
		dbSourceXref = null;
		dbSourceXrefList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlSourceXrefToDbSourceXref() {

		try{

			dbSourceXref = mapper.map(xXref1, org.immunogenomics.gl.imgt.db.model.SourceXref.class, "SourceXrefMap");
			assertEquals(dbSourceXref.getXref(), xXref1.getAcc());
			assertEquals(dbSourceXref.getPid(), xXref1.getPid());


		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("SourceXref - Mapping exception thrown",true);
		}

	}


} // end class
