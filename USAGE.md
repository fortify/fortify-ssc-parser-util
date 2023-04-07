
<!-- START-INCLUDE:repo-usage.md -->

# Fortify SSC Parser Plugin Utility Libraries - Usage Instructions

### API
For information how to use the API provided by this project, please refer to the JavaDoc. Many Fortify SSC parser plugins hosted at https://github.com/fortify utilize this project, so you may refer to the source code of these plugins to view examples on how to utilize the API provided by this project:

* **Sample Projects using fortify-ssc-parser-util**
    * JSON: https://github.com/fortify/fortify-ssc-parser-sample
    * JSON: https://github.com/fortify/fortify-ssc-parser-owasp-dependency-check
    * JSON: https://github.com/fortify/fortify-ssc-parser-clair-yair
    * JSON: https://github.com/fortify/fortify-ssc-parser-clair-rest
    * JSON: https://github.com/fortify/fortify-ssc-parser-symfony-security-checker
    * JSON: https://github.com/fortify/fortify-ssc-parser-php-security-checker
    * JSON: https://github.com/fortify/fortify-ssc-parser-tenable-io-cs
    * JSON: https://github.com/fortify/fortify-ssc-parser-sarif
    * XML: https://github.com/fortify/fortify-ssc-parser-burp
    * CycloneDX: https://github.com/fortify/fortify-ssc-parser-generic-cyclonedx
    * CycloneDX: https://github.com/fortify/fortify-ssc-parser-debricked-cyclonedx
    * CycloneDX: https://github.com/fortify/fortify-ssc-parser-owasp-dependency-track-cyclonedx

### Build System
The Maven artifacts for this project are automatically deployed to the following Maven repositories:

* **Maven Repositories**
    * **Releases**: https://repo1.maven.org/maven2/ 
    * **Snapshots**: https://s01.oss.sonatype.org/content/repositories/snapshots/

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

<!-- END-INCLUDE:repo-usage.md -->


---

*[This document was auto-generated from USAGE.template.md; do not edit by hand](https://github.com/fortify/shared-doc-resources/blob/main/USAGE.md)*
