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
package com.fortify.ssc.parser.cyclonedx;

/**
 * This enum defines the custom vulnerability attributes that are generated by the
 * CycloneDX parser plugin. 
 * 
 * @author Ruud Senden
 *
 */
public enum CustomVulnAttribute implements com.fortify.plugin.spi.VulnerabilityAttribute {
	externalId(AttrType.STRING),
	externalUrl(AttrType.STRING),
	cwes(AttrType.STRING),
	invoked(AttrType.STRING),
	controllable(AttrType.STRING),
	evidenceUrl(AttrType.STRING),
	detail(AttrType.LONG_STRING),
	recommendation(AttrType.LONG_STRING),
	toolName(AttrType.STRING),
	componentPackageType(AttrType.STRING),
	componentNamespace(AttrType.STRING),
	componentName(AttrType.STRING),
	componentVersion(AttrType.STRING),
	componentScope(AttrType.STRING),
	componentDescription(AttrType.LONG_STRING),
	componentPurl(AttrType.STRING),
	componentLicenses(AttrType.STRING),
	ratingScore(AttrType.STRING),
	ratingMethod(AttrType.STRING),
	ratingVector(AttrType.LONG_STRING),
    ;

    private final AttrType attributeType;

    CustomVulnAttribute(final AttrType attributeType) {
        this.attributeType = attributeType;
    }

    @Override
    public String attributeName() {
        return name();
    }

    @Override
    public AttrType attributeType() {
        return attributeType;
    }
}