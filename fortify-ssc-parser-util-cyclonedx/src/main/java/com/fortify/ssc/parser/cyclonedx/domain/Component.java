/*******************************************************************************
 * (c) Copyright 2020 Micro Focus or one of its affiliates
 *
 * Permission is hereby granted, free of charge, to any person obtaining a 
 * copy of this software and associated documentation files (the 
 * "Software"), to deal in the Software without restriction, including without 
 * limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to 
 * whom the Software is furnished to do so, subject to the following 
 * conditions:
 * 
 * The above copyright notice and this permission notice shall be included 
 * in all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY 
 * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE 
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF 
 * CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN 
 * CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS 
 * IN THE SOFTWARE.
 ******************************************************************************/
package com.fortify.ssc.parser.cyclonedx.domain;

import java.io.Serializable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fortify.util.mapdb.CustomSerializerElsa;

import lombok.Getter;

@Getter
public final class Component implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final CustomSerializerElsa<Component> SERIALIZER = new CustomSerializerElsa<>(Component.class);
	@JsonProperty private ComponentType type;
	@JsonProperty("bom-ref") private String bomRef;
	// @JsonProperty private Supplier supplier;
	// @JsonProperty private String author;
	// @JsonProperty private String publisher;
	@JsonProperty private String group;
	@JsonProperty private String name;
	@JsonProperty private String version;
	@JsonProperty private String description;
	@JsonProperty private ComponentScope scope;
	// @JsonProperty private ComponentHash[] hashes;
	@JsonProperty private ComponentLicenseEntry[] licenses;
	// @JsonProperty private String copyright;
	// @JsonProperty private String cpe;
	@JsonProperty private PackageUrl purl;
	// @JsonProperty private ComponentSWID swid;
	// @JsonProperty private ComponentPedigree pedigree;
	// @JsonProperty private ExternalReference[] externalReferences;
	// @JsonProperty private Component[] components;
	// @JsonProperty private ComponentEvidence evidence;
	// @JsonProperty private ComponentReleaseNotes releaseNotes;
	// @JsonProperty private Property properties;
	// @JsonProperty private JSFSignature signature;
	
	public static enum ComponentType {
		application, framework, library, container, 
		@JsonProperty("operating-system") operating_system, device, firmware, file 
	}
	
	public static enum ComponentScope {
		required, optional, excluded 
	}

	public final String getScopeName() {
		return scope==null ? "unknown" : scope.toString();
	}
	
	public String getPurlAsString() {
		return purl==null ? null : purl.getPurl();
	}
	
	public String getPackageType() {
		return purl==null ? null : purl.getPackageType();
	}
	
	public String getGroup() {
		return StringUtils.isNotBlank(group) ? group : (purl==null ? null : purl.getGroup());
	}
	
	public String getName() {
		return StringUtils.isNotBlank(name) ? name : (purl==null ? null : purl.getName());
	}
	
	public String getVersion() {
		return StringUtils.isNotBlank(version) ? version : (purl==null ? null : purl.getVersion());
	}

	public String getLicensesAsString() {
		String result = "unknown";
		if ( licenses!=null && licenses.length>0 ) {
			result = Stream.of(licenses)
				.map(ComponentLicenseEntry::getLicenseIdOrName)
				.filter(StringUtils::isNotBlank)
				.collect(Collectors.joining(", "));
		}
		return result;
	}
}