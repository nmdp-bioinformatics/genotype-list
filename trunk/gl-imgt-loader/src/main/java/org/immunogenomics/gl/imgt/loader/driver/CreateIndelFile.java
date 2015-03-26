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
package org.immunogenomics.gl.imgt.loader.driver;

import java.util.List;

import org.immunogenomics.gl.imgt.db.dao.impl.IndelDaoImpl;
import org.immunogenomics.gl.imgt.db.dto.IndelDto;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class CreateIndelFile {

	/**
	 * @author awalts
	 * @param args
	 */
	public static void main(String[] args) {

		IndelDaoImpl indelDao = new IndelDaoImpl();
		List<IndelDto> indelList = null;

		indelList = indelDao.getIndels();

		for(IndelDto dto : indelList){
			System.out.println(dto.getAlleleId());
		}

		System.out.println();

	}

}
