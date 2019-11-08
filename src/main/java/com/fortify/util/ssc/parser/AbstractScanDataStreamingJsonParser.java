/*******************************************************************************
 * (c) Copyright 2017 EntIT Software LLC
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
package com.fortify.util.ssc.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fortify.plugin.api.ScanData;
import com.fortify.util.io.Region;
import com.fortify.util.json.AbstractStreamingJsonParser;

public abstract class AbstractScanDataStreamingJsonParser<T extends AbstractScanDataStreamingJsonParser<T>> extends AbstractStreamingJsonParser<T> {
	private final List<String> supportedExtensions = new ArrayList<>();
	
	public AbstractScanDataStreamingJsonParser(String supportedExtension, String... supportedExtensions) {
		this.supportedExtensions.add(supportedExtension);
	    this.supportedExtensions.addAll(Arrays.asList(supportedExtensions));
	}
	
	/**
	 * Parse JSON contents retrieved from the given {@link ScanData} using
	 * the previously configured handlers.
	 */ 
	public final void parse(ScanData scanData) throws IOException {
		parse(scanData, null);
	}
	
	/**
	 * Parse JSON contents retrieved from the given {@link ScanData} object
	 * for the given input region, using the previously configured handlers.
	 */
	public final void parse(ScanData scanData, Region inputRegion) throws IOException {
		try ( final InputStream inputStream = scanData.getInputStream(fileName -> hasSupportedExtension(fileName)) ) {
			parse(inputStream, inputRegion);
		}
	}
	
	private final boolean hasSupportedExtension(String fileName) {
		for (String ext : supportedExtensions) {
			if (fileName.endsWith(ext)) {
				return true;
			}
		}
		return false;
	}


}
