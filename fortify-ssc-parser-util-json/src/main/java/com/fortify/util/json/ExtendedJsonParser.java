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
package com.fortify.util.json;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.util.JsonParserDelegate;
import com.fortify.util.io.Region;

public class ExtendedJsonParser extends JsonParserDelegate {
	public static final Set<JsonToken> SET_START_OBJECT = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(JsonToken.START_OBJECT)));
	public static final Set<JsonToken> SET_START_ARRAY = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(JsonToken.START_ARRAY)));
	public static final Set<JsonToken> SET_START_OBJECT_OR_ARRAY = Collections.unmodifiableSet(new HashSet<>(Arrays.asList(JsonToken.START_OBJECT, JsonToken.START_ARRAY)));
	
	public ExtendedJsonParser(JsonParser delegate) {
		super(delegate);
	}
	
	/**
	 * Assert that the current parser is currently pointing at the start tag of a 
	 * JSON array, throwing a {@link IOException} otherwise.
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final void assertStartArray() throws IOException {
		assertToken(SET_START_ARRAY);
	}

	/**
	 * Assert that the current parser is currently pointing at the start tag of a 
	 * JSON object, throwing a {@link IOException} otherwise.
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final void assertStartObject() throws IOException {
		assertToken(SET_START_OBJECT);
	}
	
	/**
	 * Assert that the current parser is currently pointing at the start tag of a 
	 * JSON object or array, throwing a {@link IOException} otherwise.
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final void assertStartObjectOrArray() throws IOException {
		assertToken(SET_START_OBJECT_OR_ARRAY);
	}
	
	/**
	 * Assert that the current parser is currently pointing at one of the 
	 * given {@link JsonToken}s.
	 * @param expectedTokens The tokens that are expected at the current parse position
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final void assertToken(Set<JsonToken> expectedTokens) throws IOException {
		if (!expectedTokens.contains(currentToken())) {
			throw new IOException(String.format("Expected one of %s at %s", expectedTokens, getTokenLocation()));
		}
	}
	
	/**
	 * Assuming the current parser instance is currently pointing at a JSON array,
	 * this method will return the number of array entries. The pointer is moved to
	 * the end of the array.
	 * 
	 * @return Number of array entries
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final int countArrayEntries() throws IOException {
		assertStartArray();
		int result = 0;
		while (nextToken()!=JsonToken.END_ARRAY) {
			result++;
			skipChildren();
		}
		return result;
	}
	
	/**
	 * Assuming the current parser instance is currently pointing at a JSON object,
	 * this method will return the number of object fields. The pointer is moved to
	 * the end of the object.
	 * 
	 * @return Number of object entries
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final int countObjectEntries() throws IOException {
		assertStartObject();
		int result = 0;
		while (nextToken()!=JsonToken.END_OBJECT) {
			result++;
			skipChildren();
		}
		return result;
	}
	
	/**
	 * Get the region (start and end position) of the current JSON object or array.
	 * The pointer is moved to the end of the object or array.
	 * 
	 * @return {@link Region} instance describing start and end position of current JSON object or array
	 * @throws IOException if there is any error while accessing or parsing the input data
	 */
	public final Region getObjectOrArrayRegion() throws IOException {
		assertStartObjectOrArray();
		// TODO Do we need to take into account file encoding to determine number of bytes
		//      for the '[' character?
		long start = getCurrentLocation().getByteOffset()-"[".getBytes().length; 
		skipChildren();
		long end = getCurrentLocation().getByteOffset();
		return new Region(start, end);
	}
}
