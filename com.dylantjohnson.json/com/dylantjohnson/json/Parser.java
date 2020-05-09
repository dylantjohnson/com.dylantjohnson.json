package com.dylantjohnson.json;

import java.io.*;
import java.math.*;
import java.util.*;

public class Parser {
    public static JsonValue parse(InputStream jsonStream) throws IOException, ParseException {
        Utils.assertNotNull(jsonStream);
        var stream = jsonStream;
        if (!stream.markSupported()) {
            stream = new BufferedInputStream(jsonStream);
        }
        var next = Utils.peekIgnoreWhitespace(stream);
        if (next == '"') {
            return parseString(stream);
        }
        if (Utils.isDigit(next)) {
            return parseNumber(stream);
        }
        if (next == '{') {
            return parseObject(stream);
        }
        if (next == '[') {
            return parseList(stream);
        }
        throw new ParseException(String.format("Unexpected first character %c", next));
    }

    private static JsonValue parseString(InputStream stream) throws IOException, ParseException {
        var next = stream.read();
        if (next != '"') {
            throw new ParseException(String.format("Expected \" but found %c", next));
        }
        var result = new StringBuilder();
        next = stream.read();
        for (var previous = -1; next != '"' || previous == '\\'; previous = next, next = stream.read()) {
            if (next < 0) {
                throw new ParseException("Encountered end of stream in the middle of a string");
            }
            result.append((char) next);
        }
        return new JsonValue(result.toString());
    }

    private static JsonValue parseNumber(InputStream stream) throws IOException, ParseException {
        var next = stream.read();
        if (!Utils.isDigit(next)) {
            throw new ParseException(String.format("Expected digit but found %c", next));
        }
        var result = new StringBuilder();
        result.append((char) next);
        while (Utils.isDigit(Utils.peek(stream))) {
            result.append((char) stream.read());
        }
        return new JsonValue(new BigDecimal(result.toString()));
    }

    private static JsonValue parseObject(InputStream stream) throws IOException, ParseException {
        var next = stream.read();
        if (next != '{') {
            throw new ParseException(String.format("Expected { but found %c", next));
        }
        var result = new HashMap<String, JsonValue>();
        while (Utils.peekIgnoreWhitespace(stream) != '}') {
            var key = parseString(stream).getStringValue().get();
            next = Utils.peekIgnoreWhitespace(stream);
            if (next != ':') {
                throw new ParseException(String.format("Expected : but found %c", next));
            }
            stream.read();
            var value = parse(stream);
            result.put(key, value);
            next = Utils.peekIgnoreWhitespace(stream);
            if (next == ',') {
                stream.read();
            } else if (next != '}') {
                throw new ParseException(String.format("Expected , or } but found %c", next));
            }
        }
        stream.read();
        return new JsonValue(new JsonObject(result));
    }

    private static JsonValue parseList(InputStream stream) throws IOException, ParseException {
        var next = stream.read();
        if (next != '[') {
            throw new ParseException(String.format("Expected [ but found %c", next));
        }
        var result = new ArrayList<JsonValue>(0);
        while (Utils.peekIgnoreWhitespace(stream) != ']') {
            result.add(parse(stream));
            next = Utils.peekIgnoreWhitespace(stream);
            if (next == ',') {
                stream.read();
            } else if (next != ']') {
                throw new ParseException(String.format("Expected , or ] but found %c", next));
            }
        }
        stream.read();
        return new JsonValue(result);
    }
}
