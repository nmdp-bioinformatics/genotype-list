gl-service
---

Install jdk 1.6 or later, maven 3.0.4 or later.


To build

$ mvn install


Functional tests are excuted against an embedded jetty during the build.


To run performance tests

$ cd gl-service-performance-tests
$ mvn -DdescriptorId=jar-with-dependencies assembly:assembly

and refer to gl-service-performance-tests/README.txt for further instructions.


To deploy to an external jetty/tomcat

$ cp gl-service-webapp/target/gl.war $CONTAINER_HOME/webapps
$ cp gl-service-explorer/target/explorer.war $CONTAINER_HOME/webapps


To install to a host:port other than the default (http://localhost:10080), edit the following property files, rebuild the war files, and redeploy

gl-service-webapp/src/main/resources/gl-service.properties
gl-service-explorer/src/main/resources/gl-service.properties


To load loci and alleles from the IMGT nomenclature database version 3.9.0, either

$ curl -X post http://localhost:10080/gl/load-imgt-alleles

or visit

http://localhost:10080/gl

in a web browser and hit the Load IMGT alleles button


To explore the gl-service API interactively, use the API explorer web interface at

http://localhost:10080/explorer


To retrieve a resource identified by a URI, use HTTP GET

$ wget -q -O - http://localhost:10080/gl/locus/0.gls && echo
HLA-A

The file extension determines the content type of the response.

$ wget -q -O - http://localhost:10080/gl/locus/0.html && echo
<!DOCTYPE html>
<html lang="en">
  <head>
    <title>http://localhost:10080/gl/locus/0</title>
  </head>
  <body>
    <p class="type"><a href="http://immunogenomics.org/gl-ontology/Locus">http://immunogenomics.org/gl-ontology/Locus</a></p>
    <p class="title">http://localhost:10080/gl/locus/0</p>
    <p class="identifier">http://localhost:10080/gl/locus/0</p>
    <p class="label">http://localhost:10080/gl/locus/0</p>
    <p class="comment">A locus represented by glstring HLA-A</p>
    <p class="hasGlstring">HLA-A</p>
  </body>
</html>


$ wget -q -O - http://localhost:10080/gl/locus/0.rdf && echo
<?xml version="1.0" encoding="UTF-8"?>
<rdf:RDF
  xmlns:dc="http://purl.org/dc/elements/1.1/"
  xmlns:gl="http://immunogenomics.org/gl-ontology/1.0"
  xmlns:owl="http://www.w3.org/2002/07/owl#"
  xmlns:rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#"
  xmlns:rdfs="http://www.w3.org/2000/01/rdf-schema#"
>
  <rdf:Description rdf:about="http://localhost:10080/gl/locus/0">
    <rdf:type rdf:resource="http://immunogenomics.org/gl-ontology/Locus"/>
    <dc:title>http://localhost:10080/gl/locus/0</dc:title>
    <dc:identifier>http://localhost:10080/gl/locus/0</dc:identifier>
    <rdfs:label>http://localhost:10080/gl/locus/0</rdfs:label>
    <rdfs:comment>A locus represented by glstring HLA-A</rdfs:comment>
    <gl:hasGlstring>HLA-A</gl:hasGlstring>
  </rdf:Description>
</rdf:RDF>

$ wget -q -O - http://localhost:10080/gl/locus/0.n3 && echo
@prefix dc: <http://purl.org/dc/elements/1.1/> .
@prefix gl: <http://immunogenomics.org/gl-ontology/1.0/> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .

<http://localhost:10080/gl/locus/0> a <http://immunogenomics.org/gl-ontology/Locus> ;
  dc:title "http://localhost:10080/gl/locus/0" ;
  dc:identifier "http://localhost:10080/gl/locus/0" ;
  rdfs:label "http://localhost:10080/gl/locus/0" ;
  rdfs:comment "A locus represented by glstring HLA-A" ;
  gl:hasGlstring "HLA-A" ;


To register a new resource described by a glstring, use HTTP POST

$ curl --header "content-type: text/plain" --data "HLA-A*01:01:01:01/HLA-A*01:01:01:02N" -X POST http://localhost:10080/gl/allele-list -v && echo
* About to connect() to localhost port 10080 (#0)
*   Trying 127.0.0.1... connected
* Connected to localhost (127.0.0.1) port 10080 (#0)
> POST /gl/allele-list HTTP/1.1
> User-Agent: curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8r zlib/1.2.3
> Host: localhost:10080
> Accept: */*
> content-type: text/plain
> Content-Length: 36
> 
< HTTP/1.1 201 Created
< Date: Tue, 12 Jun 2012-2013 20:54:55 GMT
< Content-Type: text/plain
< Location: http://localhost:10080/gl/allele-list/0
< Content-Length: 36
< Server: Jetty(8.1.3.v2012-20130416)
< 
* Connection #0 to host localhost left intact
* Closing connection #0
HLA-A*01:01:01:01/HLA-A*01:01:01:02N

The URI for the newly created resource is returned as the Location in the response header.

Other examples

$ curl --header "content-type: text/plain" --data "HLA-A" -X POST http://localhost:10080/gl/locus -v && echo

$ curl --header "content-type: text/plain" --data "HLA-A*01:01:01:01" -X POST http://localhost:10080/gl/allele -v && echo

$ curl --header "content-type: text/plain" --data "HLA-A*01:01:01:01/HLA-A*01:01:01:02N" -X POST http://localhost:10080/gl/allele-list -v && echo

$ curl --header "content-type: text/plain" --data "HLA-DRB1*08:30:02~HLA-DRB3*02:02:01:01" -X POST http://localhost:10080/gl/haplotype -v && echo

$ curl --header "content-type: text/plain" --data "HLA-A*01:01:01:01+HLA-A*01:01:01:02N" -X POST http://localhost:10080/gl/genotype -v && echo

$ curl --header "content-type: text/plain" --data "HLA-A*01:01:01:01+HLA-A*01:01:01:02N|HLA-DRB1*08:30:02~HLA-DRB3*02:02:01:01+HLA-DRB1*08:30:02~HLA-DRB5*01:12" -X POST http://localhost:10080/gl/genotype-list -v && echo

$ curl --header "content-type: text/plain" --data "HLA-A*02:01:44+HLA-A*24:20^HLA-B*15:29+HLA-B*82:02^HLA-C*05:42+HLA-C*12:13^HLA-DRB1*12:06~HLA-DRB4*01:03:01:01+HLA-DRB1*10:01:01~HLA-DRB3*02:02:03^HLA-DQB1*03:03:02:02+HLA-DQB1*06:15" -X POST http://localhost:10080/gl/multilocus-unphased-genotype -v && echo
