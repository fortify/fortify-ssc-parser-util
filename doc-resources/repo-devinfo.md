## Information for Developers

The following sections provide information that may be useful for developers of the fortify-ssc-parser-util project.

{{include:devinfo/h3.release-please.md}}

The `build.gradle` script reads the version number of the last published release from `version.txt`, by default this will result in a version named `x.y.z.SP-SNAPSHOT`, indicating that this is a snapshot of an upcoming Service Pack release. Note the 'Service Pack' here just means 'the next version after the last published release'. We never actually release service packs; once the 'Service Pack' is ready for release, a new version number will be generated by `release-please-action` based on the Conventional Commit messages.

This default behavior can be modified by passing the `-PisReleaseVersion` property, in which case the `build.gradle` will generate a version named `x.y.z.RELEASE`. This should fit nicely with [Gradle semantics](https://docs.gradle.org/current/userguide/single_versions.html), which states that for example `1.0-RELEASE < 1.0-SP1`. 

{{include:devinfo/h3.gradle-wrapper.md}}

### Commonly used commands

All commands listed below use Linux/bash notation; adjust accordingly if you are running on a different platform. All commands are to be executed from the main project directory.

* `./gradlew tasks --all`: List all available tasks
* Build & publish:
  * `./gradlew clean build`: Clean and build the project
  * `./gradlew build`: Build the project without cleaning
  * `./gradlew publishToMavenLocal`: Publish this build as a snapshot version to the local Maven repository for testing purposes
  * `./gradlew publishToOSSRH`: Publish this build as a snapshot version to OSSRH; usually only done from a GitHub Actions workflow
  * `./gradlew publishToOSSRH closeOSSRHStagingRepository -PisReleaseVersion=true`: Publish this build as a release version to the OSSRH staging area; use this for first-time publishing to check release contents
  * `./gradlew publishToOSSRH closeAndReleaseOSSRHStagingRepository -PisReleaseVersion=true`: Publish this build as a release version to Maven Central; usually only done from a GitHub Actions workflow
  
All OSSRH-related tasks require the following Gradle properties to be set:

* `signingKey`: GPG secret key used to sign the artifacts
* `signingPassword`: Password for the GPG secret key
* `OSSRHUsername`: Sonatype OSSRH user name
* `OSSRHPassword`: Sonatype OSSRH password

These properties can be set on the command line, in a local `gradle.properties` file, or through environment variables named `ORG_GRADLE_PROJECT_<propertyName>`. For automated build pipelines, it is recommended to use a Sonatype OSSRH token rather than actual username and password.

### Automated Builds & publishing

A GitHub Actions `ci.yml` workflow is used to automatically build and publish both snapshot versions and release versions to OSSRH/Maven Central:

{{include:nocomments.li.repo-target-maven-repos.md}}
