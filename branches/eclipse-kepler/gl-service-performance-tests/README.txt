gl-service-performance-tests
---


To generate random glstrings for HTTP POST performance tests, load IMGT alleles and collect allele URIs

$ curl -X post http://localhost:8080/gl/load-imgt-alleles
$ src/main/scripts/alleles.sh $log-file > alleles.txt

(this is rather an old way of doing things and is left for historical purposes)

On the AMIs with pre-installed imgt data, you can get your allele list thusly:

$ awk '{print $1}' /etc/imgt_data_loaded > alleles.txt


now, generate the jar file for the next step:

$ mvn assembly:assembly

Then run e.g.

$ java -classpath target/gl-service-performance-tests-1.0-SNAPSHOT-jar-with-dependencies.jar org.immunogenomics.gl.service.pt.GenerateSixLocusGlstrings -a alleles.txt -n 1000 > glstrings.txt

to generate 1000 glstrings.

(feel free to vary 1000 -- that is, set it to 100,000 for a longer test)


To perform HTTP POST performance tests, use the glstrings file generated above or one of the precomputed files on /vol/bio/gl-service

$ java -classpath target/gl-service-performance-tests-1.0-SNAPSHOT-jar-with-dependencies.jar org.immunogenomics.gl.service.pt.RegisterMultilocusUnphasedGenotypes --namespace http://localhost:8080/gl/ -n 4 -g glstrings.txt

On smaller machines, you may wish to use '-n 2'.  

Don't forget to ensure that tomcat7 has enough memory to run!  on ubuntu, it defaults to 128M of memory.  This must be adjusted in /etc/default/tomcat7 on ubuntu-based systems.

To perform HTTP GET performance tests, first run the HTTP POST performance tests or otherwise register a lot of new resources.  Then collect locations for jmeter testing

$ src/main/scripts/locations-for-jmeter.sh $log-file > src/main/jmeter/locations.txt

Open src/main/jmeter/gl-service-http-get-tests.jmx in Apache JMeter version 2.7 or later.  Adjust the number of concurrent users in Thread Group and the loop count per user in Loop Controller and run the performance tests.
