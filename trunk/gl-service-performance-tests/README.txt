gl-service-performance-tests
---


To generate random glstrings for HTTP POST performance tests, load IMGT alleles and collect allele URIs

$ curl -X post http://localhost:8080/gl/load-imgt-alleles
$ src/main/scripts/alleles.sh $log-file > alleles.txt

Then run e.g.

$ java -classpath target/gl-service-performance-tests-1.0-SNAPSHOT-jar-with-dependencies.jar org.immunogenomics.gl.service.pt.GenerateSixLocusGlstrings -a alleles.txt -n 1000 > glstrings.txt

to generate 1000 glstrings.


To perform HTTP POST performance tests, use the glstrings file generated above or one of the precomputed files on /vol/bio/gl-service

$ java -classpath target/gl-service-performance-tests-1.0-SNAPSHOT-jar-with-dependencies.jar org.immunogenomics.gl.service.pt.RegisterMultilocusUnphasedGenotypes --namespace http://localhost:8080/gl/ -n 4 -g glstrings.txt


To perform HTTP GET performance tests, first run the HTTP POST performance tests or otherwise register a lot of new resources.  Then collect locations for jmeter testing

$ src/main/scripts/locations-for-jmeter.sh $log-file > src/main/jmeter/locations.txt

Open src/main/jmeter/gl-service-http-get-tests.jmx in Apache JMeter version 2.7 or later.  Adjust the number of concurrent users in Thread Group and the loop count per user in Loop Controller and run the performance tests.
