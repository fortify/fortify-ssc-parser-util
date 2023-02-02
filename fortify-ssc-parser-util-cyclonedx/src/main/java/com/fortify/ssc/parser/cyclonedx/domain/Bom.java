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

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mapdb.DB;
import org.mapdb.Serializer;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fortify.util.io.Region;
import com.fortify.util.json.ExtendedJsonParser;
import com.fortify.util.json.StreamingJsonParser;
import com.fortify.util.mapdb.CustomSerializerElsa;

import lombok.Getter;
import lombok.Setter;

public final class Bom implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final CustomSerializerElsa<Bom> SERIALIZER = new CustomSerializerElsa<>(Bom.class);
	@Getter @Setter private BomFormat bomFormat;
	@Getter @Setter private String specVersion;	
	// private String serialNumber;
	// private Integer version;
	@Getter @Setter private BomMetadata metadata;
	private final Map<String, Component> componentsByBomRef;
	// private Service[] services;
	// private ExternalReference[] externalReferences;
	// private Dependency[] dependencies;
	// private Composition[] compositions;
	@Getter private Region vulnerabilitiesRegion = null;
	//@JsonProperty private JSFSignature[] signature;
	
	public static enum BomFormat {
		CycloneDX
	}
	
	/**
	 * Private constructor; instances can be created through the {@link #parseRunData(DB, ExtendedJsonParser)}
	 * method.
	 * 
	 * @param db
	 */
	private Bom(final DB db) {
		// We assume large scans may include a lot of components, so we use disk-backed collections.
		// Note that alternatively we could use a hash & position-based approach like the SARIF .NET SDK
		// (see DeferredDictionary and DeferredList) to avoid serializing entries to disk, but for now
		// disk-backed collections seem to perform well and the implementation is much easier to understand.
		this.componentsByBomRef = db.hashMap("componentsById", Serializer.STRING, Component.SERIALIZER).create();
	}

	public static final Bom parseBom(DB db, ExtendedJsonParser jsonParser) throws IOException {
		Bom bom = new Bom(db);
		new StreamingJsonParser()
			.handler("/bomFormat", BomFormat.class, bom::setBomFormat)
			.handler("/specVersion", String.class, bom::setSpecVersion)
			.handler("/metadata", BomMetadata.class, bom::setMetadata)
			.handler("/components/*", Component.class, bom::addComponent)
			.handler("/vulnerabilities", bom::setVulnerabilitiesRegion)
			.parseObjectProperties(jsonParser, "/");
		return bom;
	}
	
	public final Component getComponentByBomRef(String bomRef) {
		return componentsByBomRef.get(bomRef);
	}
	
	private final void addComponent(Component component) {
		componentsByBomRef.put(component.getBomRef(), component);
	}
	
	private final void setVulnerabilitiesRegion(ExtendedJsonParser jp) throws IOException {
		this.vulnerabilitiesRegion = jp.getObjectOrArrayRegion();
	}
	
	@Getter
	public static final class BomMetadata {
		@JsonProperty private Date timestamp;
		@JsonProperty private BomTool[] tools;
		//@JsonProperty private BomAuthor[] authors;
		//@JsonProperty private Component component;
		//@JsonProperty private BomManufacturer manufacture;
		//@JsonProperty private BomSupplier manufacture;
		//@JsonProperty private BomLicense[] licenses;
		//@JsonProperty private Property[] properties;
	}
	
	@Getter
	public static final class BomTool {
		@JsonProperty private String vendor;
		@JsonProperty private String name;
		@JsonProperty private String version;
		//@JsonProperty private Hash[] hashes;
		//@JsonProperty private ExternalReference[] externalReferences;
	}
	
	public final String getToolName() {
		String toolName = "Unknown";
		if ( getMetadata()!=null && ArrayUtils.isNotEmpty(getMetadata().getTools()) ) {
			BomTool mainTool = getMetadata().getTools()[0];
			toolName = 
					StringUtils.defaultIfBlank(mainTool.getVendor()+" ", "") 
					+ StringUtils.defaultIfBlank(mainTool.getName()+" ", "")
					+ StringUtils.defaultIfBlank(mainTool.getVersion()+" ", "");
			// Remove duplicate words, for example if tool name repeats vendor name 
			toolName = Arrays.stream( toolName.split("\\s+")).distinct().collect(Collectors.joining(" ") );;
		}
		return toolName;
	}
}