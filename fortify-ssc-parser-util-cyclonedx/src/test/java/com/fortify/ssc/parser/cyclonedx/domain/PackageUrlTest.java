package com.fortify.ssc.parser.cyclonedx.domain;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


public class PackageUrlTest {
	@ParameterizedTest
	@CsvSource({
		"pkg:bitbucket/birkenfeld/pygments-main@244fd47e07d1014f0aed9c,bitbucket,birkenfeld,pygments-main,244fd47e07d1014f0aed9c",
		"pkg:deb/debian/curl@7.50.3-1?arch=i386&distro=jessie,deb,debian,curl,7.50.3-1",
		"pkg:docker/cassandra@sha256:244fd47e07d1004f0aed9c,docker,,cassandra,sha256:244fd47e07d1004f0aed9c",
		"pkg:docker/customer/dockerimage@sha256:244fd47e07d1004f0aed9c?repository_url=gcr.io,docker,customer,dockerimage,sha256:244fd47e07d1004f0aed9c",
		"pkg:gem/jruby-launcher@1.1.2?platform=java,gem,,jruby-launcher,1.1.2",
		"pkg:gem/ruby-advisory-db-check@0.12.4,gem,,ruby-advisory-db-check,0.12.4",
		"pkg:github/package-url/purl-spec@244fd47e07d1004f0aed9c,github,package-url,purl-spec,244fd47e07d1004f0aed9c",
		"pkg:golang/google.golang.org/genproto#googleapis/api/annotations,golang,google.golang.org,genproto,",
		"pkg:maven/org.apache.xmlgraphics/batik-anim@1.9.1?packaging=sources,maven,org.apache.xmlgraphics,batik-anim,1.9.1",
		"pkg:maven/org.apache.xmlgraphics/batik-anim@1.9.1?repository_url=repo.spring.io%2Frelease,maven,org.apache.xmlgraphics,batik-anim,1.9.1",
		"pkg:npm/%40angular/animation@12.3.1,npm,%40angular,animation,12.3.1",
		"pkg:npm/foobar@12.3.1,npm,,foobar,12.3.1",
		"pkg:nuget/EnterpriseLibrary.Common@6.0.1304,nuget,,EnterpriseLibrary.Common,6.0.1304",
		"pkg:pypi/django@1.11.1,pypi,,django,1.11.1",
		"pkg:rpm/fedora/curl@7.50.3-1.fc25?arch=i386&distro=fedora-25,rpm,fedora,curl,7.50.3-1.fc25",
		"pkg:rpm/opensuse/curl@7.56.1-1.1.?arch=i386&distro=opensuse-tumbleweed,rpm,opensuse,curl,7.56.1-1.1."
	})
	void testPackageUrl(String purl, String type, String group, String name, String version) throws Exception {
		PackageUrl packageUrl = new PackageUrl(purl);
		assertEquals(purl, packageUrl.getPurl());
		assertEquals(type, packageUrl.getPackageType());
		assertEquals(group, packageUrl.getGroup());
		assertEquals(name, packageUrl.getName());
		assertEquals(version, packageUrl.getVersion());
	}
}
