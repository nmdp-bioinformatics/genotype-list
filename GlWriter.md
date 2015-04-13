Contents:


# Introduction #

The GL Service utilizes the `GlWriter` to write GLResources to a specific format.

In brief, the GL Writer:
  * Converts retrieved GLResources to the desired file format for output.

# Details #

## Components ##

The following classes are _part_ of the GL Writer:
| **Name** | **Function** |
|:---------|:-------------|
| [GlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/GlWriter.java) | Interface - Write a specified resource to a specified writer |
| [AbstractTemplatedGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/AbstractTemplatedGlWriter.java)| Abstract Templated Writer, implements GlWriter |
| [GlstringGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/GlstringGlWriter.java) | implements GlWriter |
| [HtmlGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/HtmlGlWriter.java) | extends [AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) |
| http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/JsonGlWriter.java[ JsonGlWriter.java] | implements GlWriter |
| [N3GlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/N3GlWriter.java) | extends [AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) |
| [RdfGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/RdfGlWriter.java) | extends [AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) |
| [XlinkXmlGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/XlinkGlWriter.java) | extends [AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) |
| [XmlGlWriter.java](http://code.google.com/p/genotype-list/source/browse/trunk/gl-service/src/main/java/org/immunogenomics/gl/service/writer/XmlGlWriter.java) | extends [AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) |


The following classes are _referenced_ by the GL Writer:
| **Name** | **Function** |
|:---------|:-------------|
| None | n/a |

## How it Works ##

### Structure ###

The `{format}GlWriter` **extends** `AbstractTemplatedGlWriter`, which **implements** `GlWriter`.

`GlWriter` is an interface that declares required `getContentType()` and `writeXXX()` methods for use in every type of `GlWriter`.

[AbstractTemplatedGlWriter](GlWriter#AbstractTemplatedGlWriter.md) is an abstract class that defines the `writeXXX(resource, writer)` methods for various GLResources and arbitrary writers. It also contains undefined templates representing the file format, to be defined by each specific writer.

The various format-specific `GlWriter`s provide templates to hold the actual written content. Some writers do not extend the Abstract Template, but rather define their own `writeXXX()` methods and implement the `GlWriter` directly.

### Initializing the Writer ###

There are several different format-specific implementations of the `GlWriter`.
When the SparkGlService is initialized, all types of the `GlWriter` are passed in via a map linking each file extension to each writer.

```
SparkGlService(@Namespace final String ns, final GlReader glReader, final IdResolver idResolver, final Map<String, GlWriter> glWriters)
```

### Writing ###

The GlWriter is typically called from [Spark](SparkGlService.md) within HTTP GET operations, passing in the GL Resource and looking up the writer by file extension.

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

After it has been called, the writer inserts the information contained by the GL Resource into the template, and passes it back to [Spark](SparkGlService.md), where it is output as a string in the header location.

## AbstractTemplatedGlWriter ##

The `AbstractTemplatedGlWriter` acts as a core for many format-specific GlWriters. It simply provides the two main components needed for conversion: `writeXXX()` methods and undefined templates for each type of [GL Resource](GLResources.md), to be determined by the requirements of each file format.

The write methods are specific to each resource type, and are declared in the format `writeXXX(resource, writer)`. These methods do the actual conversion, using templates provided by the writer to fill in information.

Using Genotype as an example:
```
@Override
    public final void writeGenotype(final Genotype genotype, final Writer writer) throws IOException {
        checkNotNull(genotype);
        checkNotNull(writer);
        VelocityContext context = new VelocityContext();
        context.put("genotype", genotype);
        genotypeTemplate.merge(context, writer);
    }
```

The templates are also specific to each resource type, and contain a framework to hold the retrieved content. For example, the `XmlGlWriter` provides resource-specific templates that, when filled, validate to an XML Schema (gl-resource.xsd).

Templates in the `AbstractTemplatedGlWriter`:
```
protected Template locusTemplate;
protected Template alleleTemplate;
protected Template alleleListTemplate;
protected Template haplotypeTemplate;
protected Template genotypeTemplate;
protected Template genotypeListTemplate;
protected Template multilocusUnphasedGenotypeTemplate;
```

The only GlWriters that do not take advantage of the `AbstractTemplatedGlWriter` have implemented the `writeXXX()` methods separately for various reasons.

