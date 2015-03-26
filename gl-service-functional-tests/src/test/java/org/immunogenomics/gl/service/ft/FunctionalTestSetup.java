/*

    gl-service-functional-tests  Functional tests for the URI-based RESTful service for the gl project.
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
package org.immunogenomics.gl.service.ft;

import static com.jayway.restassured.RestAssured.with;

import java.util.concurrent.atomic.AtomicBoolean;

import org.immunogenomics.gl.Allele;
import org.immunogenomics.gl.AlleleList;
import org.immunogenomics.gl.Genotype;
import org.immunogenomics.gl.GenotypeList;
import org.immunogenomics.gl.Haplotype;
import org.immunogenomics.gl.Locus;
import org.immunogenomics.gl.MultilocusUnphasedGenotype;

import com.google.common.collect.ImmutableList;

/**
 * Functional test setup.
 */
class FunctionalTestSetup {
    private static final AtomicBoolean complete = new AtomicBoolean(false);

    static final void createValidGlResources() {
        if (!complete.get()) {
            System.out.println("Creating valid gl resources . . .");

            Locus a = new Locus("http://localhost:10080/gl/locus/0", "HLA-A");
            Locus b = new Locus("http://localhost:10080/gl/locus/1", "HLA-B");
            Allele a01 = new Allele("http://localhost:10080/gl/allele/0", "A00001", "HLA-A*01:01:01:01", a);
            Allele a02 = new Allele("http://localhost:10080/gl/allele/1", "A00002", "HLA-A*02:01:01:01", a);
            Allele b01 = new Allele("http://localhost:10080/gl/allele/2", "B00001", "HLA-B*01:01:01:01", b);
            Allele b02 = new Allele("http://localhost:10080/gl/allele/3", "B00002", "HLA-B*02:01:01:01", b);
            AlleleList aAlleles0 = new AlleleList("http://localhost:10080/gl/allele-list/0", ImmutableList.of(a01, a02));
            AlleleList aAlleles1 = new AlleleList("http://localhost:10080/gl/allele-list/0", ImmutableList.of(a01, a02));
            AlleleList bAlleles0 = new AlleleList("http://localhost:10080/gl/allele-list/1", ImmutableList.of(b01, b02));
            AlleleList bAlleles1 = new AlleleList("http://localhost:10080/gl/allele-list/1", ImmutableList.of(b01, b02));
            Haplotype haplotype = new Haplotype("http://localhost:10080/gl/haplotype/0", ImmutableList.of(aAlleles0, bAlleles0));
            Haplotype aHaplotype0 = new Haplotype("http://localhost:10080/gl/haplotype/0", aAlleles0);
            Haplotype aHaplotype1 = new Haplotype("http://localhost:10080/gl/haplotype/0", aAlleles1);
            Haplotype bHaplotype0 = new Haplotype("http://localhost:10080/gl/haplotype/0", bAlleles0);
            Haplotype bHaplotype1 = new Haplotype("http://localhost:10080/gl/haplotype/0", bAlleles1);
            Genotype aGenotype0 = new Genotype("http://localhost:10080/gl/genotype/0", ImmutableList.of(aHaplotype0, aHaplotype1));
            Genotype aGenotype1 = new Genotype("http://localhost:10080/gl/genotype/0", ImmutableList.of(aHaplotype0, aHaplotype1));
            Genotype bGenotype0 = new Genotype("http://localhost:10080/gl/genotype/0", ImmutableList.of(bHaplotype0, bHaplotype1));
            Genotype bGenotype1 = new Genotype("http://localhost:10080/gl/genotype/0", ImmutableList.of(bHaplotype0, bHaplotype1));
            GenotypeList aGenotypes = new GenotypeList("http://localhost:10080/gl/genotype-list/0", ImmutableList.of(aGenotype0, aGenotype1));
            GenotypeList bGenotypes = new GenotypeList("http://localhost:10080/gl/genotype-list/0", ImmutableList.of(bGenotype0, bGenotype1));
            MultilocusUnphasedGenotype multilocusUnphasedGenotype = new MultilocusUnphasedGenotype("http://localhost:10080/gl/multilocus-unphased-genotype/0", ImmutableList.of(aGenotypes, bGenotypes));

            with().body(a.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/locus");
            with().body(b.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/locus");
            with().body(a01.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele");
            with().body(a02.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele");
            with().body(b01.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele");
            with().body(b02.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele");
            with().body(aAlleles0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele-list");
            with().body(bAlleles0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/allele-list");
            with().body(haplotype.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/haplotype");
            with().body(aHaplotype0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/haplotype");
            with().body(aHaplotype1.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/haplotype");
            with().body(bHaplotype0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/haplotype");
            with().body(bHaplotype1.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/haplotype");
            with().body(aGenotype0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/genotype");
            with().body(bGenotype0.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/genotype");
            with().body(aGenotypes.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/genotype-list");
            with().body(bGenotypes.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/genotype-list");
            with().body(multilocusUnphasedGenotype.getGlstring()).contentType("text/plain").expect().statusCode(201).when().post("http://localhost:10080/gl/multilocus-unphased-genotype");
        }
        complete.set(true);
   }
}