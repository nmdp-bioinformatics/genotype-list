Contents:


# Introduction #

The GL Service utilizes `Spark` as its webapp implementation of the URI-based RESTful service.

In brief, the SparkGlService:
  * Uses RESTful services to store and retrieve GL Strings, IDs, and [GL Resources](GLResources.md).
  * Does not implement all of the CRUD operations - only create (POST) and retrieve (GET), no updating or deleting.

# Details #

## Components ##

The following classes are _part_ of the Spark module:
| **Name** | **Function** |
|:---------|:-------------|
| [SparkGlService.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkGlService.java) | Webapp implementation of a URI-based RESTful service for the gl project using Spark. |
| [SparkModule.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkModule.java) | Spark module. |
| [NamespaceConfig.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/NamespaceConfig.java) | Configures namespace related values- provides the base URL for the GL Service. |
| [SparkGlServiceApplication.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkGlServiceApplication.java) | Wrapper for GL Service to allow Guice injection before initialization. |
| [SparkJdbcGlServiceApplication.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkJdbcGlServiceApplication.java) | Wrapper for Spark GL Service with JDBC to allow Guice injection before initialization. |
| [SparkJedisGlServiceApplication.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkJedisGlServiceApplication.java) | Wrapper for Spark GL Service with Redis/Jedis to allow Guice injection before initialization. |
| [SparkVoldemortGlServiceApplication.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-spark/src/main/java/org/immunogenomics/gl/service/spark/SparkVoldemortGlServiceApplication.java) | Wrapper for Spark GL Service with Voldemort to allow Guice injection before initialization. |


The following classes are _referenced_ by the Spark module:
| **Name** | **Function** |
|:---------|:-------------|
| [GlReader.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlReader.java) | Used in POST operations to parse and register GL Strings |
| [IdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/IdResolver.java) | Used in GET operations to retrieve GLResources corresponding with IDs. |
| [GlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlWriter.java) | Used in GET operations to write GLResources out to the desired format. |

## How it Works ##

### Initializing Spark ###

Spark uses the following constructor:
```
SparkGlService(@Namespace final String ns, final GlReader glReader, final IdResolver idResolver, final Map<String, GlWriter> glWriters)
```
This allows for an easy change in the namespace, [GLReader](GLReader.md), IdResolver, and GlWriter, based on implementation decisions.

Calling `spark.init();` initializes the post and get routes for each [GLResource](GLResources.md), and loads the IMGT alleles.


### HTTP POST ###

#### Sequence Diagrams ####

Sequence diagrams with and without comments for reference (click to enlarge):

<a href='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServicePOST.png'><img src='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServicePOST.png' alt='HTTP POST sequence diagram for the GL Service, with comments.' width='317px' height='200px' /></a>
<a href='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServicePOST-NoComments.png'><img src='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServicePOST-NoComments.png' alt='HTTP POST sequence diagram for the GL Service, without comments.' width='371px' height='200px' /></a>

#### POST Route Definitions ####

Each HTTP POST Route is based on the abstract HTTP POST Route defined in the Spark class.

[GL Resource](GLResources.md)-specific HTTP POST Routes:
```
post(new PostRoute("/locus") {
        @Override
        protected GlResource readGlResource(final String value) throws IOException {
            return glReader.readLocus(value);
        }
    });
```

The abstract HTTP POST Route:
  * Replaces the "/" and "-" characters in the path with "" and " ", respectively.
  * Reads a [GL Resource](GLResources.md) from a specified string
  * Returns HTTP Responses

#### HTTP Responses ####

| | **Status** | **Type** | **Logged message** | **Return** |
|:|:-----------|:---------|:-------------------|:-----------|
| **Empty request body** | `400` | `text/plain` |`"Unable to create {GL Resource} (400), request body was empty"`|`"Unable to create {GL Resource}"`|
| **Successful POST** | `201` |`text/plain`|`"Created (201) Location {ID}"`|`glResource.getGlstring();`|
| **IO Exception** | `400` | `text/plain` |`"Unable to create {GL Resource} (400), caught {exception}"`|`"Unable to create {GL Resource}"`|


### HTTP GET ###

#### Sequence Diagrams ####

Sequence diagrams with and without comments for reference (click to enlarge):

<a href='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServiceGET.png'><img src='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServiceGET.png' alt='HTTP GET sequence diagram for the GL Service, with comments.' width='287px' height='200px' /></a>
<a href='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServiceGET-NoComments.png'><img src='https://genotype-list.googlecode.com/svn/wiki/images/sequence-diagrams/GlServiceGET-NoComments.png' alt='HTTP GET sequence diagram for the GL Service, without comments.' width='301px' height='200px' /></a>

#### GET Route Definitions ####

[GL Resource](GLResources.md)-specific HTTP GET Routes:
```
get(new GetRoute<Locus>("/locus/:id") {
        @Override
        protected Locus findGlResource(final String id) {
            return idResolver.findLocus(id);
        }

        @Override
        protected void writeGlResource(final GlWriter glWriter, final Locus locus, final Writer writer) throws IOException {
            glWriter.writeLocus(locus, writer);
        }
    });
```

The abstract HTTP GET Route:
  * Replaces path substrings "/" and ":id" with "", and type substrings "/" "-" with "" and " ", respectively.
  * Resolve the ID to a [GL Resource](GLResources.md), if it exists
  * Write the specified [GL Resource](GLResources.md) to the specified writer
  * Returns HTTP Responses

#### HTTP Responses ####

| | **Status** | **Type** | **Logged message** | **Return** |
|:|:-----------|:---------|:-------------------|:-----------|
| **Invalid ID** |`404` |`text/plain` |`"Invalid identifier or file extension (404) {ID}"` |`"Invalid identifier or file extension"` |
| **GL Resource not found**|`404` |`text/plain` |`"{GL Resource} not found {ID}"` |`"{name} not found"` |
| **Successful GET of QR Code**|`307` |Redirect: `"http://chart.apis.google.com/chart?cht=qr&chs=128x128&chld=L&choe=UTF-8&chl=" + encode(glResource.getId())` |  |`"Redirect"` |
| **Invalid file extension**| `404`|`text/plain` |`"Invalid file extension (404) {ID}, {file extension}"` |`"Invalid file extension"` |
| **Writer IO Exception** |`500` |`text/plain` |`"Could not write {GL Resource} (500) {ID}, caught {exception}"` |`"Could not write {name}"` |
| **Successful GET** |`200` |`Content Type` (HTML, GL String, XML, RDF, etc.) |`"OK (200) {ID}"` |`stringwriter.toString();` |