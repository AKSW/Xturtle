# Xturtle vocabulary projects

We provide common vocabularies as Xturtle projects in order to allow easy
importing and referencing (see [usage documentation](https://github.com/AKSW/Xturtle/wiki/Usage)).

Currently, these projects are available.

## xturtle.core

This project contains the rdf, rdf schema, owl as well as xsd turtle files.
This most basic vocabulary project should be imported in every Xturtle
workspace and should be referenced in every project you create.

We provide slim versions of the rdf, rdf schema and owl 2 namespaces. "slim"
means, we removed triple which use other namespaces than from this project,
such as dublin core.

In addition to this, we created an [xsd namespace
document](https://github.com/AKSW/Xturtle/blob/vocabularies/xturtle.core/xsd.ttl)
to allow for internal reference and autocompletion of xml schema datatypes
which are suggested in the [datatype section of the OWL Web Ontology Language
Reference](http://www.w3.org/TR/owl-ref/#rdf-datatype).

## xturtle.geo, .schemaorg, .skos, ...

These specific vocabulary projects all reference (include) at least the
`xturtle.core` project and contain a specific vocabulary namespace document in
turtle syntax (sometimes, we hade to create slim versions, sometimes we had to
fix errors which encountered on loading a file).

## other

This directory just contains a lot of downloaded turtle files from the web.
Most of them will throw errors or need other namespaces documents / sub
projects. It is more a todo list than a project directory.

