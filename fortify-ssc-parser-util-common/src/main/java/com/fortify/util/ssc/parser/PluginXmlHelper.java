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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import lombok.Getter;

/**
 * This constants class provides some constants that can be used
 * throughout the parser implementation.
 * 
 * @author Ruud Senden
 *
 */
public class PluginXmlHelper {
	private static final PluginXmlDescriptor pluginXmlDescriptor = new PluginXmlDescriptor();
	
	private PluginXmlHelper() {}
	
	public static final PluginXmlDescriptor getPluginXmlDescriptor() {
		return pluginXmlDescriptor;
	}
	
	@Getter
	public static final class PluginXmlDescriptor {
	    private static final Logger logger = LoggerFactory.getLogger(PluginXmlDescriptor.class);
	    private final Pattern versionPattern = Pattern.compile("(\\d+)\\.(\\d+)(\\..*)?");
	    private final String engineType;
	    private final String apiVersion;
	    private final int apiMajorVersion;
	    private final int apiMinorVersion;
	    
	    private PluginXmlDescriptor() {
	        DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
	        try (InputStream inputStream = PluginXmlHelper.class.getClassLoader().getResourceAsStream("plugin.xml")) {
	            domFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
	            DocumentBuilder builder = domFactory.newDocumentBuilder();
	            Document dDoc = builder.parse(inputStream);

	            XPath xPath = XPathFactory.newInstance().newXPath();
	            this.engineType = (String) xPath.evaluate("/plugin/issue-parser/engine-type/text()", dDoc, XPathConstants.STRING);
	            this.apiVersion = (String) xPath.evaluate("/plugin/@api-version", dDoc, XPathConstants.STRING);
	            Matcher m = versionPattern.matcher(apiVersion);
	            if ( !m.matches() ) { throw new IllegalStateException("Unable to parse api-version from plugin.xml"); }
	            this.apiMajorVersion = Integer.parseInt(m.group(1));
	            this.apiMinorVersion = Integer.parseInt(m.group(2));
	            logger.info("Engine type: %s, API version: %s (major: %d, minor: %d)", engineType, apiVersion, apiMajorVersion, apiMinorVersion);
	        } catch (Exception e) {
	            throw new RuntimeException("Unable to parse plugin.xml", e);
	        }
	    }
	    
	    public final boolean isMinimumApiVersion(int major, int minor) {
	        return apiMajorVersion > major || (apiMajorVersion==major && apiMinorVersion>=minor);
	    }
	}
}
