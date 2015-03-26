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
import java.util.ArrayList;
import java.util.List;

import org.dozer.DozerBeanMapperSingletonWrapper;
import org.dozer.Mapper;
import org.dozer.MappingException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.immunogenomics.gl.imgt.xml.model.hla.Sample;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class SampleDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -8672128398241950589L;

	// the mapping object
		Mapper mapper;

		// input variables
		Sample xSample1;
		Sample xSample2;
		List<Sample> xSampleList;



		// output variables
		org.immunogenomics.gl.imgt.db.model.Sample dbSample;
		List<org.immunogenomics.gl.imgt.db.model.Sample> dbSampleList;



		/**
		 * @author awalts
		 * @throws java.lang.Exception
		 */
		@Before
		public void setUp() throws Exception {


			// input instances
			xSample1 = new Sample();
			xSample2 = new Sample();
			xSampleList = new ArrayList<Sample>();

			// output variables
			dbSample = null;
			dbSampleList = null;

			// instance of class to test
			mapper = DozerBeanMapperSingletonWrapper.getInstance();

			/** initialization of input instances **/


			// initialize the xml Samples
			xSample1.setName("name 1");

			xSample2.setName("name 2");

			xSampleList.add(xSample1);
			xSampleList.add(xSample2);


		}

		/**
		 * @author awalts
		 * @throws java.lang.Exception
		 */
		@After
		public void tearDown() throws Exception {

			/** reset all instances and variables **/

			// input variables
			xSample1 = null;
			xSample2 = null;
			xSampleList = null;

			// output variables
			dbSample = null;
			dbSampleList = null;

		}


		/**
		 * Test method
		 */
		@Test
		public void testXmlSampleToDbSample() {

			try{

				dbSample = mapper.map(xSample1, org.immunogenomics.gl.imgt.db.model.Sample.class, "SampleMap");
				assertEquals(dbSample.getSampleName(), xSample1.getName());

			}
			catch(MappingException e){
				e.printStackTrace();
				assertFalse("Sample - Mapping exception thrown",true);
			}

		}

} // end class
