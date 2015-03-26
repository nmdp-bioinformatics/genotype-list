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
import org.immunogenomics.gl.imgt.xml.model.hla.Samples;
import org.immunogenomics.gl.imgt.xml.model.hla.Sourcematerial;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class SourceMaterialDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -5084494010142820818L;


	// the mapping object
		Mapper mapper;

		// input variables
		Sourcematerial xSourcematerial1;
		Sourcematerial xSourcematerial2;
		List<Sourcematerial> xSourcematerialList;
		Samples samples;
		List<Sample> sampleList;



		// output variables
		org.immunogenomics.gl.imgt.db.model.SourceMaterial dbSourceMaterial;
		List<org.immunogenomics.gl.imgt.db.model.SourceMaterial> dbSourceMaterialList;



		/**
		 * @author awalts
		 * @throws java.lang.Exception
		 */
		@Before
		public void setUp() throws Exception {


			// input instances
			xSourcematerial1 = new Sourcematerial();
			xSourcematerial2 = new Sourcematerial();
			xSourcematerialList = new ArrayList<Sourcematerial>();

			samples = new Samples();
			samples.getSample().add(new Sample());

			// output variables
			dbSourceMaterial = null;
			dbSourceMaterialList = null;

			// instance of class to test
			mapper = DozerBeanMapperSingletonWrapper.getInstance();

			/** initialization of input instances **/

			// initialize the xml SourceMaterials
			xSourcematerial1.setSamples(samples);

			xSourcematerial2.setSamples(samples);

			xSourcematerialList.add(xSourcematerial1);
			xSourcematerialList.add(xSourcematerial2);


		}

		/**
		 * @author awalts
		 * @throws java.lang.Exception
		 */
		@After
		public void tearDown() throws Exception {

			/** reset all instances and variables **/

			// input variables
			xSourcematerial1 = null;
			xSourcematerial2 = null;
			xSourcematerialList = null;

			// output variables
			dbSourceMaterial = null;
			dbSourceMaterialList = null;

		}


		/**
		 * Test method
		 */
		@Test
		public void testXmlSourcematerialToDbSourceMaterial() {

			try{

				dbSourceMaterial = mapper.map(xSourcematerial1, org.immunogenomics.gl.imgt.db.model.SourceMaterial.class, "SourceMaterialMap");
				if (dbSourceMaterial.getSamples().size() > 0 && xSourcematerial1.getSamples().getSample().size() > 0) {
					org.immunogenomics.gl.imgt.db.model.Sample dbSample = dbSourceMaterial.getSamples().get(0);
					org.immunogenomics.gl.imgt.xml.model.hla.Sample xSample = xSourcematerial1.getSamples().getSample().get(0);
					assertEquals(dbSample.getSampleName(), xSample.getName());
				}
			}
			catch(MappingException e){
				e.printStackTrace();
				assertFalse("Sourcematerial - Mapping exception thrown",true);
			}

		}

} // end class
