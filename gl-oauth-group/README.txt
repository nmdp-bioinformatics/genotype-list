gl-oauth-apache contains the core OAuth 2.0 implementation
toy-portal - is a user portal that provides and validates OAuth 2.0 bearer tokens without authentication
toy-rest-app - is a sample REST application that uses a OAuth 2.0 bearer token Filter to protect access.

# to build perform install from gl-oauth-group
mvn install 

# to start toy-portal on port 9090
cd toy-portal
mvn tomcat:run

# to start toy-rest-app on port 8080
cd toy-rest-app
mvn tomcat:run


Sample session
$ curl -d "" "http://localhost:9090/toy-portal/file/get_token?userid=adam&realm=toy" ;echo
-cmir58-jolh4l

$ curl -d "" http://localhost:9090/toy-portal/file/validate?token=-cmir58-jolh4l ;echo
expires_in=3542,id=adam,duration=3600,realm=toy,scope=write read admin POST

$ curl -H "Authenticate: Bearer -cmir58-jolh4l" -d "" http://localhost:8080/toy-rest-app/index.html ; echo
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>rest-app</title>
</head>
<body>
Test application for securing REST operations.
</body>
</html>


$ curl -v -H "Authenticate: Bearer --bad--token--" -d "" http://localhost:8080/toy-rest-app/index.html ; echo
[[ only relevant headers shown ]]
< HTTP/1.1 401 Unauthorized
< WWW-Authenticate: Bearer realm="toy", scope="POST", error="invalid_token", error_description="token does not have realm toy"


