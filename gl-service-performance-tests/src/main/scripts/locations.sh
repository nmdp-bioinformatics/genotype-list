#!/bin/bash

# grep log files for location URIs
# grep "Created (201) Location" /usr/local/Cellar/jetty/8.1.3/libexec/logs/2012_06_13.stderrout.log | cut -f 10 -d " " > locations.txt
grep "Created (201) Location" $1 | cut -f 10 -d " "
