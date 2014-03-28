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
import org.immunogenomics.gl.imgt.xml.model.hla.Feature;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class FeatureDozerTest implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = -3090988833669794771L;

	// the mapping object
			Mapper mapper;

			// input variables
			Feature xFeature1;
			Feature xFeature2;
			List<Feature> xFeatureList;



			// output variables
			org.immunogenomics.gl.imgt.db.model.Feature dbFeature;
			List<org.immunogenomics.gl.imgt.db.model.Feature> dbFeatureList;



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

				// output variables
				dbFeature = null;
				dbFeatureList = null;

				// instance of class to test
				mapper = DozerBeanMapperSingletonWrapper.getInstance();

				/** initialization of input instances **/


				// initialize the xml Features
				xFeature1.setName("name 1");
				xFeature1.setOrder(new BigInteger("1"));
				xFeature1.setFeaturetype("feature type 1");
				xFeature1.setStatus("confirmed");

				xFeature2.setName("name 2");
				xFeature2.setOrder(new BigInteger("2"));
				xFeature2.setFeaturetype("feature type 2");
				xFeature2.setStatus("confirmed");

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
				dbFeature = null;
				dbFeatureList = null;

			}


			/**
			 * Test method
			 */
			@Test
			public void testXmlFeatureToDbFeature() {

				try{

					dbFeature = mapper.map(xFeature1, org.immunogenomics.gl.imgt.db.model.Feature.class, "FeatureMap");
					assertEquals(dbFeature.getFeatureName(), xFeature1.getName());
					assertEquals(dbFeature.getFeatureOrder(), xFeature1.getOrder());
					assertEquals(dbFeature.getFeatureStatus(), xFeature1.getStatus());
					assertEquals(dbFeature.getFeatureType(), xFeature1.getFeaturetype());

				}
				catch(MappingException e){
					e.printStackTrace();
					assertFalse("Feature - Mapping exception thrown",true);
				}

			}



} // end class
