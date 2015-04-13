/*

    gl-liftover-service  Genotype list liftover service.
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
package org.nmdp.gl.liftover.impl;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutionException;

import com.google.common.cache.LoadingCache;

import com.google.common.collect.Table;

import com.google.inject.Inject;

import org.nmdp.gl.Allele;
import org.nmdp.gl.AlleleList;
import org.nmdp.gl.Genotype;
import org.nmdp.gl.GenotypeList;
import org.nmdp.gl.Haplotype;
import org.nmdp.gl.Locus;
import org.nmdp.gl.MultilocusUnphasedGenotype;

import org.nmdp.gl.client.GlClient;
import org.nmdp.gl.client.GlClientException;

import org.nmdp.gl.liftover.AlleleNames;
import org.nmdp.gl.liftover.LiftoverService;
import org.nmdp.gl.liftover.LiftoverServiceException;
import org.nmdp.gl.liftover.LocusNames;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Genotype list liftover service implementation.
 */
final class LiftoverServiceImpl implements LiftoverService {
    /** Cache of gl clients keyed by namespace. */
    private final LoadingCache<String, GlClient> clients;

    /** Table of locus names keyed by namespace and glstring. */
    private final Table<String, String, String> locusNames;

    /** Table of allele names keyed by namespace and accession number. */
    private final Table<String, String, String> alleleNames;

    /** Logger. */
    private final Logger logger = LoggerFactory.getLogger(LiftoverServiceImpl.class);


    /**
     * Create a new genotype list liftover service implementation.
     *
     * @param clients clients, must not be null
     * @param locusNames locus names, must not be null
     * @param alleleNames allele names, must not be null
     */
    @Inject
    LiftoverServiceImpl(final LoadingCache<String, GlClient> clients,
                        @LocusNames final Table<String, String, String> locusNames,
                        @AlleleNames final Table<String, String, String> alleleNames) {
        checkNotNull(clients);
        checkNotNull(locusNames);
        checkNotNull(alleleNames);
        this.clients = clients;
        this.locusNames = locusNames;
        this.alleleNames = alleleNames;
    }


    @Override
    public Locus liftoverLocus(final String sourceNamespace, final String sourceLocusUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceLocusUri);
        checkNotNull(targetNamespace);

        String targetLocusName = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            Locus sourceLocus = source.getLocus(sourceLocusUri);
            if (sourceLocus == null) {
                throw new LiftoverServiceException("could not find source locus with URI " + sourceLocusUri);
            }

            targetLocusName = locusNames.get(targetNamespace, sourceLocus.getGlstring());
            if (targetLocusName == null) {
                throw new LiftoverServiceException("could not find locus in target nomenclature " + targetNamespace + " with glstring " + sourceLocus.getGlstring());
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        Locus targetLocus = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetLocus = target.createLocus(targetLocusName);

            // if targetLocus == null ?
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create locus in target namespace", e);
        }
        return targetLocus;
    }

    @Override
    public Allele liftoverAllele(final String sourceNamespace, final String sourceAlleleUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceAlleleUri);
        checkNotNull(targetNamespace);

        Allele sourceAllele = null;
        String targetAlleleName = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceAllele = source.getAllele(sourceAlleleUri);
            if (sourceAllele == null) {
                throw new LiftoverServiceException("could not find source allele with URI " + sourceAlleleUri);
            }

            targetAlleleName = alleleNames.get(targetNamespace, sourceAllele.getAccession());
            if (targetAlleleName == null) {
                throw new LiftoverServiceException("could not find allele in target nomenclature " + targetNamespace + " with accession " + sourceAllele.getAccession() + " or glstring " + sourceAllele.getGlstring());
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        Locus targetLocus = liftoverLocus(sourceNamespace, sourceAllele.getLocus().getId(), targetNamespace);

        Allele targetAllele = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetAllele = target.createAllele(targetLocus, targetAlleleName);

            // if targetAllele == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create allele in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetAllele;
    }

    @Override
    public AlleleList liftoverAlleleList(final String sourceNamespace, final String sourceAlleleListUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceAlleleListUri);
        checkNotNull(targetNamespace);

        AlleleList sourceAlleleList = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceAlleleList = source.getAlleleList(sourceAlleleListUri);

            if (sourceAlleleList == null) {
                throw new LiftoverServiceException("could not find source allele list with URI " + sourceAlleleListUri);
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        List<Allele> targetAlleles = new ArrayList<Allele>(sourceAlleleList.getAlleles().size());
        for (Allele sourceAllele : sourceAlleleList.getAlleles()) {
            targetAlleles.add(liftoverAllele(sourceNamespace, sourceAllele.getId(), targetNamespace));
        }

        AlleleList targetAlleleList = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetAlleleList = target.createAlleleList(targetAlleles);

            // if targetAlleleList == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create allele list in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetAlleleList;
    }

    @Override
    public Haplotype liftoverHaplotype(final String sourceNamespace, final String sourceHaplotypeUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceHaplotypeUri);
        checkNotNull(targetNamespace);

        Haplotype sourceHaplotype = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceHaplotype = source.getHaplotype(sourceHaplotypeUri);

            if (sourceHaplotype == null) {
                throw new LiftoverServiceException("could not find source haplotype with URI " + sourceHaplotypeUri);
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        List<AlleleList> targetAlleleLists = new ArrayList<AlleleList>(sourceHaplotype.getAlleleLists().size());
        for (AlleleList sourceAlleleList : sourceHaplotype.getAlleleLists()) {
            targetAlleleLists.add(liftoverAlleleList(sourceNamespace, sourceAlleleList.getId(), targetNamespace));
        }

        Haplotype targetHaplotype = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetHaplotype = target.createHaplotype(targetAlleleLists);

            // if targetHaplotype == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create haplotype in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetHaplotype;
    }

    @Override
    public Genotype liftoverGenotype(final String sourceNamespace, final String sourceGenotypeUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceGenotypeUri);
        checkNotNull(targetNamespace);

        Genotype sourceGenotype = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceGenotype = source.getGenotype(sourceGenotypeUri);

            if (sourceGenotype == null) {
                throw new LiftoverServiceException("could not find source genotype with URI " + sourceGenotypeUri);
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        List<Haplotype> targetHaplotypes = new ArrayList<Haplotype>(sourceGenotype.getHaplotypes().size());
        for (Haplotype sourceHaplotype : sourceGenotype.getHaplotypes()) {
            targetHaplotypes.add(liftoverHaplotype(sourceNamespace, sourceHaplotype.getId(), targetNamespace));
        }

        Genotype targetGenotype = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetGenotype = target.createGenotype(targetHaplotypes);

            // if targetGenotype == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create genootype in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetGenotype;
    }

    @Override
    public GenotypeList liftoverGenotypeList(final String sourceNamespace, final String sourceGenotypeListUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceGenotypeListUri);
        checkNotNull(targetNamespace);

        GenotypeList sourceGenotypeList = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceGenotypeList = source.getGenotypeList(sourceGenotypeListUri);

            if (sourceGenotypeList == null) {
                throw new LiftoverServiceException("could not find source genotype list with URI " + sourceGenotypeListUri);
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        List<Genotype> targetGenotypes = new ArrayList<Genotype>(sourceGenotypeList.getGenotypes().size());
        for (Genotype sourceGenotype : sourceGenotypeList.getGenotypes()) {
            targetGenotypes.add(liftoverGenotype(sourceNamespace, sourceGenotype.getId(), targetNamespace));
        }

        GenotypeList targetGenotypeList = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetGenotypeList = target.createGenotypeList(targetGenotypes);

            // if targetGenotypeList == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create genotype list in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetGenotypeList;
    }

    @Override
    public MultilocusUnphasedGenotype liftoverMultilocusUnphasedGenotype(final String sourceNamespace, final String sourceMultilocusUnphasedGenotypeUri, final String targetNamespace) throws LiftoverServiceException {
        checkNotNull(sourceNamespace);
        checkNotNull(sourceMultilocusUnphasedGenotypeUri);
        checkNotNull(targetNamespace);

        MultilocusUnphasedGenotype sourceMultilocusUnphasedGenotype = null;
        try {
            GlClient source = clients.get(sourceNamespace);
            sourceMultilocusUnphasedGenotype = source.getMultilocusUnphasedGenotype(sourceMultilocusUnphasedGenotypeUri);

            if (sourceMultilocusUnphasedGenotype == null) {
                throw new LiftoverServiceException("could not find source unphased multilocus genotype with URI " + sourceMultilocusUnphasedGenotypeUri);
            }
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for source namespace " + sourceNamespace, e);
        }

        List<GenotypeList> targetGenotypeLists = new ArrayList<GenotypeList>(sourceMultilocusUnphasedGenotype.getGenotypeLists().size());
        for (GenotypeList sourceGenotypeList : sourceMultilocusUnphasedGenotype.getGenotypeLists()) {
            targetGenotypeLists.add(liftoverGenotypeList(sourceNamespace, sourceGenotypeList.getId(), targetNamespace));
        }

        MultilocusUnphasedGenotype targetMultilocusUnphasedGenotype = null;
        try {
            GlClient target = clients.get(targetNamespace);
            targetMultilocusUnphasedGenotype = target.createMultilocusUnphasedGenotype(targetGenotypeLists);

            // if targetMultilocusUnphasedGenotype == null ?
        }
        catch (GlClientException e) {
            throw new LiftoverServiceException("could not create multilocus unphased genotype in target namespace", e);
        }
        catch (ExecutionException e) {
            throw new LiftoverServiceException("could not create gl client for target namespace " + targetNamespace, e);
        }
        return targetMultilocusUnphasedGenotype;
    }
}
