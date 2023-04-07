# Fortify SSC Parser Plugin Utility Libraries 


<!-- START-INCLUDE:p.marketing-intro.md -->

[Fortify Application Security](https://www.microfocus.com/en-us/solutions/application-security) provides your team with solutions to empower [DevSecOps](https://www.microfocus.com/en-us/cyberres/use-cases/devsecops) practices, enable [cloud transformation](https://www.microfocus.com/en-us/cyberres/use-cases/cloud-transformation), and secure your [software supply chain](https://www.microfocus.com/en-us/cyberres/use-cases/securing-the-software-supply-chain). As the sole Code Security solution with over two decades of expertise and acknowledged as a market leader by all major analysts, Fortify delivers the most adaptable, precise, and scalable AppSec platform available, supporting the breadth of tech you use and integrated into your preferred toolchain. We firmly believe that your great code [demands great security](https://www.microfocus.com/cyberres/application-security/developer-security), and with Fortify, go beyond 'check the box' security to achieve that.

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

The only warranties for products and services of Open Text and its affiliates and licensors (“Open Text”) are as may be set forth in the express warranty statements accompanying such products and services. Nothing herein should be construed as constituting an additional warranty. Open Text shall not be liable for technical or editorial errors or omissions contained herein. The information contained herein is subject to change without notice.

The software is provided "as is" and is not supported through the regular OpenText Support channels. Support requests may be submitted through the [GitHub Issues](https://github.com/fortify/fortify-ssc-parser-util/issues) page for this repository. A (free) GitHub account is required to submit new issues or to comment on existing issues. 

Support requests created through the GitHub Issues page may include bug reports, enhancement requests and general usage questions. Please avoid creating duplicate issues by checking whether there is any existing issue, either open or closed, that already addresses your question, bug or enhancement request. If an issue already exists, please add a comment to provide additional details if applicable.

Support requests on the GitHub Issues page are handled on a best-effort basis; there is no guaranteed response time, no guarantee that reported bugs will be fixed, and no guarantee that enhancement requests will be implemented. If you require dedicated support for this and other Fortify software, please consider purchasing OpenText Fortify Professional Services. OpenText Fortify Professional Services can assist with general usage questions, integration of the software into your processes, and implementing customizations, bug fixes, and feature requests (subject to feasibility analysis). Please contact your OpenText Sales representative or fill in the [Professional Services Contact Form](https://www.microfocus.com/en-us/cyberres/contact/professional-services) to obtain more information on pricing and the services that OpenText Fortify Professional Services can provide.

---

*[This document was auto-generated from README.template.md; do not edit by hand](https://github.com/fortify/shared-doc-resources/blob/main/USAGE.md)*
