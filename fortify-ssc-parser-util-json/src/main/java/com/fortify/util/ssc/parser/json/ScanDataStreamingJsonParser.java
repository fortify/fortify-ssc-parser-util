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
package com.fortify.util.ssc.parser.json;

import java.io.IOException;
import java.io.InputStream;

import com.fortify.plugin.api.ScanData;
import com.fortify.plugin.api.ScanEntry;
import com.fortify.util.io.Region;
import com.fortify.util.json.AbstractStreamingJsonParser;

public class ScanDataStreamingJsonParser extends AbstractStreamingJsonParser<ScanDataStreamingJsonParser> {
	/**
	 * Parse JSON contents retrieved from the given {@link ScanData} using
	 * the previously configured handlers.
	 * @param scanData {@link ScanData} instance
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */ 
	public final void parse(ScanData scanData, ScanEntry scanEntry) throws IOException {
		parse(scanData, scanEntry, null);
	}
	
	/**
	 * Parse JSON contents retrieved from the given {@link ScanData} object
	 * for the given input region, using the previously configured handlers.
	 * @param scanData {@link ScanData} instance
	 * @param inputRegion {@link Region} to be parsed
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final void parse(ScanData scanData, ScanEntry scanEntry, Region inputRegion) throws IOException {
		try ( final InputStream inputStream = scanData.getInputStream(scanEntry) ) {
			parse(inputStream, inputRegion);
		}
	}
}
