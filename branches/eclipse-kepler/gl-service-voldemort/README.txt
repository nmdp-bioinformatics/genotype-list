gl-service-voldemort
---

Start voldemort with src/main/conf as the configuration directory, e.g.

$ ./bin/voldemort-server.sh src/main/conf > voldemort.log &


If you see log4j configuration errors on startup, the 0.90.1 homebrew installation on OSX needed the following change to voldemort-server.sh

-java -Dlog4j.configuration=src/java/log4j.properties $VOLD_OPTS -cp $CLASSPATH voldemort.server.VoldemortServer $@
+java $VOLD_OPTS -cp $CLASSPATH voldemort.server.VoldemortServer $@


If using the mysql storage engine, install a mysql JDBC driver

$ cp ~/.m2/repository/mysql/mysql-connector-java/5.1.21/mysql-connector-java-5.1.21.jar /usr/local/Cellar/voldemort/0.90.1/libexec/lib/
