This project is an easy way to launch Jetty with the gl-service-webapp.war and gl-service-explorer.war

The war artifacts should be installed or deployed to a repository.


The following command will download the required artifacts and launch Jetty

mvn package jetty:run


Post the following in your browser to bring up the index.html page

http://localhost:8080/ 