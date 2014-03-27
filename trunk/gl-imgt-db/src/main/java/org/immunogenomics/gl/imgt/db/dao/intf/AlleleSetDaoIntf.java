/*

    gl-imgt-db  IMGT/HLA database persistence domain and data access objects for the gl project.
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
package org.immunogenomics.gl.imgt.db.dao.intf;

import java.util.List;

import org.immunogenomics.gl.imgt.db.model.Allele;
import org.immunogenomics.gl.imgt.db.model.Locus;
import org.immunogenomics.gl.imgt.db.model.ReleaseVersion;
import org.immunogenomics.gl.imgt.db.dto.AmbigGroupDto;
import org.immunogenomics.gl.imgt.db.dto.GGroupDto;
import org.immunogenomics.gl.imgt.db.dto.IncompleteAlleleDto;

/**
 *
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public interface AlleleSetDaoIntf {

	/**
	 * @author awalts
	 * @param releaseVer
	 * @param ggroupMap
	 * @param removedDbAlleles
	 * @param incompleteAlleles
	 * @param ambigComboGroups
	 */
	void createAmbiguousAlleleSet(Locus locus, ReleaseVersion releaseVer,
			List<GGroupDto> gGroups, List<Allele> removedDbAlleles,
			List<IncompleteAlleleDto> incompleteAlleles,
			List<AmbigGroupDto> ambigComboGroups);

	Allele findAlleleByImgtIdAndVersion(Allele alleleImgtId, ReleaseVersion releaseVersion);

//	void persistAllele(Allele allele, Locus locus, ReleaseVersion rv);

	void persistAllele(Allele allele);

} // end interface
