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
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fortify.util.io.Region;
import com.fortify.util.io.RegionInputStream;

/**
 * This abstract class provides functionality for stream-based parsing of arbitrary JSON 
 * structures. 
 * TODO Add more information/examples how to use the various
 *      parse methods.
 * 
 * @author Ruud Senden
 *
 */
public abstract class AbstractStreamingJsonParser<T extends AbstractStreamingJsonParser<T>> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractStreamingJsonParser.class);
	private final Map<String, JsonHandler> pathToHandlerMap = new LinkedHashMap<>();
	private Set<JsonToken> expectedStartTokens = ExtendedJsonParser.SET_START_OBJECT;
	private ObjectMapper objectMapper = DefaultObjectMapperFactory.getDefaultObjectMapper();
	private JsonFactory jsonFactory = new MappingJsonFactory(objectMapper)
			.disable(JsonParser.Feature.AUTO_CLOSE_SOURCE);
	@SuppressWarnings("unchecked")
	private T _this = (T)this;

	public final T handler(String path, JsonHandler handler) {
		addHandlerAndParentHandlers(path, handler);
		return _this;
	}
	
	public final <V> T handler(String path, Class<V> clazz, Consumer<V> handler) {
		return handler(path, jp->handler.accept(jp.readValueAs(clazz)));
	}
	
	public final <V> T handler(String path, Class<V> clazz, BiConsumer<String, V> handler) {
		return handler(path, jp->handler.accept(jp.getCurrentName(), jp.readValueAs(clazz)));
	}
	
	public final T expectedStartTokens(JsonToken... jsonTokens) {
		expectedStartTokens = new HashSet<>(Arrays.asList(jsonTokens));
		return _this;
	}
	
	public final T objectMapper(ObjectMapper objectMapper) {
		this.objectMapper = objectMapper;
		return _this;
	}
	
	public final T jsonFactory(JsonFactory jsonFactory) {
		this.jsonFactory = jsonFactory;
		return _this;
	}
	
	/**
	 * This method adds the given handler to {@link #pathToHandlerMap},
	 * then calls {@link #addParentHandlers(String)} to add intermediate
	 * handlers for reaching the given handler.
	 * 
	 * @param pathToHandlerMap
	 */
	private final void addHandlerAndParentHandlers(String path, JsonHandler handler) {
		pathToHandlerMap.put(path, handler);
		addParentHandlers(path);
	}

	private final void addParentHandlers(String path) {
		LOG.debug("Adding parent handlers for "+path);
		String[] pathElts = path.split("/");
		String currentPath = "";
		for ( String pathElt : pathElts ) {
			currentPath = getPath(currentPath, pathElt);
			addParentHandler(currentPath);
		}
	}
	
	/**
	 * This method adds an intermediate handler for traversing into the given path.
	 * @param pathToHandlerMap
	 * @param path
	 */
	private final void addParentHandler(final String path) {
		if ( !pathToHandlerMap.containsKey(path) ) {
			LOG.debug("Adding parent handler for "+path);
			pathToHandlerMap.put(path, jsonParser->parseObjectOrArrayChildren(jsonParser, path));
		}
	}

	/**
	 * Parse JSON contents retrieved from the given {@link InputStream} using
	 * the previously configured handlers.
	 */ 
	public final void parse(InputStream inputStream) throws IOException {
		parse(inputStream, null);
	}
	
	/**
	 * Parse JSON contents retrieved from the given {@link InputStream} object
	 * for the given input region, using the previously configured handlers.
	 */
	public final void parse(InputStream inputStream, Region inputRegion) throws IOException {
		try ( final InputStream content = 
					new RegionInputStream(inputStream, inputRegion, false);
				final ExtendedJsonParser jsonParser = 
					new ExtendedJsonParser(jsonFactory.createParser(content))
			) {
			jsonParser.nextToken();
			jsonParser.assertToken(expectedStartTokens);
			parse(jsonParser, "/");
		}
	}

	/**
	 * This method checks whether a {@link JsonHandler} has been registered for the 
	 * current JSON element. If a {@link JsonHandler} is found, this method will simply
	 * invoke the {@link JsonHandler} to parse the contents of the current JSON element.
	 * If no {@link JsonHandler} is found, this method will skip all children of the
	 * current JSON element.
	 * 
	 * This method simply returns after handling the current JSON elements; recursive
	 * parsing is handled by registered {@link JsonHandler} instances.
	 * 
	 * @param jsonParser
	 * @param parentPath
	 * @throws IOException
	 */
	private final void parse(final ExtendedJsonParser jsonParser, String parentPath) throws IOException {
		JsonToken currentToken = jsonParser.getCurrentToken();
		if ( currentToken != null && (currentToken==JsonToken.START_ARRAY || currentToken==JsonToken.START_OBJECT || currentToken.isScalarValue())) {
			String currentPath = getPath(parentPath, jsonParser.getCurrentName());
			LOG.trace("Processing "+currentPath);
			JsonHandler handler = pathToHandlerMap.computeIfAbsent(currentPath, k->pathToHandlerMap.get(getPath(parentPath, "*")));
			if ( handler != null ) {
				LOG.debug("Handling "+currentPath);
				handler.handle(jsonParser);
			} else {
				jsonParser.skipChildren();
			}
		}
	}
	
	/**
	 * Append the given currentName to the given parentPath,
	 * correctly handling the separator.
	 * 
	 * @param parentPath
	 * @param currentName
	 * @return
	 */
	private final String getPath(String parentPath, String currentName) {
		String result = parentPath;
		if ( currentName!=null ) {
			result+=result.endsWith("/")?"":"/";
			result+=currentName;
		}
		if ( "".equals(result) ) { result="/"; }
		return result;
	}

	/**
	 * Parse the children of the current JSON object or JSON array.
	 * 
	 * @param jsonParser
	 * @param currentPath
	 * @throws IOException
	 */
	private final void parseObjectOrArrayChildren(ExtendedJsonParser jsonParser, String currentPath) throws IOException {
		JsonToken currentToken = jsonParser.getCurrentToken();
		if ( currentToken==JsonToken.START_OBJECT ) {
			parseObjectProperties(jsonParser, currentPath);
		} else if ( currentToken==JsonToken.START_ARRAY ) {
			parseArrayEntries(jsonParser, currentPath);
		}
	}
	
	/**
	 * Parse the individual object properties of the current JSON object.
	 * 
	 * @param jsonParser
	 * @param currentPath
	 * @throws IOException
	 */
	public final void parseObjectProperties(ExtendedJsonParser jsonParser, String currentPath) throws IOException {
		parseChildren(jsonParser, currentPath, JsonToken.END_OBJECT);
	}
	
	/**
	 * Parse the individual array entries of the current JSON array.
	 * 
	 * @param jsonParser
	 * @param currentPath
	 * @throws IOException
	 */
	public final void parseArrayEntries(ExtendedJsonParser jsonParser, String currentPath) throws IOException {
		parseChildren(jsonParser, getPath(currentPath, "*"), JsonToken.END_ARRAY);
	}
	
	/**
	 * Parse the children of the current JSON element, up to the given endToken
	 * 
	 * @param jsonParser
	 * @param currentPath
	 * @param endToken
	 * @throws IOException
	 */
	private final void parseChildren(ExtendedJsonParser jsonParser, String currentPath, JsonToken endToken) throws IOException {
		while (jsonParser.nextToken()!=endToken) {
			parse(jsonParser, currentPath);
		}
	}
}