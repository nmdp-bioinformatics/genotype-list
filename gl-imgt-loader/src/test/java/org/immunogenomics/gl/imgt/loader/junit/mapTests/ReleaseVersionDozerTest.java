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
import org.immunogenomics.gl.imgt.xml.model.hla.Releaseversions;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class ReleaseVersionDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -1006694045223517587L;

	// the mapping object
	Mapper mapper;

	// input variables
	Releaseversions xReleaseVersions1;
	Releaseversions xReleaseVersions2;
	List<Releaseversions> xReleaseVersionsList;



	// output variables
	org.immunogenomics.gl.imgt.db.model.ReleaseVersion dbReleaseVersion;
	List<org.immunogenomics.gl.imgt.db.model.ReleaseVersion> dbReleaseVersionList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		xReleaseVersions1 = new Releaseversions();
		xReleaseVersions2 = new Releaseversions();
		xReleaseVersionsList = new ArrayList<Releaseversions>();

		// output variables
		dbReleaseVersion = null;
		dbReleaseVersionList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/


		// initialize the xml ReleaseVersions
		xReleaseVersions1.setCurrentrelease("3.5.0");
		xReleaseVersions1.setFirstreleased("1.0.0");
		xReleaseVersions1.setLastupdated("3.4.0");

		xReleaseVersions2.setCurrentrelease("3.5.0");
		xReleaseVersions2.setFirstreleased("1.4.0");
		xReleaseVersions2.setLastupdated("3.2.0");

		xReleaseVersionsList.add(xReleaseVersions1);
		xReleaseVersionsList.add(xReleaseVersions2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xReleaseVersions1 = null;
		xReleaseVersions2 = null;
		xReleaseVersionsList = null;

		// output variables
		dbReleaseVersion = null;
		dbReleaseVersionList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlReleaseversionsToDbSample() {

		try{

			dbReleaseVersion = mapper.map(xReleaseVersions1, org.immunogenomics.gl.imgt.db.model.ReleaseVersion.class, "ReleaseVersionsMap");
//			assertEquals(dbReleaseVersion.getReleaseVerConfirmedStatus(), xReleaseVersions1.getConfirmed());
			assertEquals(dbReleaseVersion.getReleaseVerCurrentRelease(), xReleaseVersions1.getCurrentrelease());
//			assertEquals(dbReleaseVersion.getReleaseVerFirstReleased(), xReleaseVersions1.getFirstreleased());
//			assertEquals(dbReleaseVersion.getReleaseVerLastUpdated(), xReleaseVersions1.getLastupdated());
//			assertEquals(dbReleaseVersion.getReleaseVerStatus(), xReleaseVersions1.getReleasestatus());

		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("ReleaseVersion - Mapping exception thrown",true);
		}

	}

} // end class

