#!/bin/bash

# grep log files for IMGT allele URIs
# grep "Loaded IMGT allele" /usr/local/Cellar/jetty/8.1.3/libexec/logs/2012_06_13.stderrout.log | cut -f 10 -d " " > alleles.txt
grep "Loaded IMGT allele" $1 | cut -f 10 -d " "
