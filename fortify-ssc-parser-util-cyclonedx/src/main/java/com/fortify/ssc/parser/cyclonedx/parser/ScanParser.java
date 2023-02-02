package com.fortify.ssc.parser.cyclonedx.parser;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;

import com.fortify.plugin.api.ScanBuilder;
import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanParsingException;

/**
 * This class parses the CycloneDX JSON to set the various {@link ScanBuilder}
 * properties.
 * 
 * @author Ruud Senden
 */
public class ScanParser {
	public static final String MSG_UNSUPPORTED_INPUT_FILE = "Unsupported input file";
	private final ScanData scanData;
    private final ScanBuilder scanBuilder;
    private String bomFormat;
    private int numFiles = 0;
    
	public ScanParser(final ScanData scanData, final ScanBuilder scanBuilder) {
		this.scanData = scanData;
		this.scanBuilder = scanBuilder;
	}
	
	public final void parse() throws ScanParsingException, IOException {
		new CycloneDXScanDataStreamingJsonParser()
			.handler("/bomFormat", jp -> bomFormat = jp.getValueAsString())
			.handler("/specVersion", jp -> scanBuilder.setEngineVersion(jp.getValueAsString()))
			.handler("/metadata/timestamp", jp -> scanBuilder.setScanDate(jp.readValueAs(Date.class)))
			//.handler("", jp -> scanBuilder.setHostName(jp.getValueAsString()))
			.handler("/serialNumber", jp -> scanBuilder.setBuildId(jp.getValueAsString()))
			.handler("/metadata/component/name", jp -> scanBuilder.setScanLabel(jp.getValueAsString()))
			.handler("/components", jp -> numFiles+=jp.countArrayEntries())
			.handler("/serialNumber", jp -> scanBuilder.setGuid(jp.getValueAsString()))
			.parse(scanData);
		
		checkBomFormat(bomFormat);
		scanBuilder.setNumFiles(numFiles);
		scanBuilder.completeScan();
	}

	private void checkBomFormat(String bomFormat) throws ScanParsingException {
		if ( StringUtils.isBlank(bomFormat) ) {
			throw new ScanParsingException(MSG_UNSUPPORTED_INPUT_FILE+"; required property bomFormat missing or empty");
		}
		if ( !"CycloneDX".equals(bomFormat) ) {
			throw new ScanParsingException(MSG_UNSUPPORTED_INPUT_FILE+"; unknown bomFormat "+bomFormat);
		}
	}
}
