Contents:


# Introduction #

The GL Service utilizes the `GlStringResolver` to look up a GL String in the cache and find or assign an id.

In brief, all implementations of the resolver:
  * Take in a GL String exactly as given by the user.
  * Check the cache for existence (dependent on the caching implementation).
    * If found in the cache: return the existing id.
    * If not found in the cache or the search times out: return a new id.

# Details #

## Components ##

The following classes are _part_ of the GlStringResolver:
| **Name** | **Function** |
|:---------|:-------------|
| [GlStringResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlstringResolver.java) | Interface |
| [CacheGlResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/cache/CacheGlstringResolver.java) | Cache implementation of the GlStringResolver |
| [JdbcGlResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcGlstringResolver.java) | JDBC implementation of the GlStringResolver |
| [JedisGlResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-redis/src/main/java/org/immunogenomics/gl/service/redis/JedisGlstringResolver.java) | Redis/Jedis implementation of the GlStringResolver |
| [VoldemortGlResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-voldemort/src/main/java/org/immunogenomics/gl/service/voldemort/VoldemortGlstringResolver.java) | Voldemort implementation of the GlStringResolver |


The following classes are _referenced_ by the GlStringResolver:
| **Name** | **Function** |
|:---------|:-------------|
| [IdSupplier.java](.md) | Generates new IDs by [GlResource Type](Datatype.md) when called. |

## How it Works ##

### Initializing an Implementation ###

There are several different implementations of the `GlstringResolver`: Cache, JDBC, Jedis/Redis, and Voldemort. For any given instance of the application, the resolver implementation is determined when the [GlReader](GLReader.md) is initialized.
```
GlstringGlReader(final GlstringResolver glstringResolver, final IdResolver idResolver, final GlRegistry glRegistry)
```

The `GlstringResolver` is then called by the Reader.
```
final String id = glstringResolver.resolveLocus(glstring);
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

If the ID is found, it is returned and there is no need to supply a new one.
```
return id;
```

If the ID is not found, the `idSupplier` is called and a new id is returned.
```
return idSupplier.createLocusId();
```
The mechanics of the idSupplier are revealed [here](IdSupplier.md).