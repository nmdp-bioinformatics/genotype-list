#!/bin/bash

# grep log files for location URIs and split them into CSV for use in jmeter scripts
# grep "Created (201) Location" /usr/local/Cellar/jetty/8.1.3/libexec/logs/2012_06_13.stderrout.log | cut -f 10 -d " " | perl -ne 'if (m/.*\/([a-z0-9-]+)\/([a-z0-9]+)/) { print "$1,$2,$_" }' > locations.txt
grep "Created (201) Location" $1 | cut -f 10 -d " " | perl -ne 'if (m/.*\/([a-z0-9-]+)\/([a-z0-9]+)/) { print "$1,$2,$_" }'
