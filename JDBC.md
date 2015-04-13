Contents:


# Introduction #

JDBC is one of the storage possibilities currently being evaluated for use in the GL Service.

The others include:
  * [Cache](Cache.md)
  * [Redis-Jedis](RedisJedis.md)
  * [Voldemort](Voldemort.md)

The core function of the GL Service is to store, retrive, and persist GL Strings; looking up GLResources by IDs and IDs by GL Strings.
As such, three major parts of the GL Service are impacted by this storage option:
  * GlStringResolver
  * IdResolver
  * [GLRegistry](GLRegistry.md)

# Details #

## Components ##

The following classes are _part_ of the JDBC implementations:
| **Name** | **Function** |
|:---------|:-------------|
| [GlstringResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlstringResolver.java) | GlStringResolver interface |
| [JdbcGlstringResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcGlstringResolver.java) | JDBC implementation of the GlStringResolver |
| [IdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/IdResolver.java) | IdResolver interface |
| [JdbcIdResolver.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcIdResolver.java) | JDBC implementation of the IdResolver |
| [GlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlRegistry.java) | GLRegistry interface |
| [JdbcGlRegistry.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service-jdbc/src/main/java/org/immunogenomics/gl/service/jdbc/JdbcGlRegistry.java) | JDBC implementation of the GLRegistry |

The following classes are _referenced_ by the JDBC implementations:
| **Name** | **Function** |
|:---------|:-------------|
| None | n/a |

## About JDBC ##

JDBC ([Java Database Connectivity](http://www.oracle.com/technetwork/java/javase/jdbc/index.html)) is a widely used API that allows connection to a wide range of databases. In our implementation, we are querying an Oracle Database using SQL and JDBC. In this database, there are two tables mapping GL Strings to IDs and IDs to [GL Resources](GLResources.md). Entries are added using SQL, and remain there until manually removed. If space runs out, no more entries may be added, but existing resources will be persisted.

JDBC is currently a required Maven dependency for this project, and is conveniently located in the Maven Central Repository.

## !JDBCGlstringResolver ##

The !JDBCGlstringResolver implements the interface GlStringResolver. The goal of the GlStringResolver is to take in a GL String and return its corresponding ID, or null if none is found.

With the JDBC implementation, the resolver runs an SQL query on the database to select an ID from the associated table by matching the GL String. If no ID is found, the idSupplier is called. Tables are set up as "`<resource>_id`."

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
private static final String LOCUS_ID_SQL = "select id from locus_id where glstring = ?";
```
```
@Override
public String resolveLocus(final String glstring) {
    checkNotNull(glstring);
    checkArgument(!glstring.isEmpty());
    QueryRunner queryRunner = new QueryRunner(dataSource);
    try {
        String id = (String) queryRunner.query(LOCUS_ID_SQL, new ScalarHandler());
        if (id != null) {
            return id;
        }
    }
    catch (SQLException e) {
        logger.warn("could not resolve id for locus with glstring " + glstring, e);
    }
    return idSupplier.createLocusId();
}
```

## !JDBCIdResolver ##

The !JDBCIdResolver implements the interface IdResolver. The intent of the IdResolver is to take in an ID and return its corresponding [GL Resource](GLResources.md), or null if none is found.

With the JDBC implementation, the resolver runs an SQL query on the database to select the GL Resource from the associated table by matching the ID. If no GL Resource is found, the method returns null. Tables are named according to the resource, as "`<resource>`."

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
private static final String LOCUS_SQL = "select locus from locus where id = ?";
```
```
@Override
public Locus findLocus(final String id) {
    QueryRunner queryRunner = new QueryRunner(dataSource);
    try {
        return queryRunner.query(LOCUS_SQL, new LocusHandler(), id);
    }
    catch (SQLException e) {
        logger.warn("could not find locus for id " + id, e);
        return null;
    }
}
```

## !JDBCGlRegistry ##

The !JDBCGlRegistry implements the interface [GLRegistry](GLRegistry.md). The purpose of the [GLRegistry](GLRegistry.md) is to enter [GL Resources](GLResources.md) in the two caches (GL String -> ID and ID -> GL Resource) where they can be looked up by the GlStringResolver and IdResolver.

With the JDBC implementation, the information is again passed in via SQL queries. The queries are similar to those used in the Resolvers, but use insert rather than select, and include both the "key" and "value" for entry. The ID and GL String are extracted from the GL Resource as needed.

The following is an example using the `Locus` [GL Resource](GLResources.md):

```
private static final String INSERT_LOCUS_SQL = "insert into locus (id, locus) values (?, ?)";
private static final String INSERT_LOCUS_ID_SQL = "insert into locus_id (glstring, id) values (?, ?)";
```
```
@Override
public void registerLocus(final Locus locus) {
    checkNotNull(locus);
    QueryRunner queryRunner = new QueryRunner(dataSource);
    try {
        queryRunner.update(INSERT_LOCUS_SQL, locus.getId(), locus);
        queryRunner.update(INSERT_LOCUS_ID_SQL, locus.getGlstring(), locus.getId());
    }
    catch (SQLException e) {
        logger.warn("could not register locus " + locus.getId(), e);
    }
}
```

# Performance Testing Results #

To be added after performance tests have been completed.