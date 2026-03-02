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
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fortify.ssc.parser.cyclonedx.domain.Bom.BomTool;

/**
 * Custom deserializer to handle both CycloneDX 1.4 and 1.5+ formats for metadata.tools field.
 * 
 * CycloneDX 1.4 and earlier: tools is an array of tool objects
 * CycloneDX 1.5+: tools is an object containing components and services arrays
 */
public final class BomToolsDeserializer extends JsonDeserializer<BomTool[]> {

    @Override
    public BomTool[] deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonToken token = p.currentToken();
        
        if (token == JsonToken.START_ARRAY) {
            // CycloneDX 1.4 format: array of tools
            return p.readValueAs(BomTool[].class);
        } else if (token == JsonToken.START_OBJECT) {
            // CycloneDX 1.5+ format: object with components/services
            List<BomTool> tools = new ArrayList<>();
            
            while (p.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = p.getCurrentName();
                p.nextToken();
                
                if ("components".equals(fieldName) || "services".equals(fieldName)) {
                    if (p.currentToken() == JsonToken.START_ARRAY) {
                        while (p.nextToken() != JsonToken.END_ARRAY) {
                            BomTool tool = p.readValueAs(BomTool.class);
                            if (tool != null) {
                                tools.add(tool);
                            }
                        }
                    }
                } else {
                    // Skip unknown fields
                    p.skipChildren();
                }
            }
            
            return tools.toArray(new BomTool[0]);
        }
        
        // If neither array nor object, return empty array
        return new BomTool[0];
    }
}