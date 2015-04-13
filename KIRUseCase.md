### Load KIR nomenclature ###

To load loci and alleles from the KIR nomenclature database version 2.4.0, either

```
$ curl -X post http://localhost:10080/gl/load-kir-alleles
```

or visit

http://localhost:10080/gl

in a web browser and hit the Load KIR alleles button.


<br />
### Example ###

Given the following KIR typing interpretation example
```
KIR2DL1 Y       001+00301|001+00302
KIR2DL2 N
KIR2DL3 Y       001+005
KIR2DL4 Y       00102+00202|00102+00801|00103+00202|00103+00801|00105+00202|00105+00801
KIR2DL5 Y       001
KIR2DS1 Y       002
KIR2DS2 Y       001
KIR2DS3 N
KIR2DS4 Y       101
KIR2DS5 Y       002
KIR3DL1 Y       004+005
KIR3DL2 Y       001+003
KIR3DL3 Y       002+003
KIR3DS1 N
```


a multilocus unphased genotype representation in GL String format might be
```
KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^
KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+
KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|
KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^
KIR2DS1*002^KIR2DS2*001^KIR2DS4*101^KIR2DS5*002^KIR3DL1*004+
KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003
```


or using `NNNN` alleles for missing or null loci
```
KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^KIR2DL2*NNNN^
KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+
KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|
KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^
KIR2DS1*002^KIR2DS2*001^KIR2DS3*NNNN^KIR2DS4*101^KIR2DS5*002^
KIR3DL1*004+KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003^
KIR3DS1*NNNN
```


<br />
Register the multilocus unphased genotype using curl
```
$ curl --header "content-type: text/plain" \
       --data "KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^KIR2DL2*NNNN^KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^KIR2DS1*002^KIR2DS2*001^KIR2DS3*NNNN^KIR2DS4*101^KIR2DS5*002^KIR3DL1*004+KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003^KIR3DS1*NNNN" \
       -X POST http://localhost:10080/gl/multilocus-unphased-genotype -v && echo
* About to connect() to localhost port 10080 (#0)
*   Trying 127.0.0.1... connected
* Connected to localhost (127.0.0.1) port 10080 (#0)
> POST /gl/multilocus-unphased-genotype HTTP/1.1
> User-Agent: curl/7.19.7 (universal-apple-darwin10.0) libcurl/7.19.7 OpenSSL/0.9.8r zlib/1.2.3
> Host: localhost:10080
> Accept: */*
> content-type: text/plain
> Content-Length: 414
> 
< HTTP/1.1 201 Created
< Date: Wed, 08 Aug 2012 21:47:44 GMT
< Content-Type: text/plain
< Location: http://localhost:10080/gl/multilocus-unphased-genotype/1
< Content-Length: 414
< Server: Jetty(8.1.4.v20120524)
< 
* Connection #0 to host localhost left intact
* Closing connection #0
KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^KIR2DL2*NNNN^KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^KIR2DS1*002^KIR2DS2*001^KIR2DS3*NNNN^KIR2DS4*101^KIR2DS5*002^KIR3DL1*004+KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003^KIR3DS1*NNNN
```

and retrieve in n3 format using wget
```
$ wget -q -O - http://localhost:10080/gl/multilocus-unphased-genotype/1.n3 && echo

@prefix dc: <http://purl.org/dc/elements/1.1/> .
@prefix gl: <http://immunogenomics.org/gl-ontology/1.0/> .
@prefix owl: <http://www.w3.org/2002/07/owl#> .
@prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> .
@prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .

<http://localhost:10080/gl/multilocus-unphased-genotype/1> a <http://immunogenomics.org/gl-ontology/MultilocusUnphasedGenotype> ;
  dc:title "http://localhost:10080/gl/multilocus-unphased-genotype/1" ;
  dc:identifier "http://localhost:10080/gl/multilocus-unphased-genotype/1" ;
  rdfs:label "http://localhost:10080/gl/multilocus-unphased-genotype/1" ;
  rdfs:comment "A multilocus unphased genotype with 14 genotype lists represented by glstring KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^KIR2DL2*NNNN^KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^KIR2DS1*002^KIR2DS2*001^KIR2DS3*NNNN^KIR2DS4*101^KIR2DS5*002^KIR3DL1*004+KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003^KIR3DS1*NNNN" ;
  gl:hasGlstring "KIR2DL1*001+KIR2DL1*00301|KIR2DL1*001+KIR2DL1*00302^KIR2DL2*NNNN^KIR2DL3*001+KIR2DL3*005^KIR2DL4*00102+KIR2DL4*00202|KIR2DL4*00102+KIR2DL4*00801|KIR2DL4*00103+KIR2DL4*00202|KIR2DL4*00103+KIR2DL4*00801|KIR2DL4*00105+KIR2DL4*00202|KIR2DL4*00105+KIR2DL4*00801^KIR2DL5*001^KIR2DS1*002^KIR2DS2*001^KIR2DS3*NNNN^KIR2DS4*101^KIR2DS5*002^KIR3DL1*004+KIR3DL1*005^KIR3DL2*001+KIR3DL2*003^KIR3DL3*002+KIR3DL3*003^KIR3DS1*NNNN" ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/5> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/6> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/7> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/8> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/9> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/a> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/b> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/c> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/d> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/e> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/f> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/g> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/h> ;
  gl:hasGenotypeList <http://localhost:10080/gl/genotype-list/i> ;
```