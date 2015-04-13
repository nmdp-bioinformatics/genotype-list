# Strict Mode #

By default the genotype list service will create new loci and alleles as necessary to register higher level genotype list resources.  Properties in `gl-service.properties` specify this behavior:

```
org.immunogenomics.gl.service.AllowNewAlleles=true
org.immunogenomics.gl.service.AllowNewLoci=true
```

For example, if an allele list represented in GL String format by `HLA-A*01:01:01:01/HLA-Z*99:99:99:99` was registered against an instance of the genotype list service with default behavior, a new locus `HLA-Z` and a new allele `HLA-Z*99:99:99:99` would be created in order to correctly represent the allele list.

<br />
For those instances of the genotype list service populated with a specific nomenclature, this default behavior can be overridden so that genotype list resources which do not adhere to the nomenclature cannot be registered.  Any attempts to register an invalid genotype list resource will result in an HTTP 400 error.

Specify the following in `gl-service.properties` to enable this _strict mode_:

```
org.immunogenomics.gl.service.AllowNewAlleles=false
org.immunogenomics.gl.service.AllowNewLoci=false
```