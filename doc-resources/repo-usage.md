# {{var:repo-title}} - Usage Instructions

### API
For information how to use the API provided by this project, please refer to the JavaDoc. Many Fortify SSC parser plugins hosted at https://github.com/fortify utilize this project, so you may refer to the source code of these plugins to view examples on how to utilize the API provided by this project:

{{include:nocomments.li.repo-sample-projects.md}}

### Build System
The Maven artifacts for this project are automatically deployed to the following Maven repositories:

{{include:nocomments.li.repo-target-maven-repos.md}}

Following is an example on how to add this library to your build.gradle script:

```groovy
dependencies {
   // For parsing JSON data
   implementationExport(group: 'com.fortify.ssc.parser.util', name: 'fortify-ssc-parser-util-json', version:'<version>') { transitive = true }
   // For parsing XML data
   implementationExport(group: 'com.fortify.ssc.parser.util', name: 'fortify-ssc-parser-util-xml', version:'<version>') { transitive = true }
}
```

Note that `implementationExport` and other parser-related Gradle functionality is defined in a separate Gradle helper script; you can either copy the relevant contents in your own build script, or reference an appropriate version of the build script, for example using a statement like the following in your build.gradle:

```groovy
apply from: "https://raw.githubusercontent.com/fortify/shared-gradle-helpers/1.8/ssc-parser-plugin-helper.gradle"
```

The configuration listed above will only allow access to release versions of this library. Usually it is not recommended to depend on snapshot versions of this library, but if necessary you can use the example below to access snapshot versions of this library. Note the additional repository definition, and the `changing: true` property on `implementationExport`.

```groovy
repositories {
   maven { url "https://s01.oss.sonatype.org/content/repositories/snapshots/" }
}

dependencies {
   implementationExport(group: 'com.fortify.ssc.parser.util', name: 'fortify-ssc-parser-util', version:'<version>', changing: true) { transitive = true }
}
```
