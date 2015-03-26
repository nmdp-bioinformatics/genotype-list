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
import org.immunogenomics.gl.imgt.xml.model.hla.Sequence;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class SequenceNucSeqDozerTest implements Serializable {

	/**
	 *
	 */
	private static final long serialVersionUID = -4860978205636422879L;


	// the mapping object
			Mapper mapper;

			// input variables
			Sequence xSequence1;
			Sequence xSequence2;
			List<Sequence> xSequenceList;



			// output variables
			org.immunogenomics.gl.imgt.db.model.NucSequence dbNucSeq;
			List<org.immunogenomics.gl.imgt.db.model.NucSequence> dbNucSeqList;



			/**
			 * @author awalts
			 * @throws java.lang.Exception
			 */
			@Before
			public void setUp() throws Exception {


				// input instances
				xSequence1 = new Sequence();
				xSequence2 = new Sequence();
				xSequenceList = new ArrayList<Sequence>();

				// output variables
				dbNucSeq = null;
				dbNucSeqList = null;

				// instance of class to test
				mapper = DozerBeanMapperSingletonWrapper.getInstance();

				/** initialization of input instances **/


				// initialize the xml Sequences
				xSequence1.setNucsequence("ATTAC");

				xSequence2.setNucsequence("GGATC");

				xSequenceList.add(xSequence1);
				xSequenceList.add(xSequence2);


			}

			/**
			 * @author awalts
			 * @throws java.lang.Exception
			 */
			@After
			public void tearDown() throws Exception {

				/** reset all instances and variables **/

				// input variables
				xSequence1 = null;
				xSequence2 = null;
				xSequenceList = null;

				// output variables
				dbNucSeq = null;
				dbNucSeqList = null;

			}


			/**
			 * Test method
			 */
			@Test
			public void testXmlSequenceToDbNucSequence() {

				try{

					dbNucSeq = mapper.map(xSequence1, org.immunogenomics.gl.imgt.db.model.NucSequence.class, "SequenceNucSeqMap");
					assertEquals(dbNucSeq.getNucSequence(), xSequence1.getNucsequence());

				}
				catch(MappingException e){
					e.printStackTrace();
					assertFalse("Sequence to NucSequence - Mapping exception thrown",true);
				}

			}

} // end class
