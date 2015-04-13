Contents:


# Introduction #

The GL Service utilizes the `GlRegistry` to add [GL Resources](GLResources.md) to their respective caches.

In brief, all implementations of the resolver:
  * Take in a GL Resource of any type
  * Add entries to the ID and GL String caches:
    * ID -> [GL Resource](GLResources.md)
    * GL String -> ID

# Details #

## Components ##

The following classes are _part_ of the GLRegistry:
| **Name** | **Function** |
|:---------|:-------------|
| [GL Registry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlRegistry.java) | Interface |
| [CacheGlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/cache/CacheGlRegistry.java) | Cache implementation of the GL Registry |
| [JdbcGlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcGlRegistry.java) | JDBC implementation of the GL Registry |
| [JedisGlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-redis/src/main/java/org/immunogenomics/gl/service/redis/JedisGlRegistry.java) | Redis/Jedis implementation of the GL Registry |
| [VoldemortGlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-voldemort/src/main/java/org/immunogenomics/gl/service/voldemort/VoldemortGlRegistry.java) | Voldemort implementation of the GL Registry |

The following classes are _referenced_ by the GLRegistry:
| **Name** | **Function** |
|:---------|:-------------|
| None | n/a |

## How it Works ##

### Initializing an Implementation ###

There are several different implementations of the `GlRegistry`: Cache, JDBC, Redis-Jedis, and Voldemort. For any given instance of the application, the resolver implementation is determined when the [GlReader](GLReader.md) is initialized.
```
GlstringGlReader(final GlstringResolver glstringResolver, final IdResolver idResolver, final GlRegistry glRegistry)
```

The `GlRegistry` is then called by the Reader.
```
glRegistry.registerLocus(locus);
```


### Registering ###

The registry's mechanics vary depending on the implementation, but the basic logic remains the same.

The ID and GL String are extracted as needed from the GL Resource, and entered in each cache.

The following example is from the [CacheGlRegistry](Cache#CacheGlRegistry.md):
```
loci.put(locus.getId(), locus);
locusIds.put(locus.getGlstring(), locus.getId());
```


#### Cache ####

Uses `com.google.common.cache.Cache`

#### JDBC ####

Runs SQL queries via `org.apache.commons.dbutils.QueryRunner`

#### Redis/Jedis ####

Uses `redis.clients.jedis.Jedis`

#### Voldemort ####

Uses `voldemort.client.StoreClient`