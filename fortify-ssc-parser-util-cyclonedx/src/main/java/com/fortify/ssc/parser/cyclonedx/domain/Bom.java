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
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanEntry;
import com.fortify.util.cache.CachedObject;
import com.fortify.util.cache.CachedObjectHashMap;
import com.fortify.util.io.Region;
import com.fortify.util.json.ExtendedJsonParser;
import com.fortify.util.json.StreamingJsonParser;

import lombok.Getter;
import lombok.Setter;

public final class Bom implements Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = LoggerFactory.getLogger(Bom.class);

    @Getter @Setter private BomFormat bomFormat;
    @Getter @Setter private String specVersion;
    // private String serialNumber;
    // private Integer version;
    @Getter @Setter private BomMetadata metadata;
    private final CachedObjectHashMap<String, Component> componentsByBomRef;
    // private Service[] services;
    // private ExternalReference[] externalReferences;
    // private Dependency[] dependencies;
    // private Composition[] compositions;
    @Getter private Region vulnerabilitiesRegion = null;
    // @JsonProperty private JSFSignature[] signature;

    public static enum BomFormat {
        CycloneDX
    }

    /**
     * Private constructor; instances can be created through the
     * {@link #parseBom(ExtendedJsonParser, InputStream, ObjectMapper)}
     * method.
     */
    private Bom() {
        // Use CachedObject wrappers for memory-efficient storage.
        // Components are cached with their byte regions, enabling lazy reload on
        // garbage collection. This replaces MapDB (45MB) with lightweight SoftReference
        // caching, significantly reducing JAR size and dependencies.
        this.componentsByBomRef = new CachedObjectHashMap<>();
    }

    /**
     * Parse CycloneDX BOM document with cached component storage.
     * 
     * Components are wrapped in CachedObject for memory efficiency:
     * - Fast path: Components served from SoftReference cache (~1 microsecond)
     * - Slow path (rare): Components re-parsed from Region on GC
     * 
     * @param jsonParser        ExtendedJsonParser positioned at root
     * @param sourceInputStream Source stream (must remain open during
     *                          parseVulnerabilities)
     * @param objectMapper      Jackson ObjectMapper for component deserialization
     * @return Parsed BOM with cached components
     * @throws IOException on parse failure
     */
    public static final Bom parseBom(ExtendedJsonParser jsonParser, ScanData scanData,
            ScanEntry scanEntry, ObjectMapper objectMapper) throws IOException {
        Bom bom = new Bom();
        StreamingJsonParser streamingParser = new StreamingJsonParser()
                .handler("/bomFormat", BomFormat.class, bom::setBomFormat)
                .handler("/specVersion", String.class, bom::setSpecVersion)
                .handler("/metadata", BomMetadata.class, bom::setMetadata)
                .handler("/vulnerabilities", bom::setVulnerabilitiesRegion)
                .handler("/components/*", jp -> bom.setComponentCached(jp, scanData, scanEntry, objectMapper));
        streamingParser.parseObjectProperties(jsonParser, "/");
        return bom;
    }

    /**
     * Parse and cache a single component with byte region tracking.
     * 
     * @param jp                JsonParser positioned at component object
     * @param sourceInputStream Source stream for region-based reload
     * @param objectMapper      ObjectMapper for deserialization
     * @throws IOException on parse failure
     */
    private final void setComponentCached(JsonParser jp, ScanData scanData, ScanEntry scanEntry,
            ObjectMapper objectMapper) throws IOException {
        try {
            CachedObject<Component> cached = CachedObject.parse(
                    jp, Component.class, scanData, scanEntry, objectMapper);
            Component component = cached.getOrReload();
            componentsByBomRef.put(component.getBomRef(), cached);
            LOG.trace("Added cached component: bomRef={}, cache_status={}",
                    component.getBomRef(), cached.getCacheStatus());
        } catch (IOException e) {
            LOG.error("Failed to parse component with byte region", e);
            throw new RuntimeException("Component parsing failed", e);
        }
    }

    /**
     * Get component by bomRef, reloading from Region if needed.
     * 
     * @param bomRef BOM reference identifier
     * @return Component, re-parsed if necessary; null if not found
     * @throws IOException if reload fails
     */
    public final Component getComponentByBomRef(String bomRef) throws IOException {
        return componentsByBomRef.getCachedObject(bomRef);
    }

    private final void setVulnerabilitiesRegion(ExtendedJsonParser jp) throws IOException {
        this.vulnerabilitiesRegion = jp.getObjectOrArrayRegion();
    }

    @Getter
    public static final class BomMetadata {
        @JsonProperty
        private Date timestamp;
        @JsonProperty
        @JsonDeserialize(using = BomToolsDeserializer.class)
        private BomTool[] tools;
        // @JsonProperty private BomAuthor[] authors;
        // @JsonProperty private Component component;
        // @JsonProperty private BomManufacturer manufacture;
        // @JsonProperty private BomSupplier manufacture;
        // @JsonProperty private BomLicense[] licenses;
        // @JsonProperty private Property[] properties;
    }

    @Getter
    public static final class BomTool {
        @JsonProperty
        private String vendor;
        @JsonProperty
        private String name;
        @JsonProperty
        private String version;
        // @JsonProperty private Hash[] hashes;
        // @JsonProperty private ExternalReference[] externalReferences;
    }

    public final String getToolName() {
        String toolName = "Unknown";
        if (getMetadata() != null && ArrayUtils.isNotEmpty(getMetadata().getTools())) {
            BomTool mainTool = getMetadata().getTools()[0];
            toolName = StringUtils.defaultIfBlank(mainTool.getVendor() + " ", "")
                    + StringUtils.defaultIfBlank(mainTool.getName() + " ", "")
                    + StringUtils.defaultIfBlank(mainTool.getVersion() + " ", "");
            // Remove duplicate words, for example if tool name repeats vendor name
            toolName = Arrays.stream(toolName.split("\\s+")).distinct().collect(Collectors.joining(" "));
        }
        return toolName;
    }
}
