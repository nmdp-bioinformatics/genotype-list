Contents:


# Introduction #

Voldemort is one of the 4 storage possibilities currently being evaluated for use in the GL Service.

The others include:
  * [Cache](Cache.md)
  * [JDBC](JDBC.md)
  * [Redis-Jedis Redis-Jedis]

The core function of the GL Service is to store, retrive, and persist GL Strings; looking up GLResources by IDs and IDs by GL Strings.
As such, three major parts of the GL Service are impacted by this storage option:
  * GlStringResolver
  * IdResolver
  * [GLRegistry](GLRegistry.md)

# Details #

## Components ##

The following classes are _part_ of the Voldemort implementations:
| **Name** | **Function** |
|:---------|:-------------|
| [GlstringResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlstringResolver.java) | GlStringResolver interface |
| [VoldemortGlstringResolver.java](.md) | Voldemort implementation of the GlStringResolver |
| [IdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/IdResolver.java) | IdResolver interface |
| [VoldemortIdResolver.java](.md) | Voldemort implementation of the IdResolver |
| [GlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlRegistry.java) | GLRegistry interface |
| [VoldemortGlRegistry.java](.md) | Voldemort implementation of the GLRegistry |

The following classes are _referenced_ by the Voldemort implementations:
| **Name** | **Function** |
|:---------|:-------------|
| None | n/a |

## About Voldemort ##

Voldemort is "a distributed, optionally persistent and fault tolerant hash table providing horizontal scalability and high-availability." Entries are added to the hash table in key/value pairs, and <add notes about persistence>.

Voldemort is currently a required Maven dependency for this project. However, it is not located in the Maven Central Repository and must be downloaded/installed separately. See [Third Party Dependencies](ThirdPartyDependencies.md) for more information.

## VoldemortGlstringResolver ##

The VoldemortGlstringResolver implements the interface GlStringResolver. The goal of the GlStringResolver is to take in a GL String and return its corresponding ID, or null if none is found.

With the Voldemort implementation, the key is a non-empty, non-null GL String, with an ID for a value. When called, Voldemort searches through the client hash table for the ID. If no ID is found, the idSupplier is called.

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
@Override
public String resolveLocus(final String glstring) {
    checkNotNull(glstring);
    checkArgument(!glstring.isEmpty());
    StoreClient<String, String> client = storeClientFactory.getStoreClient("locusId");
    String locusId = client.getValue(glstring);
    if (locusId != null) {
        return locusId;
    }
    return idSupplier.createLocusId();
}
```

## VoldemortIdResolver ##

The VoldemortIdResolver implements the interface IdResolver. The intent of the IdResolver is to take in an ID and return its corresponding [GL Resource](GLResources.md), or null if none is found.

With the Voldemort implementation, the key is an ID, and the [GL Resource](GLResources.md) is the value. When called, He-Who-Must-Not-Be-Named searches through the client hash table for the GL Resource. If no GL Resource is found, the method returns null.

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
@Override
public Locus findLocus(final String id) {
    StoreClient<String, Locus> client = storeClientFactory.getStoreClient("locus");
    return client.getValue(id);
}
```

## VoldemortGlRegistry ##

The VoldemortGlRegistry implements the interface [GLRegistry](GLRegistry.md). The purpose of the [GLRegistry](GLRegistry.md) is to enter [GL Resources](GLResources.md) in the two caches (GL String -> ID and ID -> GL Resource) where they can be looked up by the GlStringResolver and IdResolver.

With the Voldemort implementation, the entries are put into the client hash table using `put()` methods, in contrast to the `get()` methods used by the resolvers. The keys and values are extracted from the GL Resource as needed.

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
@Override
public void registerLocus(final Locus locus) {
    checkNotNull(locus);
    StoreClient<String, Locus> locusClient = storeClientFactory.getStoreClient("locus");
    locusClient.put(locus.getId(), locus);
    StoreClient<String, String> locusIdClient = storeClientFactory.getStoreClient("locusId");
    locusIdClient.put(locus.getGlstring(), locus.getId());
}
```

# Performance Testing Results #

To be added after performance tests have been completed.