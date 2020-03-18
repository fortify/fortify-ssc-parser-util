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
package com.fortify.util.ssc.parser;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;

/**
 * This constants class provides some constants that can be used
 * throughout the parser implementation.
 * 
 * @author Ruud Senden
 *
 */
public class EngineTypeHelper {
	private static final String ENGINE_TYPE = _getEngineType();
	
	private EngineTypeHelper() {}
	
	public static final String getEngineType() {
		return ENGINE_TYPE;
	}
	
	/**
	 * Get the engine type from plugin.xml
	 * 
	 * @return
	 */
	private static final String _getEngineType() {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
        try (InputStream inputStream = EngineTypeHelper.class.getClassLoader().getResourceAsStream("plugin.xml")) {
    		domFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder builder = domFactory.newDocumentBuilder();
			Document dDoc = builder.parse(inputStream);

            XPath xPath = XPathFactory.newInstance().newXPath();
            return (String) xPath.evaluate("/plugin/issue-parser/engine-type/text()", dDoc, XPathConstants.STRING);
        } catch (Exception e) {
            throw new RuntimeException("Error reading engine type from plugin.xml", e);
        }
	}
}
