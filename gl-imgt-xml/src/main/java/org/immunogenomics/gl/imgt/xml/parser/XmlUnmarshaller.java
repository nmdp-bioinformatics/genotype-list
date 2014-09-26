/*

    gl-imgt-xml  IMGT/HLA database HLA alleles XML schema bindings and unmarshaller for the gl project.
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
package org.immunogenomics.gl.imgt.xml.parser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
//import org.apache.log4j.xml.DOMConfigurator;
import org.immunogenomics.gl.imgt.xml.model.hla.ambig.AmbiguityData;
import org.immunogenomics.gl.imgt.xml.model.hla.Alleles;

/**
 * @author awalts Adrienne N. Walts <awalts@nmdp.org> (National Marrow Donor Program)
 *
 */
public class XmlUnmarshaller {

	private static final Logger LOGGER = LoggerFactory
            .getLogger("xmlEventLogger." + XmlUnmarshaller.class);

//	static
//	{
//		DOMConfigurator.configure("src/main/resources/log4j.xml");
//	}

	/**
	 * unmarshalAlleles takes an xml file, the root element of which
	 * is an org.immunogenomics.gl.imgt.xml.model.hla.Alleles object.  It
	 * unmarshalls the XML in an Alleles object.
	 *
	 *
	 * @author awalts
	 * @param xml
	 * @return the unmarshalled Alleles object
	 * @throws JAXBException
	 */
	public Alleles unmarshalAlleles(File xml) throws JAXBException {

		LOGGER.debug("Start::XmlUnmarshaller.unmarshalAlleles");

		Alleles results = null;
		JAXBContext jc = null;
		Unmarshaller u = null;

		try {
			jc = JAXBContext.newInstance("org.immunogenomics.gl.imgt.xml.model.hla");
			u = jc.createUnmarshaller();

//			If we want to print the file to the console
//			System.out.println(xmlFileToString(xml));

			// unmarshal
			Object o = u.unmarshal(xml);

			if (o instanceof Alleles) {
				results = (Alleles)o;
			}
			else {
				LOGGER.warn("XmlUnmarshaller.unmarshalAlleles:: The JAXBElement is not an instanceof Alleles.");
			}
		}
		catch (JAXBException e) {
			LOGGER.error("XmlUnmarshaller.unmarshalAlleles:: JAXBException " + e);
			throw e;
		}
		finally {
			 jc = null;
			 u = null;
		}

		LOGGER.debug("End::XmlUnmarshaller.unmarshalAlleles");

		return results;

	} // end unmarshall

	public AmbiguityData unmarshalAmbiguousAlleles(File xml) throws JAXBException {

		LOGGER.debug("Start::XmlUnmarshaller.unmarshalAmbiguousAlleles");

		AmbiguityData results = null;
		JAXBContext jc = null;
		Unmarshaller u = null;

		try {
			jc = JAXBContext.newInstance("org.immunogenomics.gl.imgt.xml.model.hla.ambig");
			u = jc.createUnmarshaller();

//			If we want to print the file to the console
//			System.out.println(xmlFileToString(xml));

			// unmarshal
			Object o = u.unmarshal(xml);

			if (o instanceof AmbiguityData) {
				results = (AmbiguityData)o;
			}
			else {
				LOGGER.warn("XmlUnmarshaller.unmarshalAmbiguousAlleles:: The JAXBElement is not an instanceof AmbiguityData.");
			}
		}
		catch (JAXBException e) {
			LOGGER.error("XmlUnmarshaller.unmarshalAmbiguousAlleles:: JAXBException " + e);
			throw e;
		}
		finally {
			 jc = null;
			 u = null;
		}

		LOGGER.debug("End::XmlUnmarshaller.unmarshalAmbiguousAlleles");

		return results;

	} // end unmarshall

	/**
	 * xmlInputStreamToString converts a FileInputStream object
	 * to a String object so it can be printed.
	 *
	 * @author awalts
	 * @param xml
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unused")
	private String xmlFileToString(File xml) throws FileNotFoundException {

		StringBuilder data = new StringBuilder();
		Scanner scanner = null;

		try {
			scanner = new Scanner(xml);

			while (scanner.hasNextLine()){

				data.append(scanner.nextLine());

			} // end while
		}
		catch(FileNotFoundException e) {
			LOGGER.error("XmlUnmarshaller.xmlFileToString:: FileNotFoundException " + e);
		}
		finally {
			scanner.close();
		}

		return data.toString();

	} // end xmlInputStreamToString

} // end class
