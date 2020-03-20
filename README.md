# Fortify SSC Parser Plugin utility library 

The **fortify-ssc-parser-util** project provides various utility classes 
for implementing custom SSC parser plugins. For now, this is mostly oriented 
at parser plugins that need to parse JSON data, however in the future support 
for other input formats may be added.

The main purpose of this project is to separate the technical aspects of 
parsing JSON data, from the functional logic for handling actual JSON contents.
As such, this project provides utility classes that handle the technical aspects
of JSON parsing, such that actual parser plugins can focus on the functional logic
for handling actual JSON contents.

### Related Links

* **Branches**: https://github.com/fortify-ps/fortify-ssc-parser-util/branches  
  Current development is usually done on latest snapshot branch, which may not be the default branch
* **Automated builds**: https://travis-ci.com/github/fortify-ps/fortify-ssc-parser-util
* **Maven Repositories**
  * **Snapshots**: https://oss.jfrog.org/artifactory/oss-snapshot-local
    * **Artifacts**: https://oss.jfrog.org/artifactory/oss-snapshot-local/com/fortify/ssc/parser/util/fortify-ssc-parser-util/
  * **Releases**: https://dl.bintray.com/fortify-ps/maven
    * **Artifacts**: https://dl.bintray.com/fortify-ps/maven/com/fortify/ssc/parser/util/fortify-ssc-parser-util/
* **Maven Artifacts**
  * **Snapshots**: https://oss.jfrog.org/artifactory/oss-snapshot-local/com/fortify/ssc/parser/util/fortify-ssc-parser-util/
  * **Releases**: https://dl.bintray.com/fortify-ps/maven/com/fortify/ssc/parser/util/fortify-ssc-parser-util/    
* **Sample Projects using fortify-client-api**
  * https://github.com/fortify-ps/fortify-ssc-parser-owasp-dependency-check
  * https://github.com/fortify-ps/fortify-ssc-parser-clair-rest
  * https://github.com/fortify-ps/fortify-ssc-parser-clair-yair
  * https://github.com/fortify-ps/fortify-ssc-parser-tenable-io-cs

## Usage

### API
Please refer to the JavaDoc and sample projects listed in the [Related Links](#related-links) section
for details on how to use this utility library.

### Build System
The Maven artifacts for this project are automatically deployed to
the Maven repositories listed in the [Related Links](#related-links) section.

Following is an example on how to add this library to your build.gradle 
script:

```groovy
repositories {
    maven { url "https://dl.bintray.com/fortify-ps/maven" }
}

dependencies {
    compileExport(group: 'com.fortify.ssc.parser.util', name: 'fortify-ssc-parser-util', version:'<version>') { transitive = true }
}
```

The configuration listed above will only allow access to release versions of this library.
Usually it is not recommended to depend on snapshot versions of this library, but if necessary
you can use the example below to access snapshot versions of this library. Note the additional
repository definition, and the `changing: true` property on `compileExport`.

```groovy
repositories {
    maven { url "https://dl.bintray.com/fortify-ps/maven" }
    maven { url "https://oss.jfrog.org/artifactory/oss-snapshot-local" }
}

dependencies {
    compileExport(group: 'com.fortify.ssc.parser.util', name: 'fortify-ssc-parser-util', version:'<version>', changing: true) { transitive = true }
}
```

Note that most projects in the fortify-ps organization use the
[repo-helper.gradle](https://github.com/fortify-ps/gradle-helpers/blob/1.0/repo-helper.gradle)
Gradle helper script to configure repositories; that script uses
slightly different repository settings than listed above in order to also 
allow access to snapshot builds. See the https://github.com/fortify-ps/gradle-helpers 
project for more information and other Gradle helper scripts.


## Information for library developers

The following sections provide information that may be useful for developers of the 
`fortify-ssc-parser-util` library.

### Gradle

It is strongly recommended to build this project using the included Gradle Wrapper
scripts; using other Gradle versions may result in build errors and other issues.

The Gradle build uses various helper scripts from https://github.com/fortify-ps/gradle-helpers;
please refer to the documentation and comments in included scripts for more information. 

### Commonly used commands

All commands listed below use Linux/bash notation; adjust accordingly if you
are running on a different platform. All commands are to be executed from
the main project directory.

* `./gradlew tasks --all`: List all available tasks
* Build & publish:
  * `./gradlew clean build`: Clean and build the project
  * `./gradlew build`: Build the project without cleaning
  * `./gradlew publish`: Publish the project to the local Maven repository, for use by other local projects. Should usually only be done from a snapshot branch; see [Versioning](#versioning).
* Version management:
  * `./gradlew printProjectVersion`: Print the current version
  * `./gradlew startSnapshotBranch -PnextVersion=2.0`: Start a new snapshot branch for an upcoming `2.0` version
  * `./gradlew releaseSnapshot`: Merge the changes from the current branch to the master branch, and create release tag
* `./fortify-scan.sh`: Run a Fortify scan; requires Fortify SCA to be installed

Note that the version management tasks operate only on the local repository; you will need to manually
push any changes (including tags and branches) to the remote repository.

### Versioning

The various version-related Gradle tasks assume the following versioning methodology:

* The `master` branch is only used for creating tagged release versions
* A branch named `<version>-SNAPSHOT` contains the current snapshot state for the upcoming release
* Optionally, other branches can be used to develop individual features, perform bug fixes, ...
  * However, note that the Gradle build may be unable to identify a correct version number for the project
  * As such, only builds from tagged versions or from a `<version>-SNAPSHOT` branch should be published to a Maven repository

### Automated Builds & publishing

Travis-CI builds are automatically triggered when there is any change in the project repository,
for example due to pushing changes, or creating tags or branches. If applicable, build artifacts 
are automatically published to a Maven repository:

* When building a tagged version, the Gradle `bintrayUpload` task will be invoked to upload the release version to JFrog Bintray
* When building a branch named `<version>-SNAPSHOT`, the Gradle `artifactoryPublish` task will be invoked to publish a snapshot version to JFrog Artifactory
* No artifacts will be deployed for any other build, for example when Travis-CI builds the `master` branch

See the [Related Links](#related-links) section for the relevant Travis-CI, Bintray and Artifactory links.


# Licensing
See [LICENSE.TXT](LICENSE.TXT)