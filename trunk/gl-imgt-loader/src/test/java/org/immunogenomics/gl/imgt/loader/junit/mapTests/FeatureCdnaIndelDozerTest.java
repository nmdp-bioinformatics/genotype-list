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
import org.immunogenomics.gl.imgt.xml.model.hla.CDNAindel;
import org.immunogenomics.gl.imgt.xml.model.hla.Feature;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class FeatureCdnaIndelDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -3338230011470389103L;

	// the mapping object
	Mapper mapper;

	// input variables
	Feature xFeature1;
	Feature xFeature2;
	List<Feature> xFeatureList;
	CDNAindel cdnaIndel;


	// output variables
	org.immunogenomics.gl.imgt.db.model.CdnaIndel dbCdnaIndel;
	List<org.immunogenomics.gl.imgt.db.model.CdnaIndel> dbCdnaIndelList;



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
		cdnaIndel = new CDNAindel();

		// output variables
		dbCdnaIndel = null;
		dbCdnaIndelList = null;

		// instance of class to test
		mapper = DozerBeanMapperSingletonWrapper.getInstance();

		/** initialization of input instances **/


		// initialize the xml Features
		cdnaIndel.setEnd(new BigInteger("10"));
		cdnaIndel.setStart(new BigInteger("1"));
		cdnaIndel.setExpressed("Y");
		cdnaIndel.setSize(new BigInteger("2"));
		cdnaIndel.setType("type 1");


		xFeature1.setCDNAindel(cdnaIndel);

		xFeature2.setCDNAindel(cdnaIndel);

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
		dbCdnaIndel = null;
		dbCdnaIndelList = null;

	}


	/**
	 * Test method
	 */
	@Test
	public void testXmlFeatureToDbSeqCoord() {

		try{

			dbCdnaIndel = mapper.map(xFeature1, org.immunogenomics.gl.imgt.db.model.CdnaIndel.class, "FeatureCDNAindelMap");
			assertEquals(dbCdnaIndel.getCdnaIndelStart(), xFeature1.getCDNAindel().getStart());
			assertEquals(dbCdnaIndel.getCdnaIndelEnd(), xFeature1.getCDNAindel().getEnd());
			assertEquals(dbCdnaIndel.getCdnaIndelExpressed(), xFeature1.getCDNAindel().getExpressed());
			assertEquals(dbCdnaIndel.getCdnaIndelSize(), xFeature1.getCDNAindel().getSize());
			assertEquals(dbCdnaIndel.getCdnaIndelType(), xFeature1.getCDNAindel().getType());


		}
		catch(MappingException e){
			e.printStackTrace();
			assertFalse("FeatureSeqCoord - Mapping exception thrown",true);
		}

	}

} // end class

