# Fortify SSC Parser Plugin utility library 
This project provides various utility classes for implementing custom 
SSC parser plugins. For now, this is mostly oriented at parser plugins 
that need to parse JSON data, however in the future support for other
input formats may be added.

## Overview
The main purpose of this project is to separate the technical aspects of 
parsing JSON data from the functional logic for handling actual JSON contents.
As such, this project provides utility classes that handle the technical aspects
of JSON parsing, such that actual parser plugins can focus on the functional logic
for handling actual JSON contents.

The following projects provide examples on how this library can be used:

- https://github.com/fortify-ps/fortify-ssc-parser-owasp-dependency-check
- https://github.com/fortify-ps/fortify-ssc-parser-sarif
- (Not migrated to this library yet: https://github.com/fortify-ps/fortify-ssc-parser-sample)

## Main library components

Following is a short overview of the main components provided by this library:

- AbstractStreamingJsonParser: Handles the technical JSON parsing aspects.

TODO

## Adding this project as a parser plugin dependency

The Maven artifacts for releases of this project are provided through 
JFrog Bintray. To use these artifacts, add the following repository
to your Gradle `repositories` closure:

```
   // repository with fortify-ps releases
   maven { url "https://dl.bintray.com/fortify-ps/maven" }
```

Based on the SSC sample parser Gradle layout, this project can then be
declared as a dependency by adding the following dependency to your Gradle
`dependencies` closure, replacing `1.0.0` with the actual version
you want to use (see [https://bintray.com/fortify-ps/maven] for available
versions):

```
   compileExport(group: 'com.fortify.ps.ssc.parser.util', name: 'fortify-ssc-parser-util', version:'1.0.0') { transitive = true }
```

### Snapshot dependency

Usually your parser plugin should use only released versions of this project.
However, if you are doing simultaneous development on both this project and
an actual parser plugin, you may want to declare a dependency on a snapshot
version of this project.

Snapshot versions are provided through JFrog Artifactory, and can be referenced
as stated below.

Repository to be added to the Gradle `repositories` closure:

```
   // repository with fortify-ps snapshots
   maven { url "https://oss.jfrog.org/artifactory/oss-snapshot-local" }
```

Dependency to be added to the Gradle `dependencies` closure, replacing 
`1.0.0-SNAPSHOT` with the actual snapshot version you want to use:

```
   compileExport(group: 'com.fortify.ps.ssc.parser.util', name: 'fortify-ssc-parser-util', version:'1.0.0-SNAPSHOT') { transitive = true }
```


## Build Environment

This project is managed using the included gradle wrapper scripts.
The commands listed in the following sections simply state `gradlew`
commands; you will need to run this as `./gradlew` on Linux/bash 
based systems.

For example, to list all available tasks, you would run one of the following
commands from the project directory:

- Windows: `gradlew tasks`
- Linux/bash: `./gradlew tasks`

### Build

To build, simply invoke the gradle wrapper with the `build` task:

`gradlew build`

### Versioning

Project version is automatically managed by the `com.intershop.gradle.scmversion`
plugin based on Git tags. In order to run tasks provided by this plugin, you will need
to set the `SCM_USERNAME` and `SCM_PASSWORD` environment variables corresponding
to your GitHub username and password. When using two-factor authentication, you will need
to use a personal access token instead of the actual password. 

After committing and pushing all changes, a new version of this project can be tagged by 
running the following command:

`gradlew tag`

This command will create a new Git tag based on the current version number, using the
format `RELEASE_<major>.<minor>.<patch>`.

After tagging for example version `1.0.0`, any subsequent commits will result
in the version number being updated to `1.1.0-SNAPSHOT`. Running `gradlew tag`
again will result in the corresponding `RELEASE_1.1.0` tag being created, after
which you can continue working on the next snapshot version.

Note that the plugin also provides a `release` task. For our development process it 
is not recommended to use this task, as it will result in a detached HEAD. 

### Deployment to local Maven repository

Based on the `maven-publish` plugin, the current workspace state can be
published as Maven artifacts to your local Maven repository using the following command:

`gradlew publish`

This is particularly useful if you are doing simultaneous development on both this 
project and an actual parser plugin.

### Deployment to public repositories

Deployment to public repositories is done using the following two plugins:

- `com.jfrog.bintray` to deploy release versions to JFrog Bintray
- `com.jfrog.artifactory` to deploy snapshot versions to JFrog Artifactory

#### Remainder of this section is not yet applicable until Travis CI issues have been resolved

Invoking the corresponding deployment tasks usually should not be done manually, 
but is instead managed through `.travis.yml`. 

Every commit will trigger a new build on [travis-ci.org]; this build will automatically 
deploy the snapshot version to JFrog Artifactory.

Tagging a release (see previous section) will also trigger a new build on [travis-ci.org],
but the release version will be deployed to JFrog Bintray instead.  
