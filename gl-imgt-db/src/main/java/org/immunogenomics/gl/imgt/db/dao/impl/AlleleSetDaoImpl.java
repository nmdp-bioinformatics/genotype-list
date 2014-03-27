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
package org.immunogenomics.gl.imgt.db.dao.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.RollbackException;

import org.apache.log4j.Logger;
import org.immunogenomics.gl.imgt.db.dao.BaseDao;
import org.immunogenomics.gl.imgt.db.dao.intf.AlleleSetDaoIntf;
import org.immunogenomics.gl.imgt.db.model.Allele;
import org.immunogenomics.gl.imgt.db.model.AlleleRlsVerDtl;
import org.immunogenomics.gl.imgt.db.model.AmbigComboElement;
import org.immunogenomics.gl.imgt.db.model.AmbigComboGroup;
import org.immunogenomics.gl.imgt.db.model.GGroup;
import org.immunogenomics.gl.imgt.db.model.GGroupAllele;
import org.immunogenomics.gl.imgt.db.model.GGrpRlsVerDtl;
import org.immunogenomics.gl.imgt.db.model.IncompleteAllele;
import org.immunogenomics.gl.imgt.db.model.Locus;
import org.immunogenomics.gl.imgt.db.model.ReleaseVersion;
import org.immunogenomics.gl.imgt.db.model.RemovedAllele;
import org.immunogenomics.gl.imgt.db.dto.AmbigComboElementDto;
import org.immunogenomics.gl.imgt.db.dto.AmbigGroupDto;
import org.immunogenomics.gl.imgt.db.dto.GGroupDto;
import org.immunogenomics.gl.imgt.db.dto.IncompleteAlleleDto;

/**
 * @author Adrienne N. Walts (awalts) <awalts@nmdp.org> Operational Bioinformatics, National Marrow Donor Program
 *
 */
public class AlleleSetDaoImpl extends BaseDao implements AlleleSetDaoIntf {

	//	private static final String USER_ID;
	private static final Logger LOGGER = Logger
			.getLogger("dbEventLogger." + AlleleSetDaoImpl.class);

	//	static
	//	{
	//		USER_ID = "imgtuser";
	//		//		DOMConfigurator.configure("src/resources/log4j.xml");
	//
	//
	//	}

	public AlleleSetDaoImpl()
	{
	}

	public void persistAllele(Allele allele){

		EntityManager em;
		ReleaseVersion rv = null;
		Locus loc = null;

		try{

			rv = getReleaseVersionByVersionNumber(allele.getReleaseVersion());
			loc = getLocusByLocusName(allele.getLocus());
		}
		catch(Exception e){
			e.printStackTrace();
		}

		em = getEntityManager();

		try{
			em.getTransaction().begin();

			// persist the release version

			//
			if(rv == null){
				em.persist(allele.getReleaseVersion());

			}
			else{
				allele.setReleaseVersion(rv);
			}

			// persist the locus if necessary
			if(loc == null){
				em.persist(allele.getLocus());
				allele.setLocus(allele.getLocus());
			}
			else{
				allele.setLocus(loc);
			}

			// persist the allele
			em.persist(allele);

			// AlleleRlsVerDtl has two direct parents, so cascade persist
			// doesn't work
			for(AlleleRlsVerDtl x : allele.getAlleleRlsVerDtls()){
				x.setAllele(allele);
				x.setReleaseVersion(allele.getReleaseVersion());
				em.persist(x);
			}

			em.getTransaction().commit();

		}
		catch (RollbackException  ex)
		{

			ex.printStackTrace();

			try
			{
				if (em.getTransaction().isActive())
				{
					em.getTransaction().rollback();

				}
			}
			catch (IllegalStateException  e)
			{
				ex.printStackTrace();
				LOGGER.error("AlleleSetDaoImpl.persistAllele::  IllegalStateException - transaction is NOT active "
						+ e	+ " from RollbackException - commit failed " + ex);
			}

			LOGGER.error("AlleleSetDaoImpl.persistAllele::  RollbackException - commit failed for Allele: "
					+ allele.getAlleleId()  + " " + allele.getAlleleName() + " " + ex);

		}
		catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if(!em.getTransaction().isActive()){
				em.close();
			}

			rv = null;
			allele = null;
			loc = null;

			closeEntityManagerFactory();
		}
	}

	//	@Override
	public void createAmbiguousAlleleSet(Locus locus, ReleaseVersion releaseVer, List<GGroupDto> gGroups,
			List<Allele> removedDbAlleles, List<IncompleteAlleleDto> incompleteAlleles,
			List<AmbigGroupDto> ambigComboGroups){

		EntityManager em1;
		ReleaseVersion rv = null;
		Locus loc = null;
		Allele allele1 = null;
		Allele allele2 = null;
		Allele extensionAllele = null;
		Allele partialAllele = null;
		RemovedAllele removedAllele = null;
		IncompleteAllele incompleteAllele = null;

		GGroup gGroup1 = null;
		GGroup gGroup2 = null;
		GGroupAllele gGroupAllele =  null;
		GGrpRlsVerDtl gGrpRlsVerDtl = null;
		List<Allele> gGroupList = null;
		List<GGroupAllele> gGroupAlleleList = null;
		List<GGrpRlsVerDtl> gGrpRlsVerDtlList = null;

		List<AmbigComboElementDto> ambigComboElementDtoList = null;
		AmbigComboGroup ambigComboGroup = null;
		AmbigComboElement ambigComboElement = null;
		List<AmbigComboElement> ambigComboElementList = null;

		String lastUpdateUserId = "imgt_user";
		Date d = new Date();

		try{

			rv = getReleaseVersionByVersionNumber(releaseVer);
			loc = getLocusByLocusName(locus);
		}
		catch(Exception e){
			e.printStackTrace();
		}

		// TODO: Determine what to do when matching current release is not found
		if (rv == null) {
			LOGGER.error("AlleleSetDaoImpl.createAmbiguousAlleleSet:: ReleaseVersion " +
					releaseVer.getReleaseVerCurrentRelease() + "not found.");
		}
		else {
			em1 = getEntityManager();

			/** Persist RemovedAlleles **/
			for(Allele a : removedDbAlleles){

				allele1 = findAlleleByImgtIdAndVersion(a, rv);
				removedAllele = new RemovedAllele(allele1, loc, rv);
				removedAllele.setCreateDte(d);
				removedAllele.setLastUpdateDte(d);
				removedAllele.setLastUpdateUserId(lastUpdateUserId);

				try{
					em1.getTransaction().begin();
					em1.persist(removedAllele);
					em1.getTransaction().commit();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			} // end for removedDbAlleles

			/** Persist IncompleteAlleles **/
			for(IncompleteAlleleDto iad : incompleteAlleles){

				extensionAllele = findAlleleByImgtIdAndVersion(iad.getExtensionAllele(), rv);
				partialAllele = findAlleleByImgtIdAndVersion(iad.getPartialAllele(), rv);

				incompleteAllele = new IncompleteAllele(partialAllele, extensionAllele, rv);

				incompleteAllele.setCreateDte(d);
				incompleteAllele.setLastUpdateDte(d);
				incompleteAllele.setLastUpdateUserId(lastUpdateUserId);

				try{
					em1.getTransaction().begin();
					em1.persist(incompleteAllele);
					em1.getTransaction().commit();
				}
				catch(Exception e){
					e.printStackTrace();
				}

			} // end for IncompleteAlleleDto

			/** Persist G Groups **/
			for(GGroupDto gDto : gGroups){

				gGroup1 = new GGroup(loc, rv, gDto.getgGroupName(), gDto.getGid());
				gGroupAlleleList = new ArrayList<GGroupAllele>();

				gGroup1.setCreateDte(d);
				gGroup1.setLastUpdateDte(d);
				gGroup1.setLastUpdateUserId(lastUpdateUserId);

				gGroupList = gDto.getgGroup();

				for(Allele a : gGroupList){
					allele1 = findAlleleByImgtIdAndVersion(a, rv);

					gGroupAllele =  new GGroupAllele(gGroup1, allele1);

					gGroupAllele.setCreateDte(d);
					gGroupAllele.setLastUpdateDte(d);
					gGroupAllele.setLastUpdateUserId(lastUpdateUserId);

					gGroupAlleleList.add(gGroupAllele);

				} // end for allele in G Group List

				gGroup1.setGGroupAlleles(gGroupAlleleList);

				gGrpRlsVerDtl = new GGrpRlsVerDtl(loc, rv, gGroup1);

				gGrpRlsVerDtl.setCreateDte(d);
				gGrpRlsVerDtl.setLastUpdateDte(d);
				gGrpRlsVerDtl.setLastUpdateUserId(lastUpdateUserId);

				gGrpRlsVerDtlList = new ArrayList<GGrpRlsVerDtl>();

				gGrpRlsVerDtlList.add(gGrpRlsVerDtl);

				gGroup1.setGGrpRlsVerDtls(gGrpRlsVerDtlList);

				try{
					em1.getTransaction().begin();
					em1.persist(gGroup1);
					em1.getTransaction().commit();
				}
				catch(Exception e){
					e.printStackTrace();
				}

			} // end for GGroupDto

			/** Persist Ambiguous Combinations **/

			for(AmbigGroupDto agDto : ambigComboGroups){

				ambigComboElementList = new ArrayList<AmbigComboElement>();

				ambigComboGroup =  new AmbigComboGroup(agDto.getAmbigSequence(), rv);

				ambigComboGroup.setCreateDte(d);
				ambigComboGroup.setLastUpdateDte(d);
				ambigComboGroup.setLastUpdateUserId(lastUpdateUserId);

				ambigComboElementDtoList = agDto.getAmbigComboElementsList();

				for(AmbigComboElementDto aceDot : ambigComboElementDtoList){

					allele1 = null;
					allele2 = null;

					gGroup1 = null;
					gGroup2 = null;

					// Check for G group allele names
					allele1 = findAlleleByImgtIdAndVersion(aceDot.getAmbigAllele1(), rv);

					if(allele1 == null){

						gGroup1 = findGGroupByAlleleAndVersion(aceDot.getAmbigAllele1(), rv);

					}

					allele2 = findAlleleByImgtIdAndVersion(aceDot.getAmbigAllele2(), rv);

					if(allele2 == null){

						gGroup2 = findGGroupByAlleleAndVersion(aceDot.getAmbigAllele1(), rv);

					}

					ambigComboElement = new AmbigComboElement(ambigComboGroup, allele1, allele2, gGroup1, gGroup2);

					if(gGroup1 == null){
						ambigComboElement.setGGroupPart1Ind("F");
					}
					else{
						ambigComboElement.setGGroupPart1Ind("T");
					}

					if(gGroup2 == null){
						ambigComboElement.setGGroupPart2Ind("F");
					}
					else{
						ambigComboElement.setGGroupPart2Ind("T");
					}

					ambigComboElement.setCreateDte(d);
					ambigComboElement.setLastUpdateDte(d);
					ambigComboElement.setLastUpdateUserId(lastUpdateUserId);

					ambigComboElementList.add(ambigComboElement);

				} // end for AmbigComboElementDto

				ambigComboGroup.setAmbigComboElements(ambigComboElementList);

				try{
					em1.getTransaction().begin();
					em1.persist(ambigComboGroup);
					em1.getTransaction().commit();
				}
				catch(Exception e){
					e.printStackTrace();
				}
			} // end for AmbigGroupDto
		} // end for else ReleaseVersion not null

	} // end createAmbiguousAlleleSet

	@SuppressWarnings("unchecked")
	public GGroup findGGroupByAlleleAndVersion(Allele allele, ReleaseVersion rv){

		GGroup result = null;
		EntityManager em = getEntityManager();
		List<GGroup> resultList = null;

		try{

			Query q = em.createQuery("Select o from GGroup o where o.releaseVersion = ?1 and o.gGroupId = ?2");
			q.setParameter(1, rv);
			q.setParameter(2, allele.getAlleleId());
			resultList = (List<GGroup>)q.getResultList();

			if(resultList.size() == 1){
				result = resultList.get(0);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			em.close();
			//			closeEntityManagerFactory();
		}

		return result;

	}

	@SuppressWarnings("unchecked")
	public Allele findAlleleByImgtIdAndVersion(Allele allele, ReleaseVersion releaseVersion){

		Allele alleleResult= null;
		EntityManager em = getEntityManager();
		List<Allele> resultList = null;

		try{

			Query q = em.createQuery("Select o from Allele o where o.releaseVersion = ?1 and o.alleleId = ?2");
			q.setParameter(1, releaseVersion);
			q.setParameter(2, allele.getAlleleId());
			resultList = (List<Allele>)q.getResultList();

			if(resultList.size() == 1){
				alleleResult = resultList.get(0);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		finally{
			em.close();
			//			closeEntityManagerFactory();
		}

		return alleleResult;

	}

	@SuppressWarnings("unchecked")
	private ReleaseVersion getReleaseVersionByVersionNumber(ReleaseVersion rv){

		ReleaseVersion rver = null;
		List<ReleaseVersion> resultList = null;
		EntityManager em = getEntityManager();

		try{

			Query q = em.createQuery("Select o from ReleaseVersion o where o.releaseVerCurrentRelease = ?1");
			q.setParameter(1, rv.getReleaseVerCurrentRelease());
			resultList = (List<ReleaseVersion>)q.getResultList();

			if(resultList.size() == 1){
				rver = resultList.get(0);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			em.close();
			//			closeEntityManagerFactory();
		}

		return rver;

	}

	@SuppressWarnings("unchecked")
	private Locus getLocusByLocusName(Locus locus){

		Locus loc = null;
		List<Locus> resultList = null;
		EntityManager em = getEntityManager();

		try{

			Query q = em.createQuery("Select o from Locus o where o.locusName = ?1");
			q.setParameter(1, locus.getLocusName());
			resultList = (List<Locus>)q.getResultList();

			if(resultList.size() == 1){
				loc = resultList.get(0);
			}

		}
		catch (Exception e) {
			// TODO: handle exception
		}
		finally{
			em.close();
			//			closeEntityManagerFactory();
		}

		return loc;

	}

	//	private Allele findAllele(String alleleId, String alleleName, String currentRelease){
	//
	//		Allele result = null;
	//		EntityManager em = getEntityManager();
	//
	//		Query query = em.createNamedQuery("findAlleleByAlleleIdAlleleNameAndCurrVersion");
	//		query.setParameter("alleleId", alleleId);
	//		query.setParameter("alleleName", alleleName);
	//		query.setParameter("currentRelease", currentRelease);
	//
	//		try{
	//
	//			result = (Allele)query.getSingleResult();
	//
	//		}
	//		catch (Exception e) {
	//			// TODO: handle exception
	//		}
	//
	//		return result;
	//
	//	}

} // end class
