# Fortify SSC Parser Plugin Utility Libraries 


<!-- START-INCLUDE:p.marketing-intro.md -->

Build secure software fast with [Fortify](https://www.microfocus.com/en-us/solutions/application-security). Fortify offers end-to-end application security solutions with the flexibility of testing on-premises and on-demand to scale and cover the entire software development lifecycle.  With Fortify, find security issues early and fix at the speed of DevOps. 

<!-- END-INCLUDE:p.marketing-intro.md -->



<!-- START-INCLUDE:repo-intro.md -->

The **fortify-ssc-parser-util** project provides various utility classes for implementing custom SSC parser plugins. For now, this is mostly oriented at parser plugins that need to parse JSON or XML data, however support for other input formats may be added in the future.

The main purpose of this project is to separate the technical aspects of parsing JSON or XML data, from the functional logic for handling actual JSON or XML contents. As such, this project provides utility classes that handle the technical parsing aspects, such that parser plugin implementations can focus on the functional logic for handling actual contents.

In addition, this project provides modules for parsing standard formats, like CycloneDX, to allow both generic and product-specific parser plugins to be developed. For example, if a particular product provides output in CycloneDX format, a product-specific parser plugin could reuse the generic CycloneDX parsing functionality while using a product-specific engine type.

<!-- END-INCLUDE:repo-intro.md -->


## Resources


<!-- START-INCLUDE:repo-resources.md -->

* **Usage**: [USAGE.md](USAGE.md)
* **Source code**: https://github.com/fortify/fortify-ssc-parser-util
* **Automated builds**: https://github.com/fortify/fortify-ssc-parser-util/actions
* **Maven Repositories**
    * **Releases**: https://repo1.maven.org/maven2/ 
    * **Snapshots**: https://s01.oss.sonatype.org/content/repositories/snapshots/
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
* **Contributing Guidelines**: [CONTRIBUTING.md](CONTRIBUTING.md)
* **Code of Conduct**: [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md)
* **License**: [LICENSE.txt](LICENSE.txt)

<!-- END-INCLUDE:repo-resources.md -->


## Support

The software is provided "as is", without warranty of any kind, and is not supported through the regular Micro Focus Support channels. Support requests may be submitted through the [GitHub Issues](https://github.com/fortify/fortify-ssc-parser-util/issues) page for this repository. A (free) GitHub account is required to submit new issues or to comment on existing issues. 

Support requests created through the GitHub Issues page may include bug reports, enhancement requests and general usage questions. Please avoid creating duplicate issues by checking whether there is any existing issue, either open or closed, that already addresses your question, bug or enhancement request. If an issue already exists, please add a comment to provide additional details if applicable.

Support requests on the GitHub Issues page are handled on a best-effort basis; there is no guaranteed response time, no guarantee that reported bugs will be fixed, and no guarantee that enhancement requests will be implemented. If you require dedicated support for this and other Fortify software, please consider purchasing Micro Focus Fortify Professional Services. Micro Focus Fortify Professional Services can assist with general usage questions, integration of the software into your processes, and implementing customizations, bug fixes, and feature requests (subject to feasibility analysis). Please contact your Micro Focus Sales representative or fill in the [Professional Services Contact Form](https://www.microfocus.com/en-us/cyberres/contact/professional-services) to obtain more information on pricing and the services that Micro Focus Fortify Professional Services can provide.

---

*This document was auto-generated from README.template.md; do not edit by hand*
