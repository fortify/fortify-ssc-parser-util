package com.fortify.ssc.parser.cyclonedx.parser;

import java.io.IOException;

import org.mapdb.DB;
import org.mapdb.DBMaker;

import com.fasterxml.jackson.core.JsonToken;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;
import com.fortify.plugin.api.VulnerabilityHandler;
import com.fortify.ssc.parser.cyclonedx.domain.Bom;
import com.fortify.ssc.parser.cyclonedx.domain.Vulnerability;
import com.fortify.util.io.Region;
import com.fortify.util.json.ExtendedJsonParser;

/**
 * This class parses a CycloneDX JSON input document to generate Fortify vulnerabilities.
 * 
 * @author Ruud Senden
 */
public final class VulnerabilitiesParser {
	private final ScanData scanData;
	private final VulnerabilitiesProducer vulnerabilitiesProducer;
	
	/**
	 * Constructor for storing {@link ScanData} and {@link VulnerabilityHandler}
	 * instances.
	 * @param scanData
	 * @param vulnerabilityHandler
	 */
	public VulnerabilitiesParser(final ScanData scanData, final VulnerabilityHandler vulnerabilityHandler) {
		this.scanData = scanData;
		this.vulnerabilitiesProducer = new VulnerabilitiesProducer(vulnerabilityHandler);
	}
	
	public final void parse() throws ScanParsingException, IOException {
		new CycloneDXScanDataStreamingJsonParser()
			.handler("/", this::parseBom)
			.parse(scanData);
	}
	
	/**
	 * Main method to commence parsing the CycloneDX document provided by the
	 * configured {@link ScanData}.
	 * @throws IOException
	 */
	public final void parseBom(ExtendedJsonParser jsonParser) throws IOException {
		try ( DB db = DBMaker.tempFileDB()
				.closeOnJvmShutdown().fileDeleteAfterClose()
				.fileMmapEnableIfSupported()
				.make() ) {
			Bom bom = Bom.parseBom(db, jsonParser);
			parseVulnerabilities(bom);
		}
	}

	/**
	 * This method re-parses the CycloneDX <code>vulnerabilities</code> array, based on the
	 * input document {@link Region} previously collected in the given {@link Bom}
	 * object. For each entry in the <code>vulnerabilities</code> array:
	 * <ol>
	 *  <li>The JSON contents are mapped to a {@link Vulnerability} object</li>
	 *  <li>The {@link Vulnerability} and {@link Bom} objects are passed to the
	 *      {@link VulnerabilitiesProducer#produceVulnerability(Bom, Vulnerability)} method to produce
	 *      the actual Fortify vulnerability (if applicable)</li>
	 * </ol>
	 * @param runData
	 * @throws IOException
	 */
	private final void parseVulnerabilities(final Bom bom) throws IOException {
		new CycloneDXScanDataStreamingJsonParser()
			.expectedStartTokens(JsonToken.START_ARRAY)
			.handler("/*", Vulnerability.class, vuln->vulnerabilitiesProducer.produceVulnerabilities(bom, vuln))
			.parse(scanData, bom.getVulnerabilitiesRegion());
	}
}
