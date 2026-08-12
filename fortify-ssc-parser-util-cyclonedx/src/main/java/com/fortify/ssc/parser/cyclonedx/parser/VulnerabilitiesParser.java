package com.fortify.ssc.parser.cyclonedx.parser;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanEntry;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.ssc.parser.cyclonedx.domain.Bom;
import com.fortify.ssc.parser.cyclonedx.domain.Vulnerability;
import com.fortify.util.io.Region;
import com.fortify.util.json.ExtendedJsonParser;
import com.fortify.util.ssc.parser.json.ScanDataStreamingJsonParser;

/**
 * This class parses a CycloneDX JSON input document to generate Fortify
 * vulnerabilities.
 * 
 * @author Ruud Senden
 */
public final class VulnerabilitiesParser {
    private final ScanData scanData;
    private final ScanEntry scanEntry;
    private final VulnerabilitiesProducer vulnerabilitiesProducer;
    private final ObjectMapper objectMapper;

    /**
     * Constructor for storing {@link ScanData} and {@link VulnerabilityHandler}
     * instances.
     * 
     * @param scanData
     * @param scanEntry
     * @param vulnerabilityHandler
     */
    public VulnerabilitiesParser(final ScanData scanData, ScanEntry scanEntry,
            final VulnerabilityHandler vulnerabilityHandler) {
        this.scanData = scanData;
        this.scanEntry = scanEntry;
        this.vulnerabilitiesProducer = new VulnerabilitiesProducer(vulnerabilityHandler);
        this.objectMapper = new ObjectMapper();
    }

    public final void parse() throws ScanParsingException, IOException {
        new ScanDataStreamingJsonParser()
                .handler("/", jp -> parseBom(jp))
                .parse(scanData, scanEntry);
    }

    /**
     * Main method to commence parsing the CycloneDX document provided by the
     * configured {@link ScanData}.
     * 
     * @param jsonParser ExtendedJsonParser at root level
     * @param sourceInputStream InputStream for CachedObject byte range extraction
     * @throws IOException on parse failure
     */
    public final void parseBom(ExtendedJsonParser jsonParser) throws IOException {
        Bom bom = Bom.parseBom(jsonParser, scanData, scanEntry, objectMapper);
        parseVulnerabilities(bom);
    }

    /**
     * This method re-parses the CycloneDX <code>vulnerabilities</code> array, based
     * on the
     * input document {@link Region} previously collected in the given {@link Bom}
     * object. For each entry in the <code>vulnerabilities</code> array:
     * <ol>
     * <li>The JSON contents are mapped to a {@link Vulnerability} object</li>
     * <li>The {@link Vulnerability} and {@link Bom} objects are passed to the
     * {@link VulnerabilitiesProducer#produceVulnerability(Bom, Vulnerability)}
     * method to produce
     * the actual Fortify vulnerability (if applicable)</li>
     * </ol>
     * 
     * @param bom The {@link Bom} object containing the {@link Region} of the
     *            <code>vulnerabilities</code> array in the input document
     * @throws ScanParsingException
     * @throws IOException
     */
    private final void parseVulnerabilities(final Bom bom) throws IOException {
        new ScanDataStreamingJsonParser()
                .expectedStartTokens(JsonToken.START_ARRAY)
                .handler("/*", Vulnerability.class, vuln -> {
                    try {
                        vulnerabilitiesProducer.produceVulnerabilities(bom, vuln);
                    } catch (IOException e) {
                        throw new RuntimeException("Failed to produce vulnerability", e);
                    }
                })
                .parse(scanData, scanEntry, bom.getVulnerabilitiesRegion());
    }
}
