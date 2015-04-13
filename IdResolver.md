Contents:


# Introduction #

The GL Service utilizes the `IdResolver` to look up an ID in the cache and return a [GL Resource](GLResources.md) or null.

This class is invoked within both the GET and POST operations of the [Spark RESTful service](SparkGlService.md) webapp.
  * GET calls the IdResolver directly to return a GLResource registered under a particular ID.
  * POST invokes the GLReader, which calls the IdResolver to determine whether a GLResource has been registered.

In brief, all implementations of the resolver:
  * Take in an id (usually returned by the [GlstringResolver](GlStringResolver.md) or passed in by the user).
  * Return a [GL Resource](GLResources.md) or null if the id has been registered.

# Details #

## Components ##

The following classes are _part_ of the IdResolver:
| **Name** | **Function** |
|:---------|:-------------|
| [IdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/IdResolver.java) | Interface |
| [CacheIdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/cache/CacheIdResolver.java) | Cache implementation of the IdResolver |
| [JdbcIdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcIdResolver.java) | JDBC implementation of the IdResolver |
| [JedisIdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-redis/src/main/java/org/immunogenomics/gl/service/redis/JedisIdResolver.java) | Redis/Jedis implementation of the IdResolver |
| [VoldemortIdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-voldemort/src/main/java/org/immunogenomics/gl/service/voldemort/VoldemortIdResolver.java) | Voldemort implementation of the IdResolver |


The following classes are _referenced_ by the IdResolver:
| **Name** | **Function** |
|:---------|:-------------|
| None | n/a |

## How it Works ##

### Initializing an Implementation ###

There are several different implementations of the `IdResolver`: Cache, JDBC, Jedis/Redis, and Voldemort. For any given instance of the service, the resolver implementation is determined when the [GL Reader](GLReader.md) is initialized.
```
GlstringGlReader(final GlstringResolver glstringResolver, final IdResolver idResolver, final GlRegistry glRegistry)
```

The `IdResolver` is then called by the Reader.
```
Locus locus = idResolver.findLocus(id);
```


### Finding an ID ###

The way the resolver finds an id depends on the chosen implementation. More details to be added soon.

#### Cache ####

Uses `com.google.common.cache.LoadingCache`

#### JDBC ####

Runs SQL queries via `org.apache.commons.dbutils.QueryRunner`

#### Redis/Jedis ####

Uses `redis.clients.jedis.Jedis`

#### Voldemort ####

Uses `voldemort.client.StoreClient`

### Supply New IDs ###

If the ID has been registered, its corresponding GL Resource is returned by the cache implementation.
If the ID has not been registered, the cache implementation returns null, and the IdResolver returns null to the GLReader.

Example from the CacheIdResolver:
```
public Locus findLocus(final String id) {
        return loci.getIfPresent(id);
    }
```