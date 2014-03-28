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
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.CDNACoordinates;
import org.immunogenomics.gl.imgt.xml.model.hla.Feature;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class FeatureCdnaCoordDozerTest implements Serializable {



	/**
	 *
	 */
	private static final long serialVersionUID = 4895956930375354513L;

	// the mapping object
	Mapper mapper;

	// input variables
	Feature xFeature1;
	Feature xFeature2;
	List<Feature> xFeatureList;
	CDNACoordinates cdnaCoord;


	// output variables
	org.immunogenomics.gl.imgt.db.model.CdnaCoordinate dbCdnaCoord;
	List<org.immunogenomics.gl.imgt.db.model.CdnaCoordinate> dbCdnaCoordList;



	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {


		// input instances
		xFeature1 = new Feature();
		xFeature2 = new Feature();
		xFeatureList = new ArrayList<Feature>();
		cdnaCoord = new CDNACoordinates();

		// output variables
		dbCdnaCoord = null;
		dbCdnaCoordList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/


		// initialize the xml Features
		cdnaCoord.setEnd(new BigInteger("10"));
		cdnaCoord.setStart(new BigInteger("1"));
		cdnaCoord.setReadingframe(new BigInteger("3"));

		xFeature1.setCDNACoordinates(cdnaCoord);

		xFeature2.setCDNACoordinates(cdnaCoord);

		xFeatureList.add(xFeature1);
		xFeatureList.add(xFeature2);


	}

	/**
	 * @author awalts
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {

		/** reset all instances and variables **/

		// input variables
		xFeature1 = null;
		xFeature2 = null;
		xFeatureList = null;

		// output variables
		dbCdnaCoord = null;
		dbCdnaCoordList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlFeatureToDbCdnaCoord() {

		try{

			dbCdnaCoord = mapper.map(xFeature1, org.immunogenomics.gl.imgt.db.model.CdnaCoordinate.class, "FeatureCDNACoordMap");
			assertEquals(dbCdnaCoord.getCdnaCoordinateStart(), xFeature1.getCDNACoordinates().getStart());
			assertEquals(dbCdnaCoord.getCdnaCoordinateEnd(), xFeature1.getCDNACoordinates().getEnd());
			assertEquals(dbCdnaCoord.getCdnaCoordinateReadingFrame(), xFeature1.getCDNACoordinates().getReadingframe());


		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("FeatureSeqCoord - Mapping exception thrown",true);
		}

	}

} // end class

