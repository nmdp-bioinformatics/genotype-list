/*

    gl-liftover-service-functional-tests  Functional tests for RESTful genotype list liftover service.
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
package org.nmdp.gl.liftover.ft;

import static com.jayway.restassured.RestAssured.given;

import static org.hamcrest.Matchers.startsWith;

import org.junit.Before;
import org.junit.Test;

/**
 * Functional tests for RESTful genotype list liftover service.
 */
public final class LiftoverServiceFT {
    private String uri;
    private String textHtml;
    private String textPlain;
    private String applicationJson;
    private String emptyRequestBody;
    private String emptyJson;

    @Before
    public void setUp() {
        uri = "http://localhost:10080/liftover";
        textHtml = "text/html";
        textPlain = "text/plain";
        applicationJson = "application/json";
        emptyRequestBody = "";
        emptyJson = "{}";
    }

    @Test
    public void testGet() {
        given().expect().statusCode(200).contentType(textPlain).when().get(uri);
    }

    @Test
    public void testGetInvalidRequest() {
        given().expect().statusCode(404).when().get(uri + "/invalid");
    }

    @Test
    public void testPostInvalidRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(404)
            .when().post(uri + "/invalid");
    }


    @Test
    public void testPostLocusEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }

    @Test
    public void testPostLocusEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }

    @Test
    public void testPostLocusMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/locus/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }

    @Test
    public void testPostLocusMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }

    @Test
    public void testPostLocusMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/locus/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }

    @Test
    public void testPostLocusLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/locus/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/locus");
    }


    @Test
    public void testPostAlleleEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }

    @Test
    public void testPostAlleleEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }

    @Test
    public void testPostAlleleMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/allele/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }

    @Test
    public void testPostAlleleMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }

    @Test
    public void testPostAlleleMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/allele/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }

    @Test
    public void testPostAlleleLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/allele/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele");
    }


    @Test
    public void testPostAlleleListEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }

    @Test
    public void testPostAlleleListEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }

    @Test
    public void testPostAlleleListMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/allele-list/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }

    @Test
    public void testPostAlleleListMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }

    @Test
    public void testPostAlleleListMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/allele-list/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }

    @Test
    public void testPostAlleleListLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/allele-list/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/allele-list");
    }


    @Test
    public void testPostHaplotypeEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }

    @Test
    public void testPostHaplotypeEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }

    @Test
    public void testPostHaplotypeMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/haplotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }

    @Test
    public void testPostHaplotypeMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }

    @Test
    public void testPostHaplotypeMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/haplotype/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }

    @Test
    public void testPostHaplotypeLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/haplotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/haplotype");
    }


    @Test
    public void testPostGenotypeEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }

    @Test
    public void testPostGenotypeEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }

    @Test
    public void testPostGenotypeMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/genotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }

    @Test
    public void testPostGenotypeMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }

    @Test
    public void testPostGenotypeMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/genotype/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }

    @Test
    public void testPostGenotypeLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/genotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype");
    }


    @Test
    public void testPostGenotypeListEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }

    @Test
    public void testPostGenotypeListEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }

    @Test
    public void testPostGenotypeListMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/genotype-list/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }

    @Test
    public void testPostGenotypeListMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }

    @Test
    public void testPostGenotypeListMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/genotype-list/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }

    @Test
    public void testPostGenotypeListLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/genotype-list/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/genotype-list");
    }


    @Test
    public void testPostMultilocusUnphasedGenotypeEmptyRequest() {
        given().body(emptyRequestBody).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }

    @Test
    public void testPostMultilocusUnphasedGenotypeEmptyJson() {
        given().body(emptyJson).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }

    @Test
    public void testPostMultilocusUnphasedGenotypeMissingSourceNamespace() {
        String missingSourceNamespace = "{\"sourceUri\":\"http://localhost:10080/source/multilocus-unphased-genotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }

    @Test
    public void testPostMultilocusUnphasedGenotypeMissingSourceUri() {
        String missingSourceUri = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(missingSourceUri).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }

    @Test
    public void testPostMultilocusUnphasedGenotypeMissingTargetNamespace() {
        String missingTargetNamespace = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/multilocus-unphased-genotype/0\"}";

        given().body(missingTargetNamespace).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }

    @Test
    public void testPostMultilocusUnphasedGenotypeLiftoverServiceIOException() {
        String body = "{\"sourceNamespace\":\"http://localhost:10080/source/\",\"sourceUri\":\"http://localhost:10080/source/multilocus-unphased-genotype/0\",\"targetNamespace\":\"http://localhost:10080/target/\"}";

        given().body(body).contentType(applicationJson)
            .expect().statusCode(400).contentType(applicationJson)
            .when().post(uri + "/multilocus-unphased-genotype");
    }
}
